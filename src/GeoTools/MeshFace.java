package GeoTools;

public class MeshFace{
	int a;
	int b;
	int c;
	int d;
	public MeshFace(int p1,int p2,int p3,int p4){
		a=p1;b=p2;c=p3;	d=p4;
	}
	public MeshFace(int p1,int p2,int p3){
		a=p1;b=p2;c=p3;	d=c;
	}
	public boolean istriangle(){if(c==d)return true;
	return false;
	}
	public boolean isquad(){if(c==d)return false;
	return true;
	}
}
