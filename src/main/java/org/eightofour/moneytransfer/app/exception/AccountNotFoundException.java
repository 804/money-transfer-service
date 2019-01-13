package org.eightofour.moneytransfer.app.exception;

/**
 * Business exception, which is thrown in case if account hasn't been found
 * in service storage.
 *
 * @author Evgeny Zubenko
 */
public class AccountNotFoundException extends Exception {
    private static final String MESSAGE_TEMPLATE = "Account with id '%s' isn't found";

    public AccountNotFoundException(String accountId) {
        super(String.format(MESSAGE_TEMPLATE, accountId));
    }
}