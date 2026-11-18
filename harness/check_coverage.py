"""Rechaza cobertura de líneas menor o igual al 80 % en reportes XML."""
import sys
from decimal import Decimal
from pathlib import Path
from xml.etree import ElementTree


def main():
    if len(sys.argv) != 3 or sys.argv[1] not in ('jacoco', 'coverage'):
        raise SystemExit('Uso: python harness/check_coverage.py jacoco|coverage reporte.xml')
    tipo, ruta = sys.argv[1], Path(sys.argv[2])
    raiz = ElementTree.parse(ruta).getroot()
    if tipo == 'jacoco':
        contador = raiz.find("./counter[@type='LINE']")
        if contador is None:
            raise SystemExit('Reporte JaCoCo sin contador de líneas')
        cubiertas = int(contador.attrib['covered'])
        total = cubiertas + int(contador.attrib['missed'])
    else:
        cubiertas = int(raiz.attrib['lines-covered'])
        total = int(raiz.attrib['lines-valid'])
    if total <= 0:
        raise SystemExit('El reporte no contiene líneas medibles')
    porcentaje = Decimal(cubiertas) * 100 / Decimal(total)
    print(f'Líneas cubiertas: {cubiertas}/{total} ({porcentaje:.2f} %)')
    if cubiertas * 100 <= total * 80:
        raise SystemExit('La cobertura debe superar el 80 %')


if __name__ == '__main__':
    main()
