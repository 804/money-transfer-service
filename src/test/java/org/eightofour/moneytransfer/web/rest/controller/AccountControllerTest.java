package org.eightofour.moneytransfer.web.rest.controller;

import org.eightofour.moneytransfer.app.model.entity.impl.AccountViewImpl;
import org.eightofour.moneytransfer.web.rest.model.response.ErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Account REST endpoint testing")
class AccountControllerTest extends AbstractControllerTest {

    @Test
    @DisplayName("Simple account creation performing")
    void testCreateAccount() {
        Response response = createAccount();
        assertEquals(
            200, response.getStatus(),
            "Account must be created successfully."
        );
        assertNotNull(
            response.readEntity(AccountViewImpl.class),
            "Account view must be returned in response."
        );
    }

    @Test
    @DisplayName("Simple account getting performing")
    void testGetAccount() {
        Response creatAccountResponse = createAccount();
        AccountViewImpl accountView = creatAccountResponse.readEntity(AccountViewImpl.class);

        Response getAccountResponse = getAccount(accountView.getId());
        assertEquals(
            200, creatAccountResponse.getStatus(),
            "Account must be created successfully."
        );
        assertEquals(
            accountView,
            getAccountResponse.readEntity(AccountViewImpl.class),
            "Created account must be stored in repository."
        );
    }

    @Test
    @DisplayName("Getting not existed account")
    void testAccountNotFound() {
        Response getAccountResponse = getAccount("test-id");
        assertEquals(
            404, getAccountResponse.getStatus(),
            "Response code must be 404."
        );
        assertEquals(
            new ErrorResponse(404, "Account with id 'test-id' isn't found"),
            getAccountResponse.readEntity(ErrorResponse.class),
            "Error response entity in response is incorrect."
        );
    }
}