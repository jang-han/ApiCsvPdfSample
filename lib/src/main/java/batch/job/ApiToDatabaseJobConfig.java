package batch.job;

import batch.tasklet.ApiToDatabaseTasklet;
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
    private final ApiToDatabaseTasklet apiToDatabaseTasklet;

    public ApiToDatabaseJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, ApiToDatabaseTasklet apiToDatabaseTasklet) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.apiToDatabaseTasklet = apiToDatabaseTasklet;
    }

    @Bean
    public Job apiToDatabaseJob() {
        return jobBuilderFactory.get("apiToDatabaseJob")
                .start(apiToDatabaseStep())
                .build();
    }

    @Bean
    public Step apiToDatabaseStep() {
        return stepBuilderFactory.get("apiToDatabaseStep")
                .tasklet(apiToDatabaseTasklet)
                .build();
    }
}