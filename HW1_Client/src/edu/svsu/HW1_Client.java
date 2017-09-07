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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class HW1_Client extends Application {

    private GridPane grid;
    private Label lReturnedIpAddress;
    private Label lConnectedStatus;
    private Button btnLookup;
    private Button btnConnect;
    private Scene scene;
    private TextField tfHostnameToLookUp;
    private Socket socket;
    private ObjectInputStream inputFromServer;
    private ObjectOutputStream outputToServer;
    private String addressString;

    /**
     * Default constructor.
     */
    public HW1_Client() {
        grid = new GridPane();
        scene = new Scene(grid, 500, 200);
        tfHostnameToLookUp = new TextField();
        lReturnedIpAddress = new Label();
        lConnectedStatus = new Label("Not Connected");
        btnLookup = new Button("Lookup");
        btnConnect = new Button("Connect");
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
        grid.add(btnConnect, 2,3,1,1);
        grid.add(lReturnedIpAddress,1,2,1,1);
        grid.add(lConnectedStatus,1,3,1,1);


        primaryStage.setTitle("DNS Client");
        primaryStage.setScene(scene);
        primaryStage.show();


        try{
            socket = new Socket("localhost", 8019);
            inputFromServer = new ObjectInputStream(socket.getInputStream());
            outputToServer = new ObjectOutputStream(socket.getOutputStream());
            lConnectedStatus.setText("Connected");
        } catch (IOException e) {}


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


        btnLookup.setOnAction((ActionEvent event) -> {
            try {
                addressString = tfHostnameToLookUp.getText();
                outputToServer.writeObject(addressString);
                lReturnedIpAddress.setText(inputFromServer.readObject().toString());

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
