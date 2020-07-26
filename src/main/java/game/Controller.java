package game;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class Controller {

    Main main;
    Game game;

    Alert gameWonAlert;
    Alert gameOverAlert;

    @FXML
    public Canvas canvas;

    @FXML
    ChoiceBox<Difficulty> difficultyChoiceBox;

    @FXML
    Text flagsText;

    public void init(){
        difficultyChoiceBox.getItems().addAll(Difficulty.values());
        difficultyChoiceBox.setValue(Difficulty.EASY);

        flagsText.setText("Flags: " + main.game.flagCount);

        gameWonAlert = new Alert(Alert.AlertType.INFORMATION);
        gameWonAlert.setHeaderText(null);
        gameWonAlert.setContentText("Congratulations! You Won!");

        gameOverAlert = new Alert(Alert.AlertType.INFORMATION);
        gameOverAlert.setHeaderText(null);
        gameOverAlert.setContentText("Game Over :(");

        this.game = main.game;
    }

    public void setMain(Main main){
        this.main = main;
    }

    @FXML
    void OnMouseClicked(MouseEvent e) {

        int size = game.size;
        int canvasSize =(int)canvas.getHeight();
        int x =(int) e.getX()/(canvasSize/size);
        int y =(int) e.getY()/(canvasSize/size);

        if(e.getButton() == MouseButton.PRIMARY){
            game.check(x, y);
        }
        else if(e.getButton() == MouseButton.SECONDARY){
            game.flag(x, y);
            flagsText.setText("Flags: "+game.flagCount);
        }
        paintCanvas();

        if(game.gameOver){
            gameOverAlert.showAndWait();
        }
        else if(game.gameWon){
            gameWonAlert.showAndWait();
        }
    }

    @FXML
    void NewGameAction(){
        game = new Game(difficultyChoiceBox.getValue());
        paintCanvas();
    }

    public void paintCanvas() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        int size = game.size;
        double canvasSize = canvas.getHeight();
        double cellSize = canvasSize/size;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(game.flags[i][j] == 1){
                    gc.setFill(Color.YELLOW);
                }
                else{
                    if(game.checked[i][j] == 0){
                        gc.setFill(Color.GRAY);
                    }
                    else{
                        if(game.bombs[i][j] == 1){
                            gc.setFill(Color.RED);
                        }
                        else{
                            gc.setFill(Color.GREEN);
                        }
                    }
                }
                gc.fillRect(i * cellSize, j * cellSize, i * cellSize + cellSize, j * cellSize + cellSize);

                //numbers
                if(game.checked[i][j] == 1 && game.adjacentBombs[i][j] != 0 && game.bombs[i][j] == 0){
                    gc.setFill(Color.BLACK);
                    gc.setFont(new Font("Computer Modern", 500/size));
                    gc.setTextAlign(TextAlignment.CENTER);
                    gc.fillText(String.valueOf(game.adjacentBombs[i][j]), i * cellSize + cellSize/2, j * cellSize + cellSize/1.2);
                }
            }
        }

        //lines
        gc.setLineWidth(1.5);
        gc.setStroke(Color.BLACK);
        for (int i = 0; i <= size; i++) {
            gc.strokeLine(i * cellSize, 0, i * cellSize, canvasSize);
            gc.strokeLine(0, i * cellSize, canvasSize, i * cellSize);
        }
    }




}