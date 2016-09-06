package crypto.ecc;

import math.algebra.group.ECPoint;
import crypto.ecc.finitefields.GF3Poly;
import crypto.ecc.finitefields.GF3n;
import crypto.ecc.finitefields.GF3nElement;

public class WEC3nPoint extends ECPoint<GF3Poly, GF3n, GF3nElement, WEC3n, WEC3nPoint> {
	
	public WEC3nPoint(WEC3n E) {
		super(E.getFiniteField().one(), E.getFiniteField().one(), E.getFiniteField().zero(), E);
	}
	
	/**
	 * Constructs an elliptic curve point with x and y coordinates
	 * @param x	X-coordinate
	 * @param y Y-coordinate
	 * @param EC Elliptic curve where the point will live
	 */
	public WEC3nPoint(GF3nElement x, GF3nElement y, WEC3n E) {
		super(x, y, E.getFiniteField().one(), E);
	}
	
	/**
	 * Constructs a new elliptic curve point which is a copy of the argument
	 * @param P Point ro copy
	 */
	private WEC3nPoint(WEC3nPoint P) {
		super(P.getX(), P.getY(), P.getZ(), P.getE());
	}
	
	@Override
	public WEC3nPoint sum(WEC3nPoint Q) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WEC3nPoint negate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WEC3nPoint duplicate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WEC3nPoint multiply(int k) {
		// TODO Auto-generated method stub
		return null;
	}
}
