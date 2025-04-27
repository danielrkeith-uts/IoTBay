package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector extends DB {
    public DBConnector() throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        conn = DriverManager.getConnection(URL);
    }

    public Connection openConnection() throws SQLException {
        conn.createStatement().executeUpdate("PRAGMA foreign_keys = ON;");
        return this.conn;
    }

    public void closeConnection() throws SQLException {
        this.conn.close();
    }
}