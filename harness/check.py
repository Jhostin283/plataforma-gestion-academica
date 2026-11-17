"""Validate local harness metadata without third-party dependencies."""
import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
STATES = {"pending", "in_progress", "blocked", "passed"}


def main():
    errors = []
    try:
        data = json.loads((ROOT / "harness/features.json").read_text(encoding="utf-8"))
        features = data["features"]
        if not isinstance(features, list) or not features:
            raise ValueError("features debe ser una lista no vacía")
        matrix = (ROOT / "harness/TRACEABILITY.md").read_text(encoding="utf-8")
    except (OSError, ValueError, KeyError, TypeError) as exc:
        print(f"ERROR: {exc}")
        return 1
    ids = [item.get("id") for item in features if isinstance(item, dict)]
    if len(ids) != len(features) or len(ids) != len(set(ids)):
        errors.append("Las tareas deben ser objetos con IDs únicos")
    by_id = {item["id"]: item for item in features if isinstance(item, dict) and isinstance(item.get("id"), str)}
    for item in features:
        if not isinstance(item, dict):
            continue
        ident = item.get("id")
        if not isinstance(ident, str) or not re.fullmatch(r"[A-Z]+-(?:[A-Z0-9]+-)?\d+", ident):
            errors.append(f"ID inválido: {ident}")
            continue
        spec = item.get("spec")
        if not isinstance(spec, str) or not (ROOT / spec).is_file():
            errors.append(f"{ident}: spec inexistente")
        else:
            content = (ROOT / spec).read_text(encoding="utf-8")
            for section in ("## Objetivo", "## Precondiciones", "## Flujo principal", "## Casos alternativos", "## Criterios de aceptación", "## Trazabilidad"):
                if section not in content:
                    errors.append(f"{ident}: falta {section}")
            if ident.startswith(("CUR-", "EST-")) and not re.search(r"^## Endpoints? propuestos?$", content, re.M):
                errors.append(f"{ident}: faltan endpoints propuestos")
            if f"- Requisito: {ident}" not in content:
                errors.append(f"{ident}: identificador de trazabilidad incorrecto")
        state = item.get("status")
        if state not in STATES:
            errors.append(f"{ident}: estado inválido")
        evidence = item.get("evidence")
        if not isinstance(evidence, list) or any(not isinstance(e, str) or not e.strip() for e in evidence):
            errors.append(f"{ident}: evidence debe ser una lista de textos no vacíos")
        elif state == "passed" and not evidence:
            errors.append(f"{ident}: tarea aprobada sin evidencia")
        if state == "blocked" and not item.get("blocked_reason"):
            errors.append(f"{ident}: bloqueo sin motivo")
        deps = item.get("depends_on")
        if not isinstance(deps, list) or any(not isinstance(d, str) or d not in by_id or d == ident for d in deps):
            errors.append(f"{ident}: dependencias inválidas")
        elif state == "passed" and any(by_id[d].get("status") != "passed" for d in deps):
            errors.append(f"{ident}: dependencia no aprobada")
        rows = re.findall(r"^\|\s*" + re.escape(ident) + r"\s*\|.*\|\s*(pending|in_progress|blocked|passed)\s*\|\s*$", matrix, re.M)
        if rows != [state]:
            errors.append(f"{ident}: falta fila única en matriz o el estado no coincide")
    visiting, visited = set(), set()

    def visit(ident):
        if ident in visiting:
            errors.append(f"{ident}: dependencia circular")
            return
        if ident in visited:
            return
        visiting.add(ident)
        deps = by_id[ident].get("depends_on", [])
        if isinstance(deps, list):
            for dep in deps:
                if isinstance(dep, str) and dep in by_id:
                    visit(dep)
        visiting.remove(ident)
        visited.add(ident)

    for ident in by_id:
        visit(ident)
    if errors:
        for error in errors:
            print(f"ERROR: {error}")
        return 1
    print(f"Harness válido: {len(features)} tareas; specs y estados de matriz comprobados.")
    return 0


if __name__ == "__main__":
    sys.exit(main())
