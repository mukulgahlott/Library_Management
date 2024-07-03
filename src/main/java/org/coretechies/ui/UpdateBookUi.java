package org.coretechies.ui;

import org.coretechies.connection.CreateConnection;
import org.coretechies.manupulation.UpdateBook;
import org.coretechies.ui.updateTables.UpdateBooksTable;

import static org.coretechies.ui.updateTables.UpdateBooksTable.idc;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.sql.SQLException;

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
            saveUpdate.setBounds(100, 220, 70, 20);
            addBookF.add(saveUpdate);
            saveUpdate.addActionListener(e -> {
                UpdateBook update = new UpdateBook();
                try {
                    update.updateChanges(CreateConnection.connectDB());
                    idc = 0;
                    addBookF.dispose();
                    UpdateBooksTable printUpdate = new UpdateBooksTable();
                    printUpdate.printTable(CreateConnection.connectDB());
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(addBookF," Error : the Character should be less than 20 ");
                }
            });

        }

        public void updateScreen(){
        AddBookScreen show = new AddBookScreen();
        show.addBookFrame();
        show.enterBookDetails();
        updatePanel();
        updateUi();
        show.closeButton();
        addBookF.setVisible(true);

        }



}
