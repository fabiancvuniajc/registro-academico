package com.example.registroacademico.infrastructure.adapters.out.persistence;

import com.example.registroacademico.application.ports.out.EstudianteRepository;
import com.example.registroacademico.domain.exceptions.RecursoNoEncontradoException;
import com.example.registroacademico.domain.models.Wrapper;
import com.example.registroacademico.domain.models.Estudiante;
import com.example.registroacademico.infrastructure.adapters.out.persistence.entities.EstudianteEntity;
import com.example.registroacademico.infrastructure.adapters.out.persistence.mappers.EstudianteMapper;
import com.example.registroacademico.infrastructure.adapters.out.persistence.repositories.EstudianteJpaRepository;
import com.example.registroacademico.infrastructure.adapters.out.persistence.repositories.ProfesorJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class PostqresqlAdapter implements EstudianteRepository {

    private final EstudianteJpaRepository estudianteJpaRepository;

    public PostqresqlAdapter(EstudianteJpaRepository estudianteJpaRepository, ProfesorJpaRepository profesorJpaRepository) {
        this.estudianteJpaRepository = estudianteJpaRepository;
    }

    @Override
    public Wrapper<Estudiante> getAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Estudiante> listEstudiante = estudianteJpaRepository.findAll(pageable)
                .map(EstudianteMapper.INSTANCE::toEstudiante);

        Wrapper<Estudiante> wrapper = new Wrapper<>();
        wrapper.setResult(listEstudiante.getContent());
        wrapper.setPage(page);
        wrapper.setSize(size);
        wrapper.setTotalElements(listEstudiante.getNumberOfElements());
        wrapper.setTotalPages(listEstudiante.getTotalPages());
        return  wrapper;
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
        Optional<EstudianteEntity> estudianteEntity = estudianteJpaRepository.findByCodigoEstudiantil(documento);
        if(estudianteEntity.isEmpty()){
            throw new RecursoNoEncontradoException("No se encuentra estudiante con codigo estudiantil: "+ documento);
        }
        return EstudianteMapper.INSTANCE.toEstudiante(estudianteEntity.get());
    }

    @Override
    public Estudiante update(String documento, Estudiante estudiante) {

        Optional<EstudianteEntity> estudianteEntity = estudianteJpaRepository.findByCodigoEstudiantil(documento);
        if(estudianteEntity.isEmpty()){
            throw new RecursoNoEncontradoException("No se encuentra estudiante con codigo estudiantil: "+ documento);
        }
        estudianteEntity.get().setNombres(estudiante.getNombres());
        estudianteEntity.get().setApellidos(estudiante.getApellidos());
        estudianteEntity.get().setFechaNacimiento(estudiante.getFechaNacimiento());
        estudianteEntity.get().setFechaCreacion(estudiante.getFechaCreacion());
        estudianteEntity.get().setEmail(estudiante.getEmail());
        estudianteEntity.get().setCodigoEstudiantil(estudiante.getCodigoEstudiantil());
        estudianteEntity.get().setEstado(estudiante.getEstado());
        estudianteJpaRepository.save(estudianteEntity.get());
        return EstudianteMapper.INSTANCE.toEstudiante(estudianteEntity.get());
    }

    @Override
    public void deleteByDocument(String documento) {
        Optional<EstudianteEntity> estudianteEntity = estudianteJpaRepository.findByCodigoEstudiantil(documento);
        estudianteEntity.ifPresent(entity -> estudianteJpaRepository.deleteById(entity.getId()));
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
