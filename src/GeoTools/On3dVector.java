package GeoTools;

public class On3dVector {
	public float x, y, z;
	/////////////////////////////////
	public  static On3dVector Xaxis(){return new On3dVector (1,0,0);}
	public  static On3dVector Yaxis(){return new On3dVector (0,1,0);}
	public  static On3dVector Zaxis(){return new On3dVector (0,0,1);}
	public void Rotate(float angle){
		 Rotate(angle,On3dVector.Zaxis());
	}
	public void Rotate(float angle,On3dVector axis )
		{
		  Rotate((float) Math.sin(angle), (float) Math.cos(angle), axis );
		}
	public void Rotate( float sin_angle,float cos_angle, On3dVector axis )
		{
		  //bool rc = false;
		  OnXform rot=new OnXform();
		  rot.Rotation( sin_angle, cos_angle, axis, new On3dPoint(0,0,0) );
		  Transform(rot);
		}
	
	public void Transform(OnXform xform )
	{
		float xx,yy,zz;
		  xx = xform.m_xform[0][0]*x + xform.m_xform[0][1]*y + xform.m_xform[0][2]*z;
		  yy = xform.m_xform[1][0]*x + xform.m_xform[1][1]*y + xform.m_xform[1][2]*z;
		  zz = xform.m_xform[2][0]*x + xform.m_xform[2][1]*y + xform.m_xform[2][2]*z;
		  x = xx;
		  y = yy;
		  z = zz;
	  }
	public On3dVector()
	  {
	x = y = z = 0;
	  };
	public On3dVector( float xx, float yy, float zz )
	  {
	x = xx;
	y = yy;
	z = zz;
	  };
	public On3dVector( double xx, double yy, double zz )
		  {
		x = (float)xx;
		y = (float)yy;
		z = (float)zz;
		  };  	  
	public On3dVector( On3dVector v ){
		x = v.x;
		y = v.y;
		z = v.z;
	}
	public float Length()
	  {
	    return (float)Math.sqrt( (x*x) + (y*y) + (z*z) );
	  } 
	public void Unitize() 
	  {
	     float m = this.Length();
	     if (m > 0) 
	     {
	        this.div(m);
	     }
	  }
	public void mul( On3dVector v )
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
	public void div( On3dVector v )
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
	public void sub( On3dVector v )
	  {
	    x -= v.x;
	    y -= v.y;
	    z -= v.z;
	  }
	public void add( On3dVector v )
	  {
	    x += v.x;
	    y += v.y;
	    z += v.z;
	  }	
	boolean PerpendicularTo(On3dVector v)
	{
	  //bool rc = false;
	  int i, j, k; 
	  float a, b;
	  k = 2;
	  if ( Math.abs(v.y) > Math.abs(v.x) ) {
	    if ( Math.abs(v.z) > Math.abs(v.y) ) {
	      // |v.z| > |v.y| > |v.x|
	      i = 2;
	      j = 1;
	      k = 0;
	      a = v.z;
	      b = -v.y;
	    }
	    else if ( Math.abs(v.z) >= Math.abs(v.x) ){
	      // |v.y| >= |v.z| >= |v.x|
	      i = 1;
	      j = 2;
	      k = 0;
	      a = v.y;
	      b = -v.z;
	    }
	    else {
	      // |v.y| > |v.x| > |v.z|
	      i = 1;
	      j = 0;
	      k = 2;
	      a = v.y;
	      b = -v.x;
	    }
	  }
	  else if ( Math.abs(v.z) > Math.abs(v.x) ) {
	    // |v.z| > |v.x| >= |v.y|
	    i = 2;
	    j = 0;
	    k = 1;
	    a = v.z;
	    b = -v.x;
	  }
	  else if ( Math.abs(v.z) > Math.abs(v.y) ) {
	    // |v.x| >= |v.z| > |v.y|
	    i = 0;
	    j = 2;
	    k = 1;
	    a = v.x;
	    b = -v.z;
	  }
	  else {
	    // |v.x| >= |v.y| >= |v.z|
	    i = 0;
	    j = 1;
	    k = 2;
	    a = v.x;
	    b = -v.y;
	  }
	  float this_v[]=new float[3]; 	
	  this_v[i] = b;
	  this_v[j] = a;
	  this_v[k] = 0.0f;
	  this.x=this_v[0];
	  this.y=this_v[1];
	  this.z=this_v[2];
	  return (a != 0.0) ? true : false;
	}
	//////////////////////////////////////////
	  public static On3dVector VectorAdd( On3dVector a, On3dVector b )
	  {
	    return new On3dVector( a.x+b.x, a.y+b.y, a.z+b.z );
	  }
	  public static On3dVector VectorSub( On3dVector a, On3dVector b )
	  {
	    return new On3dVector( a.x-b.x, a.y-b.y, a.z-b.z );
	  } 
	  public static On3dVector VectorMul( On3dVector a, On3dVector b )
	  {
	    return new On3dVector( a.x*b.x, a.y*b.y, a.z*b.z );
	  }
	  public static On3dVector VectorMul(  float b,On3dVector a )
		  {
			    return new On3dVector( a.x*b, a.y*b, a.z*b );
		 }
	  public static On3dVector VectorMul( On3dVector a, float b )
	  {
	    return new On3dVector( a.x*b, a.y*b, a.z*b );
	  }
	  public static float OnDotProduct( On3dVector v1, On3dVector v2 )
	  {
	    return ( v1.x*v2.x + v1.y*v2.y + v1.z*v2.z );
	  } 
	  public static On3dVector OnCrossProduct( On3dVector a, On3dVector b )
	  {
	     float crossX =(a.y * b.z - b.y * a.z);
	     float crossY = (a.z * b.x - b.z * a.x);
	     float crossZ = (a.x * b.y - b.x * a.y);
	     return( new On3dVector(crossX, crossY, crossZ) );
	  }
	  public static double VectorAngle( On3dVector a, On3dVector b){
		  a.Unitize() ;b.Unitize();	 
		    double d = ((a.x * b.x) + (a.y * b.y)) + (a.z * b.z);
		    if (d > 1.0)
		    {
		        d = 1.0;
		    }
		    if (d < -1.0)
		    {
		        d = -1.0;
		    }
		    return Math.acos(d);  
	  }
	//////////////////////////////
	public float LengthSquared() {
		  return (x*x + y*y + z*z);
	}
	}

