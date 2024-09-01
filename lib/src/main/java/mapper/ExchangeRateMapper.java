package mapper;

import model.ExchangeRate;
import java.util.List;

public interface ExchangeRateMapper {

	void insertExchangeRate(ExchangeRate exchangeRate);
    List<ExchangeRate> getAllExchangeRates();
}