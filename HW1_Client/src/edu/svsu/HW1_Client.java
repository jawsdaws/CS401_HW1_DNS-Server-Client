import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
    private Label lHostnameToLookUp;
    private Label lReturnedIpAddress;
    private Button btnLookup;
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
        lHostnameToLookUp = new Label("\"Hostname to lookup.\"");
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
            try {
                String serverReturn;
                socket = new Socket("localhost", 8019);
                addressString = tfHostnameToLookUp.getText();
                inputFromServer = new ObjectInputStream(socket.getInputStream());
                outputToServer = new ObjectOutputStream(socket.getOutputStream());

                outputToServer.writeObject(addressString);
                lReturnedIpAddress.setText(inputFromServer.readObject().toString());

            } catch (IOException e) {
                lReturnedIpAddress.setText("Unable to connect to the DNS server.");

            } catch (ClassNotFoundException e) {
                e.printStackTrace();

            }

        });
    }
}
