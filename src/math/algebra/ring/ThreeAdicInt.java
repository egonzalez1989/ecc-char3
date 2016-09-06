package math.algebra.ring;

import java.security.InvalidParameterException;
import java.util.Arrays;

import math.algebra.exceptions.DivideByZeroException;

import crypto.ecc.finitefields.GF3BinPoly;
import crypto.ecc.test.TestConfiguration;

public class ThreeAdicInt extends RingElementImpl<ThreeAdicInt> {

	private static final int PRECISION = 31;//TestConfiguration.EXTENSION_DEGREE;
	private static final int ARRAY_SIZE = (PRECISION >>> TestConfiguration.WORD_SIZE_EXPONENT) + 1;
	private final int[][] coefficients = new int[2][ARRAY_SIZE];
	public final static ThreeAdicInt ZERO = new ThreeAdicInt(0);
	public final static ThreeAdicInt ONE = new ThreeAdicInt(1);

	/**
	 * 
	 * @param a
	 */
	public ThreeAdicInt(int[][] a) {
		System.arraycopy(a[0], 0, this.coefficients[0], 0, a[0].length);
		System.arraycopy(a[1], 0, this.coefficients[1], 0, a[1].length);
	}

	/**
	 * 
	 * @return
	 */
	public int getPrecision() {
		return PRECISION;
	}

	/**
	 * 
	 * @param n
	 * @return
	 */
	// public static ThreeAdicInt intToPAdicInt(int n) {
	public ThreeAdicInt(int n) {
		if (n == 0)
			return;
		// return zero();
		int[][] xk = GF3BinPoly.one();
		int[][] coef = GF3BinPoly.zero();
		int mod;
		while (n > 0) {
			mod = n % 3;
			if (mod == 1) {
				coef = GF3BinPoly.sum(coef, xk);
			} else if (mod == 2) {
				coef = GF3BinPoly.sum(coef, GF3BinPoly.negate(xk));
			}
			xk = GF3BinPoly.leftShift(xk, 1);
			n = n / 3;
		}
		System.arraycopy(coef[0], 0, coefficients[0], 0,
				Math.min(ARRAY_SIZE, coef.length));
		System.arraycopy(coef[1], 0, coefficients[1], 0,
				Math.min(ARRAY_SIZE, coef.length));
	}

	/**
	 * Valuation of the 3-adic ring of integers |*|_3
	 * 
	 * @return
	 */
	public double valuate() {
		int i = 0;
		int t = (PRECISION >>> 5) + 1;
		while (i < t
				&& (this.coefficients[0][i] & this.coefficients[1][i]) != 0) {
			i++;
		}
		if (i == t)
			return 0;
		else {
			int j = GF3BinPoly.leftmostNonZeroBitPos(this.coefficients[0][i]
					| this.coefficients[1][i]);
			int n = TestConfiguration.WORD_SIZE * i + j;
			return Math.pow(3, -n);
		}
	}

	/**
	 * 
	 * @param beta
	 * @return
	 */
	public ThreeAdicInt sum(ThreeAdicInt beta) {
		return new ThreeAdicInt(sum(this.coefficients, beta.coefficients));
	}

	/**
	 * 
	 * @param beta
	 * @return
	 */
	public ThreeAdicInt multiply(ThreeAdicInt beta) {
		return new ThreeAdicInt(multiply(this.coefficients, beta.coefficients));
	}

	/**
	 * 
	 * @param beta
	 * @return
	 */
	public ThreeAdicInt divide(ThreeAdicInt beta) {
		return new ThreeAdicInt(divide(this.coefficients, beta.coefficients));
	}

	public boolean equals(ThreeAdicInt b) {
		return equals(this.coefficients, b.coefficients);
	}

	private boolean equals(int[][] a, int[][] b) {
		for (int i = 0; i < ARRAY_SIZE; i++)
			if ((a[0][i] != b[0][i]) || (a[1][i] != b[1][i]))
				return false;
		return true;
	}

	/**
	 * 
	 * @return
	 */
	public ThreeAdicInt negate() {
		return new ThreeAdicInt(negate(this.coefficients));
	}

	/**
	 * BitWise sum for polynomial approximated representation of p-adic numbers
	 * 
	 * @param a
	 * @param b
	 */
	private static int[][] sum(int[][] a, int[][] b) {
		int c[][] = new int[2][ARRAY_SIZE];
		int[][] carry = new int[2][ARRAY_SIZE];
		int aux = 0;

		// check if there are zero words in order to not to apply the procedure
		int t = ARRAY_SIZE - 1;
		while (t >= 0 && ((a[0][t] | a[1][t]) | (b[0][t] | b[1][t])) == 0)
			t--;
		t++;
		if (t == 0)
			return new int[2][ARRAY_SIZE];
		int t1 = 0;
		int nextCarry = 0;
		for (int i = 0; i < t; i++) {
			t1 = (a[0][i] | b[1][i]) ^ (a[1][i] | b[0][i]);
			c[0][i] = (a[1][i] | b[1][i]) ^ t1;
			c[1][i] = (a[0][i] | b[0][i]) ^ t1;

			//
			aux = (a[0][i] & b[0][i]) | (b[1][i] & a[0][i])
					| (b[0][i] & a[1][i]);
			carry[1][i] = nextCarry + (aux << 1);
			nextCarry = aux >>> TestConfiguration.WORD_SIZE - 1;
		}
		if (isZero(carry))
			return c;
		else
			return sum(c, carry);
	}

	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	private static int[][] multiply(int[][] a, int[][] b) {
		return rightLeftCombMultiply(a, b);
	}

	/**
	 * Bitwise multiplication for PAdic
	 * 
	 * @param a
	 * @param b
	 */
	private static int[][] rightLeftCombMultiply(int[][] a, int[][] b) {
		int aux[][], b2[][], c[][] = new int[2][ARRAY_SIZE];
		// initialize a matrix in order to not to modify the coefficients of
		// p-adic b
		aux = new int[][] { b[0], b[1] };
		b2 = sum(b, b);
		int aSize = ARRAY_SIZE - 1, bSize = aSize;
		while (aSize > 0 && (a[0][aSize] | a[1][aSize]) == 0)
			aSize--;
		while (bSize > 0 && (b[0][bSize] | b[1][bSize]) == 0)
			bSize--;
		if (bSize < aSize) {
			int[][] temp = a;
			a = b;
			b = temp;
		}

		for (int i = 0; i < TestConfiguration.WORD_SIZE; i++) {
			for (int j = 0; j <= aSize; j++) {
				// if the k-th coefficient of a[j] is 1 the we perform a sum
				// c{j} + b*p^{k*word_size + j}
				if ((1 & (a[1][j] >>> i)) == 1) {
					aux = leftShift(b, j * TestConfiguration.WORD_SIZE + i);
					if (!GF3BinPoly.isZero(aux))
						c = sum(c, aux);
				}

				// if the kth coefficient of a[j] is 2 the we perform a sum of b
				// twice
				else if ((1 & (a[0][j] >>> i)) == 1) {
					aux = leftShift(b2, j * TestConfiguration.WORD_SIZE + i);
					if (!GF3BinPoly.isZero(aux))
						c = sum(c, aux);
				}
			}
		}
		return c;
	}

	/**
	 * PAdic division in the ring of integers must have as dividend a unity,
	 * otherwise, divison cannot be performed
	 * 
	 * @param a
	 *            dividend
	 * @param b
	 *            divisor
	 */
	private static int[][] divide(int[][] a, int[][] b) {
		// Elements in the ring of p-adic integers Z_p which are not units
		// cannot be inverted
		if (((b[0][0] | b[1][0]) & 1) == 0)
			throw new InvalidParameterException(
					"element b must be a unit element in the ring of p-adic integers");

		// The remainder is a copy of a at the very beginning
		int[][] r = new int[][] { a[0], a[1] };
		int d0 = 0;
		int d1 = 0;
		int[][] d = new int[2][ARRAY_SIZE];
		int[][] aux = new int[2][ARRAY_SIZE];
		for (int i = 0; i < ARRAY_SIZE; i++) {
			for (int j = 0; j < TestConfiguration.WORD_SIZE; j++) {
				// obtain the element d_i = a_i * b_0
				d1 = (((r[0][i] >> j) & b[0][0]) | ((r[1][i] >> j) & b[1][0])) & 1;
				d0 = (((r[0][i] >> j) & b[1][0]) | ((r[1][i] >> j) & b[0][0])) & 1;
				if (d1 != 0) {
					aux = GF3BinPoly.leftShift(b, i
							* TestConfiguration.WORD_SIZE + j);
					r = sum(r, negate(aux));
					d[0][i] = d[0][i] | (d0 << j);
					d[1][i] = d[1][i] | (d1 << j);
				} else if (d0 != 0) {
					aux = GF3BinPoly.leftShift(sum(b, b), i
							* TestConfiguration.WORD_SIZE + j);
					r = sum(r, negate(aux));
					d[0][i] = d[0][i] | (d0 << j);
					d[1][i] = d[1][i] | (d1 << j);
				}
			}
			if (isZero(r))
				break;
		}
		return d;
	}

	/**
	 * 
	 * @param a
	 * @re2turn
	 */
	private static int[][] negate(int[][] a) {		
		// Buscamos el primer elemento no cero de la serie
		int i = 0;
		while (i < ARRAY_SIZE && (a[0][i] | a[1][i]) == 0){i++;}
		if (i == ARRAY_SIZE)
			return new int[2][ARRAY_SIZE];
		int n = GF3BinPoly.rightmostNonZeroBitPos((a[0][i] | a[1][i]) & 0xffffffff) - 1;
		
		// The first element of the array
		int[][] b = new int[2][ARRAY_SIZE];
		b[0][i] = (1 << n) & a[1][i];
		b[1][i] = (1 << n) & a[0][i];
		b[0][i] = b[0][i] | ((0xffffffff << (n + 1)) ^ ((a[0][i] | a[1][i]) & (0xffffffff << (n + 1))));
		b[1][i] = b[1][i] | ((0xffffffff << (n + 1)) & a[1][i]);
		
		i++;
		for (; i < ARRAY_SIZE; i++) {
			b[0][i] = 0xffffffff ^ (a[0][i] | a[1][i]);
			b[1][i] = a[1][i];
		}
//		i = 0;
//		while ((a[0][i] | a[1][i]) == 0)
//			i++;
//		b[0][i] = b[0][i] ^ (1 << n);
//		b[1][i] = b[1][i] ^ (1 << n);
		return b;
	}

	@Override
	public String toString() {
		int[][] a = this.coefficients;
		// if the polynomial is zero
		if (this.isZero()) {
			return "0";
		}
		// we get the coefficients of the polynomial and store them in an array
		// of coefficients in {1, 0, -1} by getting a1 - a0
		int n = a[0].length;
		StringBuilder pol = new StringBuilder("");
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < TestConfiguration.WORD_SIZE; j++) {
				if (((a[1][i] >> j) & 1) == 1) {
					pol.append(" + p^").append(
							i * TestConfiguration.WORD_SIZE + j);
				} else if (((a[0][i] >> j) & 1) == 1) {
					pol.append(" + 2p^").append(
							i * TestConfiguration.WORD_SIZE + j);
				}
			}
		}
		if ((a[0][0] & 1) == 1)
			pol.replace(0, 7, "2");
		else if ((a[1][0] & 1) == 1)
			pol.replace(0, 6, "1");
		return pol.toString();
	}

	private static boolean isZero(int[][] a) {
		for (int i = 0; i < ARRAY_SIZE; i++)
			if ((a[0][i] | a[1][i]) != 0)
				return false;
		return true;
	}

	public boolean isZero() {
		return isZero(coefficients);
	}

	private static int[][] leftShift(int[][] a, int n) {
		return GF3BinPoly.subPolynomial(GF3BinPoly.leftShift(a, n), PRECISION);
	}

	public ThreeAdicInt pow(int n) {
		if (this.equals(one()) || this.isZero())
			return new ThreeAdicInt(coefficients);
		int[][] square = new int[2][ARRAY_SIZE];
		square[0] = Arrays.copyOf(this.coefficients[0], ARRAY_SIZE);
		square[1] = Arrays.copyOf(this.coefficients[1], ARRAY_SIZE);

		int[][] p = one().coefficients;
		while (n > 0) {
			if ((n & 1) == 1)
				p = multiply(p, square);
			square = multiply(square, square);
			n = n >>> 1;
		}
		return new ThreeAdicInt(p);
	}

	@Override
	public ThreeAdicInt sum(ThreeAdicInt... g) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ThreeAdicInt multiply(ThreeAdicInt... g) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ThreeAdicInt zero() {
		return ZERO;
	}

	@Override
	public ThreeAdicInt one() {
		return ONE;
	}

	@Override
	public boolean isOne() {
		return this.equals(ONE);
	}
	
	/**
	 * 
	 * @return true if this element is a unit in the ring of p-adic integers
	 */
	public boolean isUnit() {
		return ((coefficients[0][0] | coefficients[1][0]) & 1) != 0;
	}
	
	public ThreeAdicInt invert() {
		return ONE.divide(this);
//		if (!this.isUnit()) {
//			throw new DivideByZeroException("Cannot invert a non-unit element");
//		}
//		int[][] b = new int[2][ARRAY_SIZE];
//		int[][] a = coefficients;
//		for (int i = 0; i < ARRAY_SIZE; i ++) {
//			for (int j = 0; j < TestConfiguration.WORD_SIZE; j++) {
//				b[0][i] = b[0][i] | (a[0][i] & (1 << j));
//				b[1][i] = b[1][i] | (a[1][i] & (1 << j));
//				
//				a = multiply(a,b);
//			}
//		}
//		return new ThreeAdicInt(a);
	}
	
	public ThreeAdicInt truncate(int n) {
		return new ThreeAdicInt(truncate(coefficients, n));
	}
	
	private int[][] truncate(int a[][], int n) {
		int[][] b = new int[2][ARRAY_SIZE];
		if (n == 0)
			return b;
		int lastIndex = ((n -1 ) >>> TestConfiguration.WORD_SIZE_EXPONENT);
		int ones = (n > 1) ? 0xff & n : 32;
		System.arraycopy(a[0], 0, b[0], 0, lastIndex);
		System.arraycopy(a[1], 0, b[1], 0, lastIndex);
		b[0][lastIndex] = a[0][lastIndex] & (0xff >> (32 - ones));
		b[1][lastIndex] = a[1][lastIndex] & (0xff >> (32 - ones));
		return b;
	}
}