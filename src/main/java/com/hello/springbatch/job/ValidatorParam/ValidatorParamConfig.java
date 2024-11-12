package com.hello.springbatch.job.ValidatorParam;

import com.hello.springbatch.job.ValidatorParam.Validator.FileParamValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class ValidatorParamConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job validatorParamJob(Step validatorParamStep) {
        return jobBuilderFactory.get("validatorParamJob")
                .incrementer(new RunIdIncrementer())
                .validator(multiValidator())
                .start(validatorParamStep)
                .build();
    }

    private CompositeJobParametersValidator multiValidator() {
        CompositeJobParametersValidator validator = new CompositeJobParametersValidator();
        validator.setValidators(Arrays.asList(new FileParamValidator()));
        return validator;
    }

    @JobScope
    @Bean
    public Step validatorParamStep(Tasklet validatorParamTasklet) {
        return stepBuilderFactory.get("validatorParamStep")
                .tasklet(validatorParamTasklet)
                .build();
    }

    @StepScope
    @Bean
    public Tasklet validatorParamTasklet(@Value("#{jobParameters['fileName']}") String fileName) {
        return (contribution, chunkContext) -> {
            System.out.println("validatorParam param tasklet");
            return RepeatStatus.FINISHED;
        };
    }
}
