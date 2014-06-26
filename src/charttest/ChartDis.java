package charttest;
import java.awt.Color;
import java.awt.Frame;
import java.util.ArrayList;

import controlP5.ControlP5;
import GeoTools.*;
import peasy.PeasyCam;
import peasy.org.apache.commons.math.geometry.Vector3D;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;

@SuppressWarnings("serial")
public class ChartDis extends PApplet {
static On3dPoint center=new On3dPoint(0,0,0);
PeasyCam cam;
MeshCreation mc=new MeshCreation();
ArrayList<chart> charts;chart chart1;
PFont font;
public void setup(){
	size(800,600,OPENGL);
	 cam = new PeasyCam(this, 200);
     cam.setMinimumDistance(50);
	 cam.setMaximumDistance(5000);	
	 charts=new ArrayList<chart> ();
float R=(float)height*4f/9f;
font = createFont("simhei", 300, true);
	
chart1=new chart(this,"fsgf",font,80,R/2,0,PI/2);
chart1.ComputeEdge(PI/20);
}
public void readText(String name){
	chart1.draw();	
}
public void draw(){
	background(0);
	OnXform.DrawWhiteGrid(this);
}

public class chart{
	private float hei=0;
	private float r=0;
	private float rot=0;
	private float range=0;
  
	private OnMesh m_mesh;
	private int color;
	private  PImage G;
	PApplet p;
	On3dPoint pos1,pos2,pos3,pos4;
public chart(PApplet parent,String text,PFont font,	
		float Hei,float R,float Rot,float Range){
	this.p=parent;
    hei=Hei; r=R;Rot=rot;range=Range;
	m_mesh=new OnMesh();
	color=p.color(255);
	initializePic(100,text,font);
}
 void initializePic(int letterSize,String str,PFont font){	
	   PGraphics  g = p.createGraphics(letterSize*2, letterSize*2, JAVA2D);     
		  g.beginDraw();
		  g.background(color(0, 0, 1, 0));
		  g.fill(color(0, 0, 0));
		  g.textAlign(CENTER, CENTER);
		  g.translate(letterSize/2, letterSize/2);
		  g.textFont(font);
		  g.text(str, 0, 0);
		  g.endDraw();
		  G=(PImage)g;	
}
public void ComputeEdge(float offset){
    float radius=r+offset/2;
	float t=offset/radius;
	float Height=hei-offset;	
	OnPolyline pl1=OnPolyline.Arc(radius,this.range-t, t);
	OnPolyline pl2=OnPolyline.Arc(radius+Height,this.range-t, t);
	this.m_mesh=mc.MeshLoft(pl1, pl2, false, false);
	this.m_mesh.Transform(OnXform.rotation(rot+t/2));
	On3dPoint Pos1=pl1.get(pl1.size()/2);
	On3dPoint Pos2=pl2.get(pl1.size()/2);
	On3dVector v=On3dPoint.PointSub(Pos2, Pos1);
	On3dVector v2=On3dVector.OnCrossProduct(v, On3dVector.Zaxis());
	v2.Unitize();v.mul(5);
	pos1=On3dPoint.PointAdd(Pos1, v2);
	pos2=On3dPoint.PointAdd(Pos2, v2);
	v.Reverse();
	pos3=On3dPoint.PointAdd(Pos2, v2);
	pos4=On3dPoint.PointAdd(Pos1, v2);
}
public void draw(){
	p.noStroke();
    p.fill(this.color);
	m_mesh.draw(p);
	p.pushMatrix();
	p.beginShape();
	texture(G);
	vertex(pos1.x, pos1.y, pos1.z+2, 0,0);
	vertex(pos2.x, pos2.y, pos2.z+2, 0,1);
	vertex(pos3.x, pos3.y, pos3.z+2, 1,1);
	vertex(pos4.x, pos4.y, pos4.z+2, 1,0);
	p.endShape();
	p.popMatrix();
}

}
}