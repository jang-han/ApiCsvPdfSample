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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Component
@StepScope
public class ApiToDatabaseTasklet implements Tasklet {

    private final SqlSessionFactory sqlSessionFactory;

    public ApiToDatabaseTasklet() {
        // MyBatis 설정 파일 읽기
        InputStream myBatisConfig = ApiToDatabaseTasklet.class.getResourceAsStream("/mybatis-config.xml");
        this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(myBatisConfig);
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println("===API to Database Job Started===");

        String urlString = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        InputStream inputStream = connection.getInputStream();

        JAXBContext jaxbContext = JAXBContext.newInstance(ExchangeRates.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        ExchangeRates exchangeRates = (ExchangeRates) unmarshaller.unmarshal(inputStream);

        SqlSession session = sqlSessionFactory.openSession();
        ExchangeRateMapper mapper = session.getMapper(ExchangeRateMapper.class);

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
        } else {
            System.out.println("환율 데이터를 가져올 수 없습니다.");
        }

        inputStream.close();
        session.close();

        System.out.println("===API to Database Job Finished===");
        return RepeatStatus.FINISHED;
    }
}
