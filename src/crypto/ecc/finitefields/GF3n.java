package crypto.ecc.finitefields;

import math.algebra.field.FiniteField;

//[1] "Software Implementation of Arithmetic in F3m", Omram Amadi, Harrel Hankerson, Alfred Menezes


/**
 * @author edgar
 * Class for finite field polynomial representation of GF(3^n). Coefficients are taken in GF(3) = {-1, 0, 1}. We will use the binary
 * representation given in [1] and used in the class GF3BinPolynomial.
 */
public class GF3n extends FiniteField<GF3Poly> {
	private int[] reductionTrinomial;
	
	/**
	 * @param t
	 */
	public GF3n(int[] t, int c) {
		super(null, c);		
		this.reductionTrinomial = t;
	}
	
	/**
	 * @param p
	 */
	public GF3n(GF3Poly p) {
		super(p, 3);
	}
	
	/* (non-Javadoc)
	 * @see math.algebra.FiniteField#one()
	 */
	@Override
	public GF3nElement one() {
		try {
			return new GF3nElement(GF3Poly.ONE, this);
		}
		catch(NotInFiniteFieldException e) {//this will never happen
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see math.algebra.FiniteField#zero()
	 */
	@Override
	public GF3nElement zero() {
		try {
			return new GF3nElement(GF3Poly.ZERO, this);
		}
		catch(NotInFiniteFieldException e) {//this will never happen
			return null;
		}
	}

	@Override
	public int getExtensionDegree() {
		if (getReductionPolynomial() != null)
			return getReductionPolynomial().getDeg();
		else
			return this.reductionTrinomial[2];
	}
	
	public int[] getReductionTrinomial() {
		return this.reductionTrinomial;
	}
	
}