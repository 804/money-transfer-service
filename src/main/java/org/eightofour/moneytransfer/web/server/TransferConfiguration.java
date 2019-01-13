package org.eightofour.moneytransfer.web.server;

import io.dropwizard.Configuration;
import io.dropwizard.jetty.HttpConnectorFactory;
import io.dropwizard.logging.DefaultLoggingFactory;
import io.dropwizard.server.DefaultServerFactory;

import java.util.Collections;

/**
 * Configuration class for Dropwizard web application
 * parameters customization:
 *  - admin endpoint disabling
 *  - HTTP port and host customization
 *  - log level customization
 *
 * @author Evgeny Zubenko
 */
public class TransferConfiguration extends Configuration {
    private static final String HTTP_HOST_PROPERTY = "http.host";
    private static final String HTTP_PORT_PROPERTY = "http.port";
    private static final String LOG_LEVEL_PROPERTY = "log.level";

    public TransferConfiguration() {
        super();
        disableAdminEndpoint();
        // HTTP server common parameters configuring by passed properties
        configureConnectionFactory();
        // logging level configuring by passed properties
        configureLogging();
    }

    private void disableAdminEndpoint() {
        ((DefaultServerFactory) getServerFactory()).setAdminConnectors(Collections.emptyList());
    }

    private void configureConnectionFactory() {
        configureHost();
        configurePort();
    }

    private void configureHost() {
        String httpHost = System.getProperty(HTTP_HOST_PROPERTY);
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
        String portProperty = System.getProperty(HTTP_PORT_PROPERTY);
        return portProperty != null
            ? Integer.parseInt(portProperty)
            : null;
    }

    private HttpConnectorFactory getApplicationConnectionFactory() {
        return (HttpConnectorFactory) ((DefaultServerFactory) getServerFactory()).getApplicationConnectors().get(0);
    }

    private void configureLogging() {
        String logLevel = System.getProperty(LOG_LEVEL_PROPERTY);
        if (logLevel != null) {
            ((DefaultLoggingFactory) getLoggingFactory()).setLevel(logLevel);
        }
    }
}