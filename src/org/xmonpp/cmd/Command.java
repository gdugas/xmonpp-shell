/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xmonpp.cmd;

import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author guillaume
 */
abstract public class Command extends Thread {

    protected InputStream stdin;
    protected OutputStream stdout;
    protected OutputStream stderr;

    abstract boolean validate();
    
    public InputStream getStdin() {
        return this.stdin;
    }

    public OutputStream getStdout() {
        return this.stdout;
    }

    public OutputStream getStderr() {
        return this.stderr;
    }

    public void setStdin(InputStream in) {
        this.stdin = in;
    }

    public void setStdout(OutputStream out) {
        this.stdout = out;
    }

    public void setStderr(OutputStream err) {
        this.stderr = err;
    }
}
