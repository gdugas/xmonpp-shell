/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xmonpp.shell;

import org.xmonpp.daemon.XmonPPDaemon;
import org.xmonpp.io.Input;
import org.xmonpp.io.InputListener;
import org.xmonpp.logger.Logger;

public class XmonppShell {

    public static Integer threadid = 0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // Parsing and validate cmdline, load config
        Init.cmdline(args);

        // Launching daemon
        XmonPPDaemon daemon = new XmonPPDaemon();
        
        // Registering message listener
        daemon.addInputListener(new InputListener() {
            @Override
            public void messageReceived(XmonPPDaemon daemon, Input input) {
            }
        });

        if (!daemon.login()) {
            Logger.error("Xmpp daemon login error - exiting program");
            System.exit(1);
        }

        // Looping while daemon is logged
        while (true) {
        }
    }
}
