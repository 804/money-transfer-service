package org.eightofour.moneytransfer.app.model.entity.impl;

import org.eightofour.moneytransfer.app.exception.IllegalAmountException;
import org.eightofour.moneytransfer.app.model.entity.api.Account;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.money.MonetaryAmount;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AccountImpl testing")
class AccountImplTest {
    private static final String TEST_ID = "test-id-0";
    private static final String OTHER_TEST_ID = "test-id-1";
    private static final Money DEFAULT_AMOUNT = Money.of(0, "USD");


    @Test
    @DisplayName("Simple creation and properties' getting")
    void accountCreationTest() {
        String expectedId = TEST_ID;
        MonetaryAmount expectedAmount = DEFAULT_AMOUNT;

        Account account = new AccountImpl(expectedId, expectedAmount);

        assertEquals(
            expectedId, account.getId(),
            "Id must be the same as passed to AccountImpl constructor."
        );
        assertEquals(
            expectedAmount, account.getAmount(),
            "Amount must be the same as passed to AccountImpl constructor."
        );
    }

    @Test
    @DisplayName("Creation with null ID")
    void accountCreationIncorrectIdTest() {
        assertThrows(
            NullPointerException.class,
            () -> new AccountImpl(null, DEFAULT_AMOUNT),
            "Constructor calling with null ID must throw NullPointerException."
        );
    }

    @Test
    @DisplayName("Creation with null amount")
    void accountCreationIncorrectAmountTest() {
        Account account = new AccountImpl(TEST_ID, null);

        assertEquals(
                DEFAULT_AMOUNT, account.getAmount(),
            "Id must be the same as passed to AccountImpl constructor."
        );
    }

    @Test
    @DisplayName("Amount setting with correct value")
    void setAmountTest() {
        MonetaryAmount unexpectedAmount = Money.of(1, "USD");
        Account account = new AccountImpl(TEST_ID, unexpectedAmount);

        Money expectedAmount = Money.of(2, "USD");
        account.setAmount(expectedAmount);

        assertNotEquals(
            unexpectedAmount, account.getAmount(),
            "Amount mustn't be the same as passed to AccountImpl constructor."
        );
        assertEquals(
            expectedAmount, account.getAmount(),
            "Amount must be the same as passed to amount setter."
        );
    }

    @Test
    @DisplayName("Amount setter with incorrect value")
    void setIncorrectAmountTest() {
        Account account = new AccountImpl(TEST_ID, DEFAULT_AMOUNT);

        assertThrows(
            IllegalAmountException.class,
            () -> account.setAmount(Money.of(-1, "USD")),
            "Amount setter must throw exception with negative new amount."
        );
    }

    @Test
    @DisplayName("Amount 'less'-comparision testing")
    void alphabeticalOrderCompareToTestLess() {
        Account account = new AccountImpl(TEST_ID, DEFAULT_AMOUNT);
        Account otherAccount = new AccountImpl(OTHER_TEST_ID, DEFAULT_AMOUNT);

        assertTrue(
            account.compareTo(otherAccount) < 0,
            "Incorrect alphabetic order has been implemented."
        );
    }

    @Test
    @DisplayName("Amount 'bigger'-comparision testing")
    void alphabeticalOrderCompareToTestBigger() {
        Account account = new AccountImpl(TEST_ID, DEFAULT_AMOUNT);
        Account otherAccount = new AccountImpl(OTHER_TEST_ID, DEFAULT_AMOUNT);

        assertTrue(
            otherAccount.compareTo(account) > 0,
            "Incorrect alphabetic order has been implemented."
        );
    }

    @Test
    @DisplayName("Amount 'equals'-comparision testing")
    void alphabeticalOrderCompareToTestEquals() {
        Account account = new AccountImpl(TEST_ID, DEFAULT_AMOUNT);
        Account otherAccount = new AccountImpl(TEST_ID, DEFAULT_AMOUNT);

        assertEquals(
            0, account.compareTo(otherAccount),
            "Incorrect alphabetic order has been implemented."
        );
        assertEquals(
            0, otherAccount.compareTo(account),
            "Incorrect alphabetic order has been implemented."
        );
    }
}