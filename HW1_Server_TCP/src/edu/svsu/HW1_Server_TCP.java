// Homework 1: DNS Server TCP
// Student Name: James Daws
// Course: CS401, Fall 2017
// Instructor: Dr. Poonam Dharam
// Date finished: 09/08/2017
// Program description: This program is the DNS server side that uses TCP.
//
// Programmer Notes: TCP is the protocol of choice for this application. TCP is reliable.
//                   Sometimes the output is correct, other times it is not.  UDP also
//                   is connectionless, so threads are not needed.
//                   A while loop starts a new thread for each client that connects.
//                   The processFile function handles the file work.
package edu.svsu;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


public class HW1_Server_TCP {

    private static InetAddress hostAddress = null;
    private static InetAddress lookupAddress = null;

    /**
     * Main
     * @param args not used
     */
    public static void main(String[] args) {


        ServerSocket serverSocket = null;
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
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Error listening on " + port);
        }

        while (true) {
            System.out.println("Listening for clients.");
            try {
                Socket socket = serverSocket.accept();
                processFile(1);
                ConnectionHandle connectionhandle = new ConnectionHandle(socket);
                Thread thread = new Thread(connectionhandle);
                thread.start();
            } catch (Exception E) {
                E.printStackTrace();
            }
        }
    }

    /**
     * The thread portion of the TCP connection so that this serve can have multiple clients.
     */
    private static class ConnectionHandle implements Runnable {

        private Socket socket;

        private ConnectionHandle(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                ObjectOutputStream outputToClient = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream inputFromClient = new ObjectInputStream(socket.getInputStream());


                while (true) {
                    IPData input = (IPData) inputFromClient.readObject();

                    System.out.println(input.getStringData());
                    try {
                        lookupAddress = InetAddress.getByName(input.getStringData());
                        IPData outData = new IPData(lookupAddress.getHostAddress(), processFile(0));
                        outputToClient.writeObject(outData);
                    } catch (UnknownHostException e) {
                        System.out.println("Unable to lookup ip address.");
                        outputToClient.writeObject("Unable to lookup ip address.");
                    }
                }
            } catch (EOFException e) {
                System.out.println("Client disconnected");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * This function reads the count file and updates the count based on the int param.
     * @param count The amount to add to the count read from the file.
     *            Use -1 will decrement.
     */
    private static int processFile(int count) {

        File file = new File("countfile.txt");

        //Check if the file exists, and create new if not.
        if (count == 0 && !file.exists()) {
            try {
                file.createNewFile();
                BufferedWriter bw;
                FileWriter fw;
                fw = new FileWriter(file);
                bw = new BufferedWriter(fw);
                bw.write("0");
                bw.close();
                return 0;
            } catch (IOException e) {

            }
        //If there is anything else add it to the total.(negatives decrement)
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
                return Integer.parseInt(line);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } return 0;
    }
}