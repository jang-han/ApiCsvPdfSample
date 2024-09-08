package batch.job;

import batch.tasklet.ApiCallTasklet;
import batch.tasklet.DatabaseSaveTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiToDatabaseJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ApiCallTasklet apiCallTasklet;
    private final DatabaseSaveTasklet databaseSaveTasklet;

    public ApiToDatabaseJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
                                  ApiCallTasklet apiCallTasklet, DatabaseSaveTasklet databaseSaveTasklet) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.apiCallTasklet = apiCallTasklet;
        this.databaseSaveTasklet = databaseSaveTasklet;
    }

    @Bean
    public Job apiToDatabaseJob() {
        return jobBuilderFactory.get("apiToDatabaseJob")
                .start(apiCallStep())
                .next(databaseSaveStep())
                .build();
    }

    @Bean
    public Step apiCallStep() {
        return stepBuilderFactory.get("apiCallStep")
                .tasklet(apiCallTasklet)
                .build();
    }

    @Bean
    public Step databaseSaveStep() {
        return stepBuilderFactory.get("databaseSaveStep")
                .tasklet(databaseSaveTasklet)
                .build();
    }
}
