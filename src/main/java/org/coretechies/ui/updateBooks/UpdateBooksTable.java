package org.coretechies.ui.updateBooks;

import org.coretechies.connection.CreateConnection;
import org.coretechies.storeBook.BookDetails;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.coretechies.connection.CreateConnection.st;
import static org.coretechies.ui.AddBookScreen.*;
import static org.coretechies.ui.LibraryManageUi.*;

public class UpdateBooksTable {

    public static String deleteName = "";
    protected String name, subject, author;
    public ResultSet showQ ;
    public int addQ;
    public static String sortQuery;
    final String sortQueryN = "SELECT * FROM book " + "ORDER BY bookName;";
    final String sortQueryS = "SELECT * FROM book " + "ORDER BY Subject;";
    final String sortQueryA = "SELECT * FROM book " + "ORDER BY Author;";
    final String DeleteQuery = "DELETE FROM book WHERE BookName ='" + deleteName + "';";
    protected Object[] rowData;
    static List<BookDetails> bookDetail = new ArrayList<>();


//print Book table
    public void printTable() {
        //  Create the table model and set column names
        String[] columnNames = new String[]{"NAME", "SUBJECT", "AUTHOR"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        CreateConnection connection = CreateConnection.getInstance();
        try {
            connection.connectDB();
            if (selectedItem.equals("Author")) {
                sortQuery = sortQueryA;
            } else if (selectedItem.equals("Subject")) {
                sortQuery = sortQueryS;
            } else {
                sortQuery = sortQueryN;
            }

            showQ = st.executeQuery(sortQuery);
            while (showQ.next()) {
                System.out.println(showQ.getString("BookName") + " " + showQ.getString("Subject") + " " + showQ.getString("Author"));
                rowData = new Object[]{showQ.getString("BookName"), showQ.getString("Subject"), showQ.getString("Author")};
                tableModel.addRow(rowData);
                bookDetail.add(new BookDetails(showQ.getString("BookName"), showQ.getString("Subject"), showQ.getString("Author")));

            }
            booksTable.setModel(tableModel);
            booksTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    // Get the selected row
                    int key = booksTable.getSelectedRow();
                    if(key>-1) {
                        deleteName = bookDetail.get(key).name();
                    }
                }
            });

        } catch (SQLException e) {
            System.out.println("404 : DATA NOT FOUND");
        }
    }
//delete Book from database
    public void delete() {
        try {
            st.executeUpdate(DeleteQuery);
            printTable();
        }
        catch (SQLException e){
            JOptionPane.showMessageDialog(mainFrame,"please select the book");
        }
    }


    public void addBooksInTable() {
        name = nameT.getText();
        subject = subjectT.getText();
        author = authorT.getText();

        if (!name.isBlank() | !subject.isBlank() | !author.isBlank()) {
            String addQuery = "INSERT INTO Book (BookName, Subject, Author)" + "VALUES ('" + name + "'," + "'" + subject + "','" + author + "')";

            try {
                addQ = st.executeUpdate(addQuery);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(addBookF, "Error : Some thing went wrong ");
                addBookF.dispose();

            }
            addBookF.dispose();
            printTable();

        } else {
            JOptionPane.showMessageDialog(addBookF, "Please fill the values");
        }

    }

    public void search() {
        String[] columnNames = new String[]{"NAME", "SUBJECT", "AUTHOR"};
        DefaultTableModel searchTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        try {

            showQ = st.executeQuery(sortQuery);
            String key = searchT.getText();
            while (showQ.next()) {
                name = showQ.getString("BookName");
                subject = showQ.getString("Subject");
                author = showQ.getString("Author");

                if (name.contains(key) | subject.contains(key) | author.contains(key)) {

                    System.out.println(showQ.getString("BookName") + " " + showQ.getString("Subject") + " " + showQ.getString("Author"));
                    rowData = new Object[]{showQ.getString("BookName"), showQ.getString("Subject"), showQ.getString("Author")};
                    searchTableModel.addRow(rowData);
                }
            }
            if (searchTableModel.getRowCount() == 0) {
                System.out.println("Contact not found");
            }
            booksTable.setModel(searchTableModel);
        } catch (SQLException e) {
            System.out.println("404 : DATA NOT FOUND");
        }
    }


}
