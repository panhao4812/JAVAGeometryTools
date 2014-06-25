package GeoTools;

import java.util.ArrayList;
import processing.core.PApplet;

public class OnPolyline{
	ArrayList<On3dPoint> m_points;
	public OnPolyline(){m_points=new ArrayList<On3dPoint>();	}
	public OnPolyline(ArrayList<On3dPoint> Points){	m_points=new ArrayList<On3dPoint>(Points);}
	public void add(On3dPoint pt){
		this.m_points.add(pt);		
	}
	public int size(){
		return this.m_points.size();
	}
	public On3dPoint get(int i){
		return (On3dPoint)this.m_points.get(i);	
	}
	public void Transform(OnXform xform) {
		for (int i = 0; i < m_points.size(); i++) {
			On3dPoint p = (On3dPoint) m_points.get(i);
			p.Transform(xform);
			m_points.set(i, p);
		}
	}
	/////////////////////////////////
	public static OnPolyline Arc(float radius,float range,float rangesub){
		OnPolyline pl=new OnPolyline();
		int n=(int)(range/rangesub)+1;
		float t=range/(float)n;
		for(int i=0;i<=n;i++){
			On3dPoint pt=new On3dPoint(PApplet.cos(t*i)*radius,PApplet.sin(t*i)*radius,0);
			pl.add(pt);
		}	
		return pl;
	}
	public static OnPolyline Arc(float radius,float range,int rangesub){
		OnPolyline pl=new OnPolyline();
		int n=rangesub;
		float t=range/(float)n;
		for(int i=0;i<=n;i++){
			On3dPoint pt=new On3dPoint(PApplet.cos(t*i)*radius,PApplet.sin(t*i)*radius,0);
			pl.add(pt);
		}	
		return pl;
	}
	/////////////////////////////////
	public void draw(PApplet p,float scale){
		if(this.m_points.size()<2)return;
		p.pushMatrix();
		p.scale(scale);
		p.beginShape(PApplet.LINES);
		for(int i=1;i<this.m_points.size();i++){
		p.vertex(this.m_points.get(i-1).x, this.m_points.get(i-1).y, this.m_points.get(i-1).z);
		p.vertex(this.m_points.get(i).x, this.m_points.get(i).y, this.m_points.get(i).z);
	}
		p.endShape();
		p.popMatrix();
	}
}
