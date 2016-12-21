package com.mtrsim;

import org.apache.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;

/**
 * Created by Navoda on 12/19/2016.
 */
public class ChangeOrders extends NewOrders {

    final static Logger logger = Logger.getLogger(ChangeOrders.class);

    private char msgType = 'A';
    private char side;

    private int ordQty = getOrdQty();
    private int minQty = getMinQty();
    private int ordType = getOrdType();
    private int TIF = getTIF();
    private int maxFloor = getMaxFloor();
    private int samaOrdId;
    private int letsOrdStatus = 203;
    private int letsOrdNo = getLetsOrdNo();
    private int leavesQty = 0;
    private int cumQty = 0;

    private double price = getPrice();
    private double ordValue, commission;

    private String rqstRef = getRqstRef();
    private String userID = getUserID();
    private String channel = getChannel();
    private String portfolio = getPortfolio();
    private String reqStatus = getReqStatus();
    private String ordCreatedDate;
    private String mubasherOrdNo = getMubasherOrdNo();
    private String symbol;
    private String expireDate;



    private void updateChangedDB(){

        this.ordQty = getOrdQty();
        this.minQty = getMinQty();
        this.ordType = getOrdType();
        this.TIF = getTIF();
        this.maxFloor = getMaxFloor();
        this.letsOrdNo = getLetsOrdNo();
        this.rqstRef = getRqstRef();
        this.userID = getUserID();
        this.channel = getChannel();
        this.portfolio = getPortfolio();
        this.reqStatus = getReqStatus();
        this.mubasherOrdNo = getMubasherOrdNo();
        this.price = getPrice();
        this.ordValue = this.ordQty * this.price;
        this.commission = ordValue * 0.00155;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime ordCreatedDate = LocalDateTime.now();
        String iso8061 = String.format((ordCreatedDate).toString());
        LocalDateTime ldt = LocalDateTime.parse(iso8061);
        this.ordCreatedDate = dtf.format(ldt);
        String updateData = "UPDATE ORDER_SUMMARY SET MUBASHERORDNO = '" + this.mubasherOrdNo + "',ORDTYPE = '" + this.ordType + "',ORDQTY = '" + this.ordQty + "',PRICE = '" + this.price + "',TIF = '" + this.TIF + "',ORDVALUE = '" + this.ordValue + "',COMMISSION = '" + commission + "',MINQTY = '" + this.minQty + "',MAXFLOOR = '" + this.minQty + "' WHERE LETSORDNO = '" + this.letsOrdNo + "'";
        logger.info("Update Query for Order Summary : " + updateData);
        CommonDBC db = new CommonDBC();
        db.dataSetter(updateData);

    }

    public String responseGeneratorChangedOrders() {

        this.updateChangedDB();
        String res;
        String[] tagAppender = {
                ("35=" + this.msgType),
                ("9998=" + this.rqstRef),
                ("1=" + this.portfolio),
                ("9974=" + this.letsOrdNo),
                ("9962=" + this.mubasherOrdNo),
                ("9971=212"),
                ("9964=" + this.ordCreatedDate),
                ("9973=100"),
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
