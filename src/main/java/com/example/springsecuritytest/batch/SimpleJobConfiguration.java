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
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SimpleJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

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
//    // @StepScope는 tasklet이나 ItemReader, ItemWriter, ItemProcessor에서 사용 가능.
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

        return new JpaPagingItemReaderBuilder<BbsEntity>()
                .name("jpaPagingItemReader")
                .entityManagerFactory(entityManagerFactory).pageSize(CHUNK_SIZE)
                .parameterValues(params)
                .queryString("SELECT categoryId.name FROM BbsEntity WHERE bbs_date between :searchFrom AND :searchTo")
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<BbsEntity, BatchResult> jpaItemProcessor() {
        log.info(">>>>>>> Processor");
        return bbsEntity -> {
            log.info(String.valueOf(bbsEntity));
            return BatchResult.builder()
                    .name(bbsEntity.getCategoryId().getName())
                    .bbsCount(0)
                    .build();
        };
    }

    @Bean
    @StepScope
    public JpaItemWriter<BatchResult> writer() {
        JpaItemWriter<BatchResult> jpaItemWriter = new JpaItemWriter<BatchResult>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }
//    @Bean
//    @StepScope
//    public JdbcPagingItemReader<BbsEntity> reader() throws Exception {
//        LocalDate beforeDate = LocalDate.now().minusDays(1);
//        LocalDateTime dayFrom = LocalDateTime.of(beforeDate.getYear(), beforeDate.getMonthValue(), beforeDate.getDayOfMonth(), 0, 0, 0);
//        LocalDateTime dayTo = LocalDateTime.of(beforeDate.getYear(), beforeDate.getMonthValue(), beforeDate.getDayOfMonth(), 23, 59, 59);
//        HashMap<String, Object> params = new HashMap<>();
//
//        params.put("searchFrom", dayFrom);
//        params.put("searchTo", dayTo);
////        JdbcPagingItemReader<BbsEntity> jdbcPagingItemReader = new JdbcPagingItemReader<>();
////        jdbcPagingItemReader.setName("bbsEntity_Reader");
////        jdbcPagingItemReader.setPageSize(CHUNK_SIZE);
////        jdbcPagingItemReader.setParameterValues(params);
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

//    @Bean
//    public PagingQueryProvider createQueryProvider() throws Exception {
//        SqlPagingQueryProviderFactoryBean queryProviderFactoryBean
//                = new SqlPagingQueryProviderFactoryBean();
//
//        queryProviderFactoryBean.setDatabaseType(String.valueOf(dataSource));
//        queryProviderFactoryBean.setSelectClause("select category_id.name, count(category_id.name)");
//        queryProviderFactoryBean.setFromClause("from bbs");
//        queryProviderFactoryBean.setWhereClause("where bbs_date between :searchFrom and :searchTo");
//        queryProviderFactoryBean.setGroupClause("group by category_id.name");
//
//        HashMap<String, Order> sortKeys = new HashMap<>();
//        sortKeys.put("category_id.name", Order.ASCENDING);
//
//        queryProviderFactoryBean.setSortKeys(sortKeys);
//        return queryProviderFactoryBean.getObject();
//    }
}