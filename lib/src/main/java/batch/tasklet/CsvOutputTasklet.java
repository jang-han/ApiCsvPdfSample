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

import java.io.InputStream;
import java.io.FileWriter;
import java.util.List;

@Component
@StepScope
public class CsvOutputTasklet implements Tasklet {

    private final SqlSessionFactory sqlSessionFactory;

    public CsvOutputTasklet() {
        // MyBatis 설정 파일 읽기
        InputStream myBatisConfig = CsvOutputTasklet.class.getResourceAsStream("/mybatis-config.xml");
        this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(myBatisConfig);
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println("===CSV Output Job Started===");

        SqlSession session = sqlSessionFactory.openSession();
        List<ExchangeRate> exchangeRates = session.selectList("mapper.ExchangeRateMapper.selectAll");

        writeCsv(exchangeRates, "exchange_rates.csv");

        session.close();
        System.out.println("===CSV Output Job Finished===");
        return RepeatStatus.FINISHED;
    }

    private void writeCsv(List<ExchangeRate> exchangeRates, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("Currency,Rate,Date\n");
            for (ExchangeRate rate : exchangeRates) {
                writer.append(rate.getCurrency()).append(',')
                        .append(rate.getRate().toString()).append(',')
                        .append(rate.getDate().toString()).append('\n');
            }
            System.out.println("CSV 파일이 생성되었습니다: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
