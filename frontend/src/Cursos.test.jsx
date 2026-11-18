import { cleanup, fireEvent, render, screen, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { afterEach, expect, test, vi } from 'vitest';
import Cursos from './Cursos';

afterEach(() => { cleanup(); vi.unstubAllGlobals(); });

test('muestra cursos y envía un registro', async () => {
  const fetchMock = vi.fn(async (_url, options) => ({ ok: true, json: async () => options.method === 'POST'
    ? { id: 'nuevo' } : { items: [{ id: '1', codigo: 'C01', nombre: 'Redes', estado: 'ACTIVO' }], total: 1 } }));
  vi.stubGlobal('fetch', fetchMock);
  render(<Cursos />);
  expect(await screen.findByText('Redes')).toBeInTheDocument();
  fireEvent.change(screen.getByLabelText('Código'), { target: { value: 'C02' } });
  fireEvent.change(screen.getByLabelText('Nombre'), { target: { value: 'Nube' } });
  fireEvent.change(screen.getByLabelText('Descripción'), { target: { value: 'Fundamentos' } });
  fireEvent.change(screen.getByLabelText('Duración (horas)'), { target: { value: '20' } });
  fireEvent.change(screen.getByLabelText('Capacidad'), { target: { value: '30' } });
  fireEvent.click(screen.getByRole('button', { name: 'Registrar curso' }));
  await waitFor(() => expect(fetchMock).toHaveBeenCalledWith(expect.stringContaining('/cursos'), expect.objectContaining({ method: 'POST' })));
});
