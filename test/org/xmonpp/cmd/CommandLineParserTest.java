/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xmonpp.cmd;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
/**
 *
 * @author guillaume
 */
public class CommandLineParserTest {

    public CommandLineParserTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of CommandLineParser.
     * TODO: adding tests
     */
    @Test
    public void testCommandLineParser() {
        CommandLineParser p = new CommandLineParser("echo gne 'gnigni' | grep gn \\\nde la route");
        ArrayList<String> commands = p.getCommands();
        
        assertEquals(2, commands.size());
    }
}
