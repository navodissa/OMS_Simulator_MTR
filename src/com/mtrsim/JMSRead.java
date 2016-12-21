package com.mtrsim;

/**
 * Created by Navoda on 12/14/2016.
 */


import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import org.apache.log4j.Logger;


public class JMSRead {

    // System exit status value (assume unset value to be 1)
    final static Logger logger = Logger.getLogger(JMSRead.class);
    private static int status = 1;
    private String producerQueue;
    private String consumerQueue;
    private String msg;
    private String JMSMsgID;

    public String getJMSMsgID() {
        return JMSMsgID;
    }

    public void setJMSMsgID(String JMSMsgID) {
        this.JMSMsgID = JMSMsgID;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getProducerQueue() {
        return producerQueue;
    }

    public void setProducerQueue(String producerQueue) {
        this.producerQueue = producerQueue;
    }

    public String getConsumerQueue() {
        return consumerQueue;
    }

    public void setConsumerQueue(String consumerQueue) {
        this.consumerQueue = consumerQueue;
    }

    public void Read(){
        Connection connection = null;
        Session session = null;
        Destination destination = null;
        Destination tempDestination = null;
        MessageProducer producer = null;
        MessageConsumer consumer = null;

        try {
            // Create a connection factory
            JmsFactoryFactory ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
            JmsConnectionFactory cf = ff.createConnectionFactory();

            // Set the properties
            cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, "127.0.0.1");
            cf.setIntProperty(WMQConstants.WMQ_PORT, 1415);
            cf.setStringProperty(WMQConstants.WMQ_CHANNEL, "CLIENT.TO.MBSHQM");
            cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
            cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, "MBSHQM");

            // Create JMS objects
            connection = cf.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            tempDestination = session.createQueue("queue:///" + consumerQueue);
            consumer = session.createConsumer(tempDestination);

            // Start the connection
            connection.start();

            // Now, receive the reply
            Message receivedMessage = consumer.receive(1000); // in ms or 15 seconds
            this.JMSMsgID = receivedMessage.getJMSMessageID();
            TextMessage receivedMsg = (TextMessage)receivedMessage;
            System.out.println("\nReceived message:\n" + receivedMsg);
            this.setMsg(receivedMsg.getText());

        } catch (NullPointerException e) { System.out.println("No Messages in Queue: " + consumerQueue); }
          catch (Exception e) {
              logger.error(e.getMessage()); }
    }

    //public static void main(String[] args) {
     public void Write(String msg) {
        // Variables
        Connection connection = null;
        Session session = null;
        Destination destination = null;
        Destination tempDestination = null;
        MessageProducer producer = null;
        MessageConsumer consumer = null;


        try {
            // Create a connection factory
            JmsFactoryFactory ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
            JmsConnectionFactory cf = ff.createConnectionFactory();

            // Set the properties
            cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, "127.0.0.1");
            cf.setIntProperty(WMQConstants.WMQ_PORT, 1415);
            cf.setStringProperty(WMQConstants.WMQ_CHANNEL, "CLIENT.TO.MBSHQM");
            cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
            cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, "MBSHQM");

            // Create JMS objects
            connection = cf.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue("queue:///" + producerQueue);
            producer = session.createProducer(destination);
            //tempDestination = session.createTemporaryQueue();
            //tempDestination = session.createQueue("queue:///" + consumerQueue);
            //consumer = session.createConsumer(tempDestination);

            //long uniqueNumber = System.currentTimeMillis() % 1000;
            TextMessage message = session
                    .createTextMessage(msg);

            // Set the JMSReplyTo
           // message.setJMSReplyTo(tempDestination);

            // Start the connection
            connection.start();
            message.setJMSCorrelationID(getJMSMsgID());
            // And, send the request
            producer.send(message);
            System.out.println("Sent message:\n" + message);

            // Now, receive the reply
            //Message receivedMessage = consumer.receive(15000); // in ms or 15 seconds
            //System.out.println("\nReceived message:\n" + receivedMessage);

            //recordSuccess();
        }
        catch (JMSException jmsex) {
            recordFailure(jmsex);
            logger.error(jmsex.getMessage());
        }
        finally {
            if (producer != null) {
                try {
                    producer.close();
                }
                catch (JMSException jmsex) {
                    System.out.println("Producer could not be closed.");
                    recordFailure(jmsex);
                    logger.error(jmsex.getMessage());
                }
            }
            if (consumer != null) {
                try {
                    consumer.close();
                }
                catch (JMSException jmsex) {
                    System.out.println("Consumer could not be closed.");
                    recordFailure(jmsex);
                    logger.error(jmsex.getMessage());
                }
            }

            if (session != null) {
                try {
                    session.close();
                }
                catch (JMSException jmsex) {
                    System.out.println("Session could not be closed.");
                    recordFailure(jmsex);
                    logger.error(jmsex.getMessage());
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                }
                catch (JMSException jmsex) {
                    System.out.println("Connection could not be closed.");
                    recordFailure(jmsex);
                    logger.error(jmsex.getMessage());
                }
            }
        }
        //System.exit(status);
        //return;
    } // end main()

    /**
     * Process a JMSException and any associated inner exceptions.
     *
     * @param jmsex
     */
    private static void processJMSException(JMSException jmsex) {
        System.out.println(jmsex);
        Throwable innerException = jmsex.getLinkedException();
        if (innerException != null) {
            System.out.println("Inner exception(s):");
        }
        while (innerException != null) {
            System.out.println(innerException);
            innerException = innerException.getCause();
        }
        return;
    }

    /**
     * Record this run as successful.
     */
    private static void recordSuccess() {
        System.out.println("SUCCESS");
        status = 0;
        return;
    }

    /**
     * Record this run as failure.
     *
     * @param ex
     */
    private static void recordFailure(Exception ex) {
        if (ex != null) {
            if (ex instanceof JMSException) {
                processJMSException((JMSException) ex);
            }
            else {
                System.out.println(ex);
            }
        }
        System.out.println("FAILURE");
        status = -1;
        return;
    }


}
