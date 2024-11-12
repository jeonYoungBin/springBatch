package com.hello.springbatch.job.ValidatorParam.Validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.util.StringUtils;

public class FileParamValidator implements JobParametersValidator {
    @Override
    public void validate(JobParameters jobParameters) throws JobParametersInvalidException {
        String fileName = jobParameters.getString("fileName");

        if(!StringUtils.endsWithIgnoreCase(fileName,"csv")) {
            throw new JobParametersInvalidException("This is not csv file");
        }
    }
}
