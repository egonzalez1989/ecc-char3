/**
 * 
 */
package math.algebra.group;

/**
 * @author egonzalez
 *
 */
public interface CyclicGroup<T extends GroupElement<T>> extends Group<T> {
	
	T getGenerator();
}
