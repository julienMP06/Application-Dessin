import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.*;
import javafx.scene.input.MouseEvent;

public class MainSceneController {
    GraphicsContext gc;

    @FXML 
    private Canvas board;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    void PenButton(ActionEvent event) {

    }

    @FXML
    void initialize() {

    }

    @FXML
    void draw(MouseEvent e){
        gc = board.getGraphicsContext2D();
        gc.fillOval(e.getX(), e.getY(), 5, 5); // fonction à changer 
        
    }

}
