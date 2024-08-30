package com.maia.kiev.primeiroprojetospringbatch.scheduleconfig;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JobScheduler {
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    @Qualifier("imprimeJob")
    private Job sampleJob;

    @Scheduled(cron = "*/10 * * * * *")
    public void runJob() throws Exception {
        jobLauncher.run(
            sampleJob,
            new JobParametersBuilder()
                .addDate("startTime", new Date())
                .toJobParameters()
        );
    }
}
