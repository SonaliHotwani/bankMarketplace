package com.ledger.service;

import com.ledger.model.BankStateForUser;
import com.ledger.model.BankUser;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BankOperationHandlerTest {

    @Test
    void shouldHandleBankOperationsAndUpdateBankStateForOneUser() {
        List<String> bankOperationsString = Arrays.asList(
                "LOAN IDIDI Dale 5000 1 6",
                "PAYMENT IDIDI Dale 1000 5",
                "BALANCE IDIDI Dale 3");
        final BankOperationHandler handler = new BankOperationHandler();
        handler.handle(bankOperationsString);

        final BankStateForUser actualBankState = handler.getBankStateForUsers().get(new BankUser("IDIDI", "Dale"));
        assertEquals(1, handler.getBankStateForUsers().size());
        assertEquals(6, actualBankState.getTransactions().size());
    }

    @Test
    void shouldHandleBankOperationsAndUpdateBankStateForMultipleUsers() {
        List<String> bankOperationsString = Arrays.asList(
                "LOAN IDIDI Dale 5000 1 6",
                "LOAN MBI Harry 10000 3 7",
                "PAYMENT IDIDI Dale 1000 5",
                "BALANCE MBI Harry 12",
                "BALANCE IDIDI Dale 3");
        final BankOperationHandler handler = new BankOperationHandler();
        handler.handle(bankOperationsString);

        final BankStateForUser bankStateForDale = handler.getBankStateForUsers().get(new BankUser("IDIDI", "Dale"));
        final BankStateForUser bankStateForHarry = handler.getBankStateForUsers().get(new BankUser("MBI", "Harry"));
        assertEquals(2, handler.getBankStateForUsers().size());
        assertEquals(6, bankStateForDale.getTransactions().size());
        assertEquals(13, bankStateForHarry.getTransactions().size());
    }

}
