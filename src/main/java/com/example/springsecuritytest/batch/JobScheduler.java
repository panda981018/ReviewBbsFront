package com.example.springsecuritytest.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobScheduler {

    private final JobLauncher jobLauncher;
    private final SimpleJobConfiguration simpleJobConfiguration;
    // 초 분 시 요일 일 월 (년도는 생략)
    @Scheduled(cron = "1 0 0 * * *")
    public void runJob() {

        LocalDate beforeDate = LocalDate.now().minusDays(1);
        LocalDateTime dayFrom = LocalDateTime.of(beforeDate.getYear(), beforeDate.getMonthValue(), beforeDate.getDayOfMonth(), 0, 0, 0);
        LocalDateTime dayTo = LocalDateTime.of(beforeDate.getYear(), beforeDate.getMonthValue(), beforeDate.getDayOfMonth(), 23, 59, 59);

        HashMap<String, JobParameter> confMap = new HashMap<>();
        confMap.put("currentTime", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(confMap);

        try {
            jobLauncher.run(simpleJobConfiguration.simpleJob(), jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException
                | JobParametersInvalidException | org.springframework.batch.core.repository.JobRestartException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}