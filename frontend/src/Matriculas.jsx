import { useEffect, useState } from 'react';
import { listarTodos, solicitar, solicitarConEstado } from './api';

export default function Matriculas() {
  const [opciones, setOpciones] = useState({ tipo: 'cargando' });
  const [estudianteId, setEstudianteId] = useState('');
  const [cursoId, setCursoId] = useState('');
  const [detalleCurso, setDetalleCurso] = useState(null);
  const [historial, setHistorial] = useState({ tipo: 'vacio' });
  const [page, setPage] = useState(0);
  const [version, setVersion] = useState(0);
  const [ocupado, setOcupado] = useState(false);
  const [mensaje, setMensaje] = useState('');
  const [error, setError] = useState('');

  useEffect(() => {
    const controller = new AbortController();
    setOpciones({ tipo: 'cargando' });
    Promise.all([listarTodos('estudiantes', controller.signal), listarTodos('cursos', controller.signal)])
      .then(([estudiantes, cursos]) => setOpciones({ tipo: 'listo', estudiantes: estudiantes.filter(x => x.estado === 'ACTIVO'), cursos: cursos.filter(x => x.estado === 'ACTIVO') }))
      .catch(e => { if (e.name !== 'AbortError') setOpciones({ tipo: 'error' }); });
    return () => controller.abort();
  }, [version]);
  useEffect(() => {
    if (!cursoId) { setDetalleCurso(null); return undefined; }
    const controller = new AbortController();
    setDetalleCurso({ tipo: 'cargando' });
    solicitar(`cursos/${cursoId}`, { signal: controller.signal }).then(data => setDetalleCurso({ tipo: 'listo', data })).catch(e => {
      if (e.name !== 'AbortError') setDetalleCurso({ tipo: 'error' });
    });
    return () => controller.abort();
  }, [cursoId, version]);
  useEffect(() => {
    if (!estudianteId) { setHistorial({ tipo: 'vacio' }); return undefined; }
    const controller = new AbortController();
    setHistorial({ tipo: 'cargando' });
    const params = new URLSearchParams({ estudianteId, page: String(page), size: '20' });
    solicitar(`matriculas?${params}`, { signal: controller.signal }).then(data => setHistorial({ tipo: 'listo', ...data })).catch(e => {
      if (e.name !== 'AbortError') setHistorial({ tipo: 'error' });
    });
    return () => controller.abort();
  }, [estudianteId, page, version]);

  async function matricular(event) {
    event.preventDefault(); setOcupado(true); setError(''); setMensaje('');
    const storageKey = `matricula:${estudianteId}:${cursoId}`;
    let clave = sessionStorage.getItem(storageKey);
    if (!clave) { clave = crypto.randomUUID(); sessionStorage.setItem(storageKey, clave); }
    try {
      const { data, status } = await solicitarConEstado('matriculas', { method: 'POST', body: { estudianteId, cursoId }, headers: { 'Idempotency-Key': clave } });
      if (status === 202 || data.estado === 'PENDIENTE') setMensaje('La matrícula está pendiente. Consulta el historial o reintenta con la misma solicitud.');
      else if (status === 201 && data.estado === 'ACTIVA') { setMensaje('Matrícula confirmada.'); sessionStorage.removeItem(storageKey); }
      else setMensaje('La operación aún no está confirmada. Consulta el historial.');
      setVersion(x => x + 1);
    } catch (e) {
      setError(e.status ? e.message : 'No se conoce el resultado. Reintenta la misma solicitud para consultarlo.');
      if ([400, 404, 409, 422].includes(e.status) || e.codigo === 'MATRICULA_COMPENSADA') sessionStorage.removeItem(storageKey);
    } finally { setOcupado(false); }
  }
  async function cancelar(id) {
    setOcupado(true); setError(''); setMensaje('');
    try {
      const { data, status } = await solicitarConEstado(`matriculas/${id}/cancelacion`, { method: 'POST' });
      setMensaje(status === 202 || data.estado === 'CANCELACION_PENDIENTE' ? 'La cancelación está pendiente.' : 'Matrícula cancelada.');
      setVersion(x => x + 1);
    } catch (e) { setError(e.message); } finally { setOcupado(false); }
  }
  return <section className="management" aria-labelledby="matriculas-titulo"><p className="eyebrow">GESTIÓN ACADÉMICA</p><h1 id="matriculas-titulo">Matrículas<span>.</span></h1><p>Selecciona un estudiante y un curso para gestionar su inscripción.</p>
    {opciones.tipo === 'cargando' && <p role="status">Cargando estudiantes y cursos…</p>}
    {opciones.tipo === 'error' && <div role="alert"><p className="error">No se pudieron cargar las opciones.</p><button onClick={() => setVersion(x => x + 1)}>Reintentar</button></div>}
    {opciones.tipo === 'listo' && <div className="management-grid"><form className="surface" onSubmit={matricular}><h2>Nueva matrícula</h2>
      <div className="field"><label htmlFor="mat-estudiante">Estudiante activo</label><select id="mat-estudiante" required value={estudianteId} onChange={e => { setEstudianteId(e.target.value); setPage(0); }}><option value="">Selecciona un estudiante</option>{opciones.estudiantes.map(x => <option key={x.id} value={x.id}>{x.nombres} {x.apellidos} · {x.documento}</option>)}</select>{opciones.estudiantes.length === 0 && <p>No hay estudiantes activos disponibles.</p>}</div>
      <div className="field"><label htmlFor="mat-curso">Curso activo</label><select id="mat-curso" required value={cursoId} onChange={e => setCursoId(e.target.value)}><option value="">Selecciona un curso</option>{opciones.cursos.map(x => <option key={x.id} value={x.id}>{x.codigo} · {x.nombre}</option>)}</select>{opciones.cursos.length === 0 && <p>No hay cursos activos disponibles.</p>}</div>
      <div aria-live="polite">{detalleCurso?.tipo === 'cargando' && <p>Consultando plazas…</p>}{detalleCurso?.tipo === 'error' && <p className="error">No se pudieron consultar las plazas.</p>}{detalleCurso?.tipo === 'listo' && <p>{detalleCurso.data.plazasDisponibles} de {detalleCurso.data.capacidad} plazas disponibles.</p>}</div>
      <button className="primary" disabled={ocupado || detalleCurso?.tipo !== 'listo' || detalleCurso.data.plazasDisponibles < 1}>{ocupado ? 'Procesando…' : 'Solicitar matrícula'}</button>
    </form><div className="surface"><h2>Historial</h2>{!estudianteId && <p>Selecciona un estudiante para consultar sus matrículas.</p>}
      <div aria-live="polite">{historial.tipo === 'cargando' && <p>Cargando historial…</p>}{historial.tipo === 'error' && <><p className="error">No se pudo cargar el historial.</p><button type="button" onClick={() => setVersion(x => x + 1)}>Reintentar</button></>}{historial.tipo === 'listo' && (historial.items.length === 0 ? <p>No hay matrículas en esta página.</p> : <div className="table-scroll"><table><thead><tr><th>Curso</th><th>Estado</th><th>Acción</th></tr></thead><tbody>{historial.items.map(x => <tr key={x.id}><td>{opciones.cursos.find(c => c.id === x.cursoId)?.nombre || x.cursoId}</td><td>{x.estado.replaceAll('_', ' ')}</td><td>{['ACTIVA', 'CANCELACION_PENDIENTE'].includes(x.estado) && <button disabled={ocupado} onClick={() => cancelar(x.id)}>{x.estado === 'ACTIVA' ? 'Cancelar' : 'Reintentar cancelación'}</button>}</td></tr>)}</tbody></table></div>)}</div>
      {historial.tipo === 'listo' && <div className="pagination"><button disabled={page === 0} onClick={() => setPage(x => x - 1)}>Anterior</button><span>Página {page + 1}</span><button disabled={(page + 1) * 20 >= historial.total} onClick={() => setPage(x => x + 1)}>Siguiente</button></div>}
    </div></div>}
    <div aria-live="polite">{mensaje && <p className="success">{mensaje}</p>}{error && <p className="error">{error}</p>}</div>
  </section>;
}
