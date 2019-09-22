package com.bobburger.invest.fx;

import java.math.BigDecimal;
import java.util.Optional;

public interface FxServiceClient {
    /**
     * Returns the exchange rate, when available, between the currency fromSymbol to the currency toSymbol
     * @param fromSymbol the base currency, that is the currency for which we want to know the value in terms of the quote currency
     * @param toSymbol the quote currency
     * @return an {@code Optional} with the exchange rate between the currency fromSymbol to the currency toSymbol, when a quote is
     *         provided by the FX service, otherwise returns an empty {@code Optional}.
     */
    Optional<BigDecimal> exchangeRate(String fromSymbol, String toSymbol);
}
