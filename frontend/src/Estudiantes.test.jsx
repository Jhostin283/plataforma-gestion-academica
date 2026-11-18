import { cleanup, fireEvent, render, screen, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { afterEach, expect, test, vi } from 'vitest';
import Estudiantes from './Estudiantes';

afterEach(() => { cleanup(); vi.unstubAllGlobals(); });

test('muestra estudiantes y envía un registro', async () => {
  const fetchMock = vi.fn(async (_url, options) => ({ ok: true, json: async () => options.method === 'POST'
    ? { id: 'nuevo' } : { items: [{ id: '1', documento: '123', nombres: 'Ana', apellidos: 'Pérez', correo: 'ana@example.com', estado: 'ACTIVO' }], total: 1 } }));
  vi.stubGlobal('fetch', fetchMock);
  render(<Estudiantes />);
  expect(await screen.findByText('Ana Pérez')).toBeInTheDocument();
  for (const [etiqueta, valor] of [['Documento', '456'], ['Nombres', 'Luis'], ['Apellidos', 'Ríos'], ['Correo', 'luis@example.com']]) {
    fireEvent.change(screen.getByLabelText(etiqueta), { target: { value: valor } });
  }
  fireEvent.click(screen.getByRole('button', { name: 'Registrar estudiante' }));
  await waitFor(() => expect(fetchMock).toHaveBeenCalledWith(expect.stringContaining('/estudiantes'), expect.objectContaining({ method: 'POST' })));
});
