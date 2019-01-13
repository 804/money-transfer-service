package org.eightofour.moneytransfer.app.service.api;

import org.eightofour.moneytransfer.app.exception.AccountNotFoundException;
import org.eightofour.moneytransfer.app.exception.NoSuchMoneyException;
import org.eightofour.moneytransfer.app.model.entity.api.AccountView;

import javax.money.MonetaryAmount;
import java.util.Optional;

/**
 * Interface for working with deposit accounts, which defines
 * common operations for accounts creating and further modifications.
 *
 * @author Evgeny Zubenko
 */
public interface AccountService {
    /**
     * Method for creation new empty deposit account.
     *
     * @return view for new account
     */
    AccountView createAccount();

    /**
     * Method for getting current state of deposit account by passed ID.
     *
     * @param accountId - passed account ID
     *
     * @return {@link Optional} for state view for needed account
     */
    Optional<AccountView> getAccount(String accountId);

    /**
     * Method for performing the recharging operation for specified account.
     *
     * @param accountId - account ID for recharging
     * @param amount    - money amount for recharging
     *
     * @throws AccountNotFoundException if specified account doesn't exist
     */
    void rechargeAccount(String accountId, MonetaryAmount amount)
            throws AccountNotFoundException;

    /**
     * Method for performing the transfer operation between specified accounts.
     *
     * @param fromAccountId - source account ID for transfer
     * @param toAccountId   - account account ID for transfer
     * @param amount        - money amount for transfer
     *
     * @throws AccountNotFoundException if one (or both) of specified accounts
     *                                  doesn't exist
     * @throws NoSuchMoneyException if source account hasn't enough money
     *                              for transfer
     */
    void transfer(String fromAccountId, String toAccountId, MonetaryAmount amount)
            throws AccountNotFoundException, NoSuchMoneyException;
}