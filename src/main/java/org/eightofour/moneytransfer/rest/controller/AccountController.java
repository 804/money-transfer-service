package org.eightofour.moneytransfer.rest.controller;


import org.eightofour.moneytransfer.model.api.AccountDto;
import org.eightofour.moneytransfer.model.impl.AccountDtoImpl;
import org.javamoney.moneta.Money;

import javax.money.Monetary;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/account")
public class AccountController {

    public AccountController() {}

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public AccountDto getAccount(@QueryParam("id") String id) {
        // stub
        return new AccountDtoImpl(
            "test-id",
            Money.of(
                8,
                Monetary.getCurrency("USD")
            )
        );
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public AccountDto createAccount() {
        // stub
        return new AccountDtoImpl(
            "test-id",
            Money.of(
                8,
                Monetary.getCurrency("USD")
            )
        );
    }
}