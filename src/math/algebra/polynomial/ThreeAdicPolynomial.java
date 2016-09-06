package math.algebra.polynomial;

import math.algebra.exceptions.DivideByZeroException;
import math.algebra.ring.ThreeAdicInt;

/**
 * Class for ring of polynomials with coefficients in the ring of 3-adic integers.
 * @author egonzalez
 *
 * @param <S> Type of the coefficients of the polynomial
 * @param <T> Class of the polynomial
 */
public class ThreeAdicPolynomial extends PolynomialImpl<ThreeAdicInt, ThreeAdicPolynomial>{
	
	public ThreeAdicPolynomial(ThreeAdicInt[] coefficients) {
		super(coefficients);
	}

	@Override
	public ThreeAdicPolynomial newInstance(ThreeAdicInt[] coefficients) {
		return new ThreeAdicPolynomial(coefficients);
	}

	@Override
	public ThreeAdicPolynomial[] divide(ThreeAdicPolynomial g) {
		ThreeAdicPolynomial r = this;
		ThreeAdicPolynomial q = zero();
		ThreeAdicPolynomial one = one();
		int j = r.getDeg() - g.getDeg();
		ThreeAdicInt a, b, c;
		while (j >= 0) {
			a = r.getPrincipalCoefficient();
			b = g.getPrincipalCoefficient();
			c = a.multiply(b.invert());
			r = r.sum(g.escalarMultiply(c).leftShift(j).negate());
			q = q.sum(one.escalarMultiply(c).leftShift(j));
			j = r.getDeg() - g.getDeg();
		}
		return new ThreeAdicPolynomial[]{q, r};
//		ThreeAdicInt m = g.getPrincipalCoefficient();
//		if (ThreeAdicInt.ONE.equals(m))
//			return monicDivide(g);
//		else if (m.isUnit()){
//			ThreeAdicInt inv = m.invert();
//			ThreeAdicInt[] gCoef = g.getCoefficients();
//			for (int i = 0; i < gCoef.length; i++) {
//				gCoef[i] = gCoef[i].multiply(inv);
//			}
//			ThreeAdicPolynomial[] qr = monicDivide(new ThreeAdicPolynomial(gCoef));
//			ThreeAdicInt[] qCoef = qr[0].getCoefficients();
//			ThreeAdicInt[] rCoef = qr[1].getCoefficients();
//			for (int i = 0; i < qCoef.length; i++) {
//				qCoef[i] = qCoef[i].multiply(m);
//			}
//			for (int i = 0; i < rCoef.length; i++) {
//				rCoef[i] = rCoef[i].multiply(m);
//			}
//			qr[0] = new ThreeAdicPolynomial(qCoef);
//			qr[1] = new ThreeAdicPolynomial(rCoef);
//			return qr;
//		}
//		else {
//			throw new DivideByZeroException("Cannot divide by polynomial: " + g);
//		}
	}
}