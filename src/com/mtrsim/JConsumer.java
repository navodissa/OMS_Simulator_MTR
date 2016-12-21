package com.mtrsim;

/**
 * Created by Navoda on 12/14/2016.
 */

import com.ibm.mq.*;
import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class JConsumer {
    private MQQueueManager mqQueueManager; // QMGR object
    private MQQueue queue; // Queue object
    private int openOptionInquire; // Open options
    private String hostName; // host name
    private String channel; // server connection channel
    private String port; // port number on which the QMGR is running
    private String qmgrName; // queue manager name
    private String qName; // queue name
    private String msgFileName; // message file name
    private String msgDir;
    private static String dateStringPre = new String();
    private static String dateStringPost = new String();
    private static String dateStringPreTemp = new String();
    private static String dateStringPostTemp = new String();
    private static boolean statusFlag = false;



    private void startDownloading() {

        try{
            JConsumer MQBrowse = new JConsumer();
            MQBrowse.init();
        } catch( Exception e) {
            e.printStackTrace();
        }
    }

    public void init() {

        try{
            String fileName = "C:\\Users\\Navoda\\J2EE\\Lesson2\\MTRSim\\src\\com\\h2\\examples\\ProducerProperties.properties";
            this.readPropertyFile(fileName);
            this.mqInit( );
            System.out.println("Mq initializing started "+new Date());
        } catch( Exception e) {
            e.printStackTrace();
        }
    }


    private void mqInit( ) { // Initiation of the MQ parameter
        try {

            System.out.println("host name : " + hostName+"\n"
                    +"QMGR name : " + qmgrName+"\n"
                    +"port number : " + port+"\n"
                    +"channel : " + channel+"\n"
                    +"file name : " + msgFileName+"\n"
                    +"queue name : " + qName+"\n"
                    +"\n");

            mqOperations();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void mqOperations() throws Exception { // connect, open, browse, close & disconnects

        try {
            mqConnect(); // queue manager connection
            mqOpen(); // pens the queue & browse
            mqClose(); // close the queue
            mqDisconnect(); // disconects the queue manager
        }

        catch (Exception exp) {
            exp.printStackTrace();
        }


    } //mqOperations ends here


    private void mqConnect() throws Exception { // Connection to the queue manager
        try {
            MQEnvironment.hostname = hostName;
            MQEnvironment.channel = channel;
            MQEnvironment.port = Integer.parseInt(port);

            System.out.println("Connecting to ---------- "+hostName + " ---------- " + channel + " ----------- " + port);

            mqQueueManager = new MQQueueManager(qmgrName);
            System.out.println("Qmgr ---------- " + qmgrName + " connection successfull ");
        }

        catch ( MQException mqExp) {
            System.out.println("Error in connecting to queue manager -- "+qmgrName+" with CC : " + mqExp.completionCode +" RC : " + mqExp.reasonCode);
            mqClose();
            mqDisconnect();
        }
    }



    private void mqDisconnect() { // disconnect to queue manager
        try {
            mqQueueManager.disconnect();
            System.out.println("Qmgr : " + qmgrName + " disconnect successfull ");
        }

        catch ( MQException mqExp) {
            System.out.println("Error in queue manager disconnect...."+"QMGR Name : " + qmgrName+"CC : " + mqExp.completionCode+"RC : " + mqExp.reasonCode);
        }
    } // end of mqDisconnect


    private void mqOpen() throws MQException {
        try {
            int openOption = 0;

            //openOption = MQC.MQOO_BROWSE
            openOption = MQC.MQOO_INPUT_SHARED; // open options for browse & share

            queue = mqQueueManager.accessQueue(qName, openOption,qmgrName,null,null);

            MQGetMessageOptions getMessageOptions = new MQGetMessageOptions();
            getMessageOptions.options = MQC.MQOO_INPUT_AS_Q_DEF;
//MQC.MQGMO_BROWSE_FIRST MQC.MQGMO_WAIT ; //for browsing
            getMessageOptions.waitInterval = MQC.MQWI_UNLIMITED;
//MQC.MQWI_UNLIMITED;
// for waiting unlimted times
// waits unlimited
            MQMessage message = new MQMessage();
            BufferedWriter writer ;
            String strMsg;

            try {
                System.out.println( "waiting for message ... ");
                queue.get(message, getMessageOptions);
                System.out.println( "Get message sucessfull... ");
                byte[] b = new byte[message.getMessageLength()];
                message.readFully(b);
                strMsg = new String(b);
                System.out.println("\n"+strMsg);
// if empty message, close the queue...
                if ( strMsg.trim().equals("") ) {
                    System.out.println("empty message, closing the queue ..." + qName);
                }
                message.clearMessage();
                writer = new BufferedWriter(new FileWriter(msgFileName+"_"+new SimpleDateFormat("yyyyMMddhhmmss").format(new Date())+".xml", true));
                writer.write("\n");
                writer.write(new String(b));
                writer.write("\n");
                writer.close();
                getMessageOptions.options = MQC.MQOO_INPUT_AS_Q_DEF;
//MQC.MQGMO_BROWSE_NEXT MQC.MQGMO_WAIT ;
            } catch (IOException e) {
                System.out.println("IOException during GET in mqOpen: " + e.getMessage());
            }
        } catch ( MQException mqExp) {
            System.out.println("Error in opening queue ...."+"Queue Name : " + qName+" CC : " + mqExp.completionCode+" RC : " + mqExp.reasonCode);
            mqClose();
            mqDisconnect();
        }

    } //end of mqOpen

    private void mqClose() { // close the queue
        try {
            queue.close();
        } catch (MQException mqExp) {
            System.out.println("Error in closing queue ...."+"Queue Name : " + qName+" CC : " + mqExp.completionCode+" RC : " + mqExp.reasonCode);
        }

    }

    private void readPropertyFile(String fileName) throws Exception { // reading from the property file
        try {
            Properties mqProperties = new Properties();
            FileInputStream fileInputStream = new FileInputStream(fileName);
            mqProperties.load(fileInputStream);
            hostName = mqProperties.getProperty("hostName");
            qmgrName = mqProperties.getProperty("qmgrName");
            port = mqProperties.getProperty("port");
            channel = mqProperties.getProperty("channel");
            msgFileName = mqProperties.getProperty("msgFileName");
            qName = mqProperties.getProperty("qName");
            msgDir = mqProperties.getProperty("msgDir");
            fileInputStream.close();
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    public static void main(String args[]){
        JConsumer consumer = new JConsumer();
        consumer.startDownloading();
    }

}