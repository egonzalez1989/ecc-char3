


import java.math.BigInteger;

import crypto.ecc.*;
import crypto.ecc.finitefields.*;

public class Tests {

	/**
	 * @param args
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		
		byte[] bigInt = new byte[]{1};
		BigInteger B = new BigInteger(bigInt);
		BigInteger A = B.shiftLeft(8);
		int st1 = A.bitLength();
		int st2 = A.bitCount();
 		bigInt = A.toByteArray();
 		bigInt = A.clearBit(0).toByteArray();
 		bigInt = A.clearBit(8).toByteArray();
 		bigInt = A.flipBit(0).toByteArray();
 		bigInt = A.flipBit(8).toByteArray();
		B = new BigInteger(1, bigInt);
		st1 = B.bitLength();
		st2 = B.bitCount();
		//BigInteger C = new BigInteger(bigInt);
		A = B.shiftRight(50);	
		st1 = BigInteger.ZERO.bitLength();
		st2 = BigInteger.ZERO.bitCount();
		A = changeBigInt(B);
		
		int x[] = new int[]{1};
		int y[] = x;
		y[0] = 0;
		
		x = new int[]{1};
		y = returnArray(x);
		y[0] = 0;
		
		long end = 0, start = 0;
		int n = 239; 
		int t[] = {-1, 24, n};
//		int t = 1000000;
//		// F3Polynomial[] F = new F3Polynomial[t];
//		// F3Polynomial G;
//		GF3n f, g;
//		Polynomial P, Q;
//
//		/********************************* ARRAY COPY *************************************************/		
//		
//		int t1 = 8;		
//		int[] x1 = new int[]{1};
//		int[][] x = new int[][]{x1};
//		System.out.println(Arrays.toString(x[0]));		
//		x1 = null;
//		System.out.println(Arrays.toString(x[0]));
///*		int[] toCopy = new int[t1];
//		int[] b = new int[t1];
//		for (int i = 0; i < t1; i++) {
//			toCopy[i] = i;
//		}
//		
//		//
//		
//		start = System.currentTimeMillis();
//		for (int i = 0; i < 1000000; i++) {
//			b = Arrays.copyOf(toCopy, t1);
//		}
//		end = System.currentTimeMillis();
//		System.out.println("CopyOf time: " + (end - start));
//
//		//
//		
//		start = System.currentTimeMillis();
//		for (int i = 0; i < 1000000; i++) {
//			b = new int[t1];
//			System.arraycopy(toCopy, 0, b, 0, t1);
//		}
//		end = System.currentTimeMillis();
//		System.out.println("System arraycopy time: " + (end - start));
//
//		//
//
//		start = System.currentTimeMillis();
//		for (int i = 0; i < 1000000; i++) {
//			b = new int[t1];
//			for (int j = 0; j < t1; j++) {				
//				b[j] = toCopy[j];
//			}
//		}
//		end = System.currentTimeMillis();
//		System.out.println("By element time: " + (end - start));
//
//		//
//
//		start = System.currentTimeMillis();
//		for (int i = 0; i < 1000000; i++) {
//			b = toCopy.clone();
//		}
//		end = System.currentTimeMillis();
//		System.out.println("Clone time: " + (end - start));
//
//		//
//
//		start = System.currentTimeMillis();
//		for (int i = 0; i < 1000000; i++) {
//			b = toCopy;
//		}
//		end = System.currentTimeMillis();
//		System.out.println("Pointer time: " + (end - start));*/

		/********************************** Polynomial initialization ********************************/

		System.out.println("Initializing Polynomial");
		// for binary polynomials
		int[][] a = GF3BinPoly.randomPolynomial(n);
		int[][] b = GF3BinPoly.randomPolynomial(n);
		
		////////////////////////////		binary            //////////////////////////
		int iter = 100000;
		GF3Poly f = new GF3Poly(a);
		GF3Poly g = new GF3Poly(b);
		
		start = System.currentTimeMillis();
		for (int i = 0; i < iter; i++) {
			f = new GF3Poly(a);
		}
		end = System.currentTimeMillis();
		System.out.println("f = " + f.toString());
		System.out.println("g = " + g.toString());
		System.out.println("Binary pol init time: " + (float)((float)(end - start)/100));
		
		/*********************************************** POLYNOMIAL SUM *************************************************/
		
		start = System.currentTimeMillis();
		for (int i = 0; i < iter; i++) {
			f.sum(g);
		}
		end = System.currentTimeMillis();
		System.out.println(f.sum(g).toString());
		System.out.println("binary sum time: " + (float)((float)(end - start)/100));
		
		/***************************************** MULTIPLICATION *********************************************/
		
		start = System.currentTimeMillis();
		for (int i = 0; i < iter; i++) {
			f.multiply(g);
		}
		end = System.currentTimeMillis();
		System.out.println("binary mult time: " + (float)((float)(end - start)/100));
		System.out.println(f.multiply(g).toString());
		
		/***************************************** DIVISION ********************************************************************/		
		
		b = GF3BinPoly.randomPolynomial(n/2);		
		
		g =  new GF3Poly(b);
		start = System.currentTimeMillis();
		for (int i = 0; i < iter; i++) {
			f.divide(g);
		}
		end = System.currentTimeMillis();
		System.out.println("binary division time: " + (float)((float)(end - start)/100));
		System.out.println("q = " + f.divide(g)[0].toString());
		System.out.println("r = " + f.divide(g)[1].toString());

		/***************************************** CUBE ********************************************************************/		
		
		start = System.currentTimeMillis();
		for (int i = 0; i < iter; i++) {
			f.cube();
		}
		end = System.currentTimeMillis();
		System.out.println("binary cube time: " + (float)((float)(end - start)/100));
		System.out.println("r = " + f.cube().toString());
		
		/*************************************** POW ***************************************************/
		
		start = System.currentTimeMillis();
		for (int i = 0; i < iter; i++) {
			f.pow(3);
		}
		end = System.currentTimeMillis();
		System.out.println("binary cube by pow time: " + (float)((float)(end - start)/100));
		System.out.println("r = " + f.pow(3).toString());
		
		start = System.currentTimeMillis();
		for (int i = 0; i < iter; i++) {
			f.cube().reduce(t);
		}
		end = System.currentTimeMillis();
		System.out.println("binary cube.reduce time: " + (float)((float)(end - start)/100));
		System.out.println("r = " + f.cube().reduce(t).toString());
		
		start = System.currentTimeMillis();
		for (int i = 0; i < iter; i++) {
			f.powAndReduce(3, t);
		}
		end = System.currentTimeMillis();
		System.out.println("binary cube by powAndReduce time: " + (float)((float)(end - start)/100));
		System.out.println("r = " + f.powAndReduce(3, t).toString());
		
//		a[1] = new int[16];
//		a[0] = new int[16];		
//		for (int i = 8; i < 16; i++) {
//			a[1][i] = 0xffffffff;
//		};
//		b[1] = new int[8];
//		for (int i = 0; i < 8; i++) {
//			b[1][i] = 0xffffffff;
//		};
//		b[0] = new int[8];
//		f = new GF3n(a);
//		g = new GF3n(b);
//		GF3n[] qr = f.divideBy(g);
//		start = System.currentTimeMillis();
//		for (int i = 0; i < 100000; i++) {
//			qr = f.divideBy(g);
//		}
//		end = System.currentTimeMillis();
//		System.out.println("GF3Polynomial Division: " + (end - start));
//		System.out.println("q =" + qr[0].toString() + "\nr = " + qr[1].toString());
//		
//		System.out.println("g = " + g.toString());
//		System.out.println("g^3 = " + g.cube().toString());
//		boolean result;
		
//		result = Polynomial.IrreducibilityTest(t);
//		result = GF3BinPolynomial.benOrTest(t);
//		start = System.currentTimeMillis();
//		for (int n = 3; n < 200; n++) {				
//			for (int k = 1; k < n; k++) {
//				t = new int[]{-1, k, n};
//				if (Polynomial.IrreducibilityTest(t)) {
//					System.out.println("x^" + n + " + x^" + k + " - 1");
//				}
//				
//				t = new int[]{1, -k, n};
//				if (Polynomial.IrreducibilityTest(t)) {
//					System.out.println("x^" + n + " - x^" + k + " + 1");
//				}
//
//				t = new int[]{-1, -k, n};
//				if (Polynomial.IrreducibilityTest(t))
//				{
//					System.out.println("x^" + n + " - x^" + k + " - 1");
//				}
//			}
//		}
//		end = System.currentTimeMillis();
//		System.out.println("Polynomial irreducibility test: " + (end - start));
//		start = System.currentTimeMillis();
//		//for (int n = 3; n < 1000; n++) {				
//			for (int k = 1; k < 200; k++) {
//				t = new int[]{-1, k, 200};
//				if(GF3BinPolynomial.benOrTest(t)) {
//					System.out.println("x^2000 + x^" + k + " - 1");				
//				}
//				
//				t = new int[]{1, -k, 200};
//				if(GF3BinPolynomial.benOrTest(t)) {
//					System.out.println("x^2000 - x^" + k + " + 1");				
//				}
//
//				t = new int[]{-1, -k, 200};
//				if(GF3BinPolynomial.benOrTest(t)) {
//					System.out.println("x^2000 - x^" + k + " - 1");				
//				}
//			}
//			//System.out.println("working deg: " + n);
//		//}
//		end = System.currentTimeMillis();
//		System.out.println("Polynomial irreducibility test: " + (end - start));
//			System.out.println("success!!!!!!!!!");
//		GF3BinPoly.init(0);
		a = GF3BinPoly.randomPolynomial(n);
		f = new GF3Poly(a);
		GF3n K = new GF3n(t);
		try {
			// creation
			GF3nElement alpha = new GF3nElement(f, K); 
			start = System.currentTimeMillis();
			for (int i = 0; i < iter; i++) {				
				alpha = new GF3nElement(f, K);
			}			
			end = System.currentTimeMillis();
			System.out.println("create: " + (float)((float)(end - start)/100));			
			System.out.println(alpha.toString());
				
			// inversion
			start = System.currentTimeMillis();
			for (int i = 0; i < iter; i++) {				
				alpha.invert();
			}			
			end = System.currentTimeMillis();
			System.out.println("invert: " + (float)((float)(end - start)/100));			
			GF3nElement beta = alpha.invert();
			System.out.println(beta.toString());

			// multiplication
			start = System.currentTimeMillis();
			for(int i = 0; i < iter; i++) {
				alpha.multiply(beta);
			}			
			end = System.currentTimeMillis();
			System.out.println("multiply: " + (float)((float)(end - start)/100));
			System.out.println(alpha.multiply(beta).toString());
			
			// sum
			start = System.currentTimeMillis();
			for(int i = 0; i < iter; i++) {
				alpha.sum(beta);
			}
			end = System.currentTimeMillis();
			System.out.println("sum: " + (float)((float)(end - start)/100));
			System.out.println(alpha.sum(beta).toString());
			
			// cube
			start = System.currentTimeMillis();
			for(int i = 0; i < iter; i++) {
				alpha.cube();
			}
			end = System.currentTimeMillis();
			System.out.println("cube: " + (float)((float)(end - start)/100));
			System.out.println(alpha.pow(3).toString());
		}
		
		catch(NotInFiniteFieldException e){System.out.println("vali�");}
		catch(NoCommonFiniteFieldException e1){System.out.println("vali�");}
		
		//
		
		try {
			HEC3n E = new HEC3n(K.one().negate());
			HEC3nPoint P = new HEC3nPoint(K.one(), K.one().negate(), E);
			P.NAFAdd(5);
			System.out.println(P.toString());
		} catch (NotInFiniteFieldException e) {
			e.printStackTrace();
		}
	}
	
	public static BigInteger changeBigInt(BigInteger a) {
		a = BigInteger.ZERO;
		return a;
	}
	
	public static int[] returnArray (int[] a) {
		return a;
	}
	
	public static final byte[] intToByteArray(int[] value) {
		int l = value.length * 4; 
		byte[] a = new byte[l];
		for (int i = 0; i < value.length; i++) {
			a[l - (4 * i + 1)] = (byte)value[i];
			a[l - (4 * i + 2)] = (byte)(value[i] >>> 8);
			a[l - (4 * i + 3)] = (byte)(value[i] >>> 16);
	        a[l - (4 * (i + 1))] = (byte)(value[i] >>> 24);
	    }
		return a;
	}
}