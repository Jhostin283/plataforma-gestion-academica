import { cleanup, render, screen, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { afterEach, expect, test, vi } from 'vitest';
import App from './App';

afterEach(() => { cleanup(); vi.unstubAllGlobals(); vi.restoreAllMocks(); });

test('muestra indicadores derivados de las respuestas', async () => {
  vi.stubGlobal('fetch', vi.fn(async url => ({ ok: true, json: async () => url.includes('matriculas')
    ? { items: [], total: 4 } : { items: [{ estado: 'ACTIVO' }, { estado: 'INACTIVO' }], total: 2 } })));
  render(<App />);
  expect(await screen.findByText('4')).toBeInTheDocument();
  expect(screen.getAllByText('1')).toHaveLength(2);
});

test('muestra un error independiente y permite reintentar', async () => {
  vi.stubGlobal('fetch', vi.fn(async url => {
    if (url.includes('cursos')) throw new Error('sin conexión');
    return { ok: true, json: async () => ({ items: [], total: 0 }) };
  }));
  render(<App />);
  await waitFor(() => expect(screen.getByText('No se pudo cargar este indicador.')).toBeInTheDocument());
  expect(screen.getByRole('button', { name: 'Reintentar' })).toBeInTheDocument();
});
