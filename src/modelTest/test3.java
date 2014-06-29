package modelTest;
import java.awt.Frame;
import controlP5.ControlP5;
import GeoTools.*;
import peasy.PeasyCam;
import processing.core.PApplet;

public class test3 extends PApplet {
	private static final long serialVersionUID = 1L;
	PeasyCam cam;
	OnPolyline pl,pl2,pl3;
	 
	 public void setup() {
		 size(600, 400, OPENGL);
		 cam = new PeasyCam(this, 200);
			cam.setMinimumDistance(50);
			cam.setMaximumDistance(5000);				
		pl=new OnPolyline();
		pl.add(new On3dPoint(0,0,0));
		pl.add(new On3dPoint(8,80,0));
		pl.add(new On3dPoint(80,180,0));
		pl.add(new On3dPoint(260,50,0));
		pl.add(new On3dPoint(170,-30,0));
		pl.add(new On3dPoint(30,-60,0));
		pl.add(new On3dPoint(0,0,0));
		pl.Transform(OnXform.translation(new On3dVector(0,0,3)));
		pl2=new OnPolyline(pl);
		pl.ccSubdivide(4);	
	 }
	 public void draw() {
			background(230);		
			OnXform.DrawDarkGrid(this);
			fill(255);
			strokeWeight(1);
			stroke(0,0,0,220);		
			pl2.draw(this, 1);
			strokeWeight(3);
			stroke(255,0,0,220);
			pl.draw(this, 1);
	 }
}
