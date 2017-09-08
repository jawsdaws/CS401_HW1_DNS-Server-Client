// Homework 1: DNS Client TCP
// Student Name: James Daws
// Course: CS401, Fall 2017
// Instructor: Dr. Poonam Dharam
// Date finished: 09/08/2017
// Program description: This program is the DNS client side that uses TCP.
//
// Programmer Notes: TCP is the proper protocol for this application. UDP is unreliable!!!!
//                   The output should be reliable.  The connect button
//                   is only needed if the server was not started before the client.
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

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class HW1_Client_TCP extends Application {

    private GridPane grid;
    private Label lReturnedIpAddress;
    private Label lConnectedStatus;
    private Label lReturnedCount;
    private Button btnLookup;
    private Button btnConnect;
    private Scene scene;
    private TextField tfHostnameToLookUp;
    private Socket socket;
    private ObjectInputStream inputFromServer;
    private ObjectOutputStream outputToServer;
    private String addressString;
    private String totalClientConnected;

    /**
     * Default constructor.
     */
    public HW1_Client_TCP() {
        totalClientConnected = "Total clients connected: ";
        grid = new GridPane();
        scene = new Scene(grid, 500, 200);
        tfHostnameToLookUp = new TextField();
        lReturnedIpAddress = new Label();
        lConnectedStatus = new Label("Not Connected");
        lReturnedCount = new Label(totalClientConnected);
        btnLookup = new Button("Lookup");
        btnConnect = new Button("Connect");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Gui Setup
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
        grid.add(lReturnedCount, 1 ,4,1,1);
        primaryStage.setTitle("DNS Client");
        primaryStage.setScene(scene);
        primaryStage.show();

        //prime the socket
        try{
            socket = new Socket("localhost", 8019);
            inputFromServer = new ObjectInputStream(socket.getInputStream());
            outputToServer = new ObjectOutputStream(socket.getOutputStream());
            lConnectedStatus.setText("Connected");
        } catch (Exception e) {}


        //Reconnect if the connection is lost.
        btnConnect.setOnAction((ActionEvent event) -> {
            if(socket == null) {
                try {
                    socket = new Socket("localhost", 8019);
                    inputFromServer = new ObjectInputStream(socket.getInputStream());
                    outputToServer = new ObjectOutputStream(socket.getOutputStream());
                    lConnectedStatus.setText("Connected");
                } catch (Exception e) {}
            }
        });


        //send the ip lookup request to the server
        btnLookup.setOnAction((ActionEvent event) -> {
            try {
                addressString = tfHostnameToLookUp.getText();
                IPData outData = new IPData(addressString);
                outputToServer.writeObject(outData);
                IPData inData = new IPData();
                inData = (IPData) inputFromServer.readObject();
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
    }
}
