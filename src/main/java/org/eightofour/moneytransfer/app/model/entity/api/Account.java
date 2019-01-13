package org.eightofour.moneytransfer.app.model.entity.api;

import javax.money.MonetaryAmount;

/**
 * Interface for discount account entity.
 * For state storing.
 *
 * @author Evgeny Zubenko
 */
public interface Account extends Comparable<Account> {
    /**
     * Getter for account ID.
     *
     * @return account ID
     */
    String getId();

    /**
     * Getter for account money amount.
     *
     * @return account money amount
     */
    MonetaryAmount getAmount();

    /**
     * Setter for account money amount.
     *
     * @param newAmount - new account money amount
     */
    void setAmount(MonetaryAmount newAmount);
}