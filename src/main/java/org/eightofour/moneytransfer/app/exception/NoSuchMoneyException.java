package org.eightofour.moneytransfer.app.exception;

/**
 * Business exception, which is thrown in case if discount account
 * hasn't enough money for transfer operation performing.
 *
 * @author Evgeny Zubenko
 */
public class NoSuchMoneyException extends Exception {
    public NoSuchMoneyException(String message, Throwable cause) {
        super(message, cause);
    }
}