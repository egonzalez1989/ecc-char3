package math.algebra.ring;

import crypto.ecc.finitefields.GF3BinPoly;
import crypto.ecc.test.TestConfiguration;
import math.algebra.polynomial.PolynomialImpl;
import math.algebra.polynomial.ThreeAdicPolynomial;

/**
 * Class for ring of polynomials with coefficients in the ring of 3-adic integers.
 * @author egonzalez
 *
 * @param <S> Type of the coefficients of the polynomial
 * @param <T> Class of the polynomial
 */
public class ThreeAdicIntExtensionElement extends RingElementImpl<ThreeAdicIntExtensionElement>{
	
	private ThreeAdicPolynomial polynomial;
	private ThreeAdicPolynomial reduction;

	public ThreeAdicIntExtensionElement(ThreeAdicPolynomial polynomial, ThreeAdicPolynomial reduction) {
		this.polynomial = polynomial;
		this.reduction = reduction;
	}
	
	@Override
	public ThreeAdicIntExtensionElement sum(ThreeAdicIntExtensionElement g) {
		return new ThreeAdicIntExtensionElement(this.polynomial.sum(g.polynomial), this.reduction);
	}

	@Override
	public ThreeAdicIntExtensionElement negate() {
		return new ThreeAdicIntExtensionElement(this.polynomial.negate(), this.reduction);
	}

	@Override
	public ThreeAdicIntExtensionElement multiply(ThreeAdicIntExtensionElement g) {
		return new ThreeAdicIntExtensionElement(this.polynomial.multiply(g.polynomial).reduce(this.reduction), this.reduction);
	}

	@Override
	public ThreeAdicIntExtensionElement zero() {
		return new ThreeAdicIntExtensionElement(new ThreeAdicPolynomial(new ThreeAdicInt[]{new ThreeAdicInt(0)}), this.reduction);
	}

	@Override
	public boolean isZero() {
		return this.polynomial.isZero();
	}

	@Override
	public ThreeAdicIntExtensionElement one() {
		return new ThreeAdicIntExtensionElement(new ThreeAdicPolynomial(new ThreeAdicInt[]{new ThreeAdicInt(1)}), this.reduction);
	}

	@Override
	public boolean isOne() {
		return this.polynomial.isOne();
	}
	
	public ThreeAdicIntExtensionElement divide(ThreeAdicIntExtensionElement g) {
		return new ThreeAdicIntExtensionElement(this.polynomial.divide(g.polynomial)[0], reduction);
	}
	
	@Override
	public ThreeAdicIntExtensionElement pow(int k) {
		return new ThreeAdicIntExtensionElement(polynomial.powAndReduce(reduction, k), reduction);
	}
	
	/**
	 * Valuation of the 3-adic ring of integers |*|_3
	 * @return
	 */
	public double valuate() {
		ThreeAdicInt[] coef = polynomial.getCoefficients();
		double v = coef[0].valuate();
		double aux = 0;
		for (int i = 1; i < coef.length; i++) {
			 if (v > (aux = coef[i].valuate()))
				 v = aux;
		 }
		return v;
	}
	
	@Override
	public String toString() {
		return polynomial.toString();
	}
	
	public ThreeAdicIntExtensionElement invert() {
		ThreeAdicPolynomial[] qr = polynomial.extendedEuclid(reduction);
		ThreeAdicInt c = qr[2].getPrincipalCoefficient().invert();
		return new ThreeAdicIntExtensionElement(qr[0].escalarMultiply(c), reduction);
	}
}