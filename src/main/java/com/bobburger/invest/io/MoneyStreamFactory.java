package com.bobburger.invest.io;

import com.bobburger.invest.models.Money;

import java.util.stream.Stream;

public interface MoneyStreamFactory {
    Stream<Money> createStream() throws Exception;
}
