package org.coretechies.ui;

import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.coretechies.manupulation.UpdateBook;
import org.coretechies.ui.updateBooks.UpdateBooksTable;

public class UpdateBookUi {
    private JPanel uPanel;
    private JButton saveUpdate;
    private final Connection connection;
    private final int bookId;
    private final UpdateBooksTable updateBooksTable;

    public UpdateBookUi(Connection connection, int bookId, UpdateBooksTable updateBooksTable) {
        this.connection = connection;
        this.bookId = bookId;
        this.updateBooksTable = updateBooksTable;
    }

    public void updatePanel() {
        uPanel = new JPanel();
        uPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Update Book", TitledBorder.LEFT, TitledBorder.TOP));
        uPanel.setBounds(20, 20, 360, 300);
        uPanel.setLayout(null);
    }

    public void updateUi() {
        saveUpdate = new JButton("Save");
        saveUpdate.setBounds(100, 140, 60, 20);
        AddBookScreen.addBookF.add(saveUpdate);
        saveUpdate.addActionListener(e -> {
            UpdateBook update = new UpdateBook(connection);
            try {
                update.updateChanges(bookId);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            AddBookScreen.addBookF.dispose();
            updateBooksTable.printTable();
        });
    }

    public void updateScreen() {
        AddBookScreen show = new AddBookScreen(connection, updateBooksTable);
        show.addContactFrame();
        show.enterContactDetails();
        updatePanel();
        updateUi();
        show.closeButton();
        try {
            UpdateBook updateBook = new UpdateBook(connection);
            updateBook.fillUpdate(bookId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AddBookScreen.addBookF.setVisible(true);
    }
}
