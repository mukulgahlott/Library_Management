package org.coretechies.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CreateConnection {
    private CreateConnection() {}

    public static Connection connectDB() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:library.db");
        var st = conn.createStatement();
        st.execute("CREATE TABLE IF NOT EXISTS book (id INTEGER PRIMARY KEY, BookName TEXT, Subject TEXT, Author TEXT, Select1 BOOLEAN)");
        return conn;
    }
}
