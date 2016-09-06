package math.algebra.polynomial;

import math.algebra.ring.RingElement;

/** 
 * @author egonzalez
 * Interface for a generic polynomial.
 *  
 * @param <T> polynomial of a concrete class. A polynomial is also a member of a ring.
 */
public interface Polynomial<T extends Polynomial<T>> extends RingElement<T>{
	
	/**
	 * reduces modulo an (irreducible) polynomial
	 * @param g
	 * @return
	 */
	T reduce(T g);
	
	/**
	 * Performs a division
	 * @param g divisor
	 * @return array where the first element is the quotient and the second one is the residue
	 */
	T[] divide(T g);
	
	/**
	 * @param g Irreducible polynomial
	 * @return Two elements of a polynomial of a concrete class:
	 * - element 0 is the coefficient for this
	 * - element 1 is the coefficient for the trinomial
	 * the inverse for the polynomial is then the element 0 given that a*this + b*g = 1 => a*this = 1 mod g
	 */
	T[] extendedEuclid(T g);
	
	/**
	 * @return degree of the polynomial
	 */
	int getDeg();
	
	/**
	 * @param g polynomial to compare with
	 * @return true if polynomials are equal in value, false otherwise
	 */
	boolean equals(T g);
	
	/**
	 * Truncates the polynomial, returning a polynomial of degree n whose coeffients are first n+1 of the original 
	 * @param n Degree of the truncated polynomial
	 * @return
	 */
	T truncate(int n);
}