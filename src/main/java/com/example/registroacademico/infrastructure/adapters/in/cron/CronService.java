package com.example.registroacademico.infrastructure.adapters.in.cron;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Dispara periodicamente el job de reporte de periodos. El cron "-"
 * (valor por defecto) desactiva la tarea.
 */
@Service
public class CronService {

    private final JobLauncher jobLauncher;
    private final Job reportePeriodosJob;
    private final ZoneId zona;

    public CronService(
            JobLauncher jobLauncher,
            //@Qualifier("reportePeriodosJob") Job reportePeriodosJob,
            @Qualifier("reporteEstudiantesInactivosJob") Job reportePeriodosJob,
            @Value("${app.taller.zona:UTC}") String zona) {

        this.jobLauncher = jobLauncher;
        this.reportePeriodosJob = reportePeriodosJob;
        this.zona = ZoneId.of(zona);
    }

    @Scheduled(cron = "${app.taller.cron:-}", zone = "${app.taller.zona:UTC}")
    public void ejecutarReporte() throws Exception {
        JobParameters parametros = new JobParametersBuilder()
                .addString("fechaCorte", LocalDate.now(zona).toString())
                .addString("ejecucionId", UUID.randomUUID().toString())
                .toJobParameters();

        jobLauncher.run(reportePeriodosJob, parametros);
    }
}