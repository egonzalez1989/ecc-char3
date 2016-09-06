package crypto.ecc;

import math.algebra.group.ECPoint;
import crypto.ecc.finitefields.GF3Poly;
import crypto.ecc.finitefields.GF3n;
import crypto.ecc.finitefields.GF3nElement;
import crypto.ecc.finitefields.NoCommonFiniteFieldException;
import crypto.ecc.finitefields.NotInFiniteFieldException;

/**
 * @author egonzalez
 *
 */
public class HEC3nPoint extends ECPoint<GF3Poly, GF3n, GF3nElement, HEC3n, HEC3nPoint> {
	
//	private static final String MULTIPLICATION_TYPE = "NAF"; 
	
	public HEC3nPoint(HEC3n E) {
		super(E.getFiniteField().one(), E.getFiniteField().one().negate(), E.getFiniteField().zero(), E);
	}
	
	/**
	 * Constructs an elliptic curve point with x and y coordinates
	 * @param x	X-coordinate
	 * @param y Y-coordinate
	 * @param E Elliptic curve where the point will live
	 */
	public HEC3nPoint(GF3nElement x, GF3nElement y, HEC3n E) {
		super(x, y, E.getFiniteField().one(), E);
	}
	
	/**
	 * Constructs a new elliptic curve point which is a copy of the argument
	 * @param P Point ro copy
	 */
	private HEC3nPoint(HEC3nPoint P) {
		super(P.getX(), P.getY(), P.getZ(), P.getE());
	}
	
	public HEC3nPoint(GF3nElement x, GF3nElement y, GF3nElement z, HEC3n E) {
		super(x, y, z, E);
	}

	@Override
	public HEC3nPoint sum(HEC3nPoint Q) {
		// if some of the points is the 0 element
		if (Q.getZ().equals(Q.getGroundField().zero())) {
			return new HEC3nPoint(this);
		}
		if (this.getZ().equals(this.getGroundField().zero())) {
			return new HEC3nPoint(Q);
		}
		HEC3nPoint P = this;
		// initialize the elements to be summed
		GF3nElement x2 = P.getX();
		GF3nElement y2 = P.getY();
		GF3nElement z2 = this.getGroundField().one();
		GF3nElement x1 = Q.getX();
		GF3nElement y1 = Q.getY();
		//
		GF3nElement O[] = new GF3nElement[13];
		try {
			O[0] = y1.multiply(x2);
			O[1] = y1.multiply(z2);
			O[2] = x1.multiply(y2);
			O[3] = x1.multiply(z2);
			O[4] = O[0].multiply(O[1]);
			O[5] = O[2].multiply(O[3]);
			O[6] = x2.multiply(y2);
			O[7] = O[3].multiply(y2);
			O[8] = O[1].multiply(x2);
			O[9] = O[2].multiply(O[4]);
			//
			O[10] = O[4].sum(O[7].negate());
			O[11] = O[5].sum(O[8].negate());
			O[12] = O[6].sum(O[9].negate());
		}
		catch(NotInFiniteFieldException e){}
		catch(NoCommonFiniteFieldException e1){}
		return new HEC3nPoint(O[10], O[11], O[12], this.getE());
	}
	
	@Override
	public HEC3nPoint duplicate() {
		if (this.getZ().equals(getGroundField().zero()))
			return new HEC3nPoint(this);
		HEC3nPoint P = this;
		// initialize the elments to be sumed
		GF3nElement x1 = P.getX();
		GF3nElement y1 = P.getY();
		GF3nElement z1 = P.getZ();
		//
		GF3nElement[] O = new GF3nElement[6];
		try {
			O[0] = (z1.sum(x1.negate())).pow(3);
			O[1] = (y1.sum(z1.negate())).pow(3);
			O[2] = (x1.sum(y1.negate())).pow(3);
			O[3] = y1.multiply(O[0]);
			O[4] = x1.multiply(O[1]);
			O[5] = z1.multiply(O[2]);
		}
		catch(NoCommonFiniteFieldException e){}
		catch(NotInFiniteFieldException r){}
		return new HEC3nPoint(O[3], O[4], O[5], this.getE());
	}
	
	@Override
	public HEC3nPoint negate() {
		return new HEC3nPoint(this.getY(), this.getX(), this.getZ(), this.getE());
	}
	
	public String toString() {
		return "(" + this.getX().toString() + " , " + this.getY().toString() + " , " + this.getZ().toString() + ")";
	}

	@Override
	public HEC3nPoint multiply(int k) {
		return this.nafSum(k);
	}
}