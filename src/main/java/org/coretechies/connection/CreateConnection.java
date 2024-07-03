package org.coretechies.connection;

import java.sql.*;


public class CreateConnection {

    private static CreateConnection instance;

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
    public static Connection connectDB() {
        try {
            final String url = "jdbc:mysql://localhost:3306/my_database";
            final String user = "root";
            final String password = "pass@123";

            Connection con = DriverManager.getConnection(url, user, password);
            Statement st = con.createStatement();
            String query = "CREATE DATABASE IF NOT EXISTS my_database";
            st.executeUpdate(query);

            String query2 = "create table if not exists book ( id int auto_increment primary key,bookName varchar (30), subject varchar (30), author varchar (30));";
            st.executeUpdate(query2);

            return con;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}