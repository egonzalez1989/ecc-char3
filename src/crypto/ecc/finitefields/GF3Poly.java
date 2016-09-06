package crypto.ecc.finitefields;

import java.security.InvalidParameterException;

import math.algebra.polynomial.Polynomial;

/**
 * @author egonzalez
 *
 */
public class GF3Poly implements Polynomial<GF3Poly>{
	int[][] coefficients;
	public final static GF3Poly ZERO = new GF3Poly(GF3BinPoly.zero());
	public final static GF3Poly ONE = new GF3Poly(GF3BinPoly.one());
	
	/**
	 * 
	 * @param a
	 */
	public GF3Poly(int[][] a) {
		int n = a.length, m = a[0].length;
		if (n != 2 || m == 0 || m != a[1].length || ((a[0][m - 1] ^ a[1][m - 1]) == 0 && m > 1))			
			throw new InvalidParameterException();
		for (int i = 0; i < m; i++)
			if ((a[1][i] & a[0][i]) != 0)
				throw new InvalidParameterException();
		coefficients = a.clone();
	}
	
	public GF3Poly (GF3Poly g) {		
		coefficients = GF3BinPoly.copyOf(g.coefficients);
	}
	
	/* (non-Javadoc)
	 * @see crypto.ecc.finitefields.Polynomial#sum(java.lang.Object)
	 */
	@Override
	public GF3Poly sum(GF3Poly g) {
		return (new GF3Poly(GF3BinPoly.sum(this.coefficients, g.coefficients)));
	}
	
	/* (non-Javadoc)
	 * @see crypto.ecc.finitefields.Polynomial#sum(T[])
	 */
	@Override
	public GF3Poly sum(GF3Poly... f) {
		int length = f.length;
		int[][][] a = new int[length + 1][][];		
		for (int i = 0; i < length; i++)
			a[i] = f[i].coefficients;
		a[length] = this.coefficients;
		return (new GF3Poly(GF3BinPoly.sum(a)));
	}
	
	/* (non-Javadoc)
	 * @see crypto.ecc.finitefields.Polynomial#multiply(java.lang.Object)
	 */
	@Override
	public GF3Poly multiply(GF3Poly g) {
		return (new GF3Poly(GF3BinPoly.multiply(this.coefficients, g.coefficients)));
	}
	
	/* (non-Javadoc)
	 * @see crypto.ecc.finitefields.Polynomial#multiply(T[])
	 */
	@Override
	public GF3Poly multiply(GF3Poly... f) {
		int length = f.length;
		int[][][] a = new int[length + 1][][];		
		for (int i = 0; i < length; i++)
			a[i] = f[i].coefficients;
		a[length] = this.coefficients;
		return (new GF3Poly(GF3BinPoly.multiply(a)));
	}
	
	
	/* (non-Javadoc)
	 * @see crypto.ecc.finitefields.Polynomial#pow(int)
	 */
	@Override
	public GF3Poly pow(int k) {
		return(new GF3Poly(GF3BinPoly.pow(this.coefficients, k)));
	}
	
	/**
	 * @param k
	 * @param t
	 * @return
	 */
	public GF3Poly powAndReduce(int k, int[] t) {
		return(new GF3Poly(GF3BinPoly.powAndReduce(this.coefficients, t, k)));
	}

	/* (non-Javadoc)
	 * @see crypto.ecc.finitefields.Polynomial#cube()
	 */
	public GF3Poly cube() {
		return(new GF3Poly(GF3BinPoly.cube(this.coefficients)));
	}
	
	/* (non-Javadoc)
	 * @see crypto.ecc.finitefields.Polynomial#negate()
	 */
	@Override
	public GF3Poly negate() {
		return(new GF3Poly(GF3BinPoly.negate(this.coefficients)));
	}
	
	/* (non-Javadoc)
	 * @see crypto.ecc.finitefields.Polynomial#divide(java.lang.Object)
	 */
	@Override
	public GF3Poly[] divide(GF3Poly g) {
		int[][][] qr;
		qr = GF3BinPoly.divide(this.coefficients, g.coefficients);
		return new GF3Poly[]{new GF3Poly(qr[0]), new GF3Poly(qr[1])};
	}
	
	/* (non-Javadoc)
	 * @see crypto.ecc.finitefields.Polynomial#extendedEuclid(java.lang.Object)
	 */
	@Override
	public GF3Poly[] extendedEuclid(GF3Poly g) {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see crypto.ecc.finitefields.Polynomial#reduce(java.lang.Object)
	 */
	@Override
	public GF3Poly reduce(GF3Poly g) {
		return null;
	}
	
	/**
	 * 
	 * @param t
	 * @return
	 */
	public GF3Poly reduce(int[] t) {
		return new GF3Poly(GF3BinPoly.reduce(this.coefficients, t));
	}
	
	/**
	 * @param t Irreducible trinomial
	 * @return Two elements of a polynomial of a concrete class:
	 * - element 0 is the coefficient for this
	 * - element 1 is the coefficient for the trinomial
	 * the inverse for the polynomial is then the element 0 given that a*this + b*t = 1 => a*this = 1 mod t
	 */
	public GF3Poly[] extendedEuclid(int[] t1) {
		int[][] t = GF3BinPoly.trinomialToPolynomial(t1);
		int[][] a = this.getCoefficients();		
		if (GF3BinPoly.getDeg(a) == 0)
			return new GF3Poly[]{new GF3Poly(this), new GF3Poly(new int[2][1])};
		// Auxiliar polynomials
		int[][] inv1 = GF3BinPoly.one();
		int[][] inv2 = GF3BinPoly.negate(GF3BinPoly.divide(t, a)[0]);
		int[][] aux;
		// Residue
		int[][] r;
		while(true) {
			r = GF3BinPoly.divide(t, a)[1];
			if (GF3BinPoly.equals(r, GF3BinPoly.ONE))
				break;
			if (GF3BinPoly.equals(r, GF3BinPoly.negate(GF3BinPoly.ONE))){
				inv2 = GF3BinPoly.negate(inv2);
				break;
			}
			aux = inv1;
			inv1 = inv2;
			t = a;
			a = r;
			inv2 = GF3BinPoly.sum(aux, GF3BinPoly.negate(GF3BinPoly.multiply(inv1, GF3BinPoly.divide(t, a)[0])));
		}
		return new GF3Poly[]{new GF3Poly(inv1), new GF3Poly(inv2)};
	}
	
//	/**
//	 * 
//	 */
//	public boolean benOrTest() {
//		return(GF3BinPoly.benOrTest(this.coefficients));
//	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return GF3BinPoly.toString(this.coefficients);
	}
	
	/**
	 * @return
	 */
	public int[][] getCoefficients() {
		return this.coefficients.clone();
	}
	
	/**
	 * @param g
	 * @return
	 */
	@Override
	public boolean equals(GF3Poly g) {
		return (GF3BinPoly.equals(this.coefficients, g.coefficients));
	}
	
	/* (non-Javadoc)
	 * @see math.algebra.Polynomial#getDeg()
	 */
	@Override
	public int getDeg() {
		return GF3BinPoly.getDeg(this.coefficients);
	}
	
	/**
	 * @param n
	 * @param l
	 * @return
	 */
	public static GF3Poly getIrreduciblePolynomial(int n, int l) {		
		return null;
	}
	
	/**
	 * @param n
	 * @return
	 */
	public static int[] getIrreducibleTrinomial(int n) {		
		return null;
	}
	
	/**
	 * @return
	 */
	public GF3Poly one() {
		return new GF3Poly(GF3BinPoly.one());
	}
	
	/**
	 * @return
	 */
	public GF3Poly zero() {
		return new GF3Poly(GF3BinPoly.zero());
	}
	
	
	/* (non-Javadoc)
	 * @see math.algebra.Polynomial#truncate(int)
	 */
	@Override	
	public GF3Poly truncate(int n) {
		return new GF3Poly(GF3BinPoly.subPolynomial(this.coefficients, n + 1));
	}

	@Override
	public boolean isZero() {
		return this.equals(ZERO);
	}

	@Override
	public boolean isOne() {
		return this.equals(ONE);
	}
}
