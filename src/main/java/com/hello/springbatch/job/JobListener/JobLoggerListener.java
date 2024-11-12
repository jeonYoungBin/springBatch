package com.hello.springbatch.job.JobListener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
public class JobLoggerListener implements JobExecutionListener {

    private static String BEFORE_MSG = "{} Job is Running";
    private static String AFTER_MSG = "{} Job is Done. (Status : {})";

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info(BEFORE_MSG, jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info(AFTER_MSG,
                jobExecution.getJobInstance().getJobName(),
                jobExecution.getStatus());

        if(jobExecution.getStatus() == BatchStatus.FAILED) {
            //email
            log.info("Job is failed");
        }
    }
}
