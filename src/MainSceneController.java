import java.beans.EventHandler;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
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
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import java.util.Vector;

public class MainSceneController {

    GraphicsContext gc;
    private double radius;

    // default settings 
    private String usage = "None"; 
    private double erasersize = 15; 
    private double pensize = 10; 
    private double shapesize = 5; 
    private Color color = Color.BLACK;
    

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
    private CheckMenuItem rectangle;

    @FXML
    private CheckMenuItem circle;

    @FXML
    private CheckMenuItem triangle;

    @FXML
    private CheckMenuItem line;

    @FXML
    private HBox MenusTools;
    
    @FXML
    private Slider Size;

    @FXML
    private Circle eraserviewer;

    private WritableImage drawings;

    private boolean rectselected, circleselected, triangleselected = false;

    // Variables pour la création de rectangle
    private double ShapeStartX, ShapeStartY;


    //private double rectStartX, rectStartY;
    private double rectWidth, rectHeight;
    private double circleWidth, circleHeight;
    private double triangleWidth, triangleHeight;

    Vector<Rectangle> rectangles = new Vector<Rectangle>();
    Vector<Line> lignes = new Vector<Line>();
    

    @FXML 
    void Changesize(MouseEvent e){
        if (eraser.isSelected()){
            System.out.println("erasersize changing");
            erasersize = Size.getValue();
            eraserviewer.setRadius(erasersize);
        }
        if (pen.isSelected()){
            System.out.println("pensize changing");
            pensize = Size.getValue();
        }
        if (rectangle.isSelected() | circle.isSelected() | triangle.isSelected() | line.isSelected()){
            System.out.println("shapesize changing");
            shapesize = Size.getValue();
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

    void ToolManager(String tool){
        usage = tool;
        

        if (tool == "pen"){

            eraser.setSelected(false); 
            rectangle.setSelected(false);
            circle.setSelected(false);
            triangle.setSelected(false);
            line.setSelected(false);

            eraserviewer.setVisible(false);

        }
        if (tool == "eraser"){

            pen.setSelected(false); 
            rectangle.setSelected(false);
            circle.setSelected(false);
            triangle.setSelected(false);
            line.setSelected(false);

            //eraserviewer.toFront();
            eraserviewer.setVisible(true);

        }
        if (tool == "rect"){

            circle.setSelected(false);
            triangle.setSelected(false);
            pen.setSelected(false); 
            eraser.setSelected(false);
            line.setSelected(false);
        
            eraserviewer.setVisible(false);

        }
        if (tool == "circle"){
            
            rectangle.setSelected(false);
            triangle.setSelected(false);
            pen.setSelected(false); 
            eraser.setSelected(false);
            line.setSelected(false);
            
            eraserviewer.setVisible(false);

        }
        if (tool == "triangle"){

            rectangle.setSelected(false);
            circle.setSelected(false);
            pen.setSelected(false); 
            eraser.setSelected(false);
            line.setSelected(false);
               
            eraserviewer.setVisible(false);
        }

        if (tool == "line"){

            rectangle.setSelected(false);
            circle.setSelected(false);
            triangle.setSelected(false);
            pen.setSelected(false); 
            eraser.setSelected(false);
               
            eraserviewer.setVisible(false);
        }        
    }


// SECTION BOUTONS //

    @FXML
    void PenButton(ActionEvent event) {
        Size.setValue(pensize);
        if (pen.isSelected()){
            ToolManager("pen");
            System.out.println("pen selected");
        }else{
            usage = "None";
            System.out.println("no tool selected");
        }
    }

    @FXML
    void EraserButton(ActionEvent event) {
        Size.setValue(erasersize);
        if (eraser.isSelected()){
            ToolManager("eraser");
            System.out.println("eraser selected");
        }else{
            eraserviewer.setVisible(false);
            usage = "None";
            System.out.println("no tool selected");
        }
    }

    @FXML
    void RecButton(ActionEvent event){
        Size.setValue(shapesize);
        if (rectangle.isSelected()){
            System.out.println("rectangle coche");
            ToolManager("rect");
        }else{
            usage = "None";
        } 
    }

    @FXML
    void CircleButton(ActionEvent event){
        Size.setValue(shapesize);
        if (circle.isSelected()){
            System.out.println("cercle coche");
            ToolManager("circle");
        }else{
            usage = "None";
        } 
        
    }
    @FXML
    void TriangleButton(ActionEvent event){
        Size.setValue(shapesize);
        if (triangle.isSelected()){
            System.out.println("triangle coche");
            ToolManager("triangle");
        }else{
            usage = "None";
        }
        
    }

    @FXML
    void LineButton(ActionEvent event){
        Size.setValue(shapesize);
        if (line.isSelected()){
            System.out.println("line coche");
            ToolManager("line");
        }else{
            usage = "None";
        } 
    }

    // Implémenter un bouton effacer toute la page
    @FXML
    void ClearButton(ActionEvent event) {
        gc.clearRect(0, 0, board.getWidth(), board.getHeight());
    }


// SECTION DESSINS //

    @FXML 
    void StartDraw(MouseEvent e){
        if (usage == "pen" & pen.isSelected()) {
            Size.setValue(pensize);
            gc.setFill(colorchoice.getValue());
            if (e.getY() >= 0) {
                gc.fillOval(e.getX() - pensize/2, e.getY()- pensize/2, pensize, pensize);
            } else {
                gc.fillOval(e.getX() - pensize/2, - pensize/2, pensize, pensize);
            }
        }  

        if (usage == "eraser" & eraser.isSelected()) {
            Size.setValue(erasersize);
            gc.setFill(colorchoice.getValue());
            radius = eraserviewer.getRadius();
            Eraserview(e);
            gc.setFill(Color.WHITESMOKE);
            gc.fillOval(e.getX()-radius, e.getY()-radius, radius * 2, radius * 2);  
        }

        if (usage == "line" | usage == "rect" | usage == "circle" | usage == "triangle"){
            Size.setValue(shapesize);
            gc.setFill(colorchoice.getValue());
            ShapeStartX = e.getX();
            ShapeStartY = e.getY();
        }
    }

    @FXML
    void Draw(MouseEvent e) {
        if (usage == "pen" & pen.isSelected()) {
            //gc.setFill(colorchoice.getValue());
            if (e.getY() >= 0) {
                gc.fillOval(e.getX() - pensize/2, e.getY()- pensize/2, pensize, pensize);
            } else {
                gc.fillOval(e.getX() - pensize/2, - pensize/2, pensize, pensize);
            }
        }  

        if (usage == "eraser" & eraser.isSelected()) {
            radius = eraserviewer.getRadius();
            Eraserview(e);
            gc.setFill(Color.WHITESMOKE);
            gc.fillOval(e.getX()-radius, e.getY()-radius, radius * 2, radius * 2);  
        }

        if (usage == "rect" & rectangle.isSelected()) {
            // drawings = board.snapshot(null, null);
            //gc.clearRect(0, 0, board.getWidth(), board.getHeight());
            // gc.drawImage(drawings, 0, 0);
            rectWidth = Math.abs(e.getX() - ShapeStartX);
            rectHeight = Math.abs(e.getY() - ShapeStartY);
            double rectX = Math.min(e.getX(), ShapeStartX);
            double rectY = Math.min(e.getY(), ShapeStartY);
            gc.strokeRect(rectX, rectY, rectWidth, rectHeight);
            

            /*gc.setFill(Color.WHITESMOKE);
		    gc.fillRect(0,0,board.getWidth(),board.getHeight());
            for (int i=0; i<rectangles.size(); i++) {
                Rectangle r = rectangles.get(i);

                //gc.setStroke(colorchoice.getValue());
                //gc.setLineWidth(shapesize);
                
                gc.strokeRect(r.getX(), r.getY(), r.getTranslateX(), r.getTranslateY()); // pas bon
            }
            gc.strokeRect(ShapeStartX,ShapeStartY,e.getX(),e.getY());*/
            
		    
             
		}
  

        if (usage == "circle" & circle.isSelected()) {
            gc.clearRect(0, 0, board.getWidth(), board.getHeight());
            circleWidth = Math.abs(e.getX() - ShapeStartX);
            circleHeight = Math.abs(e.getY() - ShapeStartY);
            double circleX = Math.min(e.getX(), ShapeStartX);
            double circleY = Math.min(e.getY(), ShapeStartY);
            gc.strokeOval(circleX, circleY, circleWidth, circleHeight);
        }    

        if (usage == "triangle" & triangle.isSelected()) {
            gc.clearRect(0, 0, board.getWidth(), board.getHeight());
            triangleWidth = Math.abs(e.getX() - ShapeStartX);
            triangleHeight = Math.abs(e.getY() - ShapeStartY);
            double triangleX = Math.min(e.getX(), ShapeStartX);
            double triangleY = Math.min(e.getY(), ShapeStartY);
            double sideLength = Math.sqrt(Math.pow(e.getX() - triangleX, 2) + Math.pow(e.getY() - triangleY, 2));
            double[] xPoints = {triangleX, e.getX(), triangleX + sideLength};
            double[] yPoints = {triangleY, e.getY(), triangleY + sideLength};
            gc.strokePolygon(xPoints, yPoints, 3);
        }    

        if ((usage == "line" & line.isSelected())){
            //gc.setFill(Color.WHITESMOKE);
		    //gc.fillRect(0,0,board.getWidth(),board.getHeight());
            // ici rajouter peutetre fonction qui redessine tout ce qu'il y avait avant
            for (int i=0; i<lignes.size(); i++) {
                    Line l=lignes.get(i);
                    gc.strokeLine(l.getStartX(),l.getStartY(),l.getEndX(),l.getEndY());
            } 
            gc.strokeLine(ShapeStartX,ShapeStartY,e.getX(),e.getY());
            }
    }


    @FXML
    void endDrawRec(MouseEvent e) {
        if (usage == "line"){
            lignes.add(new Line(ShapeStartX,ShapeStartY,e.getX(),e.getY()));
        }
        if (usage == "rect"){
            rectangles.add(new Rectangle(ShapeStartX,ShapeStartY,e.getX(),e.getY()));
        }
		Draw(e); 
	}
    
// SECTION FICHIER //

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
        gc = board.getGraphicsContext2D();
        //Redessine toutes les lignes créées jusqu'à présent
		    
        /*gc = board.getGraphicsContext2D();
        board.setOnMouseDragged(this::draw);
        board.setStyle("-fx-background-color: black;");
        assert board != null : "fx:id=\"board\" was not injected: check your FXML file 'MainScene.fxml'.";
        assert btnUnZoom != null : "fx:id=\"btnUnZoom\" was not injected: check your FXML file 'MainScene.fxml'.";
        assert btnZoom != null : "fx:id=\"btnZoom\" was not injected: check your FXML file 'MainScene.fxml'.";
*/
    }

}
