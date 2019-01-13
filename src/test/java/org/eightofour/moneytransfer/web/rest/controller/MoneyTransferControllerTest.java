package org.eightofour.moneytransfer.web.rest.controller;

import org.eightofour.moneytransfer.app.model.entity.impl.AccountViewImpl;
import org.eightofour.moneytransfer.web.rest.model.request.RechargeRequest;
import org.eightofour.moneytransfer.web.rest.model.request.TransferRequest;
import org.eightofour.moneytransfer.web.rest.model.response.ErrorResponse;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Transfer REST endpoint testing")
class MoneyTransferControllerTest extends AbstractControllerTest {
    private static final String RECHARGE_URL_TEMPLATE = "http://localhost:%d/money/recharge";
    private static final String TRANSFER_URL_TEMPLATE = "http://localhost:%d/money/transfer";

    @Test
    @DisplayName("Simple recharge operation")
    void testRechargeAccountSuccess() {
        AccountViewImpl accountViewBefore = createAccount().readEntity(AccountViewImpl.class);
        Money rechargedAmount = Money.of(5, "USD");

        Response rechargeResponse = rechargeAccount(
            new RechargeRequest(
                accountViewBefore.getId(),
                rechargedAmount
            )
        );

        assertEquals(
            200, rechargeResponse.getStatus(),
            "Response must have HTTP code 200."
        );

        AccountViewImpl accountViewAfter = getAccount(accountViewBefore.getId())
                .readEntity(AccountViewImpl.class);

        assertEquals(
            rechargedAmount, accountViewAfter.getAmount(),
            "Illegal money amount for recharged account."
        );
    }

    @Test
    @DisplayName("Recharge with illegal amount")
    void testRechargeAccountIllegalAmount() {
        Response rechargeResponse = rechargeAccount(
            new RechargeRequest(
                "test-id",
                Money.of(-1, "USD")
            )
        );

        assertEquals(
            400, rechargeResponse.getStatus(),
            "Response must have HTTP code 400."
        );
        assertEquals(
            new ErrorResponse(400, "Recharged amount can't be negative"),
            rechargeResponse.readEntity(ErrorResponse.class),
            "Incorrect response entity in error response."
        );
    }

    @Test
    @DisplayName("Recharge of not existed amount")
    void testRechargeableAccountNotFound() {
        Response rechargeResponse = rechargeAccount(
            new RechargeRequest(
                "test-id",
                Money.of(1, "USD")
            )
        );

        assertEquals(
            404, rechargeResponse.getStatus(),
            "Response must have HTTP code 404."
        );
        assertEquals(
            new ErrorResponse(404, "Account with id 'test-id' isn't found"),
            rechargeResponse.readEntity(ErrorResponse.class),
            "Incorrect response entity in error response."
        );
    }

    @Test
    @DisplayName("Simple transfer operation")
    void testTransferSuccess() {
        AccountViewImpl firstAccountViewBefore = createAccount().readEntity(AccountViewImpl.class);
        AccountViewImpl secondAccountViewBefore = createAccount().readEntity(AccountViewImpl.class);
        Money rechargedAmount = Money.of(10, "USD");
        Money transferredAmount = Money.of(5, "USD");

        rechargeAccount(
            new RechargeRequest(
                firstAccountViewBefore.getId(),
                rechargedAmount
            )
        );

        Response transferResponse = transfer(
            new TransferRequest(
                firstAccountViewBefore.getId(),
                secondAccountViewBefore.getId(),
                transferredAmount
            )
        );

        assertEquals(
            200, transferResponse.getStatus(),
            "Response must have HTTP code 200."
        );


        AccountViewImpl firstAccountViewAfter = getAccount(firstAccountViewBefore.getId())
                .readEntity(AccountViewImpl.class);
        assertEquals(
            transferredAmount, firstAccountViewAfter.getAmount(),
            "Illegal money amount for source account."
        );

        AccountViewImpl secondAccountViewAfter = getAccount(secondAccountViewBefore.getId())
                .readEntity(AccountViewImpl.class);
        assertEquals(
            transferredAmount, secondAccountViewAfter.getAmount(),
            "Illegal money amount for target account."
        );
    }

    @Test
    @DisplayName("Transfer of illegal money amount")
    void testTransferIllegalAmount() {
        Response transferResponse = transfer(
            new TransferRequest(
                "test-id-1",
                "test-id-2",
                Money.of(-1, "USD")
            )
        );

        assertEquals(
            400, transferResponse.getStatus(),
            "Response must have HTTP code 200."
        );
        assertEquals(
            new ErrorResponse(400, "Transferred amount can't be negative"),
            transferResponse.readEntity(ErrorResponse.class),
            "Incorrect response entity in error response."
        );
    }

    @Test
    @DisplayName("Transfer from not existed account")
    void testTransferAccountNotFound() {
        Response transferResponse = transfer(
            new TransferRequest(
                "test-id-1",
                "test-id-2",
                Money.of(1, "USD")
            )
        );

        assertEquals(
            404, transferResponse.getStatus(),
            "Response must have HTTP code 404."
        );
        assertEquals(
            new ErrorResponse(404, "Account with id 'test-id-1' isn't found"),
            transferResponse.readEntity(ErrorResponse.class),
            "Incorrect response entity in error response."
        );
    }

    @Test
    @DisplayName("Transfer from account which hasn't enough money")
    void testTransferNotEnoughMoney() {

        AccountViewImpl firstAccountViewBefore = createAccount().readEntity(AccountViewImpl.class);
        AccountViewImpl secondAccountViewBefore = createAccount().readEntity(AccountViewImpl.class);

        Response transferResponse = transfer(
            new TransferRequest(
                firstAccountViewBefore.getId(),
                secondAccountViewBefore.getId(),
                Money.of(5, "USD")
            )
        );

        assertEquals(
            400, transferResponse.getStatus(),
            "Response must have HTTP code 400."
        );
        assertEquals(
            new ErrorResponse(
                400,
                String.format(
                    "Source account with ID '%s' hasn't enough money for transfer",
                    firstAccountViewBefore.getId()
                )
            ),
            transferResponse.readEntity(ErrorResponse.class),
            "Incorrect response entity in error response."
        );
    }

    private Response rechargeAccount(RechargeRequest rechargeRequest) {
        return client.target(resolveURL(RECHARGE_URL_TEMPLATE))
                .request()
                .put(Entity.json(rechargeRequest));
    }

    private Response transfer(TransferRequest transferRequest) {
        return client.target(resolveURL(TRANSFER_URL_TEMPLATE))
                .request()
                .put(Entity.json(transferRequest));
    }
}