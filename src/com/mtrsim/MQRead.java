package com.mtrsim;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.ibm.mq.MQC;
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;

public class MQRead {
    private MQQueueManager _queueManager = null;
    public int port;
    public String hostname;
    public String channel;
    public String qManager;
    //public String inputQName = "MTR2OMS";
    private String inputQName;
    //public String outputQName = "OMS2MTR";
    private String outputQName;
    private String msg;
    private int qdepth;

    public int getQdepth() {
        return qdepth;
    }

    public String getMsg() {
        return msg;
    }

    public MQRead() {
        super();
    }

    public void init(String[] args) throws IllegalArgumentException {
// Set up MQ environment
        try{
            String filePath = new File("").getAbsolutePath();
            String fileName = (filePath + "\\MTRSimConfig.properties");
            this.readPropertyFile(fileName);
        } catch( Exception e) {
            e.printStackTrace();
        }
        MQEnvironment.hostname = hostname;
        MQEnvironment.channel = channel;
        MQEnvironment.port = port;
    }

 /*   public static void main(String[] args) {

        MQRead readQ = new MQRead();

        try {
            readQ.init(args);
            readQ.selectQMgr();
            readQ.read();
            readQ.write();
        } catch (IllegalArgumentException e) {
            System.out.println("Usage: java MQRead <-h host> <-p port> <-c channel> <-m QueueManagerName> <-q QueueName>");
            System.exit(1);
        } catch (MQException e) {
            System.out.println(e);
            System.exit(1);
        }
    }
*/

    public void setInputQName(String inputQName) {
        this.inputQName = inputQName;
    }

    public void setOutputQName(String outputQName) {
        this.outputQName = outputQName;
    }

    public void queueDepth(){
        int openOptions = MQC.MQOO_INQUIRE + MQC.MQOO_FAIL_IF_QUIESCING + MQC.MQOO_INPUT_SHARED;

        try {
            MQQueue queue = _queueManager.accessQueue(inputQName,
                    openOptions,
                    null, // default q manager
                    null, // no dynamic q name
                    null); // no alternate user id
            int depth = queue.getCurrentDepth();
            qdepth = depth;
            queue.close();
            _queueManager.disconnect();
        } catch ( Exception e) { e.printStackTrace(); }

    }

    public void read() throws MQException {
        int openOptions = MQC.MQOO_INQUIRE + MQC.MQOO_FAIL_IF_QUIESCING + MQC.MQOO_INPUT_SHARED;

        MQQueue queue = _queueManager.accessQueue(inputQName,
                openOptions,
                null, // default q manager
                null, // no dynamic q name
                null); // no alternate user id

        System.out.println("MQRead v1.0 connected.\n");

        int depth = queue.getCurrentDepth();
        qdepth = depth;
        System.out.println("Current depth: " + depth + "\n");
        //if (depth == 0) {
        //    return;
        //}

        MQGetMessageOptions getOptions = new MQGetMessageOptions();
        getOptions.options = MQC.MQGMO_NO_WAIT + MQC.MQGMO_FAIL_IF_QUIESCING + MQC.MQGMO_CONVERT;
//        while (true) {
            MQMessage message = new MQMessage();
            try {
                queue.get(message, getOptions);
                byte[] b = new byte[message.getMessageLength()];
                message.readFully(b);
                msg = new String(b);
                //System.out.println(new String(b));
                message.clearMessage();
            } catch (IOException e) {
                System.out.println("IOException during GET: " + e.getMessage());
 //               break;
            } catch (MQException e) {
                if (e.completionCode == 2 && e.reasonCode == MQException.MQRC_NO_MSG_AVAILABLE) {
                    if (depth > 0) {
                        System.out.println("All messages read.");
                    }
                } else {
                    System.out.println("GET Exception: " + e);
                }
 //               break;
            }
 //       }
        queue.close();
        _queueManager.disconnect();
    }

    public void selectQMgr() throws MQException {
        _queueManager = new MQQueueManager(qManager);
    }

    public void write(String msg) throws MQException {
        int lineNum = 0;
        int openOptions = MQC.MQOO_OUTPUT + MQC.MQOO_FAIL_IF_QUIESCING;
        try {
            MQQueue queue = _queueManager.accessQueue(outputQName,
                    openOptions,
                    null, // default q manager
                    null, // no dynamic q name
                    null); // no alternate user id

            DataInputStream input = new DataInputStream(System.in);

            System.out.println("MQWrite v1.0 connected");
            System.out.println("and ready for input, terminate with ^Z\n\n");

// Define a simple MQ message, and write some text in UTF format..
            MQMessage sendmsg = new MQMessage();
            sendmsg.format = MQC.MQFMT_STRING;
            sendmsg.feedback = MQC.MQFB_NONE;
            sendmsg.messageType = MQC.MQMT_DATAGRAM;
            sendmsg.replyToQueueName = "ROGER.QUEUE";
            sendmsg.replyToQueueManagerName = qManager;

            MQPutMessageOptions pmo = new MQPutMessageOptions(); // accept the defaults, same
// as MQPMO_DEFAULT constant

            String line = msg;
            sendmsg.clearMessage();
            sendmsg.messageId = MQC.MQMI_NONE;
            sendmsg.correlationId = MQC.MQCI_NONE;
            sendmsg.writeString(line);

// put the message on the queue

            queue.put(sendmsg, pmo);
            System.out.println(++lineNum + ": " + line);

            queue.close();
            _queueManager.disconnect();

        } catch (com.ibm.mq.MQException mqex) {
            System.out.println(mqex);
        } catch (java.io.IOException ioex) {
            System.out.println("An MQ IO error occurred : " + ioex);
        }

    }

    private void readPropertyFile(String fileName) throws Exception { // reading from the property file
        try {
            Properties mqProperties = new Properties();
            FileInputStream fileInputStream = new FileInputStream(fileName);
            mqProperties.load(fileInputStream);
            hostname = mqProperties.getProperty("hostName");
            qManager = mqProperties.getProperty("qmgrName");
            port = Integer.parseInt(mqProperties.getProperty("port"));
            channel = mqProperties.getProperty("channel");
            //msgFileName = mqProperties.getProperty("msgFileName");
            inputQName = mqProperties.getProperty("inputqueuename");
            //outputQName = mqProperties.getProperty("outputqueuename");
            //msgDir = mqProperties.getProperty("msgDir");
            fileInputStream.close();
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }
}