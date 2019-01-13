package org.eightofour.moneytransfer.app.model.entity.impl;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.eightofour.moneytransfer.app.model.entity.api.Account;
import org.eightofour.moneytransfer.app.model.entity.api.AccountView;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import java.io.IOException;
import java.math.BigInteger;

/**
 * Account entity view implementation for state capturing.
 *
 * @author Evgeny Zubenko
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@EqualsAndHashCode
public class AccountViewImpl implements AccountView {
    /**
     * Factory method for account state view ({@link AccountViewImpl}) creating
     * by passed account instance.
     *
     * @param account - account instance
     *
     * @return account state view
     */
    public static AccountViewImpl from(Account account) {
        return new AccountViewImpl(account.getId(), account.getAmount());
    }

    /**
     * Account ID.
     */
    @JsonProperty("id")
    private String id;

    /**
     * Account money amount.
     */
    @JsonProperty("amount")
    @JsonSerialize(using = AmountJsonSerializer.class)
    @JsonDeserialize(using = AmountJsonDeserializer.class)
    private MonetaryAmount amount;

    // serializer class for correct money amount serialization
    private static class AmountJsonSerializer extends JsonSerializer<MonetaryAmount> {
        @Override
        public void serialize(MonetaryAmount value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            gen.writeString(value.toString());
        }
    }

    // deserializer class for correct money amount deserialization
    private static class AmountJsonDeserializer extends JsonDeserializer<MonetaryAmount> {
        @Override
        public MonetaryAmount deserialize(JsonParser parser, DeserializationContext cxt)
                throws IOException {
            String value = parser.getValueAsString();
            String[] strings = value.split(" ");
            return Money.of(new BigInteger(strings[1]), strings[0]);
        }
    }
}