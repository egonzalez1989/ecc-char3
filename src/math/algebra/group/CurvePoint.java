package math.algebra.group;

import math.algebra.field.FiniteFieldElementImpl;

/**
 * @author egonzalez
 *
 */
public interface CurvePoint<T extends CurvePoint<T, S>, S extends FiniteFieldElementImpl> {
	
	public T sum(T Q);
	
	public T duplicate();
	
	public T scalarMultiply(int k);
	
	public S getX();
	
	public S getY();
}