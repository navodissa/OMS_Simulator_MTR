package com.mtrsim;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Navoda on 12/6/2016.
 */
public class CommonDBC {

    final static Logger logger = Logger.getLogger(CommonDBC.class);
    private String[] result;

    public String[] getResult() {
        return result;
    }

    public void setResult(String[] result) {
        this.result = result;
    }

    public void dataGetter(String query, String strGetter)
    {
        try
        {
            Class.forName("org.h2.Driver");
            Connection con = DriverManager.getConnection("jdbc:h2:~/test", "test", "" );
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int counter = 0;
            while( rs.next() )
            {
                String[] result = new String[10];
                result[counter] = rs.getString(strGetter);
                this.setResult(result);
                counter = counter + 1;
            }

            stmt.close();
            con.close();
        }
        catch( Exception e )
        {
            logger.error(e.getMessage());
        }

    }

    public void dataSetter(String query){
        try {
            Class.forName("org.h2.Driver");
            Connection con = DriverManager.getConnection("jdbc:h2:~/test", "test", "" );
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
            con.close();
        } catch (Exception e) { logger.error(e.getMessage()); }
    }
}
