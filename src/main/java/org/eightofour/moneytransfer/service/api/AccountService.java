package org.eightofour.moneytransfer.service.api;

import org.eightofour.moneytransfer.model.api.AccountDto;

import javax.money.MonetaryAmount;

public interface AccountService {
    String create();
    AccountDto get(String accountId);
    void topUp(String accountId, MonetaryAmount amount);
    void transfer(String fromAccountId, String toAccountId, MonetaryAmount amount);
}