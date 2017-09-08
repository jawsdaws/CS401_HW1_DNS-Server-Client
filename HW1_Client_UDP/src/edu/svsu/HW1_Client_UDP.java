// Homework 1: DNS Client UDP
// Student Name: James Daws
// Course: CS401, Fall 2017
// Instructor: Dr. Poonam Dharam
// Date finished: 09/08/2017
// Program description: This program is the DNS client side that uses UDP.
//
// Programmer Notes: UDP is a poor protocol for this application. UDP is unreliable!!!!
//                   Sometimes the output is correct, other times it is not.  The connect button
//                   Is only needed if the server was not started before the client.

package edu.svsu;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class HW1_Client_UDP extends Application {

    private GridPane grid;
    private Label lReturnedIpAddress;
    private Label lConnectedStatus;
    private Label lReturnedCount;
    private Button btnLookup;
    private Button btnConnect;
    private Scene scene;
    private TextField tfHostnameToLookUp;

    private InetAddress server;
    private DatagramSocket socket;
    private DatagramPacket rxPacket;
    private DatagramPacket sendPacket;
    private ByteArrayOutputStream outByteStream;
    private ByteArrayInputStream inByteStream;
    private ObjectInputStream inputFromServer;
    private ObjectOutputStream outputToServer;
    private String addressString;
    private String totalClientConnected;

    private int port = 8019;
    private byte[] rxData = new byte[4096];
    private byte[] sendData = new byte[4096];

    /**
     * Default constructor.
     */
    public HW1_Client_UDP() {
        grid = new GridPane();
        scene = new Scene(grid, 500, 200);
        tfHostnameToLookUp = new TextField();
        lReturnedIpAddress = new Label();
        lConnectedStatus = new Label("Not Connected");
        totalClientConnected = "Total clients connected: ";
        lReturnedCount = new Label(totalClientConnected);
        btnLookup = new Button("Lookup");
        btnConnect = new Button("Connect");
    }

    /**
     * Sort of the main.
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        //Gui setup.
        tfHostnameToLookUp.setMinSize(300, 10);
        tfHostnameToLookUp.setFont(Font.font("Courier New", 16));
        tfHostnameToLookUp.setPadding(new Insets(10, 10, 10, 10));
        tfHostnameToLookUp.setEditable(true);
        tfHostnameToLookUp.setText("Enter hostname to lookup here." + "\n");
        grid.setStyle("-fx-border-color: black");
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(tfHostnameToLookUp,1,1,1,1);
        grid.add(btnLookup, 2,1,1,1);
        grid.add(btnConnect, 2,3,1,1);
        grid.add(lReturnedIpAddress,1,2,1,1);
        grid.add(lConnectedStatus,1,3,1,1);
        grid.add(lReturnedCount,1,4,1,1);
        primaryStage.setTitle("DNS Client");
        primaryStage.setScene(scene);
        primaryStage.show();

        //Initial priming socket.
        try {
            server = InetAddress.getByName("localhost");
            socket = new DatagramSocket();
            System.out.println(socket.isConnected());
            lConnectedStatus.setText("Connected");
        } catch (Exception e) {}


        //Connected button is only need if the client was started before the server.
        btnConnect.setOnAction((ActionEvent event) -> {
            if(socket == null) {
                try {
                    socket = new DatagramSocket();
                    lConnectedStatus.setText("Connected");
                } catch (Exception e) {}
            }
        });

        //Send, rx, display
        btnLookup.setOnAction((ActionEvent event) -> {
            try {
                addressString = tfHostnameToLookUp.getText();
                outByteStream = new ByteArrayOutputStream(4096);
                outputToServer = new ObjectOutputStream(new BufferedOutputStream(outByteStream));
                IPData outData = new IPData(addressString);
                outputToServer.flush();
                outputToServer.writeObject(outData);
                outputToServer.flush();
                sendData = outByteStream.toByteArray();
                sendPacket = new DatagramPacket(sendData, sendData.length, server, port);
                socket.send(sendPacket);

                rxPacket = new DatagramPacket(rxData, sendData.length);
                socket.setSoTimeout(3000);
                socket.receive(rxPacket);
                rxData = rxPacket.getData();
                inByteStream = new ByteArrayInputStream(rxData);
                inputFromServer = new ObjectInputStream(inByteStream);
                IPData inData = (IPData) inputFromServer.readObject();
                lReturnedIpAddress.setText(inData.getStringData());
                lReturnedCount.setText(totalClientConnected + String.valueOf(inData.getCount()));


            } catch (Exception e) {
                lReturnedIpAddress.setText("Unable to connect to the DNS server.");
                try {
                    socket.close();
                    socket = null;
                } catch (Exception e1) {}
                lConnectedStatus.setText("Not Connected");
            }
        });

        //fire the button in case "Enter" is press on the testField.
        tfHostnameToLookUp.setOnAction((ActionEvent event) -> {
            btnLookup.fire();
        });
    }
}
