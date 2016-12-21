package com.mtrsim;

import org.apache.log4j.Logger;

import java.util.StringTokenizer;

/**
 * Created by Navoda on 12/6/2016.
 */
public class MsgTypeSelector {

    final static Logger logger = Logger.getLogger(MsgTypeSelector.class);
    private String response;
    private String outputQname;

    public String getOutputQname() {
        return outputQname;
    }

    public void setOutputQname(String outputQname) {
        this.outputQname = outputQname;
    }

    public void MessageSelector(String str) {
        StringTokenizer fieldtoken = new StringTokenizer(str, ";");
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
                                switch (value) {
                                    case "X":
                                        UserAuthentication newUser = new UserAuthentication();
                                        newUser.parseInputData(str);
                                        setResponse(newUser.responseGenerator());
                                        this.setOutputQname("OMS_AUTH_RSPON");
                                        logger.info("Response sent to queue: " + getOutputQname());
                                        break;
                                    case "D":
                                        NewOrders newOrder = new NewOrders();
                                        newOrder.parseInputData(str);
                                        setResponse(newOrder.responseGenerator());
                                        this.setOutputQname("FIX_RSPON");
                                        logger.info("Response sent to queue: " + getOutputQname());
                                        break;
                                    case "G":
                                        ChangeOrders changeOrder = new ChangeOrders();
                                        changeOrder.parseInputData(str);
                                        setResponse(changeOrder.responseGeneratorChangedOrders());
                                        this.setOutputQname("FIX_RSPON");
                                        logger.info("Response sent to queue: " + getOutputQname());
                                        break;
                                }
                                break;
                            case 9999:
                                switch (value) {
                                    case "P":
                                        PortfolioGenerator newPortReq = new PortfolioGenerator();
                                        newPortReq.parseInputData(str);
                                        setResponse(newPortReq.responseGenerator());
                                        this.setOutputQname("INQUIRY_RSPON");
                                        logger.info("Response sent to queue: " + getOutputQname());
                                        break;
                                    case "B":
                                        BuyingPowerGen newBuyingPw = new BuyingPowerGen();
                                        newBuyingPw.parseInputData(str);
                                        setResponse(newBuyingPw.responseGenerator());
                                        this.setOutputQname("FIX_RSPON");
                                        logger.info("Response sent to queue: " + getOutputQname());
                                        break;
                                    case "L":
                                        OrderListGenerator newOrderList = new OrderListGenerator();
                                        newOrderList.parseInputData(str);
                                        setResponse(newOrderList.responseGenerator());
                                        this.setOutputQname("INQUIRY_RSPON");
                                        logger.info("Response sent to queue: " + getOutputQname());
                                        break;
                                }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Message Selector error : " + e.getMessage().toString());
            }
        }
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}