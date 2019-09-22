package com.bobburger.invest;

import com.bobburger.invest.fx.FxServiceClient;
import com.bobburger.invest.models.Money;
import com.bobburger.invest.io.MoneyStreamFactory;
import com.bobburger.invest.models.Pair;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WalletEuroFxConverterApp implements Runnable {
    private MoneyStreamFactory moneyStreamFactory;
    private FxServiceClient fxServiceClient;
    private BiConsumer<List<Pair<Money>>, Money> resultConsumer;

    public WalletEuroFxConverterApp(MoneyStreamFactory moneyStreamFactory,
                                    FxServiceClient fxServiceClient,
                                    BiConsumer<List<Pair<Money>>, Money> resultConsumer) {
        this.moneyStreamFactory = moneyStreamFactory;
        this.fxServiceClient = fxServiceClient;
        this.resultConsumer = resultConsumer;
    }

    @Override
    public void run() {
        // We want to ensure the underlying resources that might have been opened to create the moneyStream are closed
        try (Stream<Money> moneyStream = this.moneyStreamFactory.createStream()) {
            Stream<Pair<Money>> moneyWithEuroConversionStream = getMoneyWithEuroConversionStream(moneyStream);
            List<Pair<Money>> moneysWithEuroConversion = moneyWithEuroConversionStream.collect(Collectors.toList());
            Money euroTotal = getEuroTotal(moneysWithEuroConversion);
            this.resultConsumer.accept(moneysWithEuroConversion, euroTotal);
        } catch (Exception e) {
            System.err.println(String.format("An error has occurred and the fx conversion could not be completed. Exception: %s", e.toString()));
        }
    }

    private Stream<Pair<Money>> getMoneyWithEuroConversionStream(Stream<Money> moneyStream) {
        return moneyStream
                .map(this::getMoneyWithEuroConversion)
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    private Optional<Pair<Money>> getMoneyWithEuroConversion(Money money) {
        return this.fxServiceClient
                .exchangeRate(money.getCurrencyCode(), "EUR")
                // Cryptocurrencies for which no conversion could be extracted are ignored
                .map(fxRate -> new Pair<>(money, new Money("EUR", fxRate.multiply(money.getAmount()))));
    }

    private static Money getEuroTotal(List<Pair<Money>> moneysWithEuroConversion) {
        return moneysWithEuroConversion.stream()
                .map(Pair::getSecond)
                .reduce(new Money("EUR", BigDecimal.ZERO), Money::sum);
    }
}
