/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xmonpp.cmd;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmonpp.Logger;

/**
 *
 * @author Guillaume Dugas
 */
public class CommandLineParser {

    private String cmdline;
    private ArrayList<String> commands;
    private InputStream stdin;

    public CommandLineParser(String input) {
        this.parse(input);
        this.dispatch();
    }

    /*
     * Cmdline backslash detection
     */
    public static boolean isSplitted(String str) {
        if (str.matches("^.*\\\\+$")) {
            String end = str.replaceAll("^.*(\\\\+)$", "$1");
            if (end.length() % 2 == 1) {
                return true;
            }
        }
        return false;
    }


    /*
     * CommandLine and stdin detection
     */
    private void parse(String input) {
        int i, j;
        String[] lines = input.split("\n");
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        cmdline = lines[0];

        // Parsing input
        for (i = 1; i < lines.length; i++) {
            String line = lines[i];

            // cmdline continue on next line
            if (CommandLineParser.isSplitted(cmdline)) {
                cmdline = cmdline.substring(0, cmdline.length() - 1).concat(" ").concat(line);

                // end of cmdline: getting stdin
            } else {
                for (j = i; j < lines.length; j++) {
                    line = lines[j].concat("\n");
                    try {
                        buffer.write(line.getBytes());
                    } catch (Exception e) {
                        Logger.getLogger().severe("cmdline parsing error: ".concat(e.getMessage()));
                    }
                }
                this.stdin = new ByteArrayInputStream(buffer.toByteArray());
                break;
            }
        }

        // Cleaning cmdline which ending by a backslash
        if (CommandLineParser.isSplitted(cmdline)) {
            cmdline = cmdline.substring(0, cmdline.length() - 1);
        }
    }

    /*
     * Commands chaining detection
     */
    private void dispatch() {
        int i, prevIndex = 0;
        boolean opened = false;
        String bracket = null;

        commands = new ArrayList<String>();

        // Split commands: pipe detection
        for (i = 0; i < cmdline.length(); i++) {
            char c = cmdline.charAt(i), previous = ' ', next = ' ';
            boolean isBracket = c == '"' || c == '\'';
            String row;

            if (i > 0) {
                previous = cmdline.charAt(i - 1);
            }
            if (cmdline.length() > i + 1) {
                next = cmdline.charAt(i + 1);
            }

            if (isBracket && opened == false) {

                opened = true;
            } else if (isBracket && opened == true) {

                opened = false;
            } else if (c == '|' && previous != '\\' && opened == false) {
                row = cmdline.substring(prevIndex, i).trim();
                if ((int) row.length() > 0) {
                    commands.add(row);
                }
                prevIndex = i + 1;
            }
        }

        if (prevIndex > 0 && prevIndex < cmdline.length()) {
            commands.add(cmdline.substring(prevIndex).trim());
        } else {
            commands.add(cmdline);
        }

    }

    public ArrayList<String> getCommands() {
        return this.commands;
    }

    public InputStream getStdin() {
        return this.stdin;
    }
}
