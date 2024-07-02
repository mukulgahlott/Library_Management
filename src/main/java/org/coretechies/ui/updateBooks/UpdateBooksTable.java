package org.coretechies.ui.updateBooks;

import org.coretechies.connection.CreateConnection;
import org.coretechies.storeBook.BookDetails;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


import static org.coretechies.ui.AddBookScreen.*;
import static org.coretechies.ui.LibraryManageUi.*;


//there are all the functions tha perform changes in JTable

public class UpdateBooksTable {

    public static int idc;
    static int[] key;
    protected String name, subject, author;
    public ResultSet showQ;
    public int addQ;
    public static String sortQuery;
    private final String sortQueryN = "SELECT * FROM book " + "ORDER BY bookName;";
    private final String sortQueryS = "SELECT * FROM book " + "ORDER BY Subject;";
    private final String sortQueryA = "SELECT * FROM book " + "ORDER BY Author;";
    private final String sortQueryND = "SELECT * FROM book " + "ORDER BY BookName desc;";
    private final String sortQuerySD = "SELECT * FROM book " + "ORDER BY Subject desc;";
    private final String sortQueryAD = "SELECT * FROM book " + "ORDER BY Author desc;";
    protected Object[] rowData;
    static List<BookDetails> bookDetail = new ArrayList<>();
    public Statement st;


    //print Book JTable
    public void printTable(Connection connection) {
        //  Create the table model and set column names
        String[] columnNames = new String[]{"ID", "NAME", "SUBJECT", "AUTHOR", "SELECT"};
        bookDetail.clear();
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false but cell 4 is true
                if (column == 4) {
                    return true;
                }
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // make cell 4 a boolean checkbox
                if (columnIndex == 4) {
                    return Boolean.class;
                }
                return String.class;
            }
        };

        try {
            CreateConnection.connectDB();

//    Checking the combobox list selected choice
            switch (selectedItem) {
                case "Author" -> sortQuery = sortQueryA;
                case "Subject" -> sortQuery = sortQueryS;
                case "AuthorD" -> sortQuery = sortQueryAD;
                case "SubjectD" -> sortQuery = sortQuerySD;
                case "NameD" -> sortQuery = sortQueryND;
                default -> sortQuery = sortQueryN;
            }
// Print the table on LibraryManagement home screen
            st = connection.createStatement();
            showQ = st.executeQuery(sortQuery);
            while (showQ.next()) {
                rowData = new Object[]{showQ.getInt("id"), showQ.getString("BookName"), showQ.getString("Subject"), showQ.getString("Author"), showQ.getBoolean("Select1")};
                tableModel.addRow(rowData);
                bookDetail.add(new BookDetails(showQ.getInt("id"), showQ.getString("BookName"), showQ.getString("Subject"), showQ.getString("Author")));

            }
            booksTable.setModel(tableModel);

            //   identify the selecting date
            booksTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    allow = true;
                    // Get the selected row
                    key = booksTable.getSelectedRows();
                    for (Integer i : key) {
                        idc = bookDetail.get(i).id();
                    }
                }
            });
        } catch (SQLException e) {
            System.out.println("404 : DATA NOT FOUND");
        }
    }

    //delete Book from database
    public void delete(Connection connection) {
        try {
            boolean deleteIndex = false;
            for (int j = 0; j < bookDetail.size(); j++) {
                deleteIndex = (boolean) booksTable.getValueAt(j, 4);

                if (deleteIndex) {
                    int id = (int) booksTable.getValueAt(j, 0);
                    String DeleteQuery = "DELETE FROM book WHERE Id = " + id + ";";
                    st = connection.createStatement();
                    st.executeUpdate(DeleteQuery);
                }
            }
            if (searchT.getText().isBlank()) {
                printTable(CreateConnection.connectDB());
            } else {
                search(CreateConnection.connectDB());
            }
            idc = 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(mainFrame, "please select the book");
        }
    }

    // Add new book in table
    public void addBooksInTable(Connection connection) {
        name = nameT.getText();
        subject = subjectT.getText();
        author = authorT.getText();

        if (!name.isBlank() && !subject.isBlank() && !author.isBlank()) {
            String addQuery = "INSERT INTO Book (BookName, Subject, Author)" + "VALUES ('" + name + "'," + "'" + subject + "','" + author + "')";

            try {
                st = connection.createStatement();
                addQ = st.executeUpdate(addQuery);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(addBookF, "Error : Some thing went wrong ");
                addBookF.dispose();

            }
            printTable(CreateConnection.connectDB());
            addBookF.dispose();
            allow = true;

        } else {
            JOptionPane.showMessageDialog(addBookF, "Please fill the values");
        }

    }

    public void search(Connection connection) {
        String[] columnNames = new String[]{"ID", "NAME", "SUBJECT", "AUTHOR", "SELECT"};
        bookDetail.clear();
        DefaultTableModel searchTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                if (column == 4) {
                    return true;
                }
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) {
                    return Boolean.class;
                }
                return String.class;
            }
        };
        try {
            st = connection.createStatement();
            showQ = st.executeQuery(sortQuery);

            String key = searchT.getText().toLowerCase();
            while (showQ.next()) {
                name = showQ.getString("BookName");
                subject = showQ.getString("Subject");
                author = showQ.getString("Author");

                if (name.toLowerCase().contains(key) | subject.toLowerCase().contains(key) | author.toLowerCase().contains(key)) {

                    rowData = new Object[]{showQ.getInt("Id"), showQ.getString("BookName"), showQ.getString("Subject"), showQ.getString("Author"), showQ.getBoolean("Select1")};
                    searchTableModel.addRow(rowData);
                    bookDetail.add(new BookDetails(showQ.getInt("id"), showQ.getString("BookName"), showQ.getString("Subject"), showQ.getString("Author")));
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
