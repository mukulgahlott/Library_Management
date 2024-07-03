package org.coretechies.ui.updateTables;

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
    protected Object[] rowData;
    static List<BookDetails> bookDetail = new ArrayList<>();
    public Statement st;


    //print Book JTable
    public void printTable(Connection connection) {
        //  Create the table model and set column names
        String[] columnNames = new String[]{"ID", "NAME", "SUBJECT", "AUTHOR", "QUANTITY", "ISSUED", "REMAIN", "SELECT"};
        bookDetail.clear();
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false but cell 4 is true
                if (column == 7) {
                    return true;
                }
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // make cell 4 a boolean checkbox
                if (columnIndex == 7) {
                    return Boolean.class;
                }
                return String.class;
            }
        };

        try {
            CreateConnection.connectDB();

//    Checking the combobox list selected choice
            String sortQueryN = "SELECT * FROM book " + "ORDER BY bookName;";
            String sortQueryS = "SELECT * FROM book " + "ORDER BY Subject;";
            String sortQueryA = "SELECT * FROM book " + "ORDER BY Author;";
            String sortQueryND = "SELECT * FROM book " + "ORDER BY BookName desc;";
            String sortQuerySD = "SELECT * FROM book " + "ORDER BY Subject desc;";
            String sortQueryAD = "SELECT * FROM book " + "ORDER BY Author desc;";
            switch (selectedSort) {
                case "Author" -> sortQuery = sortQueryA;
                case "Subject" -> sortQuery = sortQueryS;
                case "AuthorD" -> sortQuery = sortQueryAD;
                case "SubjectD" -> sortQuery = sortQuerySD;
                case "NameD" -> sortQuery = sortQueryND;
                case "Name" -> sortQuery = sortQueryN;
                case " IDD" -> sortQuery = "SELECT * FROM book ORDER BY id desc;";
                default -> sortQuery = "SELECT * FROM book ORDER BY Id;";
            }
// Print the table on LibraryManagement home screen
            st = connection.createStatement();
            showQ = st.executeQuery(sortQuery);
            while (showQ.next()) {
                rowData = new Object[]{showQ.getInt("id"), showQ.getString("BookName"), showQ.getString("Subject"), showQ.getString("Author"), showQ.getInt("Quantity"), showQ.getInt("Issued"), showQ.getInt("remain"), showQ.getBoolean("Select1")};
                tableModel.addRow(rowData);
                bookDetail.add(new BookDetails(showQ.getInt("id"), showQ.getString("BookName"), showQ.getString("Subject"), showQ.getString("Author")));

            }
            booksTable.setModel(tableModel);

            //   identify the selecting date
            booksTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
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
            boolean i = true;
            boolean deleteIndex = false;
            for (int j = 0; j < bookDetail.size(); j++) {
                deleteIndex = (boolean) booksTable.getValueAt(j, 7);

                if (deleteIndex) {
                    int id = (int) booksTable.getValueAt(j, 0);
                    String DeleteQuery = "DELETE FROM book WHERE Id = %d;".formatted(id);
                    st = connection.createStatement();
                    st.executeUpdate(DeleteQuery);
                    i = false;
                }
            }
            if (i) {
                JOptionPane.showMessageDialog(mainFrame, "Please select check box to delete Book ");
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
        int remaining = 0;
        int quantity = 0;
        try {
            quantity = Integer.parseInt(quantityT.getText());
            remaining = quantity;
            if (!name.isBlank() && !subject.isBlank() && !author.isBlank() && quantity != 0) {
                String addQuery = "INSERT INTO Book (BookName, Subject, Author,Quantity, remain)VALUES ('%s','%s','%s',%d,%d);".formatted(name, subject, author, quantity, remaining);

                try {
                    st = connection.createStatement();
                    addQ = st.executeUpdate(addQuery);
                    printTable(CreateConnection.connectDB());
                    addBookF.dispose();
                    allow = true;
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(addBookF, "Error : the characters should be less than 20 ");
                }
            } else {
                JOptionPane.showMessageDialog(addBookF, "Please fill the values");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(addBookF, "Please fill the Quantity with number value ");
        }

    }

    public void search(Connection connection) {
        String[] columnNames = new String[]{"ID", "NAME", "SUBJECT", "AUTHOR", "QUANTITY", "ISSUED", "REMAIN", "SELECT"};
        bookDetail.clear();
        DefaultTableModel searchTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                if (column == 7) {
                    return true;
                }
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 7) {
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
                String id = String.valueOf(showQ.getInt("id"));

                if (id.toLowerCase().contains(key) | name.toLowerCase().contains(key) | subject.toLowerCase().contains(key) | author.toLowerCase().contains(key)) {

                    rowData = new Object[]{showQ.getInt("Id"), showQ.getString("BookName"), showQ.getString("Subject"), showQ.getString("Author"), showQ.getInt("Quantity"), showQ.getInt("Issued"), showQ.getInt("remain"), showQ.getBoolean("Select1")};
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
