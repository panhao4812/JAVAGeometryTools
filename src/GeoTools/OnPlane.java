package GeoTools;
public class OnPlane {

public On3dPoint  origin;
public On3dVector xaxis;
public On3dVector yaxis;
public On3dVector zaxis;
OnPlaneEquation plane_equation=new OnPlaneEquation();
//double equation[4];
public OnPlane(){
	origin=new On3dPoint(0,0,0);
    xaxis=new On3dVector(1,0,0);
yaxis=new On3dVector(0,1,0); 
zaxis=new On3dVector(0,0,1);
plane_equation.x = plane_equation.y = plane_equation.d = 0;
plane_equation.z = 1;
}
	 OnPlane(On3dPoint P,On3dVector N)
		{	
		origin = new On3dPoint(P);
		zaxis = new On3dVector(N);
		zaxis.Unitize();
		 xaxis=new On3dVector(1,0,0);
		  xaxis.PerpendicularTo( zaxis );
		  xaxis.Unitize();
		  yaxis = On3dVector.OnCrossProduct( zaxis, xaxis );
		  yaxis.Unitize();		  
		  plane_equation.Create(P, zaxis);		
		}

public static OnPlane World_xy=new OnPlane(new On3dPoint(0,0,0),new On3dVector(0,0,1));
public  static OnPlane World_yz=new OnPlane(new On3dPoint(0 ,0 ,0) ,new On3dVector(1,0,0));
public  static OnPlane World_xz=new OnPlane(new  On3dPoint(0 ,0 ,0) ,new On3dVector(0,1,0));

///////////////////
	class OnPlaneEquation{
		public float x,y,z,d;
		public OnPlaneEquation(){
			x=0;y=0;z=0;d=0;			
		}
		public void Create( On3dPoint P, On3dVector N ){
			x = N.x;
		    y = N.y;
		    z = N.z;
		    d = -(x*P.x + y*P.y + z*P.z);		
		}
	}
}
