package com.maia.kiev.parimparjob;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.function.FunctionItemProcessor;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
//@EnableScheduling
public class BatchConfig {

    @Bean
    public Job imprimeParImparJob(JobRepository jobRepository, Step step) {
        return new JobBuilder("job", jobRepository)
                .start(step)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step imprimeParImparStep(JobRepository jobRepository, final PlatformTransactionManager transactionManager) {
        return new StepBuilder("imprimeParImparStep", jobRepository)
                .<Integer, String>chunk(10, transactionManager)
                .reader(contaAteDezReader())
                .processor(parOuImparProcessor())
                .writer(imprimeWriter())
                .build();
    }

    private IteratorItemReader<Integer> contaAteDezReader() {
        final var numerosDeUmAteDez = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        return new IteratorItemReader<>(numerosDeUmAteDez.iterator());
    }

    private FunctionItemProcessor<Integer, String> parOuImparProcessor() {
        return new FunctionItemProcessor<>
                (item -> item % 2 == 0 ? java.lang.String.format("Item %s é Par", item) : java.lang.String.format("Item %s é ímpar", item));
    }

    private ItemWriter<String> imprimeWriter() {
        return items -> items.forEach(System.out::println);
    }

    @Bean
    @StepScope
    public static Tasklet imprimeOlaTasklet(@Value("#{jobParameters['nome']}") String nome) {
        return (StepContribution contribution, ChunkContext chunkContext) -> {
            System.out.printf("Hello World! %s", nome);
            return RepeatStatus.FINISHED;
        };
    }
}
