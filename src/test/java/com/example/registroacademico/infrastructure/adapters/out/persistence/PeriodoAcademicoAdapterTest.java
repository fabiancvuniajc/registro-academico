package com.example.registroacademico.infrastructure.adapters.out.persistence;

import com.example.registroacademico.infrastructure.adapters.out.persistence.repositories.PeriodoAcademicoJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PeriodoAcademicoAdapterTest {

    @Mock
    private PeriodoAcademicoJpaRepository periodoAcademicoJpaRepository;

    @InjectMocks
    private PeriodoAcademicoAdapter periodoAcademicoAdapter;

    @Test
    void contarAbiertosVencidos_delegaEnRepositorioConEstadoAbierto() {
        LocalDate fechaCorte = LocalDate.of(2024, 1, 1);
        when(periodoAcademicoJpaRepository.countByEstadoAndFechaFinBefore("ABIERTO", fechaCorte))
                .thenReturn(3L);

        long result = periodoAcademicoAdapter.contarAbiertosVencidos(fechaCorte);

        assertThat(result).isEqualTo(3L);
        verify(periodoAcademicoJpaRepository).countByEstadoAndFechaFinBefore("ABIERTO", fechaCorte);
    }
}
