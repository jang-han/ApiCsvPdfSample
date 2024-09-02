package batch.tasklet;

import model.ExchangeRate;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import pdf.PdfUtil;

import java.io.InputStream;
import java.util.List;

@Component
@StepScope
public class PdfOutputTasklet implements Tasklet {

    private final SqlSessionFactory sqlSessionFactory;

    public PdfOutputTasklet() {
        // MyBatis 설정 파일 읽기
        InputStream myBatisConfig = PdfOutputTasklet.class.getResourceAsStream("/mybatis-config.xml");
        this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(myBatisConfig);
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println("===PDF Output Job Started===");

        SqlSession session = sqlSessionFactory.openSession();
        List<ExchangeRate> exchangeRates = session.selectList("mapper.ExchangeRateMapper.selectAll");

        PdfUtil.writePdf(exchangeRates, "exchange_rates.pdf");

        session.close();
        System.out.println("===PDF Output Job Finished===");
        return RepeatStatus.FINISHED;
    }
}
