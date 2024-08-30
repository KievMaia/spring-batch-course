package com.maia.kiev.primeiroprojetospringbatch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class PrimeiroJobSpringBatchConfig {
    @Bean
    public Job imprimeJob(JobRepository jobRepository, Step imprimeOlaStep) {
        return new JobBuilder("imprimeOlaJob", jobRepository)
                .start(imprimeOlaStep)
                .build();
    }
}
