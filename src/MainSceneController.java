import java.beans.EventHandler;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;


public class MainSceneController {

    GraphicsContext gc;
    private String usage = "pen"; //default
    private double erasersize = 15; //default
    private double pensize = 10; //default
    private double radius;
    

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Canvas board;
    
    @FXML
    private AnchorPane window;

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
    private MenuItem RecButton;

    @FXML
    private HBox MenusTools;
    
    @FXML
    private Slider Size;

    @FXML
    private Circle eraserviewer;

    // Variables pour la création de rectangle
    private Rectangle rect = new Rectangle();
    private boolean isDrawingRect = false;
    private double rectStartX, rectStartY;
    private double rectWidth, rectHeight;

    @FXML 
    void Changesize(MouseEvent event){
        if (eraser.isSelected()){
            System.out.println("erasersize changing");
            erasersize = Size.getValue();
            eraserviewer.setRadius(erasersize);
        }
        if (pen.isSelected()){
            System.out.println("pensize changing");
            pensize = Size.getValue();
        }
    }

    @FXML
    void PenButton(ActionEvent event) {
        if (pen.isSelected()){
            Size.setValue(pensize);
            usage = "pen";
            eraser.setSelected(false); //eviter que pen et eraser soient visiblement selectionnes en meme temps
            eraserviewer.setVisible(false);
            System.out.println("pen selected");
        }else{
            usage = "None";
            System.out.println("no tool selected");
        }
    }

    @FXML
    void EraserButton(ActionEvent event) {
        Background.fill(Color.BLUEVIOLET);
        if (eraser.isSelected()){
            Size.setValue(erasersize);
            usage = "eraser";
            eraserviewer.toFront();
            eraserviewer.setVisible(true);
            pen.setSelected(false); //eviter que deux pen et eraser soient visiblement selectionnes en meme temps
            System.out.println("eraser selected");
        }else{
            eraserviewer.setVisible(false);
            usage = "None";
            System.out.println("no tool selected");
        }
    }

    @FXML 
    void Eraserview(MouseEvent e){
        eraserviewer.setRadius(erasersize);
        MenusTools.toFront();
        if (eraser.isSelected()){
            // gestion de la zone de l'effaceur non exterieur au canvas (en haut et en bas)
            if (e.getY() >= 0 & e.getY() <= board.getHeight()){
                eraserviewer.setCenterX(e.getX());
                eraserviewer.setCenterY(e.getY());  
            }else{
                eraserviewer.setCenterX(e.getX());
            }
            // gestion de la zone de l'effaceur non exterieur au canvas (à droite et à gauche)
            if (e.getX() >= 0 & e.getX() <= board.getWidth()){
                eraserviewer.setCenterX(e.getX());
                eraserviewer.setCenterY(e.getY()); 
            }else{
                eraserviewer.setCenterY(e.getY());
            }  
        }
    }

    @FXML
    void Draw(MouseEvent e) {
        gc = board.getGraphicsContext2D();
        radius = eraserviewer.getRadius();
        if (usage == "pen" & pen.isSelected()) {
            gc.setFill(colorchoice.getValue());
            if (e.getY() >= 0) {
                gc.fillOval(e.getX() - pensize/2, e.getY()- pensize/2, pensize, pensize);
            } else {
                gc.fillOval(e.getX() - pensize/2, - pensize/2, pensize, pensize);
            }
        }  
        if (usage == "eraser" & eraser.isSelected()) {
            gc.setFill(Color.WHITESMOKE);
            gc.fillOval(e.getX()-radius, e.getY()-radius, radius * 2, radius * 2);  
        } else if (usage == "rect" & isDrawingRect) {
            // Calculer les dimensions du rectangle en fonction de la position de la souris
            double rectX = Math.min(e.getX(), rectStartX);
            double rectY = Math.min(e.getY(), rectStartY);
            double rectWidth = Math.abs(e.getX() - rectStartX);
            double rectHeight = Math.abs(e.getY() - rectStartY);
            
            // Redessiner le canevas sans le rectangle précédent
            gc.clearRect(0, 0, board.getWidth(), board.getHeight());
            
            // Dessiner le rectangle actuel
            gc.setStroke(colorchoice.getValue());
            gc.strokeRect(rectX, rectY, rectWidth, rectHeight);
        }
        Eraserview(e);
    }
    
    
    @FXML
    void DrawRec(ActionEvent event) {
        // Changer l'outil de dessin
        usage = "rect";
        isDrawingRect = true;
        pen.setSelected(false);
        eraser.setSelected(false);
        eraserviewer.setVisible(false);
        
        // Écouter les événements de la souris pour dessiner le rectangle
        board.setOnMousePressed(e -> {
            rectStartX = e.getX();
            rectStartY = e.getY();
            isDrawingRect = true;
        });
        
        board.setOnMouseReleased(e -> {
            isDrawingRect = false;
        });
    }
    

    @FXML
    void RectStart(MouseEvent e) {
        // Enregistrer les coordonnées de début du rectangle
        rectStartX = e.getX();
        rectStartY = e.getY();
        isDrawingRect = true;
    }
    

    @FXML
    void RectEnd(MouseEvent e) {
        // Enregistrer les coordonnées finales du rectangle
        double rectEndX = e.getX();
        double rectEndY = e.getY();
        
        // Dessiner le rectangle final
        gc.setStroke(colorchoice.getValue());
        gc.strokeRect(rectStartX, rectStartY, rectEndX - rectStartX, rectEndY - rectStartY);
        
        // Réinitialiser les variables de dessin de rectangle
        isDrawingRect = false;
        rectStartX = 0;
        rectStartY = 0;
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
