import { useEffect, useState } from 'react';
import { contarActivos, contarMatriculasActivas } from './api';
import Cursos from './Cursos';
import './styles.css';

const secciones = [
  { id: 'cursos', titulo: 'Cursos activos', descripcion: 'Oferta disponible', cargar: signal => contarActivos('cursos', signal), indice: '01' },
  { id: 'estudiantes', titulo: 'Estudiantes activos', descripcion: 'Comunidad académica', cargar: signal => contarActivos('estudiantes', signal), indice: '02' },
  { id: 'matriculas', titulo: 'Matrículas activas', descripcion: 'Inscripciones confirmadas', cargar: contarMatriculasActivas, indice: '03' },
];

function Indicador({ seccion }) {
  const [intento, setIntento] = useState(0);
  const [estado, setEstado] = useState({ tipo: 'cargando' });
  useEffect(() => {
    const controller = new AbortController();
    setEstado({ tipo: 'cargando' });
    seccion.cargar(controller.signal).then(valor => setEstado({ tipo: 'listo', valor })).catch(error => {
      if (error.name !== 'AbortError') setEstado({ tipo: 'error' });
    });
    return () => controller.abort();
  }, [seccion, intento]);
  return <article className="indicator" aria-labelledby={`titulo-${seccion.id}`}>
    <div className="indicator-top"><span className="indicator-index">{seccion.indice} / INDICADOR</span><span aria-hidden="true">↗</span></div>
    <h3 id={`titulo-${seccion.id}`}>{seccion.titulo}</h3>
    <div aria-live="polite">
      {estado.tipo === 'cargando' && <p className="status">Consultando datos…</p>}
      {estado.tipo === 'listo' && <><p className="number">{estado.valor.toLocaleString('es-PE')}</p><p className="hint">{estado.valor === 0 ? 'Sin registros activos' : seccion.descripcion}</p></>}
      {estado.tipo === 'error' && <><p className="error">No se pudo cargar este indicador.</p><button className="retry" onClick={() => setIntento(x => x + 1)}>Reintentar</button></>}
    </div>
  </article>;
}

export default function App() {
  const [pantalla, setPantalla] = useState('panel');
  return <div className="app-shell">
    <a className="skip" href="#contenido">Ir al contenido</a>
    <header className="topbar"><div className="brand"><span className="brand-mark" aria-hidden="true">A</span><div><strong>Gestión académica</strong><span>Administración educativa</span></div></div><span className="header-label">PANEL DE CONTROL</span></header>
    <nav className="navigation" aria-label="Navegación principal"><button onClick={() => setPantalla('panel')} aria-current={pantalla === 'panel' ? 'page' : undefined}>Panel</button><button onClick={() => setPantalla('cursos')} aria-current={pantalla === 'cursos' ? 'page' : undefined}>Cursos</button></nav>
    <main id="contenido" tabIndex="-1">{pantalla === 'cursos' ? <Cursos /> : <>
      <div className="breadcrumb">INICIO <span aria-hidden="true">/</span> PANEL GENERAL</div>
      <div className="intro"><div><p className="eyebrow">RESUMEN ACADÉMICO</p><h1>Tu institución, de un vistazo<span>.</span></h1><p>Consulta el estado actual de tu actividad académica desde un solo lugar.</p></div><div className="intro-aside"><span className="live-dot" aria-hidden="true" /> DATOS DEL SISTEMA</div></div>
      <section aria-labelledby="indicadores"><div className="section-heading"><div><p className="eyebrow">PANORAMA GENERAL</p><h2 id="indicadores">Actividad actual</h2></div><p>Información consultada en los servicios</p></div><div className="indicators">{secciones.map(seccion => <Indicador key={seccion.id} seccion={seccion} />)}</div></section>
      <section className="next" aria-labelledby="operaciones"><div><p className="eyebrow">GESTIÓN ACADÉMICA</p><h2 id="operaciones">Todo comienza aquí</h2><p>Accede a la oferta de cursos. Las pantallas de estudiantes y matrículas se incorporarán próximamente.</p><button className="primary" onClick={() => setPantalla('cursos')}>Gestionar cursos</button></div><div className="next-list"><span>01 <strong>Cursos</strong></span><span>02 <strong>Estudiantes</strong></span><span>03 <strong>Matrículas</strong></span></div></section>
    </>}</main><footer><span>Gestión académica</span><span>Panel de administración</span></footer>
  </div>;
}
