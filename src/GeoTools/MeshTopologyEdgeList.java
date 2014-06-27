package GeoTools;
import java.util.ArrayList;

import processing.core.PApplet;

public class MeshTopologyEdgeList {
	OnMesh m_mesh;
	 ArrayList<MeshEdge> edges=new ArrayList<MeshEdge>();
	 public MeshTopologyEdgeList(OnMesh Mesh){m_mesh=Mesh;}
	 public void add(MeshEdge E){
		 boolean sign=true;
		 for (int i=0;i<edges.size();i++) {
	    MeshEdge e = (MeshEdge)edges.get(i);
		if(E.equalTo(e)){sign=false;
		edges.get(i).faces.addAll(E.faces);
		break;}
		}
	if(sign){edges.add(E);	}
	 }
	 public void draw(PApplet p){
		 if(this.edges.size()<1)return;
	p.pushMatrix();
	p.beginShape(PApplet.LINES);
		  for (java.util.Iterator<MeshEdge> iter =  edges.iterator(); iter.hasNext();) {
			  MeshEdge e = iter.next();
			 p. vertex(m_mesh.Points.get(e.p1).x,
					   m_mesh.Points.get(e.p1).y,
					   m_mesh.Points.get(e.p1).z);
			 p. vertex(m_mesh.Points.get(e.p2).x,
					   m_mesh.Points.get(e.p2).y,
					   m_mesh.Points.get(e.p2).z);
		  }
		p.endShape();
		p.popMatrix();	  
	 }
	 public void drawProfile(PApplet p){
		 if(this.edges.size()<1)return;
	p.pushMatrix();
	p.beginShape(PApplet.LINES);
		  for (java.util.Iterator<MeshEdge> iter =  edges.iterator(); iter.hasNext();) {
			  MeshEdge e = iter.next();
			  if(e.faces.size()==1){
			 p. vertex(m_mesh.Points.get(e.p1).x,
					   m_mesh.Points.get(e.p1).y,
					   m_mesh.Points.get(e.p1).z);
			 p. vertex(m_mesh.Points.get(e.p2).x,
					   m_mesh.Points.get(e.p2).y,
					   m_mesh.Points.get(e.p2).z);
		  }
		  }
		p.endShape();
		p.popMatrix();	  
	 }
}

/*
public static List removeDuplicateWithOrder(List list) {
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            Object element = iter.next();
            if (set.add(element))
                newList.add(element);
        }
        return newList;
    }
*/