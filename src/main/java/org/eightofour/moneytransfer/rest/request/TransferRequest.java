package org.eightofour.moneytransfer.rest.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransferRequest extends TopUpRequest {
    @JsonProperty("accountFrom")
    private String accountIdFrom;

    public String getAccountIdFrom() {
        return accountIdFrom;
    }
}