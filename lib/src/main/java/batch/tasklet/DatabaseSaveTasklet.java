package batch.tasklet;

import mapper.ExchangeRateMapper;
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
import response.ExchangeRates;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Date;

@Component
@StepScope
public class DatabaseSaveTasklet implements Tasklet {

    private final SqlSessionFactory sqlSessionFactory;

    public DatabaseSaveTasklet() {
        InputStream myBatisConfig = DatabaseSaveTasklet.class.getResourceAsStream("/mybatis-config.xml");
        this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(myBatisConfig);
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println("===Database Save Task Started===");

        SqlSession session = sqlSessionFactory.openSession();
        ExchangeRateMapper mapper = session.getMapper(ExchangeRateMapper.class);

        ExchangeRates exchangeRates = (ExchangeRates) chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().get("exchangeRates");

        if (exchangeRates != null && exchangeRates.getCube() != null) {
            for (ExchangeRates.TimeCube timeCube : exchangeRates.getCube().getCubes()) {
                Date date = java.sql.Date.valueOf(timeCube.getTime());
                for (ExchangeRates.Cube cube : timeCube.getRates()) {
                    ExchangeRate rate = new ExchangeRate();
                    rate.setCurrency(cube.getCurrency());
                    rate.setRate(new BigDecimal(cube.getRate()));
                    rate.setDate(date);
                    mapper.insertExchangeRate(rate);
                }
            }
            session.commit();  // 변경 사항을 커밋하여 데이터베이스에 적용
            System.out.println("데이터베이스에 저장이 완료되었습니다.");
        } else {
            System.out.println("저장할 환율 데이터가 없습니다.");
        }

        session.close();

        System.out.println("===Database Save Task Finished===");
        return RepeatStatus.FINISHED;
    }
}