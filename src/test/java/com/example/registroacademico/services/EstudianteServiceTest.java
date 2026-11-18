package com.example.registroacademico.services;

import com.example.registroacademico.application.ports.out.EstudianteRepository;
import com.example.registroacademico.application.services.EstudianteService;
import com.example.registroacademico.domain.models.Estudiante;
import com.example.registroacademico.domain.models.Wrapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstudianteServiceTest {

    @Mock
    private EstudianteRepository estudianteRepository;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    private Gson gson;

    @InjectMocks
    private EstudianteService estudianteService;

    private Estudiante estudiante;

    @BeforeEach
    void setUp() {
        gson = new Gson();
        estudiante = new Estudiante();
        estudiante.setId(1);
        estudiante.setCodigoEstudiantil("EST001");
        estudiante.setNombres("Juan");
        estudiante.setApellidos("Perez");
        estudiante.setEmail("juan@mail.com");
        estudiante.setEstado("ACTIVO");
    }

    private EstudianteService buildServiceWithRealGson() {
        return new EstudianteService(estudianteRepository, redisTemplate, gson);
    }

    @Test
    void listar_sinCacheDisponible_consultaRepositorioYGuardaEnCache() {
        estudianteService = buildServiceWithRealGson();
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("todos_estudiantes")).thenReturn(null);

        Wrapper<Estudiante> wrapper = new Wrapper<>();
        wrapper.setResult(List.of(estudiante));
        wrapper.setTotalElements(1);
        wrapper.setTotalPages(1);
        wrapper.setPage(0);
        wrapper.setSize(10);
        when(estudianteRepository.getAll(anyInt(), anyInt(), anyString())).thenReturn(wrapper);

        Wrapper<Estudiante> result = estudianteService.listar(0, 10, "id", false);

        assertThat(result.getResult()).hasSize(1);
        assertThat(result.getResult().get(0).getCodigoEstudiantil()).isEqualTo("EST001");
        verify(estudianteRepository, times(1)).getAll(anyInt(), anyInt(), anyString());
        verify(valueOperations).set(eq("todos_estudiantes"), anyString(), any());
    }

    @Test
    void listar_conCacheDisponibleYSinRefresh_devuelveDesdeCache() {
        estudianteService = buildServiceWithRealGson();
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        Wrapper<Estudiante> wrapper = new Wrapper<>();
        wrapper.setResult(List.of(estudiante));
        wrapper.setTotalElements(1);
        wrapper.setTotalPages(1);
        wrapper.setPage(0);
        wrapper.setSize(10);
        String json = gson.toJson(wrapper);
        when(valueOperations.get("todos_estudiantes")).thenReturn(json);

        Wrapper<Estudiante> result = estudianteService.listar(0, 10, "id", false);

        assertThat(result.getResult()).hasSize(1);
        verify(estudianteRepository, never()).getAll(anyInt(), anyInt(), anyString());
    }

    @Test
    void listar_conRefreshTrue_ignoraCacheYConsultaRepositorio() {
        estudianteService = buildServiceWithRealGson();
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        Wrapper<Estudiante> wrapper = new Wrapper<>();
        wrapper.setResult(Collections.emptyList());
        when(estudianteRepository.getAll(0, 10, "id")).thenReturn(wrapper);

        Wrapper<Estudiante> result = estudianteService.listar(0, 10, "id", true);

        assertThat(result).isNotNull();
        verify(estudianteRepository).getAll(0, 10, "id");
        verify(valueOperations).set(eq("todos_estudiantes"), anyString(), any());
    }

    @Test
    void crear_delegaEnRepositorio() {
        when(estudianteRepository.save(estudiante)).thenReturn(estudiante);

        Estudiante result = estudianteService.crear(estudiante);

        assertThat(result).isEqualTo(estudiante);
        verify(estudianteRepository).save(estudiante);
    }

    @Test
    void buscar_delegaEnRepositorio() {
        when(estudianteRepository.findByDocument("EST001")).thenReturn(estudiante);

        Estudiante result = estudianteService.buscar("EST001");

        assertThat(result).isEqualTo(estudiante);
        verify(estudianteRepository).findByDocument("EST001");
    }

    @Test
    void actualizar_delegaEnRepositorio() {
        when(estudianteRepository.update("EST001", estudiante)).thenReturn(estudiante);

        Estudiante result = estudianteService.actualizar("EST001", estudiante);

        assertThat(result).isEqualTo(estudiante);
        verify(estudianteRepository).update("EST001", estudiante);
    }

    @Test
    void borrar_delegaEnRepositorio() {
        estudianteService.borrar("EST001");

        verify(estudianteRepository).deleteByDocument("EST001");
    }
}