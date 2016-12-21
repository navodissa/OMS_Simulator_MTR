package com.mtrsim;

import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Navoda on 11/30/2016.
 */
public class OMSSimServer {

    final static Logger logger = Logger.getLogger(OMSSimServer.class);
    private String inputQname;
    private String outputQname;

    public String getInputQname() {
        return inputQname;
    }

    public void setInputQname(String inputQname) {
        this.inputQname = inputQname;
    }

    public String getOutputQname() {
        return outputQname;
    }

    public void setOutputQname(String outputQname) {
        this.outputQname = outputQname;
    }

    public static void main(String args[]) {
        boolean thereAreMessages = true;
        try {
            while (true) {
                String[] inputQname = {"OMS_AUTH_REQ","INQUIRY_REQ","FIX_REQ"};
                for (int counter = 0; counter < inputQname.length; counter++) {
                    JMSRead reader = new JMSRead();
                    reader.setConsumerQueue(inputQname[counter]);
                    try {
                            reader.Read();
                            String str = reader.getMsg();
                            logger.info("Requests from MTR : " + str);
                            System.out.println(str);
                            MsgTypeSelector msgSelector = new MsgTypeSelector();
                            msgSelector.MessageSelector(str);
                            String outputQname = msgSelector.getOutputQname();
                            reader.setProducerQueue(outputQname);
                            String response = msgSelector.getResponse();
                            System.out.println(response);
                            reader.Write(response);
                        }

                     catch (NullPointerException e) {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();
                        System.out.println(dateFormat.format(date) + " Currently no messages available.");
                    }
                    catch (Exception e) {
                        logger.error("Fetal error : " + e.getMessage().toString());
                    }
                    Thread.sleep(2000);
                }
               }

            } catch(Exception e){
                e.printStackTrace();
                logger.error("Fetal error : " + e.getMessage().toString());
            }

    }
}
