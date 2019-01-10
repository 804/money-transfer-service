package org.eightofour.moneytransfer.storage.api;

import org.eightofour.moneytransfer.model.api.Account;

public interface AccouontRepository {
    void addAccount();
    Account getAccount();
}