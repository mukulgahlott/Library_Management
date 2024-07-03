package org.coretechies.ui.updateTables;

import org.coretechies.connection.CreateConnection;
import org.coretechies.storeBook.RecordDetails;
import org.coretechies.ui.RecordTableUi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.coretechies.ui.LibraryManageUi.*;
import static org.coretechies.ui.LibraryManageUi.mainFrame;
import static org.coretechies.ui.RecordTableUi.recordDet;
import static org.coretechies.ui.RecordTableUi.recordTable;

public class UpdateRecordBookTable {
    Statement st;

    public void delete(Connection connection) {
        int recordId = 0;
        int bookId = 0;
        try {
            boolean i = true;
            boolean deleteIndex = false;
            for (int j = 0; j < recordDet.size(); j++) {
                deleteIndex = (boolean) recordTable.getValueAt(j, 5);
                bookId = (Integer) recordTable.getValueAt(j, 1);
                String SelectQ = "SELECT * FROM Book WHERE  Id = " + bookId + ";";

                if (deleteIndex) {
                    recordId = (int) recordTable.getValueAt(j, 0);
                    String DeleteQuery = "DELETE FROM record WHERE sno = %d;".formatted(recordId);
                    st = connection.createStatement();
                    st.executeUpdate(DeleteQuery);
                    ResultSet rs = st.executeQuery(SelectQ);
                    if (rs.next()) {
                        int issue = rs.getInt("Issued");
                        int remain = rs.getInt("remain");
                        issue--;
                        remain++;
                        String updateQ = "update book set issued = " + issue + ", remain =" + remain + " where id =" + bookId + ";";
                        st.executeUpdate(updateQ);
                    }
                    i = false;
                }
            }
            if (i) {
                JOptionPane.showMessageDialog(mainFrame, "Please select check box to delete Book ");
            }
            if (searchT.getText().isBlank()) {
                RecordTableUi printTable = new RecordTableUi();
                printTable.printTable(CreateConnection.connectDB());
            } else {

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(mainFrame, "please select the book");
        }
    }

    public void searchRecord(Connection connection) {
        String[] columnNames = new String[]{"S.No", "BookId", "BookName", "Enrolment_No", "Class", "Select"};
        recordDet.clear();
        DefaultTableModel searchTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                if (column == 5) {
                    return true;
                }
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 5) {
                    return Boolean.class;
                }
                return String.class;
            }
        };
        try {
            st = connection.createStatement();
            String showQuery = "SELECT * FROM record";
            ResultSet showQ = st.executeQuery(showQuery);

            String key = searchT.getText().toLowerCase();
            while (showQ.next()) {
                String enroll = showQ.getString("Enrollment");
                String sn = showQ.getString("SNO");
                String cls = showQ.getString("class");
                String id = String.valueOf(showQ.getInt("BookId"));

                if (id.toLowerCase().contains(key) | enroll.toLowerCase().contains(key) | sn.toLowerCase().contains(key) | cls.toLowerCase().contains(key)) {

                    Object[] rowData = new Object[]{showQ.getInt("sno"), showQ.getInt("BookId"), showQ.getString("BookName"), showQ.getString("Enrollment"), showQ.getString("Class"), showQ.getBoolean("Select1")};
                    searchTableModel.addRow(rowData);
                    recordDet.add(new RecordDetails(showQ.getInt("sno"), showQ.getInt("BookId"), showQ.getString("Enrollment"), showQ.getString("Class")));
                }
            }
            if (searchTableModel.getRowCount() == 0) {
                System.out.println("Contact not found");
            }
            recordTable.setModel(searchTableModel);
        } catch (SQLException e) {
            System.out.println("404 : DATA NOT FOUND");
        }
    }

}
