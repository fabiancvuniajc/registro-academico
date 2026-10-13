package com.example.registroacademico.infrastructure.adapters.in.rest.mappers;

import com.example.registroacademico.domain.models.Estudiante;
import com.example.registroacademico.infrastructure.adapters.in.rest.dto.CreateEstudianteRequest;
import com.example.registroacademico.infrastructure.adapters.in.rest.dto.EstudianteResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EstudianteMapper {

    EstudianteMapper INSTANCE = Mappers.getMapper(EstudianteMapper.class);

    EstudianteResponse toEstudianteResponse(Estudiante estudiante);

    Estudiante toEstudiante(CreateEstudianteRequest estudiante);
}
