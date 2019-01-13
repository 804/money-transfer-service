package org.eightofour.moneytransfer.web.rest.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Request class for passing parameters
 * for money transfer between accounts operation.
 *
 * @author Evgeny Zubenko
 */
@Getter
public class TransferRequest extends RechargeRequest {
    /**
     * Source account for money transfer.
     */
    @JsonProperty("accountFrom")
    private String accountFromId;
}