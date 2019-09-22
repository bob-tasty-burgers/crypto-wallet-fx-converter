package com.bobburger.invest.models;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.regex.Pattern;

public class Money {
   /*
    In a production application we should validate the currency code matches an existing currency code
    We don't use java.util.Currency as the type for currency because it doesn't include cryptocurrencies,
    at least not under codes like BTC, ETH ...
     */
    public static final Pattern CURRENCY_PATTERN = Pattern.compile("[A-Z]+");

    private String currencyCode;
    private BigDecimal amount;

    public Money(String currencyCode, BigDecimal amount) {
        if(currencyCode == null || amount == null || !CURRENCY_PATTERN.matcher(currencyCode).matches()) {
            throw new IllegalArgumentException("The currencyCode and amount can not be null");
        }
        this.currencyCode = currencyCode;
        this.amount = amount;
    }

    public static Money sum(Money a, Money b) throws IllegalArgumentException {
        if(a == null || b == null || !a.currencyCode.equals(b.currencyCode)) {
            throw new IllegalArgumentException("Can only sum moneys with the same (non-null) currency code and non-null amounts");
        }
        return new Money(a.currencyCode, a.amount.add(b.amount));
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money that = (Money) o;
        return Objects.equals(currencyCode, that.currencyCode) &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currencyCode, amount);
    }

    @Override
    public String toString() {
        return "Money{" +
                "currencyCode='" + currencyCode + '\'' +
                ", amount=" + amount +
                '}';
    }
}
