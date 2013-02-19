/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xmonpp.shell;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import java.util.logging.Logger;

import org.xmonpp.Settings;

/**
 *
 * @author guillaume
 */
public class Init {

    public static void cmdline(String[] args) {
        Options opts = new Options();

        Option o_debug = new Option("d", "debug", false, "Debug");
        opts.addOption(o_debug);

        Option o_settings = new Option("f", "file", true, "settings file");
        o_settings.setRequired(true);
        opts.addOption(o_settings);

        // Loading config
        CommandLineParser parser = new PosixParser();
        try {
            CommandLine cmd = parser.parse(opts, args);
            if (!Settings.load(cmd.getOptionValue("f"))) {
                Logger.getLogger("xmonpp").severe("Unable to load settings file: ".concat(cmd.getOptionValue("f")));
                System.exit(1);
            }

            // Debug mode
            if (cmd.hasOption("d")) {
                Settings.set("debug", "true");
            }

            // Cmdline parameters error
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            Logger.getLogger("xmonpp").severe(e.getMessage());
            formatter.printHelp("CLI", opts);
            System.exit(1);
        }
    }
}
