/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xmonpp.db;

import java.sql.Connection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import org.xmonpp.Settings;

/**
 *
 * @author guillaume
 */
public class ManagerTest {

    public ManagerTest() {
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
     * Test of getConnection method, of class Manager.
     */
    @Test
    public void testGetConnection() {
        String driver = "org.sqlite.JDBC";
        Settings.set("db.driver", driver);

        String url = "jdbc:sqlite::memory:";
        Settings.set("db.url", url);

        System.out.println("Db connection: ".concat(driver).concat(" - ").concat(url));
        Connection conn = Manager.getConnection();
        assertEquals(true, conn instanceof Connection);
    }
}
