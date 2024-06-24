package org.coretechies.ui;

import org.coretechies.ui.updateBooks.UpdateBooksTable;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class LibraryManageUi {
    private final Connection connection;
    private JFrame mainFrame;
    private JPanel booksPanel;
    private JTable booksTable;
    private JButton addNewBook, delete, edit;
    private JLabel searchL;
    private JTextField searchT;
    private DefaultTableModel tableModel;
    private String selectedItem = "";
    private UpdateBooksTable updateBooksTable;

    public LibraryManageUi(Connection connection) {
        this.connection = connection;
    }

    public void contactHomeFrame() {
        mainFrame = new JFrame("Library Management");
        mainFrame.setSize(520, 540);
        mainFrame.setLayout(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void searchUi() {
        searchL = new JLabel("Search:");
        searchL.setBounds(20, 20, 60, 25);
        mainFrame.add(searchL);

        searchT = new JTextField();
        searchT.setBounds(90, 20, 200, 25);
        mainFrame.add(searchT);

        searchT.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchAndUpdate();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchAndUpdate();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchAndUpdate();
            }

            private void searchAndUpdate() {
                updateBooksTable.search(selectedItem, searchT.getText());
            }
        });
    }

    public void comboBox() {
        String[] choices = {"Name", "Subject", "Author"};
        JComboBox<String> cb = new JComboBox<>(choices);
        cb.setBounds(365, 17, 105, 30);
        cb.setVisible(true);
        mainFrame.add(cb);
        cb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = selectedItem;
                selectedItem = (String) cb.getSelectedItem();
                if (selectedItem.equals(s)) {
                    selectedItem = selectedItem + "D";
                }
                updateBooksTable.printTable();
            }
        });
    }

    public void contactTablePanel() {
        booksPanel = new JPanel();
        booksPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Books", TitledBorder.LEFT, TitledBorder.TOP));
        booksPanel.setBounds(20, 60, 443, 400);
        booksPanel.setLayout(new BorderLayout());
        mainFrame.add(booksPanel);

        tableModel = new DefaultTableModel(new String[]{"NAME", "SUBJECT", "AUTHOR", "SELECT"}, 0);
        booksTable = new JTable(tableModel);
        booksTable.getTableHeader().setReorderingAllowed(false);
        booksPanel.add(new JScrollPane(booksTable), BorderLayout.CENTER);

        updateBooksTable = new UpdateBooksTable(connection, tableModel);
        updateBooksTable.printTable();
    }

    public void editButton() {
        edit = new JButton("Edit");
        edit.setBounds(222, 470, 60, 30);
        mainFrame.add(edit);

        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = booksTable.getSelectedRow();
                if (selectedRow != -1) {
                    int bookId = updateBooksTable.getBookId(selectedRow);
                    UpdateBookUi showUpdateUi = new UpdateBookUi(connection, bookId, updateBooksTable);
                    showUpdateUi.updateScreen();
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "Please select the book");
                }
            }
        });
    }

    public void addBook() {
        addNewBook = new JButton("Add new Book");
        addNewBook.setBounds(70, 470, 130, 30);
        mainFrame.add(addNewBook);
        addNewBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddBookScreen showPanel = new AddBookScreen(connection, updateBooksTable);
                showPanel.showAddNewContactScreen();
            }
        });
    }

    public void deleteBook() {
        delete = new JButton("Delete");
        delete.setBounds(300, 470, 90, 30);
        mainFrame.add(delete);
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBooksTable.delete();
            }
        });
    }

    public void showBookManagementScreen() {
        contactHomeFrame();
        searchUi();
        comboBox();
        contactTablePanel();
        editButton();
        addBook();
        deleteBook();
        mainFrame.setVisible(true);
    }
}
