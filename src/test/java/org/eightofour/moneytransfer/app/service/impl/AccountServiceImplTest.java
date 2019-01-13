package org.eightofour.moneytransfer.app.service.impl;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.eightofour.moneytransfer.app.exception.AccountNotFoundException;
import org.eightofour.moneytransfer.app.exception.NoSuchMoneyException;
import org.eightofour.moneytransfer.app.model.entity.api.Account;
import org.eightofour.moneytransfer.app.model.entity.api.AccountView;
import org.eightofour.moneytransfer.app.model.entity.impl.AccountViewImpl;
import org.eightofour.moneytransfer.app.model.factory.api.SimpleFactory;
import org.eightofour.moneytransfer.app.model.factory.impl.AccountFactory;
import org.eightofour.moneytransfer.app.service.api.AccountService;
import org.eightofour.moneytransfer.app.service.api.IdGenerator;
import org.eightofour.moneytransfer.app.storage.api.Repository;
import org.eightofour.moneytransfer.app.storage.impl.InMemoryAccountRepository;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Account service testing")
class AccountServiceImplTest {
    private Repository<String, Account> accountRepository;
    private AccountService accountService;

    @BeforeEach
    void init() {
        IdGenerator idGenerator = new InMemoryIdGenerator();
        SimpleFactory<Account> accountFactory = new AccountFactory(idGenerator);
        this.accountRepository = new InMemoryAccountRepository();
        this.accountService = new AccountServiceImpl(accountFactory, this.accountRepository);
    }

    @Test
    @DisplayName("Create account and getting by service")
    void testCreateAccount() {
        AccountView account = accountService.createAccount();
        Optional<AccountView> viewOptional = accountService.getAccount(account.getId());

        assertTrue(
            viewOptional.isPresent(),
            "Created account view optional must not be empty,"
        );
        assertEquals(
            account,
            viewOptional.get(),
            "Created account must equal got one."
        );
    }

    @Test
    @DisplayName("Create account and getting by repository")
    void testCreatedAccountInRepository() {
        AccountView account = accountService.createAccount();
        Optional<Account> optionalAccount = accountRepository.get(account.getId());

        assertTrue(
            optionalAccount.isPresent(),
            "Created account view optional must not be empty,"
        );
        assertEquals(
            account,
            optionalAccount.map(AccountViewImpl::from).get(),
            "Created account must equal got one."
        );
    }

    @Test
    @DisplayName("Create many account and checking for uniqueness")
    void testCreateManyAccounts() {
        long uniqueAccountCount = IntStream.range(0, 10)
                .mapToObj(num -> accountService.createAccount())
                .distinct()
                .count();
        assertEquals(
            10, uniqueAccountCount,
            "Account service must create 10 unique account."
        );
    }

    @Test
    @DisplayName("Getting not existed account")
    void testAccountNotFound() {
        Optional<AccountView> optionalAccountView = accountService.getAccount("test-id");

        assertFalse(
            optionalAccountView.isPresent(),
            "View optional must be empty for non existed account getting."
        );
    }

    @Test
    @DisplayName("Single recharge operation performing")
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    void testRechargeAccountSuccess() throws AccountNotFoundException {
        AccountView account = accountService.createAccount();
        Money rechargedAmount = Money.of(2, "USD");
        accountService.rechargeAccount(account.getId(), rechargedAmount);

        Optional<Account> optionalAccountAfter = accountRepository.get(account.getId());
        assertEquals(
            rechargedAmount,
            optionalAccountAfter.get().getAmount(),
            "Account must have correct money amount after recharging operation."
        );
    }

    @Test
    @DisplayName("Multiple recharge operation performing at the same time")
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    void testManyRechargeAccountSuccess() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(5);

        AccountView account = accountService.createAccount();
        Money amount = Money.of(1, "USD");

        Supplier<Void> task = () -> {
            latch.countDown();
            try {
                latch.await();
                accountService.rechargeAccount(
                    account.getId(), amount
                );
            } catch (Exception e) {
                ExceptionUtils.rethrow(e);
            }
            return null;
        };

        try {
            assertTimeout(
                Duration.ofSeconds(30),
                () -> {
                    CompletableFuture[] completableFutures = IntStream.range(0, 5)
                        .mapToObj(num -> CompletableFuture.supplyAsync(task, executorService))
                        .toArray(CompletableFuture[]::new);

                    CompletableFuture.allOf(completableFutures).get();
                },
                "Operations must be completed in time less then 30 seconds."
            );
        } finally {
            executorService.shutdown();
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        }

        Optional<AccountView> accountView = accountService.getAccount(account.getId());
        assertEquals(
            amount.multiply(5),
            accountView.get().getAmount(),
            "Account must have correct money amount after multiple recharging operation."
        );
    }

    @Test
    @DisplayName("Recharge account, which isn't found")
    void testRechargeableAccountNotFound() {
        assertThrows(
            AccountNotFoundException.class,
            () -> accountService.rechargeAccount(
                "test-id",
                Money.of(2, "USD")
            ),
            "Service must throw exception for recharging of not existed account."
        );
    }

    @Test
    @DisplayName("Single transfer operation performing")
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    void testTransferSuccess() throws AccountNotFoundException, NoSuchMoneyException {
        AccountView firstAccount = accountService.createAccount();
        AccountView secondAccount = accountService.createAccount();

        Money rechargedAmount = Money.of(2, "USD");
        accountService.rechargeAccount(firstAccount.getId(), rechargedAmount);

        Money transferredAmount = Money.of(1, "USD");
        accountService.transfer(firstAccount.getId(), secondAccount.getId(), transferredAmount);

        Optional<Account> optionalFirstAccountAfter = accountRepository.get(firstAccount.getId());
        assertEquals(
            transferredAmount,
            optionalFirstAccountAfter.get().getAmount(),
            "Source account has incorrect money amount after transfer operation performing."
        );

        Optional<Account> optionalSecondAccountAfter = accountRepository.get(secondAccount.getId());
        assertEquals(
            transferredAmount,
            optionalSecondAccountAfter.get().getAmount(),
            "Target account has incorrect money amount after transfer operation performing."
        );
    }

    @Test
    @DisplayName("Multiple transfer operation performing at the same time")
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    void testManyTransferSuccess() throws AccountNotFoundException, InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(6);
        CountDownLatch latch = new CountDownLatch(6);

        AccountView firstAccount = accountService.createAccount();
        AccountView secondAccount = accountService.createAccount();

        Money transferredAmount = Money.of(1, "USD");

        Money initAmount = Money.of(3, "USD");
        accountService.rechargeAccount(firstAccount.getId(), initAmount);
        accountService.rechargeAccount(secondAccount.getId(), initAmount);

        Supplier<Void> firstTask = () -> {
            latch.countDown();
            try {
                latch.await();
                accountService.transfer(
                    firstAccount.getId(),
                    secondAccount.getId(),
                    transferredAmount
                );
            } catch (Exception e) {
                ExceptionUtils.rethrow(e);
            }
            return null;
        };
        Supplier<Void> secondTask = () -> {
            latch.countDown();
            try {
                latch.await();
                accountService.transfer(
                    secondAccount.getId(),
                    firstAccount.getId(),
                    transferredAmount
                );
            } catch (Exception e) {
                ExceptionUtils.rethrow(e);
            }
            return null;
        };

        try {
            assertTimeout(
                Duration.ofSeconds(30),
                () -> {
                    CompletableFuture[] firstCompletableFutures = IntStream.range(0, 3)
                            .mapToObj(num -> CompletableFuture.supplyAsync(firstTask, executorService))
                            .toArray(CompletableFuture[]::new);
                    CompletableFuture[] secondCompletableFutures = IntStream.range(0, 3)
                            .mapToObj(num -> CompletableFuture.supplyAsync(secondTask, executorService))
                            .toArray(CompletableFuture[]::new);

                    CompletableFuture.allOf(firstCompletableFutures).get();
                    CompletableFuture.allOf(secondCompletableFutures).get();
                },
                "Operations must be completed in time less then 30 seconds."
            );
        } finally {
            executorService.shutdown();
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        }

        Optional<AccountView> firstAccountView = accountService.getAccount(firstAccount.getId());
        Optional<AccountView> secondAccountView = accountService.getAccount(secondAccount.getId());
        assertEquals(
            initAmount,
            firstAccountView.get().getAmount(),
            "First account must have the same amount as in initial state."
        );
        assertEquals(
            initAmount,
            secondAccountView.get().getAmount(),
            "Second account must have the same amount as in initial state."
        );
    }

    @Test
    @DisplayName("Transfer between accounts, which aren't found")
    void testTransferAccountNotFound() {
        assertThrows(
            AccountNotFoundException.class,
            () -> accountService.transfer(
                "test-id-1",
                "test-id-2",
                Money.of(1, "USD")
            ),
            "Service must throw exception for transfer from not existed account."
        );
    }

    @Test
    @DisplayName("Transfer from accounts, which hasn't enough money")
    void testTransferNoSuchMoney() {
        AccountView firstAccount = accountService.createAccount();
        AccountView secondAccount = accountService.createAccount();

        assertThrows(
            NoSuchMoneyException.class,
            () -> accountService.transfer(
                firstAccount.getId(),
                secondAccount.getId(),
                Money.of(1, "USD")
            ),
            "Service must throw exception for transfer from account, " +
                "which hasn't enough money."
        );
    }
}