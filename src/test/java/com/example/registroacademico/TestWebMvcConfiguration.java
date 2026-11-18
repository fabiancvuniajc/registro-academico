package com.example.registroacademico;

import org.springframework.boot.SpringBootConfiguration;

/**
 * Configuración mínima usada únicamente por los tests {@code @WebMvcTest}.
 * <p>
 * Se ubica en el paquete raíz ({@code com.example.registroacademico}) porque
 * Spring Boot exige encontrar una {@code @SpringBootConfiguration} ancestro
 * del paquete del test. Deliberadamente NO declara {@code @ComponentScan}:
 * cada test {@code @WebMvcTest} debe importar explícitamente (vía
 * {@code @Import}) solo el controlador bajo prueba; de lo contrario un
 * {@code @ComponentScan} amplio registraría TODOS los controladores del
 * paquete (no solo el indicado en {@code @WebMvcTest(controllers = ...)}),
 * ya que el filtro {@code WebMvcTypeExcludeFilter} de la slice sólo se
 * aplica al escaneo automático de Boot, no a un {@code @ComponentScan}
 * declarado manualmente en otra clase.
 */
@SpringBootConfiguration
public class TestWebMvcConfiguration {
}
