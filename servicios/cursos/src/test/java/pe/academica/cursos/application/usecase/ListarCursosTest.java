package pe.academica.cursos.application.usecase;

import pe.academica.cursos.application.exception.ParametrosConsultaInvalidosException;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import pe.academica.cursos.application.port.out.CursoRepository;
import pe.academica.cursos.application.model.PaginaCursos;

class ListarCursosTest {
    private final CursoRepository repository = mock(CursoRepository.class);
    private final ListarCursos listar = new ListarCursos(repository);

    @Test void normalizaFiltroYDevuelvePagina() {
        var pagina = new PaginaCursos(List.of(), 0, 20, 0);
        when(repository.listar("Java", 0, 20)).thenReturn(pagina);
        assertThat(listar.ejecutar(" Java ", 0, 20)).isEqualTo(pagina);
        verify(repository).listar("Java", 0, 20);
    }

    @Test void rechazaValoresFueraDeRango() {
        assertThatThrownBy(() -> listar.ejecutar("", -1, 20)).isInstanceOf(ParametrosConsultaInvalidosException.class);
        assertThatThrownBy(() -> listar.ejecutar("", 0, 0)).isInstanceOf(ParametrosConsultaInvalidosException.class);
        assertThatThrownBy(() -> listar.ejecutar("", 0, 101)).isInstanceOf(ParametrosConsultaInvalidosException.class);
        verifyNoInteractions(repository);
    }
}
