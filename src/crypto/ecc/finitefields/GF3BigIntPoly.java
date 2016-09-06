package crypto.ecc.finitefields;

import java.math.BigInteger;
import java.security.InvalidParameterException;
import java.util.ArrayList;

public class GF3BigIntPoly {
	
	/**
	 * coefficients of this polynomial
	 */
	private final BigInteger a[] = new BigInteger[2];
	
	public GF3BigIntPoly() {
		this.a[0] = BigInteger.ZERO;
		this.a[1] = BigInteger.ZERO;
	}
	
	/**
	 * Contructor
	 * @param a0
	 * @param a1
	 */
	public GF3BigIntPoly(BigInteger a0, BigInteger a1) {
		if (a0.compareTo(BigInteger.ZERO) * a0.compareTo(BigInteger.ZERO) < 0)
			throw new InvalidParameterException("Value of BigInteger coefficients mut not be zero");
		this.a[0] = a0;
		this.a[1] = a1;
	}
	
	/**
	 * @param a
	 */
	public GF3BigIntPoly(BigInteger a[]) {
		this(a[0], a[1]);
	}
	
	public GF3BigIntPoly(GF3BigIntPoly q) {
		// as we know thta the coefficients of the given polynomial have been validated we pass the parameters directly
		this.a[0] = q.a[0];
		this.a[1] = q.a[1];
	}
	
	/**
	 * returns the zero polynomial, which consists of two zero bigintegers
	 */
	public static GF3BigIntPoly zero() {
		return new GF3BigIntPoly();
	}
	
	/**
	 * returns the polynomial 1, which has the element a0 = 0 and a1 = 1
	 */
	public static GF3BigIntPoly one() {
		return new GF3BigIntPoly(new BigInteger[]{BigInteger.ZERO, BigInteger.ONE});
	}

//	/**
//	 * Leftmost non-zero bit in the binary representation of a number
//	 * @param n
//	 * @return leftmost non-zerobit of n
//	 */
//	public static int leftmostNonZeroBitPos(int n) {
//		int i = 0;
//		if (n == 0)
//			return i;
//		while ((n << i) > 0) {
//			i++;
//		}
//		return (WORD_SIZE - i);
//	}
	
	/**
	 * gets the degree of the polynomail based on the las non zero element of the arrays thta represent it
	 * @param a
	 * @return
	 */
	public int getDeg() {
		if (this.equals(GF3BigIntPoly.zero()))
			return Integer.MIN_VALUE;
		return this.a[0].or(this.a[1]).bitLength() - 1;
	}
	
	/**
	 * BitWise sum for polynomial in GF3[X]
	 * @param a
	 * @param b
	 */
	public GF3BigIntPoly sum(GF3BigIntPoly g) {
		BigInteger b[] = g.a;
		BigInteger t = a[0].or(b[1]).xor(a[1].or(b[0]));
        BigInteger c[] = new BigInteger[2];
        c[0] = this.a[1].or(b[1]).xor(t);
        c[1] = this.a[0].or(b[0]).xor(t);
        return new GF3BigIntPoly(c);
    }
	
	/**
	 * Sum of an arbitrary number of polynomials
	 * @param a
	 * @return
	 */
	public GF3BigIntPoly sum(GF3BigIntPoly... g) {
		int n = g.length;
		if (n == 0)
			return new GF3BigIntPoly(this);
		GF3BigIntPoly s = this.sum(g[0]);
		for (int i = 1; i < n; i++) {
			s = s.sum(g[i]);
		}
		return (s);
	}
	/**
	 * Gets the additive negative of the polynomial
	 * @param a
	 * @return
	 */
	public GF3BigIntPoly negate() {
		return new GF3BigIntPoly(this.a[1], this.a[0]);
	}
	
	/**
	 * returns the sign of the principal coefficient
	 */
	public int sign() {
		if (this.equals(GF3BigIntPoly.zero()))
			return 0;
		if (this.a[0].bitLength() > this.a[1].bitLength())
			return -1;
		else
			return 1;
	}
	
	/**
	 * @param n
	 * @return
	 */
	public GF3BigIntPoly shiftLeft(int n) {
		BigInteger a[] = new BigInteger[2];
		a[0] = this.a[0].shiftLeft(n);
		a[1] = this.a[1].shiftLeft(n);
		return new GF3BigIntPoly(a);
	}
	
	/**
	 * @param n
	 * @return
	 */
	public GF3BigIntPoly shiftRight(int n) {
		BigInteger a[] = new BigInteger[2];
		a[0] = this.a[0].shiftRight(n);
		a[1] = this.a[1].shiftRight(n);
		return new GF3BigIntPoly(a);
	}
	
	/**
	 * Bitwise multiplication for polynomials in GF3[X]
	 * @param a
	 * @param b
	 */
	public GF3BigIntPoly rightLeftCombMultiply(GF3BigIntPoly g) {	
		 
		// polynomial to record the result of the multiplication
		GF3BigIntPoly m = GF3BigIntPoly.zero();
		
		// comb method
		// bits will store the positions of the non-zero bits of the coefficients
		BigInteger bits = g.a[0].or(g.a[1]);
		int k = 0;
		int sign;
		GF3BigIntPoly aux = new GF3BigIntPoly(g);
		while ((k = bits.bitLength() - 1) != -1) {			
			// this iterates through each coefficient of the polynomial g 
			// if the kth coefficient of a[j] is 1 the we perform a sum
			sign = aux.sign();
            if (sign == -1) {
                m = m.sum(this.shiftLeft(k).negate());
            }
            // if the kth coefficient of a[j] is -1 the we perform a rest c{j} - b
            else if (sign == 1) {
            	m = m.sum(this.shiftLeft(k));
            }
            
            // clear the 1 bit found in order to get the next non zero bit 
            bits = bits.clearBit(k);
            aux.a[0] = aux.a[0].clearBit(k);
            aux.a[1] = aux.a[1].clearBit(k);
        }
		return m;
	}
	
	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public GF3BigIntPoly multiply(GF3BigIntPoly g) {
		return this.rightLeftCombMultiply(g);
	}
	
	/**
	 * 
	 * @param a
	 * @return
	 */
	public GF3BigIntPoly multiply(GF3BigIntPoly... g) {
		int n = g.length;
		if (n == 0)
			return new GF3BigIntPoly(this);
		GF3BigIntPoly m = this.multiply(g[0]);
		for (int i = 1; i < n; i++) {
			m = m.multiply(g[i]);
		}
		return (m);
	}
	
	/**
	 * Cube of a polynomial
	 */
	public GF3BigIntPoly cube() {
		int k = 0;
		BigInteger bits = this.a[0].or(this.a[1]);
		GF3BigIntPoly c = new GF3BigIntPoly();
		while ((k = bits.bitLength()) != 0) {
			c = c.sum(GF3BigIntPoly.one().shiftLeft(k));
		}
		return c;
	}
	
	/**
	 * Cube of a polynomial
	 */
	public GF3BigIntPoly cubeWithBigInt() {
		int k = 0;
		BigInteger bits = this.a[0].or(this.a[1]);
		BigInteger c0 = BigInteger.ZERO;
		BigInteger c1 = BigInteger.ZERO;
		while ((k = bits.bitLength() - 1) != -1) {
			c0 = c0.setBit(3 * k);
			c1 = c1.setBit(3 * k);
		}
		return new GF3BigIntPoly(c0, c1);
	}
	
	public GF3BigIntPoly cubeWithByte() {
		int WORD_SIZE = 8;
		byte[][] b = new byte[2][];
		b[0] = this.a[0].toByteArray();
		b[1] = this.a[1].toByteArray();
		int tb = b[1].length;
		int tc = (this.getDeg() * 3) / WORD_SIZE + 1;
		byte[][] c = new byte[2][tc];
		// If the cube of the polynomial requires less than 3*ta elements (because of the degree of the cube)
		int t2 = 3 * tb - tc;
		int t = 0;
		// 
		int l = WORD_SIZE / 3;
		int W1 = (WORD_SIZE + 1) / 3;
		int W2 = (2 * (WORD_SIZE + 1)) / 3;
		if (t2 == 0)
			t = tb;
		else {
			t = tb - 1;
		}
		// Insert two zeros between every element of the arrays
		for (int i = 0; i < t; i++) {
			for (int j = 0; j <= l; j++)
			{
				c[1][3 * i] += ((b[1][i] >> j) & 1) << 3 * j;
				c[1][3 * i + 1] += ((b[1][i] >> j + W1) & 1) << 3 * j + 1;
				c[1][3 * i + 2] += ((b[1][i] >> j + W2) & 1) << 3 * j + 2;
				c[0][3 * i] += ((b[0][i] >> j) & 1) << 3 * j;
				c[0][3 * i + 1] += ((b[0][i] >> j + W1) & 1) << 3 * j + 1;
				c[0][3 * i + 2] += ((b[0][i] >> j + W2) & 1) << 3 * j + 2;
			}
			// In the last array we could get a 1 bit at the beginning, so we make it zero if necessary 
			c[1][3 * i + 2] = (byte)((c[1][3 * i + 2] >> 1) << 1);
			c[0][3 * i + 2] = (byte)((c[0][3 * i + 2] >> 1) << 1);
		}
		if (t2 == 2) {
		for (int j = 0; j <= l; j++)
			{
				c[1][3 * t] += ((b[1][t] >> j) & 1) << 3 * j;
				c[0][3 * t] += ((b[0][t] >> j) & 1) << 3 * j;
			}
		}
		else if (t2 == 1) {
			for (int j = 0; j <= l; j++)
			{
				c[1][3 * t] += ((b[1][t] >> j) & 1) << 3 * j;
				c[1][3 * t + 1] += ((b[1][t] >> j + W1) & 1) << 3 * j + 1;
				c[0][3 * t] += ((b[0][t] >> j) & 1) << 3 * j;
				c[0][3 * t + 1] += ((b[0][t] >> j + W1) & 1) << 3 * j + 1;
			}
		}
		BigInteger r[] = new BigInteger[]{new BigInteger(c[0]), new BigInteger(c[1])};
		return new GF3BigIntPoly(r);
	}
	
	/**
	 * power of a polynomial
	 * @param a
	 * @param k
	 * @return
	 */
	public GF3BigIntPoly pow(int k) {
		
		GF3BigIntPoly f = this;
		
		//We cannot compute a negative power
		if (k < 0) {
			throw new InvalidParameterException("Power must be positive or zero");
		}
		
		// if a is the zero polynomial we return it 
		if (f.getDeg() == Integer.MIN_VALUE)
			return zero();
		
		// if k = 0 and a is not zero the the result must be the polynomial 1
		if (k == 0)
			return one();
		
		// Express k as a sum of powers of 3 with coefficients in {0, 1, 2}
		// list of coefficient of the ternary representation of k
		ArrayList<Integer> c = new ArrayList<Integer>();
		while(k > 0) {
			c.add(k % 3);
			k = k/3;
		}
		c.add(k);
		int size = c.size();

		// Calculate the power of tjh polynomial using consecutive squaring and precalculating this^2 for 2 valued coefficients
		GF3BigIntPoly square = this.multiply(this);
		
		// result of the operation
		GF3BigIntPoly p;
		
		// we initilalize the resulting polynomial
		p = one();
		
		// 
		for(int i = 0; i < size; i++) {
			switch (c.get(i)) {
				// each time we have a 1 coefficient the value of p will be multiplied by f^(3^i)
				case 1 :					
					p = p.multiply(f);
					break;
				// if the coefficient is 2 the we multiply by (f^2)^(3^i)
				case 2 :				
					p = p.multiply(square);
					break;
				default :
					break;
			}
			
			// calculate f^(3^i) and (f^2)^(3^i) for further use
			f = f.cube();
			square = square.cube();
		}
		return p;
	}
	
	/**
	 * power of a polynomial reduced modulo a trinomial t, every operation is reduced modulo the trinomial
	 * @param a
	 * @param t
	 * @param k
	 * @return
	 */
	public GF3BigIntPoly powAndReduce(int[] t, int k) {
		
		GF3BigIntPoly f = this;
		
		//We cannot compute a negative power
		if (k < 0) {
			throw new InvalidParameterException("Power must be positive or zero");
		}
		
		// if a is the zero polynomial we return it 
		if (this.getDeg() == Integer.MIN_VALUE)
			return zero();
		
		// if k = 0 and a is not zero the the result must be the polynomial 1
		if (k == 0)
			return one();
		
		// Express k as a sum of powers of 3 with coefficients in {0, 1, 2}
		// list of coefficient of the ternary representation of k
		ArrayList<Integer> c = new ArrayList<Integer>();
		while(k > 0) {
			c.add(k % 3);
			k = k/3;
		}
		c.add(k);
		int size = c.size();

		// Calculate the power of tjh polynomial using consecutive squaring and precalculating this^2 for 2 valued coefficients
		GF3BigIntPoly square = this.multiply(this).reduce(t);
		
		// result of the operation
		GF3BigIntPoly p;
		
		// we initilalize the resulting polynomial
		p = one();
		
		// 
		for(int i = 0; i < size; i++) {
			switch (c.get(i)) {
				// each time we have a 1 coefficient the value of p will be multiplied by f^(3^i)
				case 1 :					
					p = p.multiply(f).reduce(t);
					break;
				// if the coefficient is 2 the we multiply by (f^2)^(3^i)
				case 2 :				
					p = p.multiply(square).reduce(t);
					break;
				default :
					break;
			}
			
			// calculate f^(3^i) and (f^2)^(3^i) for further use
			f = f.cube().reduce(t);
			square = square.cube().reduce(t);
		}
		return p;
	}
	
	/**
	 * 
	 * @param a dividend
	 * @param b divisor
	 */
	public GF3BigIntPoly[] divide(GF3BigIntPoly g) {
		// it is not possible to divide by zero, however this is not likely to happen, as the polynomial b will
		// be an irreducible nonzero polynomial, we could erase this validation
		if (g.equals(GF3BigIntPoly.zero()))
			throw new IllegalArgumentException("The second argument must not be zero");
		
		// Sign of the prinicipal coefficient
		int sign = g.sign();
		
		// degrees of the polynomials
		int fdeg = this.getDeg(), gdeg = g.getDeg();
		
		// if the degree of the divisor is greater than that of the dicidend then there is no much to do
		if (fdeg < gdeg) {
			return new GF3BigIntPoly[] {GF3BigIntPoly.zero(), new GF3BigIntPoly(this)};
		}
		
		int k = fdeg - gdeg;
		
		// We use the negative of g, at the end of the process the quotient will be negated to return the right result
		if (sign == -1)
			g = g.negate();
		
		// sum of b * x^k and the result of remainder in each iteration process (initialize r = a)
		GF3BigIntPoly r  = new GF3BigIntPoly(this);
		
		// quotient
		GF3BigIntPoly q = GF3BigIntPoly.zero();
		
		// Polynomial b * x^k
		GF3BigIntPoly bxk;
		
		// Principal coeficcient of r 
		int coef;
		
		// Degree of r initilized with degree of this
		int rdeg = fdeg;
		
		// Begin the process, we perform a step until the degree of the residue is greater than the degree of the divisor
		while(rdeg >= gdeg) {
			
			// this represents the polynomial b * x^k that helps us in the syntetic division process
			bxk = g.shiftLeft(k);
			
			// 
			coef = r.sign();
			
			if (coef == 1) {
				// sum r and -b * x^k
				r = r.sum(bxk.negate());
				// add the coefficient for the quotient, the right position of the coefficient will be set later
				q = q.sum(one().shiftLeft(k));
			}
			else if (coef == -1) {
				// sum r and b * x^k
				r = r.sum(bxk);
				// add the coefficient for the quotient, the right position of the coefficient will be set later
				q = q.sum(one().shiftLeft(k).negate());
			}			
//			if ((getDeg(r)) == Integer.MIN_VALUE) {
//				break;
//			}
			// get the  degree of the new residue
			rdeg = r.getDeg();
			// left shift to reposition the q coefficients
			k = rdeg - gdeg; 
			//q = leftShift(q, k);
		}
		if (sign == -1)
			q = q.negate();
		return new GF3BigIntPoly[] {q, r};
	}
	
//	/**
//	 * 
//	 * @param a
//	 * @return
//	 */
//	public static int getCoefficient(int[][] a, int n) {
//		if (n > getDeg(a) || n < 0)
//			throw new InvalidParameterException("n cannot be greater than degree or less than zero");
//		int wordIndex = n / WORD_SIZE;
//		int bitIndex = n % WORD_SIZE;
//		if (((a[0][wordIndex] >>> bitIndex) & 1) == 1)
//			return -1;
//		if (((a[1][wordIndex] >>> bitIndex) & 1) == 1)
//			return 1;
//		return 0;
//	}
	
//	/**
//	 * 
//	 * @param n Degree of the desired polynomial
//	 * @return A random polynomial of degree n
//	 */
//	public static int[][] randomPolynomial (int n) {
//		if (n < 0)
//			throw new InvalidParameterException("degree of polynomial must be non-negative");
//		int[][] a = zero();
//		// the principal coefficient must not be zero
//		int random = (int) Math.round(Math.random());
//		if (random == 0)
//			a[0][0] += 1;
//		else if (random == 1)
//			a[1][0] += 1;
//		for (int i = 1; i < n; i++) {
//			a = leftShift(a, 1);
//			// create the coefficients one at a time
//			random = (int) Math.round(Math.random()*2 -1);
//			if (random == 1)
//				a[0][0] += 1;
//			else if (random == -1)
//				a[1][0] += 1;
//		}
//		return a;
//	}
	
	/**
	 * 
	 * @param a
	 * @param t positions and signs of the irreducible trinomial t = x^n + ax^k + b for the reduction
	 * (none coefficient must not be zero)
	 * @return reduced polynomial
	 */
	public GF3BigIntPoly reduce(int[] t) {
		// degree of the irreducible trinomial
		int n = t[2];
		if (this.getDeg() < n)
			return new GF3BigIntPoly(this);
		// coefficient of the second nonzero monomial
		int k = t[1];
		// get the first n terms of the polynomial a
		GF3BigIntPoly f1 = this.subPolynomial(n - 1);
		// get the rest of the coefficients
		GF3BigIntPoly f2 = this.shiftRight(n);
		// we sum and reduce (a_m * x^(m-n) + ... + a_n) * (-ax^k + -b) + (a_(n-1) * x^n + ... + a_0) = f2 * (-ax^k - b) + f1 
		if (k > 0) {
			if (t[0] > 0)
				return f2.shiftLeft(k).negate().sum(f2.negate()).sum(f1);
			else
				return f2.shiftLeft(k).negate().sum(f2).sum(f1);
		}
		else {
			if (t[0] > 0)
				return f2.shiftLeft(k).sum(f2.negate()).sum(f1);
			else
				return f2.shiftLeft(k).sum(f2).sum(f1);
		}
	}
	
	/**
	 * gets a polynomial of degree k with the coeficcients from a_0 to a_k of a
	 * @param a original polynomial
	 * @param k degree
	 * @return
	 */
	public GF3BigIntPoly subPolynomial(int k) {
		// Get the byte representation of the BigIntegers 
		byte[][] p = new byte[2][];
		p[0] = this.a[0].toByteArray();
		p[1] = this.a[1].toByteArray();
		
		// We need to turn the bits at the left of k to zero
		// all bytes beyon the leftmost word will be zero
		int t = k / 8;
		for (int i = t; i < p[0].length; i++)
			p[0][i] = 0;
		for (int i = t; i < p[0].length; i++)
			p[0][i] = 0;
		
		// we need to turn to zero the remaining bits of the leftmost word
		k = 8 - (k % 8);
		if (k != 0) {
			p[0][t - 1] = (byte)((p[0][t - 1] << k) >>> k);
			p[1][t - 1] = (byte)((p[1][t - 1] << k) >>> k);
		}
		return new GF3BigIntPoly(new BigInteger(p[0]), new BigInteger(p[1]));
	}
	
	/**
	 * return gcd applying the Euclide algorithm
	 * @param a
	 * @param b
	 * @return
	 */
	public GF3BigIntPoly gcd(GF3BigIntPoly g) {
		
		int n = this.getDeg();
		int m = g.getDeg();
		if (n == Integer.MIN_VALUE)
			return new GF3BigIntPoly(g);
		if (m == Integer.MIN_VALUE)
			return new GF3BigIntPoly(this);
		if (n == 0 || m == 0)
			return one();
		
		// Polinomio residuo de la division F / G si n > m o G / F si m > n
		GF3BigIntPoly r;
		
		if (n >= m) {
			r = this.divide(g)[1];
			return (g.gcd(r));
		}
		else {
			r = g.divide(this)[1];
			return (this.gcd(r));
		}
	}
	
	/**
	 * Ben-Or's irreducibility test for a trinomial t
	 * @param a
	 * @return
	 */
	public static boolean benOrTest(int[] t) {
		int n = t[2];
		GF3BigIntPoly f = trinomialToPolynomial(t);
		GF3BigIntPoly r = one().shiftLeft(1);
		GF3BigIntPoly commonDivisor;
		GF3BigIntPoly xk = one().shiftLeft(1);
		for (int i = 1; i <= (n + 1)/2; i++) {
			r = r.cube().reduce(t);
			commonDivisor = f.gcd(r.sum(xk.negate()));
			if (!one().equals(commonDivisor)) {
				return false;
			}
		}
		return true;
	}
	
//	/**
//	 * Ben-Or's irreducibility test for a polynomial a
//	 * @param a
//	 * @return
//	 */
//	public static boolean benOrTest(int[][] a) {
//		int n = getDeg(a);
//		int[][] r = leftShift(one(), 1);
//		int[][] commonDivisor;
//		int[][] xk = leftShift(one(), 1);
//		for (int i = 1; i <= (n + 1)/2; i++) {
//			r = reduce(cube(r), t);
//			commonDivisor = gcd(a, sum(r, negate(xk)));
//			if (!equals(one(), commonDivisor)) {
//				return false;
//			}
//		}
//		return true;
//	}
	
	/**
	 * compares both arrays to verify equality
	 * @param a
	 * @param b
	 * @return
	 */
	public boolean equals (GF3BigIntPoly g) {
		return (this.a[0].equals(g.a[0]) && this.a[1].equals(g.a[1]));
	}
	
	/**
	 * 
	 * @param a
	 * @return copy of polynomial a
	 */
	public static int[][] copyOf(int[][] a) {
		return new int[][] {a[0], a[1]};
	}
	
	/**
	 * 
	 * @param t
	 * @return
	 */
	public static GF3BigIntPoly trinomialToPolynomial(int[] t) {
		if (t.length != 3)
			throw new InvalidParameterException("the argument must be an array with 3 integer");
		int n = t[2], k = t[1];
		GF3BigIntPoly one = GF3BigIntPoly.one();
		if (k > 0)
			if (t[0] > 0)
				return one.shiftLeft(n).sum(one().shiftLeft(k), one);
			else
				return one.shiftLeft(n).sum(one().shiftLeft(k), one.negate());
		else
			if (t[0] > 0)
				return one.shiftLeft(n).sum(one().shiftLeft(k).negate(), one);
			else
				return one.shiftLeft(n).sum(one().shiftLeft(k).negate(), one.negate());
	}
	
	/**
	 * Presents a readable way of looking for a polynomial
	 * 
	 * @returns: polynomial string
	 */
	public String toString() {
		// if the polynomial is zero
		if (this.equals(GF3BigIntPoly.zero())) {
			return "0";
		}
		
		GF3BigIntPoly f = new GF3BigIntPoly(this);
		
		// Positions of nonzero bits
		BigInteger bits = this.a[0].or(this.a[1]);
		
		// string of the polybonomial
		StringBuilder pol = new StringBuilder("");		
		int k, sign;
		while ((k = bits.bitLength() - 1) != -1) {
			if ((sign = f.sign()) == 1)
				pol.append(" + x^" + k);
			else if (sign == -1)
				pol.append(" - x^" + k);
			bits = bits.clearBit(k);
			f.a[0] = f.a[0].clearBit(k);
			f.a[1] = f.a[1].clearBit(k);
		}
		return (pol.toString());
	}
}