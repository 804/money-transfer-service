package org.eightofour.moneytransfer.app.service.impl;

import org.eightofour.moneytransfer.app.service.api.IdGenerator;

import java.util.concurrent.atomic.AtomicLong;

import static java.lang.Math.abs;
import static org.apache.commons.lang3.StringUtils.leftPad;

/**
 * In-memory implementation for ID generator.
 *
 * @author Evgeny Zubenko
 */
public class InMemoryIdGenerator implements IdGenerator {
    private AtomicLong longState = new AtomicLong(0);

    @Override
    public String generateId() {
        long currentState = longState.getAndIncrement();
        return stateToId(currentState);
    }

    private String stateToId(long state) {
        return signPart(state) + absPart(state);
    }

    private String signPart(long state) {
        return state >= 0 ? "0" : "1";
    }

    private String absPart(long state) {
        return leftPad(
            String.valueOf(abs(state)),
            19, '0'
        );
    }
}