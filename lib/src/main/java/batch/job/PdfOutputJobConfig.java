package batch.job;

import batch.tasklet.PdfOutputTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PdfOutputJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final PdfOutputTasklet pdfOutputTasklet;

    public PdfOutputJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, PdfOutputTasklet pdfOutputTasklet) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.pdfOutputTasklet = pdfOutputTasklet;
    }

    @Bean
    public Job pdfOutputJob() {
        return jobBuilderFactory.get("pdfOutputJob")
                .start(pdfOutputStep())
                .build();
    }

    @Bean
    public Step pdfOutputStep() {
        return stepBuilderFactory.get("pdfOutputStep")
                .tasklet(pdfOutputTasklet)
                .build();
    }
}
