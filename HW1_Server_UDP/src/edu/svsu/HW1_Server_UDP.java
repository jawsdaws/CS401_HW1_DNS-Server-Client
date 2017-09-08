// Homework 1: DNS Server UDP
// Student Name: James Daws
// Course: CS401, Fall 2017
// Instructor: Dr. Poonam Dharam
// Date finished: 09/08/2017
// Program description: This program is the DNS server side that uses UDP.
//
// Programmer Notes: UDP is a poor protocal for this application. UDP is unreliable!!!!
//                   Sometimes the output is correct, other times it is not.  UDP also
//                   is connectionless, so threads are not needed.
//                   Most of the processing is done in the main while loop, and the processFile
//                   function handles the file work.

package edu.svsu;

import java.io.*;
import java.net.*;

public class HW1_Server_UDP {

    private static int counter = 0;
    private static InetAddress hostAddress = null;
    private static InetAddress returnAddress = null;
    private static InetAddress lookupAddress = null;
    private static ByteArrayOutputStream outByteStream;
    private static ObjectInputStream inputFromClient = null;
    private static ObjectOutputStream outputToClient = null;
    private static ByteArrayInputStream byteInput = null;
    private static DatagramSocket datagramSocket = null;
    private static DatagramPacket rxPacket = null;
    private static DatagramPacket sendPacket = null;
    private static byte[] rxData = new byte[4096];
    private static byte[] sendData = new byte[4096];
    private static int returnPort;
    private static String returnIP;


    /*
        Main.  Not much else to say.
    */
    public static void main(String[] args) {

        //Setup block.  Mostly used for debugging.
        int port = 8019;
        String hostname = "localhost";
        processFile(0);
        System.out.println("Welcome to the DNS server.");
        System.out.println("The hostname is " + hostname);

        try {
            hostAddress = InetAddress.getByName(hostname);
        } catch (UnknownHostException e) {
            System.out.println("Unable to lookup ip address.");
        }

        System.out.println("The ip is " + hostAddress.getHostAddress());

        try {
            datagramSocket = new DatagramSocket(port);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error listening on " + port);
        }

        //The primary while loop.  Listen. Rx a packet. Process it,
        // and send it back.  Last, count a connection.

        while (true) {
            try {
                System.out.println("Listening for clients.");
                rxPacket = new DatagramPacket(rxData, sendData.length);
                datagramSocket.receive(rxPacket);
                rxData = rxPacket.getData();
                returnAddress = rxPacket.getAddress();
                returnPort = rxPacket.getPort();
                byteInput = new ByteArrayInputStream(rxData);
                inputFromClient = new ObjectInputStream(byteInput);

                try {
                    lookupAddress = InetAddress.getByName((String) inputFromClient.readObject());
                    returnIP = lookupAddress.getHostAddress();
                } catch (UnknownHostException ue) {
                    ue.printStackTrace();
                    returnIP = "Unknown host";
                }

                outByteStream = new ByteArrayOutputStream(returnIP.getBytes().length);
                outputToClient = new ObjectOutputStream(new BufferedOutputStream(outByteStream));
                outputToClient.flush();
                outputToClient.writeObject(returnIP);
                outputToClient.flush();
                sendData = outByteStream.toByteArray();
                sendPacket = new DatagramPacket(sendData, sendData.length, returnAddress, returnPort);
                datagramSocket.send(sendPacket);
                processFile(1);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This function reads the count file and updates the count based on the int param.
     * @param count The amount to add to the count read from the file.
     *            Use -1 will decrement.
     */
    private static void processFile(int count) {

        File file = new File("countfile.txt");

        //Create a new file if one does not exist.
        if (count == 0 && !file.exists()) {
            try {
                file.createNewFile();
                BufferedWriter bw;
                FileWriter fw;
                fw = new FileWriter(file);
                bw = new BufferedWriter(fw);
                bw.write("0");
                bw.close();
            } catch (IOException e) {

            }
        //If there is anything other than zero add it to the total.(negatives decrement)
        } else {
            try {
                BufferedReader br;
                br = new BufferedReader(new FileReader(file));
                String line = br.readLine();
                br.close();
                BufferedWriter bw;
                FileWriter fw;
                fw = new FileWriter(file);
                bw = new BufferedWriter(fw);
                bw.write(Integer.toString(Integer.parseInt(line) + count));
                bw.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}