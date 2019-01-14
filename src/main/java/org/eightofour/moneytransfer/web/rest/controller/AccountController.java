package org.eightofour.moneytransfer.web.rest.controller;


import lombok.extern.slf4j.Slf4j;
import org.eightofour.moneytransfer.app.exception.AccountNotFoundException;
import org.eightofour.moneytransfer.app.model.entity.api.AccountView;
import org.eightofour.moneytransfer.app.service.api.AccountService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

/**
 * Controller class for REST API endpoints, which
 * implement common account operations logic.
 *
 * @author Evgeny Zubenko
 */
@Slf4j
@Path("/account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Endpoint for account getting operation performing.
     *
     * @param id - needed account ID
     *
     * @return needed account state view
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public AccountView getAccount(@QueryParam("id") String id) throws AccountNotFoundException {
        Optional<AccountView> accountOptional = accountService.getAccount(id);
        return accountOptional.orElseThrow(() -> new AccountNotFoundException(id));
    }

    /**
     * Endpoint for account creation operation performing.
     *
     * @return created account state view
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public AccountView createAccount() {
        return accountService.createAccount();
    }
}