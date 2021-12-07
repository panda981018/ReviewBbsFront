package com.example.springsecuritytest.batch;

import com.example.springsecuritytest.domain.entity.BatchResult;
import com.example.springsecuritytest.domain.entity.BbsEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SimpleJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory; // 영속성 관리를 위해 선언

    private static final int CHUNK_SIZE = 10;

    @Bean
    public Job simpleJob() throws Exception {
        return jobBuilderFactory.get("jw_jpaPagingItemReaderJob")
                .incrementer(new RunIdIncrementer())
                .start(chunkStep())
                .build();
    }

    @Bean
    @JobScope
    public Step chunkStep() throws Exception {
        return stepBuilderFactory.get("jw_step01")
                .<BbsEntity, BatchResult>chunk(CHUNK_SIZE)
                .reader(jpaPagingItemReader())
                .processor(jpaItemProcessor())
                .writer(writer())
                .build();
    }


    @Bean
    @StepScope
    public JpaPagingItemReader<BbsEntity> jpaPagingItemReader() {

        LocalDate beforeDate = LocalDate.now().minusDays(1);
        LocalDateTime dayFrom = LocalDateTime.of(beforeDate.getYear(), beforeDate.getMonthValue(), beforeDate.getDayOfMonth(), 0, 0, 0);
        LocalDateTime dayTo = LocalDateTime.of(beforeDate.getYear(), beforeDate.getMonthValue(), beforeDate.getDayOfMonth(), 23, 59, 59);

        HashMap<String, Object> params = new HashMap<>();
        params.put("searchFrom", dayFrom);
        params.put("searchTo", dayTo);

        String queryStr = "SELECT categoryId.name, COUNT(categoryId.name) " +
                "FROM BbsEntity " +
                "WHERE bbsDate BETWEEN :searchFrom AND :searchTo " +
                "GROUP BY categoryId.name";

        return new JpaPagingItemReaderBuilder<BbsEntity>()
                .name("jw_jpaPagingItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(CHUNK_SIZE)
                .parameterValues(params)
                .queryString(queryStr)
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<Object, BatchResult> jpaItemProcessor() {
        log.info(">>>>>>> Processor");
        return result -> {
            log.info("processor result = {}", result);
            LocalDate beforeDate = LocalDate.now().minusDays(1);
            Object[] objList = (Object[]) result;
            Iterator<Object> ite = Arrays.stream(objList).iterator();
            List<String> objToString = new ArrayList<>();

            while (ite.hasNext()) {
                String str = ite.next().toString();
                objToString.add(str);
            } // 리스트로 변경하는 작업

            // batchresult로 만드는 작업
            return BatchResult.builder()
                    .name(objToString.get(0))
                    .bbsCount(Integer.parseInt(objToString.get(1)))
                    .staticsDate(beforeDate)
                    .build();
        };
    }

    @Bean
    @StepScope
    public JpaItemWriter<BatchResult> writer() {
        return new JpaItemWriterBuilder<BatchResult>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}