package com.bobburger.invest.io;

import com.bobburger.invest.models.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TextMoneyParserTest {
    @Test
    void testValidText() {
        checkParsedText(new Money("BTC", new BigDecimal(10)), "BTC=10");
        checkParsedText(new Money("ETH", new BigDecimal(5)), "ETH=5");
        checkParsedText(new Money("XRP", new BigDecimal(2000)), "XRP=2000");
        checkParsedText(new Money("BTC", new BigDecimal("2.01")), "BTC=2.01");
        checkParsedText(new Money("ETH", new BigDecimal(5)), "ETH=5.");
    }

    private void checkParsedText(Money expected, String text) {
        Optional<Money> parsedMoney = TextMoneyParser.parse(text);
        assertEquals(Optional.of(expected), parsedMoney);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "NO_EQUALS_SIGN",
            "==",
            "=",
            "BTC=",
            "=1",
            "lowerCapsCurrency=10",
            "CURRENCY_WITH_UNDERSCORES=10",
            "BTC=1.2.3",
            "BTC=NOT_A_NUMBER"
    })
    @NullAndEmptySource
    void testInvalidText(String invalidText) {
        assertTrue(TextMoneyParser.parse(invalidText).isEmpty());
    }
}
