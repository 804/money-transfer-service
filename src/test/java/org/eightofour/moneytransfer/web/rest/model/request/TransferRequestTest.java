package org.eightofour.moneytransfer.web.rest.model.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.eightofour.moneytransfer.util.ResourceUtil.getResourceAsStream;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Transfer request testing")
class TransferRequestTest {
    private static final String TEST_ID_FROM = "test-from";
    private static final String TEST_ID_TO = "test-to";
    private static final Money TEST_AMOUNT = Money.of(1, "USD");
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    @DisplayName("Construction")
    void testCreation() {
        TransferRequest transferRequest = new TransferRequest(TEST_ID_FROM, TEST_ID_TO, TEST_AMOUNT);

        assertEquals(
            TEST_ID_FROM, transferRequest.getAccountFromId(),
            "Source account ID in request must be the same as in constructor"
        );
        assertEquals(
            TEST_ID_TO, transferRequest.getAccountToId(),
            "Target account ID in request must be the same as in constructor"
        );
        assertEquals(
            TEST_AMOUNT, transferRequest.getAmount(),
            "Amount in request must be the same as in constructor"
        );
    }

    @Test
    @DisplayName("Custom deserialization")
    void testCustomDeserialization() throws IOException {
        TransferRequest transferRequest = OBJECT_MAPPER.readValue(
            getResourceAsStream(getClass(), "transfer.json"),
            TransferRequest.class
        );

        assertEquals(
            TEST_ID_FROM, transferRequest.getAccountFromId(),
            "Source account ID in request must be the same as in deserialized JSON"
        );
        assertEquals(
            TEST_ID_TO, transferRequest.getAccountToId(),
            "Target account ID in request must be the same as in deserialized JSON"
        );
        assertEquals(
            TEST_AMOUNT, transferRequest.getAmount(),
            "Amount in request must be the same as in deserialized JSON"
        );
    }
}