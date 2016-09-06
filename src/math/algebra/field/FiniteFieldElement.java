package math.algebra.field;

import math.algebra.ring.RingElement;

public interface FiniteFieldElement<T extends FiniteFieldElement<T>> extends RingElement<T> {
	
	T invert();	
}
