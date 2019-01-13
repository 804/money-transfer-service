package org.eightofour.moneytransfer.app.model.entity.impl;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.eightofour.moneytransfer.app.exception.IllegalAmountException;
import org.eightofour.moneytransfer.app.model.entity.api.Account;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import java.util.Objects;

/**
 * Account entity implementation for storing.
 *
 * @author Evgeny Zubenko
 */
@Getter
@EqualsAndHashCode
public class AccountImpl implements Account {
    private static final String ILLEGAL_AMOUNT_MSG_TEMPLATE =
        "New amount for account '%s' can't be negative.";

    /**
     * Account ID.
     */
    private final String id;

    /**
     * Account money amount.
     */
    private MonetaryAmount amount;

    public AccountImpl(String id, MonetaryAmount amount) {
        Objects.requireNonNull(id);
        this.id = id;

        if (amount == null) amount = Money.of(0, "USD");
        this.amount = amount;
    }

    @Override
    public void setAmount(MonetaryAmount newAmount) {
        if (newAmount.isNegative()) {
            throw new IllegalAmountException(
                String.format(ILLEGAL_AMOUNT_MSG_TEMPLATE, id)
            );
        }
        this.amount = newAmount;
    }

    // comparision only in alphabetic order by account ID
    @Override
    @SuppressWarnings("NullableProblems")
    public int compareTo(Account account) {
        Objects.requireNonNull(account);
        return this.id.compareTo(account.getId());
    }
}