import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class MainSceneController {

    GraphicsContext gc;
    private String usage = "None";

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Canvas board;

    @FXML
    private Button btnUnZoom;

    @FXML
    private Button btnZoom;

    @FXML
    private ColorPicker colorchoice;

    @FXML
    private ToggleButton eraser;

    @FXML
    private ToggleButton pen;

    @FXML
    private Slider pensize;

    @FXML
    void PenButton(ActionEvent event) {
        if (pen.isSelected()){
            usage = "pen";
            eraser.setSelected(false); //eviter que pen et eraser soient visiblement selectionnes en meme temps
            System.out.println("pen selected");
        }else{
            usage = "None";
            System.out.println("no tool selected");
        }
    }

    @FXML
    void EraserButton(ActionEvent event) {
        if (eraser.isSelected()){
            usage = "eraser";
            pen.setSelected(false); //eviter que deux pen et eraser soient visiblement selectionnes en meme temps
            System.out.println("eraser selected");
        }else{
            usage = "None";
            System.out.println("no tool selected");
        }
    }

    @FXML
    void draw(MouseEvent e) {
        gc = board.getGraphicsContext2D();
        if (usage == "pen"){
            gc.setFill(colorchoice.getValue());
            gc.fillOval(e.getX(), e.getY(), pensize.getValue(), pensize.getValue());
        }
        if (usage == "eraser"){
            gc.setFill(Color.WHITESMOKE);
            gc.fillOval(e.getX(), e.getY(), 30, 30);
        } 
    }

    @FXML
    void handleZoomInButtonAction(ActionEvent event) {

    }

    @FXML
    void initialize() {
        /*gc = board.getGraphicsContext2D();
        board.setOnMouseDragged(this::draw);
        board.setStyle("-fx-background-color: black;");
        assert board != null : "fx:id=\"board\" was not injected: check your FXML file 'MainScene.fxml'.";
        assert btnUnZoom != null : "fx:id=\"btnUnZoom\" was not injected: check your FXML file 'MainScene.fxml'.";
        assert btnZoom != null : "fx:id=\"btnZoom\" was not injected: check your FXML file 'MainScene.fxml'.";
*/
    }

}
