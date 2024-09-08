package batch.tasklet;

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
import java.net.HttpURLConnection;
import java.net.URL;

@Component
@StepScope
public class ApiCallTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println("===API Call Task Started===");

        String urlString = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
        HttpURLConnection connection = null;

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            try (InputStream inputStream = connection.getInputStream()) {
            	// JAXB를 사용하여 XML 데이터를 ExchangeRates 객체로 언마샬링
                JAXBContext jaxbContext = JAXBContext.newInstance(ExchangeRates.class);
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                ExchangeRates exchangeRates = (ExchangeRates) unmarshaller.unmarshal(inputStream);

                // 데이터 처리를 여기서 수행
                System.out.println("Data successfully unmarshalled: " + exchangeRates);
            }
        } catch (Exception e) {
            System.out.println("Error during API call: " + e.getMessage());
            throw e;  // 예외를 재발생시켜 테스트에서 확인 가능하도록 함
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        System.out.println("===API Call Task Finished===");
        return RepeatStatus.FINISHED;
    }
}
