package com.bobburger.invest.fx;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

public class FxServiceClientMock implements FxServiceClient {
    Map<String, BigDecimal> toEuroExchangeRates;

    public FxServiceClientMock(Map<String, BigDecimal> toEuroExchangeRates) {
        this.toEuroExchangeRates = toEuroExchangeRates;
    }

    @Override
    public Optional<BigDecimal> exchangeRate(String fromSymbol, String toSymbol) {
        if("EUR".equals(toSymbol) && fromSymbol != null) {
            return Optional.ofNullable(toEuroExchangeRates.get(fromSymbol));
        } else {
            return Optional.empty();
        }
    }
}