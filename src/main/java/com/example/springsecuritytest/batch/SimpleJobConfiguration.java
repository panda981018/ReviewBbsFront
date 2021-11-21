package com.example.springsecuritytest.batch;

import com.example.springsecuritytest.domain.entity.BatchResult;
import com.example.springsecuritytest.domain.entity.BbsEntity;
import com.example.springsecuritytest.dto.BatchDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.*;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.*;
import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import com.example.springsecuritytest.conf.AppConfig.*;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SimpleJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManager entityManager;
    private final EntityManagerFactory entityManagerFactory; // 영속성 관리를 위해 선언

    private static final int CHUNK_SIZE = 10;

    @Bean
    public Job simpleJob() throws Exception {
        return jobBuilderFactory.get("jdbcPagingItemReaderJob")
                .incrementer(new RunIdIncrementer())
                .start(chunkStep())
                .build();
    }

    // taskelt
//    @Bean
//    @JobScope // Step 선언문에서 사용가능함.
//    public Step simpleStep1(@Value("#{jobParameters[startDay]}") String startDay) { // tasklet 방식
//        startDay = LocalDate.now().minusDays(1).toString();
//        LocalDate startLocalDay = LocalDate.parse(startDay, DateTimeFormatter.ISO_LOCAL_DATE);
////        LocalDate beforeDay = LocalDate.now().minusDays(1); // 오늘 11/9 어제 11/8
//        LocalDateTime start = LocalDateTime.of(startLocalDay.getYear(), startLocalDay.getMonthValue(), startLocalDay.getDayOfMonth(), 0, 0, 0);
//        LocalDateTime end = LocalDateTime.of(startLocalDay.getYear(), startLocalDay.getMonthValue(), startLocalDay.getDayOfMonth(), 23, 59, 59);
//
//        return stepBuilderFactory.get("simpleStep2")
//                .tasklet(((contribution, chunkContext) -> {
//                    log.info(">> This is Step2");
//                    bbsService.batchTest(start, end);
//                    return RepeatStatus.FINISHED;
//                }))
//                .build();
//    }

//    @Bean
//    @JobScope
//    public Step simpleStep2() {
//        LocalDate beforeDay = LocalDate.now().minusDays(1); // 오늘 11/9 어제 11/8
//        LocalDateTime start = LocalDateTime.of(beforeDay.getYear(), beforeDay.getMonthValue(), beforeDay.getDayOfMonth(), 0, 0, 0);
//        LocalDateTime end = LocalDateTime.of(beforeDay.getYear(), beforeDay.getMonthValue(), beforeDay.getDayOfMonth(), 23, 59, 59);
//
//        return stepBuilderFactory.get("simpleStep2")
//                .tasklet(((contribution, chunkContext) -> {
//                    log.info(">> This is Step2");
//                    bbsService.batchTest(start, end);
//                    return RepeatStatus.FINISHED;
//                }))
//                .build();
//    }

//    @BeforeStep
//    public void beforeStep(final StepExecution stepExecution) {
//        JobParameters jobParameters = new JobParametersBuilder()
//                .addString("beforeDay", LocalDate.now().minusDays(2).toString())
//                .toJobParameters();
//
//        JobParameters parameters = stepExecution.getJobExecution().getJobParameters();
//    }

    @Bean
    @JobScope
    public Step chunkStep() throws Exception {
        return stepBuilderFactory.get("chunkStep1")
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

//        String queryStr = "SELECT b.categoryId.name, nvl(TEMP.COUNT, 0)" +
//                "FROM BbsEntity b" +
//                "LEFT JOIN" +
//                "(SELECT B2.categoryId.name, COUNT(B2.categoryId.name) as COUNT" +
//                "FROM BbsEntity B2" +
//                "WHERE B2.bbsDate between :searchFrom AND :searchTo GROUP BY B2.categoryId.name) TEMP ON b.id = TEMP.id";

        String queryStr = "SELECT categoryId.name, COUNT(categoryId.name) " +
                "FROM BbsEntity " +
                "WHERE bbsDate BETWEEN :searchFrom AND :searchTo " +
                "GROUP BY categoryId.name";

//        String queryStr = "SELECT categoryId.name, COUNT(categoryId.name) " +
//                "FROM BbsEntity " +
//                "WHERE bbsDate BETWEEN :searchFrom AND :searchTo " +
//                "GROUP BY categoryId.name";

        return new JpaPagingItemReaderBuilder<BbsEntity>()
                .name("jpaPagingItemReader")
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
                    .bbsCount(Long.parseLong(objToString.get(1)))
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

//    @Bean
//    @StepScope
//    public JdbcPagingItemReader<BbsEntity> reader() throws Exception {
//        LocalDate beforeDate = LocalDate.now().minusDays(2);
//        LocalDateTime dayFrom = LocalDateTime.of(beforeDate.getYear(), beforeDate.getMonthValue(), beforeDate.getDayOfMonth(), 0, 0, 0);
//        LocalDateTime dayTo = LocalDateTime.of(beforeDate.getYear(), beforeDate.getMonthValue(), beforeDate.getDayOfMonth(), 23, 59, 59);
//        HashMap<String, Object> params = new HashMap<>();
//
//        params.put("searchFrom", dayFrom);
//        params.put("searchTo", dayTo);
//
//        return new JdbcPagingItemReaderBuilder<BbsEntity>()
//                .pageSize(CHUNK_SIZE)
//                .fetchSize(CHUNK_SIZE)
//                .dataSource(dataSource)
//                .rowMapper(new BeanPropertyRowMapper<>(BbsEntity.class))
//                .queryProvider(createQueryProvider())
//                .parameterValues(params)
//                .name("jdbcPagingItemReader")
//                .build();
//    }
//
//    @Bean
//    public PagingQueryProvider createQueryProvider() throws Exception {
//        SqlPagingQueryProviderFactoryBean queryProviderFactoryBean
//                = new SqlPagingQueryProviderFactoryBean();
//
//        queryProviderFactoryBean.setDatabaseType(String.valueOf(dataSource));
//        queryProviderFactoryBean.setSelectClause("SELECT category_id.name, count(category_id.name)");
//        queryProviderFactoryBean.setFromClause("FROM bbs");
//        queryProviderFactoryBean.setWhereClause("WHERE bbs_date BETWEEN :searchFrom AND :searchTo");
//        queryProviderFactoryBean.setGroupClause("GROUP BY category_id.name");
//
//        HashMap<String, Order> sortKeys = new HashMap<>();
//        sortKeys.put("category_id.name", Order.ASCENDING);
//
//        queryProviderFactoryBean.setSortKeys(sortKeys);
//        return queryProviderFactoryBean.getObject();
//    }
}