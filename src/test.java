
import java.awt.Frame;
import controlP5.ControlP5;
import GeoTools.*;
import peasy.PeasyCam;
import processing.core.PApplet;
public class test extends PApplet {
	private static final long serialVersionUID = 1L;
	PeasyCam cam;
	OnMesh amesh,amesh2,amesh3;
	ColorSlider slider;
	fireImport iprt;
	float Range=0;
	private ControlP5 cp5;ControlFrame cf;
	On3dVector dir;
	ControlFrame addControlFrame(String theName, int theWidth, int theHeight) {
		  Frame f = new Frame(theName);
		  ControlFrame p = new ControlFrame(this, theWidth, theHeight);
		  f.add(p);
		  p.init();
		  f.setTitle(theName);
		  f.setSize(p.w, p.h);
		  f.setLocation(100, 100);
		  f.setResizable(false);
		  f.setVisible(true);
		  return p;
		}
	public void setup() {
		slider=new ColorSlider(this);
		iprt=new fireImport(this);
		slider.colors.add(color(255, 20, 20));
		slider.colors.add(color(255, 255, 50));
		slider.colors.add(color(20, 255, 20));
	  
			
		cam = new PeasyCam(this, 200);
		cam.setMinimumDistance(50);
		cam.setMaximumDistance(5000);		
		 amesh=new OnMesh();
		 amesh2=new OnMesh();
		 amesh3=new OnMesh();
		size(600, 400, OPENGL);
		cp5 = new ControlP5(this);
	  cf=addControlFrame("range",190,80);
	

	//	pts=iprt.readGCodeFile("C:/Users/Administrator/Desktop/PrintSTL/h1.gcode");
		//println(pts.size());
		// noLoop();
		 dir=On3dVector.Xaxis();
	
		ambientLight(148, 148, 148);
		lightSpecular(230, 230, 230);
		directionalLight(255, 255, 255, 0, -0.5f, -1);
		specular(255, 255, 255);
	    shininess(16.0f);// */
	    smooth();
	    amesh3=iprt.read3dsFile("land3.3ds");
	    amesh2=iprt.read3dsFile("land2.3ds");
		amesh=iprt.read3dsFile("land1.3ds");
		
		amesh.CombineIdenticalVertices();
		OnXform xf=new OnXform();
		xf.Scale(new On3dPoint(-2,0,0),5);
	    amesh.Transform(xf);amesh2.Transform(xf);amesh3.Transform(xf);
	    amesh.ComputeVertexNormals();
	    amesh.colors=new int[amesh.VertexCount()];
	    
	    caculate();
	}
	public void caculate(){
		 minZ=PI;maxZ=0;
			dir.Rotate(Range);
			float[] t=new float[amesh.normals.length];
			for(int i=0;i<amesh.normals.length;i++){
				On3dVector v=amesh.normals[i];	
				t[i]=(float)On3dVector.VectorAngle(v,dir);
				if(minZ>t[i])minZ=t[i];
				if(maxZ<t[i])maxZ=t[i];
			}
			for(int i=0;i<amesh.normals.length;i++){
			amesh.colors[i]=slider.getGradient((t[i]-minZ)/(maxZ-minZ));	
			}
			println("max:"+maxZ);	println("min:"+minZ);	
	}
	float minZ=PI,maxZ=0;float range2=0;
	public void draw() {
		background(30);
		if(Range!=range2)caculate();
			range2=Range;
		OnXform.DrawWhiteGrid(this);
    if (amesh.VertexCount()>0){
    	stroke(0,20);
    	//noStroke();
    	amesh.draw(this,1);
    	fill(255);stroke(0,10);
    	amesh2.draw(this,1);	
    	amesh3.draw(this,1);
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