/**
 * 
 */
package crypto.ecc;


import crypto.ecc.finitefields.GF3n;
import crypto.ecc.finitefields.GF3nElement;
import crypto.ecc.finitefields.NotInFiniteFieldException;
import math.algebra.field.FiniteField;
import math.algebra.field.FiniteFieldElementImpl;
import math.algebra.group.ECPoint;
import math.algebra.group.EllipticCurve;

/**
 * @author egonzalez
 *
 */
public class HEC3n implements EllipticCurve {
	
	private GF3nElement d;
	/**
	 * 
	 * @param c
	 */
	public HEC3n(GF3nElement d) {
		this.d = d;
	}

	/* (non-Javadoc)
	 * @see math.algebra.Group#getSize()
	 */
	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Elliptic curves in Hessian form are singular if and only d=0
	 */
	@Override	
	public boolean isSingular() {
		return this.d.isZero();
	}

	/* (non-Javadoc)
	 * @see crypto.ecc.EllipticCurve#isWeak()
	 */
	public boolean isWeak() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Elliptic curves in Hessian form always are ordinary
	 */
	@Override
	public boolean isSupersingular() {
		return false;
	}

	/* (non-Javadoc)
	 * @see crypto.ecc.EllipticCurve#isAnomalous()
	 */
	@Override
	public boolean isAnomalous() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see math.algebra.Group#getIdentity()
	 */
	@Override	
	public HEC3nPoint getIdentity() {
		return new HEC3nPoint(this);
	}

	public HEC3nPoint getGenerator() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Converts the elliptic curve in Hessian form to Weierstrass form (maybe not necessary)
	 * @return
	 */
	public WEC3n toWeierstrass() {
		try {
			GF3nElement one = this.d.getFiniteField().one();
			GF3nElement zero = this.d.getFiniteField().zero();
			return new WEC3n(one, zero, this.d.invert().negate().cube());
		}
		catch(NotInFiniteFieldException e) {
			// Should never happen
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * returns the finitefield of the elliptic curve
	 */
	@Override
	public GF3n getFiniteField() {
		// TODO Auto-generated method stub
		return this.d.getFiniteField();
	}
}
