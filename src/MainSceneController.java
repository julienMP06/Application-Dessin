import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import javax.xml.crypto.dsig.Transform;

import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.scene.transform.Translate;



public class MainSceneController {

    GraphicsContext gc;
    private double radius;

    // default settings 
    private String usage = "None"; 
    private double erasersize = 15; 
    private double pensize = 10; 
    private double shapesize = 5;
    
    @FXML
    private Label ZoomLabel;

    @FXML
    private ResourceBundle resources;

    @FXML
    private Label statutLabel;

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
    private Button btnZoom;

    @FXML
    private MenuItem SaveButton;

    @FXML
    private MenuItem openButton;

    @FXML
    private Button clearBut;

    @FXML
    private HBox MenusTools;

 // boutons et autres outils //

    @FXML
    private ToggleButton eraser;

    @FXML
    private ToggleButton pen;

    @FXML
    private ToggleButton selection;
    
    @FXML
    private Slider size;

    @FXML
    private ColorPicker colorchoice;

// formes disponibles //    

    @FXML
    private CheckMenuItem rectangle;

    @FXML
    private CheckMenuItem circle;

    @FXML
    private CheckMenuItem triangle;

    @FXML
    private CheckMenuItem line;    

// outils guidés //   

    @FXML
    private Circle eraserviewer;

    @FXML
    private Rectangle selectviewer;

// forme séléctionnée //

    @FXML
    private Rectangle selectrectangle;
    
    @FXML
    private Ellipse selectcircle;


// options pour la séléction //

    @FXML
    private MenuButton options;

    @FXML 
    private ColorPicker colorselector;

    @FXML 
    private CheckMenuItem eraseselected;

    @FXML
    private CheckMenuItem sizeselected;

    @FXML
    private CheckMenuItem colorselected;

    @FXML
    private CheckMenuItem moveselected;

    @FXML
    private CheckMenuItem fillselected;

    Circle circleshape = new Circle();
    Rectangle rectshape = new Rectangle();
    Rectangle rectobject = new Rectangle();
    //Point2D point = new Point2D();
    Line lineshape = new Line();
        
        
    private WritableImage drawings;

    // Variables pour la création de formes
    private double ShapeStartX, ShapeStartY;

    private double rectX, rectY;
    private double rectWidth, rectHeight;
    private double circleWidth, circleHeight;
    private double triangleWidth, triangleHeight;

    // pour la séléction
    double p1X, p1Y, p2X, p2Y;


    // pour la redimension
    @FXML
    Circle p1, p2, p3, p4;

    private List<Circle> pointsforsize = new ArrayList<>();
    private List<Object> allTOOLS = new ArrayList<>();
    private List<Object> dessins = new ArrayList<>();

    @FXML 
    void Changesize(MouseEvent e){
        if (eraser.isSelected()){
            System.out.println("erasersize changing");
            erasersize = size.getValue();
            eraserviewer.setRadius(erasersize);
        }
        if (pen.isSelected()){
            System.out.println("pensize changing");
            pensize = size.getValue();
        }
        if (rectangle.isSelected() | circle.isSelected() | triangle.isSelected() | line.isSelected()){
            System.out.println("shapesize changing");
            shapesize = size.getValue();
        }
    }

    @FXML 
    void Toolview(MouseEvent e){
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
            selectrectangle.setVisible(false);

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
            selectrectangle.setVisible(false);

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
            selectrectangle.setVisible(false);

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
            selectrectangle.setVisible(false);

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
            selectrectangle.setVisible(false);
            
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
            selectrectangle.setVisible(false);

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
            selectrectangle.setVisible(false);

        }  
    }


// SECTION BOUTONS //

    @FXML
    void PenButton(ActionEvent event) {
        size.setValue(pensize);
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
        size.setValue(erasersize);
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
        size.setValue(shapesize);
        if (rectangle.isSelected()){
            //System.out.println("rectangle coche");
            ToolManager("rect");
        }else{
            usage = "None";
        } 
    }

    @FXML
    void CircleButton(ActionEvent event){
        size.setValue(shapesize);
        if (circle.isSelected()){
            //System.out.println("cercle coche");
            ToolManager("circle");
        }else{
            usage = "None";
        } 
        
    }
    @FXML
    void TriangleButton(ActionEvent event){
        size.setValue(shapesize);
        if (triangle.isSelected()){
            //System.out.println("triangle coche");
            ToolManager("triangle");
        }else{
            usage = "None";
        }
        
    }

    @FXML
    void LineButton(ActionEvent event){
        size.setValue(shapesize);
        if (line.isSelected()){
            //System.out.println("line coche");
            ToolManager("line");
        }else{
            usage = "None";
        } 
    }

    @FXML
    void SelectButton(ActionEvent event){
        if (selection.isSelected()){
            //System.out.println("select coche");
            ToolManager("select");
        }else{
            for (Circle p : pointsforsize) {
                p.setVisible(false);
            }
            sizeselected.setSelected(false);
            selectrectangle.setVisible(false);
            usage = "None";
        } 
    }

    // Implémentation d'un bouton effacer toute la page
    @FXML
    void ClearButton(ActionEvent event) {
        dessins.clear();
        //add : remove all selections options and selectionviewer
        gc.setFill(Color.WHITESMOKE);
        gc.fillRect(0, 0, board.getWidth(), board.getHeight());
        drawings = board.snapshot(null, null);
    }

// SECTION DESSINS //

    @FXML 
    void StartDraw(MouseEvent e){
        if (usage == "pen" & pen.isSelected()) {
            size.setValue(pensize);
            gc.setFill(colorchoice.getValue());
            if (e.getY() >= 0) {
                gc.fillOval(e.getX() - pensize/2, e.getY()- pensize/2, pensize, pensize);
            } else {
                gc.fillOval(e.getX() - pensize/2, - pensize/2, pensize, pensize);
            }
        }  

        if (usage == "eraser" & eraser.isSelected()) {
            size.setValue(erasersize);
            radius = eraserviewer.getRadius();
            Toolview(e);
            gc.fillOval(e.getX()-radius, e.getY()-radius, radius * 2, radius * 2);  
        }

        if (usage == "line" | usage == "rect" | usage == "circle" | usage == "triangle"){
            size.setValue(shapesize);
            ShapeStartX = e.getX();
            ShapeStartY = e.getY();
        }

        if (usage == "select"){
            selectviewer.getStrokeDashArray().addAll(40d, 40d);
            ShapeStartX = e.getX();
            ShapeStartY = e.getY();
        }
    }

    @FXML
    void Draw(MouseEvent e) {
        if (usage == "pen" & pen.isSelected()) {
            drawings = board.snapshot(null, null);
            if (e.getY() >= 0) {
                gc.fillOval(e.getX() - pensize/2, e.getY()- pensize/2, pensize, pensize);
            } else {
                gc.fillOval(e.getX() - pensize/2, - pensize/2, pensize, pensize);
            }
        }  

        if (usage == "eraser" & eraser.isSelected()) {
            drawings = board.snapshot(null, null);
            radius = eraserviewer.getRadius();
            Toolview(e);
            gc.setFill(Color.WHITESMOKE);
            gc.fillOval(e.getX()-radius, e.getY()-radius, radius * 2, radius * 2);  
            
        }

        if (usage == "rect" & rectangle.isSelected()) {
            gc.setFill(Color.WHITESMOKE);
            gc.fillRect(0, 0, board.getWidth(), board.getHeight());
            gc.setStroke(colorchoice.getValue());
            gc.setLineWidth(shapesize);
            rectWidth = Math.abs(e.getX() - ShapeStartX);
            rectHeight = Math.abs(e.getY() - ShapeStartY);
            rectX = Math.min(e.getX(), ShapeStartX);
            rectY = Math.min(e.getY(), ShapeStartY);
            gc.drawImage(drawings, 0, 0);
            gc.strokeRect(rectX, rectY, rectWidth, rectHeight);
            
            
		}
  
        if (usage == "circle" & circle.isSelected()) {
            gc.setFill(Color.WHITESMOKE);
            gc.fillRect(0, 0, board.getWidth(), board.getHeight());
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
            gc.fillRect(0, 0, board.getWidth(), board.getHeight());
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
            gc.fillRect(0, 0, board.getWidth(), board.getHeight());
            gc.setStroke(colorchoice.getValue());
            gc.setLineWidth(shapesize);
            gc.drawImage(drawings, 0, 0);
            gc.strokeLine(ShapeStartX,ShapeStartY,e.getX(),e.getY());
            //lines.add()
            }

        if (usage == "select" & selection.isSelected()) {
            board.setCursor(Cursor.NONE);
            selectviewer.setVisible(true);

            //gère le cas où on utilisait la redimension juste avant
            for (Circle p : pointsforsize) {
                p.setVisible(false);
            }
            sizeselected.setSelected(false);

            selectviewer.setWidth(e.getX()-selectviewer.getX());
            selectviewer.setHeight(e.getY()-selectviewer.getY());
        }
    }


    @FXML
    void endDrawRec(MouseEvent e) {
        if (usage == "select"){
            selectviewer.setVisible(false);
            board.setCursor(Cursor.DEFAULT);
            ShapeSelector();
        }else{
            if (usage == "line"){
                Line line = new Line(ShapeStartX,ShapeStartY,e.getX(),e.getY());
                line.setStrokeWidth(shapesize);
                line.setStroke(colorchoice.getValue());
                dessins.add(line);
            }
            if (usage == "rect"){
                Rectangle rect = new Rectangle(rectX, rectY, rectWidth, rectHeight);
                rect.setStrokeWidth(shapesize);
                rect.setStroke(colorchoice.getValue());
                rect.setFill(Color.TRANSPARENT);
                dessins.add(rect);
        
            }
            if (usage == "circle"){
        
            }
            if (usage == "triangle"){
                
            }
            drawings = board.snapshot(null, null);
		    Draw(e);
            System.out.println(dessins);
            
        } 
	}

    void ShapeSelector(){
        String shape = "None";
        for (int i = dessins.size()-1; i > -1; i--){      
            Object objet = dessins.get(i);

            if (objet instanceof Rectangle) {
                rectshape = (Rectangle) objet;
                shape = "rect";
                p1X = rectshape.getX() - rectshape.getStrokeWidth()/2 + 3;
                p2X = rectshape.getX() + rectshape.getWidth() + rectshape.getStrokeWidth()/2 - 3;
                p1Y = rectshape.getY() - rectshape.getStrokeWidth()/2 + 3;
                p2Y = rectshape.getY() + rectshape.getHeight() + rectshape.getStrokeWidth()/2 - 3;

            }else if (objet instanceof Circle) {
                shape = "circle";
                circleshape = (Circle) objet;
                p1X = circleshape.getCenterX();
                p1Y = circleshape.getCenterY();
                radius = circleshape.getRadius();
                
            }else if (objet instanceof Line) {
                shape = "line";
                lineshape = (Line) objet;
                p1X = lineshape.getStartX();
                p1Y = lineshape.getStartY();
                p2X = lineshape.getEndX();
                p2Y = lineshape.getEndY();
                
            }else if (objet instanceof Point2D) {
                shape = "point";
                Point2D point = (Point2D) objet;
                

            } /*else if (objet instanceof Triangle) {
                Triangle triangle = (Triangle) objet;
                double centerX = (triangle.getPoint1().getX() + triangle.getPoint2().getX() + triangle.getPoint3().getX()) / 3;
                double centerY = (triangle.getPoint1().getY() + triangle.getPoint2().getY() + triangle.getPoint3().getY()) / 3;
                // Utiliser les coordonnées du centre du triangle*/
            else{
                System.out.println("no shapes drew");
            }

            if ((selectviewer.contains(p1X, p1Y) & selectviewer.contains(p2X, p2Y))){
                ShapeViewer(shape);
                options.setLayoutX(p2X + 15);
                options.setLayoutY(p2Y - 15);
                options.show();
                //System.out.println("selected succeed");
                
                break;
                
            }else{
                System.out.println("no shapes in the area");
            }
        }
    }

    void ShapeViewer(String s){
        if (s == "rect"){
            selectrectangle.setX(p1X);
            selectrectangle.setY(p1Y);
            selectrectangle.setWidth(p2X-p1X);
            selectrectangle.setHeight(p2Y-p1Y);
            selectrectangle.getStrokeDashArray().addAll(5d, 40d);
            selectrectangle.setVisible(true);
        }else if (s == "circle"){
            selectcircle.setCenterX(10);
        }else if (s == ""){
            
        }

    }

    @FXML
    void SelectOptions(ActionEvent e){
        if (eraseselected.isSelected()){
            //System.out.println("shape selected erased");
            dessins.remove(rectshape);
            redraw();

            eraseselected.setSelected(false);
            selectrectangle.setVisible(false);
        }

        if (sizeselected.isSelected()){
            sizeviewerwithpoints(rectshape);
            
            selectrectangle.setVisible(false);
        
        }

        if (colorselected.isSelected() | fillselected.isSelected()){
            colorselector.setLayoutX(rectshape.getX() + rectshape.getWidth() + 15);
            colorselector.setLayoutY(rectshape.getY() + rectshape.getHeight() - 15);
            colorselector.show();
            selectrectangle.setVisible(false);
        }

        if (moveselected.isSelected()){
            selectrectangle.setVisible(true);
        }
        /*if (duplicate.isSelected()){

        }*/
        
        
           
    }

    void sizeviewerwithpoints(Object object){

        if (object instanceof Rectangle){

            p1.setVisible(true);
            p2.setVisible(true);
            p3.setVisible(true);
            
            p4.setVisible(true);
            //p5.setVisible(true);

            //p6.setVisible(true);
            //p7.setVisible(true);
            //p8.setVisible(true);

            relocatedPoints();

        }
        
    }

    void relocatedPoints(){
        //par niveau du rectangle (haut, centre, bas)

        //haut
        p1.setCenterX(rectshape.getX());
        p1.setCenterY(rectshape.getY());

        //p2.setCenterX(rectshape.getX()+rectshape.getWidth()/2);
        //p2.setCenterY(rectshape.getY());

        p2.setCenterX(rectshape.getX()+rectshape.getWidth());
        p2.setCenterY(rectshape.getY());

        /*centre
        p4.setCenterX(rectshape.getX());
        p4.setCenterY(rectshape.getY() + rectshape.getHeight()/2);

        p5.setCenterX(rectshape.getX()+rectshape.getWidth());
        p5.setCenterY(rectshape.getY() + rectshape.getHeight()/2);*/

        //bas
        p3.setCenterX(rectshape.getX());
        p3.setCenterY(rectshape.getY() + rectshape.getHeight());

        //p7.setCenterX(rectshape.getX()+rectshape.getWidth()/2);
        //p7.setCenterY(rectshape.getY() + rectshape.getHeight());

        p4.setCenterX(rectshape.getX()+rectshape.getWidth());
        p4.setCenterY(rectshape.getY() + rectshape.getHeight());
    }

    

    void redraw(){
        gc.setFill(Color.WHITESMOKE);
        gc.fillRect(0, 0, board.getWidth(), board.getHeight());
        for(int i = 0; i < dessins.size(); i++){
            Object objet = dessins.get(i);

            if (objet instanceof Rectangle) {
                rectobject = (Rectangle) objet;
                gc.setLineWidth(rectobject.getStrokeWidth());
                gc.setStroke(rectobject.getStroke());
                gc.setFill(rectobject.getFill());
                gc.fillRect(rectobject.getX()+rectobject.getStrokeWidth()/2, rectobject.getY()+rectobject.getStrokeWidth()/2, rectobject.getWidth()-rectobject.getStrokeWidth(), rectobject.getHeight()-rectobject.getStrokeWidth());
                gc.strokeRect(rectobject.getX(), rectobject.getY(), rectobject.getWidth(), rectobject.getHeight());
            }
        }
        drawings = board.snapshot(null, null);

    }


    @FXML
    void PointSelectorEnter(MouseEvent e){
        if (e.getSource() instanceof Circle){
            Circle p = (Circle) e.getSource();
            p.setStroke(Color.BLUE);
            p.setRadius(p.getRadius()+3);
        }
    }
    
    @FXML
    void PointSelectorExit(MouseEvent e){
        if (e.getSource() instanceof Circle){
            Circle p = (Circle) e.getSource();
            p.setStroke(Color.BLACK);
            p.setRadius(p.getRadius()-3);
        }
    }

    @FXML 
    void SizeSelector(MouseEvent e){
        double X = 0;
        double Y = 0;
        if (e.getSource() instanceof Circle){
            Circle p = (Circle) e.getSource();
            ShowPointsSelector(p, false);
            rectX = rectshape.getX();
            rectY = rectshape.getY();

            if (p == p1){ //point haut gauche

                X = e.getX();
                Y = e.getY();

                rectshape.setX(e.getX());
                rectshape.setY(e.getY());

                rectshape.setWidth(rectshape.getWidth() - e.getX() + rectX); 
                rectshape.setHeight(rectshape.getHeight() - e.getY() + rectY); 

                p.setCenterX(e.getX());
                p.setCenterY(e.getY());

                

            }else if (p == p2){ //point haut droit

                X = rectX;
                Y = e.getY();

                rectshape.setY(e.getY());

                rectshape.setWidth(e.getX() - rectX); 
                rectshape.setHeight(rectshape.getHeight() - e.getY() + rectY); 

                p.setCenterX(e.getX());
                p.setCenterY(e.getY());

                
            }else if (p == p3){ //point bas gauche

                X = rectX;
                Y = rectY;

                rectshape.setX(e.getX());
                

                rectshape.setWidth(rectshape.getWidth() - e.getX() + rectX); 
                rectshape.setHeight(e.getY() - rectY); 

                p.setCenterX(e.getX());
                p.setCenterY(e.getY());

            }else if (p == p4){ //point bas droit

                X = rectX;
                Y = rectY;
            
                rectshape.setWidth(e.getX() - rectX); 
                rectshape.setHeight(e.getY() - rectY); 

                p.setCenterX(e.getX());
                p.setCenterY(e.getY());
            }

            gc.setFill(Color.WHITESMOKE);
            gc.fillRect(0, 0, board.getWidth(), board.getHeight());

            gc.drawImage(drawings, 0, 0);
            gc.setFill(rectshape.getFill());
            gc.setStroke(rectshape.getStroke());
            gc.strokeRect(X, Y, rectshape.getWidth(), rectshape.getHeight());
            gc.fillRect(X+rectshape.getStrokeWidth()/2, Y+rectshape.getStrokeWidth()/2, rectshape.getWidth()-rectshape.getStrokeWidth(), rectshape.getHeight()-rectshape.getStrokeWidth());

        }else{

        }
    }

    @FXML
    void BeginResize(){
        System.out.println("begin");
        dessins.remove(rectshape);
        redraw();
    }

    @FXML
    void SizeSelected(MouseEvent e){
        if (e.getSource() instanceof Circle){
            Circle p = (Circle) e.getSource();
            dessins.add(rectshape);
            redraw();
            relocatedPoints();
            ShowPointsSelector(p, true);
            sizeselected.setSelected(false);
        }
    }

    void ShowPointsSelector(Circle p, Boolean b){
        if (b == false){
            for (Circle point : pointsforsize) {
                if (point != p){
                    point.setVisible(false);
                }
            }
        }else{
            for (Circle point : pointsforsize) {
                point.setVisible(true); 
            }
        }
    } 

    @FXML
    void ColorSelector(){

        if (colorselected.isSelected()){

        rectshape.setStroke(colorselector.getValue());
        gc.setLineWidth(rectshape.getStrokeWidth());
        gc.setStroke(colorselector.getValue());
        gc.strokeRect(rectshape.getX(), rectshape.getY(), rectshape.getWidth(), rectshape.getHeight());
        colorselected.setSelected(false);
        redraw();
        }


        if (fillselected.isSelected()){

        rectshape.setFill(colorselector.getValue());
        gc.setFill(colorselector.getValue());
        gc.fillRect(rectshape.getX()+rectshape.getStrokeWidth()/2, rectshape.getY()+rectshape.getStrokeWidth()/2, rectshape.getWidth()-rectshape.getStrokeWidth(), rectshape.getHeight()-rectshape.getStrokeWidth());
        fillselected.setSelected(false);
        redraw();

        }
        drawings = board.snapshot(null, null);
        
    }

    private double startX = 0;
    private double startY = 0;

    @FXML
    void StartMoveSelector(MouseEvent e){
        dessins.remove(rectshape);
        redraw();
        startX = e.getX();
        startY = e.getY();
    }

    @FXML
    void MoveSelector(MouseEvent e){
        if (moveselected.isSelected()){
            if (e.getSource() instanceof Rectangle){  //selectedrectangle
                Rectangle r = (Rectangle) e.getSource();
                double offsetX = e.getX() - startX;
                double offsetY = e.getY() - startY;
                r.setX(r.getX() + offsetX);
                r.setY(r.getY() + offsetY);
    
                gc.setLineWidth(rectshape.getStrokeWidth());
                gc.setStroke(rectshape.getStroke());
                gc.drawImage(drawings, 0, 0);
                gc.setFill(rectshape.getFill());
                gc.fillRect(r.getX()+rectshape.getStrokeWidth()/2, r.getY()+rectshape.getStrokeWidth()/2, 
                rectshape.getWidth()-rectshape.getStrokeWidth(), rectshape.getHeight()-rectshape.getStrokeWidth());
                gc.strokeRect(r.getX(), r.getY(), rectshape.getWidth(), rectshape.getHeight());
    
                startX = e.getX();
                startY = e.getY();
            }
        }
    }
    

    @FXML
    void EndMoveSelector(MouseEvent e){
        selectrectangle.setVisible(false);
        moveselected.setSelected(false);
        Rectangle rectmoved = new Rectangle(selectrectangle.getX(), selectrectangle.getY(), rectshape.getWidth(), rectshape.getHeight());
        rectmoved.setStrokeWidth(rectshape.getStrokeWidth());
        rectmoved.setStroke(rectshape.getStroke());
        rectmoved.setFill(rectshape.getFill());
        dessins.add(rectmoved);
        redraw();
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
    private void updateZoomValueLabel() {
        ZoomLabel.setText(String.format("%.0f%%", (zoomFactor*100)));
    }

    @FXML
    private void updateStatuLabel() {
        statutLabel.setText(usage);    
    }

    @FXML
    private void ZoomIn(){
        zoomFactor *= 1.1;
        board.setScaleX(zoomFactor);
        board.setScaleY(zoomFactor);
        updateZoomValueLabel();
    }

    @FXML
    private void ZoomOut(){
        if (zoomFactor*100 <= 100){
            System.out.println("Impossible");
        } else {
            zoomFactor /= 1.1;
            board.setScaleX(zoomFactor);
            board.setScaleY(zoomFactor);
            updateZoomValueLabel();
        }
    }    

    @FXML
    void initialize() {
        drawings = new WritableImage((int) board.getWidth(), (int) board.getHeight());

        //allTOOLS.add();
        pointsforsize.add(p1);pointsforsize.add(p2);pointsforsize.add(p3);
        pointsforsize.add(p4);
        gc = board.getGraphicsContext2D();
        gc.setFill(Color.WHITESMOKE);
        gc.fillRect(0, 0, board.getWidth(), board.getHeight());
        
    }

}