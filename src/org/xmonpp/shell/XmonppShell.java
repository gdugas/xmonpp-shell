/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xmonpp.shell;

import org.xmonpp.Daemon;
import org.xmonpp.io.Input;
import org.xmonpp.io.InputListener;
import org.xmonpp.Logger;
import org.xmonpp.db.Manager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;

public class XmonppShell {

    public static Integer threadid = 0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // Parsing and validate cmdline, load config
        Init.cmdline(args);

        // Launching daemon
        Daemon daemon = new Daemon();
        if (!daemon.login()) {
            Logger.getLogger().severe("Xmpp daemon login error - exiting program");
            System.exit(1);
        }

        // Registering runtime
        Connection conn = Manager.getConnection();
        try {
            Date date = new Date();
            PreparedStatement state = conn.prepareStatement("INSERT INTO xmonpp_process (start) VALUES (?)");
            state.setLong(1, date.getTime());
            state.executeUpdate();

        } catch (Exception e) {
            Logger.getLogger().severe("Process recording error: ".concat(e.getMessage()));
            System.exit(1);
        }


        // Registering message listener
        daemon.addInputListener(new InputListener() {

            @Override
            public void messageReceived(Daemon daemon, Input input) {
                InputProcessor processor = new InputProcessor(daemon, input);
                processor.start();
            }
        });


        // Looping while daemon is logged
        while (true) {
        }
    }
}
