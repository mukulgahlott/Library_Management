package org.coretechies.ui;

import org.coretechies.connection.CreateConnection;
import org.coretechies.manupulation.IssueBookLogic;
import org.coretechies.manupulation.UpdateBook;
import org.coretechies.ui.updateTables.UpdateBooksTable;
import org.coretechies.ui.updateTables.UpdateRecordBookTable;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import static org.coretechies.ui.updateTables.UpdateBooksTable.idc;


public class LibraryManageUi {


    public static boolean allow = true;
    public static JFrame mainFrame;
    protected JPanel booksPanel;
    public static JTable booksTable;
    public static JButton addNewBook, delete, edit, issueB;
    protected JLabel searchL;
    public static JTextField searchT;
    public DefaultTableModel tableModel;
    public static String selectedSort = "";
    UpdateBooksTable showBook = new UpdateBooksTable();
    public static String getSelectedTable = "BOOKS";


    // Build the main frame
    public void bookHomeFrame() {
        mainFrame = new JFrame("Library Management");
        mainFrame.setSize(640, 540);
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
                String searchText = searchT.getText();
                if (searchText.isBlank()) {
                    showBook.printTable(CreateConnection.connectDB());
                    try {
                        RecordTableUi showRecord = new RecordTableUi();
                        showRecord.printTable(CreateConnection.connectDB());
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(mainFrame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    if (getSelectedTable.equals("ISSUE RECORD")) {
                        UpdateRecordBookTable searching = new UpdateRecordBookTable();
                        searching.searchRecord(CreateConnection.connectDB());
                    } else {
                        UpdateBooksTable searching = new UpdateBooksTable();
                        searching.search(CreateConnection.connectDB());
                    }
                }
            }
        });

    }

    //  build ComboBox for short Books as user want
    public void sortComboBox() {
        String[] choices = {" ID", "Name", "Subject", "Author",};
        JComboBox<String> cb = new JComboBox<>(choices);
        cb.setBounds(365, 17, 105, 30);
        cb.setVisible(true);
        mainFrame.add(cb);
        cb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = selectedSort;
                selectedSort = (String) cb.getSelectedItem();
                assert selectedSort != null;
                if (selectedSort.equals(s)) {
                    selectedSort = (String) cb.getSelectedItem() + "D";
                    UpdateBooksTable sortTable = new UpdateBooksTable();
                    sortTable.printTable(CreateConnection.connectDB());
                } else {
                    showBook.printTable(CreateConnection.connectDB());
                }
            }
        });
    }

    public void tableChangeComboBox(String[] choices) {
        JComboBox<String> chose = new JComboBox<>(choices);
        chose.setBounds(480, 17, 105, 30);
        chose.setVisible(true);
        mainFrame.add(chose);
        chose.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                getSelectedTable = (String) chose.getSelectedItem();
                if (getSelectedTable.equals("ISSUE RECORD")) {
                    mainFrame.dispose();
                    RecordTableUi recordTable = new RecordTableUi();
                    try {
                        recordTable.showRecords();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    mainFrame.dispose();
                    showBookManagementScreen();
                }

            }
        });
    }


    // Build contact panel to display saved contacts
    public void contactTablePanel() {

        booksPanel = new JPanel();
        booksPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Books", TitledBorder.LEFT, TitledBorder.TOP));
        booksPanel.setBounds(20, 60, 580, 400);
        booksPanel.setLayout(new BorderLayout());
        mainFrame.add(booksPanel);

        tableModel = new DefaultTableModel(new String[]{"ID", "NAME", "SUBJECT", "AUTHOR", "QUANTITY", "ISSUED", "REMAIN", "SELECT"}, 0);

        booksTable = new JTable(tableModel);
        booksTable.getTableHeader().setReorderingAllowed(false);

        booksPanel.add(new JScrollPane(booksTable), BorderLayout.CENTER);
        // Refresh the table to display data
        showBook.printTable(CreateConnection.connectDB());
    }

    // edit button to Update the contacts
    public void editButton() {
        edit = new JButton("Edit");
        edit.setBounds(222, 470, 60, 30);
        mainFrame.add(edit);

        edit.addActionListener(e -> {
            if (!(idc == 0)) {
                if (allow) {
                    allow = false;
                    UpdateBookUi showUpdateUi = new UpdateBookUi();
                    showUpdateUi.updateScreen();
                    try {
                        UpdateBook fill = new UpdateBook();
                        fill.fillUpdate(CreateConnection.connectDB());
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(mainFrame, "Data does not Exist ");
                    }
                } else {
                    //alert the user that the frame is already open
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
            del.delete(CreateConnection.connectDB());


        });

    }

    public void issueBook() {
        issueB = new JButton("Issue Book");
        issueB.setBounds(405, 470, 100, 30);
        mainFrame.add(issueB);
        issueB.addActionListener(e -> {
            if (idc != 0) {
                if (allow) {
                    allow = false;
                    IssueBookToStudentUi showPanel = new IssueBookToStudentUi();
                    showPanel.showIssueScreen();
                    IssueBookLogic fill = new IssueBookLogic();
                    try {
                        fill.fillBookDetails(CreateConnection.connectDB());
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "A window is already open");
                }
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Please select the book ");
            }

        });

    }

    public void deSelect() {
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {
                if (event.getID() == MouseEvent.MOUSE_CLICKED) {
                    MouseEvent mevent = (MouseEvent) event;
                    int row = booksTable.rowAtPoint(mevent.getPoint());
                    if (row == -1) {
                        booksTable.clearSelection();
                        idc = 0;
                    }
                }
            }
        }, AWTEvent.MOUSE_EVENT_MASK);
    }

    // Function to show ContactBookScreen
    public void showBookManagementScreen() {
        bookHomeFrame();
        searchUi();
        sortComboBox();
        tableChangeComboBox(new String[]{"BOOKS", "ISSUE RECORD"});
        contactTablePanel();
        editButton();
        addBook();
        deleteBook();
        issueBook();
        deSelect();
        mainFrame.setVisible(true);

    }
}
