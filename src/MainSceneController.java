import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class MainSceneController {

    GraphicsContext gc;

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
    void PenButton(ActionEvent event) {

    }

    @FXML
    void draw(MouseEvent e) {
        gc = board.getGraphicsContext2D();
        gc.setFill(Color.RED);
        gc.fillOval(e.getX(), e.getY(), 5, 5); // fonction à changer 
        System.out.println(e.getX() + " " + e.getY());
    }

    @FXML
    void handleZoomInButtonAction(ActionEvent event) {

    }

    @FXML
    void initialize() {
        gc = board.getGraphicsContext2D();
        board.setOnMouseDragged(this::draw);
        board.setStyle("-fx-background-color: black;");
        assert board != null : "fx:id=\"board\" was not injected: check your FXML file 'MainScene.fxml'.";
        assert btnUnZoom != null : "fx:id=\"btnUnZoom\" was not injected: check your FXML file 'MainScene.fxml'.";
        assert btnZoom != null : "fx:id=\"btnZoom\" was not injected: check your FXML file 'MainScene.fxml'.";

    }

}
