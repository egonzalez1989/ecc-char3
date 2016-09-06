/**
 * 
 */
package math.algebra.ring;

/**
 * @author egonzalez
 *
 * @param <T> concrete class of the ring
 */
public interface RingElement<T extends RingElement<T>> {
		
	/**
	 * @param g
	 * @return
	 */
	T sum(T g);
	
	/**
	 * 
	 * @param g
	 * @return
	 */
	@SuppressWarnings("unchecked")
	T sum(T... g);
	
	/**
	 * 
	 * @return
	 */
	T negate();
	
	/**
	 * 
	 * @param g
	 * @return
	 */
	T multiply(T g);
	
	/**
	 * 
	 * @param g
	 * @return
	 */
	@SuppressWarnings("unchecked")
	T multiply(T... g);
		
	/**
	 * 
	 * @param k
	 * @return
	 */
	T pow(int k);
		
	/**
	 * 
	 * @return visual form of the element
	 */
	String toString();
	
	/**
	 * 
	 * @return
	 */
	T zero();
	
	/**
	 * 
	 * @return
	 */
	boolean isZero();
	
	/**
	 * 
	 * @return
	 */
	T one();
	
	/**
	 * 
	 * @return
	 */
	boolean isOne();
}