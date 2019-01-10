package org.eightofour.moneytransfer.model.api;

import javax.money.MonetaryAmount;

public interface Account {
    String geiId();
    MonetaryAmount getAmount();
    void setAmount(MonetaryAmount newAmount);
}