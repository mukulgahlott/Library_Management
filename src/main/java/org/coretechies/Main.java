package org.coretechies;


import com.nqadmin.rowset.JdbcRowSetImpl;
import org.coretechies.connection.CreateConnection;
import org.coretechies.ui.LibraryManageUi;

import javax.sql.RowSet;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello and welcome!");

        CreateConnection connection = CreateConnection.getInstance();
        connection.connectDB();
        LibraryManageUi show = new LibraryManageUi();
        show.showBookManagementScreen();

    }
}