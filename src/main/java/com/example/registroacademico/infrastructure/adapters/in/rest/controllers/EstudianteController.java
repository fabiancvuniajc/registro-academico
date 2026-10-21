package com.example.registroacademico.infrastructure.adapters.in.rest.controllers;

import com.example.registroacademico.application.ports.in.EstudianteUseCase;
import com.example.registroacademico.domain.models.Estudiante;
import com.example.registroacademico.domain.models.Wrapper;
import com.example.registroacademico.infrastructure.adapters.in.rest.dto.CreateEstudianteRequest;
import com.example.registroacademico.infrastructure.adapters.in.rest.dto.EstudianteResponse;
import com.example.registroacademico.infrastructure.adapters.in.rest.mappers.EstudianteMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// El restController le permite a la clase ser administrada por la inversion de control de spring
@RestController
// El request mapping es el encargo de establecer la raiz de la url del controlador
@RequestMapping("/api/v1/estudiantes")
// Sirve para identificar que hace el controlador
@Tag(name = "Estudiantes", description = "Administracion de estudiantes universitarios")
public class EstudianteController {

    //aca se declara el useCase que va ser utilizado con injeccion de dependencia
    private final EstudianteUseCase estudianteUseCase;

    //aca se realiza la injeccion de dependencia
    public EstudianteController(EstudianteUseCase estudianteUseCase) {
        this.estudianteUseCase = estudianteUseCase;
    }

    @GetMapping
    @Operation(summary = "Consulta de estudiantes", description = "Consulta de estudiantes universitarios")
    @ApiResponse(responseCode = "200", description = "Consulta de estudiantes exitosa")
    @ApiResponse(responseCode = "503", description = "Error interno")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Wrapper<EstudianteResponse>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {

        Wrapper<Estudiante> wrapper = estudianteUseCase.listar(page, size, sortBy);

        List<EstudianteResponse> list = wrapper.getResult().stream()
                .map(EstudianteMapper.INSTANCE::toEstudianteResponse)
                .toList();
        Wrapper<EstudianteResponse> wrapperResponse = new Wrapper<>();
        wrapperResponse.setResult(list);
        wrapperResponse.setTotalElements(wrapper.getTotalElements());
        wrapperResponse.setTotalPages(wrapper.getTotalPages());
        wrapperResponse.setPage(wrapper.getPage());
        wrapperResponse.setSize(wrapper.getSize());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(wrapperResponse);
    }

    @PostMapping
    public ResponseEntity<Estudiante> crear(
            @Valid @RequestBody CreateEstudianteRequest estudiante) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(estudianteUseCase.crear(
                        EstudianteMapper.INSTANCE.toEstudiante(estudiante)));
    }

    @GetMapping("/{documento}")
    public ResponseEntity<Estudiante> buscar(@PathVariable String documento) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estudianteUseCase.buscar(documento));

    }

    @PutMapping("/{documento}")
    public ResponseEntity<Estudiante> actualizar(@PathVariable String documento,
                                                 @RequestBody Estudiante estudiante) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estudianteUseCase.actualizar(documento, estudiante));
    }

    @DeleteMapping("/{documento}")
    public ResponseEntity<Void> borrar(@PathVariable String documento) {
        estudianteUseCase.borrar(documento);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /*
    private Estudiante toEstudiante(CreateEstudianteRequest createEstudianteRequest) {
        Estudiante estudiante = new Estudiante();
        estudiante.setNombre(createEstudianteRequest.getNombre());
        estudiante.setDocumento(createEstudianteRequest.getDocumento());
        estudiante.setEmail(createEstudianteRequest.getEmail());
        estudiante.setEdad(createEstudianteRequest.getEdad());
        return estudiante;
    }

    private EstudianteResponse toEstudianteResponse(Estudiante estudiante) {
        EstudianteResponse estudianteResponse = new EstudianteResponse();
        estudianteResponse.setNombre(estudiante.getNombre());
        estudianteResponse.setDocumento(estudiante.getDocumento());
        estudianteResponse.setEmail(estudiante.getEmail());
        return estudianteResponse;
    }*/
}
