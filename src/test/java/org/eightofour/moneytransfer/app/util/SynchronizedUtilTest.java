package org.eightofour.moneytransfer.app.util;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTimeout;

@DisplayName("Synchronization util testing")
class SynchronizedUtilTest {
    private static final List<Pair<String, String>> ARGUMENT_PAIRS =
            ImmutableList.of(
                Pair.of("a", "b"),
                Pair.of("b", "c"),
                Pair.of("c", "a")
            );

    private CountDownLatch latch;
    private AtomicBoolean atomicBoolean;
    private ExecutorService executorService;

    @BeforeEach
    void init() {
        executorService = Executors.newFixedThreadPool(3);
        atomicBoolean = new AtomicBoolean(false);
        latch = new CountDownLatch(3);
    }

    @AfterEach
    void close() throws InterruptedException {
        executorService.shutdown();
        if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
        }
    }

    @Test
    @DisplayName("Synchronized function execution")
    void applyWithSync() {
        Function<Object, Void> function = obj -> {
            syncAction(atomicBoolean);
            return null;
        };

        Object argument = new Object();

        Supplier<Void> task = () -> {
            latchAwait(latch);
            SynchronizedUtil.applyWithSync(argument, function);
            return null;
        };

        assertTimeout(
            Duration.ofSeconds(30),
            () -> {
                CompletableFuture[] completableFutures = IntStream.range(0, 3)
                    .mapToObj(num -> CompletableFuture.supplyAsync(task, executorService))
                    .toArray(CompletableFuture[]::new);

                CompletableFuture.allOf(completableFutures).get();
            },
            "Functions must be completed in time less then 30 seconds."
        );
    }

    @Test
    @DisplayName("Synchronized consumer execution")
    void acceptWithSync() {
        Consumer<Object> consumer = obj -> syncAction(atomicBoolean);
        Object argument = new Object();

        Supplier<Void> task = () -> {
            latchAwait(latch);
            SynchronizedUtil.acceptWithSync(argument, consumer);
            return null;
        };

        assertTimeout(
            Duration.ofSeconds(30),
            () -> {
                CompletableFuture[] completableFutures = IntStream.range(0, 3)
                    .mapToObj(num -> CompletableFuture.supplyAsync(task, executorService))
                    .toArray(CompletableFuture[]::new);

                CompletableFuture.allOf(completableFutures).get();
            },
            "Consumer must be completed in time less then 30 seconds."
        );
    }

    @Test
    @DisplayName("Synchronized bi-consumer execution")
    void acceptWithOrderedSync() {
        BiConsumer<String, String> consumer = (str1, str2) -> syncAction(atomicBoolean);

        assertTimeout(
            Duration.ofSeconds(30),
            () -> {
                CompletableFuture[] completableFutures = ARGUMENT_PAIRS.stream()
                    .map(
                        pair -> CompletableFuture.supplyAsync(
                            getTask(latch, consumer, pair),
                            executorService
                        )
                    ).toArray(CompletableFuture[]::new);

                CompletableFuture.allOf(completableFutures).get();
            },
            "Consumer must be completed in time less then 30 seconds."
        );
    }

    private void syncAction(AtomicBoolean atomicBoolean) {
        assertFalse(
            atomicBoolean.get(),
            ""
        );
        atomicBoolean.set(true);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            ExceptionUtils.rethrow(e);
        }
        atomicBoolean.set(false);
    }

    private void latchAwait(CountDownLatch latch) {
        latch.countDown();
        try {
            latch.await();
        } catch (InterruptedException e) {
            ExceptionUtils.rethrow(e);
        }
    }

    private Supplier<Void> getTask(CountDownLatch latch,
                                   BiConsumer<String, String> consumer,
                                   Pair<String, String> pair) {
        return () -> {
            latchAwait(latch);
            SynchronizedUtil.acceptWithOrderedSync(
                pair.getLeft(),
                pair.getRight(),
                consumer
            );
            return null;
        };
    }
}