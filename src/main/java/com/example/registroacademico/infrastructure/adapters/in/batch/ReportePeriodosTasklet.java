package com.example.registroacademico.infrastructure.adapters.in.batch;

import java.time.LocalDate;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.registroacademico.application.ports.in.ReportePeriodosUseCase;

/**
 * Adaptador de entrada ("in") para Spring Batch: delega la logica de negocio
 * en {@link ReportePeriodosUseCase}, sin conocer detalles de persistencia.
 *
 * Al ser un @Component con @StepScope, Spring gestiona su ciclo de vida y
 * resuelve los jobParameters por cada ejecucion del step, evitando tener
 * que instanciarlo manualmente (new) en la configuracion del batch.
 */
@Component
@StepScope
public class ReportePeriodosTasklet implements Tasklet {

    private final ReportePeriodosUseCase reportePeriodosUseCase;
    private final String fechaCorte;

    public ReportePeriodosTasklet(
            ReportePeriodosUseCase reportePeriodosUseCase,
            @Value("#{jobParameters['fechaCorte']}") String fechaCorte) {
        this.reportePeriodosUseCase = reportePeriodosUseCase;
        this.fechaCorte = fechaCorte;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        LocalDate fecha = LocalDate.parse(fechaCorte);
        reportePeriodosUseCase.reportarPeriodosVencidos(fecha);
        return RepeatStatus.FINISHED;
    }
}
