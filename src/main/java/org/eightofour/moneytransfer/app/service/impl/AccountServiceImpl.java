package org.eightofour.moneytransfer.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.eightofour.moneytransfer.app.exception.AccountNotFoundException;
import org.eightofour.moneytransfer.app.exception.IllegalAmountException;
import org.eightofour.moneytransfer.app.exception.NoSuchMoneyException;
import org.eightofour.moneytransfer.app.model.entity.api.Account;
import org.eightofour.moneytransfer.app.model.entity.api.AccountView;
import org.eightofour.moneytransfer.app.model.entity.impl.AccountViewImpl;
import org.eightofour.moneytransfer.app.model.factory.api.SimpleFactory;
import org.eightofour.moneytransfer.app.service.api.AccountService;
import org.eightofour.moneytransfer.app.storage.api.Repository;

import javax.money.MonetaryAmount;
import java.util.Optional;

import static org.eightofour.moneytransfer.app.util.SynchronizedUtil.*;

/**
 * Implementation of service with working with deposit account and
 * operation performing with them.
 *
 * @author Evgeny Zubenko
 */
@Slf4j
public class AccountServiceImpl implements AccountService {
    private static final String NO_SUCH_MONEY_MSG_TEMPLATE =
        "Source account with ID '%s' hasn't enough money for transfer";

    private final SimpleFactory<Account> accountFactory;
    private final Repository<String, Account> accountRepository;

    public AccountServiceImpl(SimpleFactory<Account> accountFactory,
                              Repository<String, Account> accountRepository) {
        this.accountFactory = accountFactory;
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountView createAccount() {
        Account newAccount = accountFactory.create();
        accountRepository.add(newAccount.getId(), newAccount);
        log.info("Account with ID '{}' has been created", newAccount.getId());
        return AccountViewImpl.from(newAccount);
    }

    @Override
    public Optional<AccountView> getAccount(String accountId) {
        Optional<Account> accountOpt = accountRepository.get(accountId);

        return accountOpt.map(
            // need to synchronize access to account for getting consistent view
            account -> applyWithSync(account, AccountViewImpl::from)
        );
    }

    @Override
    public void rechargeAccount(String accountId, MonetaryAmount amount)
            throws AccountNotFoundException {
        try {
            Account account = getAccountWithException(accountId);

            // need to synchronize access to account during operation performing
            // for consistency assurance and result publication between different threads
            // (see happened-before rule for 'synchronized'-block)
            acceptWithSync(
                account,
                acc -> {
                    MonetaryAmount newAmount = acc.getAmount().add(amount);
                    acc.setAmount(newAmount);
                }
            );
            log.info(
                "Recharging operation has been performed successfully: account '{}', amount '{}'",
                account.getId(), amount
            );
        } catch (Exception exc) {
            log.error(
                "Recharging operation has been failed: account - '{}', amount - '{}', cause: {}",
                accountId, amount, exc.getMessage()
            );
            throw exc;
        }

    }

    @Override
    public void transfer(String fromAccountId, String toAccountId, MonetaryAmount amount)
            throws AccountNotFoundException, NoSuchMoneyException {
        try {
            Account fromAccount = getAccountWithException(fromAccountId);
            Account toAccount = getAccountWithException(toAccountId);

            if (!fromAccountId.equals(toAccountId)) {
                // need to synchronize access to accounts during operation performing
                // for consistency assurance and result publication between different threads
                // (see happened-before rule for 'synchronized'-block)
                // ordering for monitors capturing is needed for dead-lock elimination
                acceptWithOrderedSync(
                    fromAccount, toAccount, (fromAcc, toAcc) -> {
                        MonetaryAmount newFromAmount = fromAcc.getAmount().subtract(amount);
                        MonetaryAmount newToAmount = toAcc.getAmount().add(amount);

                        fromAcc.setAmount(newFromAmount);
                        toAcc.setAmount(newToAmount);
                    }
                );
            }

            log.info(
                "Transfer operation has been performed successfully: " +
                    "fromAccount - '{}', toAccount - '{}', amount - '{}'",
                fromAccountId, toAccountId, amount
            );
        } catch (IllegalAmountException exc) {
            log.error(
                "Transfer operation has been failed: fromAccount - '{}', " +
                    "toAccount - '{}', amount - '{}', cause - '{}'",
                fromAccountId, toAccountId, amount, exc.getMessage()
            );
            throw new NoSuchMoneyException(
                String.format(NO_SUCH_MONEY_MSG_TEMPLATE, fromAccountId), exc
            );
        }  catch(Exception exc) {
            log.error(
                "Transfer operation has been failed: fromAccount - '{}', " +
                    "toAccount - '{}', amount - '{}', cause - '{}'",
                fromAccountId, toAccountId, amount, exc.getMessage()
            );
            throw exc;
        }
    }

    private Account getAccountWithException(String accountId)
            throws AccountNotFoundException {
        return accountRepository.get(accountId)
                .orElseThrow(
                    () -> new AccountNotFoundException(accountId)
                );
    }
}