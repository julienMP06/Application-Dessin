import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.spec.EllipticCurve;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import javax.xml.crypto.dsig.Transform;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.transform.Translate;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
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
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.FileChooser;


public class MainSceneController {

    GraphicsContext gc;

    // default settings 
    private String usage = "None"; 
    private String usageselected = "None";
    private String shape = "None";
    private double erasersize = 15; 
    private double pensize = 10; 
    private double shapesize = 5;
    
    

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label ZoomLabel;

    @FXML
    private Label statutLabel;

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

    
    Rectangle rectshape = new Rectangle();
    Line lineshape = new Line();
    Ellipse circleshape = new Ellipse();

    Rectangle rectobject = new Rectangle();
    Line lineobject = new Line();
    Ellipse circleobject = new Ellipse();
    //Point2D point = new Point2D();
    
        
        
    private WritableImage drawings;
    private double radiusEraser;

    // Variables pour la création de formes
    private double ShapeStartX, ShapeStartY;

    private double radiusX, radiusY; 
    



    private double rectWidth, rectHeight;
    private double circleWidth, circleHeight;
    private double triangleWidth, triangleHeight;

    private double rectX, rectY;
    private double circleX, circleY;

    private double p1X, p1Y, p2X, p2Y;
    
    private double lineX, lineY;
    private double lineEndX, lineEndY;


    // pour la redimension
    @FXML
    Circle p1, p2, p3, p4;

    // deplacement
    private double startX = 0;
    private double startY = 0;


    double X;
    double Y;

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
            CancelSelectOption();
            ToolManager("pen");
            updateStatuLabel();
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
            CancelSelectOption();
            ToolManager("eraser");
            updateStatuLabel();
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
            CancelSelectOption();
            ToolManager("rect");
            updateStatuLabel();
        }else{
            usage = "None";
        } 
    }

    @FXML
    void CircleButton(ActionEvent event){
        size.setValue(shapesize);
        if (circle.isSelected()){
            //System.out.println("cercle coche");
            CancelSelectOption();
            ToolManager("circle");
            updateStatuLabel();
        }else{
            usage = "None";
        } 
        
    }
    @FXML
    void TriangleButton(ActionEvent event){
        size.setValue(shapesize);
        if (triangle.isSelected()){
            //System.out.println("triangle coche");
            CancelSelectOption();
            ToolManager("triangle");
            updateStatuLabel();
        }else{
            usage = "None";
        }
        
    }

    @FXML
    void LineButton(ActionEvent event){
        size.setValue(shapesize);
        if (line.isSelected()){
            //System.out.println("line coche");
            CancelSelectOption();
            ToolManager("line");
            updateStatuLabel();
        }else{
            usage = "None";
        } 
    }

    @FXML
    void SelectButton(ActionEvent event){
        if (selection.isSelected()){
            //System.out.println("select coche");
            ToolManager("select");
            updateStatuLabel();
        }else{
            CancelSelectOption();
            sizeselected.setSelected(false);
            selectrectangle.setVisible(false);
            usage = "None";
        } 
    }

    // Implémentation d'un bouton effacer toute la page
    @FXML
    void ClearButton(ActionEvent event) {
        dessins.clear();

        CancelSelectOption();

        //add : remove all selections options and selectionviewer
        gc.setFill(Color.WHITESMOKE);
        gc.fillRect(0, 0, board.getWidth(), board.getHeight());
        drawings = board.snapshot(null, null);
        //au cas où
        usageselected = "None";
    }

    


// SECTION DESSINS //

    @FXML 
    void StartDraw(MouseEvent e){
        if (usage == "pen") {
            size.setValue(pensize);
            gc.setFill(colorchoice.getValue());
            if (e.getY() >= 0) {
                gc.fillOval(e.getX() - pensize/2, e.getY()- pensize/2, pensize, pensize);
            } else {
                gc.fillOval(e.getX() - pensize/2, - pensize/2, pensize, pensize);
            }
        }  

        if (usage == "eraser" ) {
            size.setValue(erasersize);
            radiusEraser = eraserviewer.getRadius();
            Toolview(e);
            gc.fillOval(e.getX()-radiusEraser, e.getY()-radiusEraser, radiusEraser * 2, radiusEraser * 2);  
        }

        if (usage == "line" | usage == "rect" | usage == "circle" | usage == "triangle"){
            size.setValue(shapesize);
            ShapeStartX = e.getX();
            ShapeStartY = e.getY();
        }

        if (usage == "select"){
            ShapeStartX = e.getX();
            ShapeStartY = e.getY();
            selectrectangle.setVisible(false);
            
        }

    }

    @FXML
    void CancelSelectOption(){
        if (usage == "select"){
            if (usageselected == "None"){
                usage = "None";
                selection.setSelected(false);
                selectrectangle.setVisible(false);
                selectcircle.setVisible(false);
                /*for (Node option : options.getChildrenUnmodifiable()) {
                    option.setVisible(false);
                }*/
                
            }else{
                if (usageselected == "size"){
                    for (Circle p : pointsforsize) {
                        p.setVisible(false);
                    }
                    sizeselected.setSelected(false);
                    System.out.println("canceled");

                }
                if (usageselected == "color"){
                    selectrectangle.setVisible(false);
                    selectcircle.setVisible(false);
                    colorselected.setSelected(false);
                    fillselected.setSelected(false);
                }
                if (usageselected == "move"){
                    selectrectangle.setVisible(false);
                    selectcircle.setVisible(false);
                    moveselected.setSelected(false);
                }
            }
            // le menu des options doit se fermer
        }
    }

    @FXML
    void Draw(MouseEvent e) {
        if (usage == "pen" & pen.isSelected()) {
            if (e.getY() >= 0) {
                gc.fillOval(e.getX() - pensize/2, e.getY()- pensize/2, pensize, pensize);
            } else {
                gc.fillOval(e.getX() - pensize/2, - pensize/2, pensize, pensize);
            }
        }  

        if (usage == "eraser" & eraser.isSelected()) {
            radiusEraser = eraserviewer.getRadius();
            Toolview(e);
            gc.setFill(Color.WHITESMOKE);
            gc.fillOval(e.getX()-radiusEraser, e.getY()-radiusEraser, radiusEraser * 2, radiusEraser * 2);  
            
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
            circleX = Math.min(e.getX(), ShapeStartX);
            circleY = Math.min(e.getY(), ShapeStartY);

            gc.drawImage(drawings, 0, 0);
                        //upper bound left  ///diameters 
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

            //lineX = Math.min(e.getX(), ShapeStartX);
            //lineY = Math.min(e.getY(), ShapeStartY);


            lineX = e.getX();
            lineY = e.getY();
            

            gc.strokeLine(ShapeStartX, ShapeStartY, lineX, lineY);
        }

        if (usage == "select" & selection.isSelected()) {
            selectviewer.setVisible(true);

            //gère le cas où on utilisait la redimension juste avant
            for (Circle p : pointsforsize) {
                p.setVisible(false);
            }


            sizeselected.setSelected(false);
            moveselected.setSelected(false);
            fillselected.setSelected(false);
            colorselected.setSelected(false);


            selectviewer.setX(ShapeStartX);
            selectviewer.setY(ShapeStartY);

            selectviewer.setWidth(e.getX()-selectviewer.getX());
            selectviewer.setHeight(e.getY()-selectviewer.getY());
        }
    }


    @FXML
    void endDrawRec(MouseEvent e) {
        if (usage == "select"){
            selectviewer.setVisible(false);
            //selectviewer.setDisable(true);
            board.setCursor(Cursor.DEFAULT);
            ShapeSelector();
        }else{
            if (usage == "line"){
                //lineX = Math.min(lineX, ShapeStartX);
                //lineY = Math.min(lineY, ShapeStartY);
                //lineEndX = Math.max(lineX, ShapeStartX);
                //lineEndY = Math.max(lineY, ShapeStartY);
                Line line = new Line(ShapeStartX, ShapeStartY, lineX, lineY);
                line.setStrokeWidth(shapesize);
                line.setStroke(colorchoice.getValue());
                dessins.add(line);
                redraw();
            }
            if (usage == "rect"){
                Rectangle rect = new Rectangle(rectX, rectY, rectWidth, rectHeight);
                rect.setStrokeWidth(shapesize);
                rect.setStroke(colorchoice.getValue());
                rect.setFill(Color.TRANSPARENT);
                dessins.add(rect);
                redraw();
        
            }
            if (usage == "circle"){
                Ellipse circle = new Ellipse(circleX+circleWidth/2, circleY+circleHeight/2, circleWidth/2, circleHeight/2);
                circle.setStroke(colorchoice.getValue());
                circle.setStrokeWidth(shapesize);
                circle.setFill(Color.TRANSPARENT);
                dessins.add(circle);
                redraw();
            }
            if (usage == "triangle"){
                redraw();
            }
            
            System.out.println(dessins);
            
            
        } 
         //pour l'instant pen n'est pas enregistré
	}

    void ShapeSelector(){
        for (int i = dessins.size()-1; i > -1; i--){      
            Object objet = dessins.get(i);

            if (objet instanceof Rectangle) {
                rectshape = (Rectangle) objet;
                shape = "rect";
                p1X = rectshape.getX() - rectshape.getStrokeWidth()/2;
                p1Y = rectshape.getY() - rectshape.getStrokeWidth()/2;

                p2X = rectshape.getX() + rectshape.getWidth() + rectshape.getStrokeWidth()/2;
                p2Y = rectshape.getY() + rectshape.getHeight() + rectshape.getStrokeWidth()/2;

            }else if (objet instanceof Ellipse) {
                shape = "circle";
                circleshape = (Ellipse) objet;
                p1X = circleshape.getCenterX();
                p1Y = circleshape.getCenterY();
                
                radiusX = circleshape.getRadiusX() + circleshape.getStrokeWidth()/2;
                radiusY = circleshape.getRadiusY() + circleshape.getStrokeWidth()/2;

                
            }else if (objet instanceof Line) {
                shape = "line";
                lineshape = (Line) objet;
                p1X = lineshape.getStartX() - lineshape.getStrokeWidth()/2;
                p1Y = lineshape.getStartY() - lineshape.getStrokeWidth()/2;

                p2X = lineshape.getEndX() + lineshape.getStrokeWidth()/2;
                p2Y = lineshape.getEndY() + lineshape.getStrokeWidth()/2;
                
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

            if (shape == "rect" | shape == "line"){
                if ((selectviewer.contains(p1X, p1Y) & selectviewer.contains(p2X, p2Y))){
                    ShapeViewer();
                    options.setLayoutX(p2X + 15);
                    options.setLayoutY(p2Y - 115);
                    options.show();
                    
                    
                    break;
                    
                }
            }
            
            if (shape == "circle"){
                if ((selectviewer.contains(p1X - radiusX, p1Y - radiusY) & selectviewer.contains(p1X + radiusX, p1Y + radiusY))){
                //if (selectviewer.contains(p1X, p1Y)){    
                    ShapeViewer();
                    options.setLayoutX(p1X + radiusX + 15);
                    options.setLayoutY(p1Y - 115);
                    options.show();
                    System.out.println("selected succeed");
                    break;
                }
            }
        }

    
    }


    void ShapeViewer(){
        if (shape == "rect"){
            selectrectangle.setX(p1X);
            selectrectangle.setY(p1Y);
            selectrectangle.setWidth(p2X-p1X);
            selectrectangle.setHeight(p2Y-p1Y);
            selectrectangle.getStrokeDashArray().addAll(20d, 40d);
            selectrectangle.setVisible(true);
        }else if (shape == "circle"){
            selectcircle.setCenterX(p1X);
            selectcircle.setCenterY(p1Y);
            selectcircle.setRadiusX(radiusX);
            selectcircle.setRadiusY(radiusY);
            selectcircle.getStrokeDashArray().addAll(5d, 40d);
            selectcircle.setVisible(true);
        }else if (shape == "line"){
            //selectline.getPoints().removeAll(selectline.getPoints());
            //System.out.println("points removed");
            //selectline.getPoints().addAll(new Double[]{p1X,p1Y, 
                                                        //p1X+}); 
            selectrectangle.setX(p1X);
            selectrectangle.setY(p1Y);
            selectrectangle.setWidth(p2X-p1X);
            selectrectangle.setHeight(p2Y-p1Y);
            selectrectangle.getStrokeDashArray().addAll(20d, 40d);
            selectrectangle.setVisible(true);                 
            
        }else{

        }

    }

    Double[] CorrectLineViewer(){
        Double[] points;
        Double width = lineshape.getStrokeWidth();
        //if (p1X < p2X){
            points = new Double[]{p1X - width, p1Y + width,
                p2X + width, p2Y + width,
                 
                p2X + width, p2Y - width,
                p1X - width, p1Y - width};
            
        //}else if{

        //}
        return points;
    }

    @FXML
    void SelectOptions(ActionEvent e){
        if (eraseselected.isSelected()){
            //System.out.println("shape selected erased");
            if (shape == "rect"){
                dessins.remove(rectshape);
                selectrectangle.setVisible(false);
            }else if (shape == "line"){
                dessins.remove(lineshape);
                selectrectangle.setVisible(false);
            }else if (shape == "circle"){
                dessins.remove(circleshape);
                selectcircle.setVisible(false);
            }
            redraw();

            eraseselected.setSelected(false);
        }

        if (sizeselected.isSelected()){
            usageselected = "size";

            if (shape == "rect"){
                selectrectangle.setVisible(false);
    
                p1.setVisible(true);
                p2.setVisible(true);
                p3.setVisible(true);
                p4.setVisible(true);
    
            }else if (shape == "line"){
                selectrectangle.setVisible(false);
    
                p1.setVisible(true);
                p2.setVisible(true);
                
                
            }else if (shape == "circle"){
                selectcircle.setVisible(false);

                p1.setVisible(true);
                p2.setVisible(true);
                p3.setVisible(true);
                p4.setVisible(true);

            }
            relocatedPoints();
            
            
        
        }

        if (colorselected.isSelected() | fillselected.isSelected()){
            usageselected = "color";
            if (shape == "rect"){
                colorselector.setLayoutX(rectshape.getX() + rectshape.getWidth() + 15);
                colorselector.setLayoutY(rectshape.getY() + rectshape.getHeight() - 15);
            }else if (shape == "line"){
                colorselector.setLayoutX(lineshape.getEndX() + lineshape.getStrokeWidth() + 15);
                colorselector.setLayoutY(lineshape.getEndY() + lineshape.getStrokeWidth() - 15);
            }else if (shape == "circle"){
                colorselector.setLayoutX(circleshape.getCenterX() + circleshape.getRadiusX() + 15);
                colorselector.setLayoutY(circleshape.getCenterY() + circleshape.getRadiusY() - 15);
            }
            colorselector.show();
        }

        if (moveselected.isSelected()){
            usageselected = "move";
            selectrectangle.setVisible(true);
        }
        /*if (duplicate.isSelected()){

        }*/
        
        
           
    }

    void relocatedPoints(){
        if (shape == "rect"){

            //haut gauche
            p1.setCenterX(rectshape.getX() - rectshape.getStrokeWidth()/2);
            p1.setCenterY(rectshape.getY() - rectshape.getStrokeWidth()/2);

            //haut doit
            p2.setCenterX(rectshape.getX() + rectshape.getWidth() + rectshape.getStrokeWidth()/2);
            p2.setCenterY(rectshape.getY() - rectshape.getStrokeWidth()/2);
    
            //bas gauche
            p3.setCenterX(rectshape.getX() - rectshape.getStrokeWidth()/2);
            p3.setCenterY(rectshape.getY() + rectshape.getHeight() + rectshape.getStrokeWidth()/2);

            // bas droit
            p4.setCenterX(rectshape.getX() + rectshape.getWidth() + rectshape.getStrokeWidth()/2);
            p4.setCenterY(rectshape.getY() + rectshape.getHeight() + rectshape.getStrokeWidth()/2);

        }else if (shape == "line"){
            p1.setCenterX(lineshape.getStartX());
            p1.setCenterY(lineshape.getStartY());

            p2.setCenterX(lineshape.getEndX());
            p2.setCenterY(lineshape.getEndY());

        }else if (shape == "circle"){
            //haut
            p1.setCenterX(circleshape.getCenterX());
            p1.setCenterY(circleshape.getCenterY() - circleshape.getRadiusY() - circleshape.getStrokeWidth()/2);

            //bas
            p2.setCenterX(circleshape.getCenterX());
            p2.setCenterY(circleshape.getCenterY() + circleshape.getRadiusY() + circleshape.getStrokeWidth()/2);

            //droit
            p3.setCenterX(circleshape.getCenterX() + circleshape.getRadiusX() + circleshape.getStrokeWidth()/2);
            p3.setCenterY(circleshape.getCenterY());

            //gauche
            p4.setCenterX(circleshape.getCenterX() - circleshape.getRadiusX() - circleshape.getStrokeWidth()/2);
            p4.setCenterY(circleshape.getCenterY());
        }
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

            if (objet instanceof Line) {
                lineobject = (Line) objet;
                gc.setLineWidth(lineobject.getStrokeWidth());
                gc.setStroke(lineobject.getStroke());
                gc.strokeLine(lineobject.getStartX(), lineobject.getStartY(), lineobject.getEndX(), lineobject.getEndY());
            }

            if (objet instanceof Ellipse) {
                circleobject = (Ellipse) objet;
                gc.setLineWidth(circleobject.getStrokeWidth());
                gc.setStroke(circleobject.getStroke());
                gc.setFill(circleobject.getFill());
                gc.fillOval(circleobject.getCenterX() - circleobject.getRadiusX(), circleobject.getCenterY() - circleobject.getRadiusY(), circleobject.getRadiusX()*2, circleobject.getRadiusY()*2);
                gc.strokeOval(circleobject.getCenterX() - circleobject.getRadiusX(), circleobject.getCenterY() - circleobject.getRadiusY(), circleobject.getRadiusX()*2, circleobject.getRadiusY()*2);

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
        
        if (e.getSource() instanceof Circle){
            // the point dragged
            Circle p = (Circle) e.getSource();

            //removes all other points while dragging
            ShowPointsSelector(p, false);
            
            if (shape == "rect"){
                rectX = rectshape.getX();
                rectY = rectshape.getY();

                resizerect(p, e);
                
            }else if (shape == "line"){
                lineX = lineshape.getStartX();
                lineY = lineshape.getStartY();

                resizeline(p, e);

            }else if (shape == "circle"){ 
                circleX = p.getCenterX();
                circleY = p.getCenterY();

                resizecircle(p, e);
            }

            gc.setFill(Color.WHITESMOKE);
            gc.fillRect(0, 0, board.getWidth(), board.getHeight());

            gc.drawImage(drawings, 0, 0);
            
            
            if (shape == "rect"){

                gc.setFill(rectshape.getFill());
                gc.setStroke(rectshape.getStroke());
                gc.setLineWidth(rectshape.getStrokeWidth());
                gc.strokeRect(X, Y, rectshape.getWidth(), rectshape.getHeight());
                gc.fillRect(X+rectshape.getStrokeWidth()/2, Y+rectshape.getStrokeWidth()/2, 
                rectshape.getWidth()-rectshape.getStrokeWidth(), rectshape.getHeight()-rectshape.getStrokeWidth());
            
            }else if (shape == "line"){

                gc.setStroke(lineshape.getStroke());
                gc.setLineWidth(lineshape.getStrokeWidth());
                gc.strokeLine(lineshape.getStartX(), lineshape.getStartY(), lineshape.getEndX(), lineshape.getEndY());
           
            }else if (shape == "circle"){

                gc.setFill(circleshape.getFill());
                gc.setStroke(circleshape.getStroke());
                gc.setLineWidth(circleshape.getStrokeWidth());
            
                //décalage seulement sur l'affichage pendant la redimension
                gc.strokeOval(circleshape.getCenterX() - circleobject.getRadiusX(), circleshape.getCenterY() - circleobject.getRadiusY(), circleshape.getRadiusX()*2, circleshape.getRadiusY()*2);
                gc.fillOval(circleshape.getCenterX() - circleobject.getRadiusX(), circleshape.getCenterY() - circleobject.getRadiusY(), circleshape.getRadiusX()*2, circleshape.getRadiusY()*2);
    
            }

        }
    }
    
    void resizerect(Circle p, MouseEvent e){
        p.setCenterX(e.getX());
        p.setCenterY(e.getY());
        
        if (p == p1){ //point haut gauche


            X = e.getX() + rectshape.getStrokeWidth()/2;
            Y = e.getY() + rectshape.getStrokeWidth()/2;

            rectshape.setX(e.getX());
            rectshape.setY(e.getY());

            rectshape.setWidth(rectshape.getWidth() - e.getX() + rectX); 
            rectshape.setHeight(rectshape.getHeight() - e.getY() + rectY );      

        }
        if (p == p2){ //point haut droit

            X = rectX - rectshape.getStrokeWidth()/2;
            Y = e.getY() + rectshape.getStrokeWidth()/2;

            rectshape.setY(e.getY());

            rectshape.setWidth(e.getX() - rectX); 
            rectshape.setHeight(rectshape.getHeight() - e.getY() + rectY);

            
        }
        if (p == p3){ //point bas gauche

            X = rectX + rectshape.getStrokeWidth()/2;
            Y = rectY - rectshape.getStrokeWidth()/2;

            rectshape.setX(e.getX());
            

            rectshape.setWidth(rectshape.getWidth() - e.getX() + rectX); 
            rectshape.setHeight(e.getY() - rectY); 


        }
        if (p == p4){ //point bas droit

            X = rectX - rectshape.getStrokeWidth()/2;
            Y = rectY - rectshape.getStrokeWidth()/2;
        
            rectshape.setWidth(e.getX() - rectX); 
            rectshape.setHeight(e.getY() - rectY); 

        }
    }

    void resizeline(Circle p, MouseEvent e){
        X = e.getX();
        Y = e.getY();
        p.setCenterX(e.getX());
        p.setCenterY(e.getY());
        //double pente = Math.abs((lineshape.getEndY() - lineshape.getStartY()))/(Math.abs(lineshape.getStartX() - lineshape.getEndX())); 
        if (p == p1){ //point de départ
            lineshape.setStartX(X);  
            lineshape.setStartY(Y);    
        }
        if (p == p2){ //point de fin
            lineshape.setEndX(X);  
            lineshape.setEndY(Y);
        }
    }

    void resizecircle(Circle p, MouseEvent e){

        if (p == p1){ //point en haut
            p.setCenterY(e.getY());
            circleshape.setCenterY(circleshape.getCenterY() - circleY + e.getY());
            circleshape.setRadiusY(circleshape.getRadiusY() + circleY - e.getY());
        }
        if (p == p2){ //point en bas
            p.setCenterY(e.getY());
            circleshape.setCenterY(circleshape.getCenterY() - circleY + e.getY());
            circleshape.setRadiusY(circleshape.getRadiusY() - circleY + e.getY());
        }
        if (p == p3){ // point à droite
             p.setCenterX(e.getX());
             circleshape.setCenterX(circleshape.getCenterX() - circleX + e.getX());
             circleshape.setRadiusX(circleshape.getRadiusX() - circleX + e.getX());
        }
        if (p == p4){ // point à gauche
            p.setCenterX(e.getX());
             circleshape.setCenterX(circleshape.getCenterX() - circleX + e.getX());
             circleshape.setRadiusX(circleshape.getRadiusX() + circleX - e.getX());
        }
        
    }

    @FXML
    void BeginResize(){
        System.out.println("begin");
        if (shape == "rect"){
            dessins.remove(rectshape);
        }else if (shape == "line"){
            dessins.remove(lineshape);
        }else if (shape == "circle"){
            dessins.remove(circleshape);
        }
        redraw();
    }

    @FXML
    void SizeSelected(MouseEvent e){
        
        if (e.getSource() instanceof Circle){
            Circle p = (Circle) e.getSource();
            if (shape == "rect"){
                // gère un décalage de la figure rectangulaire 
                if (p == p1){
                    rectshape.setX(rectshape.getX() + rectshape.getStrokeWidth()/2);
                    rectshape.setY(rectshape.getY() + rectshape.getStrokeWidth()/2);
                }
                if (p == p2){
                    rectshape.setX(rectshape.getX() - rectshape.getStrokeWidth()/2);
                    rectshape.setY(rectshape.getY() + rectshape.getStrokeWidth()/2);
                }
                if (p == p3){
                    rectshape.setX(rectshape.getX() + rectshape.getStrokeWidth()/2);
                    rectshape.setY(rectshape.getY() - rectshape.getStrokeWidth()/2);
                }
                if (p == p4){
                    rectshape.setX(rectshape.getX() - rectshape.getStrokeWidth()/2);
                    rectshape.setY(rectshape.getY() - rectshape.getStrokeWidth()/2);
                }
                dessins.add(rectshape);
            }else if(shape == "line"){
                dessins.add(lineshape);
            }else if(shape == "circle"){

                dessins.add(circleshape);
            }else if(shape == ""){
                
            }


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
            if (shape =="rect"){
                rectshape.setStroke(colorselector.getValue());
                selectrectangle.setVisible(false);
            }else if (shape =="line"){
                lineshape.setStroke(colorselector.getValue());
                selectrectangle.setVisible(false);
            }else if(shape == "circle"){
                circleshape.setStroke(colorselector.getValue());
                selectcircle.setVisible(false);
            }

            colorselected.setSelected(false);
            redraw();
        }

        if (fillselected.isSelected()){
            if (shape =="rect"){
                rectshape.setFill(colorselector.getValue());
                selectrectangle.setVisible(false);
            }else if (shape =="line"){
                lineshape.setStroke(colorselector.getValue());
                selectrectangle.setVisible(false);
            }else if (shape == "circle"){
                circleshape.setFill(colorselector.getValue());
                selectcircle.setVisible(false);
            }
        
            fillselected.setSelected(false);
            redraw();

        }
        
        drawings = board.snapshot(null, null);
        
    }

    

    @FXML
    void StartMoveSelector(MouseEvent e){
        if (shape == "rect"){
            dessins.remove(rectshape);
        }else if (shape == "line"){
            dessins.remove(lineshape);
        }else if (shape == "circle"){
            dessins.remove(circleshape);
        }
        redraw();
        startX = e.getX();
        startY = e.getY();
    }

    @FXML
    void MoveSelector(MouseEvent e){
        if (moveselected.isSelected()){
            if (shape == "rect" | shape == "line"){  
                Rectangle r = (Rectangle) e.getSource();
                double offsetX = e.getX() - startX;
                double offsetY = e.getY() - startY;
                r.setX(r.getX() + offsetX);
                r.setY(r.getY() + offsetY);

        
                
                
                gc.setFill(Color.WHITESMOKE);
                gc.fillRect(0, 0, board.getWidth(), board.getHeight());
                gc.drawImage(drawings, 0, 0);
                
                if (shape == "rect"){
                    gc.setLineWidth(rectshape.getStrokeWidth());
                    gc.setStroke(rectshape.getStroke());
                    gc.setFill(rectshape.getFill());
                    gc.fillRect(r.getX()+rectshape.getStrokeWidth()/2, r.getY()+rectshape.getStrokeWidth()/2, 
                    rectshape.getWidth()-rectshape.getStrokeWidth(), rectshape.getHeight()-rectshape.getStrokeWidth());
                    gc.strokeRect(r.getX(), r.getY(), rectshape.getWidth(), rectshape.getHeight());
                
                }else if (shape == "line"){
                    gc.setLineWidth(lineshape.getStrokeWidth());
                    gc.setStroke(lineshape.getStroke());
                    
                    gc.strokeLine(r.getX(), r.getY(), r.getX()+r.getWidth(), r.getY()+r.getHeight());
                }

                startX = e.getX();
                startY = e.getY();

            }else if (shape == "circle"){
                

                startX = e.getX();
                startY = e.getY();
            }
        }
    }

    @FXML
    void EndMoveSelector(MouseEvent e){
        selectrectangle.setVisible(false);
        
        if (shape == "rect"){
            Rectangle rectmoved = new Rectangle(selectrectangle.getX(), selectrectangle.getY(), rectshape.getWidth(), rectshape.getHeight());
            rectmoved.setStrokeWidth(rectshape.getStrokeWidth());
            rectmoved.setStroke(rectshape.getStroke());
            rectmoved.setFill(rectshape.getFill());
            dessins.add(rectmoved);
        }else if (shape == "line"){
            // léger décalage visible
            Line linemoved = new Line(selectrectangle.getX(), selectrectangle.getY(), selectrectangle.getX()+selectrectangle.getWidth(), selectrectangle.getY()+selectrectangle.getHeight());
            linemoved.setStrokeWidth(lineshape.getStrokeWidth());
            linemoved.setStroke(lineshape.getStroke());
            dessins.add(linemoved);
        }else if (shape == "circle"){

        }



        redraw();

        moveselected.setSelected(false);
    }

    @FXML
    private void updateZoomValueLabel() {
        ZoomLabel.setText(String.format("%.0f%%", (zoomFactor*100)));
    }

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
    void initialize() {
        drawings = new WritableImage((int) board.getWidth(), (int) board.getHeight());

        selectviewer.getStrokeDashArray().addAll(40d, 40d);

        allTOOLS.add(pen);allTOOLS.add(eraser);allTOOLS.add(rectangle);allTOOLS.add(circle);allTOOLS.add(triangle);
        allTOOLS.add(line);allTOOLS.add(selection);allTOOLS.add(eraserviewer);allTOOLS.add(selectviewer);allTOOLS.add(selectrectangle);
            
        pointsforsize.add(p1);pointsforsize.add(p2);pointsforsize.add(p3);
        pointsforsize.add(p4);

        gc = board.getGraphicsContext2D();
        gc.setFill(Color.WHITESMOKE);
        gc.fillRect(0, 0, board.getWidth(), board.getHeight());
        
    }

}