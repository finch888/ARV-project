import Data.DataModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load((getClass().getResource("UI.fxml")));
        primaryStage.setTitle("ARV Desktop Application");
        primaryStage.setScene(new Scene(root, 890, 630));
        primaryStage.setResizable(false);
        primaryStage.show();
        DataModel.getInstance().open();

    }

    @Override
    public void stop() throws Exception {
        super.stop();
        DataModel.getInstance().close();
    }

    public static void main(String[] args) {
        try {
            DataModel.getInstance().createFolders();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        launch(args);
    }
}
