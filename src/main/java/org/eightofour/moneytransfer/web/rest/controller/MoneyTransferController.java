package org.eightofour.moneytransfer.web.rest.controller;

import org.eightofour.moneytransfer.app.exception.AccountNotFoundException;
import org.eightofour.moneytransfer.app.exception.IllegalAmountException;
import org.eightofour.moneytransfer.app.exception.NoSuchMoneyException;
import org.eightofour.moneytransfer.app.service.api.AccountService;
import org.eightofour.moneytransfer.web.rest.model.request.RechargeRequest;
import org.eightofour.moneytransfer.web.rest.model.request.TransferRequest;

import javax.money.MonetaryAmount;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Controller class for REST API endpoints, which
 * implement transfer money logic.
 *
 * @author Evgeny Zubenko
 */
@Path("/money")
@Consumes(MediaType.APPLICATION_JSON)
public class MoneyTransferController {
    private static final String RECHARGE_VALIDATION_ERROR_MESSAGE =
            "Recharged amount can't be negative";
    private static final String TRANSFER_VALIDATION_ERROR_MESSAGE =
            "Transferred amount can't be negative";

    private final AccountService accountService;

    public MoneyTransferController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Endpoint for account recharging operation performing.
     *
     * @param request - request object with
     *                  passed operation parameters
     *
     * @return {@link Response} instance for current operation
     *         (always code 200 without exceptions)
     *
     * @throws AccountNotFoundException if target account
     *                                  from request wasn't found
     */
    @PUT
    @Path("/recharge")
    public Response rechargeAccount(RechargeRequest request)
            throws AccountNotFoundException {
        validateAmount(request.getAmount(), RECHARGE_VALIDATION_ERROR_MESSAGE);
        accountService.rechargeAccount(
            request.getAccountToId(),
            request.getAmount()
        );
        return Response.status(200).build();
    }

    /**
     * Endpoint for money transfer between accounts operation performing.
     *
     * @param request - request object with
     *                  passed operation parameters
     *
     * @return {@link Response} instance for current operation
     *         (always code 200 without exceptions)
     *
     * @throws AccountNotFoundException if source or target account
     *                                  from request wasn't found
     * @throws NoSuchMoneyException if source account hasn't enough
     *                              money for transfer
     */
    @PUT
    @Path("/transfer")
    public Response transfer(TransferRequest request)
            throws AccountNotFoundException, NoSuchMoneyException {
        validateAmount(request.getAmount(), TRANSFER_VALIDATION_ERROR_MESSAGE);
        accountService.transfer(
            request.getAccountFromId(),
            request.getAccountToId(),
            request.getAmount()
        );
        return Response.status(200).build();
    }

    private void validateAmount(MonetaryAmount amount, String errorMessage) {
        if (amount.isNegative()) {
            throw new IllegalAmountException(errorMessage);
        }
    }
}