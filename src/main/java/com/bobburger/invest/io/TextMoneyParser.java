package com.bobburger.invest.io;

import com.bobburger.invest.models.Money;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.regex.Pattern;

public class TextMoneyParser {
    /**
     * Returns an {@code Optional} with the parsed currency amount, if the text
     * is a valid currency amount, otherwise returns an empty {@code Optional}.
     *
     * @param text the possibly invalid text to parse into a currency amount
     * @return an {@code Optional} with the parsed currency amount, if the text
     *         is a valid currency amount, otherwise returns an empty {@code Optional}.
     */
    public static Optional<Money> parse(String text) {
        if(isValidText(text)) {
            String[] parts = text.split("=");
            String currencyCode = parts[0];
            BigDecimal amount = new BigDecimal(parts[1]);
            return Optional.of(new Money(currencyCode, amount));
        } else {
            System.err.println(String.format("Could not parse currency amount %s", text));
            return Optional.empty();
        }
    }

    private static boolean isValidText(String text) {
        if(text == null) {
            return false;
        }
        String[] parts = text.split("=");
        return parts.length == 2 && isValidCurrencyCode(parts[0]) && isValidAmount(parts[1]);
    }

    private static boolean isValidCurrencyCode(String currencyCode) {
        return Money.CURRENCY_PATTERN.matcher(currencyCode).matches();
    }

    private static boolean isValidAmount(String amount) {
        try {
            new BigDecimal(amount);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
