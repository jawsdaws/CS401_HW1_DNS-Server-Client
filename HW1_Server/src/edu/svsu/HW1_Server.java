package edu.svsu;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class HW1_Server {

    private static int counter = 0;
    private static InetAddress hostAddress = null;
    private static InetAddress lookupAddress = null;

    public static void main(String[] args) {


        ServerSocket serverSocket = null;
        int port = 8019;
        String hostname = "localhost";

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
                ConnectionHandle connectionhandle = new ConnectionHandle(socket);
                Thread thread = new Thread(connectionhandle);
                thread.start();
            } catch (Exception E) {
                E.printStackTrace();
            }
        }
    }

    static class ConnectionHandle implements Runnable {

        private Socket socket;

        private ConnectionHandle(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            System.out.println("Connection.");
            counter++;
            try {
                ObjectOutputStream outputToClient = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream inputFromClient = new ObjectInputStream(socket.getInputStream());


                Object input =  inputFromClient.readObject();
                try {
                    lookupAddress = InetAddress.getByName(input.toString());
                } catch (UnknownHostException e) {
                    System.out.println("Unable to lookup ip address.");
                }

                outputToClient.writeObject(lookupAddress.getHostAddress());



            } catch (EOFException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}