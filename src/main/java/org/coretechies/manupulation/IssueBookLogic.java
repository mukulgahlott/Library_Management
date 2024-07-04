package org.coretechies.manupulation;

import org.coretechies.ui.updateTables.UpdateBooksTable;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

import static org.coretechies.ui.IssueBookToStudentUi.*;
import static org.coretechies.ui.LibraryManageUi.allow;
import static org.coretechies.ui.LibraryManageUi.mainFrame;


public class IssueBookLogic {
    Statement st;
    final String showQuery = "SELECT * FROM book where id = " + UpdateBooksTable.idc + ";";

    public void fillBookDetails(Connection connection) throws SQLException {
        st = connection.createStatement();
        ResultSet showQ = st.executeQuery(showQuery);
        while (showQ.next()) {
            bookIDT.setText(String.valueOf(showQ.getInt("id")));
            bookNT.setText(showQ.getString("BookName"));

        }
    }

    public void issueBook(Connection connection) throws SQLException {


        String bookName = bookNT.getText();
        String enroll = enrollmentT.getText();
        String cls = (String) clsT.getSelectedItem();
        int id = 0;
        try {
            id = Integer.parseInt(bookIDT.getText());
            String InsertQ = "INSERT INTO record (BookId ,BookName,Enrollment ,Class )VALUES (%d,'%s','%s','%s');".formatted(id, bookName, enroll, cls);
            String SelectQ = "SELECT * FROM Book WHERE  Id = " + id + ";";

            if (!enroll.isBlank() && !Objects.requireNonNull(cls).isBlank()) {
                st = connection.createStatement();
                ResultSet rs = st.executeQuery(SelectQ);
                if (rs.next()) {
                    int issue = rs.getInt("Issued");
                    int remain = rs.getInt("remain");
                    if (remain > 0) {
                        issue++;
                        remain--;
                        String updateQ = "update book set issued = " + issue + ", remain =" + remain + " where id =" + id + ";";
                        st.executeUpdate(InsertQ);
                        st.executeUpdate(updateQ);
                    }
                    else {
                        allow = true;
                        issueBookF.dispose();
                        JOptionPane.showMessageDialog(mainFrame,"This Book does not Available");


                    }
                }
                allow = true;
                issueBookF.dispose();

            } else {
                JOptionPane.showMessageDialog(issueBookF, "please fill all values");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(issueBookF, "please fill number values");

        }


    }


}