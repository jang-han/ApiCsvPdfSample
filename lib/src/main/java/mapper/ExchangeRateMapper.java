package mapper;

import model.ExchangeRate;
import java.util.List;

public interface ExchangeRateMapper {

    void insertExchangeRate(ExchangeRate exchangeRate);
    List<ExchangeRate> selectAll();  // 메서드 이름을 XML 매퍼 파일의 ID와 일치시킴
}
