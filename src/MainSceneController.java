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
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
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
    private double selectsize = 10; 
    

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    private double zoomFactor = 1.0;

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
    private Button clearBut;

    @FXML
    private ColorPicker colorchoice;

    @FXML
    private ToggleButton eraser;

    @FXML
    private ToggleButton pen;

    @FXML
    private ToggleButton selection;

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

    @FXML
    private Rectangle selectviewer;

    private WritableImage drawings;

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

        if (selection.isSelected()){
            selectsize = Size.getValue();
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
        if (selection.isSelected()){
            selectviewer.setX(e.getX());
            selectviewer.setY(e.getY());
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
            selection.setSelected(false);

            eraserviewer.setVisible(false);
            selectviewer.setVisible(false);

        }
        if (tool == "eraser"){

            pen.setSelected(false); 
            rectangle.setSelected(false);
            circle.setSelected(false);
            triangle.setSelected(false);
            line.setSelected(false);
            selection.setSelected(false);

            eraserviewer.setVisible(true);
            selectviewer.setVisible(false);

        }
        if (tool == "rect"){

            circle.setSelected(false);
            triangle.setSelected(false);
            pen.setSelected(false); 
            eraser.setSelected(false);
            line.setSelected(false);
            selection.setSelected(false);
        
            eraserviewer.setVisible(false);
            selectviewer.setVisible(false);

        }
        if (tool == "circle"){

            rectangle.setSelected(false);
            triangle.setSelected(false);
            pen.setSelected(false); 
            eraser.setSelected(false);
            line.setSelected(false);
            selection.setSelected(false);
            
            eraserviewer.setVisible(false);
            selectviewer.setVisible(false);

        }
        if (tool == "triangle"){

            rectangle.setSelected(false);
            circle.setSelected(false);
            pen.setSelected(false); 
            eraser.setSelected(false);
            line.setSelected(false);
            selection.setSelected(false);
               
            eraserviewer.setVisible(false);
            selectviewer.setVisible(false);
        }
        if (tool == "line"){

            rectangle.setSelected(false);
            circle.setSelected(false);
            triangle.setSelected(false);
            pen.setSelected(false); 
            eraser.setSelected(false);
            selection.setSelected(false);
               
            eraserviewer.setVisible(false);
            selectviewer.setVisible(false);
        }    
        if (tool == "select"){

            rectangle.setSelected(false);
            circle.setSelected(false);
            triangle.setSelected(false);
            pen.setSelected(false); 
            eraser.setSelected(false);
            line.setSelected(false);
            
            eraserviewer.setVisible(false);
            selectviewer.setVisible(false);
        }    
    }


// SECTION BOUTONS //

    @FXML
    void PenButton(ActionEvent event) {
        Size.setValue(pensize);
        if (pen.isSelected()){
            ToolManager("pen");
            //System.out.println("pen selected");
        }else{
            usage = "None";
            //System.out.println("no tool selected");
        }
    }

    @FXML
    void EraserButton(ActionEvent event) {
        Size.setValue(erasersize);
        if (eraser.isSelected()){
            ToolManager("eraser");
            //System.out.println("eraser selected");
        }else{
            eraserviewer.setVisible(false);
            usage = "None";
            //System.out.println("no tool selected");
        }
    }

    @FXML
    void RecButton(ActionEvent event){
        Size.setValue(shapesize);
        if (rectangle.isSelected()){
            //System.out.println("rectangle coche");
            ToolManager("rect");
        }else{
            usage = "None";
        } 
    }

    @FXML
    void CircleButton(ActionEvent event){
        Size.setValue(shapesize);
        if (circle.isSelected()){
            //System.out.println("cercle coche");
            ToolManager("circle");
        }else{
            usage = "None";
        } 
        
    }
    @FXML
    void TriangleButton(ActionEvent event){
        Size.setValue(shapesize);
        if (triangle.isSelected()){
            //System.out.println("triangle coche");
            ToolManager("triangle");
        }else{
            usage = "None";
        }
        
    }

    @FXML
    void LineButton(ActionEvent event){
        Size.setValue(shapesize);
        if (line.isSelected()){
            //System.out.println("line coche");
            ToolManager("line");
        }else{
            usage = "None";
        } 
    }

    @FXML
    void SelectButton(ActionEvent event){
        Size.setValue(selectsize);
        if (selection.isSelected()){
            System.out.println("select coche");
            ToolManager("select");
        }else{
            usage = "None";
        } 
    }

    // Implémentation d'un bouton effacer toute la page
    @FXML
    void ClearButton(ActionEvent event) {
        gc.setFill(Color.WHITESMOKE);
        gc.fillRect(0, 0, board.getWidth(), board.getHeight());
        drawings = board.snapshot(null, null);
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
            radius = eraserviewer.getRadius();
            Eraserview(e);
            gc.fillOval(e.getX()-radius, e.getY()-radius, radius * 2, radius * 2);  
        }

        if (usage == "line" | usage == "rect" | usage == "circle" | usage == "triangle"){
            Size.setValue(shapesize);
            ShapeStartX = e.getX();
            ShapeStartY = e.getY();
        }

        if (usage == "select"){
            Size.setValue(selectsize);
            ShapeStartX = e.getX();
            ShapeStartY = e.getY();
        }

    }

    @FXML
    void Draw(MouseEvent e) {
        if (usage == "pen" & pen.isSelected()) {
            drawings = board.snapshot(null, null);
            // D'apres eytan il faut utiliser le moveto et lineto avec un stroke
            if (e.getY() >= 0) {
                gc.fillOval(e.getX() - pensize/2, e.getY()- pensize/2, pensize, pensize);
            } else {
                gc.fillOval(e.getX() - pensize/2, - pensize/2, pensize, pensize);
            }
        }  

        if (usage == "eraser" & eraser.isSelected()) {
            drawings = board.snapshot(null, null);
            radius = eraserviewer.getRadius();
            Eraserview(e);
            gc.setFill(Color.WHITESMOKE);
            gc.fillOval(e.getX()-radius, e.getY()-radius, radius * 2, radius * 2);  
            
        }

        if (usage == "rect" & rectangle.isSelected()) {
            gc.setFill(Color.WHITESMOKE);
            gc.clearRect(0, 0, board.getWidth(), board.getHeight());
            gc.setStroke(colorchoice.getValue());
            gc.setLineWidth(shapesize);
            rectWidth = Math.abs(e.getX() - ShapeStartX);
            rectHeight = Math.abs(e.getY() - ShapeStartY);
            double rectX = Math.min(e.getX(), ShapeStartX);
            double rectY = Math.min(e.getY(), ShapeStartY);
            gc.drawImage(drawings, 0, 0);
            gc.strokeRect(rectX, rectY, rectWidth, rectHeight);
		}
  

        if (usage == "circle" & circle.isSelected()) {
            gc.setFill(Color.WHITESMOKE);
            gc.clearRect(0, 0, board.getWidth(), board.getHeight());
            gc.setStroke(colorchoice.getValue());
            gc.setLineWidth(shapesize);
            circleWidth = Math.abs(e.getX() - ShapeStartX);
            circleHeight = Math.abs(e.getY() - ShapeStartY);
            double circleX = Math.min(e.getX(), ShapeStartX);
            double circleY = Math.min(e.getY(), ShapeStartY);
            gc.drawImage(drawings, 0, 0);
            gc.strokeOval(circleX, circleY, circleWidth, circleHeight);
        }    

        if (usage == "triangle" && triangle.isSelected()) {
            gc.setFill(Color.WHITESMOKE);
            gc.clearRect(0, 0, board.getWidth(), board.getHeight());
            gc.setStroke(colorchoice.getValue());
            gc.setLineWidth(shapesize);
            triangleWidth = Math.abs(e.getX() - ShapeStartX);
            triangleHeight = Math.abs(e.getY() - ShapeStartY);
            double triangleX = Math.min(e.getX(), ShapeStartX);
            double triangleY = Math.min(e.getY(), ShapeStartY);
            gc.drawImage(drawings, 0, 0);
            // Dans le cas ou on dessine un triangle vers le haut il faut changer l'ordre des points pour piouvoir avoir un triangle dans le bon sens
            if (e.getY() < ShapeStartY) {
                gc.strokePolygon(new double[]{triangleX, triangleX + triangleWidth, triangleX + triangleWidth/2},
                                 new double[]{triangleY, triangleY, triangleY + triangleHeight}, 
                                 3);
            } else {
                gc.strokePolygon(new double[]{triangleX, triangleX + triangleWidth, triangleX + triangleWidth/2},
                                 new double[]{triangleY + triangleHeight, triangleY + triangleHeight, triangleY}, 
                                 3);
            }
        }
        
        if ((usage == "line" & line.isSelected())){
            gc.setFill(Color.WHITESMOKE);
            gc.clearRect(0, 0, board.getWidth(), board.getHeight());
            gc.setStroke(colorchoice.getValue());
            gc.setLineWidth(shapesize);
            gc.drawImage(drawings, 0, 0);
            gc.strokeLine(ShapeStartX,ShapeStartY,e.getX(),e.getY());
            }

        if (usage == "select" & selection.isSelected()) {
            /*gc.setFill(Color.WHITESMOKE);
            gc.clearRect(0, 0, board.getWidth(), board.getHeight());
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(selectsize);
            gc.setLineDashes(30, 30);
           
            rectWidth = Math.abs(e.getX() - ShapeStartX);
            rectHeight = Math.abs(e.getY() - ShapeStartY);
            double rectX = Math.min(e.getX(), ShapeStartX);
            double rectY = Math.min(e.getY(), ShapeStartY);
            gc.drawImage(drawings, 0, 0);
            gc.strokeRect(rectX, rectY, rectWidth, rectHeight);
            gc.setLineDashes(0, 0);*/
            board.setCursor(Cursor.NONE);
            selectviewer.setVisible(true);
            selectviewer.setWidth(e.getX()-selectviewer.getX());
            selectviewer.setHeight(e.getY()-selectviewer.getY());
        }
    }


    @FXML
    void endDrawRec(MouseEvent e) {
        if (usage == "select"){
            selectviewer.setVisible(false);
            board.setCursor(Cursor.DEFAULT);
        }else{/*if (usage == "line"){
            // lignes.add(new Line(ShapeStartX,ShapeStartY,e.getX(),e.getY()));
            drawings = board.snapshot(null, null);
        }
        if (usage == "rect"){
            // rectangles.add(new Rectangle(ShapeStartX,ShapeStartY,e.getX(),e.getY()));
            drawings = board.snapshot(null, null);
        }
        if (usage == "circle"){
            drawings = board.snapshot(null, null);
        }
        if (usage == "triangle"){
            drawings = board.snapshot(null, null);
        }*/
            drawings = board.snapshot(null, null);
		    Draw(e);
        } 
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
            // Charger l'image à partir du fichier
            Image image = new Image(file.toURI().toString());

            // Dessiner l'image sur le Canvas
            GraphicsContext gc = board.getGraphicsContext2D();
            gc.drawImage(image, 0, 0);

            // Stocker l'image dans l'attribut drawings
            drawings = board.snapshot(null, null);
        }
    }

    @FXML
    private void ZoomIn() {
        zoomFactor *= 1.1;
        board.setScaleX(zoomFactor);
        board.setScaleY(zoomFactor);
    }

    @FXML
    private void ZoomOut() {
        zoomFactor /= 1.1;
        board.setScaleX(zoomFactor);
        board.setScaleY(zoomFactor);
    }

    @FXML
    void initialize() {
        drawings = new WritableImage((int) board.getWidth(), (int) board.getHeight());
        gc = board.getGraphicsContext2D();
        gc.setFill(Color.WHITESMOKE);
        gc.fillRect(0, 0, board.getWidth(), board.getHeight());
    }

}