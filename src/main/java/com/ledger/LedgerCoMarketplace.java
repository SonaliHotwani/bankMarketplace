package com.ledger;

import com.ledger.service.BankOperationsHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class LedgerCoMarketplace {

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("Test input not provided");
            System.exit(0);
        }

        final List<String> bankOperations = Files.readAllLines(Paths.get("/Users/admin/Documents/geektrustProblems/src/main/resources/BankOperations.txt"));

        new BankOperationsHandler().handle(bankOperations);
    }
}
