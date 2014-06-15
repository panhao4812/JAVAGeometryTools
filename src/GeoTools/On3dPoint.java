package GeoTools;
public class On3dPoint {
	public float x, y, z;
	//////////////////////////////constructor
	public On3dPoint()
	  {
	x = y = z = 0;
	  };
	public On3dPoint( float xx, float yy, float zz )
	  {
	x = xx;
	y = yy;
	z = zz;
	  };
	public On3dPoint( double xx, double yy, double zz )
		  {
		x = (float)xx;
		y = (float)yy;
		z = (float)zz;
		  };  	  
	public On3dPoint( On3dPoint v )
	  {
	x = v.x;
	y = v.y;
	z = v.z;
	  }
	public On3dPoint( On3dVector v )
		  {
		x = v.x;
		y = v.y;
		z = v.z;
		  }
	  /////////////////////////////////methods
		public void Transform(OnXform xform )
		{
		  float xx,yy,zz,ww;
		  
		    ww = (xform.m_xform[3][0]*x + xform.m_xform[3][1]*y + xform.m_xform[3][2]*z + xform.m_xform[3][3]);
		    if ( ww != 0.0f )ww = 1.0f/ww;
		    xx = ww*(xform.m_xform[0][0]*x + xform.m_xform[0][1]*y + xform.m_xform[0][2]*z + xform.m_xform[0][3]);
		    yy = (ww*(xform.m_xform[1][0]*x + xform.m_xform[1][1]*y + xform.m_xform[1][2]*z + xform.m_xform[1][3]));
		    zz = ww*(xform.m_xform[2][0]*x + xform.m_xform[2][1]*y + xform.m_xform[2][2]*z + xform.m_xform[2][3]);
		    x = xx;
		    y = yy;
		    z = zz;
		  }
	public float DistanceTo(On3dPoint v2){
		float dx = x - v2.x;
	    float dy = y- v2.y;
	    float dz = z - v2.z;
	    return (float)Math.sqrt(dx*dx + dy*dy + dz*dz);
	}
	public void set( float xx, float yy, float zz )
	  {
	x = xx;
	y = yy;
	z = zz;
	  };
	public void set( On3dPoint v )
	  {
	x = v.x;
	y = v.y;
	z = v.z;
	  };
	public void mul( On3dPoint v )
	  {
	    x *= v.x;
	    y *= v.y;
	    z *= v.z;
	  }
	public void mul( float s )
	  {
	    x *= s;
	    y *= s;
	    z *= s;
	  }
	public void div( On3dPoint v )
	  {
	    x /= v.x;
	    y /= v.y;
	    z /= v.z;
	  }	
	public void div( float s )
	  {
	    x /= s;
	    y /= s;
	    z /= s;
	  }
	public void sub( On3dPoint v )
	  {
	    x -= v.x;
	    y -= v.y;
	    z -= v.z;
	  }
	public void add( On3dPoint v )
	  {
	    x += v.x;
	    y += v.y;
	    z += v.z;
	  }
	public void add( On3dVector v )
	  {
	    x += v.x;
	    y += v.y;
	    z += v.z;
	  }
	public float dot(On3dPoint v )
	  {
	    return ( x*v.x + y*v.y + z*v.z );
	  }
	public float Length()
	  {
	    return (float)Math.sqrt( (x*x) + (y*y) + (z*z) );
	  } 
	/////////////////////////////////////////Static functions
	public static On3dPoint  PointMul(On3dPoint p, float s )
	  {
	 float x=  p.x * s;
	 float y = p.y * s;
	 float z  =p.z * s;
	    return new On3dPoint(x,y,z);
	  }
	 public static On3dPoint PointAdd( On3dPoint a, On3dPoint b )
	  {
	    return new On3dPoint( a.x+b.x, a.y+b.y, a.z+b.z );
	  }
	 public static On3dPoint PointAdd( On3dPoint a, On3dVector b )
	  {
	    return new On3dPoint( a.x+b.x, a.y+b.y, a.z+b.z );
	  }
	 public static On3dVector PointSub( On3dPoint a, On3dPoint b )
	  {
	    return new On3dVector( a.x-b.x, a.y-b.y, a.z-b.z );
	  } 
	 public static On3dPoint PointSub( On3dPoint a, On3dVector b )
	  {
	    return new On3dPoint( a.x-b.x, a.y-b.y, a.z-b.z );
	  } 
	 public static float DistanceTo( On3dPoint v1, On3dPoint v2 )
	  {
	    float dx = v1.x - v2.x;
	    float dy = v1.y - v2.y;
	    float dz = v1.z - v2.z;
	    return (float)Math.sqrt(dx*dx + dy*dy + dz*dz);
	  }
//////////////////////////////////////
}
