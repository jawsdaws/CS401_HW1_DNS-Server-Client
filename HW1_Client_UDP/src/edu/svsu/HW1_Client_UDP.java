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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;


public class HW1_Client_UDP extends Application {

    private GridPane grid;
    private Label lReturnedIpAddress;
    private Button btnLookup;
    private Scene scene;
    private TextField tfHostnameToLookUp;
    private DatagramSocket socket;
    private DatagramPacket sendPacket;
    private ByteArrayOutputStream outputStream;
    private ObjectInputStream inputFromServer;
    private ObjectOutputStream outputToServer;
    private String addressString;
    private InetAddress serverAddress;
    private String server = "localhost";
    private int port = 8019;

    /**
     * Default constructor.
     */
    public HW1_Client_UDP() {
        grid = new GridPane();
        scene = new Scene(grid, 500, 200);
        tfHostnameToLookUp = new TextField();
        lReturnedIpAddress = new Label();
        btnLookup = new Button("Lookup");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
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
        grid.add(lReturnedIpAddress,1,2,1,1);


        primaryStage.setTitle("DNS Client");
        primaryStage.setScene(scene);
        primaryStage.show();


        btnLookup.setOnAction((ActionEvent event) -> {
            try{
                addressString = tfHostnameToLookUp.getText();

                socket = new DatagramSocket();
                serverAddress = InetAddress.getByName(server);
                outputStream = new ByteArrayOutputStream();
                outputToServer = new ObjectOutputStream(outputStream);
                outputToServer.writeObject(addressString);
                System.out.println("FUCK!!!");
                byte[] data = outputStream.toByteArray();
                sendPacket = new DatagramPacket(data, data.length, serverAddress, port);
                socket.send(sendPacket);
                //addressString = tfHostnameToLookUp.getText();
                //outputToServer.writeObject(addressString);
                //lReturnedIpAddress.setText(inputFromServer.readObject().toString());

            } catch (Exception e) {
                lReturnedIpAddress.setText("Unable to connect to the DNS server.");
                try {
                    socket.close();
                    socket = null;
                } catch (Exception e1) {}
            }
        });
    }
}
