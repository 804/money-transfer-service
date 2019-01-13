package org.eightofour.moneytransfer.app.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.eightofour.moneytransfer.app.model.entity.api.Account;
import org.eightofour.moneytransfer.app.storage.api.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory repository for {@link Account} storing.
 *
 * @author Evgeny Zubenko
 */
@Slf4j
public class InMemoryAccountRepository implements Repository<String, Account> {
    // in-memory thread-safe storage
    private final Map<String, Account> storage = new ConcurrentHashMap<>();

    @Override
    public void add(String key, Account account) {
        storage.put(account.getId(), account);
        log.trace(
            "New account has been added to repository: id '{}'",
            account.getId()
        );
    }

    @Override
    public Optional<Account> get(String key) {
        Account account = storage.get(key);
        if (account != null) {
            log.trace(
                "Account has been retrieved from repository: id '{}'", key
            );
        }
        return Optional.ofNullable(account);
    }
}