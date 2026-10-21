package com.example.registroacademico.application.services;

import com.example.registroacademico.application.ports.in.EstudianteUseCase;
import com.example.registroacademico.application.ports.out.EstudianteRepository;
import com.example.registroacademico.domain.models.Estudiante;
import com.example.registroacademico.domain.models.Wrapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstudianteService implements EstudianteUseCase {

    private final EstudianteRepository estudianteRepository;

    public EstudianteService(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    @Override
    public Wrapper<Estudiante> listar(int page, int size, String sortBy) {
        return estudianteRepository.getAll(page, size, sortBy);
    }

    @Override
    public Estudiante crear(Estudiante estudiante) {
        // aplicaria las reglas de negocio requeridas antes de ir a crear
        Estudiante result = estudianteRepository.save(estudiante);
        return result;
    }

    @Override
    public Estudiante buscar(String documento) {
        return estudianteRepository.findByDocument(documento);
    }

    @Override
    public Estudiante actualizar(String documento, Estudiante estudiante) {
        return estudianteRepository.update(documento, estudiante);
    }

    @Override
    public void borrar(String documento) {
        estudianteRepository.deleteByDocument(documento);
    }
}
