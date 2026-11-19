package com.example.registroacademico.infrastructure.adapters.out.persistence;

import com.example.registroacademico.domain.exceptions.RecursoNoEncontradoException;
import com.example.registroacademico.domain.models.Estudiante;
import com.example.registroacademico.domain.models.Wrapper;
import com.example.registroacademico.infrastructure.adapters.out.persistence.entities.EstudianteEntity;
import com.example.registroacademico.infrastructure.adapters.out.persistence.repositories.EstudianteJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostqresqlAdapterTest {

    @Mock
    private EstudianteJpaRepository estudianteJpaRepository;

    private PostqresqlAdapter postqresqlAdapter;

    private EstudianteEntity entity;

    @BeforeEach
    void setUp() {
        postqresqlAdapter = new PostqresqlAdapter(estudianteJpaRepository);
        entity = new EstudianteEntity();
        entity.setId(1);
        entity.setCodigoEstudiantil("EST001");
        entity.setNombres("Juan");
        entity.setApellidos("Perez");
        entity.setEmail("juan@mail.com");
        entity.setEstado("ACTIVO");
    }

    @Test
    void getAll_devuelveWrapperConResultadosPaginados() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<EstudianteEntity> page = new PageImpl<>(List.of(entity), pageable, 1);
        when(estudianteJpaRepository.findAll(any(Pageable.class))).thenReturn(page);

        Wrapper<Estudiante> result = postqresqlAdapter.getAll(0, 10, "id");

        assertThat(result.getResults()).hasSize(1);
        assertThat(result.getResults().get(0).getCodigoEstudiantil()).isEqualTo("EST001");
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getSize()).isEqualTo(10);
    }

    @Test
    void save_persisteYDevuelveEstudianteMapeado() {
        Estudiante estudiante = new Estudiante();
        estudiante.setCodigoEstudiantil("EST001");
        when(estudianteJpaRepository.save(any(EstudianteEntity.class))).thenReturn(entity);

        Estudiante result = postqresqlAdapter.save(estudiante);

        assertThat(result.getCodigoEstudiantil()).isEqualTo("EST001");
        assertThat(estudiante.getFechaCreacion()).isNotNull();
        verify(estudianteJpaRepository).save(any(EstudianteEntity.class));
    }

    @Test
    void findByDocument_existente_devuelveEstudiante() {
        when(estudianteJpaRepository.findByCodigoEstudiantil("EST001")).thenReturn(Optional.of(entity));

        Estudiante result = postqresqlAdapter.findByDocument("EST001");

        assertThat(result.getCodigoEstudiantil()).isEqualTo("EST001");
    }

    @Test
    void findByDocument_noExistente_lanzaRecursoNoEncontradoException() {
        when(estudianteJpaRepository.findByCodigoEstudiantil("NOEXISTE")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postqresqlAdapter.findByDocument("NOEXISTE"))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("NOEXISTE");
    }

    @Test
    void update_existente_actualizaYDevuelveEstudiante() {
        when(estudianteJpaRepository.findByCodigoEstudiantil("EST001")).thenReturn(Optional.of(entity));
        when(estudianteJpaRepository.save(any(EstudianteEntity.class))).thenReturn(entity);

        Estudiante estudiante = new Estudiante();
        estudiante.setNombres("Juan Actualizado");
        estudiante.setApellidos("Perez");
        estudiante.setEmail("nuevo@mail.com");
        estudiante.setCodigoEstudiantil("EST001");
        estudiante.setEstado("ACTIVO");

        Estudiante result = postqresqlAdapter.update("EST001", estudiante);

        assertThat(result).isNotNull();
        verify(estudianteJpaRepository).save(entity);
    }

    @Test
    void update_noExistente_noActualiza() {
        when(estudianteJpaRepository.findByCodigoEstudiantil("NOEXISTE")).thenReturn(Optional.empty());

        Estudiante estudiante = new Estudiante();

        Estudiante updateEstudiante = postqresqlAdapter.update("NOEXISTE", estudiante);

        assertThat(updateEstudiante).isEqualTo(estudiante);
        verify(estudianteJpaRepository, never()).save(any(EstudianteEntity.class));
    }

    @Test
    void deleteByDocument_existente_borraPorId() {
        when(estudianteJpaRepository.findByCodigoEstudiantil("EST001")).thenReturn(Optional.of(entity));

        postqresqlAdapter.deleteByDocument("EST001");

        verify(estudianteJpaRepository).deleteById(entity.getId());
    }

    @Test
    void deleteByDocument_noExistente_noHaceNada() {
        when(estudianteJpaRepository.findByCodigoEstudiantil("NOEXISTE")).thenReturn(Optional.empty());

        postqresqlAdapter.deleteByDocument("NOEXISTE");

        verify(estudianteJpaRepository, never()).deleteById(anyInt());
    }
}