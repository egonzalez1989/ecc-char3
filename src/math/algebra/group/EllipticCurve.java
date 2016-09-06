/**
 * 
 */
package math.algebra.group;

import math.algebra.field.FiniteField;

/**
 * @author egonzalez
 *
 */
public interface EllipticCurve extends Group{
	
	public boolean isSingular();
	
	public boolean isWeak();
	
	public boolean isSupersingular();
	
	public boolean isAnomalous();
	
	public <S extends FiniteField> S getFiniteField();
}