package GeoTools;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public  class  fireImport {
	PApplet p;
public fireImport(PApplet parent){p=parent;}
public	int read4(  DataInputStream objBinaryReader){  
		int a=0,b=0,c=0,d=0;
		try {		
			a=objBinaryReader.readUnsignedByte();	
			b=objBinaryReader.readUnsignedByte();	
			c=objBinaryReader.readUnsignedByte();
			d=objBinaryReader.readUnsignedByte();
		//	print(a+"/");print(b+"/");print(c+"/");print(d+"|");	
		} catch (IOException e) {		
			Count=4;
		}return d*16*16*16*16*16*16+c*16*16*16*16+b*16*16+a;
	}
public	int read2(  DataInputStream objBinaryReader){  		
		int a=0,b=0 ;
		try {			
			a=objBinaryReader.readUnsignedByte();	
			b=objBinaryReader.readUnsignedByte();		
			//print(a+"/");print(b+"|");	
		} catch (IOException e) {
		Count=2;
		}return b*16*16+a;
	}
public	float readFloat( DataInputStream objBinaryReader){
		byte[]b=new byte[4];
		try {
			objBinaryReader.read(b);
		} catch (IOException e) {
			Count=3;
		}
		 ByteBuffer bb = ByteBuffer.allocateDirect(4); 
		 bb=bb.order(ByteOrder.LITTLE_ENDIAN);
	      bb.put(b); 
	      bb.rewind();
	      return bb.getFloat();	
}
	int Count=0;
	@SuppressWarnings("static-access")
	public	OnMesh read3dsFile(String fileName){		
	OnMesh amesh=new OnMesh();
		 Count=0;
		try {
		File file = new File(fileName);
        FileInputStream in = new FileInputStream(file);
        DataInputStream objBinaryReader=new DataInputStream(in);  
       
        while (Count==0)    	
        {
        int str2 = read2(objBinaryReader);
        int str4 = read4(objBinaryReader);    
        if(str2 == 0x4D4D ){ //println("0x4D4D");println(str4+ "/");      	
        }
        else if(str2 == 0x3D3D ){ //println("0x3D3D");
        	}
        else if(str2 == 0x4000){// println("0x4000");
        char c ;int i = 0;String ss = "";
        do{
        c = (char) objBinaryReader.readUnsignedByte();
        ss += c;
        i++;
        }while(c != '\0' && i < 20);
      p.println(ss.toString());
      //  println("------");
        }
        else if(str2 == 0x4100){// println("0x4100"); 
        	}
        else if(str2 == 0x4110 ){// println("0x4110");
        int str1 =  read2(objBinaryReader);
        for(int i = 0;i < str1;i++){
        //println("point" + i + ":");
        float s1 = readFloat (objBinaryReader);
      //  println(s1);
        float s2 = readFloat (objBinaryReader);
      //  println(s2);
        float  s3 = readFloat (objBinaryReader);
      //  println(s3);
        
     
    	//println("minZ"+minZ);
		//println("maxZ"+maxZ);
       amesh.SetVertex(new On3dPoint(s1,s2,s3));
        }
        //println("------");  
        }
        else if(str2 == 0x4120){ //println("0x4120");
        int str1 =  read2(objBinaryReader);   
        for(int i = 0;i <str1;i++){
       // println("face" + i + ":");
        int s1 = read2(objBinaryReader);//println(s1);
        int  s2 = read2(objBinaryReader);//println(s2);
        int  s3 =  read2(objBinaryReader);// println(s3);
        read2(objBinaryReader);// println(s.ToString());
        amesh.SetTriangle(s1,s2,s3);
        }
      //  println("------");
        }
             else {
      // println(str2);
      // println(str4);
     objBinaryReader.skipBytes(str4 - 6);
        }
        }
        
		 }catch (FileNotFoundException e) {		  
			p. println(e.toString());
			   Count=-1;
			  } catch (IOException e){
				 p. println(e.toString());
				  Count=-2;
			  } 
			  return amesh;
	}
	@SuppressWarnings({ "resource", "static-access" })
	public ArrayList<PVector> readGCodeFile(String fileName){
		ArrayList<PVector>  pts=new 	ArrayList<PVector>  ();
		float lx = 0;
		float ly = 0;
		float lz = 0;
		 try {  
			  FileReader read = new FileReader(fileName);
			   BufferedReader br = new BufferedReader(read);
			   String row="";
			   int blank=0;			
			   while(blank<10){			  		 
				   row = br.readLine();
				   if(row!=null){			  
					 //  blank=0;
			   String[] p=row.split(" ");
			if(p.length>0){
			if(p[0].equals("G0")||p[0].equals("G1")){
			if(p.length>1){ 
				float X=lx,Y=ly,Z=lz;
				for(int i=1;i<p.length;i++){
					 String[] xx=p[i].split("X");
					if(xx.length==2)X=Float.parseFloat(xx[1]);
					 String[] yy=p[i].split("Y");
						if(yy.length==2)Y=Float.parseFloat(yy[1]);
						 String[] zz=p[i].split("Z");
							if(zz.length==2)Z+=0.2f;//Float.parseFloat(zz[1]);	
							lx=X;ly=Y;lz=Z;
			}
				pts.add(new PVector(X,Y,Z));			
			}
			}
			}
			   }else{
				   blank++;	
				   p.println("blanklines");
			   }
			   }
			  }catch (FileNotFoundException e) {				  
				   p.println(e.toString());
				  } catch (IOException e){
					  p.println(e.toString());
				  } 
		 return pts;
	}
	public OnMesh readobjFile(String fileName){
	OnMesh amesh=new OnMesh();
		try {  
			  FileReader read = new FileReader(fileName);
			   @SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(read);
			   String row="";
			   int blank=0;			
			   while(blank<10){			  		 
				   row = br.readLine();
				   if(row!=null){			  
					  blank=0;
			  String[] p=row.split(" ");
			if(p.length>0){if(p.length>=4){ 
		if(p[0].equals("v")){			
			 float xx=Float.parseFloat(p[1]);
			 float yy=Float.parseFloat(p[2]);
			 float zz=Float.parseFloat(p[3]);
			 amesh.Points.add(new On3dPoint(xx,yy,zz));			
		}
		if(p[0].equals("f")){
			 int p1=Integer.parseInt(p[1].split("/")[0]);
			 int p2=Integer.parseInt(p[2].split("/")[0]);
			 int p3=Integer.parseInt(p[3].split("/")[0]);
			 int p4=p3;			
			if(p.length==5){
			 p4=Integer.parseInt(p[4].split("/")[0]);
	     	}
			amesh.faces.add(new MeshFace(p1-1,p2-1,p3-1,p4-1));	
		}		
			}
			}
			   }else{
				   blank++;	
				   PApplet.println("blanklines");
			   }
			   }
			  }catch (FileNotFoundException e) {				  
				  PApplet.println(e.toString());
				  } catch (IOException e){
					  PApplet.println(e.toString());
				  } 	
		return amesh;
	}
}
