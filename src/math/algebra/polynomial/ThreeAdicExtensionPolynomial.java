package math.algebra.polynomial;

import math.algebra.ring.ThreeAdicInt;
import math.algebra.ring.ThreeAdicIntExtensionElement;

public class ThreeAdicExtensionPolynomial extends PolynomialImpl<ThreeAdicIntExtensionElement, ThreeAdicExtensionPolynomial> {
	
	/**
	 * 
	 * @param coefficients
	 */
	public ThreeAdicExtensionPolynomial(
			ThreeAdicIntExtensionElement[] coefficients) {
		super(coefficients);
	}
	
	/*
	 * (non-Javadoc)
	 * @see math.algebra.polynomial.Polynomial#divide(math.algebra.polynomial.Polynomial)
	 */
	@Override
	public ThreeAdicExtensionPolynomial[] divide(ThreeAdicExtensionPolynomial g) {
		return monicDivide(g);
	}
	
	/*
	 * (non-Javadoc)
	 * @see math.algebra.polynomial.PolynomialImpl#newInstance(R[])
	 */
	@Override
	public ThreeAdicExtensionPolynomial newInstance(ThreeAdicIntExtensionElement[] coefficients) {
		// TODO Auto-generated method stub
		return new ThreeAdicExtensionPolynomial(coefficients);
	}
}