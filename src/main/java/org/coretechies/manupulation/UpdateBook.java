package org.coretechies.manupulation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import org.coretechies.ui.AddBookScreen;

public class UpdateBook {
    private final Connection connection;

    public UpdateBook(Connection connection) {
        this.connection = connection;
    }

    public void fillUpdate(int bookId) throws SQLException {
        String showQuery = "SELECT * FROM book WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(showQuery)) {
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                AddBookScreen.nameT.setText(rs.getString("BookName"));
                AddBookScreen.subjectT.setText(rs.getString("Subject"));
                AddBookScreen.authorT.setText(rs.getString("Author"));
            }
        }
    }

    public void updateChanges(int bookId) throws SQLException {
        String name = AddBookScreen.nameT.getText();
        String subject = AddBookScreen.subjectT.getText();
        String author = AddBookScreen.authorT.getText();
        String updateQ = "UPDATE book SET BookName = ?, Subject = ?, Author = ? WHERE id = ?";

        if (!name.isBlank() && !subject.isBlank() && !author.isBlank()) {
            try (PreparedStatement ps = connection.prepareStatement(updateQ)) {
                ps.setString(1, name);
                ps.setString(2, subject);
                ps.setString(3, author);
                ps.setInt(4, bookId);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(AddBookScreen.addBookF, "Book updated successfully");
            }
        } else {
            JOptionPane.showMessageDialog(AddBookScreen.addBookF, "Please enter all values");
        }
    }
}
