package game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class Main extends Application {

    Controller C;
    Game game;

    @Override
    public void start(Stage stage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Objects.requireNonNull(getClass().getClassLoader().getResource("control.fxml")));
        SplitPane rootLayout = loader.load();
        C = loader.getController();
        C.setMain(this);
        Scene scene = new Scene(rootLayout);
        stage.setScene(scene);
        stage.setTitle("Minesweeper");
        game = new Game(Difficulty.EASY);
        C.init();
        stage.show();
        C.paintCanvas();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
