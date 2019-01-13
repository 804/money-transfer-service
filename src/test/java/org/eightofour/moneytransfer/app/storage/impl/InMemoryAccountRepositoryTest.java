package org.eightofour.moneytransfer.app.storage.impl;

import org.eightofour.moneytransfer.app.model.entity.api.Account;
import org.eightofour.moneytransfer.app.model.entity.impl.AccountImpl;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("In-memory account repository testing")
class InMemoryAccountRepositoryTest {
    private static final String TEST_ID = "test-id";
    private InMemoryAccountRepository repository;

    @BeforeEach
    void init() {
        this.repository = new InMemoryAccountRepository();
    }

    @Test
    @DisplayName("Common 'add-get' case testing")
    void testStore() {
        Account account = new AccountImpl(TEST_ID, Money.of(0, "USD"));

        repository.add(account.getId(), account);
        Optional<Account> optionalAccount = repository.get(account.getId());

        assertTrue(
            optionalAccount.isPresent(),
            "Got optional must contain account"
        );
        assertEquals(
            account, optionalAccount.get(),
            "Got optional must contain the same account as account, which was stored"
        );
    }

    @Test
    @DisplayName("'Account-not-found' case testing")
    void testNotFound() {
        Optional<Account> optionalAccount = repository.get(TEST_ID);
        assertFalse(
            optionalAccount.isPresent(),
            "Got optional mustn't contain account"
        );
    }
}