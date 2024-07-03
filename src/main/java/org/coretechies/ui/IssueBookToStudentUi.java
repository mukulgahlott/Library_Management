package org.coretechies.ui;

import org.coretechies.connection.CreateConnection;
import org.coretechies.manupulation.IssueBookLogic;
import org.coretechies.ui.updateTables.UpdateBooksTable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import static org.coretechies.ui.LibraryManageUi.allow;

public class IssueBookToStudentUi {

    public static JFrame issueBookF;
    public JLabel bookIDL, bookNL, enrollmentL, classL;
    public static JTextField bookIDT, bookNT, enrollmentT;
    public static JComboBox<String> clsT;
    public JButton doneB;


    public void issueFrame() {
        issueBookF = new JFrame("ISSUE BOOK");
        issueBookF.setSize(380, 300);
        issueBookF.setLayout(null);
        issueBookF.setLocation(130, 130);

        issueBookF.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                allow = true;
            }
        });
    }

    public void bookStudentDetails() {

        bookIDL = new JLabel("Book ID: ");
        bookIDL.setBounds(30, 10, 100, 20);
        bookNL = new JLabel("Book Name : ");
        bookNL.setBounds(30, 60, 100, 20);
        enrollmentL = new JLabel("Enrollment Number :");
        enrollmentL.setBounds(30, 110, 100, 20);
        classL = new JLabel("Class Name :");
        classL.setBounds(30, 160, 100, 20);


        bookIDT = new JTextField(13);
        bookIDT.setBounds(150, 10, 200, 30);
        bookIDT.setEditable(false);
        bookNT = new JTextField(13);
        bookNT.setBounds(150, 55, 200, 30);
        bookNT.setEditable(false);
        enrollmentT = new JTextField(13);
        enrollmentT.setBounds(150, 105, 200, 30);
        String[] choice = {"BCA", "BBA", "MCA", "MBA", "CS", " Other"};
        clsT = new JComboBox<>(choice);
        clsT.setBounds(150, 155, 200, 30);
        clsT.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clsT.getSelectedItem();
            }
        });


        issueBookF.add(bookIDL);
        issueBookF.add(bookIDT);
        issueBookF.add(bookNL);
        issueBookF.add(bookNT);
        issueBookF.add(enrollmentL);
        issueBookF.add(enrollmentT);
        issueBookF.add(classL);
        issueBookF.add(clsT);

    }

    public void doneButton() {
        doneB = new JButton("Done");
        doneB.setBounds(100, 220, 70, 20);
        issueBookF.add(doneB);
        doneB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IssueBookLogic issued = new IssueBookLogic();
                try {
                    issued.issueBook(CreateConnection.connectDB());
                    UpdateBooksTable print = new UpdateBooksTable();
                    print.printTable(CreateConnection.connectDB());
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(issueBookF, "The Character should be less than 30");
                }
            }
        });
    }

    public void closeButton() {
        JButton closeB = new JButton("Close");
        closeB.setBounds(250, 220, 70, 20);
        issueBookF.add(closeB);

        closeB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                issueBookF.dispose();
                allow = true;
            }
        });
    }

    public void showIssueScreen() {
        issueFrame();
        bookStudentDetails();
        doneButton();
        closeButton();
        issueBookF.setVisible(true);
    }


}
