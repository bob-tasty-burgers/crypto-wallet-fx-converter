package com.bobburger.invest.io;

import com.bobburger.invest.models.Money;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TextFileMoneyStreamFactoryTest {
    /*
    The file crypto_wallet.txt contains a mix of valid and invalid lines. The valid lines should be correctly parsed,
    whereas the invalid lines should be ignored.
     */
    @Test
    void testFileParsing() throws Exception {
        String filePathString = TextFileMoneyStreamFactoryTest.class.getClassLoader().getResource("crypto_wallet.txt").getPath();
        MoneyStreamFactory moneyStreamFactory = new TextFileMoneyStreamFactory(filePathString);
        try (Stream<Money> moneyStream = moneyStreamFactory.createStream()){
            List<Money> moneyList = moneyStream.collect(Collectors.toList());
            assertEquals(4, moneyList.size());
            assertEquals(new Money("BTC", new BigDecimal("10.25")), moneyList.get(0));
            assertEquals(new Money("ETH", new BigDecimal(5)), moneyList.get(1));
            assertEquals(new Money("XRP", new BigDecimal(2000)), moneyList.get(2));
            assertEquals(new Money("LTC", new BigDecimal(20)), moneyList.get(3));
        }
    }

    @Test
    void testNonExistentPathProducesException() {
        assertThrows(IOException.class, () -> new TextFileMoneyStreamFactory("non_existing_path").createStream());
    }
}
