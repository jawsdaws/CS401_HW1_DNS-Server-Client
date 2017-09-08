package edu.svsu;

import java.io.*;
import java.net.*;

public class HW1_Server_UDP {

    private static int counter = 0;
    private static InetAddress hostAddress = null;
    private static InetAddress returnAddress = null;
    private static InetAddress lookupAddress = null;
    private static ByteArrayOutputStream outByteStream;
    private static ByteArrayInputStream inByteStream;
    private static ObjectInputStream inputFromClient = null;
    private static ObjectOutputStream outputToClient = null;
    private static ByteArrayInputStream byteInput = null;
    private static DatagramSocket datagramSocket = null;
    private static DatagramPacket rxPacket = null;
    private static DatagramPacket sendPacket = null;
    private static byte[] rxData = new byte[2048];
    private static byte[] sendData = new byte[2048];
    private static int returnPort;

    public static void main(String[] args) {


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


                lookupAddress = InetAddress.getByName((String) inputFromClient.readObject());
                System.out.println(lookupAddress.getHostAddress());

                outByteStream = new ByteArrayOutputStream(2048);
                outputToClient = new ObjectOutputStream(new BufferedOutputStream(outByteStream));
                outputToClient.flush();
                outputToClient.writeObject(lookupAddress.getHostAddress());
                outputToClient.flush();
                sendData = outByteStream.toByteArray();
                sendPacket = new DatagramPacket(sendData, sendData.length, returnAddress, returnPort);
                datagramSocket.send(sendPacket);



            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("FUCK!!");
            }
        }
    }

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
                    Object input =  inputFromClient.readObject();
                    System.out.println(input.toString());
                    try {
                        lookupAddress = InetAddress.getByName(input.toString());
                        outputToClient.writeObject(lookupAddress.getHostAddress());
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
    private static void processFile(int count) {

        File file = new File("countfile.txt");

        //0 should only be passed in count if there is no file.
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