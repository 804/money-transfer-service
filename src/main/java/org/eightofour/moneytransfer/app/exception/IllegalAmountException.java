package org.eightofour.moneytransfer.app.exception;

/**
 * Business exception, which is thrown in case if money amount
 * has illegal for current case state.
 *
 * @author Evgeny Zubenko
 */
public class IllegalAmountException extends RuntimeException {
    public IllegalAmountException(String message) {
        super(message);
    }
}