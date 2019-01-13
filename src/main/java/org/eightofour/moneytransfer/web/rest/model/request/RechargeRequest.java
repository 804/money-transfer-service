package org.eightofour.moneytransfer.web.rest.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Request class for passing parameters
 * for account recharging account operation.
 *
 * @author Evgeny Zubenko
 */
@Getter
public class RechargeRequest {
    /**
     * Target account ID for transfer.
     */
    @JsonProperty("accountTo")
    private String accountToId;

    /**
     * Transferred money amount.
     */
    @JsonProperty("amount")
    @JsonDeserialize(using = AmountJsonDeserializer.class)
    private MonetaryAmount amount;

    // deserializer class for correct money amount deserialization
    private static class AmountJsonDeserializer extends JsonDeserializer<MonetaryAmount> {
        @Override
        public MonetaryAmount deserialize(JsonParser parser, DeserializationContext cxt)
                throws IOException {
            String amountStr = parser.getValueAsString();
            return Money.of(new BigDecimal(amountStr), "USD");
        }
    }
}