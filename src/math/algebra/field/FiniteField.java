package math.algebra.field;

import java.math.BigInteger;

import math.algebra.polynomial.Polynomial;

/**
 * Finite field
 * @author egonzalez
 *
 */
public abstract class FiniteField<T extends Polynomial<T>> {
	
	/**
	 * 
	 */	
	private int characteristic;
		
	/**
	 * 
	 */
	private T reductionPolynomial;
	
	/**
	 * 
	 */
//	private int[] reductionTrinomial;
	

	/**
	 * @param p Irreducible polynomial for this finite field
	 * @param c characteristic
	 */
	public FiniteField(T p, int c) {
		this.reductionPolynomial = p;
		this.characteristic = c;
	}
	
	/**
	 * @param t Irreducible trinomial for this finite field
	 * @param c characteristic of this finite field
	 */
//	public FiniteField(int[] t, int c) {
//		this.characteristic = c;
//		this.reductionTrinomial = t;
//	}
	
	/**
	 * 
	 * @return
	 */
	public T getReductionPolynomial() {
		return this.reductionPolynomial;
	}
	
//	public int[] getReductionTrinomial() {
//		return this.reductionTrinomial;
//	}
	
	/**
	 * 
	 * @return
	 */
	public int getChar() {
		return this.characteristic;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getExtensionDegree() {
		return this.reductionPolynomial.getDeg();
	}
//	public int getExtDegree() {
//		if (this.reductionPolynomial != null)
//			return this.reductionPolynomial.getDeg();
//		else
//			return this.reductionTrinomial[2];
//	}
	
	/**
	 * 
	 * @return
	 */
	public BigInteger getSize() {
		BigInteger characteristic = BigInteger.valueOf(getChar());
		return characteristic.pow(getExtensionDegree());
	}
	
	/**
	 * @return
	 */
	public abstract <S extends FiniteFieldElementImpl<T, S>> S one();
	
	/**
	 * @return
	 */
	public abstract <S extends FiniteFieldElementImpl<T, S>> S zero();
}