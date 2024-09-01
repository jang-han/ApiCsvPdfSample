package app;

import mapper.ExchangeRateMapper;
import model.ExchangeRate;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import response.ExchangeRates;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.FileWriter;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import pdf.PdfUtil;

public class MainApplication {

    public static void main(String[] args) {
        try {
            String urlString = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            InputStream inputStream = connection.getInputStream();

            JAXBContext jaxbContext = JAXBContext.newInstance(ExchangeRates.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            ExchangeRates exchangeRates = (ExchangeRates) unmarshaller.unmarshal(inputStream);

            // MyBatis 설정 파일 읽기
            InputStream myBatisConfig = MainApplication.class.getResourceAsStream("/mybatis-config.xml");
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(myBatisConfig);
            SqlSession session = sqlSessionFactory.openSession();
            ExchangeRateMapper mapper = session.getMapper(ExchangeRateMapper.class);

            List<ExchangeRate> exchangeRateList = new ArrayList<>();
            
            if (exchangeRates != null && exchangeRates.getCube() != null) {
                for (ExchangeRates.TimeCube timeCube : exchangeRates.getCube().getCubes()) {
                    Date date = java.sql.Date.valueOf(timeCube.getTime());
                    for (ExchangeRates.Cube cube : timeCube.getRates()) {
                        ExchangeRate rate = new ExchangeRate();
                        rate.setCurrency(cube.getCurrency());
                        rate.setRate(new BigDecimal(cube.getRate()));
                        rate.setDate(date);
                        mapper.insertExchangeRate(rate);
                        exchangeRateList.add(rate);  // CSV 파일로 저장할 리스트에 추가
                    }
                }
                session.commit();  // 변경 사항을 커밋하여 데이터베이스에 적용
            } else {
                System.out.println("환율 데이터를 가져올 수 없습니다.");
            }

            writeCsv(exchangeRateList, "exchange_rates.csv");
            
            // PDF 출력
            PdfUtil.writePdf(exchangeRateList, "exchange_rates.pdf");
            
            inputStream.close();
            session.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // CSV 파일로 데이터를 출력하는 메서드
    private static void writeCsv(List<ExchangeRate> exchangeRates, String filePath) {
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
