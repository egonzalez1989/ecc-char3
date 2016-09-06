package crypto.ecc.finitefields;

import java.security.InvalidParameterException;
import java.util.ArrayList;

import crypto.ecc.test.TestConfiguration;

public class GF3BinPoly {
	
//	private static boolean isInit = false;
	// this must change according to the platform architecture, must see how to do it in java
	private static int WORD_SIZE = 32; //Integer.parseInt(System.getProperty("sun.arch.data.model"));

	public final static int[][] ONE = one();
	public final static int[][] ZERO = one();
	
	/**
	 * 
	 */
	public static int[][] zero() {
		return new int[2][1];
	}
	
	/**
	 * 
	 */
	public static int[][] one() {
		return new int[][]{new int[1], new int[]{1}};
	}
	
	/**
	 * Leftshifts both arrays the required positions
	 * @param a
	 * @param shiftSize
	 */
	public static int[][] leftShift (int[][] a , int n) {
		if (n < 0)
			return GF3BinPoly.rightShift(a, -n);
		if (n == 0)
			return GF3BinPoly.copyOf(a);
		
		// as every word has 32 bits, if n > 32 then it's sure that we will need extra-words (more than the original array)
		int nWords = n >>> TestConfiguration.WORD_SIZE_EXPONENT;
		
		// number bits to be shifted (excluding the full words)
		int nBits = n & 0x1f;
//		int[][] shiftedA;
		int[][] shiftedA = new int[2][];
		int t = a[0].length;
		
		// begin the shifting process
		// if there are no single bits to shift then we only have to shift words
		if (nBits == 0) {
//			shiftedA = new int[2][t + nWords];
			shiftedA[0] = new int[t + nWords];
			shiftedA[1] = new int[t + nWords];
			System.arraycopy(a[0], 0, shiftedA[0], nWords, t);
			System.arraycopy(a[1], 0, shiftedA[1], nWords, t);
		}
		else {
			int carryBits = WORD_SIZE - nBits;
			int lastBits0 = a[0][t - 1] >>> carryBits, 
				lastBits1 = a[1][t - 1] >>> carryBits;
			//int lastBits[] = new int[] {a[0][t - 1] >>> carryBits, a[1][t - 1] >>> carryBits,};
			
			// if som of the lastbits is non-zero then we have to add these bits in an extra word
			if ((lastBits0 | lastBits1) != 0) {
//				shiftedA = new int[2][t + nWords + 1];
				shiftedA[0] = new int[t + nWords + 1];
				shiftedA[1] = new int[t + nWords + 1];
				shiftedA[0][t + nWords] = lastBits0;
				shiftedA[1][t + nWords] = lastBits1;
			}
			else {
//				shiftedA = new int[2][t + nWords];
				shiftedA[0] = new int[t + nWords];
				shiftedA[1] = new int[t + nWords];
			}
			
			// fill the words accordingly
			shiftedA[0][nWords] = a[0][0] << nBits;
			shiftedA[1][nWords] = a[1][0] << nBits;
			int j = nWords + 1;
			for (int i = 1; i < t; i++) {
				shiftedA[0][j] = a[0][i - 1] >>> carryBits | a[0][i] << nBits;
				shiftedA[1][j++] = a[1][i - 1] >>> carryBits | a[1][i] << nBits;
			}
		}
		return shiftedA;		
	}
	
	/**
	 * Right-shifts both arrays the required positions, rightmost bits will be lost
	 * @param a
	 * @param shiftSize
	 */
	public static int[][] rightShift (int[][] a, int n) {		
		if (n < 0)
			return leftShift(a, -n);
		if (n == 0)
			return GF3BinPoly.copyOf(a);
		
		// if he shiftSize is bigger than the degree then we return de zero polynomial
		int aDeg = getDeg(a);
		if (n > aDeg)
			return GF3BinPoly.zero();
		int ta = a[0].length;
		
		// number of complete words to be shifted
		int nWords = n >>> TestConfiguration.WORD_SIZE_EXPONENT;
		int t = ta - nWords;
		
		// number bits to be shifted (excluding the full words)
		int[][] shiftedA = new int[2][];
		int nBits = n & 0x1f;
		
		// Only full words will be shifted
		if (nBits == 0) {
			shiftedA[0] = new int[t];
			shiftedA[1] = new int[t];
			System.arraycopy(a[0], nWords, shiftedA[0], 0, t);
			System.arraycopy(a[1], nWords, shiftedA[1], 0, t);
		}
		else {
			// if there are single bits to move then we must verify if the last word  becomes 0 because of the action of the
			// bit shift
			int lastBits0 = a[0][ta - 1] >>> nBits, 
				lastBits1 = a[1][ta - 1] >>> nBits;
			if ((lastBits0 | lastBits1) != 0) {
				shiftedA[0] = new int[t];
				shiftedA[1] = new int[t];
				shiftedA[0][--t] = lastBits0;
				shiftedA[1][t] = lastBits1;
			}
			else {
				shiftedA[0] = new int[--t];
				shiftedA[1] = new int[t];
			}
			int carryBits = WORD_SIZE - nBits;
			int j = nWords;
			
			// fill the words			
			for (int i = 0; i < t; i++) {
				shiftedA[0][i] = a[0][j] >>> nBits | a[0][j + 1] << carryBits;
		        shiftedA[1][i] = a[1][j++] >>> nBits | a[1][j] << carryBits;            
			}			
		}
        return shiftedA;
	}
	
	/**
	 * Position (starting at 1) of the leftmost (greater degree) non-zero bit in the binary representation of a number
	 * @param n
	 * @return leftmost non-zerobit of n
	 */
	public static int leftmostNonZeroBitPos(int n) {
		if (n == 0)
			return 0;
		int i = 1;
		while (n > 0) {
			n = n << 1;
			i++;
		}
		return (WORD_SIZE - i);		
	}
	
	/**
	 * Position (starting at 1) of the rightmost (lower degree) non-zero bit in the binary representation of a number
	 * @param n
	 * @return rightmost non-zerobit of n
	 */
	public static int rightmostNonZeroBitPos(int n) {
		if (n == 0)
			return 0;
		int i = 1;
		while ((n & 1) != 1) {
			n = n >> 1;
			i++;
		}
		return i;		
	}
	
	/**
	 * Gets the degree of the polynomail based on the last non-zero element of the arrays that represent it
	 * @param a binary representation of the polynomial
	 * @return
	 */
	public static int getDeg(int[][] a) {
		// As we are supposing that no polynomial has its last word with value zero (except the zero polynomial)
		// we get the degree based on the number of words an the last nonzero bit of the last word
		int t = a[0].length;
		
		// Verify for the zero polynomial
		if (t - 1 == 0 && (a[0][0] ^ a[1][0]) == 0)
			return Integer.MIN_VALUE;
		
		// Perform an or operation, the last nonzero bit in n will be in the position of the last nonzero bit of some
		// of the polynomial arrays
		int n = a[0][t - 1] ^ a[1][t - 1];
		int pos = leftmostNonZeroBitPos(n);
		return ((t - 1) * WORD_SIZE + pos);
	}
	
	/**
	 * BitWise sum for polynomial in GF3[X]
	 * @param a
	 * @param b
	 */
	public static int[][] sum(int[][] a, int[][] b) {		
    	// matrix with the result of the sum 
		int[][] c;
		int s, t = a[0].length;
		
    	// if the arrays have different size then we just copy the last elements of the longest
    	if (t > b[0].length) {
        	s = b[0].length;
        	c = new int[][] {new int[t], new int[t]};
        	for (int i = s; i < t; i++) {
        		c[0][i] = a[0][i];
                c[1][i] = a[1][i];
        	}       
        }
        else if (t < b[0].length){
        	t = b[0].length;
        	s = a[0].length;
        	c = new int[][] {new int[t], new int[t]};
        	for (int i = s; i < t; i++) {
        		c[0][i] = b[0][i];
                c[1][i] = b[1][i];
        	}        	
        }
    	
    	// if both arrays have the same length, we remove the zero words by comparing when one word of a is the
    	// negative of the corresponding word in b
        else {
        	while (t > 0 && b[1][t - 1] == a[0][t - 1] && b[0][t - 1] == a[1][t - 1])
        		t--;
        	s = t;
        	c = new int[][] {new int[t], new int[t]};
        }
    	
    	// zero
    	if (t == 0)
    		return new int[][] {new int[1], new int[1]};
    	
        // Apply the sum just as in [1] for the rest of the elements    	
    	int carry = 0;
        for (int i = 0; i < s; i++) {
            carry = (a[0][i] | b[1][i]) ^ (a[1][i] | b[0][i]);
            c[0][i] = (a[1][i] | b[1][i]) ^ carry;
            c[1][i] = (a[0][i] | b[0][i]) ^ carry;
        }
        return c;
    }
	
	/**
	 * Sum of an arbitrary number of polynomials
	 * @param a
	 * @return
	 */
	public static int[][] sum(int[][]... a) {
		int n = a.length;
		if (n == 0)
			return new int[2][1];
		if (n == 1)
			return a[0];
		int[][] s = sum(a[0], a[1]);
		for (int i = 2; i < n; i++) {
			s = sum(s, a[i]);
		}
		return (s);
	}
	
	/**
	 * Gets the additive inverse of the polynomial
	 * @param a
	 * @return
	 */
	public static int[][] negate(int[][] a) {
		// the inverse is given by changing the order of the arrays
		return new int[][]{a[1], a[0]};
	}
	
	/**
	 * Bitwise multiplication for polynomials in GF3[X]
	 * @param a
	 * @param b
	 */
	public static int[][] rightLeftCombMultiply(int[][] a, int[][] b) {		
		int aux[][];
		int adeg = getDeg(a);
		int bdeg = getDeg(b);
		int[][] c = zero();
		
		// if one of the polynomials is zero then their product is zero
		if (adeg == Integer.MAX_VALUE || bdeg == Integer.MIN_VALUE) {
			return c;
		}
					
		// in order to make the less possible iterations for the product, we choose a[][] to be the array with
		// less elements
		if (adeg > bdeg) {
			aux = a;
			a = b;
			b = aux;
			int tmp = adeg;
			adeg = bdeg;
			bdeg = tmp;
		}
		
		// initialize a matrix in order to not to modify the coefficients of polynomial b
		aux = new int[][] {b[0], b[1]};		
		
		// Iterate through each position of one word of the coefficient arrays to perform the comb method
		int ta = a[0].length;
		for (int k = 0; k < WORD_SIZE; k++) {
			// if the kth coefficient of a[j] is 1 the we perform a sum c{j} + b
            if ((1 & (a[1][0] >>> k)) == 1)
                c = sum(c, aux);
            
            // if the kth coefficient of a[j] is -1 the we perform a rest c{j} - b
            else if ((1 & (a[0][0] >>> k)) == 1)
            	c = sum(c, negate(aux));
            
            for (int j = 1; j < ta; j++) {
            	// left-shift i words to perform the sum of c{j} and b (or c and b*z^{iW})
            	aux = leftShift(aux, WORD_SIZE);
            	
                // if the k-th coefficient of a[j] is 1 the we perform a sum c{j} + b
                if ((1 & (a[1][j] >>> k)) == 1) {
                    c = sum(c, aux);
                
                }
                
                // if the k-th coefficient of a[j] is -1 the we perform a rest c{j} - b
                else if ((1 & (a[0][j] >>> k)) == 1) {
                	c = sum(c, negate(aux));
                }
            
            }
            
            // When we finish with the kth element of every word we proceed to the element (k+1)th
            aux = leftShift(b, k + 1);
        }
		return c;
	}
	
	/**
	 *multiplication of polinomials 
	 * @param a
	 * @param b
	 * @return
	 */
	public static int[][] multiply(int[][] a, int[][] b) {
		return rightLeftCombMultiply(a, b);
	}
	
	/**
	 *multiplication of more than two polynomials 
	 * @param a
	 * @return
	 */
	public static int[][] multiply(int[][]... a) {
		int n = a.length;
		if (n == 0)
			return GF3BinPoly.copyOf(a[0]);
		int[][] m = multiply(a[0], a[1]);
		for (int i = 2; i < n; i++) {
			m = multiply(m, a[i]);
		}
		return (m);
	}
	
	/**
	 * Cube of a polynomial
	 * @param a
	 */
	public static int[][] cube(int[][] a) {
		int r = WORD_SIZE % 3;
		// cube of a polynomial depending on the word size
		if (r == 1)
			return cubeEvenExp(a);
		else
			return cubeOddExp(a);			
	}
	
	/**
	 * Cube of a polynomial when having wordSize of the form 2^(2n-1) for som n
	 * @param a
	 */
	public static int[][] cubeOddExp(int[][] a){		
		int ta = a[1].length;		
		
		// each word will give rise to three words which could be zero, 
		int w1 = WORD_SIZE / 3;
		int w2 = (WORD_SIZE + 1) / 3;
		int w3 = 2 * w2;
		
		// the cube must use as many words as a polynomial of three times the degree of the original polynomial
		int[][] c = new int[2][];
		int ref = a[0][ta - 1] | a[1][ta - 1];
		
		// if the last 10 bits of the last word have at least a non-zero bit then we need exactly 3 * ta words
		int t;
		if ((ref >>> w3) > 0) {
			t = 3 * ta;
			c[0] = new int[t];
			c[1] = new int[t];			
		}
		
		// if the last 21 bits (but not the last 10 bits) have a nonzero bit then we need 3 * ta -1 words
		else if ((ref >>> w2) > 0) {
			t = 3 * ta - 1;
			c[0] = new int[t];
			c[1] = new int[t];			
			// fill the arrays of the cube that are given by the last word
			ta--;
			t = t - 2;
			for (int j = 0; j <= w1; j++) {
				c[1][t] += ((a[1][ta] >> j) & 1) << 3 * j;
				c[1][t + 1] += ((a[1][ta] >> j + w2) & 1) << 3 * j + 1;
				c[0][t] += ((a[0][ta] >> j) & 1) << 3 * j;
				c[0][t + 1] += ((a[0][ta] >> j + w2) & 1) << 3 * j + 1;
			}
		}
		
		// finally, we have that the rightmost non-zero bit in the last words lies in the first 11 bits
		else {
			t = 3 * ta - 2;
			c[0] = new int[t];
			c[1] = new int[t];
			t--;
			ta--;
			for (int j = 0; j <= w1; j++) {
				c[1][t] += ((a[1][ta] >> j) & 1) << 3 * j;
				c[0][t] += ((a[0][ta] >> j) & 1) << 3 * j;
			}
		}
	
		t--;
		// fill the remaining words
		for (int i = 0; i < ta; i++) {
			for (int j = 0; j <= w1; j++)
			{
				c[1][3 * i] += ((a[1][i] >> j) & 1) << 3 * j;
				c[1][3 * i + 1] += ((a[1][i] >> j + w2) & 1) << 3 * j + 1;
				c[1][3 * i + 2] += ((a[1][i] >> j + w3) & 1) << 3 * j + 2;
				c[0][3 * i] += ((a[0][i] >> j) & 1) << 3 * j;
				c[0][3 * i + 1] += ((a[0][i] >> j + w2) & 1) << 3 * j + 1;
				c[0][3 * i + 2] += ((a[0][i] >> j + w3) & 1) << 3 * j + 2;
			}
			
			// In the last array we could get a 1 bit at the beginning, so we make it zero if necessary 
			c[1][3 * i + 2] = (c[1][3 * i + 2] >> 1) << 1;
			c[0][3 * i + 2] = (c[0][3 * i + 2] >> 1) << 1;
		}		
		return c;
	}
	
	/**
	 * Cube of a polynomial when having wordSize of the form 2^(2n) for some n
	 * @param a
	 */
	public static int[][] cubeEvenExp(int[][] a) {
		System.out.println("Not yet");
		return new int[1][0];
	}
	
	/**
	 * power of a polynomial
	 * @param a
	 * @param k
	 * @return
	 */
	public static int[][] pow(int[][] a, int k) {
		// We cannot compute a negative power
		if (k < 0)
			throw new InvalidParameterException("Power must be positive or zero");
		// if k = 0 and a is not zero the the result must be the polynomial 1
		if (k == 0)
			return one();
		
		// Express k as a sum of powers of 3 with coefficients in {0, 1, 2}
		// list of coefficient of the ternary representation of k
		ArrayList<Integer> c = new ArrayList<Integer>();
		while(k > 0) {
			c.add(k & 0X03);
			k = k / 3;
		}
		c.add(k);
		int size = c.size();
		
		// Calculate the power of a using consecutive squaring an precalculating a^2 for 2 valued coefficients
		int[][] square = multiply(a, a);
		
		// result of the operation
		int[][] p;
		
		// we initilalize the polynomial according to the value of the first coefficient found
		p = one();
		if (c.get(0) == 1)
			p = new int[][]{a[0], a[1]};
		else if (c.get(0) == 2)
			p = new int[][]{square[0], square[1]};
		
		// 
		for(int i = 1; i < size; i++) {
			// calculate a^(3^i) and (a^2)^(3^i) for further use (a will not be changed in the caller code since
			// array's reference are passed by value 
			a = cube(a);
			square = cube(square);
			switch (c.get(i)) {
				// each time we have a 1 coefficient the value of p will be multiplied by a^(3^i)
				case 1 :					
					p = multiply(p, a);
					break;
				// if the coefficient is 2 the we multiply by (a^2)^(3^i)
				case 2 :				
					p = multiply(p, square);
					break;
				default :
					break;
			}
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
	public static int[][] powAndReduce(int[][] a, int[] t, int k) {
		//We cannot compute a negative power
		if (k < 0) {
			throw new InvalidParameterException("Power must be positive or zero");
		} 
		if (k == 0)
			return one();
		
		// Express k as a sum of powers of 3 with coefficients in {0, 1, 2}
		// list of coefficient of the ternary representation of k
		ArrayList<Integer> c = new ArrayList<Integer>();
		while(k > 0) {
			c.add(k & 0x03);
			k = k / 3;
		}
		c.add(k);
		
		int size = c.size();
		
		// Calculate the power of a using consecutive squaring an precalculating a^2 for 2 valued coefficients
		int[][] square = reduce(multiply(a, a), t);
		
		// result of the operation
		int[][] p;
		// we initilalize the polynomial according to the value of the first coefficient found
		p = one();
		if (c.get(0) == 1)
			p = new int[][]{a[0], a[1]};
		else
			p = new int[][]{square[0], square[1]};
		// 
		for(int i = 1; i < size; i++) {
			// calculate a^(3^i) and (a^2)^(3^i) for further use (a will not be changed in the caller code since
			// array's reference are passed by value 
			a = reduce(cube(a), t);
			square = reduce(cube(square), t);
			switch (c.get(i)) {
				// each time we have a 1 coefficient the value of p will be multiplied by a^(3^i)
				case 1 :	
					p = multiply(p, a);
					p = reduce(p, t);
					break;
				// if the coefficient is 2 the we multiply by (a^2)^(3^i)
				case 2 :				
					p = multiply(p, square);
					p = reduce(p, t);
					break;
				default :
					break;
			}
		}
		return p;
	}
	
	/**
	 * 
	 * @param a dividend
	 * @param b divisor
	 */
	public static int[][][] divide(int[][] a, int[][] b) {
		// it is not possible to divide by zero, however this is not likely to happen, as the polynomial b will be an irreducible nonzero polynomial
//		if (b[0].length == 1 && (b[0][0] ^ b[1][0]) == 0)
//			throw new IllegalArgumentException("The second argument must not be zero");
		// Indicates the sign of the prinicipal coeffcient: true = negative, false = positive
		boolean flag = false;
		int adeg = getDeg(a), bdeg = getDeg(b);
		int tb = b[1].length;
		
		// if the degree of the divisor is greater than that of the dividend then there is no much to do
		if (adeg - bdeg < 0) {
			return new int[][][] {zero(), a};
		}
		
		int k = adeg - bdeg;
		// if the principal coefficient of b is negative multiply it by -1 (as the reference is passed by value there is no side effect)
		// this comparison is made by noting that a right unsigned shift always has a result a non-negative number, in this case we
		// identify the sign of the polynomial by verifying which of the numbers in the last word is greater, because of the leftmost 1
		// bit position, otherwise, a special case is when we have that one of the last words is zero an the other 1, so the last criteria
		// won't apply an we check jus by direct comparison (without shifting)
		
		if ((b[0][tb - 1] >>> 1) > (b[1][tb - 1] >>> 1) || (b[0][tb - 1] == 1 && b[1][tb - 1] == 0)) {
			b = negate(b);
			flag = true;
		}
		
		// sum of b * x^k and the result of remainder in each iteration process (initialize r = a)
		int[][] r  = new int[][]{a[0], a[1]};
		// quotient
		int[][] q = zero();
		// Polynomial b * x^k
		int[][] bxk;
		// Principal coeficcient of r 
		int coef;
		
		// Degree of r initilized with degree of a
		int rdeg = adeg;
		while(rdeg >= bdeg) {
			// this represents the polynomial b * x^k that helps us in the syntethic division process
			bxk = leftShift(b, k);
			
			// sign of the principal coefficient of r
			coef = sign(r);
			
			if (coef == 1) {
				r = sum(r, negate(bxk));
				// add the coefficient for the quotient, the right position of the coefficient will be set later
				q = sum(q, leftShift(one(), k));
			}
			
			else if (coef == -1) {
				r = sum(r, bxk);
				// add the coefficient for the quotient, the right position of the coefficient will be set later
				q = sum(q, negate(leftShift(one(), k)));
			}			
			
			// get the  degree of the new residue
			rdeg = getDeg(r);
			
			// left shift to reposition the q coefficients
			k = rdeg - bdeg; 
		}
		
		if (flag)
			q = negate(q);
		return new int[][][] {q, r};
	}
	
	/**
	 * Gets the coefficient of x^n
	 * @param a
	 * @return
	 */
	public static int getCoefficient(int[][] a, int n) {
		if (n > getDeg(a) || n < 0)
			throw new InvalidParameterException("n cannot be greater than degree or less than zero");
		int wordIndex = n >>> TestConfiguration.WORD_SIZE_EXPONENT;
		int bitIndex = n & 0x1f;
		if (((a[0][wordIndex] >>> bitIndex) & 1) == 1)
			return -1;
		if (((a[1][wordIndex] >>> bitIndex) & 1) == 1)
			return 1;
		return 0;
	}
	
	/**
	 * Gets the sign of a
	 * @param a
	 * @return
	 */
	public static int sign(int[][] a) {
		int t = a[0].length - 1;
		if ((a[0][t] | a[1][t]) == 1)
			return a[1][t] - a[0][t];
		else if ((a[0][t] >>> 1) > (a[0][t] >> 1))
			return -1;
		else
			return 1;
	}
	
	/**
	 * Produces a random polynomial with coefficient in {1, 0, -1}
	 * @param n Degree of the desired polynomial
	 * @return A random polynomial of degree n
	 */
	public static int[][] randomPolynomial (int n) {
		if (n < 0)
			throw new InvalidParameterException("degree of polynomial must be non-negative");
		int[][] a = zero();
		
		// the principal coefficient must not be zero
		int random = (int) Math.round(Math.random());
		if (random == 0)
			a[0][0] += 1;
		else if (random == 1)
			a[1][0] += 1;
		
		// get the other coefficients
		for (int i = 1; i < n; i++) {
			a = leftShift(a, 1);
			// create the coefficients one at a time
			random = (int) Math.round(Math.random()*2 -1);
			if (random == 1)
				a[0][0] += 1;
			else if (random == -1)
				a[1][0] += 1;
		}
		return a;
	}
	
	/**
	 * Reduces a polynomial with the given trinomial t
	 * @param a
	 * @param t positions and signs of the irreducible trinomial t = x^n + ax^k + b for the reduction
	 * (none coefficient must not be zero)
	 * @return reduced polynomial
	 */
	public static int[][] reduce(int[][] a, int[] t) {
		// degree of the irreducible trinomial
		int n = t[2];
		if (getDeg(a) < n)
			return a;
		
		// coefficient of the second nonzero monomial
		int k = t[1];
		// get the first n terms of the polynomial a
		int[][] a1 = subPolynomial(a, n - 1);
		// get the rest of the coefficients
		int[][] a2 = rightShift(a, n);
		
		// we sum and reduce (a_m * x^(m-n) + ... + a_n) * (-ax^k + -b) + (a_(n-1) * x^n + ... + a_0)
		if (k > 0) {
			if (t[0] > 0)		
				return reduce(sum(negate(leftShift(a2, k)), negate(a2), a1), t);
			else
				return reduce(sum(negate(leftShift(a2, k)), a2, a1), t);
		}
		else {
			if (t[0] > 0)
				return reduce(sum(leftShift(a2, -k), negate(a2), a1), t);
			else
				return reduce(sum(leftShift(a2, -k), a2, a1), t);
		}
	}
	
	/**
	 * Gets a polynomial of degree n with the coeficcients from a_0 to a_n of a
	 * @param a original polynomial
	 * @param n degree
	 * @return
	 */
	public static int[][] subPolynomial(int[][] a, int n) {
		if (n < 0)
			throw new InvalidParameterException("n must be positive");
		
		// if the original polynomial has lower degree than the truncated polynomial, we return it
		if (getDeg(a) < n)
			return a;
		// size of the resulting polynomial
		int t = (n >>> TestConfiguration.WORD_SIZE_EXPONENT) + 1;
		
		// words of the original polynomial
		int ta = a[0].length;
		if (equals(a, zero()) |  t > ta)
			return copyOf(a);
		
		// bits of the last word that will be taked in account for the new polynomial
		// check how many bits of the last word are required, the rest of the word (left part) must be zero
		int nBits = (n & 0x1f) + 1;
		int aux = -1 >>> WORD_SIZE - nBits;
		int lastBits0 = a[0][t - 1] & aux;
		int lastBits1 = a[1][t - 1] & aux;
		
		// sub-polynomial
		int[][] b = new int[2][];
		
		// if the last bits are non zero then we copy all the word and set the value of the last word  
		if ((lastBits0 | lastBits1) != 0) {
			b[0] = new int[t];
			b[1] = new int[t];
			b[0][--t] = lastBits0;
			b[1][t] = lastBits1;
			System.arraycopy(a[0], 0, b[0], 0, t);
			System.arraycopy(a[0], 0, b[0], 0, t);
		}
		
		// if the last bit are zero we need to find the first nonzero word, otherwise we will have an invalid polynomial
		// (with a zero in the leftmost word)
		else {			
			while (t > 0 && ((a[0][--t] | a[1][t]) == 0)) {}
			if (t == 0)
				return GF3BinPoly.zero();
			else {
				b[0] = new int[t];
				b[1] = new int[t];
				System.arraycopy(a[0], 0, b[0], 0, --t);
				System.arraycopy(a[0], 0, b[0], 0, t);
			}
		}
		return b;
	}
	
	/**
	 * return gcd applying the Euclide Algorithm
	 * @param a
	 * @param b
	 * @return
	 */
	public static int[][] gcd(int[][] a, int[][] b) {		
		int n = getDeg(a);
		int m = getDeg(b);
		
		// if any value is zero return the one  of the original polynomials
		if (n == Integer.MIN_VALUE)
			return b;
		if (m == Integer.MIN_VALUE)
			return a;
		
		// if any of the polynomials is constant then the gcd is 1
		if (n == 0)
			return one();
		if (m == 0)
			return one();
		
		// residue
		int[][] r;
		
		// apply the gcd algorithm
		if (n >= m) {
			r = divide(a, b)[1];
			return (gcd(b, r));
		}
		else {
			r = divide(b, a)[1];
			return (gcd(a, r));
		}
	}
	
	/**
	 * Ben-Or's irreducibility test for a trinomial t
	 * @param a
	 * @return
	 */
	public static boolean benOrTest(int[] t) {
		int n = t[2];
		int[][] a = trinomialToPolynomial(t);
		int[][] r = leftShift(one(), 1);
		int[][] commonDivisor;
		int[][] xk = leftShift(one(), 1);
		for (int i = 1; i <= (n + 1)/2; i++) {
			r = reduce(cube(r), t);
			commonDivisor = gcd(a, sum(r, negate(xk)));
			if (!equals(one(), commonDivisor)) {
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
	 * compares polynomial�s values to verify equality
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean equals (int[][] a, int[][] b) {
		// the verification is made word by word
		int ta = a[0].length;
		
		if (ta != b[0].length)
			return false;
		
		int i = 0;
		while (i < ta) {
			if (((a[0][i] ^ b[0][i]) | (a[1][i] ^ b[1][i])) != 0)
				return false;
			i++;
		}
		return true;
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
	public static int[][] trinomialToPolynomial(int[] t) {
		if (t.length != 3)
			throw new InvalidParameterException("the argument must be an array with 3 integer");
		int[][] a;
		int n = t[2], k = t[1];
		if (k > 0)
			if (t[0] > 0)
				a = sum(leftShift(one(), n), leftShift(one(), k), one());
			else
				a = sum(leftShift(one(), n), leftShift(one(), k), negate(one()));
		else
			if (t[0] > 0)
				a = sum(leftShift(one(), n), negate(leftShift(one(), -k)), one());
			else
				a = sum(leftShift(one(), n), negate(leftShift(one(), -k)), negate(one()));
		return a;
	}
	
	/**
	 * Presents a readable way of looking for a polynomial
	 * 
	 * @returns: polynomial string
	 */
	public static String toString(int[][] a) {
		// if the polynomial is zero
		if (equals(a, zero())) {
			return "0";
		}
		// we get the coefficients of the polynomial and store them in an array
		// of coefficients in {1, 0, -1} by getting a1 - a0
		int n = a[0].length;
		StringBuilder pol = new StringBuilder("");
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < WORD_SIZE; j++) {
				if (((a[1][i] >> j) & 1) == 1) {
					pol.append(" + X^").append(i * WORD_SIZE + j);
				}
				else if (((a[0][i] >> j)& 1) == 1) {
					pol.append(" - X^").append(i * WORD_SIZE + j);
				}
			}
		}
		pol.replace(0, 6, ((a[1][0] & 1) - (a[0][0] & 1)) + "");
		return (pol.toString());
	}
	
	public static boolean isZero(int a[][]) {
		return ((a[0].length == 1) && (a[0][0] | a[1][0]) == 0);
	}
}