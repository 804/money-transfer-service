package org.eightofour.moneytransfer.app.model.factory.impl;

import lombok.extern.slf4j.Slf4j;
import org.eightofour.moneytransfer.app.model.entity.api.Account;
import org.eightofour.moneytransfer.app.model.entity.impl.AccountImpl;
import org.eightofour.moneytransfer.app.model.factory.api.SimpleFactory;
import org.eightofour.moneytransfer.app.service.api.IdGenerator;
import org.javamoney.moneta.Money;

/**
 * Implementation for simple account factory.
 *
 * @author Evgeny Zubenko
 */
@Slf4j
public class AccountFactory implements SimpleFactory<Account> {
    //
    private final IdGenerator idGenerator;

    public AccountFactory(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public Account create() {
        AccountImpl newAccount = new AccountImpl(
            idGenerator.generateId(),
            Money.of(0, "USD")
        );
        log.trace(
            "New account has been created: id - '{}'",
            newAccount.getId()
        );
        return newAccount;
    }
}