package com.example.registroacademico.infrastructure.adapters.out.persistence;

import com.example.registroacademico.application.ports.out.EstudianteRepository;
import com.example.registroacademico.domain.models.Estudiante;
import com.example.registroacademico.infrastructure.adapters.out.persistence.entities.EstudianteEntity;
import com.example.registroacademico.infrastructure.adapters.out.persistence.mappers.EstudianteMapper;
import com.example.registroacademico.infrastructure.adapters.out.persistence.repositories.EstudianteJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class PostqresqlAdapter implements EstudianteRepository {

    private final EstudianteJpaRepository estudianteJpaRepository;

    public PostqresqlAdapter(EstudianteJpaRepository estudianteJpaRepository) {
        this.estudianteJpaRepository = estudianteJpaRepository;
    }

    @Override
    public Page<Estudiante> getAll(Pageable pageable) {
        return estudianteJpaRepository.findAll(pageable)
                .map(EstudianteMapper.INSTANCE::toEstudiante);
    }

    @Override
    public Estudiante save(Estudiante estudiante) {
        estudiante.setFechaCreacion(new Date());
        EstudianteEntity estudianteEntity = estudianteJpaRepository
                .save(EstudianteMapper.INSTANCE.toEstudianteEntity(estudiante));
        return EstudianteMapper.INSTANCE.toEstudiante(estudianteEntity);
    }

    @Override
    public Estudiante findByDocument(String documento) {
        EstudianteEntity estudianteEntity = estudianteJpaRepository.findByCodigoEstudiantil(documento);
        return EstudianteMapper.INSTANCE.toEstudiante(estudianteEntity);
    }

    @Override
    public Estudiante update(String documento, Estudiante estudiante) {

        EstudianteEntity estudianteEntity = estudianteJpaRepository.findByCodigoEstudiantil(documento);
        estudianteEntity.setNombres(estudiante.getNombres());
        estudianteEntity.setApellidos(estudiante.getApellidos());
        estudianteEntity.setFechaNacimiento(estudiante.getFechaNacimiento());
        estudianteEntity.setFechaCreacion(estudiante.getFechaCreacion());
        estudianteEntity.setEmail(estudiante.getEmail());
        estudianteEntity.setCodigoEstudiantil(estudiante.getCodigoEstudiantil());
        estudianteEntity.setEstado(estudiante.getEstado());
        estudianteJpaRepository.save(estudianteEntity);
        return EstudianteMapper.INSTANCE.toEstudiante(estudianteEntity);
    }

    @Override
    public void deleteByDocument(String documento) {
        EstudianteEntity estudianteEntity = estudianteJpaRepository.findByCodigoEstudiantil(documento);
        estudianteJpaRepository.deleteById(estudianteEntity.getId());
    }

    /*private Estudiante toEstudiante(EstudianteEntity estudianteEntity) {
        Estudiante estudiante = new Estudiante();
        estudiante.setId(estudianteEntity.getId());
        estudiante.setDocumento(estudianteEntity.getDocumento());
        estudiante.setNombre(estudianteEntity.getNombre());
        estudiante.setEdad(estudianteEntity.getEdad());
        estudiante.setEmail(estudianteEntity.getEmail());
        return estudiante;
    }

    private EstudianteEntity toEstudianteEntity(Estudiante estudiante) {
        EstudianteEntity estudianteEntity = new EstudianteEntity();
        estudianteEntity.setId(estudiante.getId());
        estudianteEntity.setDocumento(estudiante.getDocumento());
        estudianteEntity.setNombre(estudiante.getNombre());
        estudianteEntity.setEdad(estudiante.getEdad());
        estudianteEntity.setEmail(estudiante.getEmail());
        return estudianteEntity;
    }*/
}
