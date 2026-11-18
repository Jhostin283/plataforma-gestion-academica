import { useEffect, useState } from 'react';
import { solicitar } from './api';

const vacio = { documento: '', nombres: '', apellidos: '', correo: '' };
const etiquetas = { documento: 'Documento', nombres: 'Nombres', apellidos: 'Apellidos', correo: 'Correo' };

export default function Estudiantes() {
  const [busqueda, setBusqueda] = useState('');
  const [page, setPage] = useState(0);
  const [version, setVersion] = useState(0);
  const [lista, setLista] = useState({ tipo: 'cargando' });
  const [form, setForm] = useState(vacio);
  const [editando, setEditando] = useState(null);
  const [ocupado, setOcupado] = useState(false);
  const [error, setError] = useState('');
  const [detalles, setDetalles] = useState({});
  const [mensaje, setMensaje] = useState('');

  useEffect(() => {
    const controller = new AbortController();
    setLista({ tipo: 'cargando' });
    const params = new URLSearchParams({ buscar: busqueda, page: String(page), size: '20' });
    solicitar(`estudiantes?${params}`, { signal: controller.signal }).then(data => setLista({ tipo: 'listo', ...data })).catch(e => {
      if (e.name !== 'AbortError') setLista({ tipo: 'error' });
    });
    return () => controller.abort();
  }, [busqueda, page, version]);

  function limpiar() { setEditando(null); setForm(vacio); setError(''); setDetalles({}); }
  async function guardar(event) {
    event.preventDefault(); setOcupado(true); setError(''); setDetalles({}); setMensaje('');
    try {
      await solicitar(editando ? `estudiantes/${editando}` : 'estudiantes', { method: editando ? 'PUT' : 'POST', body: form });
      setMensaje(editando ? 'Estudiante actualizado.' : 'Estudiante registrado.'); limpiar(); setVersion(x => x + 1);
    } catch (e) { setError(e.message); setDetalles(e.detalles && typeof e.detalles === 'object' ? e.detalles : {}); }
    finally { setOcupado(false); }
  }
  async function cambiarEstado(estudiante) {
    setOcupado(true); setError(''); setMensaje('');
    try {
      await solicitar(`estudiantes/${estudiante.id}/estado`, { method: 'PATCH', body: { estado: estudiante.estado === 'ACTIVO' ? 'INACTIVO' : 'ACTIVO' } });
      setMensaje('Estado actualizado.'); setVersion(x => x + 1);
    } catch (e) { setError(e.message); } finally { setOcupado(false); }
  }
  return <section className="management" aria-labelledby="estudiantes-titulo">
    <p className="eyebrow">GESTIÓN ACADÉMICA</p><h1 id="estudiantes-titulo">Estudiantes<span>.</span></h1>
    <p>Consulta y actualiza los datos de la comunidad académica.</p>
    <div className="management-grid"><div className="surface"><h2>Estudiantes registrados</h2>
      <label htmlFor="buscar-estudiante">Buscar por nombre o documento</label><input id="buscar-estudiante" value={busqueda} onChange={e => { setBusqueda(e.target.value); setPage(0); }} />
      <div aria-live="polite">{lista.tipo === 'cargando' && <p>Cargando estudiantes…</p>}{lista.tipo === 'error' && <><p className="error">No se pudieron cargar los estudiantes.</p><button type="button" onClick={() => setVersion(x => x + 1)}>Reintentar</button></>}
      {lista.tipo === 'listo' && (lista.items.length === 0 ? <p>No hay estudiantes para mostrar.</p> : <div className="table-scroll"><table><thead><tr><th>Documento</th><th>Nombre</th><th>Estado</th><th>Acciones</th></tr></thead><tbody>{lista.items.map(estudiante => <tr key={estudiante.id}><td>{estudiante.documento}</td><td>{estudiante.nombres} {estudiante.apellidos}</td><td>{estudiante.estado === 'ACTIVO' ? 'Activo' : 'Inactivo'}</td><td><button type="button" disabled={ocupado} onClick={() => { setEditando(estudiante.id); setForm({ documento: estudiante.documento, nombres: estudiante.nombres, apellidos: estudiante.apellidos, correo: estudiante.correo }); setError(''); setDetalles({}); }}>Editar</button>{' '}<button type="button" disabled={ocupado} onClick={() => cambiarEstado(estudiante)}>{estudiante.estado === 'ACTIVO' ? 'Desactivar' : 'Activar'}</button></td></tr>)}</tbody></table></div>)}
      {lista.tipo === 'listo' && <div className="pagination"><button type="button" disabled={page === 0} onClick={() => setPage(x => x - 1)}>Anterior</button><span>Página {page + 1}</span><button type="button" disabled={(page + 1) * 20 >= lista.total} onClick={() => setPage(x => x + 1)}>Siguiente</button></div>}</div>
    </div><form className="surface" onSubmit={guardar}><h2>{editando ? 'Editar estudiante' : 'Registrar estudiante'}</h2>
      {Object.keys(etiquetas).map(campo => {
        const detalle = detalles[`body.${campo}`] || detalles[campo];
        return <div className="field" key={campo}><label htmlFor={`estudiante-${campo}`}>{etiquetas[campo]}</label><input id={`estudiante-${campo}`} name={campo} value={form[campo]} onChange={e => setForm({ ...form, [campo]: e.target.value })} required type={campo === 'correo' ? 'email' : 'text'} aria-invalid={typeof detalle === 'string' || undefined} aria-describedby={typeof detalle === 'string' ? `error-est-${campo}` : undefined} />{typeof detalle === 'string' && <p className="error" id={`error-est-${campo}`}>{detalle}</p>}</div>;
      })}
      <div aria-live="polite">{error && <p className="error">{error}</p>}{mensaje && <p className="success">{mensaje}</p>}</div><button className="primary" disabled={ocupado}>{ocupado ? 'Guardando…' : editando ? 'Guardar cambios' : 'Registrar estudiante'}</button>{editando && <button type="button" disabled={ocupado} onClick={limpiar}>Cancelar edición</button>}
    </form></div>
  </section>;
}
