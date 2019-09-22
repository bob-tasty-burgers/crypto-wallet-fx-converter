package com.bobburger.invest;

import com.bobburger.invest.fx.FxServiceClient;
import com.bobburger.invest.fx.FxServiceClientImpl;
import com.bobburger.invest.models.Money;
import com.bobburger.invest.io.MoneyStreamFactory;
import com.bobburger.invest.io.TextFileMoneyStreamFactory;
import com.bobburger.invest.models.Pair;

import java.nio.file.InvalidPathException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        Optional<MoneyStreamFactory> moneyStreamFactoryOpt = getMoneyStreamFactory(args);
        moneyStreamFactoryOpt.ifPresent(moneyStreamFactory -> {
            FxServiceClient fxServiceClient = new FxServiceClientImpl("https://min-api.cryptocompare.com/data/price");
            // In a production application we would use a dependency injection framework (like Guice) instead of manually
            // injecting concrete implementations to create the application
            WalletEuroFxConverterApp app = new WalletEuroFxConverterApp(moneyStreamFactory, fxServiceClient, Main::printResults);
            app.run();
        });
    }

    private static void printResults(List<Pair<Money>> moneysWithEuroConversion, Money euroTotal) {
        System.out.println("Bob, here is the value of your cryptocurrencies in euros:");
        DecimalFormat df = new DecimalFormat("#,###.##");
        moneysWithEuroConversion.forEach(moneyWithEuroConversion -> {
            Money crypto = moneyWithEuroConversion.getFirst();
            Money euro = moneyWithEuroConversion.getSecond();
            System.out.println(
                    String.format("%s %s: %s %s",
                            df.format(crypto.getAmount()), crypto.getCurrencyCode(),
                            df.format(euro.getAmount()), euro.getCurrencyCode()
                    )
            );
        });
        System.out.println(String.format("Total: %s %s", df.format(euroTotal.getAmount()), euroTotal.getCurrencyCode()));
    }

    private static Optional<MoneyStreamFactory> getMoneyStreamFactory(String[] args) {
        if(args.length > 1) {
            System.err.println("At most one argument, with the crypto wallet file path, can be provided.");
            return Optional.empty();
        }
        String filePathString = args.length > 0 ? args[0] : "bobs_crypto.txt";

        try {
            return Optional.of(new TextFileMoneyStreamFactory(filePathString));
        } catch (InvalidPathException invalidPathException) {
            System.err.println("The file path is invalid.");
            return Optional.empty();
        }
    }
}
