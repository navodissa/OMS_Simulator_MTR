package com.mtrsim;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;

/**
 * Created by Navoda on 12/15/2016.
 */
public class NewOrders {

    final static Logger logger = Logger.getLogger(NewOrders.class);

    private char msgType = 'A';
    private char side;

    private int ordQty;
    private int minQty;
    private int ordType;
    private int TIF;
    private int maxFloor;
    private int samaOrdId;
    private int letsOrdStatus = 203;
    private int letsOrdNo;
    private int leavesQty = 0;
    private int cumQty = 0;

    private double price;
    private double ordValue, commission;

    private String rqstRef;
    private String userID;
    private String channel;
    private String portfolio;
    private String reqStatus = "100";
    private String ordCreatedDate;
    private String mubasherOrdNo;
    private String symbol;
    private String expireDate;

    public char getSide() {
        return side;
    }

    public void setSide(char side) {
        this.side = side;
    }

    public int getSamaOrdId() {
        return samaOrdId;
    }

    public void setSamaOrdId(int samaOrdId) {
        this.samaOrdId = samaOrdId;
    }

    public int getLetsOrdStatus() {
        return letsOrdStatus;
    }

    public void setLetsOrdStatus(int letsOrdStatus) {
        this.letsOrdStatus = letsOrdStatus;
    }

    public int getLeavesQty() {
        return leavesQty;
    }

    public void setLeavesQty(int leavesQty) {
        this.leavesQty = leavesQty;
    }

    public int getCumQty() {
        return cumQty;
    }

    public void setCumQty(int cumQty) {
        this.cumQty = cumQty;
    }

    public double getOrdValue() {
        return ordValue;
    }

    public void setOrdValue(double ordValue) {
        this.ordValue = ordValue;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public int getOrdQty() {
        return ordQty;
    }

    public void setOrdQty(int ordQty) {
        this.ordQty = ordQty;
    }

    public int getMinQty() {
        return minQty;
    }

    public void setMinQty(int minQty) {
        this.minQty = minQty;
    }

    public int getOrdType() {
        return ordType;
    }

    public void setOrdType(int ordType) {
        this.ordType = ordType;
    }

    public int getTIF() {
        return TIF;
    }

    public void setTIF(int TIF) {
        this.TIF = TIF;
    }

    public int getMaxFloor() {
        return maxFloor;
    }

    public void setMaxFloor(int maxFloor) {
        this.maxFloor = maxFloor;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

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

    public String getReqStatus() {
        return reqStatus;
    }

    public void setReqStatus(String reqStatus) {
        this.reqStatus = reqStatus;
    }

    public String getOrdCreatedDate() {
        return ordCreatedDate;
    }

    public void setOrdCreatedDate(String ordCreatedDate) {
        ordCreatedDate = ordCreatedDate;
    }

    public int getLetsOrdNo() {
        return letsOrdNo;
    }

    public void setLetsOrdNo(int letsOrdNo) {
        this.letsOrdNo = letsOrdNo;
    }

    public String getMubasherOrdNo() {
        return mubasherOrdNo;
    }

    public void setMubasherOrdNo(String mubasherOrdNo) {
        this.mubasherOrdNo = mubasherOrdNo;
    }

    public void parseInputData(String strMsg) {

        logger.info("Starting to parse New Order Request");
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
                            case 55:
                                this.symbol = value;
                                break;
                            case 40:
                                this.ordType = Integer.parseInt(value);
                                break;
                            case 38:
                                this.ordQty = Integer.parseInt(value);
                                break;
                            case 110:
                                this.minQty = Integer.parseInt(value);
                                break;
                            case 44:
                                this.price = Double.parseDouble(value);
                                break;
                            case 59:
                                this.TIF = Integer.parseInt(value);
                                break;
                            case 126:
                                this.expireDate = value;
                                break;
                            case 9959:
                                this.side = value.charAt(0);
                                break;
                            case 111:
                                this.maxFloor = Integer.parseInt(value);
                                break;
                            case 9962:
                                this.mubasherOrdNo = value;
                                break;
                            case 9974:
                                this.letsOrdNo = Integer.parseInt(value);
                                break;
                        }

                    }
                }
                // System.out.println("Tokinizer completed");

            } catch (Exception e) {
                logger.error("Error in parsing :" + e.getMessage().toString());
            }
        }
        logger.info("New Order parsing Successfully done");
    }

    private String dataGetter(String givenQuery, String column){
        String query = (givenQuery);
        CommonDBC db = new CommonDBC();
        db.dataGetter(query, column);
        String[] variable = db.getResult();
        if (variable[0] == null) {
            return "";
        } else {
            return variable[0];
        }
    }

    private void updateDB(){

        String samaQuery = "Select SAMAORDID from ORDER_SUMMARY order by SAMAORDID desc limit 1";
        setSamaOrdId(Integer.parseInt(dataGetter(samaQuery,"SAMAORDID")) + 1);

        String letsOrdNoQuery = "Select LETSORDNO from ORDER_SUMMARY order by LETSORDNO desc limit 1";
        setLetsOrdNo(Integer.parseInt(dataGetter(letsOrdNoQuery,"LETSORDNO")) + 1);

        setOrdValue(this.ordQty * this.price);
        setCommission(ordValue * 0.00155);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime ordCreatedDate = LocalDateTime.now();
        String iso8061 = String.format((ordCreatedDate).toString());
        LocalDateTime ldt = LocalDateTime.parse(iso8061);
        this.ordCreatedDate = dtf.format(ldt);

        String updateData = "INSERT INTO ORDER_SUMMARY (USERID,PORTFOLIO,SAMAORDID,LETSORDSTATUS,LETSORDNO,MUBASHERORDNO,SYMBOL,ORDTYPE,ORDQTY,LEAVESQTY,CUMQTY,PRICE,TIF,EXPIRYDATE,SIDE,ORDVALUE,COMMISSION,MINQTY,MAXFLOOR,ORDCREATEDDATE) VALUES ('" + this.userID + "','" + this.portfolio + "','" + this.samaOrdId + "','" + this.letsOrdStatus + "','" + this.letsOrdNo + "','" + this.mubasherOrdNo + "','" + this.symbol + "','" + this.ordType + "','" + this.ordQty + "','" + leavesQty + "','" + cumQty + "','" + this.price + "','" + this.TIF + "','" + this.expireDate + "','" + this.side + "','" + ordValue + "','" + commission + "','" + this.minQty + "','" + this.maxFloor + "','" + this.ordCreatedDate + "')";
        double totalOrdVal = this.ordValue + this.commission;
        String updateData2 = "UPDATE CASH_ACCOUNTS SET BUYINGPOWER=(BUYINGPOWER-" + totalOrdVal + "),BLOCKEDAMOUNT=BLOCKEDAMOUNT+" + totalOrdVal + " WHERE PORTFOLIO = '" + this.portfolio + "'";
        CommonDBC db = new CommonDBC();
        db.dataSetter(updateData);
        logger.info("Query Executed to Update Order Summary:" + updateData);
        db.dataSetter(updateData2);
        logger.info("Query Executed to Update Cash Account:" + updateData2);

    }

    public String responseGenerator() {

        this.updateDB();
        String res;
        String[] tagAppender = {
                ("35=" + this.msgType),
                ("9998=" + this.rqstRef),
                ("1=" + this.portfolio),
                ("9974=" + this.letsOrdNo),
                ("9962=" + this.mubasherOrdNo),
                ("9964=" + this.ordCreatedDate),
                ("9973=" + this.reqStatus),
                ("8002=" + this.channel + ";")};

        StringJoiner sj = new StringJoiner(";");
        for (int counter = 0; counter < tagAppender.length; counter++){
            sj.add(tagAppender[counter]);
        }

        res = sj.toString();
        logger.info("Output for MTR : " + res);
        return res;
    }
}
