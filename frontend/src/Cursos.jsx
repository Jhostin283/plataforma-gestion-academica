import { useEffect, useState } from 'react';
import { solicitar } from './api';

const vacio = { codigo: '', nombre: '', descripcion: '', duracionHoras: '', capacidad: '' };

export default function Cursos() {
  const [busqueda, setBusqueda] = useState('');
  const [page, setPage] = useState(0);
  const [version, setVersion] = useState(0);
  const [lista, setLista] = useState({ tipo: 'cargando' });
  const [form, setForm] = useState(vacio);
  const [editando, setEditando] = useState(null);
  const [guardando, setGuardando] = useState(false);
  const [mensaje, setMensaje] = useState('');
  const [error, setError] = useState('');
  const [erroresCampo, setErroresCampo] = useState({});

  useEffect(() => {
    const controller = new AbortController();
    setLista({ tipo: 'cargando' });
    const params = new URLSearchParams({ buscar: busqueda, page: String(page), size: '20' });
    solicitar(`cursos?${params}`, { signal: controller.signal }).then(data => setLista({ tipo: 'listo', ...data })).catch(e => {
      if (e.name !== 'AbortError') setLista({ tipo: 'error' });
    });
    return () => controller.abort();
  }, [busqueda, page, version]);

  function cambiar(event) { setForm({ ...form, [event.target.name]: event.target.value }); }
  function limpiar() { setEditando(null); setForm(vacio); setError(''); setErroresCampo({}); }
  async function guardar(event) {
    event.preventDefault();
    setGuardando(true); setError(''); setErroresCampo({}); setMensaje('');
    try {
      await solicitar(editando ? `cursos/${editando}` : 'cursos', {
        method: editando ? 'PUT' : 'POST', body: { ...form, duracionHoras: Number(form.duracionHoras), capacidad: Number(form.capacidad) },
      });
      setMensaje(editando ? 'Curso actualizado.' : 'Curso registrado.');
      limpiar(); setVersion(x => x + 1);
    } catch (e) { setError(e.message); setErroresCampo(e.detalles && typeof e.detalles === 'object' ? e.detalles : {}); } finally { setGuardando(false); }
  }
  async function estado(curso) {
    setGuardando(true); setError(''); setMensaje('');
    try {
      await solicitar(`cursos/${curso.id}/estado`, { method: 'PATCH', body: { estado: curso.estado === 'ACTIVO' ? 'INACTIVO' : 'ACTIVO' } });
      setMensaje('Estado actualizado.'); setVersion(x => x + 1);
    } catch (e) { setError(e.message); } finally { setGuardando(false); }
  }
  return <section className="management" aria-labelledby="cursos-titulo">
    <p className="eyebrow">GESTIÓN ACADÉMICA</p><h1 id="cursos-titulo">Cursos<span>.</span></h1>
    <p>Consulta la oferta y administra sus datos.</p>
    <div className="management-grid"><div className="surface"><h2>Oferta de cursos</h2>
      <label htmlFor="buscar-curso">Buscar por nombre o código</label><input id="buscar-curso" value={busqueda} onChange={e => { setBusqueda(e.target.value); setPage(0); }} />
      <div aria-live="polite">{lista.tipo === 'cargando' && <p>Cargando cursos…</p>}{lista.tipo === 'error' && <><p className="error">No se pudieron cargar los cursos.</p><button onClick={() => setVersion(x => x + 1)}>Reintentar</button></>}
      {lista.tipo === 'listo' && (lista.items.length === 0 ? <p>No hay cursos para mostrar.</p> : <div className="table-scroll"><table><thead><tr><th>Código</th><th>Nombre</th><th>Estado</th><th>Acciones</th></tr></thead><tbody>{lista.items.map(curso => <tr key={curso.id}><td>{curso.codigo}</td><td>{curso.nombre}</td><td>{curso.estado === 'ACTIVO' ? 'Activo' : 'Inactivo'}</td><td><button disabled={guardando} onClick={() => { setEditando(curso.id); setForm({ codigo: curso.codigo, nombre: curso.nombre, descripcion: curso.descripcion, duracionHoras: curso.duracionHoras, capacidad: curso.capacidad }); setError(''); }}>Editar</button>{' '}<button disabled={guardando} onClick={() => estado(curso)}>{curso.estado === 'ACTIVO' ? 'Desactivar' : 'Activar'}</button></td></tr>)}</tbody></table></div>)}
      {lista.tipo === 'listo' && <div className="pagination"><button disabled={page === 0} onClick={() => setPage(x => x - 1)}>Anterior</button><span>Página {page + 1}</span><button disabled={(page + 1) * 20 >= lista.total} onClick={() => setPage(x => x + 1)}>Siguiente</button></div>}</div>
    </div><form className="surface" onSubmit={guardar}><h2>{editando ? 'Editar curso' : 'Registrar curso'}</h2>
      {['codigo', 'nombre', 'descripcion', 'duracionHoras', 'capacidad'].map(campo => <div className="field" key={campo}><label htmlFor={`curso-${campo}`}>{{ codigo: 'Código', nombre: 'Nombre', descripcion: 'Descripción', duracionHoras: 'Duración (horas)', capacidad: 'Capacidad' }[campo]}</label><input id={`curso-${campo}`} name={campo} value={form[campo]} onChange={cambiar} required aria-invalid={typeof erroresCampo[campo] === 'string' || undefined} aria-describedby={typeof erroresCampo[campo] === 'string' ? `error-${campo}` : undefined} type={['duracionHoras', 'capacidad'].includes(campo) ? 'number' : 'text'} min={['duracionHoras', 'capacidad'].includes(campo) ? 1 : undefined} />{typeof erroresCampo[campo] === 'string' && <p className="error" id={`error-${campo}`}>{erroresCampo[campo]}</p>}</div>)}
      <div aria-live="polite">{error && <p className="error">{error}</p>}{mensaje && <p className="success">{mensaje}</p>}</div><button className="primary" disabled={guardando}>{guardando ? 'Guardando…' : editando ? 'Guardar cambios' : 'Registrar curso'}</button>{editando && <button type="button" disabled={guardando} onClick={limpiar}>Cancelar edición</button>}
    </form></div>
  </section>;
}
