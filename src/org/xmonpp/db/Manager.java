/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xmonpp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.xmonpp.Settings;


/**
 *
 * @author guillaume
 */
public class Manager {

    static private Connection conn;
    static protected final Logger logger = Logger.getLogger("xmonpp");

    static public Connection createConnection(String driver, String url) {
        Connection dbconn = null;

        try {
            Class.forName(driver);
        } catch (Exception e) {
            logger.severe("Sql driver error (".concat(driver).concat(") : ").concat(e.getMessage()));
        }

        try {
            dbconn = DriverManager.getConnection(url);
            return dbconn;
        } catch (SQLException e) {
            logger.severe("Database connection error: ".concat(e.getMessage()));
        }

        return dbconn;
    }

    static public Connection getConnection() {
        if (Manager.conn == null) {
            String driver = Settings.get("db.driver", "").toString();
            String url = Settings.get("db.url", "").toString();
            Manager.conn = Manager.createConnection(driver, url);
        }
        return Manager.conn;
    }
}
