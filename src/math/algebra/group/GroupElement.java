/**
 * 
 */
package math.algebra.group;

/**
 * @author egonzalez
 *
 */
public interface GroupElement<T extends GroupElement<T>> {
	public T operate(T alpha);
	public T invert();
}
