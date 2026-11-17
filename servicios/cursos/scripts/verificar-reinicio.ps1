param(
    [string]$JarPath = (Join-Path $PSScriptRoot '../target/servicio-cursos-0.1.0-SNAPSHOT.jar')
)

$ErrorActionPreference = 'Stop'
foreach ($name in @('CURSOS_DB_URL', 'CURSOS_DB_USER', 'CURSOS_DB_PASSWORD')) {
    if (-not [Environment]::GetEnvironmentVariable($name)) { throw "Falta configurar $name en esta terminal." }
}
if (-not (Test-Path -LiteralPath $JarPath -PathType Leaf)) { throw 'No se encontró el JAR. Ejecuta mvn clean verify primero.' }
$javaCommand = Get-Command java -ErrorAction Stop
$jar = (Resolve-Path -LiteralPath $JarPath).Path
$listener = [System.Net.Sockets.TcpListener]::new([System.Net.IPAddress]::Loopback, 0)
$listener.Start()
$port = $listener.LocalEndpoint.Port
$listener.Stop()
$base = "http://127.0.0.1:$port"
$process = $null

function Start-Cursos {
    $startArguments = @{
        FilePath = $javaCommand.Source
        ArgumentList = @('-jar', "`"$jar`"", "--server.port=$port")
        PassThru = $true
    }
    if ($env:OS -eq 'Windows_NT') { $startArguments.WindowStyle = 'Hidden' }
    $p = Start-Process @startArguments
    try {
        for ($i = 0; $i -lt 60; $i++) {
            if ($p.HasExited) { throw "El servicio terminó antes de iniciar (código $($p.ExitCode))." }
            try {
                $health = Invoke-RestMethod -Uri "$base/actuator/health" -TimeoutSec 2
                if ($health.status -eq 'UP') { return $p }
            } catch { Start-Sleep -Seconds 1 }
        }
        throw 'El servicio no estuvo saludable en 60 segundos.'
    } catch {
        if (-not $p.HasExited) { Stop-Process -Id $p.Id -Force }
        throw
    }
}

try {
    $process = Start-Cursos
    $codigo = 'REINICIO-' + [guid]::NewGuid().ToString('N')
    $body = @{ codigo = $codigo; nombre = 'Curso de prueba'; descripcion = 'Prueba de persistencia'; duracionHoras = 20; capacidad = 15 } | ConvertTo-Json
    $created = Invoke-RestMethod -Method Post -Uri "$base/api/v1/cursos" -ContentType 'application/json' -Body $body
    if (-not $created.id -or $created.codigo -ne $codigo) { throw 'La respuesta de creación no coincide con el curso enviado.' }
    Stop-Process -Id $process.Id -Force
    $process.WaitForExit()
    $process = $null
    $process = Start-Cursos
    $consultado = Invoke-RestMethod -Uri "$base/api/v1/cursos/$($created.id)"
    if ($consultado.id -ne $created.id -or $consultado.codigo -ne $codigo -or $consultado.estado -ne 'ACTIVO') {
        throw 'El curso consultado después del reinicio no coincide con el registrado.'
    }
    Write-Output "OK: curso $($created.id) consultado después de reiniciar el servicio."
} finally {
    if ($process -and -not $process.HasExited) {
        Stop-Process -Id $process.Id -Force
        $process.WaitForExit(10000) | Out-Null
    }
}
