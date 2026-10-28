package com.example.registroacademico.application.services;

import com.example.registroacademico.application.ports.in.EstudianteUseCase;
import com.example.registroacademico.application.ports.out.EstudianteRepository;
import com.example.registroacademico.domain.models.Estudiante;
import com.example.registroacademico.domain.models.Wrapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.Duration;

@Service
public class EstudianteService implements EstudianteUseCase {

    private static final Duration TTL = Duration.ofMinutes(15);
    private final EstudianteRepository estudianteRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final Gson gson;

    public EstudianteService(EstudianteRepository estudianteRepository, RedisTemplate<String, String> redisTemplate, Gson gson) {
        this.estudianteRepository = estudianteRepository;
        this.redisTemplate = redisTemplate;
        this.gson = gson;
    }

    /*
    @Override
    public Wrapper<Estudiante> listar(int page, int size, String sortBy) {
        return estudianteRepository.getAll(page, size, sortBy);
    }*/

    @Override
    public Wrapper<Estudiante> listar(int page, int size, String sortBy, boolean refresh) {
        String valor = redisTemplate.opsForValue().get("todos_estudiantes");

        if (valor == null || refresh) {
            Wrapper<Estudiante> estudiantes = estudianteRepository.getAll(page, size, sortBy);
            String json = gson.toJson(estudiantes);
            redisTemplate.opsForValue().set("todos_estudiantes", json, TTL);
            return estudiantes;
        }
        Type pageEstudiantes = new TypeToken<Wrapper<Estudiante>>() {}.getType();
        return gson.fromJson(valor, pageEstudiantes);
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
