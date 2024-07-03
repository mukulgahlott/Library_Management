package org.coretechies.ui;

import org.coretechies.connection.CreateConnection;
import org.coretechies.storeBook.RecordDetails;
import org.coretechies.ui.updateTables.UpdateRecordBookTable;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.coretechies.ui.LibraryManageUi.mainFrame;
import static org.coretechies.ui.updateTables.UpdateBooksTable.idc;

public class RecordTableUi {
    public static String selectedClass = "All";
    public static JTable recordTable;
    public static List<RecordDetails> recordDet = new ArrayList<RecordDetails>();

    public void recordTable() throws SQLException {

        JPanel recordPanel = new JPanel();
        recordPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Records", TitledBorder.LEFT, TitledBorder.TOP));
        recordPanel.setBounds(20, 60, 580, 400);
        recordPanel.setLayout(new BorderLayout());
        mainFrame.add(recordPanel);
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"S.no", "BookID", "BookName", "Enrollment_No", "Class"}, 0);
        recordTable = new JTable(tableModel);
        recordTable.getTableHeader().setReorderingAllowed(false);

        recordPanel.add(new JScrollPane(recordTable), BorderLayout.CENTER);
        // Refresh the table to display data
        printTable(CreateConnection.connectDB());


    }

    public void selectClass() throws SQLException {

        String[] choice = {"All", "BCA", "BBA", "MCA", "MBA", "CS", " Other"};
        JComboBox<String> cb = new JComboBox<>(choice);
        cb.setBounds(365, 17, 105, 30);
        cb.setVisible(true);
        mainFrame.add(cb);
        cb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                selectedClass = (String) cb.getSelectedItem();
                try {
                    printTable(CreateConnection.connectDB());
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public void printTable(Connection connection) throws SQLException {

        recordDet.clear();
        String ShowQ = "select * from record where class = '%s';".formatted(selectedClass);
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(ShowQ);
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"S.no", "BookID", "BookName", "Enrollment_No", "Class", "Select"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 5) {
                    return true;
                }
                //all cells false
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // make cell 4 a boolean checkbox
                if (columnIndex == 5) {
                    return Boolean.class;
                }
                return String.class;
            }
        };
        String ShowQAll = "select * from record ";
        if (selectedClass.equals("All") || selectedClass.equals("null")) {
            rs = st.executeQuery(ShowQAll);
        }
        while (rs.next()) {
            Object[] rowData = new Object[]{rs.getInt("sno"), rs.getInt("bookId"), rs.getString("bookName"), rs.getString("enrollment"), rs.getString("class"), rs.getBoolean("select1")};
            tableModel.addRow(rowData);
            recordDet.add(new RecordDetails(rs.getInt("sno"), rs.getInt("bookId"), rs.getString("enrollment"), rs.getString("class")));

        }
        recordTable.setModel(tableModel);

    }

    public void deleteRecordButton() throws SQLException {
        JButton delete = new JButton("Delete");
        delete.setBounds(300, 470, 90, 30);
        mainFrame.add(delete);
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                UpdateRecordBookTable deleteRec = new UpdateRecordBookTable();
                deleteRec.delete(CreateConnection.connectDB());
            }
        });
    }

    public void showRecords() throws SQLException {

        idc = 0;
        LibraryManageUi ui = new LibraryManageUi();
        ui.bookHomeFrame();
        ui.searchUi();
        ui.tableChangeComboBox(new String[]{"ISSUE RECORD", "BOOKS "});
        selectClass();
        recordTable();
        deleteRecordButton();
        mainFrame.setVisible(true);

    }


}
