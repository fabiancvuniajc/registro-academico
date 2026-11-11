package com.example.registroacademico.infrastructure.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.registroacademico.infrastructure.adapters.in.batch.ReportePeriodosTasklet;

/**
 * Job de ejemplo para el taller: reporta los periodos academicos abiertos
 * cuya fecha de fin es anterior a la fecha de corte. No modifica datos.
 *
 * Este adaptador de entrada ("in") solo orquesta Job/Step, delegando la
 * ejecucion del paso a {@link ReportePeriodosTasklet}, que a su vez delega
 * la logica de negocio y el acceso a datos en el caso de uso de application,
 * respetando la arquitectura hexagonal (infrastructure -> application -> domain).
 */
@Configuration
public class ReportePeriodosBatchConfig {

    @Bean
    public Job reportePeriodosJob(JobRepository jobRepository, Step reportePeriodosStep) {
        return new JobBuilder("reportePeriodosJob", jobRepository)
                .start(reportePeriodosStep)
                .build();
    }

    @Bean
    public Step reportePeriodosStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ReportePeriodosTasklet reportePeriodosTasklet) {

        return new StepBuilder("reportePeriodosStep", jobRepository)
                .tasklet(reportePeriodosTasklet, transactionManager)
                .build();
    }
}