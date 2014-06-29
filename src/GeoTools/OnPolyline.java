package GeoTools;

import java.util.ArrayList;
import processing.core.PApplet;

public class OnPolyline {
	ArrayList<On3dPoint> m_points;

	public OnPolyline() {
		m_points = new ArrayList<On3dPoint>();
	}

	public OnPolyline(OnPolyline pl) {
		m_points = new ArrayList<On3dPoint>(pl.m_points);
	}

	public OnPolyline(ArrayList<On3dPoint> Points) {
		m_points = new ArrayList<On3dPoint>(Points);
	}

	public void add(On3dPoint pt) {
		this.m_points.add(pt);
	}

	public int size() {
		return this.m_points.size();
	}

	public On3dPoint get(int i) {
		return (On3dPoint) this.m_points.get(i);
	}

	public void Transform(OnXform xform) {
		for (int i = 0; i < m_points.size(); i++) {
			On3dPoint p = (On3dPoint) m_points.get(i);
			p.Transform(xform);
			m_points.set(i, p);
		}
	}

	// ///////////////////////////////
	public static OnPolyline Arc(float radius, float range, float rangesub) {
		OnPolyline pl = new OnPolyline();
		int n = (int) (range / rangesub) + 1;
		float t = range / (float) n;
		for (int i = 0; i <= n; i++) {
			On3dPoint pt = new On3dPoint(PApplet.cos(t * i) * radius,
					PApplet.sin(t * i) * radius, 0);
			pl.add(pt);
		}
		return pl;
	}

	public static OnPolyline Arc(float radius, float range, int rangesub) {
		OnPolyline pl = new OnPolyline();
		int n = rangesub;
		float t = range / (float) n;
		for (int i = 0; i <= n; i++) {
			On3dPoint pt = new On3dPoint(PApplet.cos(t * i) * radius,
					PApplet.sin(t * i) * radius, 0);
			pl.add(pt);
		}
		return pl;
	}

	public void ccSubdivide(int level) {
		for (int i = 0; i < level; i++) {
			this.m_points = cc_Subdivide();
		}
	}

	public static OnPolyline ccSubdivide(OnPolyline pl, int level) {
		OnPolyline pl2 = new OnPolyline(pl);
		pl2.ccSubdivide(level);
		return pl2;
	}

	private ArrayList<On3dPoint> cc_Subdivide() {
		ArrayList<On3dPoint> ps2 = new ArrayList<On3dPoint>();
		if (this.m_points.size() < 3) {
			return this.m_points;
		}
		if (this.m_points.get(0).DistanceTo(
				this.m_points.get(this.m_points.size() - 1)) > 0.001f) {
			ps2.add(this.m_points.get(0));
			On3dPoint pt = On3dPoint.PointAdd(this.m_points.get(0),
					this.m_points.get(1));
			pt.mul(0.5f);
			ps2.add(pt);
			for (int i = 1; i < this.m_points.size() - 1; i++) {
				On3dPoint p1 = new On3dPoint(this.m_points.get(i - 1));
				On3dPoint p2 = new On3dPoint(this.m_points.get(i));
				On3dPoint p3 = new On3dPoint(this.m_points.get(i + 1));
				On3dPoint p4 = On3dPoint.PointAdd(p2, p3);
				p4.mul(0.5f);
				p1.mul(1f / 6f);
				p2.mul(2f / 3f);
				p3.mul(1f / 6f);
				p1.add(p2);
				p1.add(p3);
				ps2.add(p1);
				ps2.add(p4);
			}
			ps2.add(this.m_points.get(this.m_points.size() - 1));
		} else {
			if (this.m_points.size() < 4) {
				return this.m_points;
			}
			On3dPoint p1 = new On3dPoint(
					this.m_points.get(this.m_points.size() - 2));
			On3dPoint p2 = new On3dPoint(this.m_points.get(0));
			On3dPoint p3 = new On3dPoint(this.m_points.get(1));
			p1.mul(1f / 6f);
			p2.mul(2f / 3f);
			p3.mul(1f / 6f);
			p1.add(p2);
			p1.add(p3);
			ps2.add(p1);
			On3dPoint pt = On3dPoint.PointAdd(this.m_points.get(0),
					this.m_points.get(1));
			pt.mul(0.5f);
			ps2.add(pt);
			for (int i = 1; i < this.m_points.size() - 1; i++) {
				p1 = new On3dPoint(this.m_points.get(i - 1));
				p2 = new On3dPoint(this.m_points.get(i));
				p3 = new On3dPoint(this.m_points.get(i + 1));
				On3dPoint p4 = On3dPoint.PointAdd(p2, p3);
				p4.mul(0.5f);
				p1.mul(1f / 6f);
				p2.mul(2f / 3f);
				p3.mul(1f / 6f);
				p1.add(p2);
				p1.add(p3);
				ps2.add(p1);
				ps2.add(p4);
			}
			ps2.add(ps2.get(0));
		}
		return ps2;
	}

	// ///////////////////////////////
	public void draw(PApplet p, float scale) {
		if (this.m_points.size() < 2)
			return;
		p.pushMatrix();
		p.scale(scale);
		p.beginShape(PApplet.LINES);
		for (int i = 1; i < this.m_points.size(); i++) {
			p.vertex(this.m_points.get(i - 1).x, this.m_points.get(i - 1).y,
					this.m_points.get(i - 1).z);
			p.vertex(this.m_points.get(i).x, this.m_points.get(i).y,
					this.m_points.get(i).z);
		}
		p.endShape();
		p.popMatrix();
	}

	public void drawCurve(PApplet p, float scale) {
		if (this.m_points.size() < 2)
			return;
		p.pushMatrix();
		p.scale(scale);
		p.beginShape();
		p.curveVertex(this.m_points.get(0).x, this.m_points.get(0).y,
				this.m_points.get(0).z);
		for (int i = 1; i < this.m_points.size(); i++) {
			p.curveVertex(this.m_points.get(i).x, this.m_points.get(i).y,
					this.m_points.get(i).z);
		}
		int t = this.m_points.size() - 1;
		p.curveVertex(this.m_points.get(t).x, this.m_points.get(t).y,
				this.m_points.get(t).z);
		p.endShape();
		p.popMatrix();
	}
}
