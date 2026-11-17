"""Run checks currently available; never provision or install dependencies."""
import subprocess
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
result = subprocess.run([sys.executable, str(ROOT / "harness/check.py")], cwd=ROOT, check=False)
if result.returncode:
    sys.exit(result.returncode)
print("Inicialización del harness completada. Todavía no hay aplicaciones ni pruebas de producto configuradas.")
