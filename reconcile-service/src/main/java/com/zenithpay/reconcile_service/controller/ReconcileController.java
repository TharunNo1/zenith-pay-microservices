package com.zenithpay.reconcile_service.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.support.TaskExecutorJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reconcile")
@Slf4j
public class ReconcileController {

    @Autowired
    private TaskExecutorJobOperator jobLauncher;
    @Autowired
    private Job reconcileJob;


    @PostMapping("/run")
    public ResponseEntity<Map<String, Object>> triggerJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("startAt", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution execution = this.jobLauncher.start(this.reconcileJob, jobParameters);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Reconciliation Job Started!");
            response.put("executionId", execution.getId());
            response.put("status", execution.getStatus().toString());

            return ResponseEntity.accepted().body(response);
        }
        catch (Exception e) {
            log.error("Failed to start reconciliation job", e);
            return ResponseEntity.internalServerError().body(Map.of("error", "Couldn't start job: "+ e.getMessage()));
        }
    }
}
