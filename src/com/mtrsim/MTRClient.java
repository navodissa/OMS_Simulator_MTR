package com.mtrsim;

import com.ibm.mq.MQException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Navoda on 11/30/2016.
 */
public class MTRClient {
    public static void main(String[] args) {


            String[] reqests = {"35=X;9998=061220161541;8000=HSBCNET24;8001=Password@123;8002=MUB;8102=E;8010=192.168.0.1;",
                                "9999=P;9998=06122016219205;9975=HSBCNET24;1=0600054124;8002=MUB;",
                                "9999=B;9998=06122016219206;9975=HSBCNET24;1=0600054124;8002=MUB;",
                                "9999=L;9998=06122016219206;9975=HSBCNET24;1=0600054124;8002=MUB;"};
                    /* ,
                    "35=X;9998=53232;8000=ssbcnet24;8001=password@222;8002=PRO;8102=A",
                    "35=X;9998=44512;8000=Asbcnet23;8001=password@123;8002=MUB;8102=A",
                    "35=B;9998=66643;8000=Hsbcnet28;8001=password@123;8002=MOB;8102=E"}; */
            MQRead readQ = new MQRead();
            for (int counter = 0; counter < reqests.length; counter++) {
                try {
                    readQ.init(args);
                    readQ.selectQMgr();
                    readQ.setOutputQName("OMS_AUTH_REQ");
                    readQ.write(reqests[counter]);
                } catch (IllegalArgumentException e) {
                    System.out.println("Usage: java MQRead <-h host> <-p port> <-c channel> <-m QueueManagerName> <-q QueueName>");
                    System.exit(1);
                } catch (MQException e) {
                    System.out.println(e);
                    System.exit(1);
                }
                //                   Socket s = new Socket("localhost", 6666); // Working
                //                  DataOutputStream dout = new DataOutputStream(s.getOutputStream());  // Working
                //                  dout.writeUTF(reqests[counter]);  // Working
//            dout.writeUTF("35=X;9998=54412;8000=Hsbcnet24;8001=password@123;8002=MUB;8102=E");
//            String ex = "open";
//            while (ex != "Exit"){
//                Scanner reader = new Scanner(System.in);
//               String n = reader.nextLine();
//                dout.writeUTF(n);
//            }

//                    DataInputStream dis = new DataInputStream(s.getInputStream());  // Working
//                    String res = (String) dis.readUTF();  // Working
//                    System.out.println(res);  // Working

//                    dout.flush();  // Working
//                    dout.close();  // Working
//                    s.close();  // Working


            }
        }
    }

