package org.eightofour.moneytransfer.app.model.factory.api;

/**
 * Interface for factory for creating entity without parameters.
 *
 * @author Evgeny Zubenko
 */
public interface SimpleFactory<Entity> {
    /**
     * Creates new entity without passed attributes.
     *
     * @return new entity
     */
    Entity create();
}