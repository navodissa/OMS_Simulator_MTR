package com.mtrsim;

import org.apache.log4j.Logger;

import java.sql.*;
import java.util.StringJoiner;
import java.util.StringTokenizer;

/**
 * Created by Navoda on 12/6/2016.
 */
public class PortfolioGenerator {

    final static Logger logger = Logger.getLogger(PortfolioGenerator.class);
    private char msgType;
    private String rqstRef;
    private String userID;
    private String channel;
    private String portfolio;
    private int inqStatus = 150;
    private String symbol;
    private int holdings;
    private double avgCost;
    private int pledge;
    private double marketPrice;

    public char getMsgType() {
        return msgType;
    }

    public void setMsgType(char msgType) {
        this.msgType = msgType;
    }

    public String getRqstRef() {
        return rqstRef;
    }

    public void setRqstRef(String rqstRef) {
        this.rqstRef = rqstRef;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(String portfolio) {
        this.portfolio = portfolio;
    }

    public int getInqStatus() {
        return inqStatus;
    }

    public void setInqStatus(int inqStatus) {
        this.inqStatus = inqStatus;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol() {
        String query = ("SELECT * FROM PORTFOLIOS WHERE UserID = '" + userID + "'");
        CommonDBC db = new CommonDBC();
        db.dataGetter(query, "Symbol");
        String[] symbol = db.getResult();
        if (symbol[0] == null) {
            this.symbol = ("");
        } else {
            this.symbol = symbol[0];
        }
    }

    public int getHoldings() {
        return holdings;
    }

    public void setHoldings() {
        String query = ("SELECT * FROM PORTFOLIOS WHERE UserID = '" + userID + "'");
        CommonDBC db = new CommonDBC();
        db.dataGetter(query, "Holdings");
        String[] holdings = db.getResult();
        if (holdings[0] == null) {
            this.holdings = 0;
        } else {
            this.holdings = Integer.parseInt(holdings[0]);
        }
    }

    public double getAvgCost() {
        return avgCost;
    }

    public void setAvgCost() {
        String query = ("SELECT * FROM PORTFOLIOS WHERE UserID = '" + userID + "'");
        CommonDBC db = new CommonDBC();
        db.dataGetter(query, "AvgCost");
        String[] avgCost = db.getResult();
        if (avgCost[0] == null) {
            this.avgCost = 0.0;
        } else {
            this.avgCost = Double.parseDouble(avgCost[0]);
        }
    }

    public int getPledge() {
        return pledge;
    }

    public void setPledge() {
        String query = ("SELECT * FROM PORTFOLIOS WHERE UserID = '" + userID + "'");
        CommonDBC db = new CommonDBC();
        db.dataGetter(query, "Pledge");
        String[] pledge = db.getResult();
        if (pledge[0] == null) {
            this.pledge = 0;
        } else {
            this.pledge = Integer.parseInt(pledge[0]);
        }
    }

    public double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice() {
        String query = ("SELECT * FROM PORTFOLIOS WHERE UserID = '" + userID + "'");
        CommonDBC db = new CommonDBC();
        db.dataGetter(query, "MarketPrice");
        String[] marketPrice = db.getResult();
        if (marketPrice[0] == null) {
            this.avgCost = 0.0;
        } else {
            this.marketPrice = Double.parseDouble(marketPrice[0]);
        }
    }

    public void parseInputData(String strMsg) {

        StringTokenizer fieldtoken = new StringTokenizer(strMsg, ";");
        while (fieldtoken.hasMoreTokens()) {
            try {
                StringTokenizer tokenizer = new StringTokenizer(fieldtoken.nextToken(), "=");
                while (tokenizer.hasMoreTokens()) {
                    int tag = Integer.parseInt(tokenizer.nextToken().trim());
                    if (tokenizer.hasMoreTokens()) {
                        String value = tokenizer.nextToken().trim();
                        if (value.equals("null")) {
                            value = null;
                        }

                        switch (tag) {
                            case 9999:
                                this.msgType = value.charAt(0);
                                break;
                            case 9998:
                                this.rqstRef = value;
                                break;
                            case 1:
                                this.portfolio = value;
                                break;
                            case 9975:
                                this.userID = value;
                                break;
                            case 8002:
                                this.channel = value;
                                break;
                        }

                    }
                }
                // System.out.println("Tokinizer completed");
            } catch (Exception e) {
                logger.error("Error in parsing :" + e.getMessage().toString());
            }
        }
        logger.info("Portfolio request parsing Successful");
    }

    public String responseGenerator(){
        /*
        setSymbol();
        setHoldings();
        setAvgCost();
        setPledge();
        setMarketPrice();
        String res;

        String[] tagAppender = {
                ("9999=" + this.msgType),
                ("9998=" + this.rqstRef),
                ("1=" + this.portfolio),
                ("9970=" + this.inqStatus),
                ("55=" + this.symbol),
                ("9997=" + this.holdings),
                ("9996=" + this.avgCost),
                ("9995=" + this.pledge),
                ("44=" + this.marketPrice + ";")};

        StringJoiner sj = new StringJoiner(";");
        for (int counter = 0; counter < tagAppender.length; counter++){
            sj.add(tagAppender[counter]);
        }

        res = sj.toString();
        return res;
        */

        StringBuilder str = new StringBuilder("9999=" + this.msgType + ";9998=" + this.rqstRef + ";1=" + this.portfolio + ";9970=" + this.inqStatus  + ";");

        try {
            String query = ("SELECT SYMBOL,HOLDINGS,AVGCOST,PLEDGE,MARKETPRICE FROM PORTFOLIOS WHERE UserID = '" + this.userID + "'");
            Class.forName("org.h2.Driver");
            Connection con = DriverManager.getConnection("jdbc:h2:~/test", "test", "");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            logger.info("Portfolio query : " + query);

            if (rs != null) {
                while (rs.next()) {
                    ResultSetMetaData resultSetMetaData = rs.getMetaData();
                    for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                        String[] tags = {"55=", "9997=", "9996=", "9995=", "44="};
                        int type = resultSetMetaData.getColumnType(i);
                        if (type == Types.VARCHAR || type == Types.CHAR) {
                            str.append(tags[i - 1]);
                            str.append(rs.getString(i));
                            str.append(";");
                        } else {
                            str.append(tags[i - 1]);
                            str.append(rs.getLong(i));
                            str.append(";");
                        }
                    }

                    str.append("\n");
                }

            }

        } catch (Exception e) {
            logger.error(e.getMessage().toString());
        }
        logger.info("Portfolio output to MTR : " + str.toString());
        return str.toString();
    }
}
