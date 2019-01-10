package org.eightofour.moneytransfer.rest.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class TopUpRequest {
    @JsonProperty("accountTo")
    private String accountIdTo;
    @JsonProperty("amount")
    private BigDecimal amount;

    public String getAccountIdTo() {
        return accountIdTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}