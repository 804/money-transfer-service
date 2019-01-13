package org.eightofour.moneytransfer.app.model.entity.impl;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eightofour.moneytransfer.app.model.entity.api.Account;
import org.eightofour.moneytransfer.app.model.entity.api.AccountView;

import javax.money.MonetaryAmount;
import java.io.IOException;

/**
 * Account entity view implementation for state capturing.
 *
 * @author Evgeny Zubenko
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
    private final String id;

    /**
     * Account money amount.
     */
    @JsonProperty("amount")
    @JsonSerialize(using = AmountJsonSerializer.class)
    private final MonetaryAmount amount;

    // serializer class for correct money amount serialization
    private static class AmountJsonSerializer extends JsonSerializer<MonetaryAmount> {
        @Override
        public void serialize(MonetaryAmount value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            gen.writeString(value.toString());
        }
    }
}