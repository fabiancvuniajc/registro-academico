package com.example.registroacademico.infrastructure.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.registroacademico.domain.models.Estudiante;
import com.example.registroacademico.infrastructure.adapters.in.batch.EstudianteInactivoItemProcessor;
import com.example.registroacademico.infrastructure.adapters.in.batch.EstudianteInactivoItemReader;
import com.example.registroacademico.infrastructure.adapters.in.batch.EstudianteInactivoItemWriter;
import com.example.registroacademico.infrastructure.adapters.out.persistence.entities.EstudianteEntity;

/**
 * Job de ejemplo para el taller: reporta los estudiantes que no tienen
 * matricula en el periodo academico actual (estado 'ABIERTO'). No
 * modifica datos.
 *
 * A diferencia de {@link ReportePeriodosBatchConfig} (un Tasklet simple),
 * este Step es chunk-oriented: lee, procesa y escribe los estudiantes en
 * lotes, delegando cada etapa a un adaptador de entrada ("in") dedicado
 * y manteniendo la logica de negocio en application.
 */
@Configuration
public class ReporteEstudiantesInactivosBatchConfig {

    private static final int CHUNK_SIZE = 5;

    @Bean
    public Job reporteEstudiantesInactivosJob(JobRepository jobRepository, Step reporteEstudiantesInactivosStep) {
        return new JobBuilder("reporteEstudiantesInactivosJob", jobRepository)
                .start(reporteEstudiantesInactivosStep)
                .build();
    }

    @Bean
    public Step reporteEstudiantesInactivosStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            EstudianteInactivoItemReader reader,
            EstudianteInactivoItemProcessor processor,
            EstudianteInactivoItemWriter writer) {

        return new StepBuilder("reporteEstudiantesInactivosStep", jobRepository)
                .<EstudianteEntity, Estudiante>chunk(CHUNK_SIZE, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}