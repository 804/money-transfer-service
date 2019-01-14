package org.eightofour.moneytransfer.app.storage.api;

import java.util.Optional;

/**
 * Interface, which defines common operations for entity storing by key.
 *
 * @author Evgeny Zubenko
 */
public interface Repository<Key, Entity> {
    /**
     * Adds entity to repository by specified key.
     *
     * @param key     - key for storing
     * @param account - stored entity
     */
    void add(Key key, Entity account);

    /**
     * Gets entity by passed key.
     *
     * @param key - passed key
     *
     * @return {@link Optional} needed entity
     */
    Optional<Entity> get(Key key);
}