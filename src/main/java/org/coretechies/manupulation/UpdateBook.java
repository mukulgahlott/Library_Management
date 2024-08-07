package org.coretechies.manupulation;

import org.coretechies.ui.updateTables.UpdateBooksTable;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import static org.coretechies.ui.AddBookScreen.*;
import static org.coretechies.ui.LibraryManageUi.allow;
import static org.coretechies.ui.LibraryManageUi.mainFrame;
import static org.coretechies.ui.updateTables.UpdateBooksTable.idc;


public class UpdateBook {
    final String showQuery = "SELECT * FROM book where id = " + UpdateBooksTable.idc + ";";
    Statement st;


    public void fillUpdate(Connection connection) throws SQLException {
        st = connection.createStatement();
        ResultSet showQ = st.executeQuery(showQuery);
        while (showQ.next()) {
            nameT.setText(showQ.getString("BookName"));
            subjectT.setText(showQ.getString("Subject"));
            authorT.setText(showQ.getString("Author"));
        }
    }

    public void updateChanges(Connection connection) throws SQLException {

        String name = nameT.getText();
        String subject = subjectT.getText();
        String author = authorT.getText();
        try {

            String updateQ = "UPDATE Book " +
                    "SET BookName = '" + name + "', Subject = '" + subject + "', Author = '" + author +
                    "' WHERE ID = " + idc + ";";
            String RecordUpdate ="UPDATE record Set BookName = '"+name+"' where BookID = " + idc + ";";

            if (!name.isBlank() && !subject.isBlank() && !author.isBlank()) {
                st = connection.createStatement();
                st.executeUpdate(updateQ);
                st.executeUpdate(RecordUpdate);
                addBookF.dispose();
                JOptionPane.showMessageDialog(mainFrame, "Book Updated Success fully");
                idc = 0;
                allow = true;
            }

        else {
                JOptionPane.showMessageDialog(addBookF, "please enter all values");
            }
        }
        catch (NumberFormatException e){
            JOptionPane.showMessageDialog(addBookF, "Please fill the Quantity with number value");

        }

    }
}
