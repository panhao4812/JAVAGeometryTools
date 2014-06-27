package GeoTools;

import java.util.ArrayList;
public class MeshCreation {
	public MeshCreation(){}
    public OnMesh MeshLoft(OnPolyline pl1, OnPolyline pl2, boolean isPolyClosed, boolean isClosed)
    {
    	ArrayList<OnPolyline> pls = new ArrayList<OnPolyline>();
        pls.add(pl1);
        pls.add(pl2);
        return MeshLoft(pls, isPolyClosed, isClosed);
    }
    public OnMesh MeshLoft(ArrayList<OnPolyline> pl, boolean isPolyClosed, boolean isClosed)
    {
        int U = pl.get(0).size();
        int V = pl.size();
        if (isPolyClosed) { U++; }
        if (isClosed) { pl.add(pl.get(0)); V++; }
        ArrayList<On3dPoint> pls = new ArrayList<On3dPoint>();
        for (int i = 0; i < pl.size(); i++)
        {
            for (int j = 0; j < pl.get(i).size(); j++)
            {
                pls.add(pl.get(i).get(j));
            }
            if (isPolyClosed) { pls.add(pl.get(i).get(0)); }
        }
        return MeshFromPoints(pls, U, V);
    }
    public OnMesh MeshFromPoints(ArrayList<On3dPoint> pl, int u, int v)
    {
        if (u * v > pl.size() || u < 2 || v < 2) return null;
        OnMesh mesh = new OnMesh();
        for (int i = 0; i < pl.size(); i++)
        {
            mesh.Points.add(pl.get(i));
        }
        for (int i = 1; i < u; i++)
        {
            for (int j = 1; j < v; j++)
            {
                mesh.faces.add(new MeshFace(
                (j - 1) * u + i - 1,
                (j - 1) * u + i,
                (j) * u + i,
                (j) * u + i - 1));
            }
        }    
        return mesh;
    }
	 public OnMesh MeshFromPoints(On3dPoint p1, On3dPoint p2, On3dPoint p3, On3dPoint p4)
     {
		 OnMesh mesh = new OnMesh();
         mesh.Points.add(p1);
         mesh.Points.add(p2);
         mesh.Points.add(p3);
         mesh.Points.add(p4);
         mesh.faces.add(new MeshFace(0, 1, 2, 3));
         return mesh;
     }
     public OnMesh MeshFromPoints(On3dPoint p1, On3dPoint p2, On3dPoint p3)
     {
    	 OnMesh mesh = new OnMesh();
         mesh.Points.add(p1);
         mesh.Points.add(p2);
         mesh.Points.add(p3);
         mesh.faces.add(new MeshFace(0, 1, 2));
         return mesh;
     }
     public static OnMesh purlins(ArrayList<On3dPoint> pl, float r) {
 		ArrayList<ArrayList<On3dPoint>> pll = new ArrayList<ArrayList<On3dPoint>>();
 		for (int i = 0; i < pl.size(); i++) {
 			ArrayList<On3dPoint> pts = new ArrayList<On3dPoint>();
 			pts.add(new On3dPoint(pl.get(i).x + 1.732 * r / 2, pl.get(i).y, pl
 					.get(i).z + 1.5 * r));
 			pts.add(new On3dPoint(pl.get(i).x + r, pl.get(i).y, pl.get(i).z + 1
 					* r));
 			pts.add(new On3dPoint(pl.get(i).x + 1.414 * r / 2, pl.get(i).y, pl
 					.get(i).z + (1 - 1.414 / 2) * r));
 			pts.add(new On3dPoint(pl.get(i)));
 			pts.add(new On3dPoint(pl.get(i).x - 1.414 * r / 2, pl.get(i).y, pl
 					.get(i).z + (1 - 1.414 / 2) * r));
 			pts.add(new On3dPoint(pl.get(i).x - r, pl.get(i).y, pl.get(i).z + 1
 					* r));
 			pts.add(new On3dPoint(pl.get(i).x - 1.732 * r / 2, pl.get(i).y, pl
 					.get(i).z + 1.5 * r));
 			pts.add(new On3dPoint(pl.get(i).x + 1.732 * r / 2, pl.get(i).y, pl
 					.get(i).z + 1.5 * r));
 			pll.add(pts);
 		}
 		return loft2Mesh(pll);
 	}

 	public static OnMesh column(On3dPoint p1, On3dPoint p2, float radius) {
 		On3dVector v0 = On3dPoint.PointSub(p2, p1);
 		On3dVector v = new On3dVector(0, v0.Length(), 0);
 		On3dPoint v1 = new On3dPoint(0, 0, radius);
 		On3dPoint v2 = new On3dPoint(radius * Math.sqrt(2) / 2, 0, radius
 				* Math.sqrt(2) / 2);
 		On3dPoint v3 = new On3dPoint(radius, 0, 0);
 		On3dPoint v4 = new On3dPoint(radius * Math.sqrt(2) / 2, 0, -radius
 				* Math.sqrt(2) / 2);
 		On3dPoint v5 = new On3dPoint(0, 0, -radius);
 		On3dPoint v6 = new On3dPoint(-radius * Math.sqrt(2) / 2, 0, -radius
 				* Math.sqrt(2) / 2);
 		On3dPoint v7 = new On3dPoint(-radius, 0, 0);
 		On3dPoint v8 = new On3dPoint(-radius * Math.sqrt(2) / 2, 0, radius
 				* Math.sqrt(2) / 2);
 		On3dPoint vc = new On3dPoint();
 		vc.add(v1);
 		vc.add(v2);
 		vc.add(v3);
 		vc.add(v4);
 		vc.add(v5);
 		vc.add(v6);
 		vc.add(v7);
 		vc.add(v8);
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
 		mesh.SetVertex(8, On3dPoint.PointAdd(v1, v));
 		mesh.SetVertex(9, On3dPoint.PointAdd(v2, v));
 		mesh.SetVertex(10, On3dPoint.PointAdd(v3, v));
 		mesh.SetVertex(11, On3dPoint.PointAdd(v4, v));
 		mesh.SetVertex(12, On3dPoint.PointAdd(v5, v));
 		mesh.SetVertex(13, On3dPoint.PointAdd(v6, v));
 		mesh.SetVertex(14, On3dPoint.PointAdd(v7, v));
 		mesh.SetVertex(15, On3dPoint.PointAdd(v8, v));
 		mesh.SetVertex(16, vc);
 		mesh.SetVertex(17, On3dPoint.PointAdd(vc, v));
 		/*
 		 * showPoint(v1);showPoint(On3dPoint.PointAdd(v1 , v));
 		 * showPoint(v2);showPoint(On3dPoint.PointAdd(v2 , v));
 		 * showPoint(v3);showPoint(On3dPoint.PointAdd(v3 , v));
 		 * showPoint(v4);showPoint(On3dPoint.PointAdd(v4 , v));
 		 * showPoint(v5);showPoint(On3dPoint.PointAdd(v5 , v));
 		 * showPoint(v6);showPoint(On3dPoint.PointAdd(v6 , v));
 		 * showPoint(v7);showPoint(On3dPoint.PointAdd(v7 , v));
 		 * showPoint(v8);showPoint(On3dPoint.PointAdd(v8 , v));
 		 */
 		mesh.SetQuad(0, 0, 8, 9, 1);
 		mesh.SetQuad(1, 1, 9, 10, 2);
 		mesh.SetQuad(2, 2, 10, 11, 3);
 		mesh.SetQuad(3, 3, 11, 12, 4);
 		mesh.SetQuad(4, 4, 12, 13, 5);
 		mesh.SetQuad(5, 5, 13, 14, 6);
 		mesh.SetQuad(6, 6, 14, 15, 7);
 		mesh.SetQuad(7, 7, 15, 8, 0);
 		// caps
 		mesh.SetQuad(8, 0, 1, 2, 16);
 		mesh.SetQuad(9, 2, 3, 4, 16);
 		mesh.SetQuad(10, 4, 5, 6, 16);
 		mesh.SetQuad(11, 6, 7, 0, 16);
 		mesh.SetQuad(12, 10, 9, 8, 17);
 		mesh.SetQuad(13, 12, 11, 10, 17);
 		mesh.SetQuad(14, 14, 13, 12, 17);
 		mesh.SetQuad(15, 8, 15, 14, 17);
 		// mesh.ComputeFaceNormals()
 		// mesh.FlipFaceOrientation()
 		OnPlane pl1 = new OnPlane(new On3dPoint(0, 0, 0), new On3dVector(0, 1,
 				0));
 		OnPlane pl2 = new OnPlane(p1, v0);
 		OnXform xf = new OnXform();
 		OnXform xf2 = new OnXform();
 		xf.ChangeBasis(new OnPlane(new On3dPoint(0, 0, 0), new On3dVector(0, 0,
 				1)), pl1);
 		xf2.ChangeBasis(pl2, new OnPlane(new On3dPoint(0, 0, 0),
 				new On3dVector(0, 0, 1)));
 		mesh.Transform(xf);
 		mesh.Transform(xf2);
 		return mesh;
 	}

 	public static OnMesh stub(On3dPoint p1, On3dPoint p2, float[] Layers) {
 		float length = p1.DistanceTo(p2);
 		length /= Layers.length;
 		ArrayList<ArrayList<On3dPoint>> pll = new ArrayList<ArrayList<On3dPoint>>();
 		for (int i = 0; i < Layers.length; i++) {
 			ArrayList<On3dPoint> pts = new ArrayList<On3dPoint>();
 			pts.add(new On3dPoint(0, length * i, Layers[i]));
 			pts.add(new On3dPoint(Layers[i] * Math.sqrt(2) / 2, length * i,
 					Layers[i] * Math.sqrt(2) / 2));
 			pts.add(new On3dPoint(Layers[i], length * i, 0));
 			pts.add(new On3dPoint(Layers[i] * Math.sqrt(2) / 2, length * i,
 					-Layers[i] * Math.sqrt(2) / 2));
 			pts.add(new On3dPoint(0, length * i, -Layers[i]));
 			pts.add(new On3dPoint(-Layers[i] * Math.sqrt(2) / 2, length * i,
 					-Layers[i] * Math.sqrt(2) / 2));
 			pts.add(new On3dPoint(-Layers[i], length * i, 0));
 			pts.add(new On3dPoint(-Layers[i] * Math.sqrt(2) / 2, length * i,
 					Layers[i] * Math.sqrt(2) / 2));
 			pts.add(new On3dPoint(0, length * i, Layers[i]));
 			pll.add(pts);
 		}
 		OnMesh mesh = loft2Mesh(pll);

 		On3dVector v0 = On3dPoint.PointSub(p2, p1);
 		OnPlane pl1 = new OnPlane(new On3dPoint(0, 0, 0), new On3dVector(0, 1,
 				0));
 		OnPlane pl2 = new OnPlane(p1, v0);
 		OnXform xf = new OnXform();
 		OnXform xf2 = new OnXform();
 		xf.ChangeBasis(new OnPlane(new On3dPoint(0, 0, 0), new On3dVector(0, 0,
 				1)), pl1);
 		xf2.ChangeBasis(pl2, new OnPlane(new On3dPoint(0, 0, 0),
 				new On3dVector(0, 0, 1)));
 		mesh.CombineIdenticalVertices();
 		mesh.Transform(xf);
 		mesh.Transform(xf2);
 		return mesh;

 	}

 	public static OnMesh loft2Mesh(ArrayList<ArrayList<On3dPoint>> pll) {
 		OnMesh mesh = new OnMesh();
 		On3dPoint cen = new On3dPoint();
 		for (int j = 0; j <= pll.size() - 2; j++) {
 			ArrayList<On3dPoint> pts1 = (ArrayList<On3dPoint>) pll.get(j);
 			ArrayList<On3dPoint> pts2 = (ArrayList<On3dPoint>) pll.get(j + 1);

 			for (int i = 0; i <= pts1.size() - 2; i++) {
 				On3dPoint p1 = (On3dPoint) pts1.get(i);
 				On3dPoint p2 = (On3dPoint) pts2.get(i);
 				On3dPoint p3 = (On3dPoint) pts2.get(i + 1);
 				On3dPoint p4 = (On3dPoint) pts1.get(i + 1);
 				int i1 = mesh.VertexCount() + 0;
 				int i2 = mesh.VertexCount() + 1;
 				int i3 = mesh.VertexCount() + 2;
 				int i4 = mesh.VertexCount() + 3;
 				mesh.SetVertex(i1, p1);
 				mesh.SetVertex(i2, p2);
 				mesh.SetVertex(i3, p3);
 				mesh.SetVertex(i4, p4);
 				mesh.SetQuad(mesh.faces.size(), i1, i2, i3, i4);
 				if (j == pll.size() - 2)
 					cen.add(pts2.get(i));
 			}
 		}

 		ArrayList<On3dPoint> pts3 = (ArrayList<On3dPoint>) pll
 				.get(pll.size() - 1);
 		cen.div(pts3.size() - 1);

 		for (int i = 0; i <= pts3.size() - 2; i++) {
 			On3dPoint p1 = (On3dPoint) pts3.get(i);
 			On3dPoint p2 = (On3dPoint) pts3.get(i + 1);
 			int i1 = mesh.VertexCount() + 0;
 			int i2 = mesh.VertexCount() + 1;
 			int i3 = mesh.VertexCount() + 2;
 			mesh.SetVertex(i1, p1);
 			mesh.SetVertex(i2, p2);
 			mesh.SetVertex(i3, cen);
 			mesh.SetTriangle(mesh.faces.size(), i1, i2, i3);
 		}

 		// mesh.CombineIdenticalVertices()
 		// mesh.ComputeFaceNormals()
 		return mesh;
 	}

 	// //////////
 	public static OnMesh[] followlines2(OnMesh mesh, OnMesh mesh1,
 			OnMesh mesh2, float[] t, float iso) {
 		OnMesh[] M = new OnMesh[2];
 		mesh1 = new OnMesh();
 		mesh2 = new OnMesh();
 		for (int i = 0; i < mesh.faces.size(); i++) {
 			if (mesh.faces.get(i).isTriangle()) {
 				int a = mesh.faces.get(i).a;
 				int b = mesh.faces.get(i).b;
 				int c = mesh.faces.get(i).c;
 				// PApplet.println(a+"/"+b+"/"+c+"/");

 				On3dPoint p1 = mesh.Points.get(a);
 				On3dPoint p2 = mesh.Points.get(b);
 				On3dPoint p3 = mesh.Points.get(c);
 				float t1 = t[a];
 				float t2 = t[b];
 				float t3 = t[c];
 				Solve3Face(p1, p2, p3, t1, t2, t3, iso, mesh1, mesh2);
 				// PApplet.println(ls.FaceCount()+"/"+ls.Points.size());
 			}
 		}
 		M[0] = mesh1;
 		M[1] = mesh2;
 		return M;
 	}

 	public static boolean Solve3Face(On3dPoint p1, On3dPoint p2, On3dPoint p3,
 			float t1, float t2, float t3, float iso, OnMesh mesh, OnMesh mesh2) {
 		int n = mesh.VertexCount();
 		int n2 = mesh2.VertexCount();
 		int square_idx = 0;
 		if (t1 < iso)
 			square_idx |= 1;
 		if (t2 < iso)
 			square_idx |= 2;
 		if (t3 < iso)
 			square_idx |= 4;
 		int a = TriLine[square_idx][0];
 		int b = TriLine[square_idx][1];
 		int c = TriLine[square_idx][2];
 		int d = TriLine[square_idx][3];
 		int e = TriLine[square_idx][4];
 		int f = TriLine[square_idx][5];
 		int g = TriLine[square_idx][6];
 		int h = TriLine[square_idx][7];
 		if (a == -1) {
 			mesh2.Points.add(p1);
 			mesh2.Points.add(p2);
 			mesh2.Points.add(p3);
 			mesh2.faces.add(new MeshFace(0 + n2, 1 + n2, 2 + n2));
 			return true;
 		}
 		else if (a == -2) {
 				mesh.Points.add(p1);
 				mesh.Points.add(p2);
 				mesh.Points.add(p3);
 				mesh.faces.add(new MeshFace(0 + n, 1 + n, 2 + n));
 				return true;
 			}
 		else{
 			On3dPoint[] L = new On3dPoint[3];
 			On3dPoint ve1, ve2;
 			ve1 = On3dPoint.PointMul(p2, (t1 - iso) / (t1 - t2));
 			ve2 = On3dPoint.PointMul(p1, (iso - t2) / (t1 - t2));
 			L[0]=On3dPoint.PointAdd(ve1, ve2);
 			ve1 = On3dPoint.PointMul(p3, (t2 - iso) / (t2 - t3));
 			ve2 = On3dPoint.PointMul(p2, (iso - t3) / (t2 - t3));
 			L[1]=On3dPoint.PointAdd(ve1, ve2);
 			ve1 = On3dPoint.PointMul(p1, (t3 - iso) / (t3 - t1));
 			ve2 = On3dPoint.PointMul(p3, (iso - t1) / (t3 - t1));
 			L[2]=On3dPoint.PointAdd(ve1, ve2);
 			On3dPoint[] L2 = new On3dPoint[3];
 			L2[0] = p1;
 			L2[1] = p2;
 			L2[2] = p3;		
 			mesh.Points.add(L[a]);
 			mesh.Points.add(L[b]);
 			mesh.Points.add(L2[c]);
 			if(d!=c){	mesh.Points.add(L2[d]);	mesh.faces.add(new MeshFace(0 + n, 1 + n, 2 + n, 3 + n));}
 			else{mesh.faces.add(new MeshFace(0 + n, 1 + n, 2 + n));}
 			mesh2.Points.add(L[e]);
 			mesh2.Points.add(L[f]);
 			mesh2.Points.add(L2[g]);
 			if(h!=g){	mesh2.Points.add(L2[h]);mesh2.faces.add(new MeshFace(0 + n2, 1 + n2, 2 + n2, 3 + n2));}
 			else{mesh2.faces.add(new MeshFace(0 + n2, 1 + n2, 2 + n2));}
 					
 			return true;
 		}
 	}
 	// {intersect_line1。from,intersect_line1.to ,face。v,face。v,
 	// intersect_line1。from,intersect_line1.to ,face。v ,face。v}
 	static int[][] TriLine = { 
 		{ -1, -1, -1, -1, -2, -2, -2, -2 },
 		{ 0, 2, 0, 0, 2, 0, 1, 2 }, 
 		{ 1, 0, 1, 1, 0, 1, 2, 0 },
 		{ 1, 2, 0, 1, 2, 1, 2, 2 }, 
 		{ 2, 1, 2, 2, 1, 2, 0, 1 },
 		{ 0, 1, 2, 0, 1, 0, 1,1 }, 
 		{ 2, 0, 1, 2, 0, 2, 0, 0 },
 		{ -2, -2, -2, -2, -1, -1, -1, -1 }, };
}
