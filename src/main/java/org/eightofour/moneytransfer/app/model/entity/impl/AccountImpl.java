package org.eightofour.moneytransfer.app.model.entity.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.eightofour.moneytransfer.app.exception.IllegalAmountException;
import org.eightofour.moneytransfer.app.model.entity.api.Account;

import javax.money.MonetaryAmount;
import java.util.Objects;

/**
 * Account entity implementation for storing.
 *
 * @author Evgeny Zubenko
 */
@Getter
@AllArgsConstructor
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

    @Override
    public void setAmount(MonetaryAmount newAmount) {
        if (newAmount.isNegative()) {
            throw new IllegalAmountException(
                String.format(ILLEGAL_AMOUNT_MSG_TEMPLATE, id)
            );
        }
        this.amount = newAmount;
    }

    @Override
    @SuppressWarnings("all")
    public int compareTo(Account account) {
        Objects.requireNonNull(account);
        return this.id.compareTo(account.getId());
    }
}