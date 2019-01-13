package org.eightofour.moneytransfer.web.server;

import com.google.common.collect.ImmutableList;
import io.dropwizard.Application;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Environment;
import org.eightofour.moneytransfer.app.model.entity.api.Account;
import org.eightofour.moneytransfer.app.model.factory.api.SimpleFactory;
import org.eightofour.moneytransfer.app.model.factory.impl.AccountFactory;
import org.eightofour.moneytransfer.app.service.api.AccountService;
import org.eightofour.moneytransfer.app.service.api.IdGenerator;
import org.eightofour.moneytransfer.app.service.impl.AccountServiceImpl;
import org.eightofour.moneytransfer.app.service.impl.InMemoryIdGenerator;
import org.eightofour.moneytransfer.app.storage.api.Repository;
import org.eightofour.moneytransfer.app.storage.impl.InMemoryAccountRepository;
import org.eightofour.moneytransfer.web.rest.controller.AccountController;
import org.eightofour.moneytransfer.web.rest.controller.MoneyTransferController;
import org.eightofour.moneytransfer.web.rest.exception.mapper.AccountNotFoundExceptionMapper;
import org.eightofour.moneytransfer.web.rest.exception.mapper.IllegalAmountExceptionMapper;
import org.eightofour.moneytransfer.web.rest.exception.mapper.NoSuchMoneyExceptionMapper;

import javax.ws.rs.ext.ExceptionMapper;
import java.util.List;

/**
 * Application Dropwizard class Jersey resources registration:
 *  - REST-controllers, with main logic
 *  - exception mappers, with exception handling logic
 *
 * @author Evgeny Zubenko
 */
public class TransferWebApplication extends Application<TransferConfiguration> {
    // JAX-RS exception mappers
    private static final List<ExceptionMapper> EXCEPTION_MAPPERS =
        ImmutableList.of(
            new NoSuchMoneyExceptionMapper(),
            new AccountNotFoundExceptionMapper(),
            new IllegalAmountExceptionMapper()
        );

    @Override
    public void run(TransferConfiguration configuration,
                    Environment environment) {
        registerControllersForJersey(environment);
        registerExceptionMappersForJersey(environment);
    }

    private void registerControllersForJersey(Environment environment) {
        JerseyEnvironment jerseyEnvironment = environment.jersey();
        createControllers().forEach(jerseyEnvironment::register);
    }

    private List<Object> createControllers() {
        IdGenerator idGenerator = new InMemoryIdGenerator();
        SimpleFactory<Account> accountFactory = new AccountFactory(idGenerator);
        Repository<String, Account> accountRepository = new InMemoryAccountRepository();
        AccountService accountService = new AccountServiceImpl(accountFactory, accountRepository);

        return ImmutableList.of(
            new AccountController(accountService),
            new MoneyTransferController(accountService)
        );
    }

    private void registerExceptionMappersForJersey(Environment environment) {
        JerseyEnvironment jerseyEnvironment = environment.jersey();
        EXCEPTION_MAPPERS.forEach(jerseyEnvironment::register);
    }
}