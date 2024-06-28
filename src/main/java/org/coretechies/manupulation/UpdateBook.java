package org.coretechies.manupulation;

import org.coretechies.ui.updateBooks.UpdateBooksTable;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.coretechies.connection.CreateConnection.st;
import static org.coretechies.ui.AddBookScreen.*;
import static org.coretechies.ui.LibraryManageUi.allow;
import static org.coretechies.ui.LibraryManageUi.mainFrame;
import static org.coretechies.ui.updateBooks.UpdateBooksTable.idc;


public class UpdateBook {
    final String showQuery = "SELECT * FROM book where id = " + UpdateBooksTable.idc + ";";


    public void fillUpdate() throws SQLException {
        ResultSet showQ = st.executeQuery(showQuery);
        while (showQ.next()) {
            nameT.setText(showQ.getString("BookName"));
            subjectT.setText(showQ.getString("Subject"));
            authorT.setText(showQ.getString("Author"));
        }
    }

    public void updateChanges() throws SQLException {

        String name = nameT.getText();
        String subject = subjectT.getText();
        String author = authorT.getText();
        String updateQ = "UPDATE Book " +
                "SET BookName = '" + name + "', Subject = '" + subject + "', Author = '" + author + "' " +
                "WHERE ID = " + idc + ";";

        if (!name.isBlank() && !subject.isBlank() && !author.isBlank()) {
            st.executeUpdate(updateQ);
            JOptionPane.showMessageDialog(mainFrame,"Book Updated Success fully");
            allow = true;
        }
        else {
            JOptionPane.showMessageDialog(addBookF,"please enter all values");


        }

    }
}
