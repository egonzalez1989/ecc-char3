/**
 * 
 */
package math.algebra.group;

/**
 * @author egonzalez
 *
 */
public interface Group<T extends GroupElement<T>> {	
	/**
	 * 
	 * @return
	 */
	public T getIdentity();
	
	/**
	 * 
	 * @return
	 */
	public int getSize();
}
