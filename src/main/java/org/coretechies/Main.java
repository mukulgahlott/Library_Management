package org.coretechies;


import com.nqadmin.rowset.JdbcRowSetImpl;
import org.coretechies.connection.CreateConnection;
import org.coretechies.ui.LibraryManageUi;

import javax.sql.RowSet;
import java.sql.Connection;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello and welcome!");


        Connection connection = CreateConnection.connectDB();
        LibraryManageUi show = new LibraryManageUi();
        show.showBookManagementScreen();

    }
}