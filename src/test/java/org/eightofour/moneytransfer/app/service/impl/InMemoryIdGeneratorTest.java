package org.eightofour.moneytransfer.app.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("In-memory ID generator testing")
class InMemoryIdGeneratorTest {
    private InMemoryIdGenerator idGenerator;

    @BeforeEach
    void init() {
        this.idGenerator = new InMemoryIdGenerator();
    }

    @Test
    @DisplayName("Common generation logic testing")
    void testGenerateId() {
        String id = idGenerator.generateId();
        assertNotNull(id, "Generated ID mustn't be null");
    }

    @Test
    @DisplayName("Sequentially generation logic testing")
    void testGenerateSequenceOfUniqueId() {
        String firstId = idGenerator.generateId();
        String secondId = idGenerator.generateId();
        String thirdId = idGenerator.generateId();

        assertNotEquals(
            firstId, secondId,
            "Generated IDs mustn't be equals"
        );
        assertNotEquals(
            secondId, thirdId,
            "Generated IDs mustn't be equals"
        );
        assertNotEquals(
            firstId, thirdId,
            "Generated IDs mustn't be equals"
        );
    }
}