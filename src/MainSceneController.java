import java.beans.EventHandler;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
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
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;


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
    private MenuItem SaveButton;

    @FXML
    private MenuItem openButton;

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
    private MenuItem CircleButton;

    @FXML
    private MenuItem TriangleButton;

    @FXML
    private HBox MenusTools;
    
    @FXML
    private Slider Size;

    @FXML
    private Circle eraserviewer;

    private WritableImage drawings;

    // Variables pour la création de rectangle
    private boolean isDrawingRect = false;
    private double rectStartX, rectStartY;
    private double rectWidth, rectHeight;

    private double circleStartX, circleStartY;
    private double circleWidth, circleHeight;

    private double triangleStartX, triangleStartY;
    private double triangleWidth, triangleHeight;

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
        }
        if (usage == "rect" & isDrawingRect) {
            // drawings = board.snapshot(null, null);
            gc.clearRect(0, 0, board.getWidth(), board.getHeight());
            // gc.drawImage(drawings, 0, 0);
            rectWidth = Math.abs(e.getX() - rectStartX);
            rectHeight = Math.abs(e.getY() - rectStartY);
            double rectX = Math.min(e.getX(), rectStartX);
            double rectY = Math.min(e.getY(), rectStartY);
            gc.setStroke(colorchoice.getValue());
            gc.setLineWidth(pensize);
            gc.strokeRect(rectX, rectY, rectWidth, rectHeight);
        }    
        if (usage == "circle") {
            gc.clearRect(0, 0, board.getWidth(), board.getHeight());
            circleWidth = Math.abs(e.getX() - circleStartX);
            circleHeight = Math.abs(e.getY() - circleStartY);
            double circleX = Math.min(e.getX(), circleStartX);
            double circleY = Math.min(e.getY(), circleStartY);
            gc.setStroke(colorchoice.getValue());
            gc.setLineWidth(pensize);
            gc.strokeOval(circleX, circleY, circleWidth, circleHeight);
        }    
        if (usage == "triangle") {
            gc.clearRect(0, 0, board.getWidth(), board.getHeight());
            triangleWidth = Math.abs(e.getX() - triangleStartX);
            triangleHeight = Math.abs(e.getY() - triangleStartY);
            double triangleX = Math.min(e.getX(), triangleStartX);
            double triangleY = Math.min(e.getY(), triangleStartY);
            double sideLength = Math.sqrt(Math.pow(e.getX() - triangleX, 2) + Math.pow(e.getY() - triangleY, 2));
            double[] xPoints = {triangleX, e.getX(), triangleX + sideLength};
            double[] yPoints = {triangleY, e.getY(), triangleY + sideLength};
            gc.setStroke(colorchoice.getValue());
            gc.setLineWidth(pensize);
            gc.strokePolygon(xPoints, yPoints, 3);
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
    void DrawCircle(ActionEvent event) {
        // Changer l'outil de dessin
        usage = "circle";
        pen.setSelected(false);
        eraser.setSelected(false);
        eraserviewer.setVisible(false);
            
        // Écouter les événements de la souris pour dessiner le rectangle
        board.setOnMousePressed(e -> {
            circleStartX = e.getX();
            circleStartY = e.getY();
        });
            
        board.setOnMouseReleased(e -> {
        });
    }

    @FXML
    void DrawTriangle(ActionEvent event) {
        // Changer l'outil de dessin
        usage = "triangle";
        pen.setSelected(false);
        eraser.setSelected(false);
        eraserviewer.setVisible(false);
            
        // Écouter les événements de la souris pour dessiner le rectangle
        board.setOnMousePressed(e -> {
            triangleStartX = e.getX();
            triangleStartY = e.getY();
        });
            
        board.setOnMouseReleased(e -> {
        });
    }

    @FXML
    void RectButton(ActionEvent event) {
        usage = "rect";
    }

    @FXML
    void CircleButton(ActionEvent event) {
        usage = "rect";
    }

    // Implémenter un bouton effacer toute la page
    @FXML
    void ClearButton(ActionEvent event) {
        gc.clearRect(0, 0, board.getWidth(), board.getHeight());
    }
    
    @FXML
    private void saveDrawing(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le dessin");
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                WritableImage image = new WritableImage((int) board.getWidth(), (int) board.getHeight());
                board.snapshot(null, image);
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (IOException ex) {
                // Gérer les erreurs d'écriture de fichier ici
            }
        }
    }   

    @FXML
    private void openFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ouvrir un fichier");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            // Ajoutez ici le code pour traiter le fichier ouvert
        }
    }


    @FXML
    void handleZoomInButtonAction(ActionEvent event) {

    }

    @FXML
    void initialize() {
        drawings = new WritableImage((int) board.getWidth(), (int) board.getHeight());
        /*gc = board.getGraphicsContext2D();
        board.setOnMouseDragged(this::draw);
        board.setStyle("-fx-background-color: black;");
        assert board != null : "fx:id=\"board\" was not injected: check your FXML file 'MainScene.fxml'.";
        assert btnUnZoom != null : "fx:id=\"btnUnZoom\" was not injected: check your FXML file 'MainScene.fxml'.";
        assert btnZoom != null : "fx:id=\"btnZoom\" was not injected: check your FXML file 'MainScene.fxml'.";
*/
    }

}
