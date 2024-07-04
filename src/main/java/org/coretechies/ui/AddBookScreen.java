package org.coretechies.ui;

import org.coretechies.connection.CreateConnection;
import org.coretechies.ui.updateTables.UpdateBooksTable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static org.coretechies.ui.LibraryManageUi.allow;

public class AddBookScreen {

    public static JFrame addBookF;
    protected JLabel nameL, subjectL, authorL,quantityL;
    public static JTextField nameT, subjectT, authorT , quantityT;
    protected JButton saveBook, closeB;

    public void addBookFrame() {

        addBookF = new JFrame("Add and Update Book");
        addBookF.setSize(380, 300);
        addBookF.setLayout(null);
        addBookF.setLocation(130,130);

        addBookF.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                allow = true;
            }
        });
    }

    public void enterBookDetails() {

        nameL = new JLabel("Book Name : ");
        nameL.setBounds(30, 10, 100, 20);
        subjectL = new JLabel("Subject :");
        subjectL.setBounds(30, 60, 100, 20);
        authorL = new JLabel("Author Name :");
        authorL.setBounds(30, 110, 100, 20);
        quantityL =new JLabel("Add Quantity : ");


        nameT = new JTextField(13);
        nameT.setBounds(150, 10, 200, 30);
        subjectT = new JTextField(13);
        subjectT.setBounds(150, 55, 200, 30);
        authorT = new JTextField(13);
        authorT.setBounds(150, 105, 200, 30);


        addBookF.add(nameL);
        addBookF.add(nameT);
        addBookF.add(subjectL);
        addBookF.add(subjectT);
        addBookF.add(authorL);
        addBookF.add(authorT);

    }
    public void setQuantity(){
        quantityL =new JLabel("Add Quantity : ");
        quantityL.setBounds(30, 160, 100, 20);

        quantityT = new JTextField(3);
        quantityT.setBounds(150, 160, 30, 25);

        addBookF.add(quantityL);
        addBookF.add(quantityT);
    }

    public void AddButton() {
        saveBook = new JButton("ADD");
        saveBook.setBounds(100, 220, 70, 20);
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
        closeB.setBounds(250, 220, 70, 20);
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

        addBookFrame();
        enterBookDetails();
        setQuantity();
        AddButton();
        closeButton();

        addBookF.setVisible(true);

    }

}
