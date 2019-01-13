package org.eightofour.moneytransfer.web.rest.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.money.MonetaryAmount;

/**
 * Request class for passing parameters
 * for money transfer between accounts operation.
 *
 * @author Evgeny Zubenko
 */
@Getter
@NoArgsConstructor
public class TransferRequest extends RechargeRequest {
    /**
     * Source account for money transfer.
     */
    @JsonProperty(value = "accountFrom", required = true)
    private String accountFromId;

    public TransferRequest(String accountFromId, String accountToId,
                           MonetaryAmount amount) {
        super(accountToId, amount);
        this.accountFromId = accountFromId;
    }
}