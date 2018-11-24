


import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class OFX extends Application{
    public void addObject(OpticalObject o, double xPos, double height, String title, Paint p, Group main) {
        o.setStartX(xPos);
        o.setEndX(xPos);
        o.setStartY(Lense.opticalaxisY);
        o.setEndY(height);
        o.setTitle(title);
        FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
        Label titleLabel = new Label(title);
        double offsetTitle = fontLoader.computeStringWidth(titleLabel.getText(), titleLabel.getFont())/2;
        titleLabel.relocate(o.getStartX()-offsetTitle, (o.getEndY()>540 ? o.getEndY()+20 : o.getEndY()-20));
        o.setStroke(p);
        main.getChildren().addAll(o,titleLabel);
    }
    public void addLense(Lense l, double xPos, String title, double FD, Group main, OpticalObject o, boolean lines) {
        l=new Lense(xPos, FD, title, main);
        OpticalObject temp=o.generateImage(l, "Image of "+o.getTitle(), o.getStroke(), main, lines);
        Line backupLine = new Line(o.getStartX(),o.getStartY(),o.getEndX(),o.getEndY());
        backupLine.setStroke(o.getStroke());
        main.getChildren().add(backupLine);
        o.setStartX(temp.getStartX());
        o.setEndX(temp.getEndX());
        o.setStartY(temp.getStartY());
        o.setEndY(temp.getEndY());
        o.setTitle(temp.getTitle());
        o.setStroke(temp.getStroke());
    }
    public void clearFields(TextField... n) {
        for (TextField n1 : n) {
            n1.setText("");
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage pri) {
       Group main = new Group();
       
       Text tObjectTitle = new Text("Enter Object Title: ");
       Text tObjectXPos = new Text("Enter Object X-Pos("+Screen.getPrimary().getBounds().getMinX()+"-"+Screen.getPrimary().getBounds().getMaxX()+"): ");
       Text tObjectHeight = new Text("Enter Object Height: ");
       Text tObjectColor = new Text("Choose Object Color: ");
       TextField txtObjectTitle = new TextField();
       TextField txtObjectXPos = new TextField();
       TextField txtObjectHeight = new TextField();
       ColorPicker cprObjectColor = new ColorPicker();
       Button bAddObject = new Button("Add Object");
       OpticalObject mainObject=new OpticalObject();
       bAddObject.setOnAction(e -> addObject(mainObject,Double.parseDouble(txtObjectXPos.getText()), 540-Double.parseDouble(txtObjectHeight.getText()), txtObjectTitle.getText(),cprObjectColor.getValue(), main));
       Button bObjectFieldClear = new Button("Clear Fields");
       bObjectFieldClear.setOnAction(e -> clearFields(txtObjectTitle,txtObjectXPos,txtObjectHeight));
       
       Text tLenseTitle = new Text("Enter Lense Title: ");
       Text tLenseXPos = new Text("Enter Lense X-Pos("+Screen.getPrimary().getBounds().getMinX()+"-"+Screen.getPrimary().getBounds().getMaxX()+"): ");
       Text tLenseFocusDistance = new Text("Enter Focus Distance: ");
       TextField txtLenseTitle = new TextField();
       TextField txtLenseXPos = new TextField();
       TextField txtLenseFocusDistance = new TextField();
       CheckBox cbDrawLines = new CheckBox("Draw Lines");
       Button bAddLense = new Button("Add Lense");
       Lense lense=new Lense();
       bAddLense.setOnAction(e -> addLense(lense,Double.parseDouble(txtLenseXPos.getText()), txtLenseTitle.getText(),Double.parseDouble(txtLenseFocusDistance.getText()), main, mainObject, cbDrawLines.isSelected()));
       Button bLenseFieldClear = new Button("Clear Fields");
       bLenseFieldClear.setOnAction(e -> clearFields(txtLenseTitle,txtLenseXPos,txtLenseFocusDistance));
       
       
       
       GridPane gp = new GridPane(); 
       gp.setMinSize(400, 200);  
       gp.setPadding(new Insets(10, 10, 10, 10)); 
       gp.setVgap(5); 
       gp.setHgap(5);       
       gp.setAlignment(Pos.CENTER); 
       gp.add(tObjectTitle, 0, 0);
       gp.add(txtObjectTitle, 1, 0);
       gp.add(tObjectXPos, 0, 1);
       gp.add(txtObjectXPos, 1, 1);
       gp.add(tObjectHeight, 0, 2);
       gp.add(txtObjectHeight, 1, 2);
       gp.add(tObjectColor, 0, 3);
       gp.add(cprObjectColor, 1, 3);
       gp.add(bAddObject, 0, 4);
       gp.add(bObjectFieldClear, 1, 4);
       gp.add(tLenseTitle, 3, 0);
       gp.add(txtLenseTitle, 4, 0);
       gp.add(tLenseXPos, 3, 1);
       gp.add(txtLenseXPos, 4, 1);
       gp.add(tLenseFocusDistance, 3, 2);
       gp.add(txtLenseFocusDistance, 4, 2);
       gp.add(cbDrawLines, 3, 3);
       gp.add(bAddLense, 3, 4);
       gp.add(bLenseFieldClear, 4, 4);
       Button bClearEverything = new Button("Clear Diagram");
       bClearEverything.setOnAction(e -> setupAxis(main));
       gp.add(bClearEverything,2,5);
       Scene priScene = new Scene(gp);
       pri.setTitle("Setup");
       pri.setScene(priScene);
       
        
       Stage sec = new Stage();
       setupAxis(main);
       Scene scene = new Scene(main, Screen.getPrimary().getBounds().getMaxY(), Screen.getPrimary().getBounds().getMaxX(), Color.WHITE);
       sec.setScene(scene);
       sec.setTitle("Diagram");
       
       /*
       Lense l1 = new Lense(250,100,"Converging 1",main);
       OpticalObject o1 = new OpticalObject(50, 540-100, "Object 1",Color.RED, main);
       OpticalObject i1 = o1.generateImage(l1,"Image 1",Color.BLUE, main, true);
       Lense l2 = new Lense(580,100,"Converging 2",main);
       OpticalObject i2 = i1.generateImage(l2,"Image 2",Color.YELLOW, main, true);
       Lense l3 = new Lense(1250,-100,"Diverging 1",main);
       OpticalObject i3 = i2.generateImage(l3,"Final Image",Color.GREEN, main, true);
       */
       sec.show();
       pri.show();
    }
    public void setupAxis(Group main) {
       main.getChildren().clear();
       Line opticalaxismarking100 = new Line(Screen.getPrimary().getBounds().getMinX(),Lense.opticalaxisY,Screen.getPrimary().getBounds().getMaxX(),Lense.opticalaxisY);
       opticalaxismarking100.getStrokeDashArray().addAll(1d, 99d);
       opticalaxismarking100.setStrokeWidth(5);
       main.getChildren().add(opticalaxismarking100);
       Line opticalaxismarking10 = new Line(Screen.getPrimary().getBounds().getMinX(),Lense.opticalaxisY,Screen.getPrimary().getBounds().getMaxX(),Lense.opticalaxisY);
       opticalaxismarking10.getStrokeDashArray().addAll(1d, 9d);
       opticalaxismarking10.setStrokeWidth(3);
       main.getChildren().add(opticalaxismarking10);
       Line opticalaxis = new Line(Screen.getPrimary().getBounds().getMinX(),Lense.opticalaxisY,Screen.getPrimary().getBounds().getMaxX(),Lense.opticalaxisY);
       main.getChildren().add(opticalaxis);
    }
}
class MouseHandler implements EventHandler<MouseEvent> {
    @Override
    public void handle(MouseEvent addButton) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
class OpticalObject extends Line {
    private String title;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public OpticalObject() {
        super(0, Lense.opticalaxisY, 0, 0);
        this.title="";
    }
    public OpticalObject(double startX, double endY, String title, Paint p, Group main) {
        super(startX, Lense.opticalaxisY, startX, endY);
        this.title=title;
        FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
        Label titleLabel = new Label(title);
        double offsetTitle = fontLoader.computeStringWidth(titleLabel.getText(), titleLabel.getFont())/2;
        titleLabel.relocate(startX-offsetTitle, (endY>540 ? endY+20 : endY-20));
        this.setStroke(p);
        main.getChildren().addAll(this,titleLabel);
    }
    public OpticalObject generateImage(Lense l, String title, Paint p, Group main, boolean lines) {
        double dobj = l.getStartX()-this.getStartX();
        double hobj = Lense.opticalaxisY-this.getEndY();
        if(dobj!=l.getFD()) {
        if(lines) {
        genL1(l,main);
        genL2(l,main);
        genL3(l,main);   
        }
        double di = 1/(1/l.getFD()-1/dobj);
        double m = -1*(di/dobj);
        return new OpticalObject(l.getStartX()+di,Lense.opticalaxisY-m*hobj,title,p, main);  
        } else return new OpticalObject(this.getStartX(),this.getEndY(),"Impossible",p, main);
        
    }
    private void genL1(Lense l, Group main) {
        if(l.getFD()>0) {
           if(l.getStartX()-this.getStartX()>l.getFD()) {
           Line l1a = new Line(this.getStartX(),this.getEndY(),l.getStartX(),this.getEndY());
           double xb2=Screen.getPrimary().getBounds().getMaxX();
           double m=(l.getF2Y()-l1a.getEndY())/(l.getF2X()-l1a.getEndX());
           double c=l1a.getEndY()-m*l1a.getEndX();
           Line l1b = new Line(l1a.getEndX(), l1a.getEndY(), xb2, m*xb2 + c);
           main.getChildren().addAll(l1a,l1b);
           } else {
           Line l1a = new Line(this.getStartX(),this.getEndY(),l.getStartX(),this.getEndY());
           double xb2=Screen.getPrimary().getBounds().getMaxX();
           double m=(l.getF2Y()-l1a.getEndY())/(l.getF2X()-l1a.getEndX());
           double c=l1a.getEndY()-m*l1a.getEndX();
           Line l1b = new Line(l1a.getEndX(), l1a.getEndY(), xb2, m*xb2 + c);
           Line l1c = new Line(l1b.getStartX(),l1b.getStartY(),Screen.getPrimary().getBounds().getMinX(),m*Screen.getPrimary().getBounds().getMinX()+c);
           l1c.getStrokeDashArray().addAll(25d, 10d);
           main.getChildren().addAll(l1a,l1b,l1c);
           }
        } else {
            
            double xb2=l.getStartX();
            double m=(l.getF2Y()-this.getEndY())/(l.getF2X()-this.getStartX());
            double c=this.getEndY()-m*this.getStartX();
            Line l1a = new Line(this.getStartX(),this.getEndY(),xb2,m*xb2 + c);
            Line l1b = new Line(l1a.getEndX(),l1a.getEndY(),Screen.getPrimary().getBounds().getMaxX(),l1a.getEndY() );
            Line l1c = new Line(l1b.getStartX(),l1b.getStartY(),Screen.getPrimary().getBounds().getMinX(),l1a.getEndY());
            l1c.getStrokeDashArray().addAll(25d, 10d);
            main.getChildren().addAll(l1a,l1b,l1c);   
            
        }
    }
    private void genL2(Lense l, Group main) {
        if(l.getFD()>0) {
           if(l.getStartX()-this.getStartX()>l.getFD()) {
           Line l2a = new Line(this.getStartX(),this.getEndY(),l.getStartX(),Lense.opticalaxisY);
           double xb2=Screen.getPrimary().getBounds().getMaxX();
           double m=(l2a.getEndY()-l2a.getStartY())/(l2a.getEndX()-l2a.getStartX());
           double c=l2a.getStartY()-m*l2a.getStartX();
           Line l2b = new Line(l2a.getStartX(), l2a.getStartY(), xb2, m*xb2 + c);
           main.getChildren().addAll(l2a,l2b); 
           } else {
           Line l2a = new Line(this.getStartX(),this.getEndY(),l.getStartX(),Lense.opticalaxisY);
           double xb2=Screen.getPrimary().getBounds().getMaxX();
           double m=(l2a.getEndY()-l2a.getStartY())/(l2a.getEndX()-l2a.getStartX());
           double c=l2a.getStartY()-m*l2a.getStartX();
           Line l2b = new Line(l2a.getStartX(), l2a.getStartY(), xb2, m*xb2 + c);
           Line l2c = new Line(l2a.getStartX(),l2a.getStartY(),Screen.getPrimary().getBounds().getMinX(),m*Screen.getPrimary().getBounds().getMinX()+c);
           l2c.getStrokeDashArray().addAll(25d, 10d);
           main.getChildren().addAll(l2a,l2b,l2c);
           }
           
        } else {
           
           Line l2a = new Line(this.getStartX(),this.getEndY(),l.getStartX(),Lense.opticalaxisY);
           double xb2=Screen.getPrimary().getBounds().getMaxX();
           double m=(l2a.getEndY()-l2a.getStartY())/(l2a.getEndX()-l2a.getStartX());
           double c=l2a.getStartY()-m*l2a.getStartX();
           Line l2b = new Line(l2a.getStartX(), l2a.getStartY(), xb2, m*xb2 + c);
           main.getChildren().addAll(l2a,l2b);    
           
            
        }
    }
    private void genL3(Lense l, Group main) {
        if(l.getFD()>0) {
           if(l.getStartX()-this.getStartX()>l.getFD()) {
           double xb2=l.getStartX();
           double m=(l.getF1Y()-this.getEndY())/(l.getF1X()-this.getStartX());
           double c=this.getEndY()-m*this.getStartX();
           Line l3a = new Line(this.getStartX(),this.getEndY(),xb2,m*xb2 + c);
           Line l3b = new Line(l3a.getEndX(),l3a.getEndY(),Screen.getPrimary().getBounds().getMaxX(),l3a.getEndY() );
           main.getChildren().addAll(l3a,l3b); 
           } else {
           double xb2=l.getStartX();
           double m=(l.getF1Y()-this.getEndY())/(l.getF1X()-this.getStartX());
           double c=this.getEndY()-m*this.getStartX();
           Line l3a = new Line(this.getStartX(),this.getEndY(),xb2,m*xb2 + c);
           Line l3b = new Line(l3a.getEndX(),l3a.getEndY(),Screen.getPrimary().getBounds().getMaxX(),l3a.getEndY() );
           Line l3c = new Line(l3b.getStartX(),l3b.getStartY(),Screen.getPrimary().getBounds().getMinX(),l3a.getEndY());
           l3c.getStrokeDashArray().addAll(25d, 10d);
           main.getChildren().addAll(l3a,l3b,l3c);
           }
           
        } else {
            Line l1a = new Line(this.getStartX(),this.getEndY(),l.getStartX(),this.getEndY());
           double xb2=Screen.getPrimary().getBounds().getMaxX();
           double m=(l.getF1Y()-l1a.getEndY())/(l.getF1X()-l1a.getEndX());
           double c=l1a.getEndY()-m*l1a.getEndX();
           Line l1b = new Line(l1a.getEndX(), l1a.getEndY(), xb2, m*xb2 + c);
           Line l1c = new Line(l1b.getStartX(),l1b.getStartY(),Screen.getPrimary().getBounds().getMinX(),m*Screen.getPrimary().getBounds().getMinX()+c);
           l1c.getStrokeDashArray().addAll(25d, 10d);
           main.getChildren().addAll(l1a,l1b,l1c);
        }
    }
}


class Lense extends Line {
    public static double opticalaxisY = 540;
    private Circle f1;
    private Circle f2;
    private double f1x;
    private double f1y;
    private double f2x;
    private double f2y;
    private double fd; //focus distance
    private String title;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Lense() {
        
    }
    public Lense(double startX, double fd, String title, Group main) {
        super(startX, 40, startX, 1040);
        this.title=title;
        this.fd=fd;
        this.f1x=startX-Math.abs(fd);
        this.f2x=startX+Math.abs(fd);
        this.f2y=this.f1y=opticalaxisY;
        this.f1=new Circle(f1x,f1y,5,Color.BLACK);
        this.f2=new Circle(f2x,f2y,5,Color.BLACK);
        FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
        Label fp1 = new Label(title+" FP1");
        double offset1 = fontLoader.computeStringWidth(fp1.getText(), fp1.getFont())/2;
        Label fp2 = new Label(title+" FP2");
        double offset2 = fontLoader.computeStringWidth(fp2.getText(), fp2.getFont())/2;
        Label titleLabel = new Label(title);
        double offset3 = fontLoader.computeStringWidth(titleLabel.getText(), titleLabel.getFont())/2;
        fp1.relocate(f1x-offset1, f1y+20);
        fp2.relocate(f2x-offset2, f2y+20);
        titleLabel.relocate(startX-offset3, 20);
        main.getChildren().addAll(this,this.getF1(),this.getF2(),fp1,fp2,titleLabel);
    }
    
    //Setters and getters
    
    public double getF1X() {
        return f1x;
    }
    public void setF1X(double f) {
        this.f1x=f;
    }
    public double getF1Y() {
        return f1y;
    }
    public void setF1Y(double f) {
        this.f1y=f;
    }
    public double getF2X() {
        return f2x;
    }
    public void setF2X(double f) {
        this.f2x=f;
    }
    public double getF2Y() {
        return f2y;
    }
    public void setF2Y(double f) {
        this.f2y=f;
    }
    public double getFD() {
        return fd;
    }
    public void setFD(double f) {
        this.fd=f;
    }
    public Circle getF1() {
        return f1;
    }
    public Circle getF2() {
        return f2;
    }
}
