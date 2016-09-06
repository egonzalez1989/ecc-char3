package math.algebra.polynomial;

import java.lang.reflect.Array;

import math.algebra.exceptions.DivideByZeroException;
import math.algebra.ring.RingElementImpl;

/**
 * Class for ring of polynomials with coefficients in a ring R.
 * @author egonzalez
 *
 * @param <S> Type of the coefficients of the polynomial
 * @param <T> Class of the polynomial
 */
public abstract class PolynomialImpl<R extends RingElementImpl<R>, P extends PolynomialImpl<R,P>>
extends RingElementImpl<P>
implements Polynomial<P> {
	
	/**
	 * 
	 */
	private R[] coefficients;
	
	/**
	 * 
	 * @param coefficients
	 */
	public PolynomialImpl(R[] coefficients) {
		this.coefficients = coefficients;
	}
	
	/*
	 * (non-Javadoc)
	 * @see math.algebra.ring.RingElement#sum(math.algebra.ring.RingElement)
	 */
	@Override
	public P sum(P g) {
		if (g.isZero())
			return newInstance(this.coefficients);
		if (this.isZero())
			return newInstance(g.getCoefficients());
		P f = (P)this;
		if (g.getDeg() < f.getDeg()) {
			P aux = f;
			f = g;
			g = aux;			
		}
		R[] gCoef = g.getCoefficients();
		R[] fCoef = f.getCoefficients();
		R[] sumCoef = (R[])Array.newInstance(gCoef[0].getClass(), g.getDeg() + 1);
		int size = gCoef.length;
		for (int i = 0; i <= f.getDeg(); i++)
			sumCoef[i] = gCoef[i].sum(fCoef[i]);
		for (int i = f.getDeg() + 1; i < size; i++)
			sumCoef[i] = gCoef[i];
		size--;
		while (size > 0 && sumCoef[size].isZero())
			size--;
		R[] hCoef = (R[])Array.newInstance(gCoef[0].getClass(), size + 1);
		System.arraycopy(sumCoef, 0, hCoef, 0, size + 1);
		return newInstance(hCoef);
	}
	
	/*
	 * (non-Javadoc)
	 * @see math.algebra.ring.RingElement#multiply(math.algebra.ring.RingElement)
	 */
	@Override
	public P multiply(P g) {
		if (g.isZero() || this.isZero())
			return zero();
		if (this.isOne())
			return newInstance(g.getCoefficients());
		if (g.isOne())
			return newInstance(coefficients);
		int size = this.getDeg() + g.getDeg() + 1;
		R[] gCoef = g.getCoefficients();
		R[] pCoef = (R[])Array.newInstance(gCoef[0].getClass(), size);
		for (int i = 0; i < size; i++)
			pCoef[i] = gCoef[0].zero();
		for (int i = 0; i <= this.getDeg(); i++) {
			for (int j = 0;  j <= g.getDeg(); j++) {
				pCoef[i + j] = pCoef[i + j].sum(
						this.coefficients[i].multiply(gCoef[j]));
			}
		}
		return newInstance(pCoef);
	}
	
	/**
	 * 
	 * @param g
	 * @return
	 */
	public P[] monicDivide(P g) {
		P[] result = (P[])Array.newInstance(g.getClass(), 2);
		if (g.isZero())
			throw new DivideByZeroException();
		if (g.isOne()) {
			result[0] = newInstance(g.getCoefficients());
			result[1] = zero();
		}
		// Remainder polynomial
		P r = newInstance(coefficients);
		// Quotient polynomial
		P q = zero();
		P one = one();
		P gk;
		R[] gkCoef;
		int k = r.getDeg() - g.getDeg();
		R[] auxCoef = (R[])Array.newInstance(coefficients[0].getClass(), 1);
		while (k >= 0) {
			gkCoef = g.getCoefficients();
			for (int i = 0; i <= g.getDeg() ; i++)
				gkCoef[i] = gkCoef[i].multiply(r.getPrincipalCoefficient());
			// Polynomial g(x)*x^k with k = deg(r) - deg(g)
			gk = newInstance(gkCoef).leftShift(k);
			
			auxCoef[0] = r.getPrincipalCoefficient();
			q = q.sum(newInstance(auxCoef).leftShift(k));
			//
			r = r.sum(gk.negate());
			k = r.getDeg() - g.getDeg();
		}
		P[] qr = (P[])Array.newInstance(this.getClass(), 2);
		qr[0] = q;
		qr[1] = r;
		return qr;
	}
	
	/**
	 * 
	 * @return
	 */
	public R[] getCoefficients() {
		return coefficients.clone();
	}
	
	/*
	 * (non-Javadoc)
	 * @see math.algebra.polynomial.Polynomial#getDeg()
	 */
	@Override
	public int getDeg() {
		if (this.isZero())
			return Integer.MIN_VALUE;
		else
			return this.coefficients.length - 1;
	}
	
	/**
	 * 
	 * @param k
	 * @return
	 */
	public P leftShift(int k) {
		R[] pCoef = (R[])Array.newInstance(coefficients[0].getClass(), coefficients.length + k);
		for (int i = 0; i < k; i++) {
			pCoef[i] = coefficients[0].zero();
		}
		System.arraycopy(coefficients, 0, pCoef, k, coefficients.length);
		return newInstance(pCoef);
	}
	
	/*
	 * (non-Javadoc)
	 * @see math.algebra.polynomial.Polynomial#equals(math.algebra.polynomial.Polynomial)
	 */
	@Override
	public boolean equals(P g) {
		if (g.getDeg() != this.getDeg())
			return false;
		R[] gCoeff = (R[])g.getCoefficients();
		for (int i = 0; i < g.getDeg(); i++)
			if (!gCoeff[i].equals(coefficients[i]))
				return false;
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see math.algebra.polynomial.Polynomial#truncate(int)
	 */
	@Override
	public P truncate(int n) {
		if (n > coefficients.length)
			return newInstance(coefficients);
		int size = getDeg();
		while (coefficients[size].isZero() && n > 0)
			size--;
		R[] tCoeff = (R[])Array.newInstance(coefficients[0].getClass(), size);
		System.arraycopy(coefficients, 0, tCoeff, 0, size);
		return newInstance(tCoeff);
	}
	
	/*
	 * (non-Javadoc)
	 * @see math.algebra.ring.RingElementImpl#sum(T[])
	 */
	@Override
	public P sum(P... g) {
		P s = zero();;
		for (int i = 0; i < g.length; i++)
			s = s.sum(g[i]);
		return s;
	}
	
	/*
	 * (non-Javadoc)
	 * @see math.algebra.ring.RingElement#negate()
	 */
	@Override
	public P negate() {
		R[] nCoeff = (R[])Array.newInstance(coefficients[0].getClass(), coefficients.length);
		nCoeff[0] = coefficients[0].zero();
		for (int i = 0; i <= getDeg(); i++)
			nCoeff[i] = coefficients[i].negate();
		return newInstance(nCoeff);
	}
	
	/*
	 * (non-Javadoc)
	 * @see math.algebra.ring.RingElementImpl#multiply(T[])
	 */
	@Override
	public P multiply(P... g) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see math.algebra.ring.RingElement#zero()
	 */
	@Override
	public P zero() {
		R[] zCoeff = (R[])Array.newInstance(coefficients[0].getClass(), 1);
		zCoeff[0] = coefficients[0].zero();
		return newInstance(zCoeff);
	}
	
	/*
	 * (non-Javadoc)
	 * @see math.algebra.ring.RingElement#isZero()
	 */
	@Override
	public boolean isZero(){
		return (coefficients.length == 1 && coefficients[0].isZero());
	}

	/*
	 * (non-Javadoc)
	 * @see math.algebra.ring.RingElement#one()
	 */
	@Override
	public P one() {
		R[] zCoeff = (R[])Array.newInstance(coefficients[0].getClass(), 1);
		zCoeff[0] = coefficients[0].one();
		return newInstance(zCoeff);
	}
	
	/*
	 * (non-Javadoc)
	 * @see math.algebra.ring.RingElement#isOne()
	 */
	@Override
	public boolean isOne(){
		return (coefficients.length == 1 && coefficients[0].isOne());
	}
	
	/*
	 * (non-Javadoc)
	 * @see math.algebra.polynomial.Polynomial#reduce(math.algebra.polynomial.Polynomial)
	 */
	@Override
	public P reduce(P g) {
//		return this.divide(g)[1];
		P r = newInstance(coefficients);
		if (getDeg() < g.getDeg())
			return r;
		P f0 = this.subPolynomial(0, g.getDeg() - 1);
		P f1 = this.subPolynomial(g.getDeg(), this.getDeg()).multiply(g.subPolynomial(0, g.getDeg() - 1).negate());
		r = f0.sum(f1).reduce(g);
		return r;
	}
	
	/**
	 * 
	 * @param k
	 * @return
	 */
	public P powAndReduce(P red, int k) {
		if (this.isOne() || this.isZero())
			return (P)this;
		P square = (P)this;
		P p = one();
		while (k > 0) {
			if ((k & 1) == 1) {
				p = p.multiply(square);
				p = p.reduce(red);
			}
			square = square.multiply(square);
			square = square.reduce(red);
			k = k >>> 1;
		}
		return p;
	}
	
	/**
	 * 
	 * @param x
	 * @return
	 */
	public R evaluate(R x) {
		R result = coefficients[0].zero();
		for (int i = 0; i < coefficients.length; i++) {
			result = result.sum(coefficients[i].multiply(x.pow(i)));
		}
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder pol = new StringBuilder();
		int i = 1;
		if (isZero())
			return "0";
		if (!coefficients[0].isZero())
			pol.append("(" + coefficients[0] + ")");
		for (; i < coefficients.length; i++) {
			if (!coefficients[0].isZero())
				pol.append(" + (" + coefficients[i] + ")X^" + i);
		}
		return pol.toString();
	}
	
	/*
	 * (non-Javadoc)
	 * @see math.algebra.polynomial.Polynomial#extendedEuclid(math.algebra.polynomial.Polynomial)
	 */
	@Override
	public P[] extendedEuclid(P g) {
		P[] result = (P[])Array.newInstance(getClass(), 3);
		P r = (P)this;
		P old_r = g;
		P s = zero();
		P old_s = one();
		P t = one();
		P old_t = zero();
		P quotient;
		P aux;
		int j;
		while (!r.isZero()) {
			quotient = old_r.divide(r)[0];
			aux = r;
			r = old_r.sum(quotient.multiply(r).negate());
			old_r = aux;
			aux = s;
			s = old_s.sum(quotient.multiply(s).negate());
			old_s = aux;
			aux = t;
			t = old_t.sum(quotient.multiply(t).negate());
			old_t = aux;
		}
		result[0] = old_t;
		result[1] = old_s;
		result[2] = old_r;
		return result;
	}
		
	public R getPrincipalCoefficient() {
		return coefficients[getDeg()];
	}
	
	/**
	 * 
	 * @param coefficients
	 * @return
	 */
	public abstract P newInstance(R[] coefficients);
	
	/**
	 * 
	 * @param deg
	 * @return
	 */
	public P subPolynomial(int from, int to) {
		if (to >= getDeg())
			to = getDeg();
		final P z = zero();
		while (to >= from && coefficients[to].equals(z))
			to--;
		if (to < from)
			return z;
		R[] coef = (R[])Array.newInstance(coefficients[0].getClass(), to - from + 1);
		System.arraycopy(coefficients, from, coef, 0, coef.length);
		return newInstance(coef);
	}
	
	public P escalarMultiply(R c) {
		R[] coef = this.getCoefficients();
		for (int i = 0; i < coef.length; i++) {
			coef[i] = coef[i].multiply(c);
		}
		return newInstance(coef);
	}
}