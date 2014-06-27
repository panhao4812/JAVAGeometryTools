package modelTest;
import java.awt.Frame;
import controlP5.ControlP5;
import GeoTools.*;
import peasy.PeasyCam;
import processing.core.PApplet;

public class test extends PApplet {
	private static final long serialVersionUID = 1L;
	PeasyCam cam;
	OnMesh amesh,amesh2,amesh3,amesh4;
	ColorSlider slider;
	fireImport iprt;
	float Range=0,Dir=0.5f;
	ControlFrame cf;
	On3dVector dir;
	MeshTopologyEdgeList el1;
	float minZ=PI,maxZ=0;float range2=3;float Dir2=3;
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
		 amesh4=new OnMesh();
		size(600, 400, OPENGL);
		new ControlP5(this);
	    cf=addControlFrame("range",190,80);
	
		ambientLight(148, 148, 148);
		lightSpecular(230, 230, 230);
		directionalLight(255, 255, 255, 0, -0.5f, -1);
		specular(255, 255, 255);
	    shininess(16.0f);// */
	    smooth();
	   amesh=iprt.readobjFile("land1.obj");
	  //  amesh2=iprt.read3dsFile("t2.3ds");
	//	amesh=iprt.read3dsFile("rabbit.3ds");	             
	//	amesh.Append(iprt.read3dsFile("rabbit2.3ds"));	
	//	amesh.Append(iprt.read3dsFile("rabbit3.3ds"));	
	//.Append(iprt.read3dsFile("rabbit4.3ds"));	
		
		amesh.CombineIdenticalVertices();
		OnXform xf=new OnXform();
		xf.Scale(new On3dPoint(0,0,0),4             );
	    amesh.Transform(xf);amesh2.Transform(xf);amesh3.Transform(xf);
	    amesh.ComputeVertexNormals();
	    amesh.colors=new int[amesh.VertexCount()];
	/*  caculate();			
	for(int i=0;i<amesh4.FaceCount();i++){
	    	println(amesh4.faces.get(i).a+","+amesh4.faces.get(i).b+","+amesh4.faces.get(i).c);
	    }
	  for(int i=0;i<amesh4.VertexCount();i++){
	    	println(amesh4.Points.get(i).x+","+amesh4.Points.get(i).y+","+amesh4.Points.get(i).z);
	    }	
	    */ 
	    caculate();
	}
	float[] t;
	public void caculate(){
		if(Range!=range2 ){
		   minZ=Float.MAX_VALUE;maxZ=0;	
		    PerlinNoise noise=new PerlinNoise();
			t=new float[amesh.normals.length];
			for(int i=0;i<amesh.normals.length;i++){				
				float x=amesh.Points.get(i).x*Range;
				float y=amesh.Points.get(i).y*Range;
				float z=amesh.Points.get(i).z*Range;
				
				t[i]=sin(26*atan2(x,y)) +sin(26*atan2(y,z)) +sin(26*atan2(z,x));
				//t[i]=pow(sin(5*x),2) + pow(cos(5*y),2) +pow(cos(5*z),2)- 1.3f;
		/*
		 * 
				Cnd:  sin(16*atan2(x,y)+2*sin(2*z))>0 
				Cnd:  sin(26*atan2(x,y)) +sin(26*atan2(y,z)) +sin(26*atan2(z,x))>0 		
			*/	
				if(minZ>t[i])minZ=t[i];
				if(maxZ<t[i])maxZ=t[i];
			}
			println(maxZ);
			println(minZ);
		}
		/* minZ=PI;maxZ=0;	 dir=On3dVector.Xaxis();
			dir.Rotate(Range);
		
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
			*/

	
		OnMesh[] M= MeshCreation.followlines2(amesh,amesh3,amesh4 ,t,Dir*(maxZ-minZ)+minZ);
		amesh3=M[0];amesh4=M[1];
		el1=amesh3.TopologyEdgeList();
           // print("m3 "+ amesh3.VertexCount());
           // print("m4 "+ amesh4.VertexCount());
		    //return new OnMesh();
	}
	
	public void draw() {
		background(230);
		if((Range!=range2 )||(Dir!=Dir2)){ 
			caculate();
			range2=Range;
			Dir2=Dir;
		}
		OnXform.DrawDarkGrid(this);
    if (amesh.VertexCount()>0){
    
   
    	//
    	//amesh.draw(this,1);   	
    stroke(0,20);
    	fill(255,255,255);
     	//amesh2.draw(this,1);
    	noStroke();
     	//stroke(0,40);
     	fill(145,200,65);
    	amesh3.draw(this,1);  
    	fill(90,170,125);
       if(keyPressed)  amesh4.draw(this,1);
       stroke(20,20,20);
       el1.drawProfile(this);
    }   
	}

	// /////////////////////////////////////////////////////////////
}