import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import GeoTools.*;
import peasy.PeasyCam;
import processing.core.PApplet;
import processing.core.PVector;
public class test extends PApplet {
	private static final long serialVersionUID = 1L;
	PeasyCam cam;
	ArrayList<PVector> pts = new ArrayList<PVector>();
	float Bay = 120;
	float height = 100;
	int size = 3;
	OnMesh amesh;
	float maxX=0;float minX=0;
	float maxY=0;float minY=0;
	float maxZ=0;float minZ=0;
	ColorSlider slider;
	public	int[] c;
	fireImport iprt;
	public void setup() {
		slider=new ColorSlider(this);
		iprt=new fireImport(this);
		slider.colors.add(color(130, 230, 200));			
		slider.colors.add(color(200, 255, 200));
		slider.colors.add(color(150, 255, 150));
	    slider.colors.add(color(250, 150, 80));
			
		cam = new PeasyCam(this, 200);
		cam.setMinimumDistance(50);
		cam.setMaximumDistance(5000);
		
		  amesh=new OnMesh();
		size(600, 400, OPENGL);
	
	//	pts=iprt.readGCodeFile("C:/Users/Administrator/Desktop/PrintSTL/h1.gcode");
		//println(pts.size());
		// noLoop();
		
		
		ambientLight(148, 148, 148);
		lightSpecular(230, 230, 230);
		directionalLight(255, 255, 255, 0, -0.5f, -1);
		specular(255, 255, 255);
	    shininess(16.0f);// */
	    smooth();
	   
		amesh=iprt.read3dsFile("land1.3ds");
		amesh.CombineIdenticalVertices();
		OnXform xf=new OnXform();
		xf.Scale(new On3dPoint(-2,0,0),5);
		xf.printme();
	amesh.Transform(xf);
		println("minZ"+minZ);
		println("maxZ"+maxZ);
		c=new int[amesh.VertexCount()];
		for(int i=0;i<c.length;i++){
			On3dPoint p=(On3dPoint)amesh.Points.get(i);
		c[i]=slider.getGradient((p.z-minZ)/(maxZ-minZ));		
		}
		
	}
	public void draw() {
		background(230);
	
		OnXform.DrawGrid(this);
    if (amesh.VertexCount()>0){
    	stroke(0,250);
    	//noStroke();
    	amesh.draw(this,1);
    	stroke(100,100,255,140);
    	amesh.drawNormals(this,13);
    }
		
		if(pts.size()>0){
	    stroke(0,10);
		for (int i = 1; i < pts.size(); i++) {			
			PVector v1 = (PVector) pts.get(i-1);
			PVector v2 = (PVector) pts.get(i);			
			beginShape(LINES);
			vertex(v1.x, v1.y, v1.z);
			vertex(v2.x, v2.y, v2.z);
			endShape();
			//point(v1.x, v1.y, v1.z/10);
		}
		}
	}

	
		void showPoint(On3dPoint p) {
		noStroke();
		fill(150, 0, 0);
		pushMatrix();
		translate(p.x, p.y, p.z);
		sphere(4);
		popMatrix();
	}
	// /////////////////////////////////////////////////////////////
}