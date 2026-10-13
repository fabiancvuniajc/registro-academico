package com.example.registroacademico.infrastructure.adapters.out.persistence.mappers;

import com.example.registroacademico.domain.models.Estudiante;
import com.example.registroacademico.infrastructure.adapters.out.persistence.entities.EstudianteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EstudianteMapper {

    EstudianteMapper INSTANCE = Mappers.getMapper(EstudianteMapper.class);

    EstudianteEntity toEstudianteEntity(Estudiante estudiante);

    Estudiante toEstudiante(EstudianteEntity estudianteEntity);
}
