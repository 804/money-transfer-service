package org.eightofour.moneytransfer.app.model.factory.impl;

import org.eightofour.moneytransfer.app.model.entity.api.Account;
import org.eightofour.moneytransfer.app.model.entity.impl.AccountImpl;
import org.eightofour.moneytransfer.app.model.factory.api.SimpleFactory;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Account Factory test")
class AccountFactoryTest {
    private static final String TEST_ID = "test-id";
    private static final Money DEFAULT_AMOUNT = Money.of(0, "USD");

    @Test
    @DisplayName("Main factory method testing")
    void testCreate() {
        SimpleFactory<Account> factory = new AccountFactory(() -> TEST_ID);
        Account account = factory.create();

        Account expectedAccount = new AccountImpl(TEST_ID, DEFAULT_AMOUNT);

        assertEquals(
            expectedAccount, account,
            "Created by factory account must be as expected one"
        );
    }
}