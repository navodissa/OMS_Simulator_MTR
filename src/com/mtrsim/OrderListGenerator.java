package com.mtrsim;

import org.apache.log4j.Logger;

import java.sql.*;
import java.util.StringJoiner;
import java.util.StringTokenizer;

/**
 * Created by Navoda on 12/7/2016.
 */
public class OrderListGenerator {

    final static Logger logger = Logger.getLogger(OrderListGenerator.class);
    private char msgType;
    private String rqstRef;
    private String userID;
    private String channel;
    private String portfolio;
    private String inqStatus = "150";
    private char respMode = 'Q';
    private String recordNo = "1";
    private String samaOrdID;
    private String letsOrdStatus;
    private String letsOrdNo;
    private String mubasherOrdNo;
    private String symbol;
    private String ordType;
    private int ordQty;
    private int leavesQty;
    private int cumQty;
    private double price;
    private int TIF;
    private String expiryDate;
    private int side;
    private double ordValue;
    private double commission;
    private int minQty;
    private int maxFloor;
    private String ordCreatedDate;

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

    public String getInqStatus() {
        return inqStatus;
    }

    public void setInqStatus(String inqStatus) {
        this.inqStatus = inqStatus;
    }

    public char getRespMode() {
        return respMode;
    }

    public void setRespMode(char respMode) {
        this.respMode = respMode;
    }

    public String getRecordNo() {
        return recordNo;
    }

    public void setRecordNo(String recordNo) {
        this.recordNo = recordNo;
    }

    public String getSamaOrdID() {
        return samaOrdID;
    }

    public void setSamaOrdID() {
    }

    public String getLetsOrdStatus() {
        return letsOrdStatus;
    }

    public void setLetsOrdStatus() {
    }

    public String getLetsOrdNo() {
        return letsOrdNo;
    }

    public void setLetsOrdNo() {
    }

    public String getMubasherOrdNo() {
        return mubasherOrdNo;
    }

    public void setMubasherOrdNo() {
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol() {
    }

    public String getOrdType() {
        return ordType;
    }

    public void setOrdType() {
    }

    public int getOrdQty() {
        return ordQty;
    }

    public void setOrdQty() {
    }

    public int getLeavesQty() {
        return leavesQty;
    }

    public void setLeavesQty() {
    }

    public int getCumQty() {
        return cumQty;
    }

    public void setCumQty() {
    }

    public double getPrice() {
        return price;
    }

    public void setPrice() {
    }

    public int getTIF() {
        return TIF;
    }

    public void setTIF() {
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate() {
    }

    public int getSide() {
        return side;
    }

    public void setSide() {
    }

    public double getOrdValue() {
        return ordValue;
    }

    public void setOrdValue() {
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission() {
    }

    public int getMinQty() {
        return minQty;
    }

    public void setMinQty() {
    }

    public int getMaxFloor() {
        return maxFloor;
    }

    public void setMaxFloor() {
    }

    public String getOrdCreatedDate() {
        return ordCreatedDate;
    }

    public void setOrdCreatedDate() {
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
        logger.info("Order List request parsing Successful");
    }

    public String responseGenerator() {

        StringBuilder str = new StringBuilder("9999=L;9998=" + this.rqstRef + ";1=" + this.portfolio + ";9970=" + this.inqStatus + ";9961=" + this.respMode + ";");

        try {
            String query = ("SELECT SAMAORDID,LETSORDSTATUS,LETSORDNO,MUBASHERORDNO,SYMBOL,ORDTYPE,ORDQTY,LEAVESQTY,CUMQTY,PRICE,TIF,EXPIRYDATE,SIDE,ORDVALUE,COMMISSION,MINQTY,MAXFLOOR,ORDCREATEDDATE FROM ORDER_SUMMARY WHERE UserID = '" + this.userID + "'");
            Class.forName("org.h2.Driver");
            Connection con = DriverManager.getConnection("jdbc:h2:~/test", "test", "");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            logger.info("Order List query : " + query);

            int counter = 1;

            if (rs != null) {
                while (rs.next()) {
                    str.append("9960=" + counter + ";");
                    ResultSetMetaData resultSetMetaData = rs.getMetaData();
                    for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                        String[] tags = {"37=", "9971=", "9974=", "9962=", "55=", "40=", "38=", "151=", "14=", "44=", "50=", "126=", "9959=", "9977=", "9965=", "110=", "111=", "9964="};
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
                    counter++;
                }

            }

        } catch (Exception e) {
            logger.error(e.getMessage().toString());
        }
        logger.info("Order List output to MTR : " + str.toString());
        return str.toString();
    }

}
