package com.hello.springbatch.job.dbDataReadWrite;

import com.hello.springbatch.job.core.domain.account.Accounts;
import com.hello.springbatch.job.core.domain.account.AccountsRepository;
import com.hello.springbatch.job.core.domain.orders.Orders;
import com.hello.springbatch.job.core.domain.orders.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.*;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManagerFactory;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class TrMigrationConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final OrdersRepository ordersRepository;
    private final AccountsRepository accountsRepository;
    private final EntityManagerFactory managerFactory;
    @Bean
    public Job trMigrationJob(Step trMigrationStep) {
        return jobBuilderFactory.get("trMigrationJob")
                .incrementer(new RunIdIncrementer())
                .start(trMigrationStep)
                .build();
    }

    @JobScope
    @Bean
    public Step trMigrationStep(ItemReader tbOrderReader, ItemProcessor toOrderProcessor, ItemWriter toOrderWriter) {
        return stepBuilderFactory.get("trMigrationStep")
                .<Orders, Accounts> chunk(5)
                .reader(tbOrderReader)
                .processor(toOrderProcessor)
                .writer(toOrderWriter)
                .build();
    }

    @StepScope
    @Bean
    public JpaPagingItemReader<Orders> tbOrderReader() {
        return new JpaPagingItemReaderBuilder<Orders>()
                .name("tbOrderReader")
                .entityManagerFactory(managerFactory)
                .pageSize(5)
                .queryString("select o from Orders o where o.price > :price")
                .parameterValues(Collections.singletonMap("price", 10000))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Orders, Accounts> toOrderProcessor() {
        return orders -> new Accounts(orders);
    }

    @StepScope
    @Bean
    public ItemWriter<Accounts> toOrderWriter() {
        return new ItemWriter<>() {
            @Override
            public void write(List<? extends Accounts> list) throws Exception {
                accountsRepository.deleteAll();
                accountsRepository.saveAll(list);
            }
        };
        //return list -> list.forEach(account -> accountsRepository.save(account));
    }
}
