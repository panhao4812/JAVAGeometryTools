package GeoTools;

import processing.core.PApplet;

public class OnXform {
	float m_xform[][] = new float[4][4]; // [i][j] = row i, column j. I.e.,
public float[][] get_m_xform(){
	return this.m_xform;	
}
	//
	// [0][0] [0][1] [0][2] [0][3]
	// [1][0] [1][1] [1][2] [1][3]
	// [2][0] [2][1] [2][2] [2][3]
	// [3][0] [3][1] [3][2] [3][3]

	public void printme() {
		PApplet.println(m_xform[0][0] + " " + m_xform[0][1] + " "
				+ m_xform[0][2] + " " + m_xform[0][3]);
		PApplet.println(m_xform[1][0] + " " + m_xform[1][1] + " "
				+ m_xform[1][2] + " " + m_xform[1][3]);
		PApplet.println(m_xform[2][0] + " " + m_xform[2][1] + " "
				+ m_xform[2][2] + " " + m_xform[2][3]);
		PApplet.println(m_xform[3][0] + " " + m_xform[3][1] + " "
				+ m_xform[3][2] + " " + m_xform[3][3]);
	}

	public String ToString() {
		String str = "";
		for (int i = 0; i < this.m_xform.length; i++) {
			for (int j = 0; j < this.m_xform[i].length; j++) {
				str += this.m_xform[i][j];
				str += "/";
			}
		}
		return str;
	}

	public OnXform(On3dPoint P, On3dVector X, On3dVector Y, On3dVector Z) {
		m_xform[0][0] = X.x;
		m_xform[1][0] = X.y;
		m_xform[2][0] = X.z;
		m_xform[3][0] = 0;

		m_xform[0][1] = Y.x;
		m_xform[1][1] = Y.y;
		m_xform[2][1] = Y.z;
		m_xform[3][1] = 0;

		m_xform[0][2] = Z.x;
		m_xform[1][2] = Z.y;
		m_xform[2][2] = Z.z;
		m_xform[3][2] = 0;

		m_xform[0][3] = P.x;
		m_xform[1][3] = P.y;
		m_xform[2][3] = P.z;
		m_xform[3][3] = 1;
	}

	public OnXform(float[][] m) {
		this.m_xform = m;
	}

	public OnXform() {
		this.Zero();
		this.m_xform[3][3] = 1;
	}

	public OnXform(int d) {
		this.Zero();
		m_xform[0][0] = m_xform[1][1] = m_xform[2][2] = (float) d;
		m_xform[3][3] = 1.0f;
	}

	public OnXform(float d) {
		this.Zero();
		m_xform[0][0] = m_xform[1][1] = m_xform[2][2] = d;
		m_xform[3][3] = 1.0f;
	}

	public void Zero() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				m_xform[i][j] = 0;
			}
		}
	}

	public void Identity() {
		Zero();
		m_xform[0][0] = m_xform[1][1] = m_xform[2][2] = m_xform[3][3] = 1.0f;
	}

	public void Rotation(float angle, On3dVector axis, On3dPoint center) {
		this.Rotation((float) Math.sin(angle), (float) Math.cos(angle), axis,
				center);
	}

	public void Rotation(On3dVector start_dir, On3dVector end_dir,
			On3dPoint rotation_center) {
		start_dir.Unitize();
		end_dir.Unitize();
		float cos_angle = On3dVector.OnDotProduct(start_dir, end_dir);
		On3dVector axis = On3dVector.OnCrossProduct(start_dir, end_dir);
		float sin_angle = axis.Length();
		if (0.0 == sin_angle) {
			axis.PerpendicularTo(start_dir);
			axis.Unitize();
			sin_angle = 0.0f;
			cos_angle = (cos_angle < 0.0f) ? -1.0f : 1.0f;
		}
		Rotation(sin_angle, cos_angle, axis, rotation_center);
	}

	public boolean ChangeBasis(On3dPoint P0, On3dVector X0, On3dVector Y0,
			On3dVector Z0, On3dPoint P1, On3dVector X1, On3dVector Y1,
			On3dVector Z1) {
		boolean rc = false;
		// Q = P0 + a0*X0 + b0*Y0 + c0*Z0 = P1 + a1*X1 + b1*Y1 + c1*Z1
		// then this transform will map the point (a0,b0,c0) to (a1,b1,c1)
	
		OnXform F0 = new OnXform(P0, X0, Y0, Z0); // Frame 0
	
		// T1 translates by -P1
		OnXform T1 = new OnXform();
		T1.Translation(-P1.x, -P1.y, -P1.z);
		OnXform CB = new OnXform();
		rc = CB.ChangeBasis(new On3dVector(1, 0, 0), new On3dVector(0, 1, 0),
				new On3dVector(0, 0, 1), X1, Y1, Z1);
		CB.mul(T1);
		CB.mul(F0);
		// System.out.println("cb="+CB.ToString());
		this.m_xform = CB.m_xform;
		return rc;
	}

	public void Rotation(float sin_angle, float cos_angle, On3dVector axis,
			On3dPoint center) {
		Identity();
		if (Math.abs(sin_angle) >= 1.0f - Float.MIN_VALUE
				&& Math.abs(cos_angle) <= Float.MIN_VALUE) {
			cos_angle = 0.0f;
			if (sin_angle < 0.0f)
				sin_angle = -1.0f;
			else
				sin_angle = 1.0f;
		} else if (Math.abs(cos_angle) >= 1.0f - Float.MIN_VALUE
				&& Math.abs(sin_angle) <= Float.MIN_VALUE) {
			sin_angle = 0.0f;
			if (cos_angle < 0.0f)
				cos_angle = -1.0f;
			else
				cos_angle = 1.0f;
		} else if (Math.abs(cos_angle * cos_angle + sin_angle * sin_angle
				- 1.0f) > Float.MIN_VALUE) {
			On3dVector cs = new On3dVector(cos_angle, sin_angle, 0);
			cs.Unitize();
			cos_angle = cs.x;
			sin_angle = cs.y;
		} else {
			if (cos_angle >= 1.0f) {
				sin_angle = 0.0f;
				cos_angle = 1.0f;
			} else if (cos_angle <= -1.0f) {
				sin_angle = 0.0f;
				cos_angle = -1.0f;
			}

			if (sin_angle >= 1.0f) {
				sin_angle = 1.0f;
				cos_angle = 0.0f;
			} else if (sin_angle <= -1.0f) {
				sin_angle = -1.0f;
				cos_angle = 0.0f;
			}
		}

		if (sin_angle != 0.0f || cos_angle != 1.0f) {
			final float one_minus_cos_angle = 1.0f - cos_angle;
			On3dVector a = axis;
			if (Math.abs(a.LengthSquared() - 1.0) > Float.MIN_VALUE)
				a.Unitize();

			m_xform[0][0] = (a.x * a.x * one_minus_cos_angle + cos_angle);
			m_xform[0][1] = (a.x * a.y * one_minus_cos_angle - a.z * sin_angle);
			m_xform[0][2] = (a.x * a.z * one_minus_cos_angle + a.y * sin_angle);

			m_xform[1][0] = a.y * a.x * one_minus_cos_angle + a.z * sin_angle;
			m_xform[1][1] = a.y * a.y * one_minus_cos_angle + cos_angle;
			m_xform[1][2] = a.y * a.z * one_minus_cos_angle - a.x * sin_angle;

			m_xform[2][0] = a.z * a.x * one_minus_cos_angle - a.y * sin_angle;
			m_xform[2][1] = a.z * a.y * one_minus_cos_angle + a.x * sin_angle;
			m_xform[2][2] = a.z * a.z * one_minus_cos_angle + cos_angle;

			if (center.x != 0.0 || center.y != 0.0 || center.z != 0.0) {
				m_xform[0][3] = -((m_xform[0][0] - 1.0f) * center.x
						+ m_xform[0][1] * center.y + m_xform[0][2] * center.z);
				m_xform[1][3] = -(m_xform[1][0] * center.x
						+ (m_xform[1][1] - 1.0f) * center.y + m_xform[1][2]
						* center.z);
				m_xform[2][3] = -(m_xform[2][0] * center.x + m_xform[2][1]
						* center.y + (m_xform[2][2] - 1.0f) * center.z);
			}

			m_xform[3][0] = m_xform[3][1] = m_xform[3][2] = 0.0f;
			m_xform[3][3] = 1.0f;
		}
	}

	public void mul(OnXform rhs) {
		float m[][] = new float[4][4];
		float p[] = new float[16];
		int index = 0;
		for (int i = 0; i < rhs.m_xform.length; i++) {
			for (int j = 0; j < rhs.m_xform[i].length; j++) {
				p[index++] = rhs.m_xform[i][j];
			}
		}
		m[0][0] = m_xform[0][0] * p[0] + m_xform[0][1] * p[4] + m_xform[0][2]
				* p[8] + m_xform[0][3] * p[12];
		m[0][1] = m_xform[0][0] * p[1] + m_xform[0][1] * p[5] + m_xform[0][2]
				* p[9] + m_xform[0][3] * p[13];
		m[0][2] = m_xform[0][0] * p[2] + m_xform[0][1] * p[6] + m_xform[0][2]
				* p[10] + m_xform[0][3] * p[14];
		m[0][3] = m_xform[0][0] * p[3] + m_xform[0][1] * p[7] + m_xform[0][2]
				* p[11] + m_xform[0][3] * p[15];

		m[1][0] = m_xform[1][0] * p[0] + m_xform[1][1] * p[4] + m_xform[1][2]
				* p[8] + m_xform[1][3] * p[12];
		m[1][1] = m_xform[1][0] * p[1] + m_xform[1][1] * p[5] + m_xform[1][2]
				* p[9] + m_xform[1][3] * p[13];
		m[1][2] = m_xform[1][0] * p[2] + m_xform[1][1] * p[6] + m_xform[1][2]
				* p[10] + m_xform[1][3] * p[14];
		m[1][3] = m_xform[1][0] * p[3] + m_xform[1][1] * p[7] + m_xform[1][2]
				* p[11] + m_xform[1][3] * p[15];

		m[2][0] = m_xform[2][0] * p[0] + m_xform[2][1] * p[4] + m_xform[2][2]
				* p[8] + m_xform[2][3] * p[12];
		m[2][1] = m_xform[2][0] * p[1] + m_xform[2][1] * p[5] + m_xform[2][2]
				* p[9] + m_xform[2][3] * p[13];
		m[2][2] = m_xform[2][0] * p[2] + m_xform[2][1] * p[6] + m_xform[2][2]
				* p[10] + m_xform[2][3] * p[14];
		m[2][3] = m_xform[2][0] * p[3] + m_xform[2][1] * p[7] + m_xform[2][2]
				* p[11] + m_xform[2][3] * p[15];

		m[3][0] = m_xform[3][0] * p[0] + m_xform[3][1] * p[4] + m_xform[3][2]
				* p[8] + m_xform[3][3] * p[12];
		m[3][1] = m_xform[3][0] * p[1] + m_xform[3][1] * p[5] + m_xform[3][2]
				* p[9] + m_xform[3][3] * p[13];
		m[3][2] = m_xform[3][0] * p[2] + m_xform[3][1] * p[6] + m_xform[3][2]
				* p[10] + m_xform[3][3] * p[14];
		m[3][3] = m_xform[3][0] * p[3] + m_xform[3][1] * p[7] + m_xform[3][2]
				* p[11] + m_xform[3][3] * p[15];

		this.m_xform = m;
	}

	public static OnXform XformMul(OnXform xf1, OnXform rhs) {
		float m[][] = new float[4][4];
		float p[] = new float[16];
		int index = 0;
		for (int i = 0; i < rhs.m_xform.length; i++) {
			for (int j = 0; j < rhs.m_xform[i].length; j++) {
				p[index++] = rhs.m_xform[i][j];
			}
		}
		m[0][0] = xf1.m_xform[0][0] * p[0] + xf1.m_xform[0][1] * p[4]
				+ xf1.m_xform[0][2] * p[8] + xf1.m_xform[0][3] * p[12];
		m[0][1] = xf1.m_xform[0][0] * p[1] + xf1.m_xform[0][1] * p[5]
				+ xf1.m_xform[0][2] * p[9] + xf1.m_xform[0][3] * p[13];
		m[0][2] = xf1.m_xform[0][0] * p[2] + xf1.m_xform[0][1] * p[6]
				+ xf1.m_xform[0][2] * p[10] + xf1.m_xform[0][3] * p[14];
		m[0][3] = xf1.m_xform[0][0] * p[3] + xf1.m_xform[0][1] * p[7]
				+ xf1.m_xform[0][2] * p[11] + xf1.m_xform[0][3] * p[15];

		m[1][0] = xf1.m_xform[1][0] * p[0] + xf1.m_xform[1][1] * p[4]
				+ xf1.m_xform[1][2] * p[8] + xf1.m_xform[1][3] * p[12];
		m[1][1] = xf1.m_xform[1][0] * p[1] + xf1.m_xform[1][1] * p[5]
				+ xf1.m_xform[1][2] * p[9] + xf1.m_xform[1][3] * p[13];
		m[1][2] = xf1.m_xform[1][0] * p[2] + xf1.m_xform[1][1] * p[6]
				+ xf1.m_xform[1][2] * p[10] + xf1.m_xform[1][3] * p[14];
		m[1][3] = xf1.m_xform[1][0] * p[3] + xf1.m_xform[1][1] * p[7]
				+ xf1.m_xform[1][2] * p[11] + xf1.m_xform[1][3] * p[15];

		m[2][0] = xf1.m_xform[2][0] * p[0] + xf1.m_xform[2][1] * p[4]
				+ xf1.m_xform[2][2] * p[8] + xf1.m_xform[2][3] * p[12];
		m[2][1] = xf1.m_xform[2][0] * p[1] + xf1.m_xform[2][1] * p[5]
				+ xf1.m_xform[2][2] * p[9] + xf1.m_xform[2][3] * p[13];
		m[2][2] = xf1.m_xform[2][0] * p[2] + xf1.m_xform[2][1] * p[6]
				+ xf1.m_xform[2][2] * p[10] + xf1.m_xform[2][3] * p[14];
		m[2][3] = xf1.m_xform[2][0] * p[3] + xf1.m_xform[2][1] * p[7]
				+ xf1.m_xform[2][2] * p[11] + xf1.m_xform[2][3] * p[15];

		m[3][0] = xf1.m_xform[3][0] * p[0] + xf1.m_xform[3][1] * p[4]
				+ xf1.m_xform[3][2] * p[8] + xf1.m_xform[3][3] * p[12];
		m[3][1] = xf1.m_xform[3][0] * p[1] + xf1.m_xform[3][1] * p[5]
				+ xf1.m_xform[3][2] * p[9] + xf1.m_xform[3][3] * p[13];
		m[3][2] = xf1.m_xform[3][0] * p[2] + xf1.m_xform[3][1] * p[6]
				+ xf1.m_xform[3][2] * p[10] + xf1.m_xform[3][3] * p[14];
		m[3][3] = xf1.m_xform[3][0] * p[3] + xf1.m_xform[3][1] * p[7]
				+ xf1.m_xform[3][2] * p[11] + xf1.m_xform[3][3] * p[15];
		return new OnXform(m);
	}

	public void Translation(On3dVector v) {
		Identity();
		m_xform[0][3] = v.x;
		m_xform[1][3] = v.y;
		m_xform[2][3] = v.z;
		m_xform[3][3] = 1.0f;
	}

	public void Translation(float x, float y, float z) {
		Identity();
		m_xform[0][3] = x;
		m_xform[1][3] = y;
		m_xform[2][3] = z;
		m_xform[3][3] = 1.0f;
	}

	public boolean ChangeBasis(OnPlane plane0, // initial plane
			OnPlane plane1 // final plane
	) {
		return ChangeBasis(plane0.origin, plane0.xaxis, plane0.yaxis,
				plane0.zaxis, plane1.origin, plane1.xaxis, plane1.yaxis,
				plane1.zaxis);
	}

	public boolean ChangeBasis(On3dVector X0, On3dVector Y0, On3dVector Z0,
			On3dVector X1, On3dVector Y1, On3dVector Z1) {
		// Q = a0*X0 + b0*Y0 + c0*Z0 = a1*X1 + b1*Y1 + c1*Z1
		// then this transform will map the point (a0,b0,c0) to (a1,b1,c1)
		Zero();
		m_xform[3][3] = 1;
		float a, b, c, d;
		a = On3dVector.OnDotProduct(X1, Y1);
		b = On3dVector.OnDotProduct(X1, Z1);
		c = On3dVector.OnDotProduct(Y1, Z1);
		float R[][] = {
				{ On3dVector.OnDotProduct(X1, X1), a, b,
						On3dVector.OnDotProduct(X1, X0),
						On3dVector.OnDotProduct(X1, Y0),
						On3dVector.OnDotProduct(X1, Z0) },
				{ a, On3dVector.OnDotProduct(Y1, Y1), c,
						On3dVector.OnDotProduct(Y1, X0),
						On3dVector.OnDotProduct(Y1, Y0),
						On3dVector.OnDotProduct(Y1, Z0) },
				{ b, c, On3dVector.OnDotProduct(Z1, Z1),
						On3dVector.OnDotProduct(Z1, X0),
						On3dVector.OnDotProduct(Z1, Y0),
						On3dVector.OnDotProduct(Z1, Z0) } };
		// float R[3][6] = {{X1*X1, a, b, X0*X1, X0*Y1, X0*Z1},
		// { a, Y1*Y1, c, Y0*X1, Y0*Y1, Y0*Z1},
		// { b, c, Z1*Z1, Z0*X1, Z0*Y1, Z0*Z1}};
		// row reduce R
		int i0 = (R[0][0] >= R[1][1]) ? 0 : 1;
		if (R[2][2] > R[i0][i0])
			i0 = 2;
		int i1 = (i0 + 1) % 3;
		int i2 = (i1 + 1) % 3;
		if (R[i0][i0] == 0.0)
			return false;
		d = 1.0f / R[i0][i0];
		R[i0][0] *= d;
		R[i0][1] *= d;
		R[i0][2] *= d;
		R[i0][3] *= d;
		R[i0][4] *= d;
		R[i0][5] *= d;
		R[i0][i0] = 1f;
		if (R[i1][i0] != 0.0) {
			d = -R[i1][i0];
			R[i1][0] += d * R[i0][0];
			R[i1][1] += d * R[i0][1];
			R[i1][2] += d * R[i0][2];
			R[i1][3] += d * R[i0][3];
			R[i1][4] += d * R[i0][4];
			R[i1][5] += d * R[i0][5];
			R[i1][i0] = 0.0f;
		}
		if (R[i2][i0] != 0.0) {
			d = -R[i2][i0];
			R[i2][0] += d * R[i0][0];
			R[i2][1] += d * R[i0][1];
			R[i2][2] += d * R[i0][2];
			R[i2][3] += d * R[i0][3];
			R[i2][4] += d * R[i0][4];
			R[i2][5] += d * R[i0][5];
			R[i2][i0] = 0f;
		}

		if (Math.abs(R[i1][i1]) < Math.abs(R[i2][i2])) {
			int i = i1;
			i1 = i2;
			i2 = i;
		}
		if (R[i1][i1] == 0.0)
			return false;
		d = 1.0f / R[i1][i1];
		R[i1][0] *= d;
		R[i1][1] *= d;
		R[i1][2] *= d;
		R[i1][3] *= d;
		R[i1][4] *= d;
		R[i1][5] *= d;
		R[i1][i1] = 1.0f;
		if (R[i0][i1] != 0.0) {
			d = -R[i0][i1];
			R[i0][0] += d * R[i1][0];
			R[i0][1] += d * R[i1][1];
			R[i0][2] += d * R[i1][2];
			R[i0][3] += d * R[i1][3];
			R[i0][4] += d * R[i1][4];
			R[i0][5] += d * R[i1][5];
			R[i0][i1] = 0.0f;
		}
		if (R[i2][i1] != 0.0) {
			d = -R[i2][i1];
			R[i2][0] += d * R[i1][0];
			R[i2][1] += d * R[i1][1];
			R[i2][2] += d * R[i1][2];
			R[i2][3] += d * R[i1][3];
			R[i2][4] += d * R[i1][4];
			R[i2][5] += d * R[i1][5];
			R[i2][i1] = 0.0f;
		}

		if (R[i2][i2] == 0.0)
			return false;
		d = 1.0f / R[i2][i2];
		R[i2][0] *= d;
		R[i2][1] *= d;
		R[i2][2] *= d;
		R[i2][3] *= d;
		R[i2][4] *= d;
		R[i2][5] *= d;
		R[i2][i2] = 1.0f;
		if (R[i0][i2] != 0.0) {
			d = -R[i0][i2];
			R[i0][0] += d * R[i2][0];
			R[i0][1] += d * R[i2][1];
			R[i0][2] += d * R[i2][2];
			R[i0][3] += d * R[i2][3];
			R[i0][4] += d * R[i2][4];
			R[i0][5] += d * R[i2][5];
			R[i0][i2] = 0.0f;
		}
		if (R[i1][i2] != 0.0) {
			d = -R[i1][i2];
			R[i1][0] += d * R[i2][0];
			R[i1][1] += d * R[i2][1];
			R[i1][2] += d * R[i2][2];
			R[i1][3] += d * R[i2][3];
			R[i1][4] += d * R[i2][4];
			R[i1][5] += d * R[i2][5];
			R[i1][i2] = 0.0f;
		}

		m_xform[0][0] = R[0][3];
		m_xform[0][1] = R[0][4];
		m_xform[0][2] = R[0][5];

		m_xform[1][0] = R[1][3];
		m_xform[1][1] = R[1][4];
		m_xform[1][2] = R[1][5];

		m_xform[2][0] = R[2][3];
		m_xform[2][1] = R[2][4];
		m_xform[2][2] = R[2][5];

		return true;
	}

	public void Scale(float x, float y, float z) {
		this.Zero();
		m_xform[0][0] = x;
		m_xform[1][1] = y;
		m_xform[2][2] = z;
		m_xform[3][3] = 1.0f;
	}

	public void Scale(On3dVector v) {
		this.Zero();
		m_xform[0][0] = v.x;
		m_xform[1][1] = v.y;
		m_xform[2][2] = v.z;
		m_xform[3][3] = 1.0f;
	}

	public void Scale(On3dPoint fixed_point, float scale_factor) {
		if (fixed_point.x == 0.0 && fixed_point.y == 0.0
				&& fixed_point.z == 0.0) {
			Scale(scale_factor, scale_factor, scale_factor);
		} else {
			OnXform t0 = new OnXform(), t1 = new OnXform(), s = new OnXform();
			t0.Translation(On3dPoint.PointSub(new On3dPoint(), fixed_point));
			s.Scale(scale_factor, scale_factor, scale_factor);
			t1.Translation(On3dPoint.PointSub(fixed_point, new On3dPoint()));
			this.m_xform = t1.get_m_xform();
			this.mul(s);
			this.mul(t0);
		}
	}

	public void Scale(OnPlane plane, float x_scale_factor,
			float y_scale_factor, float z_scale_factor) {
		Shear(plane, On3dVector.VectorMul(x_scale_factor, plane.xaxis),
				On3dVector.VectorMul(y_scale_factor, plane.yaxis),
				On3dVector.VectorMul(z_scale_factor, plane.zaxis));
	}

	public void Shear(OnPlane plane, On3dVector x1, On3dVector y1, On3dVector z1) {
		OnXform t0 = new OnXform(), t1 = new OnXform(), s0 = new OnXform(1), s1 = new OnXform(
				1);
		t0.Translation(On3dPoint.PointSub(new On3dPoint(), plane.origin));
		s0.m_xform[0][0] = plane.xaxis.x;
		s0.m_xform[0][1] = plane.xaxis.y;
		s0.m_xform[0][2] = plane.xaxis.z;
		s0.m_xform[1][0] = plane.yaxis.x;
		s0.m_xform[1][1] = plane.yaxis.y;
		s0.m_xform[1][2] = plane.yaxis.z;
		s0.m_xform[2][0] = plane.zaxis.x;
		s0.m_xform[2][1] = plane.zaxis.y;
		s0.m_xform[2][2] = plane.zaxis.z;
		s1.m_xform[0][0] = x1.x;
		s1.m_xform[1][0] = x1.y;
		s1.m_xform[2][0] = x1.z;
		s1.m_xform[0][1] = y1.x;
		s1.m_xform[1][1] = y1.y;
		s1.m_xform[2][1] = y1.z;
		s1.m_xform[0][2] = z1.x;
		s1.m_xform[1][2] = z1.y;
		s1.m_xform[2][2] = z1.z;
		t1.Translation(On3dPoint.PointSub(plane.origin, new On3dPoint()));
		this.m_xform = t1.get_m_xform();
		this.mul(s1);
		this.mul(s0);
		this.mul(t0);
	}
	public static void DrawWhiteGrid(PApplet p) {
		float gridSize = 4;
		// strokeWeight(0.5f);
		for (int x = -64; x < 65; x++) {
			p.stroke(250, 12);
			if (x % 8 == 0) {
				p.stroke(255, 32);
			}
			if (x == 0) {
				p.line(0, 0, 0, -254, 0, 0);
			} else {
				p.line(x * gridSize, -64 * gridSize, 0, x * gridSize,
						64 * gridSize, 0);
			}
		}

		for (int y = -64; y < 65; y++) {
			p.stroke(250, 12);
			if (y % 8 == 0) {
				p.stroke(255, 32);
			}
			if (y == 0) {
				p.line(0, 0, 0, 0, -254, 0);
			} else {
				p.line(-64 * gridSize, y * gridSize, 0, 64 * gridSize, y
						* gridSize, 0);
			}
		}
		// strokeWeight(1f);
		p.stroke(255, 0, 0);
		p.line(0, 0, 0, 254, 0, 0);
		p.stroke(0, 255, 0);
		p.line(0, 0, 0, 0, 254, 0);

		// stroke(0,0,255);
		// line(0,0,0,0,0,254);
	}
	public static void DrawDarkGrid(PApplet p) {
		float gridSize = 4;
		// strokeWeight(0.5f);
		for (int x = -64; x < 65; x++) {
			p.stroke(0, 12);
			if (x % 8 == 0) {
				p.stroke(0, 32);
			}
			if (x == 0) {
				p.line(0, 0, 0, -254, 0, 0);
			} else {
				p.line(x * gridSize, -64 * gridSize, 0, x * gridSize,
						64 * gridSize, 0);
			}
		}

		for (int y = -64; y < 65; y++) {
			p.stroke(0, 12);
			if (y % 8 == 0) {
				p.stroke(0, 32);
			}
			if (y == 0) {
				p.line(0, 0, 0, 0, -254, 0);
			} else {
				p.line(-64 * gridSize, y * gridSize, 0, 64 * gridSize, y
						* gridSize, 0);
			}
		}
		// strokeWeight(1f);
		p.stroke(255, 0, 0);
		p.line(0, 0, 0, 254, 0, 0);
		p.stroke(0, 255, 0);
		p.line(0, 0, 0, 0, 254, 0);

		// stroke(0,0,255);
		// line(0,0,0,0,0,254);
	}

	public static void CreatCoordinateSystem(PApplet p) {
		p.pushMatrix();
		p.stroke(255, 0, 0);
		p.line(0, 0, 0, 500, 0, 0);
		p.stroke(0, 255, 0);
		p.line(0, 0, 0, 0, 500, 0);
		p.stroke(0, 0, 255);
		p.line(0, 0, 0, 0, 0, 500);
		p.popMatrix();

	}
}
