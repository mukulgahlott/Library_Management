package org.coretechies.ui;

import org.coretechies.manupulation.UpdateBook;
import org.coretechies.ui.updateBooks.UpdateBooksTable;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import static org.coretechies.ui.updateBooks.UpdateBooksTable.id;


public class LibraryManageUi {


    private String key;
    public static boolean allow = true;
    public static JFrame mainFrame;
    protected JPanel booksPanel;
    public static JTable booksTable;
    public static JButton addNewBook, delete, edit;
    protected JLabel searchL;
    public static JTextField searchT;
    public static DefaultTableModel tableModel;
    public static String selectedItem = "";


    // Build the main frame
    public void contactHomeFrame() {
        mainFrame = new JFrame("Library Management");
        mainFrame.setSize(520, 540);
        mainFrame.setLayout(null); // Use absolute positioning
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Build the search UI
    public void searchUi() {
        searchL = new JLabel("Search:");
        searchL.setBounds(20, 20, 60, 25);
        mainFrame.add(searchL);

        searchT = new JTextField();
        searchT.setBounds(90, 20, 200, 25);
        mainFrame.add(searchT);

        // Add DocumentListener to the search text field
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
                UpdateBooksTable searching = new UpdateBooksTable();
                searching.search();
            }
        });

    }

    //  build ComboBox for short Books as user want
    public void comboBox() {
        String[] choices = {"Name", "Subject", "Author",};
        JComboBox<String> cb = new JComboBox<>(choices);
        cb.setBounds(365, 17, 105, 30);
        cb.setVisible(true);
        mainFrame.add(cb);
        cb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = selectedItem;
                selectedItem = (String) cb.getSelectedItem();
                assert selectedItem != null;
                if (selectedItem.equals(s)) {
                    selectedItem = (String) cb.getSelectedItem() + "D";
                    UpdateBooksTable sortTable = new UpdateBooksTable();
                    sortTable.printTable();
                } else {
                    UpdateBooksTable sortTable = new UpdateBooksTable();
                    sortTable.printTable();
                }
            }
        });
    }

    // Build contact panel to display saved contacts
    public void contactTablePanel() {

        booksPanel = new JPanel();
        booksPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Books", TitledBorder.LEFT, TitledBorder.TOP));
        booksPanel.setBounds(20, 60, 443, 400);
        booksPanel.setLayout(new BorderLayout());
        mainFrame.add(booksPanel);

        tableModel = new DefaultTableModel(new String[]{"NAME", "SUBJECT", "AUTHOR","SELECT"}, 0);

        booksTable = new JTable(tableModel);
        booksTable.getTableHeader().setReorderingAllowed(false);

        booksPanel.add(new JScrollPane(booksTable), BorderLayout.CENTER);
        // Refresh the table to display data
        UpdateBooksTable showBook = new UpdateBooksTable();
        showBook.printTable();
    }

    // edit button to Update the contacts
    public void editButton() {
        edit = new JButton("Edit");
        edit.setBounds(222, 470, 60, 30);
        mainFrame.add(edit);

        edit.addActionListener(e -> {
            if (!(id == 0)) {
                if (allow) {
                    allow = false;
                    UpdateBookUi showUpdateUi = new UpdateBookUi();
                    showUpdateUi.updateScreen();
                    try {
                        UpdateBook fill = new UpdateBook();
                        fill.fillUpdate();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    //alert the user that the frame is already open
                    //I recommend a JOptionPane such as this
                    JOptionPane.showMessageDialog(mainFrame, "A window is already open");
                }
            } else {
                JOptionPane.showMessageDialog(mainFrame, "please select the book ");
            }
        });

    }

    // Build a button to open Add New BOOK screen
    public void addBook() {

        addNewBook = new JButton("Add new Book");
        addNewBook.setBounds(70, 470, 130, 30);
        mainFrame.add(addNewBook);
        addNewBook.addActionListener(e -> {
            if (allow) {
                allow = false;
                AddBookScreen showPanel = new AddBookScreen();
                showPanel.showAddNewContactScreen();
            } else {
                JOptionPane.showMessageDialog(mainFrame, "A window is already open");
            }
        });

    }

    public void deleteBook() {
        delete = new JButton("Delete");
        delete.setBounds(300, 470, 90, 30);
        mainFrame.add(delete);
        delete.addActionListener(e -> {
            UpdateBooksTable del = new UpdateBooksTable();
            del.delete();


        });

    }

    // Function to show ContactBookScreen
    public void showBookManagementScreen() {
        contactHomeFrame();
        searchUi();
        comboBox();
        contactTablePanel();
        editButton();
        addBook();
        deleteBook();
        UpdateBooksTable select = new UpdateBooksTable();
        mainFrame.setVisible(true);

    }
}
