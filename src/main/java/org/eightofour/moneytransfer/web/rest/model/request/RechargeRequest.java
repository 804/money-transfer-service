package org.eightofour.moneytransfer.web.rest.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
public class RechargeRequest {
    /**
     * Target account ID for transfer.
     */
    @JsonProperty(value = "accountTo", required = true)
    private String accountToId;

    /**
     * Transferred money amount.
     */
    @JsonProperty(value = "amount", required = true)
    @JsonDeserialize(using = AmountJsonDeserializer.class)
    @JsonSerialize(using = AmountJsonSerializer.class)
    private MonetaryAmount amount;


    // deserializer class for correct money amount deserialization
    private static class AmountJsonDeserializer extends JsonDeserializer<MonetaryAmount> {
        @Override
        public MonetaryAmount deserialize(JsonParser parser, DeserializationContext context)
                throws IOException {
            String amountStr = parser.getValueAsString();
            return Money.of(new BigDecimal(amountStr), "USD");
        }
    }

    // serializer class for correct money amount serialization
    private static class AmountJsonSerializer extends JsonSerializer<MonetaryAmount> {
        @Override
        public void serialize(MonetaryAmount value, JsonGenerator generator,
                              SerializerProvider serializers) throws IOException {
            generator.writeNumber(value.toString().split(" ")[1]);
        }
    }
}