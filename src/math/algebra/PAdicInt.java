package math.algebra;

/**
 * The ring of p-adic integers will be represented in this class via polynomials wuth coefficiens
 * in F_3 of degree N, where N is the precision of our approximation (Given that p-adic numbers
 * arise as an infinite power series, we must approximate as we cannot work with the infinite
 * coefficient of the power series)
 * @author egonzalez
 *
 */
public abstract class PAdicInt<P extends Polynomial<P>, I extends PAdicInt<P, I>> {
	
	//Polynomial appoximation
	private P coefficients;
	
	/**
	 * 
	 * @param n approximation
	 * @param p polynomial representation
	 */
	public PAdicInt(P p) {
		coefficients = p.truncate(getPrecision());
	}
	
	/**
	 * 
	 * @param beta
	 * @return
	 */
	public abstract I multiply(I beta);
	
	
	public abstract I divide(I beta);
	/**
	 * 
	 * @return
	 */
	public abstract I sum(I beta);
	
	/**
	 * 
	 * @return
	 */
	public abstract I negate();
	
	/**
	 * 
	 * @return
	 */	
	public I modulo(int n) {
		return getInstance(coefficients.truncate(n));
	}
	
	/**
	 * new instance of p-adic number
	 * @param p
	 * @return
	 */
	public abstract I getInstance(P p);
	
	public abstract int getPrecision();
	
	public abstract I intToPAdicInt(int n);
	
	public abstract boolean isZero();
	
	public P getCoefficents() {
		return this.coefficients;
	}
	
	public abstract double valuate();
	
	public abstract I pow(int n);
}
