package math.algebra.field;

import math.algebra.polynomial.Polynomial;
import crypto.ecc.finitefields.NoCommonFiniteFieldException;
import crypto.ecc.finitefields.NotInFiniteFieldException;


//Clase para los elementos de un campo
/**
 * @author egonzalez
 *
 * @param <T>
 */
public abstract class FiniteFieldElementImpl<T extends Polynomial<T>, S extends FiniteFieldElementImpl<T, S>> implements FiniteFieldElement<S> {

	/**
	 * polynomial representaion
	 */
	protected T poly;
	
	/**
	 * points to a concrete Finite Field. Two elements that are to be operated most point to the same object
	 * in memory, even if two finite fields are "algebraically" identical, trying to add two elemnts with FF's in
	 * different memory location will throw an exception 
	 */
	protected FiniteField<T> finiteField;
	
	/**
	 * @param f
	 * @return
	 * @throws NotInFiniteFieldException
	 */
	protected abstract S getFieldElement(T f);
	
	/**
	 * @param beta
	 * @return
	 * @throws NotInFiniteFieldException
	 */
	protected abstract S getFieldElement(S beta);
	
	/**
	 * @param p
	 * @param K
	 * @throws NotInFiniteFieldException
	 */
	public FiniteFieldElementImpl(T p, FiniteField<T> K) {
		if (p.getDeg() >= K.getExtensionDegree())
			throw new NotInFiniteFieldException();
		this.poly = p;
		if (finiteField == null)
			this.finiteField = K;
	}
	
	/**
	 * @param beta
	 */
	public FiniteFieldElementImpl(FiniteFieldElementImpl<T, S> beta) {		
		this.poly = beta.poly;
	}
	
	/**
	 * 
	 * @param beta
	 * @return
	 * @throws NoCommonFiniteFieldException 
	 */
	public S sum(S beta) {
		if (beta.getFiniteField().equals(this.finiteField))
			return(getFieldElement(this.poly.sum(beta.getPolynomial())));
		else
			throw new NoCommonFiniteFieldException();
	}
	
	/**
	 * 
	 * @param beta
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public S sum(S... beta) {		
		S s = getFieldElement(this.getPolynomial());
		for (int i = 0; i < beta.length; i++) {
			if (!beta[i].getFiniteField().equals(this.finiteField))
				throw new NoCommonFiniteFieldException();
			s = s.sum(beta[i]);
		}
		return s;			
	}
	
	/**
	 * 
	 * @param beta
	 * @return
	 */
	public S multiply(S beta) {
		if (beta.getFiniteField().equals(this.finiteField))
			return(getFieldElement(this.poly.multiply(beta.getPolynomial()).reduce(this.finiteField.getReductionPolynomial()) ));
		else
			throw new NoCommonFiniteFieldException();
	}
	
	/**
	 * 
	 * @param beta
	 * @return
	 */
	public S multiply(@SuppressWarnings("unchecked") S... beta) throws NoCommonFiniteFieldException, NotInFiniteFieldException {
		S m = getFieldElement(this.getPolynomial());
		for (int i = 0; i < beta.length; i++) {
			if (!beta[i].getFiniteField().equals(this.finiteField))
				throw new NoCommonFiniteFieldException();
			m = m.multiply(beta[i]);
		}
		return m;
	}
	
	/**
	 * 
	 * @return
	 * @throws NotInFiniteFieldException 
	 */
	public S negate() {
		try {
			return (getFieldElement(this.poly.negate()));
		}
		catch (NotInFiniteFieldException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public S invert() {
		return (getFieldElement(this.poly.extendedEuclid(this.finiteField.getReductionPolynomial())[0]));
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean equals(FiniteFieldElementImpl<T, S> beta) {
		return (this.finiteField.equals(beta.finiteField) && this.poly.equals(beta.poly));
	}
	
	/**
	 * 
	 * @return
	 */
	public <U extends FiniteField<T>> U getFiniteField() {
		return (U)this.finiteField;
	}
	
	/**
	 * 
	 * @return
	 */
	public T getPolynomial() {
		return this.poly;
	}
	
	/**
	 * @param k
	 * @return
	 * @throws NotInFiniteFieldException
	 */
	public abstract S pow(int k) throws NotInFiniteFieldException;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.poly.toString();
	}
	
	public boolean isZero() {
		return this.equals(this.getFiniteField().zero());
	}
}
