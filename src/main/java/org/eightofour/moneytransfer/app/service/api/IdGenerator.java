package org.eightofour.moneytransfer.app.service.api;

/**
 * Interface, that defines service for unique ID generation.
 *
 * @author Evgeny Zubenko
 */
public interface IdGenerator {
    /**
     * Generate new unique ID.
     *
     * @return new unique ID
     */
    String generateId();
}