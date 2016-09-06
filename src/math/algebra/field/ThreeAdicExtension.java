package math.algebra.field;

import math.algebra.polynomial.ThreeAdicPolynomial;

public class ThreeAdicExtension extends FiniteField<ThreeAdicPolynomial>{
	
	ThreeAdicPolynomial irr;
	
	public ThreeAdicExtension(ThreeAdicPolynomial p, int c) {
		super(p, c);
	}

	@Override
	public <S extends FiniteFieldElementImpl<ThreeAdicPolynomial, S>> S one() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends FiniteFieldElementImpl<ThreeAdicPolynomial, S>> S zero() {
		// TODO Auto-generated method stub
		return null;
	}

}
