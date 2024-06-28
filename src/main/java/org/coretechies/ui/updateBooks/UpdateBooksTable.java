package org.coretechies.ui.updateBooks;

import org.coretechies.connection.CreateConnection;
import org.coretechies.storeBook.BookDetails;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.coretechies.connection.CreateConnection.st;
import static org.coretechies.ui.AddBookScreen.*;
import static org.coretechies.ui.LibraryManageUi.*;

public class UpdateBooksTable {

    public static int idc;
    static int[] key;
    protected String name, subject, author;
    public ResultSet showQ;
    public int addQ;
    public static String sortQuery;
    final String sortQueryN = "SELECT * FROM book " + "ORDER BY bookName;";
    final String sortQueryS = "SELECT * FROM book " + "ORDER BY Subject;";
    final String sortQueryA = "SELECT * FROM book " + "ORDER BY Author;";
    final String sortQueryND = "SELECT * FROM book " + "ORDER BY BookName desc;";
    final String sortQuerySD = "SELECT * FROM book " + "ORDER BY Subject desc;";
    final String sortQueryAD = "SELECT * FROM book " + "ORDER BY Author desc;";
    protected Object[] rowData;
    static List<BookDetails> bookDetail = new ArrayList<>();

    //print Book table
    public void printTable() {
        //  Create the table model and set column names
        String[] columnNames = new String[]{"ID","NAME", "SUBJECT", "AUTHOR","SELECT"};
        bookDetail.clear();
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable (int row, int column) {
                //all cells false
                if (column==4){
                    return true;
                }
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex==4){
                    return Boolean.class;
                }
                return String.class;
            }
        };

        CreateConnection connection = CreateConnection.getInstance();
        try {
            connection.connectDB();
            if (selectedItem.equals("Author")) {
                sortQuery = sortQueryA;
            } else if (selectedItem.equals("Subject")) {
                sortQuery = sortQueryS;
            } else if (selectedItem.equals("AuthorD")) {
                sortQuery = sortQueryAD;
            } else if (selectedItem.equals("SubjectD")) {
                sortQuery = sortQuerySD;
            }else if (selectedItem.equals("NameD")) {
                sortQuery = sortQueryND;
            }
            else {
                sortQuery = sortQueryN;
            }

            showQ = st.executeQuery(sortQuery);
            while (showQ.next()) {
                rowData = new Object[]{showQ.getInt("id"),showQ.getString("BookName"), showQ.getString("Subject"), showQ.getString("Author"),showQ.getBoolean("Select1")};
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
                    for (Integer i : key){
                        idc = bookDetail.get(i).id();
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
                boolean deleteIndex = false ;

                for (int j = 0;j<bookDetail.size(); j++) {
                   deleteIndex = (boolean) booksTable.getValueAt(j, 4);

                   if (deleteIndex){
                       int id = (int) booksTable.getValueAt(j,0);
                       String DeleteQuery = "DELETE FROM book WHERE Id = "+ id +";";
                       st.executeUpdate(DeleteQuery);
                   }
                }
                if (searchT.getText().isBlank()){
                    printTable();
                }
                else {
                    search();
                }
                idc = 0;
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(mainFrame, "please select the book");
            }

    }


    public void addBooksInTable() {
        name = nameT.getText();
        subject = subjectT.getText();
        author = authorT.getText();

        if (!name.isBlank() && !subject.isBlank() && !author.isBlank()) {
            String addQuery = "INSERT INTO Book (BookName, Subject, Author)" + "VALUES ('" + name + "'," + "'" + subject + "','" + author + "')";

            try {
                addQ = st.executeUpdate(addQuery);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(addBookF, "Error : Some thing went wrong ");
                addBookF.dispose();

            }
            printTable();
            addBookF.dispose();
            allow = true;

        } else {
            JOptionPane.showMessageDialog(addBookF, "Please fill the values");
        }

    }

    public void search() {
        String[] columnNames = new String[]{"ID","NAME", "SUBJECT", "AUTHOR","SELECT"};
        bookDetail.clear();
        DefaultTableModel searchTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                if (column==4){
                    return true;
                }
                return false;
            }
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex==4){
                return Boolean.class;
            }
            return String.class;
        }
        };
        try {

            showQ = st.executeQuery(sortQuery);
            String key = searchT.getText().toLowerCase();
            while (showQ.next()) {
                name = showQ.getString("BookName");
                subject = showQ.getString("Subject");
                author = showQ.getString("Author");

                if (name.toLowerCase().contains(key) | subject.toLowerCase().contains(key) | author.toLowerCase().contains(key)) {

                    rowData = new Object[]{showQ.getInt("Id"),showQ.getString("BookName"), showQ.getString("Subject"), showQ.getString("Author"),showQ.getBoolean("Select1")};
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
