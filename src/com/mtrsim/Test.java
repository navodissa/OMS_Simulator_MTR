package com.mtrsim;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Navoda on 12/18/2016.
 */
public class Test {

    public static void main (String args []) {

        try {
            String query = ("SELECT UserID,LETSORDNO,SYMBOL,ORDTYPE FROM ORDER_SUMMARY WHERE UserID = 'HSBCNET24'");
            Class.forName("org.h2.Driver");
            Connection con = DriverManager.getConnection("jdbc:h2:~/test", "test", "");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
/*            List rowValues = new ArrayList();
            while (rs.next()) {
                System.out.println(rs.getString(1));
                rowValues.add(rs.getString(1));
            }
            String[] rows = (String[]) rowValues.toArray(new String[rowValues.size()]);
            for (int i = 0; i < rows.length; i++){
                System.out.println(rows[i] + "\n");
            } */
            StringBuilder str = new StringBuilder("Test;");

            if (rs != null) {
                while (rs.next()) {
                    ResultSetMetaData resultSetMetaData = rs.getMetaData();
                    for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                        String[] tags = {"55=","9997=","9999=","44="};
                        int type = resultSetMetaData.getColumnType(i);
                        if (type == Types.VARCHAR || type == Types.CHAR) {
                            str.append(tags[i-1]);
                            str.append(rs.getString(i));
                            str.append(";");
                        } else {
                            str.append(tags[i-1]);
                            str.append(rs.getLong(i));
                            str.append(";");
                        }
                    }

                    str.append("\n");
//                    System.out.println(str);
                }

        }

        System.out.println(str);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            LocalDateTime ordCreatedDate = LocalDateTime.now();
            String iso8061 = String.format((ordCreatedDate).toString());
            LocalDateTime ldt = LocalDateTime.parse(iso8061);
            System.out.println(dtf.format(ldt));
        } catch (Exception e) { e.printStackTrace(); }

    }
}
