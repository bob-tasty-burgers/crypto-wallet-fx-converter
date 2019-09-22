package com.bobburger.invest.io;

import com.bobburger.invest.models.Money;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

public class TextFileMoneyStreamFactory implements MoneyStreamFactory {
    Path filePath;

    public TextFileMoneyStreamFactory(String filePathString) {
        this.filePath = Paths.get(filePathString);
    }

    @Override
    public Stream<Money> createStream() throws Exception {
        try {
            return Files.lines(filePath)
                    .map(TextMoneyParser::parse)
                    .filter(Optional::isPresent)
                    .map(Optional::get);
        } catch (IOException ioException) {
            System.err.println("There was an error reading the file with the currency amounts.");
            throw ioException;
        }
    }
}