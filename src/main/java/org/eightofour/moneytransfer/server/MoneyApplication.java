package org.eightofour.moneytransfer.server;

import com.google.common.collect.Lists;
import io.dropwizard.Application;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Environment;
import org.eightofour.moneytransfer.rest.controller.AccountController;
import org.eightofour.moneytransfer.rest.controller.MoneyController;

import java.util.List;

public class MoneyApplication extends Application<ApplicationConfiguration> {
    private static final List<Object> CONTROLLERS =
        Lists.newArrayList(new AccountController(), new MoneyController());

    @Override
    public void run(ApplicationConfiguration configuration,
                    Environment environment) {
        registerForJersey(environment);
    }

    private void registerForJersey(Environment environment) {
        JerseyEnvironment jerseyEnvironment = environment.jersey();
        CONTROLLERS.forEach(jerseyEnvironment::register);
    }
}