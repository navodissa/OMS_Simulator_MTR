package com.mtrsim;

import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringJoiner;
import java.util.StringTokenizer;

/**
 * Created by Navoda on 12/6/2016.
 */
public class UserAuthentication {

    final static Logger logger = Logger.getLogger(UserAuthentication.class);
    // -- Tags in the incoming message
    private char msgType;
    private String rqstRef;
    private String userID;
    private String password;
    private String channel;
    private char language;
    private String newtag;

    // -- Additional Tags in the outgoing message
    private String failDesc;
    private int authRes;
    private String portfolio;
    private String lastLogin;
    private String priceUser;
    private char otpA;
    private String mobile;
    private String otpET;
    private int maxOTP;
    private String account;


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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public char getLanguage() {
        return language;
    }

    public void setLanguage(char language) {
        this.language = language;
    }

    public String getNewtag() { return newtag; }

    public void setNewtag(String newtag) { this.newtag = newtag; }

    public String getFailDesc() { return failDesc; }

    public void setFailDesc() {
        String query = ("SELECT * FROM USERS WHERE UserID = '" + userID + "'");
        CommonDBC db = new CommonDBC();
        db.dataGetter(query, "Password");
        String[] password = db.getResult();
        if (password[0].equals(getPassword())) {
            updateDBLastLogin();
            setAuthRes(1);
            this.failDesc = "";
        } else {
            setAuthRes(0);
            this.failDesc = "Invalid Password";
        }
    }

    public int getAuthRes() {
        return authRes;
    }

    public void setAuthRes(int authRes) {
        this.authRes = authRes;
    }

    public String getPortfolio() {
        return portfolio;
    }

    public void setPortfolio() {
        String query = ("SELECT * FROM USERS WHERE UserID = '" + userID + "'");
        CommonDBC db = new CommonDBC();
        db.dataGetter(query, "Portfolio");
        String[] portfolio = db.getResult();
        //StringBuffer sb = new StringBuffer();
        this.portfolio = portfolio[0];
        //for (int counter = 0; counter < portfolio.length; counter++){
        //    this.portfolio = sb.append(this.portfolio).append(portfolio[counter]).append(",").toString();
        //}
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void updateDBLastLogin() {
        String timeStamp = new SimpleDateFormat("ddMMyyyy-HH:mm:ss").format(Calendar.getInstance().getTime());
        String query = ("UPDATE USERS SET LastLogin = '" + timeStamp + "' WHERE UserID = '" + userID + "'");
        CommonDBC db = new CommonDBC();
        db.dataSetter(query);
    }

    public void setLastLogin() {
        String query = ("SELECT * FROM USERS WHERE UserID = '" + userID + "'");
        CommonDBC db = new CommonDBC();
        db.dataGetter(query, "LastLogin");
        String[] lastLogin = db.getResult();
        if (lastLogin[0] == null) {
            this.lastLogin = "";
        } else {
            this.lastLogin = lastLogin[0];
        }
    }

    public String getPriceUser() {
        return priceUser;
    }

    public void setPriceUser() {
        String query = ("SELECT * FROM USERS WHERE UserID = '" + userID + "'");
        CommonDBC db = new CommonDBC();
        db.dataGetter(query, "PriceUser");
        String[] priceUser = db.getResult();
        if (priceUser[0] == null) {
            this.priceUser = "";
        } else {
            this.priceUser = priceUser[0];
        }
    }

    public char getOtpA() {
        return otpA;
    }

    public void setOtpA() {
        String query = ("SELECT * FROM USERS WHERE UserID = '" + userID + "'");
        CommonDBC db = new CommonDBC();
        db.dataGetter(query, "OTPAvailability");
        String[] otpA = db.getResult();
        if (otpA[0] == null) {
            this.otpA = ("N").charAt(0);
        } else {
            this.otpA = otpA[0].charAt(0);
        }
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile() {
        String query = ("SELECT * FROM USERS WHERE UserID = '" + userID + "'");
        CommonDBC db = new CommonDBC();
        db.dataGetter(query, "Mobile");
        String[] mobile = db.getResult();
        if (mobile[0] == null) {
            this.mobile = "01111111";
        } else {
            this.mobile = mobile[0];
        }
    }

    public String getOtpET() {
        return otpET;
    }

    public void setOtpET() {
        if (this.otpA == 'Y') {
            String query = ("SELECT * FROM USERS WHERE UserID = '" + userID + "'");
            CommonDBC db = new CommonDBC();
            db.dataGetter(query, "OTPExpireTime");
            String[] otpET = db.getResult();
            if (otpET[0] == null) {
                this.otpET = "1";
            } else {
                this.otpET = otpET[0];
            }
        } else {
            this.otpET = "1";
        }
    }

    public int getMaxOTP() {
        return maxOTP;
    }

    public void setMaxOTP() {
        String query = ("SELECT * FROM USERS WHERE UserID = '" + userID + "'");
        CommonDBC db = new CommonDBC();
        db.dataGetter(query, "MaxOTP");
        String[] maxOTP = db.getResult();
        if (maxOTP[0] == null) {
            this.maxOTP = Integer.parseInt("3");
        } else {
            this.maxOTP = Integer.parseInt(maxOTP[0]);
        }
    }

    public void setAccount() {
        String query = ("SELECT * FROM USERS WHERE UserID = '" + userID + "'");
        CommonDBC db = new CommonDBC();
        db.dataGetter(query, "Accounts");
        String[] account = db.getResult();
        //StringBuffer sb = new StringBuffer();
        this.account = account[0];
        //for (int counter = 0; counter < account.length; counter++){
        //    this.account = sb.append(this.account).append(account[counter]).append(",").toString();
        //}
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
                            case 35:
                                this.msgType = value.charAt(0);
                                break;
                            case 9998:
                                this.rqstRef = value;
                                break;
                            case 8000:
                                this.userID = value;
                                break;
                            case 8001:
                                this.password = value;
                                break;
                            case 8002:
                                this.channel = value;
                                break;
                            case 8102:
                                this.language = value.charAt(0);
                                break;
                            case 8010:
                                this.newtag = value;

                        }

                    }
                }
                // System.out.println("Tokinizer completed");
            } catch (Exception e) {
                logger.error("Error in parsing :" + e.getMessage().toString());
            }
        }
        logger.info("User Authentication request parsing Successful");
    }

    public String responseGenerator(){
        setFailDesc();
        setPortfolio();
        setLastLogin();
        setPriceUser();
        setOtpA();
        setMobile();
        setOtpET();
        setMaxOTP();
        setAccount();
        // StringBuffer sb = new StringBuffer();
        String res;
        /*
        String[] tagAppender = {
                ("35=" + this.msgType + ";"),
                ("9998=" + this.rqstRef + ";"),
                ("8005=" + Integer.toString(this.authRes) + ";"),
                ("8006=" + this.failDesc + ";"),
                ("8000=" + this.userID + ";"),
                ("1=" + this.portfolio + ";"),
                ("9992=" + this.account + ";"),
                ("8007=" + this.lastLogin + ";"),
                ("8008=" + this.priceUser + ";"),
                ("8002=" + this.channel + ";"),
                ("8103=" + this.otpA + ";"),
                ("8102=" + this.language + ";"),
                ("8104=" + this.mobile + ";"),
                ("8105=" + this.otpET + ";"),
                ("8006=" + this.maxOTP + ";")}; */

        String[] tagAppender = {
                ("35=" + this.msgType),
                ("9998=" + this.rqstRef),
                ("8005=" + Integer.toString(this.authRes)),
                ("8006=" + this.failDesc),
                ("8000=" + this.userID),
                ("1=" + this.portfolio),
                ("9992=" + this.account),
                ("8007=" + this.lastLogin),
                ("8008=" + this.priceUser),
                ("8002=" + this.channel),
                ("8103=" + this.otpA),
                ("8102=" + this.language),
                ("8104=" + this.mobile),
                ("8105=" + this.otpET),
                ("8006=" + this.maxOTP + ";")};

        StringJoiner sj = new StringJoiner(";");
        for (int counter = 0; counter < tagAppender.length; counter++){
            sj.add(tagAppender[counter]);
        }

        res = sj.toString();
        //updateDBLastLogin();
   //     for (int counter = 0; counter < tagAppender.length; counter++) {
   //         res = sb.append(tagAppender[counter]).toString();
        //    }

       // StringBuilder builder = new StringBuilder();
        //for(String s : tagAppender) {
        //    builder.append(s);
       // }
       // res = builder.toString();
        //res = sb.append(append(";").append("8000=").append(this.userID).append(";").append("8002=").append(this.channel).append(";").append("8102=").append(Character.toString(this.language)).append(";").append("8005=").append(Integer.toString(this.authRes)).append(";").append("1=").append(this.portfolio).append(";").toString();
        logger.info("User Authentication output to MTR : " + res);
        return res;
    }

}
