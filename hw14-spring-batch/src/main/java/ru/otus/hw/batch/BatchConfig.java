package ru.otus.hw.batch;

import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.models.db.Author;
import ru.otus.hw.models.db.Book;
import ru.otus.hw.models.db.Comment;
import ru.otus.hw.models.db.Genre;
import ru.otus.hw.models.mongo.MongoAuthor;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.models.mongo.MongoComment;
import ru.otus.hw.models.mongo.MongoGenre;

@Slf4j
@Configuration
public class BatchConfig {
    public static final String TRANSFER_JOB_NAME = "transferJob";

    private static final int CHUNK_SIZE = 5;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Bean
    public JpaPagingItemReader<Author> authorReader(EntityManagerFactory entityManagerFactory) {
        log.info("Creating author reader");
        return new JpaPagingItemReaderBuilder<Author>()
                .name("authorReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select a from Author a")
                .pageSize(1000)
                .build();
    }

    @Bean
    public JpaPagingItemReader<Genre> genreReader(EntityManagerFactory entityManagerFactory) {
        log.info("Creating genre reader");
        return new JpaPagingItemReaderBuilder<Genre>()
                .name("genreReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select a from Genre a")
                .pageSize(1000)
                .build();
    }

    @Bean
    public JpaPagingItemReader<Book> bookReader(EntityManagerFactory entityManagerFactory) {
        log.info("Create book reader");
        return new JpaPagingItemReaderBuilder<Book>()
                .name("bookReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select b from Book b")
                .pageSize(1000)
                .build();
    }

    @Bean
    public JpaPagingItemReader<Comment> commentReader(EntityManagerFactory entityManagerFactory) {
        log.info("Create comment reader");
        return new JpaPagingItemReaderBuilder<Comment>()
                .name("commentReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select c from Comment c")
                .pageSize(1000)
                .build();
    }

    @Bean
    public MongoItemWriter<MongoAuthor> authorWriter(MongoTemplate template) {
        log.info("Creating author writer");
        return new MongoItemWriterBuilder<MongoAuthor>()
                .template(template)
                .build();
    }

    @Bean
    public MongoItemWriter<MongoGenre> genreWriter(MongoTemplate template) {
        log.info("Creating genre writer");
        return new MongoItemWriterBuilder<MongoGenre>()
                .template(template)
                .build();
    }

    @Bean
    public MongoItemWriter<MongoBook> bookWriter(MongoTemplate template) {
        log.info("Creating book writer");
        return new MongoItemWriterBuilder<MongoBook>()
                .template(template)
                .build();
    }

    @Bean
    public MongoItemWriter<MongoComment> commentWriter(MongoTemplate template) {
        log.info("Creating comment writer");
        return new MongoItemWriterBuilder<MongoComment>()
                .template(template)
                .build();
    }

    @Bean
    public Step transferAuthorsStep(
            ItemReader<Author> reader,
            MongoItemWriter<MongoAuthor> writer,
            ItemProcessor<Author, MongoAuthor> itemProcessor
    ) {
        return new StepBuilder("transferAuthorsStep", jobRepository)
                .<Author, MongoAuthor>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .listener(getAuthorWriteListener())
                .build();
    }

    @Bean
    public Step transferGenresStep(
            ItemReader<Genre> reader,
            MongoItemWriter<MongoGenre> writer,
            ItemProcessor<Genre, MongoGenre> itemProcessor
    ) {
        return new StepBuilder("transferGenresStep", jobRepository)
                .<Genre, MongoGenre>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .listener(getGenreWriteListener())
                .build();
    }

    @Bean
    public Step transferBooksStep(
            ItemReader<Book> reader,
            MongoItemWriter<MongoBook> writer,
            ItemProcessor<Book, MongoBook> itemProcessor
    ) {
        return new StepBuilder("transferBooksStep", jobRepository)
                .<Book, MongoBook>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step transferCommentsStep(
            ItemReader<Comment> reader,
            MongoItemWriter<MongoComment> writer,
            ItemProcessor<Comment, MongoComment> itemProcessor
    ) {
        return new StepBuilder("transferCommentsStep", jobRepository)
                .<Comment, MongoComment>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job transferJob(
            Step transferAuthorsStep,
            Step transferGenresStep,
            Step transferBooksStep,
            Step transferCommentsStep
    ) {
        return new JobBuilder(TRANSFER_JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(transferAuthorsStep)
                .next(transferGenresStep)
                .next(transferBooksStep)
                .next(transferCommentsStep)
                .end()
                .listener(getJobExecutionListener())
                .build();
    }

    private JobExecutionListener getJobExecutionListener() {
        return new JobExecutionListener() {
            @Override
            public void beforeJob(JobExecution jobExecution) {
                log.info("Start job");
                JobExecutionListener.super.beforeJob(jobExecution);
            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                log.info("Finish job");
                JobExecutionListener.super.afterJob(jobExecution);
            }
        };
    }

    private ItemWriteListener<MongoAuthor> getAuthorWriteListener() {
        return new ItemWriteListener<>() {
            @Override
            public void afterWrite(Chunk<? extends MongoAuthor> items) {
                items.forEach(item -> log.info("Author written - {}", item));
                ItemWriteListener.super.afterWrite(items);
            }
        };
    }

    private ItemWriteListener<MongoGenre> getGenreWriteListener() {
        return new ItemWriteListener<>() {
            @Override
            public void afterWrite(Chunk<? extends MongoGenre> items) {
                items.forEach(item -> log.info("Genre written - {}", item));
                ItemWriteListener.super.afterWrite(items);
            }
        };
    }
}
