package org.coretechies;

import org.coretechies.connection.CreateConnection;
import org.coretechies.ui.LibraryManageUi;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            Connection connection = CreateConnection.connectDB();
            LibraryManageUi libraryManageUi = new LibraryManageUi(connection);
            libraryManageUi.showBookManagementScreen();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
