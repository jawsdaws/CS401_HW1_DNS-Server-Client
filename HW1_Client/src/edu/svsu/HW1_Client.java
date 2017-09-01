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
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


public class HW1_Client extends Application {

    private VBox vbox;
    private Scene scene;
    private TextArea taDisplay;
    private String hostname;

    /**
     * Default constructor.
     */
    public HW1_Client() {
        hostname = "localhost";
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
        taDisplay.setText("Welcome to the DNS Server." + "\n");
        taDisplay.appendText("The hostname is " + hostname + "\n");

        vbox.setStyle("-fx-border-color: black");
        vbox.getChildren().add(taDisplay);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);
        primaryStage.setTitle("DNS Server");
        primaryStage.setScene(scene);
        primaryStage.show();

        server();

    }

    private void server() {

        InetAddress address = null;
        try {
            address = InetAddress.getByName(hostname);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        taDisplay.appendText("The ip is " + address.getHostAddress() + "\n");
        SocketAcceptLoop sal = new SocketAcceptLoop();
        Thread thread = new Thread(sal);
        thread.start();
    }

    class SocketAcceptLoop implements Runnable {

        @Override
        public void run() {
            taDisplay.appendText("Listening for clients.\n");
            while (true) {
                try {
                    ServerSocket serverSocket = new ServerSocket(8000);
                    Socket socket = serverSocket.accept();
                    ConnectionHandle connectionhandle = new ConnectionHandle(socket);
                    Thread thread = new Thread(connectionhandle);
                    thread.start();
                } catch (Exception E) {

                }
            }
        }
    }

    class ConnectionHandle implements Runnable {

        private Socket socket;

        private ConnectionHandle(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
                DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
