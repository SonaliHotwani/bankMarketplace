package com.ledger;

import com.ledger.exception.InvalidBankOperationException;
import com.ledger.service.BankOperationHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class LedgerCoMarketplace {

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("Test input not provided");
            System.exit(0);
        }

        try {
            final List<String> lines = Files.readAllLines(Paths.get(args[0]));
            final List<String> bankOperations = lines.stream().filter(line -> !line.isEmpty()).collect(Collectors.toList());
            new BankOperationHandler().handle(bankOperations);
        } catch (InvalidBankOperationException exception) {
            System.err.println(exception.getMessage());
        }
    }
}
