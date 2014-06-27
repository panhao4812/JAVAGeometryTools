package charttest;
import java.util.ArrayList;

import GeoTools.*;
import peasy.PeasyCam;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;

@SuppressWarnings("serial")
public class ChartDis extends PApplet {
static On3dPoint center=new On3dPoint(0,0,0);
PeasyCam cam;
MeshCreation mc=new MeshCreation();
ArrayList<chart> charts;
PFont font;
ColorSlider slider;
public void setup(){
	size(800,600,OPENGL);
	 cam = new PeasyCam(this, 200);
     cam.setMinimumDistance(50);
	 cam.setMaximumDistance(5000);	
	 charts=new ArrayList<chart> ();
	 slider=new ColorSlider(this);
		slider.colors.add(color(255, 0, 0,80));
		slider.colors.add(color(255, 255, 0,80));
		slider.colors.add(color(0, 255, 255,80));		
		slider.colors.add(color(0, 0, 255,80));	
		slider.colors.add(color(255, 0, 255,80));	
		slider.colors.add(color(255, 0, 0,80));	 
float R=(float)height;
font = createFont("simhei", 300, true);
textureMode(NORMAL);	
for(int i=0;i<60;i++){
chart chart1=new chart(this,"²âÊÔ·½¿é",font,120,R,PI/30*i,PI/30);

chart chart2=new chart(this,"T"+i+"A",font,120,R-120,PI/30*i,PI/60);
chart chart3=new chart(this,"T"+i+"B",font,120,R-120,PI/30*i+PI/60,PI/60);
chart1.color=slider.getGradient((float)i/60);
chart1.ComputeEdge(3f);
chart2.color=slider.getGradient((float)i/60);
chart2.ComputeEdge(3f);
chart3.color=slider.getGradient((float)(i+0.5f)/60);
chart3.ComputeEdge(3f);
charts.add(chart1);
charts.add(chart2);
charts.add(chart3);
}
}
public void readText(String name){
	
}
public void draw(){
	background(255);
	OnXform.DrawDarkGrid(this);
	for(int i=0;i<charts.size();i++){
	charts.get(i).draw();	
	}
}

public class chart{
	private float hei=0;
	private float r=0;
	private float rot=0;
	private float range=0;
  
	private OnMesh m_mesh;
	private MeshTopologyEdgeList el;
	private int color;
	private  PImage G;
	PApplet p;
	On3dPoint pos1,pos2,pos3,pos4;
public chart(PApplet parent,String text,PFont font,	
		float Hei,float R,float Rot,float Range){
	this.p=parent;
    hei=Hei; r=R;rot=Rot;range=Range;
	m_mesh=new OnMesh();
	color=p.color(255);
	initializePic(100,text,font);
}
 void initializePic(int letterSize,String str,PFont font){	
	//println(str.length());
	   PGraphics  g = p.createGraphics(letterSize* str.length(), letterSize, JAVA2D);     
		  g.beginDraw();
		  g.background(color(255, 255, 255,0));
		  g.fill(0);
		  g.textAlign(CENTER, CENTER);
		  g.textFont(font);
		  g.textSize(letterSize);
		  g.text(str, letterSize* str.length()/2,(int)((float)letterSize/2.5));
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
	this.m_mesh.Transform(OnXform.translation(On3dVector.Zaxis()));
	el=m_mesh.TopologyEdgeList();
	On3dPoint Pos1=pl1.get(pl1.size()/2);
	On3dPoint Pos2=pl2.get(pl1.size()/2);
	On3dVector v=On3dPoint.PointSub(Pos2, Pos1);
	On3dVector v2=On3dVector.OnCrossProduct(v, On3dVector.Zaxis());
	v2.Unitize();v2.mul(10);v.Unitize();v.mul(10f*G.width/G.height);
	On3dPoint cen=On3dPoint.PointAdd(Pos1,Pos2);cen.mul(0.5f);
	Pos2=On3dPoint.PointAdd(cen, v);
	v.Reverse();
	Pos1=On3dPoint.PointAdd(cen, v);
	
	pos1=On3dPoint.PointAdd(Pos1, v2);
	pos2=On3dPoint.PointAdd(Pos2, v2);
	v2.Reverse();
	pos3=On3dPoint.PointAdd(Pos2, v2);
	pos4=On3dPoint.PointAdd(Pos1, v2);
	//PApplet.println("pos1:"+pos1.x+"/"+pos1.y+"/"+pos1.z);
	//PApplet.println("pos2:"+pos2.x+"/"+pos2.y+"/"+pos2.z);
	//PApplet.println("pos3:"+pos3.x+"/"+pos3.y+"/"+pos3.z);
	//PApplet.println("pos4:"+pos4.x+"/"+pos4.y+"/"+pos4.z);
}
public void draw(){
	p.noStroke();//p.stroke(0,60);
    p.fill(this.color);
	//m_mesh.draw(p);	
	p.pushMatrix();
	p.beginShape();
	texture(G);
	vertex(pos1.x, pos1.y, pos1.z+1, 0,0);
	vertex(pos2.x, pos2.y, pos2.z+1, 1,0);
	vertex(pos3.x, pos3.y, pos3.z+1, 1,1);
	vertex(pos4.x, pos4.y, pos4.z+1, 0,1);
	p.endShape();
	p.popMatrix();
	
	p.stroke(p.color(this.color,60));
	el.drawProfile(p);
}

}
}
///////////////////////
/*fhfyjngkm
*/