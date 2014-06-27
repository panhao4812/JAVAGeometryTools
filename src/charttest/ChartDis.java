package charttest;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

font = createFont("simhei", 300, true);
textureMode(NORMAL);	



readText("bool1.csv");CombineCharts();
for(int i=0;i<charts.size();i++){
	charts.get(i).initializePic(font);
	charts.get(i).ComputeEdge(3f);	
	}
}
public void CombineCharts(){
	ArrayList<chart> charts2=new ArrayList<chart>();
	charts2.add(charts.get(0));
	for(int i=1;i<charts.size();i++){
		boolean sign=true;
		println("i:"+i+" id:"+charts.get(i).text);
	for(int j=charts2.size()-1;j>=0;j--){
		println("j:"+j+" id:"+charts2.get(j).text);
		if(charts2.get(j).CombineHorizontalCharts(charts.get(i))){
			sign=false;break;
		}
	}if(sign)charts2.add(charts.get(i));
//	println(sign);
	}
	
	charts=charts2;
}

public void readText(String fileName){
	ArrayList<String>pts=new ArrayList<String>();
	 try {  
		  FileReader read = new FileReader(fileName);
		   BufferedReader br = new BufferedReader(read);
		   String row="";
		   int blank=0;			
		   while(blank<10){			  		 
			   row = br.readLine();
			   if(row!=null){			  
				  blank=0;pts.add(row);			
		      }else{
			   blank++;	println("blanklines:"+blank);
		   }
		   }
	  }catch (FileNotFoundException e) {				  
		   println(e.toString());
		  } catch (IOException e){
			  println(e.toString());
		  }
	 //////////////
	 float R=(float)height;
String[][] str=new String[pts.size()][];
	 for(int i=0;i<pts.size();i++){
		 str[i]= pts.get(i).split(",",-1);	 
		 println(str[i].length);
	 }	
	for(int i=0;i<pts.size();i++){ 
	     if (str[i][0].equals("class")){
	    	 for(int j=1;j<str[i].length;j++){
	    		 if(str[i][j].equals("")){
	    			 str[i][j]=str[i][j-1];
	    		 }    	    
	    		 chart c=new chart(this,str[i][j]);
	    c.hei=100f;c.r=R-100f*i;
	    float t=str[0].length-1;
	    c.rot=(float)j/t*PI*2;
	    c.range=1f/t*PI*2;
	    charts.add(c); 
	    	 }
	     }
	}
}
public void draw(){
	background(255);
	OnXform.DrawDarkGrid(this);
	for(int i=0;i<charts.size();i++){
	charts.get(i).draw();	
	}
}

public class chart{
	public float hei=0;
	public float r=0;
	public float rot=0;
	public float range=0;
    public String text="";
    public int letterSize=100;
    
	private OnMesh m_mesh;
	private MeshTopologyEdgeList el;
	private int color;
	private  PImage G;
	PApplet p;
	On3dPoint pos1,pos2,pos3,pos4;
	public 	chart(PApplet parent,String Text){
		this.p=parent;text=Text;
		m_mesh=new OnMesh();
		color=p.color(255);
	}
public chart(PApplet parent,String Text,	
		float Hei,float R,float Rot,float Range){
	this.p=parent;text=Text;  
    hei=Hei; r=R;rot=Rot;range=Range; 
	m_mesh=new OnMesh();
	color=p.color(255);
}
public void initializePic(PFont font){	
	//println(str.length());
	   PGraphics  g = p.createGraphics(letterSize* text.length()/2, letterSize, JAVA2D);     
		  g.beginDraw();
		  g.background(color(255,0));
		  g.fill(0);
		  g.textAlign(CENTER, CENTER);
		  g.textFont(font);
		  g.textSize(letterSize);
		  g.text(text, g.width/2,g.height/2.5f);
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
	m_mesh.draw(p);	
	p.pushMatrix();
	p.beginShape();
	texture(G);
	vertex(pos1.x, pos1.y, pos1.z+1, 0,0);
	vertex(pos2.x, pos2.y, pos2.z+1, 1,0);
	vertex(pos3.x, pos3.y, pos3.z+1, 1,1);
	vertex(pos4.x, pos4.y, pos4.z+1, 0,1);
	p.endShape();
	p.popMatrix();
	
	p.stroke(p.color(0));
	el.drawProfile(p);
}
public final boolean CombineHorizontalCharts(chart C){
	if(this.r==C.r && this.hei==C.hei && this.text.equals(C.text)){
this.range+=C.range;
if(C.rot<rot)rot=C.rot;
return true;}
return false;
}
public final boolean CombineVerticalCharts(chart C){
	if(this.range==C.range && this.rot==C.rot && this.text.equals(C.text)){
		this.hei+=C.hei;if(C.r<r)r=C.r;
		return true;}
		return false;
}
}
///////
}
///////////////////////
/*²âÊÔ2
*/