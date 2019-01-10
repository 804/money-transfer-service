package org.eightofour.moneytransfer.rest.controller;

import org.eightofour.moneytransfer.model.impl.AccountDtoImpl;
import org.eightofour.moneytransfer.rest.request.TopUpRequest;
import org.eightofour.moneytransfer.rest.request.TransferRequest;
import org.javamoney.moneta.Money;

import javax.money.Monetary;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("/money")
@Consumes(MediaType.APPLICATION_JSON)
public class MoneyController {

    public MoneyController() {}

    @PUT
    @Path("/topup")
    public void topUpAccount(TopUpRequest request) {
        System.out.println(1);
        // stub
    }

    @PUT
    @Path("/transfer")
    public void transfer(TransferRequest request) {
        System.out.println(1);
        // stub
    }
}