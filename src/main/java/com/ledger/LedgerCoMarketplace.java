package com.ledger;

import com.ledger.service.BankOperationHandler;
import com.ledger.service.BankOperationTransformer;

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
        final List<String> lines = Files.readAllLines(Paths.get(args[0]));
        final List<String> bankOperations = lines.stream().filter(line -> !line.isEmpty()).collect(Collectors.toList());
        new BankOperationHandler(new BankOperationTransformer()).handle(bankOperations);
    }
}
