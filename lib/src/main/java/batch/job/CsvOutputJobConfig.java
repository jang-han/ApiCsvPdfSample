package batch.job;

import batch.tasklet.CsvOutputTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CsvOutputJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final CsvOutputTasklet csvOutputTasklet;

    public CsvOutputJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, CsvOutputTasklet csvOutputTasklet) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.csvOutputTasklet = csvOutputTasklet;
    }

    @Bean
    public Job csvOutputJob() {
        return jobBuilderFactory.get("csvOutputJob")
                .start(csvOutputStep())
                .build();
    }

    @Bean
    public Step csvOutputStep() {
        return stepBuilderFactory.get("csvOutputStep")
                .tasklet(csvOutputTasklet)
                .build();
    }
}
