package GeoTools;

import java.util.ArrayList;

public class MeshEdge {
public int p1;
public int p2;
public ArrayList<Integer> faces;
public MeshEdge(int v1,int v2){
if(v1<=v2){p1=v1;p2=v2;	}
else{p1=v2;p2=v1;}		
faces=new ArrayList<Integer>();
}
public MeshEdge(int v1,int v2,int index){
if(v1<=v2){p1=v1;p2=v2;	}
else{p1=v2;p2=v1;}		
faces=new ArrayList<Integer>();
faces.add(index);
}
public MeshEdge(int v1,int v2,ArrayList<Integer> index){
if(v1<=v2){p1=v1;p2=v2;	}
else{p1=v2;p2=v1;}		
faces=new ArrayList<Integer>();
faces.addAll(index);
}
public MeshEdge(int v1,int v2,Integer[] index){
if(v1<=v2){p1=v1;p2=v2;	}
else{p1=v2;p2=v1;}		
faces=new ArrayList<Integer>();
for(int i=0;i<index.length;i++){
	faces.add(index[i]);
}
}
public void addFace(int f){
	this.faces.add(f);
}
public void removeDumpFace(){
	if(faces==null)return;
	if (faces.size()<1)return;
	ArrayList<Integer> fs=new ArrayList<Integer>();
	fs.add(faces.get(0));
	if(faces.size()>1){
	for(int i=1;i<faces.size();i++){
boolean sign=true; 
for(int j=0;(j<fs.size());j++){
if	(fs.get(j)==faces.get(i)){sign=false;break;}
}
	if(sign)fs.add(faces.get(i));
	}
	}
	this.faces=new ArrayList<Integer>(fs);
}
public boolean equalTo(MeshEdge e){
	if((p1==e.p1)&&(p2==e.p2)){return true;}else{return false;}	
}
}
