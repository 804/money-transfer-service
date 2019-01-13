package org.eightofour.moneytransfer.app.model.entity.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eightofour.moneytransfer.app.model.entity.api.Account;
import org.eightofour.moneytransfer.app.model.entity.api.AccountView;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.eightofour.moneytransfer.util.ResourceUtil.getResourceAsString;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Account view testing")
class AccountViewImplTest {
    private static final Money DEFAULT_AMOUNT = Money.of(1, "USD");
    private static final String TEST_ID = "test";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private AccountView accountView;

    @BeforeEach
    void init() {
        Account account = new AccountImpl(TEST_ID, DEFAULT_AMOUNT);
        this.accountView = AccountViewImpl.from(account);
    }

    @Test
    void testFactoryMethod() {
        assertEquals(
            TEST_ID, accountView.getId(),
            "ID from view must be the same as in original account"
        );
        assertEquals(
            DEFAULT_AMOUNT, accountView.getAmount(),
            "Money amount from view must be the same as in original account"
        );
    }

    @Test
    @DisplayName("Account view serialization")
    void testViewSerialization() throws IOException {
        String serializedAccountView = OBJECT_MAPPER.writeValueAsString(accountView);
        String expectedSerializedAccountView = getResourceAsString(getClass(), "account.json");

        assertEquals(
            expectedSerializedAccountView,
            serializedAccountView,
            "Serialization was performed incorrectly."
        );
    }
}