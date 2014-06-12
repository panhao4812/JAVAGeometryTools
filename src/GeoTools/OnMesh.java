package GeoTools;

import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PConstants;
public class OnMesh {	
public	ArrayList<On3dPoint> Points=new ArrayList<On3dPoint>();
public	ArrayList<MeshFace> faces=new ArrayList<MeshFace>();
public	int[] colors;
public	On3dVector[] normals;

public OnMesh(){	
}
public OnMesh(OnMesh mesh){	
	this.Points.clear();
	for (int i=0;i<mesh.Points.size();i++){
		Points.add(new On3dPoint(mesh.Points.get(i)));
	}
	this.faces.clear();
	for (int i=0;i<mesh.faces.size();i++){
		faces.add(new MeshFace(mesh.faces.get(i).a,mesh.faces.get(i).b,mesh.faces.get(i).c,mesh.faces.get(i).d));
	}

}
public int FaceCount(){
	return faces.size();
}
public int VertexCount(){
	return Points.size();
}
public void SetVertex(int index,On3dPoint P){
	Points.add(index, P);
}
public void SetVertex(On3dPoint P){
	Points.add(P);
}
public void SetQuad(int index,int p1,int p2,int p3,int p4){
	faces.add(index, new MeshFace(p1,p2,p3,p4));
}
public void SetQuad(int p1, int p2, int p3,int p4) {
	faces.add( new MeshFace(p1,p2,p3,p4));
}
public void SetTriangle(int index, int p1, int p2, int p3) {
	faces.add(index, new MeshFace(p1,p2,p3));
}
public void SetTriangle(int p1, int p2, int p3) {
	faces.add( new MeshFace(p1,p2,p3));
}
public void Transform(OnXform xform ){
	for(int i = 0;i < Points.size(); i ++){
On3dPoint p=(On3dPoint)Points.get(i);
p.Transform(xform);
Points.set(i, p);
}
}
public On3dVector ONTriangleNormal(On3dPoint A,On3dPoint B,On3dPoint C)
{
  // N = normal to triangle's plane
	On3dVector N=new On3dVector();
  double a, b, c, d;
  N.x = A.y*(B.z-C.z) + B.y*(C.z-A.z) + C.y*(A.z-B.z);
  N.y = A.z*(B.x-C.x) + B.z*(C.x-A.x) + C.z*(A.x-B.x);
  N.z = A.x*(B.y-C.y) + B.x*(C.y-A.y) + C.x*(A.y-B.y);

  a = Math.abs(N.x);
  b = Math.abs(N.y);
  c = Math.abs(N.z);
  if ( b > a )
  {
    if ( c > b )
    {
      // c is largest
      if ( c >Float.MIN_VALUE)
      {
        a /= c; b /= c; d = c*Math.sqrt(1.0 + a*a + b*b);
      }
      else
      {
        d = c;
      }
    }
    else
    {
      if ( b > Float.MIN_VALUE )
      {
        // b is largest
        a /= b; c /= b; d = b*Math.sqrt(1.0 + c*c + a*a);
      }
      else
      {
        d = b;
      }
    }
  }
  else if ( c > a )
  {
    // c is largest
    if ( c >Float.MIN_VALUE )
    {
      a /= c; b /= c; d = c*Math.sqrt(1.0 + a*a + b*b);
    }
    else
    {
      d = c;
    }
  }
  else if ( a > Float.MIN_VALUE )
  {
    // a is largest
    b /= a; c /= a; d = a*Math.sqrt(1.0 + b*b + c*c);
  }
  else
  {
    d = a;
  }
  if ( d > 0.0 )
  {
    N.x /= d; N.y /= d; N.z /= d;
  }
  return N;
}
public void CombineIdenticalVertices(){
	ArrayList<On3dPoint> pn =new ArrayList<On3dPoint>();
	int arrn[] =new int[Points.size()];
     pn.add(Points.get(0));
    arrn[0] = 0;
     for (int i =1; i< Points.size() ;i++){
     boolean sign  = true;
     for (int  j=pn.size() - 1;j>=0;j--){ 
         if (Points.get(i).DistanceTo(pn.get(j)) < 0.00001 ){
           arrn[i] = j;
           sign = false;
         break;
         }
}
       if (sign ){ pn.add(Points.get(i));arrn[i] = pn.size() - 1;}
}
this.Points=pn;
for(int i=0;i<faces.size();i++){
	faces.get(i).a=arrn[faces.get(i).a];
	faces.get(i).b=arrn[faces.get(i).b];
	faces.get(i).c=arrn[faces.get(i).c];
	faces.get(i).d=arrn[faces.get(i).d];
}
}
public void ComputeVertexNormals()
{
   int fcount = FaceCount();
   int vcount = VertexCount();
  int fi;
  On3dVector[]  m_F=new   On3dVector[fcount];	
  if ( fcount > 0 && vcount > 0 ) {
	  m_F= ComputeFaceNormals();
  }
   normals = new On3dVector[vcount];
      for ( fi = 0; fi < vcount; fi++ ) {
    	  normals[fi]=new On3dVector();
      }
      for ( fi = 0; fi < fcount; fi++ ) {
        MeshFace f = (MeshFace)faces.get(fi);    
          normals[f.a].add(m_F[fi]);
          normals[f.b].add(m_F[fi]);
          normals[f.c].add(m_F[fi]);
          if ( f.isquad() )normals[f.d].add(m_F[fi]);       
      }
      for ( fi = 0; fi < vcount; fi++ ) {
    	  normals[fi].Unitize();
      }   
}

public On3dVector[]  ComputeFaceNormals()
{
   int fcount = FaceCount();
   On3dVector[] facenormals=new On3dVector[fcount];		   
  if ( fcount > 0 )
  {
    On3dVector a, b, n;       
      for (int fi = 0; fi < fcount; fi++ ) {
        a =On3dPoint.PointSub(Points.get(faces.get(fi).c) , Points.get(faces.get(fi).a));
        b =On3dPoint.PointSub(Points.get(faces.get(fi).d) , Points.get(faces.get(fi).b));
        n =On3dVector.OnCrossProduct( a, b ); // works for triangles, quads, and nonplanar quads
        n.Unitize();
        facenormals[fi]=n;
      }
  }
  return facenormals;
}
public On3dPoint[] faceCenter()
{
	On3dPoint[]cen=new On3dPoint[this.FaceCount()];
	if(this.FaceCount()>0){
	for(int i=0;i<cen.length;i++){	
		  MeshFace f = (MeshFace)faces.get(i);    
		cen[i]=new On3dPoint();
		cen[i].add(this.Points.get(f.a));
		cen[i].add(this.Points.get(f.b));
		cen[i].add(this.Points.get(f.c));
		if(f.isquad()){cen[i].add(this.Points.get(f.d));
		cen[i].mul(0.25f);}else{cen[i].mul(1f/3f);}
		}	
	}return cen;
}
public void drawFaceNormals(PApplet p,float t){
	On3dVector[]  m_F=ComputeFaceNormals() ;
	 On3dPoint[]  m_cen=faceCenter();
	 p.pushMatrix();
		for (int i = 1; i < m_F.length; i++) {
//p.println(m_cen[0].x+"/"+m_cen[0].y+"/"+m_cen[0].z);		
			On3dPoint v1 = m_cen[i];m_F[i].mul(t);
			On3dPoint v2 = On3dPoint.PointAdd(v1,m_F[i]);			
			p.beginShape(PConstants.LINES);
			p.vertex(v1.x, v1.y, v1.z);
			p.vertex(v2.x, v2.y, v2.z);
			p.endShape();	
		}	
		p.popMatrix();
}
public void drawNormals(PApplet p,float t){
	ComputeVertexNormals();
	 p.pushMatrix();
		for (int i = 1; i < this.normals.length; i++) {	
			On3dPoint v1 = Points.get(i);normals[i].mul(t);
			On3dPoint v2 = On3dPoint.PointAdd(Points.get(i),normals[i]);	
		//p.println(Points.get(0).x+"/"+Points.get(0).y+"/"+Points.get(0).z);			
			p.beginShape(PConstants.LINES);
			p.vertex(v1.x, v1.y, v1.z);
			p.vertex(v2.x, v2.y, v2.z);
			p.endShape();	
		}	
		p.popMatrix();
}
public void draw(PApplet p){draw(p,1);}
public void draw(PApplet p,float t){
	if(colors!=null){
	if (this.colors.length==this.VertexCount()){show( p,this.colors,t);return;}
	}
	show(p,t);
}
 private void show(PApplet p,float t){
	p.pushMatrix();
	p.scale(t);
	p.beginShape(PConstants.TRIANGLES);
	for(int i=0;i<faces.size();i++){
		MeshFace f=(MeshFace)faces.get(i);
		if(f.istriangle()){
		p.vertex(Points.get(f.a).x,Points.get(f.a).y,Points.get(f.a).z);
		p.vertex(Points.get(f.b).x,Points.get(f.b).y,Points.get(f.b).z);
		p.vertex(Points.get(f.c).x,Points.get(f.c).y,Points.get(f.c).z);
	}	
	}
	p.endShape();
	p.beginShape(PConstants.QUADS);
	for(int i=0;i<faces.size();i++){
		MeshFace f=(MeshFace)faces.get(i);
		if(f.isquad()){
	p.vertex(Points.get(f.a).x,Points.get(f.a).y,Points.get(f.a).z);
	p.vertex(Points.get(f.b).x,Points.get(f.b).y,Points.get(f.b).z);
	p.vertex(Points.get(f.c).x,Points.get(f.c).y,Points.get(f.c).z);
	p.vertex(Points.get(f.d).x,Points.get(f.d).y,Points.get(f.d).z);
		}
	}
	p.endShape();
	p.popMatrix();
}
private void show(PApplet p,int[] c,float t){
	p.pushMatrix();p.scale(t);
	p.beginShape(PConstants.TRIANGLES);
	for(int i=0;i<faces.size();i++){
		MeshFace f=(MeshFace)faces.get(i);		
		p.fill(c[f.a]);	p.vertex(Points.get(f.a).x,Points.get(f.a).y,Points.get(f.a).z);
		p.fill(c[f.b]);	p.vertex(Points.get(f.b).x,Points.get(f.b).y,Points.get(f.b).z);
		p.fill(c[f.c]);	p.vertex(Points.get(f.c).x,Points.get(f.c).y,Points.get(f.c).z);
	}	
	p.endShape();
	p.beginShape(PConstants.QUADS);
	for(int i=0;i<faces.size();i++){
		MeshFace f=(MeshFace)faces.get(i);
	p.fill(c[f.a]);p.vertex(Points.get(f.a).x,Points.get(f.a).y,Points.get(f.a).z);
	p.fill(c[f.b]);p.vertex(Points.get(f.b).x,Points.get(f.b).y,Points.get(f.b).z);
	p.fill(c[f.c]);p.vertex(Points.get(f.c).x,Points.get(f.c).y,Points.get(f.c).z);
	p.fill(c[f.d]);p.vertex(Points.get(f.d).x,Points.get(f.d).y,Points.get(f.d).z);
	}
	p.endShape();
	p.popMatrix();
}


public static OnMesh purlins(ArrayList<On3dPoint> pl,float r){
	ArrayList< ArrayList<On3dPoint>> pll =new ArrayList< ArrayList<On3dPoint>>() ;
    for( int i =0; i< pl.size();i++){
    	ArrayList<On3dPoint> pts =new ArrayList<On3dPoint>();
      pts.add(new On3dPoint(pl.get(i).x + 1.732 * r / 2, pl.get(i).y, pl.get(i).z + 1.5 * r));
      pts.add(new On3dPoint(pl.get(i).x + r, pl.get(i).y, pl.get(i).z + 1 * r));
      pts.add(new On3dPoint(pl.get(i).x + 1.414 * r / 2, pl.get(i).y, pl.get(i).z + (1 - 1.414 / 2) * r));
      pts.add(new On3dPoint(pl.get(i)));
      pts.add(new On3dPoint(pl.get(i).x - 1.414 * r / 2, pl.get(i).y, pl.get(i).z + (1 - 1.414 / 2) * r));
      pts.add(new On3dPoint(pl.get(i).x - r, pl.get(i).y, pl.get(i).z + 1 * r));
      pts.add(new On3dPoint(pl.get(i).x - 1.732 * r / 2, pl.get(i).y, pl.get(i).z + 1.5 * r));
      pts.add(new On3dPoint(pl.get(i).x + 1.732 * r / 2, pl.get(i).y, pl.get(i).z + 1.5 * r));
      pll.add(pts);
    }
    return loft2Mesh(pll);
}
public static OnMesh column(On3dPoint p1, On3dPoint p2, float radius){
	On3dVector v0  = On3dPoint.PointSub(p2,p1);
	On3dVector v = new On3dVector(0, v0.Length(), 0);
	On3dPoint v1 = new On3dPoint(0, 0, radius);
	On3dPoint v2 = new On3dPoint(radius * Math.sqrt(2) / 2, 0, radius * Math.sqrt(2) / 2);
	On3dPoint v3 = new On3dPoint(radius, 0, 0);
	On3dPoint v4 = new On3dPoint(radius * Math.sqrt(2) / 2, 0, -radius * Math.sqrt(2) / 2);
	On3dPoint v5 = new On3dPoint(0, 0, -radius);
	On3dPoint v6 = new On3dPoint(-radius * Math.sqrt(2) / 2, 0, -radius * Math.sqrt(2) / 2);
	On3dPoint v7 = new On3dPoint(-radius, 0, 0);
	On3dPoint v8 = new On3dPoint(-radius * Math.sqrt(2) / 2, 0, radius * Math.sqrt(2) / 2);
	On3dPoint vc =new On3dPoint(); 
	vc.add(v1);vc.add(v2);vc.add(v3);vc.add(v4);vc.add(v5);vc.add(v6);vc.add(v7);vc.add(v8);
	vc.div(8);
	OnMesh mesh = new OnMesh();
	mesh.SetVertex(0, v1);
	mesh.SetVertex(1, v2);
	mesh.SetVertex(2, v3);
	mesh.SetVertex(3, v4);
	mesh.SetVertex(4, v5);
	mesh.SetVertex(5, v6);
	mesh.SetVertex(6, v7);
	mesh.SetVertex(7, v8);
	mesh.SetVertex(8, On3dPoint.PointAdd(v1 , v));
	mesh.SetVertex(9, On3dPoint.PointAdd(v2 , v));
	mesh.SetVertex(10, On3dPoint.PointAdd(v3 , v));
	mesh.SetVertex(11, On3dPoint.PointAdd(v4 , v));
	mesh.SetVertex(12, On3dPoint.PointAdd(v5 , v));
	mesh.SetVertex(13, On3dPoint.PointAdd(v6 , v));
	mesh.SetVertex(14, On3dPoint.PointAdd(v7 , v));
	mesh.SetVertex(15, On3dPoint.PointAdd(v8 , v));
	mesh.SetVertex(16, vc);
	mesh.SetVertex(17, On3dPoint.PointAdd(vc ,v));
	/*showPoint(v1);showPoint(On3dPoint.PointAdd(v1 , v));
	showPoint(v2);showPoint(On3dPoint.PointAdd(v2 , v));
	showPoint(v3);showPoint(On3dPoint.PointAdd(v3 , v));
	showPoint(v4);showPoint(On3dPoint.PointAdd(v4 , v));
	showPoint(v5);showPoint(On3dPoint.PointAdd(v5 , v));
	showPoint(v6);showPoint(On3dPoint.PointAdd(v6 , v));
	showPoint(v7);showPoint(On3dPoint.PointAdd(v7 , v));
	showPoint(v8);showPoint(On3dPoint.PointAdd(v8 , v));*/
	mesh.SetQuad(0, 0, 8, 9, 1);
	mesh.SetQuad(1, 1, 9, 10, 2);
	mesh.SetQuad(2, 2, 10, 11, 3);
	mesh.SetQuad(3, 3, 11, 12, 4);
	mesh.SetQuad(4, 4, 12, 13, 5);
	mesh.SetQuad(5, 5, 13, 14, 6);
	mesh.SetQuad(6, 6, 14, 15, 7);
	mesh.SetQuad(7, 7, 15, 8, 0);
	//caps
	mesh.SetQuad(8, 0, 1, 2, 16);
	mesh.SetQuad(9, 2, 3, 4, 16);
	mesh.SetQuad(10, 4, 5, 6, 16);
	mesh.SetQuad(11, 6, 7, 0, 16);
	mesh.SetQuad(12, 10, 9, 8, 17);
	mesh.SetQuad(13, 12, 11, 10, 17);
	mesh.SetQuad(14, 14, 13, 12, 17);
	mesh.SetQuad(15, 8, 15, 14, 17);
	//mesh.ComputeFaceNormals()
	//mesh.FlipFaceOrientation()
	OnPlane pl1 = new OnPlane(new On3dPoint(0, 0, 0), new On3dVector(0, 1, 0));
	OnPlane pl2 = new OnPlane(p1, v0);
	OnXform xf = new OnXform();
	OnXform xf2 = new OnXform();
	xf.ChangeBasis(new OnPlane(new On3dPoint(0,0,0),new On3dVector(0,0,1)), pl1);
	xf2.ChangeBasis(pl2,new OnPlane(new On3dPoint(0,0,0),new On3dVector(0,0,1)));
	mesh.Transform(xf);
	mesh.Transform(xf2);
	return mesh;
	}
public static OnMesh stub(On3dPoint p1,On3dPoint p2,float[]  Layers){
	float length=p1.DistanceTo(p2);
	length/=Layers.length;
	ArrayList< ArrayList<On3dPoint>> pll =new ArrayList< ArrayList<On3dPoint>>() ;
    for( int i =0; i< Layers.length;i++){
    	ArrayList<On3dPoint> pts =new ArrayList<On3dPoint>();
    	pts.add( new On3dPoint(0, length*i, Layers[i]));
    	pts.add( new On3dPoint(Layers[i] * Math.sqrt(2) / 2, length*i, Layers[i] * Math.sqrt(2) / 2));
    	pts.add(new On3dPoint(Layers[i], length*i, 0));
    	pts.add(new On3dPoint(Layers[i] * Math.sqrt(2) / 2, length*i, -Layers[i] * Math.sqrt(2) / 2));
    	pts.add( new On3dPoint(0, length*i, -Layers[i]));
    	pts.add(new On3dPoint(-Layers[i] * Math.sqrt(2) / 2, length*i, -Layers[i] * Math.sqrt(2) / 2));
    	pts.add( new On3dPoint(-Layers[i], length*i, 0));
    	pts.add( new On3dPoint(-Layers[i] * Math.sqrt(2) / 2, length*i, Layers[i] * Math.sqrt(2) / 2));
    	pts.add( new On3dPoint(0, length*i, Layers[i]));
      pll.add(pts);
    }
    OnMesh mesh=loft2Mesh(pll);
    
    On3dVector v0  = On3dPoint.PointSub(p2,p1);
	OnPlane pl1 = new OnPlane(new On3dPoint(0, 0, 0), new On3dVector(0, 1, 0));
	OnPlane pl2 = new OnPlane(p1, v0);
	OnXform xf = new OnXform();
	OnXform xf2 = new OnXform();
	xf.ChangeBasis(new OnPlane(new On3dPoint(0,0,0),new On3dVector(0,0,1)), pl1);
	xf2.ChangeBasis(pl2,new OnPlane(new On3dPoint(0,0,0),new On3dVector(0,0,1)));
	mesh.CombineIdenticalVertices();
	mesh.Transform(xf);
	mesh.Transform(xf2);	
	return mesh;
	
}
public static OnMesh loft2Mesh(ArrayList< ArrayList<On3dPoint>> pll){
	 OnMesh mesh =new OnMesh();
	 On3dPoint cen =new On3dPoint();
    for( int j = 0; j<= pll.size() - 2;j++){
    	ArrayList<On3dPoint> pts1  = (ArrayList<On3dPoint>)pll.get(j);
      ArrayList<On3dPoint> pts2  =  (ArrayList<On3dPoint>)pll.get(j + 1);

     for(int i=0;i<= pts1.size() - 2;i++){
    	 On3dPoint p1 = (On3dPoint)pts1.get(i);
         On3dPoint p2  = (On3dPoint)pts2.get(i);
         On3dPoint p3 = (On3dPoint)pts2.get(i + 1);
        On3dPoint p4  = (On3dPoint)pts1.get(i + 1);
        int i1 = mesh.VertexCount() + 0;
        int i2  = mesh.VertexCount() + 1;
        int i3 = mesh.VertexCount() + 2;
        int i4  = mesh.VertexCount() + 3;
        mesh.SetVertex(i1, p1);
        mesh.SetVertex(i2, p2);
        mesh.SetVertex(i3, p3);
        mesh.SetVertex(i4, p4);
        mesh.SetQuad(mesh.faces.size(), i1, i2, i3, i4);
        if (j == pll.size() - 2 ) cen.add( pts2.get(i));	         
    }
    }

    ArrayList<On3dPoint> pts3  =(ArrayList<On3dPoint>) pll.get(pll.size() - 1);
    cen.div(pts3.size() - 1);

    for (int i =0 ;i<= pts3.size() - 2;i++){
    On3dPoint p1 =(On3dPoint) pts3.get(i);
      On3dPoint p2=(On3dPoint) pts3.get(i + 1);
      int i1  = mesh.VertexCount() + 0;
      int i2  = mesh.VertexCount() + 1;
      int i3  = mesh.VertexCount() + 2;
      mesh.SetVertex(i1, p1);
      mesh.SetVertex(i2, p2);
      mesh.SetVertex(i3, cen);
      mesh.SetTriangle(mesh.faces.size(), i1, i2, i3);
    }

   // mesh.CombineIdenticalVertices()
    //mesh.ComputeFaceNormals()
    return mesh;
}


}
