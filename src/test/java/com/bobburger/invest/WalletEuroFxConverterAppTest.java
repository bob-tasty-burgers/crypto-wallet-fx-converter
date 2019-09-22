package com.bobburger.invest;

import com.bobburger.invest.fx.FxServiceClient;
import com.bobburger.invest.fx.FxServiceClientMock;
import com.bobburger.invest.io.MoneyStreamFactory;
import com.bobburger.invest.io.TextFileMoneyStreamFactory;
import com.bobburger.invest.models.Money;
import com.bobburger.invest.models.Pair;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WalletEuroFxConverterAppTest {
    /*
    The file crypto_wallet.txt contains a mix of valid and invalid lines. The valid lines should be correctly parsed,
    whereas the invalid lines should be ignored. Additionally, the mocked FX service client doesn't have an exchange rate
    for LTC; we should therefore not see any EUR conversion in the output for LTC.
     */
    @Test
    void testApp() {
        createApp("crypto_wallet.txt", this::checkTestResults).run();
    }

    private void checkTestResults(List<Pair<Money>> moneysWithEuroConversion, Money euroTotal) {
        assertEquals(3, moneysWithEuroConversion.size());
        assertEquals(moneyWithEuroConversion("BTC", "10.25", "93385.1875"), moneysWithEuroConversion.get(0));
        assertEquals(moneyWithEuroConversion("ETH", "5", "989.20"), moneysWithEuroConversion.get(1));
        assertEquals(moneyWithEuroConversion("XRP", "2000", "527.6000"), moneysWithEuroConversion.get(2));
        assertEquals(new Money("EUR", new BigDecimal("94901.9875")), euroTotal);
    }

    private Pair<Money> moneyWithEuroConversion(String baseCurrency, String baseCurrencyAmount, String euroAmount) {
        return new Pair<>(
                new Money(baseCurrency, new BigDecimal(baseCurrencyAmount)),
                new Money("EUR", new BigDecimal(euroAmount)));
    }

    private WalletEuroFxConverterApp createApp(String walletFilePath, BiConsumer<List<Pair<Money>>, Money> resultConsumer) {
        String filePathString = WalletEuroFxConverterApp.class.getClassLoader().getResource(walletFilePath).getPath();
        MoneyStreamFactory moneyStreamFactory = new TextFileMoneyStreamFactory(filePathString);
        // Only interface where we need to use a mocked implementation to get predictable results
        FxServiceClient fxServiceClient = getMockedFxServiceClient();
        return new WalletEuroFxConverterApp(moneyStreamFactory, fxServiceClient, resultConsumer);
    }

    private FxServiceClient getMockedFxServiceClient() {
        Map<String, BigDecimal> toEuroExchangeRates = Map.of(
                "BTC", new BigDecimal("9110.75"),
                "ETH", new BigDecimal("197.84"),
                "XRP", new BigDecimal("0.2638")
        );
        return new FxServiceClientMock(toEuroExchangeRates);
    }
}