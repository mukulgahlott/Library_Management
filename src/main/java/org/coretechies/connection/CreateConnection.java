package org.coretechies.connection;

import java.sql.*;

public class CreateConnection {

    private static CreateConnection instance;
    final String url = "jdbc:mysql://localhost:3306/my_database";
    final String user = "root";
    final String password = "pass@123";
    public static Statement st;

    //    Empty constructor
    private CreateConnection() {
    }

    public static CreateConnection getInstance() {

        if (instance == null) {
            // if instance is null, initialize
            instance = new CreateConnection();
        }
        return instance;
    }

//create connection with database
    public void connectDB() {
        try {
            Connection con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
