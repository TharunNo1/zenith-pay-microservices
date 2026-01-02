package com.zenithpay.reconcile_service.config;

import com.zenithpay.reconcile_service.model.Transaction;
import com.zenithpay.reconcile_service.processor.ReconcileProcessor;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableJdbcJobRepository;
import org.springframework.batch.core.configuration.support.JdbcDefaultBatchConfiguration;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.parameters.RunIdIncrementer;
import org.springframework.batch.core.launch.support.TaskExecutorJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.batch.infrastructure.item.database.JpaPagingItemReader;
import org.springframework.batch.infrastructure.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.infrastructure.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;


@Configuration
public class BatchConfig extends JdbcDefaultBatchConfiguration {

    @Bean
    public JobRegistry getJobRegistry() {
        return new  MapJobRegistry();
    }

    @Bean
    public JpaPagingItemReader<Transaction> reader(EntityManagerFactory emf) {
        return new JpaPagingItemReaderBuilder<Transaction>()
                .name("transactionReader")
                .entityManagerFactory(emf)
                .queryString("select t from Transaction t WHERE t.status = 'PENDING' order by t.id asc")
                .pageSize(10)
                .build();
    }

    @Bean
    public JpaItemWriter<Transaction> writer(EntityManagerFactory emf) {
        return new JpaItemWriterBuilder<Transaction>()
                .entityManagerFactory(emf)
                .build();
    }

    @Bean
    public Step reconcileStep(JobRepository jobRepository,
                              PlatformTransactionManager transactionManager,
                              JpaPagingItemReader<Transaction> reader,
                              ReconcileProcessor processor,
                              JpaItemWriter<Transaction> writer){
        return new StepBuilder("reconcileStep", jobRepository)
                .<Transaction, Transaction>chunk(10)
                .transactionManager(transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();

    }

    @Bean
    public Job reconcileJob(JobRepository jobRepository, Step reconcileStep) {
        return new JobBuilder("reconcileJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(reconcileStep)
                .build();
    }

    @Bean
    public TaskExecutorJobOperator jobLauncher(JobRepository jobRepository, JobRegistry jobRegistry) throws Exception {
        TaskExecutorJobOperator launcher =  new TaskExecutorJobOperator();
        launcher.setJobRepository(jobRepository);
        launcher.setJobRegistry(jobRegistry);
        launcher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        launcher.afterPropertiesSet();
        return launcher;
    }


}
