package modelTest;
import java.awt.Frame;
import controlP5.ControlP5;
import GeoTools.*;
import peasy.PeasyCam;
import processing.core.PApplet;

public class test2 extends PApplet {
	private static final long serialVersionUID = 1L;
	PeasyCam cam;
	OnMesh amesh;
	fireImport iprt;
	 
	 
	 public void setup() {
		 size(600, 400, OPENGL);
		 iprt=new fireImport(this);
		 amesh=iprt.readobjFile("land1.obj");
		 cam = new PeasyCam(this, 200);
			cam.setMinimumDistance(50);
			cam.setMaximumDistance(5000);	
			println(amesh.Points.size()+"/"+amesh.faces.size());
			amesh.CombineIdenticalVertices();
			OnXform xf=new OnXform();
			xf.Rotation(On3dVector.Yaxis(),On3dVector.Zaxis(),new On3dPoint());
			amesh.Transform(xf);
			xf=new OnXform();
			xf.Scale(new On3dPoint(1,1,0), 5);
			amesh.Transform(xf);
	 }
	 public void draw() {
			background(30);
			OnXform.DrawWhiteGrid(this);
			fill(255);
			stroke(0,20);
			amesh.draw(this,1);  
	 }
}
