package edu.svsu;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class HW1_Server {

    public static void main(String[] args) {

        InetAddress address = null;
        String hostname = "localhost";
        System.out.println("Welcome to the DNS server.");
        System.out.println("The hostname is " + hostname);

        try {
            address = InetAddress.getByName(hostname);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        System.out.println("The ip is " + address.getHostAddress());
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8019);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            System.out.println("Listening for clients.");


            try {

                Socket socket = serverSocket.accept();
                ConnectionHandle connectionhandle = new ConnectionHandle(socket);
                Thread thread = new Thread(connectionhandle);
                thread.start();
            } catch (Exception E) {
                //E.printStackTrace();
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
            try {
                DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
                DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());
                //while(true){
                    //System.out.println("FUCK");
                //}
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}