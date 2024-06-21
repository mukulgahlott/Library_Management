package org.coretechies.manupulation;

import com.mysql.cj.x.protobuf.MysqlxCrud;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.coretechies.connection.CreateConnection.st;
import static org.coretechies.ui.AddBookScreen.*;


public class UpdateBook {
    final String showQuery = "SELECT * FROM book ;";


    public void fillUpdate() throws SQLException {
        ResultSet showQ = st.executeQuery(showQuery);
    while (showQ.next()) {
        nameT.setText(showQ.getString("BookName"));
        subjectT.setText(showQ.getString("Subject"));
        authorT.setText(showQ.getString("Author"));
    }
    }
    public void updateChanges(){


    }



}
