package GeoTools;

import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PConstants;

public class OnMesh {
	public ArrayList<On3dPoint> Points = new ArrayList<On3dPoint>();
	public ArrayList<MeshFace> faces = new ArrayList<MeshFace>();	
	public int[] colors;
	public On3dVector[] normals;

	public OnMesh() {
	}
	public OnMesh(OnMesh mesh) {
		this.Points.clear();
		for (int i = 0; i < mesh.Points.size(); i++) {
			Points.add(new On3dPoint(mesh.Points.get(i)));
		}
		this.faces.clear();
		for (int i = 0; i < mesh.faces.size(); i++) {
			faces.add(new MeshFace(mesh.faces.get(i).a, mesh.faces.get(i).b,
					mesh.faces.get(i).c, mesh.faces.get(i).d));
		}
	}

	public void Append(OnMesh mesh) {
		int n = this.VertexCount();
		for (int i = 0; i < mesh.FaceCount(); i++) {
			MeshFace mf = mesh.faces.get(i);
			// PApplet.println(n);
			this.faces
					.add(new MeshFace(mf.a + n, mf.b + n, mf.c + n, mf.d + n));
		}
		for (int i = 0; i < mesh.VertexCount(); i++) {
			this.Points.add(mesh.Points.get(i));
		}
	}

	public int FaceCount() {
		return faces.size();
	}

	public int VertexCount() {
		return Points.size();
	}

	public void SetVertex(int index, On3dPoint P) {
		Points.add(index, P);
	}

	public void SetVertex(On3dPoint P) {
		Points.add(P);
	}

	public void SetQuad(int index, int p1, int p2, int p3, int p4) {
		faces.add(index, new MeshFace(p1, p2, p3, p4));
	}

	public void SetQuad(int p1, int p2, int p3, int p4) {
		faces.add(new MeshFace(p1, p2, p3, p4));
	}

	public void SetTriangle(int index, int p1, int p2, int p3) {
		faces.add(index, new MeshFace(p1, p2, p3));
	}

	public void SetTriangle(int p1, int p2, int p3) {
		faces.add(new MeshFace(p1, p2, p3));
	}

	public void Transform(OnXform xform) {
		for (int i = 0; i < Points.size(); i++) {
			On3dPoint p = (On3dPoint) Points.get(i);
			p.Transform(xform);
			Points.set(i, p);
		}
	}

	public On3dVector ONTriangleNormal(On3dPoint A, On3dPoint B, On3dPoint C) {
		// N = normal to triangle's plane
		On3dVector N = new On3dVector();
		float a, b, c, d;
		N.x = A.y * (B.z - C.z) + B.y * (C.z - A.z) + C.y * (A.z - B.z);
		N.y = A.z * (B.x - C.x) + B.z * (C.x - A.x) + C.z * (A.x - B.x);
		N.z = A.x * (B.y - C.y) + B.x * (C.y - A.y) + C.x * (A.y - B.y);

		a = Math.abs(N.x);
		b = Math.abs(N.y);
		c = Math.abs(N.z);
		if (b > a) {
			if (c > b) {
				// c is largest
				if (c > Float.MIN_VALUE) {
					a /= c;
					b /= c;
					d = (float) (c * Math.sqrt(1.0 + a * a + b * b));
				} else {
					d = c;
				}
			} else {
				if (b > Float.MIN_VALUE) {
					// b is largest
					a /= b;
					c /= b;
					d = (float) (b * Math.sqrt(1.0 + c * c + a * a));
				} else {
					d = b;
				}
			}
		} else if (c > a) {
			// c is largest
			if (c > Float.MIN_VALUE) {
				a /= c;
				b /= c;
				d = (float) (c * Math.sqrt(1.0 + a * a + b * b));
			} else {
				d = c;
			}
		} else if (a > Float.MIN_VALUE) {
			// a is largest
			b /= a;
			c /= a;
			d = (float) (a * Math.sqrt(1.0 + b * b + c * c));
		} else {
			d = a;
		}
		if (d > 0.0) {
			N.x /= d;
			N.y /= d;
			N.z /= d;
		}
		return N;
	}

	public void CombineIdenticalVertices() {
		ArrayList<On3dPoint> pn = new ArrayList<On3dPoint>();
		int arrn[] = new int[Points.size()];
		pn.add(Points.get(0));
		arrn[0] = 0;
		for (int i = 1; i < Points.size(); i++) {
			boolean sign = true;
			for (int j = pn.size() - 1; j >= 0; j--) {
				if (Points.get(i).DistanceTo(pn.get(j)) < 0.00001) {
					arrn[i] = j;
					sign = false;
					break;
				}
			}
			if (sign) {
				pn.add(Points.get(i));
				arrn[i] = pn.size() - 1;
			}
		}
		this.Points = pn;
		for (int i = 0; i < faces.size(); i++) {
			faces.get(i).a = arrn[faces.get(i).a];
			faces.get(i).b = arrn[faces.get(i).b];
			faces.get(i).c = arrn[faces.get(i).c];
			faces.get(i).d = arrn[faces.get(i).d];
		}
	}

	public MeshTopologyEdgeList TopologyEdgeList(){
		MeshTopologyEdgeList el=new MeshTopologyEdgeList(this);	
		if(this.faces.size()==0||this.Points.size()==0)return el;
		CombineIdenticalVertices();	
		for(int i=0;i<faces.size();i++){
		 MeshFace f=(MeshFace)faces.get(i);
		 if(f.isTriangle()){
			 el.add(new MeshEdge(f.a,f.b,i)) ;
			 el.add(new MeshEdge(f.b,f.c,i)) ;
			 el.add(new MeshEdge(f.c,f.a,i)) ;
		 }
		 if(f.isQuad()){
			 el.add(new MeshEdge(f.a,f.b,i)) ;
			 el.add(new MeshEdge(f.b,f.c,i)) ;
			 el.add(new MeshEdge(f.c,f.d,i)) ;
			 el.add(new MeshEdge(f.d,f.a,i)) ;
		 }
		}		
		return el;
	}
	
	public void ComputeVertexNormals() {
		int fcount = FaceCount();
		int vcount = VertexCount();
		int fi;
		On3dVector[] m_F = new On3dVector[fcount];
		if (fcount > 0 && vcount > 0) {
			m_F = ComputeFaceNormals();
		}
		normals = new On3dVector[vcount];
		for (fi = 0; fi < vcount; fi++) {
			normals[fi] = new On3dVector();
		}
		for (fi = 0; fi < fcount; fi++) {
			MeshFace f = (MeshFace) faces.get(fi);
			normals[f.a].add(m_F[fi]);
			normals[f.b].add(m_F[fi]);
			normals[f.c].add(m_F[fi]);
			if (f.isQuad())
				normals[f.d].add(m_F[fi]);
		}
		for (fi = 0; fi < vcount; fi++) {
			normals[fi].Unitize();
		}
	}

	public On3dVector[] ComputeFaceNormals() {
		int fcount = FaceCount();
		On3dVector[] facenormals = new On3dVector[fcount];
		if (fcount > 0) {
			On3dVector a, b, n;
			for (int fi = 0; fi < fcount; fi++) {
				a = On3dPoint.PointSub(Points.get(faces.get(fi).c),
						Points.get(faces.get(fi).a));
				b = On3dPoint.PointSub(Points.get(faces.get(fi).d),
						Points.get(faces.get(fi).b));
				n = On3dVector.OnCrossProduct(a, b); // works for triangles,
														// quads, and nonplanar
														// quads
				n.Unitize();
				facenormals[fi] = n;
			}
		}
		return facenormals;
	}

	public On3dPoint[] faceCenter() {
		On3dPoint[] cen = new On3dPoint[this.FaceCount()];
		if (this.FaceCount() > 0) {
			for (int i = 0; i < cen.length; i++) {
				MeshFace f = (MeshFace) faces.get(i);
				cen[i] = new On3dPoint();
				cen[i].add(this.Points.get(f.a));
				cen[i].add(this.Points.get(f.b));
				cen[i].add(this.Points.get(f.c));
				if (f.isQuad()) {
					cen[i].add(this.Points.get(f.d));
					cen[i].mul(0.25f);
				} else {
					cen[i].mul(1f / 3f);
				}
			}
		}
		return cen;
	}

	public void drawFaceNormals(PApplet p, float t) {
		On3dVector[] m_F = ComputeFaceNormals();
		On3dPoint[] m_cen = faceCenter();
		p.pushMatrix();
		for (int i = 1; i < m_F.length; i++) {
			// p.println(m_cen[0].x+"/"+m_cen[0].y+"/"+m_cen[0].z);
			On3dPoint v1 = m_cen[i];
			m_F[i].mul(t);
			On3dPoint v2 = On3dPoint.PointAdd(v1, m_F[i]);
			p.beginShape(PConstants.LINES);
			p.vertex(v1.x, v1.y, v1.z);
			p.vertex(v2.x, v2.y, v2.z);
			p.endShape();
		}
		p.popMatrix();
	}

	public void drawNormals(PApplet p, float t) {
		ComputeVertexNormals();
		p.pushMatrix();
		for (int i = 1; i < this.normals.length; i++) {
			On3dPoint v1 = Points.get(i);
			normals[i].mul(t);
			On3dPoint v2 = On3dPoint.PointAdd(Points.get(i), normals[i]);
			// p.println(Points.get(0).x+"/"+Points.get(0).y+"/"+Points.get(0).z);
			p.beginShape(PConstants.LINES);
			p.vertex(v1.x, v1.y, v1.z);
			p.vertex(v2.x, v2.y, v2.z);
			p.endShape();
		}
		p.popMatrix();
	}

	public void draw(PApplet p) {
		draw(p, 1);
	}

	public void draw(PApplet p, float t) {
		if (colors != null) {
			if (this.colors.length == this.VertexCount()) {
				show(p, this.colors, t);
				return;
			}
		}
		show(p, t);
	}

	private void show(PApplet p, float t) {
		p.pushMatrix();
		p.scale(t);
		p.beginShape(PConstants.TRIANGLES);
		for (int i = 0; i < faces.size(); i++) {
			MeshFace f = (MeshFace) faces.get(i);
			if (f.isTriangle()) {
				p.vertex(Points.get(f.a).x, Points.get(f.a).y,
						Points.get(f.a).z);
				p.vertex(Points.get(f.b).x, Points.get(f.b).y,
						Points.get(f.b).z);
				p.vertex(Points.get(f.c).x, Points.get(f.c).y,
						Points.get(f.c).z);
			}
		}
		p.endShape();
		p.beginShape(PConstants.QUADS);
		for (int i = 0; i < faces.size(); i++) {
			MeshFace f = (MeshFace) faces.get(i);
			if (f.isQuad()) {
				p.vertex(Points.get(f.a).x, Points.get(f.a).y,
						Points.get(f.a).z);
				p.vertex(Points.get(f.b).x, Points.get(f.b).y,
						Points.get(f.b).z);
				p.vertex(Points.get(f.c).x, Points.get(f.c).y,
						Points.get(f.c).z);
				p.vertex(Points.get(f.d).x, Points.get(f.d).y,
						Points.get(f.d).z);
			}
		}
		p.endShape();
		p.popMatrix();
	}

	private void show(PApplet p, int[] c, float t) {
		p.pushMatrix();
		p.scale(t);
		p.beginShape(PConstants.TRIANGLES);
		for (int i = 0; i < faces.size(); i++) {
			MeshFace f = (MeshFace) faces.get(i);
			p.fill(c[f.a]);
			p.vertex(Points.get(f.a).x, Points.get(f.a).y, Points.get(f.a).z);
			p.fill(c[f.b]);
			p.vertex(Points.get(f.b).x, Points.get(f.b).y, Points.get(f.b).z);
			p.fill(c[f.c]);
			p.vertex(Points.get(f.c).x, Points.get(f.c).y, Points.get(f.c).z);
		}
		p.endShape();
		p.beginShape(PConstants.QUADS);
		for (int i = 0; i < faces.size(); i++) {
			MeshFace f = (MeshFace) faces.get(i);
			p.fill(c[f.a]);
			p.vertex(Points.get(f.a).x, Points.get(f.a).y, Points.get(f.a).z);
			p.fill(c[f.b]);
			p.vertex(Points.get(f.b).x, Points.get(f.b).y, Points.get(f.b).z);
			p.fill(c[f.c]);
			p.vertex(Points.get(f.c).x, Points.get(f.c).y, Points.get(f.c).z);
			p.fill(c[f.d]);
			p.vertex(Points.get(f.d).x, Points.get(f.d).y, Points.get(f.d).z);
		}
		p.endShape();
		p.popMatrix();
	}

	
}
