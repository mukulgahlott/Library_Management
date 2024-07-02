package org.coretechies.ui;

import org.coretechies.connection.CreateConnection;
import org.coretechies.ui.updateBooks.UpdateBooksTable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static org.coretechies.ui.LibraryManageUi.allow;

public class AddBookScreen {

    public static JFrame addBookF;
    protected JLabel nameL, subjectL, authorL;
    public static JTextField nameT, subjectT, authorT;
    protected JButton saveBook, closeB;

    public void addContactFrame() {

        addBookF = new JFrame("Add and Update Book");
        addBookF.setSize(380, 200);
        addBookF.setLayout(null);

        addBookF.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                allow = true;
            }
        });
    }

    public void enterContactDetails() {

        nameL = new JLabel("Book Name :   ");
        nameL.setBounds(30, 10, 100, 20);
        subjectL = new JLabel("Subject :");
        subjectL.setBounds(30, 40, 100, 20);
        authorL = new JLabel("Author Name :");
        authorL.setBounds(30, 70, 100, 20);

        nameT = new JTextField(13);
        nameT.setBounds(150, 10, 200, 30);
        subjectT = new JTextField(13);
        subjectT.setBounds(150, 40, 200, 30);
        authorT = new JTextField(13);
        authorT.setBounds(150, 70, 200, 30);

        addBookF.add(nameL);
        addBookF.add(nameT);
        addBookF.add(subjectL);
        addBookF.add(subjectT);
        addBookF.add(authorL);
        addBookF.add(authorT);
    }

    public void AddButton() {
        saveBook = new JButton("ADD");
        saveBook.setBounds(100, 140, 70, 20);
        addBookF.add(saveBook);
        saveBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UpdateBooksTable saveInDb = new UpdateBooksTable();
                saveInDb.addBooksInTable(CreateConnection.connectDB());
            }
        });
    }

    public void closeButton() {
        closeB = new JButton("Close");
        closeB.setBounds(250, 140, 70, 20);
        addBookF.add(closeB);

        closeB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBookF.dispose();
                allow = true;
            }
        });
    }

    public void showAddNewContactScreen() {

        addContactFrame();
        enterContactDetails();
        AddButton();
        closeButton();

        addBookF.setVisible(true);

    }

}
