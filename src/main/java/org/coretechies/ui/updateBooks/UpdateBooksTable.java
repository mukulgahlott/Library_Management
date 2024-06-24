package org.coretechies.ui.updateBooks;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import org.coretechies.storeBook.BookDetails;
import org.coretechies.ui.AddBookScreen;

public class UpdateBooksTable {
    private final Connection connection;
    private final DefaultTableModel tableModel;
    private JFrame parentFrame = new JFrame();

    public UpdateBooksTable(Connection connection, DefaultTableModel tableModel) {
        this.connection = connection;
        this.tableModel = tableModel;
        this.parentFrame = parentFrame;
    }

    public void printTable() {
        tableModel.setRowCount(0); // Clear the table
        String query = "SELECT * FROM book";

        try (ResultSet rs = connection.createStatement().executeQuery(query)) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getString("BookName"),
                        rs.getString("Subject"),
                        rs.getString("Author"),
                        rs.getBoolean("Select1")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void search(String selectedItem, String searchText) {
        List<BookDetails> books = new ArrayList<>();
        String query = "SELECT * FROM book WHERE LOWER(" + selectedItem + ") LIKE ?";

        try (var ps = connection.prepareStatement(query)) {
            ps.setString(1, "%" + searchText.toLowerCase() + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                books.add(new BookDetails(
                        rs.getInt("id"),
                        rs.getString("BookName"),
                        rs.getString("Subject"),
                        rs.getString("Author")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tableModel.setRowCount(0);
        for (BookDetails book : books) {
            tableModel.addRow(new Object[]{book.name(), book.subject(), book.author()});
        }
    }

    public void addBooksInTable() {
        String name = AddBookScreen.nameT.getText();
        String subject = AddBookScreen.subjectT.getText();
        String author = AddBookScreen.authorT.getText();

        String query = "INSERT INTO book (BookName, Subject, Author, Select1) VALUES (?, ?, ?, ?)";

        try (var ps = connection.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setString(2, subject);
            ps.setString(3, author);
            ps.setBoolean(4, false);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(AddBookScreen.addBookF, "Book added successfully");
            printTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        List<Integer> idsToDelete = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            boolean isSelected = (boolean) tableModel.getValueAt(i, 3);
            if (isSelected) {
                idsToDelete.add((int) tableModel.getValueAt(i, 0));
            }
        }

        if (idsToDelete.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "No books selected for deletion");
        } else {
            String query = "DELETE FROM book WHERE id IN (" + idsToDelete.toString().replaceAll("[\\[\\]]", "") + ")";
            try {
                connection.createStatement().executeUpdate(query);
                JOptionPane.showMessageDialog(parentFrame, "Selected books deleted successfully");
                printTable();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int getBookId(int rowIndex) {
        return (int) tableModel.getValueAt(rowIndex, 0);
    }
}
