/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xmonpp.shell;

import org.xmonpp.cmd.Command;
import org.xmonpp.cmd.CommandLineParser;
import org.xmonpp.Settings;
import org.xmonpp.Daemon;
import org.xmonpp.io.Input;
import org.xmonpp.io.Output;
import org.xmonpp.Loader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 *
 * @author Guillaume Dugas
 */
public class InputProcessor extends Thread {

    private CommandLineParser parser;
    private Daemon daemon;
    private Input input;
    protected Logger logger = Logger.getLogger("xmonpp");

    public InputProcessor(Daemon daemon, Input input) {
        this.daemon = daemon;
        this.input = input;
    }

    /*
     * Receive and parse message, detect commands, load them and call process method
     */
    @Override
    public void run() {
        this.parser = new CommandLineParser(input.getMessage().getBody());
        ArrayList<String> commands = this.parser.getCommands();
        Command runnables[] = new Command[commands.size()];

        // Scanning requested commands
        int i, size = commands.size();
        for (i = 0; i < size; i++) {
            String cmdname = commands.get(i);
            String classname = (String) Settings.get("commands.".concat(cmdname));
            if (classname == null) {
                this.commandUnavailable(cmdname);
                return;
            }

            // Init command
            try {
                Class cls = Loader.loadClass(classname);
                Command runnable = (Command) cls.newInstance();

                runnables[i] = runnable;
                if (i == 0) {
                    InputStream in = this.parser.getStdin();
                    if (in == null) {
                        runnable.setStdin(new ByteArrayInputStream("".getBytes()));
                    } else {
                        runnable.setStdin(in);
                    }

                    runnable.setStdout(new ByteArrayOutputStream());
                } else {
                    PipedInputStream in = new PipedInputStream();
                    PipedOutputStream out = new PipedOutputStream();
                    Command previous = runnables[i - 1];

                    if (i == size - 1) {
                        runnable.setStdout(new ByteArrayOutputStream());
                    } else {
                        runnable.setStdout(new ByteArrayOutputStream());
                    }

                    in.connect(out);
                    previous.setStdout(out);
                    runnable.setStdin(in);
                }

                // Init errors:
            } catch (Exception e) {
                logger.severe("Command `".concat(cmdname).concat("` error: unable to load class ").concat(classname));
            }
        }

        // Execute commands
        for (i = 0; i < runnables.length; i++) {
            Command runnable = runnables[i];
            runnable.start();
        }

        Command last = runnables[runnables.length - 1];

        try {
            while (last.isAlive()) {
            }
            Output o = new Output(this.input.getChat(), last.getStdout().toString());
            o.send();
        } catch (Exception e) {
            String message = "Execution error: ".concat(e.getMessage());

            logger.severe(message);
            Output o = new Output(this.input.getChat(), message);
        }

    }

    public void commandUnavailable(String name) {
        String message = "Unavailable command:".concat(name);
        logger.finest(message);

        Output output = new Output(this.input.getChat(), message);
        output.send();
    }
}
