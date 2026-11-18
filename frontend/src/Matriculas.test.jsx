import { cleanup, fireEvent, render, screen, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { afterEach, expect, test, vi } from 'vitest';
import Matriculas from './Matriculas';

afterEach(() => { cleanup(); vi.unstubAllGlobals(); sessionStorage.clear(); });

test('muestra el resultado pendiente sin confirmarlo', async () => {
  vi.stubGlobal('crypto', { randomUUID: () => '11111111-1111-4111-8111-111111111111' });
  vi.stubGlobal('fetch', vi.fn(async (url, options = {}) => {
    if (options.method === 'POST') return { ok: true, status: 202, json: async () => ({ estado: 'PENDIENTE' }) };
    if (url.includes('/cursos/c1')) return { ok: true, status: 200, json: async () => ({ capacidad: 10, plazasDisponibles: 3 }) };
    if (url.includes('/estudiantes')) return { ok: true, status: 200, json: async () => ({ items: [{ id: 'e1', nombres: 'Ana', apellidos: 'Ríos', documento: '123', estado: 'ACTIVO' }], total: 1 }) };
    if (url.includes('/cursos')) return { ok: true, status: 200, json: async () => ({ items: [{ id: 'c1', nombre: 'Redes', codigo: 'C1', estado: 'ACTIVO' }], total: 1 }) };
    return { ok: true, status: 200, json: async () => ({ items: [], total: 0 }) };
  }));
  render(<Matriculas />);
  fireEvent.change(await screen.findByLabelText('Estudiante activo'), { target: { value: 'e1' } });
  fireEvent.change(screen.getByLabelText('Curso activo'), { target: { value: 'c1' } });
  await waitFor(() => expect(screen.getByRole('button', { name: 'Solicitar matrícula' })).toBeEnabled());
  fireEvent.click(screen.getByRole('button', { name: 'Solicitar matrícula' }));
  expect(await screen.findByText(/La matrícula está pendiente/)).toBeInTheDocument();
  expect(screen.queryByText('Matrícula confirmada.')).not.toBeInTheDocument();
});
