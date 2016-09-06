package crypto.ecc.finitefields;

import math.algebra.field.FiniteFieldElementImpl;

public class GF3nElement extends FiniteFieldElementImpl<GF3Poly, GF3nElement> { //implements FiniteFieldElement {
	
	/**
	 * creates a field element with the coefficients given by the double array 
	 * @param a polynomial representing the element
	 * @param K finite field related to the eleent
	 * @throws NotInFiniteFieldException
	 */
	public GF3nElement (GF3Poly f, GF3n K) {
		super(f, K);
	}
	
	/**
	 * builds a new element using an existing one, the resulting element lies in the same field and has the same polynomial representation
	 * @param alpha
	 * @throws NotInFiniteFieldException
	 */
	public GF3nElement (GF3nElement alpha) {		
		super(alpha);
	}

	/* (non-Javadoc)
	 * @see math.algebra.FiniteFieldElement#getFieldElement(math.algebra.Polynomial)
	 */
	@Override
	protected GF3nElement getFieldElement(GF3Poly f) throws NotInFiniteFieldException {
		return new GF3nElement(f, (GF3n)this.finiteField);
	}
	
	/* (non-Javadoc)
	 * @see math.algebra.FiniteFieldElement#getFieldElement(math.algebra.FiniteFieldElement)
	 */
	@Override
	protected GF3nElement getFieldElement(GF3nElement alpha) throws NotInFiniteFieldException {
		return new GF3nElement(alpha);
	}

	/**
	 * @return
	 * @throws NotInFiniteFieldException
	 */
	public GF3nElement cube() throws NotInFiniteFieldException {
		return new GF3nElement(this.poly.cube().reduce(this.finiteField.getReductionPolynomial()), (GF3n)this.finiteField);
	}
	
	/* (non-Javadoc)
	 * @see math.algebra.FiniteFieldElement#pow(int)
	 */
	@Override
	public GF3nElement pow(int k) throws NotInFiniteFieldException {
		return new GF3nElement(this.poly.powAndReduce(k, ((GF3n)finiteField).getReductionTrinomial()), (GF3n)this.finiteField);
	}
	
	@Override
	public GF3nElement multiply(GF3nElement beta) throws NoCommonFiniteFieldException, NotInFiniteFieldException {
		if (beta.getFiniteField().equals(this.finiteField))
			return(getFieldElement(this.poly.multiply(beta.getPolynomial()).reduce(((GF3n)finiteField).getReductionTrinomial()) ));
		else
			throw new NoCommonFiniteFieldException();
	}
	
	@Override
	public GF3nElement invert() throws NotInFiniteFieldException {
		return (getFieldElement(this.poly.extendedEuclid(((GF3n)finiteField).getReductionTrinomial())[0]));
	}

	@Override
	public GF3nElement zero() {
		return finiteField.zero();
	}

	@Override
	public GF3nElement one() {
		return finiteField.one();
	}

	@Override
	public boolean isOne() {
		return (one().equals(this));
	}
}