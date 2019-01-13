package org.eightofour.moneytransfer.web.rest.model.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.eightofour.moneytransfer.util.ResourceUtil.getResourceAsStream;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Recharge request testing")
class RechargeRequestTest {
    private static final String TEST_ID_TO = "test-to";
    private static final Money TEST_AMOUNT = Money.of(1, "USD");
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    @DisplayName("Construction testing")
    void testCreation() {
        RechargeRequest rechargeRequest = new RechargeRequest(TEST_ID_TO, TEST_AMOUNT);

        assertEquals(
            TEST_ID_TO, rechargeRequest.getAccountToId(),
            "Target account ID in request must be the same as in constructor"
        );
        assertEquals(
            TEST_AMOUNT, rechargeRequest.getAmount(),
            "Amount in request must be the same as in constructor"
        );
    }

    @Test
    @DisplayName("Custom deserialization testing")
    void testCustomDeserialization() throws IOException {
        RechargeRequest rechargeRequest = OBJECT_MAPPER.readValue(
            getResourceAsStream(getClass(), "recharge.json"),
            RechargeRequest.class
        );

        assertEquals(
            TEST_ID_TO, rechargeRequest.getAccountToId(),
            "Target account ID in request must be the same as in deserialized JSON"
        );
        assertEquals(
            TEST_AMOUNT, rechargeRequest.getAmount(),
            "Amount in request must be the same as in deserialized JSON"
        );
    }
}