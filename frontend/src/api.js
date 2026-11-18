const baseUrl = (import.meta.env.VITE_API_BASE_URL || '').replace(/\/$/, '');

async function pagina(path, params, signal) {
  const query = new URLSearchParams(params);
  const response = await fetch(`${baseUrl}/api/v1/${path}?${query}`, { signal });
  if (!response.ok) throw new Error('No se pudo consultar el servicio');
  const data = await response.json();
  if (!Array.isArray(data.items) || !Number.isInteger(data.total) || data.total < 0) throw new Error('Respuesta inválida');
  return data;
}

// Los listados de cursos y estudiantes no ofrecen filtro por estado.
export async function contarActivos(path, signal) {
  let page = 0;
  let count = 0;
  let leidos = 0;
  while (true) {
    const resultado = await pagina(path, { page: String(page), size: '100' }, signal);
    count += resultado.items.filter(item => item.estado === 'ACTIVO').length;
    leidos += resultado.items.length;
    if (leidos >= resultado.total || resultado.items.length === 0) {
      if (leidos < resultado.total) throw new Error('Listado incompleto');
      return count;
    }
    page += 1;
  }
}

export async function contarMatriculasActivas(signal) {
  return (await pagina('matriculas', { estado: 'ACTIVA', page: '0', size: '1' }, signal)).total;
}

export async function solicitar(path, { method = 'GET', body, signal } = {}) {
  const response = await fetch(`${baseUrl}/api/v1/${path}`, {
    method, signal, headers: body ? { 'Content-Type': 'application/json' } : {},
    body: body ? JSON.stringify(body) : undefined,
  });
  const data = await response.json().catch(() => ({}));
  if (!response.ok) {
    const error = new Error(data.mensaje || 'No se pudo completar la solicitud');
    error.detalles = data.detalles || {};
    throw error;
  }
  return data;
}
