package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextArea;


public class HW1_Client extends Application {

    private Label lTopLabel;
    private Stage primaryStage;
    private VBox vbox;
    private Scene scene;
    private TextArea taDisplay;

    /**
     * Default constructor.
     */
    public HW1_Client() {
        lTopLabel = new Label("Welcome to the DNS Server!!");
        vbox = new VBox();
        scene = new Scene(vbox, 500, 500);
        taDisplay = new TextArea();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        taDisplay.setMinSize(500, 500);
        taDisplay.setFont(Font.font("Courier New", 16));
        taDisplay.setPadding(new Insets(10, 10, 10, 10));
        taDisplay.setEditable(false);
        taDisplay.setText("FUCKKK!!!!!");

        vbox.setStyle("-fx-border-color: black");
        vbox.getChildren().add(taDisplay);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);
        primaryStage.setTitle("DNS Server");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
