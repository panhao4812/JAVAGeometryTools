package charttest;
import java.awt.Frame;
import controlP5.ControlP5;
import GeoTools.*;
import peasy.PeasyCam;
import processing.core.PApplet;
import processing.core.PConstants;

public class Chart1 extends PApplet {
static On3dPoint center=new On3dPoint(0,0,0);
PeasyCam cam;
OnMesh mesh1;
MeshCreation mc=new MeshCreation();
public void setup(){
	size(800,600,OPENGL);
	 cam = new PeasyCam(this, 200);
		cam.setMinimumDistance(50);
		cam.setMaximumDistance(5000);	
	
float R=(float)height*4f/9f;
	OnPolyline pl=OnPolyline.Arc(300, PI*2, PI/90);
	OnPolyline pl2=OnPolyline.Arc(200, PI*2, PI/90);
	mesh1=mc.MeshLoft(pl, pl2, false, false);
}
public void readText(String name){
	
	
	
}
public void draw(){
	background(0);
	OnXform.DrawWhiteGrid(this);
	fill(255);
	noStroke();	stroke(0,20);
	mesh1.draw(this);

}

class chart{
	
	
	
	
}
}