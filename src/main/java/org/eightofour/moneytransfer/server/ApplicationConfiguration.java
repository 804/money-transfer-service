package org.eightofour.moneytransfer.server;

import io.dropwizard.Configuration;
import io.dropwizard.jetty.HttpConnectorFactory;
import io.dropwizard.server.DefaultServerFactory;

import java.util.Collections;

public class ApplicationConfiguration extends Configuration {
    public ApplicationConfiguration() {
        super();
        disableAdmin();
        configureConnectionFactory();
    }

    private void disableAdmin() {
        ((DefaultServerFactory) getServerFactory()).setAdminConnectors(Collections.emptyList());
    }

    private void configureConnectionFactory() {
        configureHost();
        configurePort();
    }

    private void configureHost() {
        String httpHost = System.getProperty("http.host");
        if (httpHost != null) {
            getApplicationConnectionFactory().setBindHost(httpHost);
        }
    }

    private void configurePort() {
        Integer httpPort = getPortFromProperty();
        if (httpPort != null) {
            getApplicationConnectionFactory().setPort(httpPort);
        }
    }

    private Integer getPortFromProperty() {
        String portProperty = System.getProperty("http.port");
        return portProperty != null
            ? Integer.parseInt(portProperty)
            : null;
    }

    private HttpConnectorFactory getApplicationConnectionFactory() {
        return (HttpConnectorFactory) ((DefaultServerFactory) getServerFactory()).getApplicationConnectors().get(0);
    }
}