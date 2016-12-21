package com.mtrsim;

import org.apache.log4j.Logger;

import java.util.StringJoiner;
import java.util.StringTokenizer;

/**
 * Created by Navoda on 12/7/2016.
 */
public class BuyingPowerGen {

    final static Logger logger = Logger.getLogger(PortfolioGenerator.class);
    private char msgType;
    private String rqstRef;
    private String userID;
    private String channel;
    private String portfolio;
    private String accountNo;
    private String accountType;
    private double balance;
    private double netSecurityVal;
    private double odLimit;
    private double marginPerc;
    private double buyingPower;
    private double blockedAmt;

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

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo() {
        String query = ("SELECT * FROM CASH_ACCOUNTS WHERE UserID = '" + userID + "' AND Portfolio ='" + portfolio + "'");
        CommonDBC db = new CommonDBC();
        db.dataGetter(query, "AccountNo");
        String[] accountNo = db.getResult();
        if (accountNo[0] == null) {
            this.accountNo = "";
        } else {
            this.accountNo = accountNo[0];
        }
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType() {
        String query = ("SELECT * FROM CASH_ACCOUNTS WHERE UserID = '" + userID + "' AND Portfolio ='" + portfolio + "'");
        CommonDBC db = new CommonDBC();
        db.dataGetter(query, "AccountType");
        String[] accountType = db.getResult();
        if (accountType[0] == null) {
            this.accountType = "";
        } else {
            this.accountType = accountType[0];
        }
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance() {
        String query = ("SELECT * FROM CASH_ACCOUNTS WHERE UserID = '" + userID + "' AND Portfolio ='" + portfolio + "'");
        CommonDBC db = new CommonDBC();
        db.dataGetter(query, "Balance");
        String[] balance = db.getResult();
        if (balance[0] == null) {
            this.balance =  0.0;
        } else {
            this.balance = Double.parseDouble(balance[0]);
        }
    }

    public double getNetSecurityVal() {
        return netSecurityVal;
    }

    public void setNetSecurityVal() {
        String query = ("SELECT * FROM CASH_ACCOUNTS WHERE UserID = '" + userID + "' AND Portfolio ='" + portfolio + "'");
        CommonDBC db = new CommonDBC();
        db.dataGetter(query, "NetSecurityValue");
        String[] netSecurityVal = db.getResult();
        if (netSecurityVal[0] == null) {
            this.netSecurityVal =  0.0;
        } else {
            this.netSecurityVal = Double.parseDouble(netSecurityVal[0]);
        }
    }

    public double getOdLimit() {
        return odLimit;
    }

    public void setOdLimit() {
        String query = ("SELECT * FROM CASH_ACCOUNTS WHERE UserID = '" + userID + "' AND Portfolio ='" + portfolio + "'");
        CommonDBC db = new CommonDBC();
        db.dataGetter(query, "ODLimit");
        String[] odLimit = db.getResult();
        if (odLimit[0] == null) {
            this.odLimit =  0.0;
        } else {
            this.odLimit = Double.parseDouble(odLimit[0]);
        }
    }

    public double getMarginPerc() {
        return marginPerc;
    }

    public void setMarginPerc() {
        String query = ("SELECT * FROM CASH_ACCOUNTS WHERE UserID = '" + userID + "' AND Portfolio ='" + portfolio + "'");
        CommonDBC db = new CommonDBC();
        db.dataGetter(query, "MarginPercentage");
        String[] marginPerc = db.getResult();
        if (marginPerc[0] == null) {
            this.marginPerc =  0.0;
        } else {
            this.marginPerc = Double.parseDouble(marginPerc[0]);
        }
    }

    public double getBuyingPower() {
        return buyingPower;
    }

    public void setBuyingPower() {
        String query = ("SELECT * FROM CASH_ACCOUNTS WHERE UserID = '" + userID + "' AND Portfolio ='" + portfolio + "'");
        CommonDBC db = new CommonDBC();
        db.dataGetter(query, "BuyingPower");
        String[] buyingPower = db.getResult();
        if (buyingPower[0] == null) {
            this.buyingPower =  0.0;
        } else {
            this.buyingPower = Double.parseDouble(buyingPower[0]);
        }
    }

    public double getBlockedAmt() {
        return blockedAmt;
    }

    public void setBlockedAmt() {
        String query = ("SELECT * FROM CASH_ACCOUNTS WHERE UserID = '" + userID + "' AND Portfolio ='" + portfolio + "'");
        CommonDBC db = new CommonDBC();
        db.dataGetter(query, "BlockedAmount");
        String[] blockedAmt = db.getResult();
        if (blockedAmt[0] == null) {
            this.blockedAmt =  0.0;
        } else {
            this.blockedAmt = Double.parseDouble(blockedAmt[0]);
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
        logger.info("Buying Power request parsing Successful");
    }

    public String responseGenerator(){
        setAccountNo();
        setAccountType();
        setBalance();
        setNetSecurityVal();
        setMarginPerc();
        setBlockedAmt();
        setBuyingPower();
        setOdLimit();
        String res;

        String[] tagAppender = {
                ("9999=" + this.msgType),
                ("9998=" + this.rqstRef),
                ("1=" + this.portfolio),
                ("9992=" + this.accountNo),
                ("9991=" + this.accountType),
                ("9990=" + this.balance),
                ("9989=" + this.netSecurityVal),
                ("9988=" + this.odLimit),
                ("9987=" + this.marginPerc),
                ("9986=" + this.buyingPower),
                ("9985=" + this.blockedAmt),
                ("8002=" + this.channel + ";")};

        StringJoiner sj = new StringJoiner(";");
        for (int counter = 0; counter < tagAppender.length; counter++){
            sj.add(tagAppender[counter]);
        }

        res = sj.toString();
        logger.info("Buying Power output to MTR : " + res);
        return res;
    }

}
