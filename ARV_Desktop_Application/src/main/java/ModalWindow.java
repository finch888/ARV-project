import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ModalWindow {
    public static void display (String path, String name) throws FileNotFoundException {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinHeight(900);

        FileInputStream fileInputStream = new FileInputStream(path);
        Image image = new Image(fileInputStream);
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(700);
        imageView.setFitWidth(1000);

        ToggleButton toggleButton = new ToggleButton("Show Name");
        toggleButton.setFont(new Font(18));

        Label label = new Label();
        label.visibleProperty().bind(toggleButton.selectedProperty());
        label.setText(name);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setFont(new Font(32));
        label.setPadding(new Insets(30));

        VBox layout = new VBox(40);
        layout.getChildren().addAll(imageView, toggleButton, label);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}
