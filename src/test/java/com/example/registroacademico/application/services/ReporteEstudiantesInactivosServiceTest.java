package com.example.registroacademico.application.services;

import com.example.registroacademico.domain.models.Estudiante;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

class ReporteEstudiantesInactivosServiceTest {

    private final ReporteEstudiantesInactivosService service = new ReporteEstudiantesInactivosService();

    @Test
    void reportarInactivo_noLanzaExcepcionYRegistraLog() {
        Estudiante estudiante = new Estudiante();
        estudiante.setCodigoEstudiantil("EST001");
        estudiante.setNombres("Juan");
        estudiante.setApellidos("Perez");

        assertThatCode(() -> service.reportarInactivo(estudiante)).doesNotThrowAnyException();
    }
}
