package org.eightofour.moneytransfer;

import org.eightofour.moneytransfer.web.server.TransferWebApplication;

/**
 * Main class for application running.
 *
 * @author Evgeny Zubenko
 */
public class Application {
    public static void main(String[] args) throws Exception {
        new TransferWebApplication().run(args);
    }
}