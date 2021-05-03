package com.ledger.service;

import com.ledger.exception.InvalidBankOperationException;
import com.ledger.model.*;
import org.apache.commons.lang3.EnumUtils;

import java.math.BigDecimal;

public class BankOperationTransformer {

    BankOperation getBankOperation(String bankOperation) {
        final String[] attributes = bankOperation.split(" ");
        if (attributes.length > 3 && EnumUtils.isValidEnum(Command.class, attributes[0])) {
            Command command = Command.valueOf(attributes[0]);
            final BankUser bankUser = new BankUser(attributes[1], attributes[2]);
            switch (command) {
                case LOAN:
                    return createLoanOperation(attributes, bankUser);
                case BALANCE:
                    return createBalanceOperation(attributes, bankUser);
                case PAYMENT:
                    return createPaymentOperation(attributes, bankUser);
            }
        }
        throw new InvalidBankOperationException("Operation: " + bankOperation + " not supported");
    }

    private PaymentOperation createPaymentOperation(String[] attributes, BankUser bankUser) {
        if (attributes.length == 5)
            return new PaymentOperation(Command.PAYMENT,
                    bankUser,
                    new BigDecimal(attributes[3]).intValue(),
                    Integer.valueOf(attributes[4]));
        throw new InvalidBankOperationException("Missing information in PAYMENT Command");
    }

    private BalanceOperation createBalanceOperation(String[] attributes, BankUser bankUser) {
        return new BalanceOperation(Command.BALANCE,
                bankUser,
                Integer.valueOf(attributes[3]));
    }

    private LoanOperation createLoanOperation(String[] attributes, BankUser bankUser) {
        if (attributes.length == 6)
            return new LoanOperation(Command.LOAN,
                    bankUser,
                    new LoanDetails(new BigDecimal(attributes[3]), new BigDecimal(attributes[4]), new BigDecimal(attributes[5])));
        throw new InvalidBankOperationException("Missing information in LOAN Command");
    }
}
