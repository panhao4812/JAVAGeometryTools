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
}
