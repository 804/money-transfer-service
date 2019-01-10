package org.eightofour.moneytransfer.model.impl;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.eightofour.moneytransfer.model.api.AccountDto;

import javax.money.MonetaryAmount;

public class AccountDtoImpl implements AccountDto {
    private String id;
    private String amountStr;

    public AccountDtoImpl(String id, MonetaryAmount amount) {
        this.id = id;
        this.amountStr = amount.toString();
    }

    @Override
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @Override
    @JsonProperty("amount")
    public String getAmountStr() {
        return amountStr;
    }
}