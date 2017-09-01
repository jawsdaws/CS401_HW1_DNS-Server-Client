import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextArea;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;


public class HW1_Client extends Application {

    private VBox vbox;
    private Scene scene;
    private TextArea taDisplay;
    private Socket socket;
    private DataInputStream inputFromServer;
    private DataOutputStream outputToServer;

    /**
     * Default constructor.
     */
    public HW1_Client() {
        vbox = new VBox();
        scene = new Scene(vbox, 500, 500);
        taDisplay = new TextArea();
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

        socket = new Socket("localhost", 8019);
        inputFromServer = new DataInputStream(socket.getInputStream());
        outputToServer = new DataOutputStream(socket.getOutputStream());
    }
}
