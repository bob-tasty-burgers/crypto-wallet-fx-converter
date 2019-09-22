package com.bobburger.invest.models;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MoneyTest {
    @Test
    void testCanNotCreateInvalidMoney() {
        assertThrows(IllegalArgumentException.class, () -> new Money(null, new BigDecimal(1)));
        assertThrows(IllegalArgumentException.class, () -> new Money("EUR", null));
        assertThrows(IllegalArgumentException.class, () -> new Money("currencyWithLowerCaps", new BigDecimal(1)));
        assertThrows(IllegalArgumentException.class, () -> new Money("", new BigDecimal(1)));
    }

    @Test
    void testValidSum() {
        assertEquals(
                new Money("EUR", new BigDecimal("32.143")),
                Money.sum(new Money("EUR", new BigDecimal(31)),
                          new Money("EUR", new BigDecimal("1.143"))));
        assertEquals(
                new Money("CHF", new BigDecimal("4.52")),
                Money.sum(new Money("CHF", new BigDecimal("0.02")),
                          new Money("CHF", new BigDecimal("4.5"))));
    }

    @Test
    void testInvalidSum() {
        checkInvalidSum(
                null,
                null);
        checkInvalidSum(
                new Money("EUR", new BigDecimal(31)),
                null);
        checkInvalidSum(
                null,
                new Money("EUR", new BigDecimal(31)));
        checkInvalidSum(
                new Money("EUR", new BigDecimal(1)),
                new Money("CHF", new BigDecimal(2)));
    }

    private void checkInvalidSum(Money a, Money b) {
        assertThrows(IllegalArgumentException.class, () -> Money.sum(a, b));
    }
}
