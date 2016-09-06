package math.algebra.group;

import java.util.ArrayList;

import math.algebra.AlgorithmHelper;
import math.algebra.field.FiniteField;
import math.algebra.field.FiniteFieldElementImpl;
import math.algebra.polynomial.Polynomial;
import crypto.ecc.HEC3nPoint;

/**
 * To establish all the elements of an elliptic curve point we need to ensure that the clases of the wild card are
 * completely defined. This helps us to use the methods arbitrarily in the implementation used by nan elliptic curve
 * @author egonzalez
 *
 * @param <P> Polynomial
 * @param <F> FiniteField
 * @param <FE> FieldElement
 * @param <EC> Elliptic curve
 * @param <ECP> Elliptic curve point
 */
public abstract class ECPoint<P extends Polynomial<P>, F extends FiniteField<P>, FE extends FiniteFieldElementImpl<P, FE>,
	EC extends EllipticCurve, ECP extends ECPoint<P, F, FE, EC, ECP>> implements GroupElement<ECP> {
	
	/**
	 * points to a concrete Elliptic Curve. Two points that are to be operated most point to the same object
	 * in memory, even if two elliptic curves are "algebraically" identical, trying to add two points with EC's in
	 * different memory location will throw an exception 
	 */
	private EC E;
	
	/**
	 * x-coordinate of this point 
	 */
	private FE x;
	
	/**
	 * y-coordinate of this point 
	 */
	private FE y;
	
	/**
	 * z-coordinate for the sum in proyective coordinates
	 */
	private FE z;
	
	/**
	 * Field defined by the elliptic curve 
	 */
	private F groundField;
	
	/**
	 * Constructs an elliptic curve point with x, y an z coordinates
	 * @param x	X-coordinate
	 * @param y Y-coordinate
	 * @param z Z-coordinate
	 * @param EC Elliptic curve where the point will live
	 */
	protected ECPoint(FE x, FE y, FE z, EC ec) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.groundField = ec.getFiniteField();
		if (E == null)
			E = ec;
	}
	
	public FE getX() {
		return this.x;
	}
	
	public FE getY() {
		return this.y;
	}
	
	protected FE getZ() {
		return this.z;
	}
	
	public EC getE() {
		return this.E;
	}
	
	public F getGroundField() {
		return this.groundField;
	}
	
	public abstract ECP sum(ECP Q);
	
	public abstract ECP negate();
	
	public abstract ECP duplicate();
	
	/**
	 * The operation of the group of points is the sum
	 */
	@Override
	public ECP operate(ECP Q) {
		return this.sum(Q);
	}
	
	/**
	 * The inverse in the group of points is the negative point
	 */
	@Override
	public ECP invert() {
		return this.negate();
	}
	
	/**
	 * Sum by the non-adjacent form method 
	 * @param k
	 * @return
	 */
	public ECP nafSum(int k) {
		ArrayList<Byte> ki = AlgorithmHelper.naf(k);
		ECP Q = (ECP)this.E.getIdentity();
		ECP P = (ECP)this;
		int l = ki.size();
		for (int i = l - 1; i >= 0; i--) {
			Q = Q.duplicate();
			if (ki.get(i) == 1)
				Q = Q.sum(P);
			else if (ki.get(i) == -1)
				Q = Q.sum(P.negate());
		}
		return Q;
	}
	
	/**
	 * Scalar multiplication
	 */
	public ECP multiply(int k) {
		return nafSum(k);
	}
	
//	@Override
//	public ECP getIdentity() {
//		return getPoint();
//	}
}