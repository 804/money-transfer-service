package org.eightofour.moneytransfer.app.model.entity.api;

import javax.money.MonetaryAmount;

/**
 * Interface for state view of discount account.
 * It's needed for state capturing.
 *
 * @author Evgeny Zubenko
 */
public interface AccountView {
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
}