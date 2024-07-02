package org.coretechies.ui;

import org.coretechies.connection.CreateConnection;
import org.coretechies.manupulation.UpdateBook;
import org.coretechies.ui.updateBooks.UpdateBooksTable;

import static org.coretechies.ui.updateBooks.UpdateBooksTable.idc;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.sql.SQLException;
import java.sql.Statement;

import static org.coretechies.ui.AddBookScreen.addBookF;

public class UpdateBookUi {

    public JPanel uPanel;
    protected JButton saveUpdate;

    public void updatePanel() {

        uPanel = new JPanel();
        uPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Update Book",
                TitledBorder.LEFT, TitledBorder.TOP));
        uPanel.setBounds(20, 20, 360, 300);
        uPanel.setLayout(null);

    }

        public void updateUi(){
            saveUpdate = new JButton("Save");
            saveUpdate.setBounds(100,140,60,20);
            addBookF.add(saveUpdate);
            saveUpdate.addActionListener(e -> {
                UpdateBook update = new UpdateBook();
                try {
                    update.updateChanges();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                idc = 0;
                addBookF.dispose();
                UpdateBooksTable printUpdate = new UpdateBooksTable();
                printUpdate.printTable(CreateConnection.connectDB());
            });

        }

        public void updateScreen(){
        AddBookScreen show = new AddBookScreen();
        show.addContactFrame();
        show.enterContactDetails();
        updatePanel();
        updateUi();
        show.closeButton();
        addBookF.setVisible(true);

        }



}
