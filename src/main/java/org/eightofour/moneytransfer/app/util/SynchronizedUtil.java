package org.eightofour.moneytransfer.app.util;

import org.apache.commons.lang3.tuple.Pair;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Util class performing functional actions
 * with synchronization by arguments.
 *
 * @author Evgeny Zubenko
 */
public class SynchronizedUtil {
    private SynchronizedUtil() {
        throw new IllegalStateException("This class isn't instantiable");
    }

    /**
     * Apply function with synchronization by its argument.
     *
     * @param argument - function argument
     * @param function - applying function
     *
     * @param <Argument> - function argument type
     * @param <Result>   - function result type
     *
     * @return function applying result
     */
    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    public static <Argument, Result> Result applyWithSync(Argument argument,
                                                          Function<Argument, Result> function) {
        synchronized (argument) {
            return function.apply(argument);
        }
    }

    /**
     * Consume argument with specified logic with synchronization by argument.
     *
     * @param argument - consumed argument
     * @param consumer - consuming logic
     *
     * @param <Argument> - argument type
     */
    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    public static <Argument> void acceptWithSync(Argument argument,
                                                 Consumer<Argument> consumer) {
        synchronized (argument) {
            consumer.accept(argument);
        }
    }

    /**
     * Consume two arguments with specified logic with ordered synchronization by arguments.
     * "Ordered synchronization" means that arguments' monitors must be capture
     * in order defined by {@link Comparable} implementation. It's needed for deadlock elimination.
     *
     * @param firstArgument  - first consumed argument
     * @param secondArgument - second consumed argument
     * @param biConsumer     - consuming logic
     *
     * @param <Argument> argument type; must be {@link Comparable}
     */
    public static <Argument extends Comparable> void acceptWithOrderedSync(Argument firstArgument,
                                                                           Argument secondArgument,
                                                                           BiConsumer<Argument, Argument> biConsumer) {
        // ordering for synchronization order definition
        Pair<Argument, Argument> syncPair = getOrderedPair(firstArgument, secondArgument);
        synchronized (syncPair.getLeft()) {
            synchronized (syncPair.getRight()) {
                biConsumer.accept(firstArgument, secondArgument);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static <Argument extends Comparable> Pair<Argument, Argument> getOrderedPair(Argument firstArgument,
                                                                                        Argument secondArgument) {
        return firstArgument.compareTo(secondArgument) < 0
                ? Pair.of(firstArgument, secondArgument)
                : Pair.of(secondArgument, firstArgument);
    }
}