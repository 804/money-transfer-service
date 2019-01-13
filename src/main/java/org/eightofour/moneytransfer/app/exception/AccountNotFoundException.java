package org.eightofour.moneytransfer.app.exception;

/**
 * Business exception, which is thrown in case if account hasn't been found
 * in service storage.
 *
 * @author Evgeny Zubenko
 */
public class AccountNotFoundException extends Exception {
    public AccountNotFoundException(String accountId) {
        super("Account with id '" + accountId + "' isn't found");
    }
}