package crypto.ecc;

import java.security.InvalidParameterException;

import crypto.ecc.finitefields.GF3n;
import crypto.ecc.finitefields.GF3nElement;
import crypto.ecc.finitefields.NoCommonFiniteFieldException;
import crypto.ecc.finitefields.NotInFiniteFieldException;
import math.algebra.group.EllipticCurve;

public class WEC3n implements EllipticCurve {

	private GF3nElement a;
	private GF3nElement b;
	private GF3nElement c;
	
	public WEC3n(GF3nElement a,GF3nElement b,GF3nElement c) {
		if (!(a.getFiniteField().equals(b.getFiniteField()) || b.getFiniteField().equals(c.getFiniteField())))
			throw new InvalidParameterException("coefficients are not in the same finite field");
		this.a = new GF3nElement(a);
		this.b = new GF3nElement(b);
		this.c = new GF3nElement(c);	
	}

	@Override
	public WEC3nPoint getIdentity() {
		return new WEC3nPoint(this);
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isSingular() {
		if (a.isZero() && b.isZero())
			return false;
		else if (!(a.isZero() || b.isZero())) {
				try {
					GF3nElement x = a.invert().multiply(b);
					if(x.cube().sum(x.multiply(b).negate()).sum(c).isZero())
						return false;
					else
						return true;
				} catch (NoCommonFiniteFieldException | NotInFiniteFieldException e) {
					// This shouldn't happen never
					e.printStackTrace();
					return false;
				}			
		}
		else
			return true;
	}
	
	@Override
	public boolean isWeak() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSupersingular() {
		return false;
	}

	@Override
	public boolean isAnomalous() {
		// TODO Auto-generated method stub
		return false;
	}

	public HEC3n toHessian() {
		if (this.isSupersingular() && !this.a.equals(this.a.getFiniteField().one()))
			throw new InvalidParameterException("Elliptic curve must be ordinary and the quadratic coefficient must be 1.");
		try {
			GF3nElement x = a.invert().multiply(b);
			GF3nElement newC = x.cube().sum(x.multiply(b).negate()).sum(c);
			int n = this.a.getFiniteField().getExtensionDegree();
			for (int i = 1; i < n; i++)
				newC = newC.cube();
			newC = newC.invert().negate();			
			return new HEC3n(newC);
		} catch (NoCommonFiniteFieldException | NotInFiniteFieldException e) {
			// This shouldn't happen never
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public GF3n getFiniteField() {
		return this.a.getFiniteField();
	}
}
