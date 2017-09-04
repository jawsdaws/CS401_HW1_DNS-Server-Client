import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class HW1_Client extends Application {

    private VBox vbox;
    private Scene scene;
    private TextArea taDisplay;
    private Socket socket;
    private ObjectInputStream inputFromServer;
    private ObjectOutputStream outputToServer;
    private String addressString;

    /**
     * Default constructor.
     */
    public HW1_Client() {
        vbox = new VBox();
        scene = new Scene(vbox, 500, 500);
        taDisplay = new TextArea();

        addressString = "www.google.com";
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        taDisplay.setMinSize(500, 500);
        taDisplay.setFont(Font.font("Courier New", 16));
        taDisplay.setPadding(new Insets(10, 10, 10, 10));
        taDisplay.setEditable(false);
        taDisplay.setText("Welcome to the DNS Client." + "\n");

        vbox.setStyle("-fx-border-color: black");
        vbox.getChildren().add(taDisplay);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);
        primaryStage.setTitle("DNS Server");
        primaryStage.setScene(scene);
        primaryStage.show();


        try {
            socket = new Socket("localhost", 8019);
            inputFromServer = new ObjectInputStream(socket.getInputStream());
            outputToServer = new ObjectOutputStream(socket.getOutputStream());

            outputToServer.writeObject(addressString);
            taDisplay.setText(inputFromServer.readObject().toString());

        } catch (IOException e) {

        }

    }
}
