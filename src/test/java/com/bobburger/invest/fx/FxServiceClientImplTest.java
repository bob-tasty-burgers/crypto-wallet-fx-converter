package com.bobburger.invest.fx;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FxServiceClientImplTest {
    @Test
    void testValidResponseParsing() {
        checkParsedResponse(new BigDecimal("9110.75"), "{\"EUR\":9110.75}");
        checkParsedResponse(new BigDecimal(9110), "{\"EUR\":9110}");
        checkParsedResponse(new BigDecimal(9110), "{\"EUR\":9110.}");
        checkParsedResponse(new BigDecimal("0.2638"), "{\"EUR\":0.2638}");
    }

    private void checkParsedResponse(BigDecimal expected, String text) {
        Optional<BigDecimal> actual = FxServiceClientImpl.parseExchangeRate(text, "EUR");
        assertEquals(Optional.of(expected), actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "{\"Response\":\"Error\",\"Message\":\"There is no data for the symbol BTCCC .\",\"HasWarning\":false,\"Type\":2,\"RateLimit\":{},\"Data\":{},\"ParamWithError\":\"fsym\"}",
            "{\"Response\":\"Error\",\"Message\":\"fsym param seems to be missing.\",\"HasWarning\":false,\"Type\":2,\"RateLimit\":{},\"Data\":{},\"ParamWithError\":\"fsym\"}",
            "{INVALIDJSON:1}",
            "{\"INVALIDJSON\":2",
            "{}",
            "{\"BTC\":}",
            "{:1}",
            "{\"CurrencyWithLowerCaps\":1}",
            "{\"CURRENCY_WITH_UNDERSCORES\":1}",
            "{\"BTC\":1.2.3}",
            "{\"BTC\":NOT_A_NUMBER}",
            "{\"CHF\":1}"
    })
    @NullAndEmptySource
    void testInvalidResponseParsing(String invalidResponse) {
        Optional<BigDecimal> exchangeRate = FxServiceClientImpl.parseExchangeRate(invalidResponse, "EUR");
        assertTrue(exchangeRate.isEmpty());
    }
}
