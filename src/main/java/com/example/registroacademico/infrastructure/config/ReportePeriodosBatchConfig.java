package com.example.registroacademico.infrastructure.config;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Job de ejemplo para el taller: reporta los periodos academicos abiertos
 * cuya fecha de fin es anterior a la fecha de corte. No modifica datos.
 */
@Configuration
public class ReportePeriodosBatchConfig {

    private static final Logger log =
            LoggerFactory.getLogger(ReportePeriodosBatchConfig.class);

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
            Tasklet reportePeriodosTasklet) {

        return new StepBuilder("reportePeriodosStep", jobRepository)
                .tasklet(reportePeriodosTasklet, transactionManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet reportePeriodosTasklet(
            JdbcTemplate jdbcTemplate,
            @Value("#{jobParameters['fechaCorte']}") String fechaCorte) {

        return (contribution, chunkContext) -> {
            LocalDate fecha = LocalDate.parse(fechaCorte);

            Long cantidad = jdbcTemplate.queryForObject("""
                    SELECT COUNT(*)
                    FROM periodo_academico
                    WHERE estado = 'ABIERTO'
                      AND fecha_fin < ?
                    """, Long.class, fecha);

            log.info("Periodos abiertos vencidos al {}: {}", fecha, cantidad);
            return RepeatStatus.FINISHED;
        };
    }
}