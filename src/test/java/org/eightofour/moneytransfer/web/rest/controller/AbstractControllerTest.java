package org.eightofour.moneytransfer.web.rest.controller;

import io.dropwizard.testing.DropwizardTestSupport;
import org.apache.commons.lang3.StringUtils;
import org.eightofour.moneytransfer.web.server.TransferConfiguration;
import org.eightofour.moneytransfer.web.server.TransferWebApplication;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import static io.dropwizard.testing.ResourceHelpers.resourceFilePath;

public abstract class AbstractControllerTest {
    private static final String ACCOUNT_URL_TEMPLATE = "http://localhost:%d/account";
    private static final DropwizardTestSupport<TransferConfiguration> TEST_SERVER =
            new DropwizardTestSupport<>(
                TransferWebApplication.class,
                resourceFilePath("test_setting.yaml")
            );

    @BeforeAll
    public static void beforeClass() {
        TEST_SERVER.before();
    }

    @AfterAll
    public static void afterClass() {
        TEST_SERVER.after();
    }

    protected Client client = new JerseyClientBuilder().build();

    protected Response createAccount() {
        return client.target(resolveURL(ACCOUNT_URL_TEMPLATE))
            .request()
            .post(Entity.text(StringUtils.EMPTY));
    }

    protected Response getAccount(String id) {
        return client.target(resolveURL(ACCOUNT_URL_TEMPLATE))
                .queryParam("id", id)
                .request()
                .get();
    }

    protected String resolveURL(String pattern) {
        return String.format(pattern, TEST_SERVER.getLocalPort());
    }
}