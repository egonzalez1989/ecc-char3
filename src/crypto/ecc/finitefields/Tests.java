package crypto.ecc.finitefields;

import math.algebra.ring.ThreeAdicInt;

public class Tests {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long end, start;
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
//
//		/********************************** Polynomial initialization ********************************/
//
//		System.out.println("Initializing Polynomial");
//		int[][] a = new int[2][t1];
//		int[][] b = new int[][] {new int[] {0xffffffff}, new int[1]};
//		for (int i = 0; i < a[1].length; i++) {
////			if (i % 2 == 0) {
//				a[1][i] = 0xffffffff;
//				a[0][i] = 0x00000000;
////			}
////			else {
////				a[0][i] = 0xffffffff;
////				a[1][i] = 0x00000000;
////			}
//		}
//		
//		f = new GF3n(a);
//		
//		/*
//		 * start = System.currentTimeMillis(); for (int i = 0; i < t; i++) {
//		 * F[i] = new F3Polynomial(new int[] {0xffffffff, 0xffffffff,
//		 * 0xffffffff, 0xffffffff}, new int[] {0, 0, 0, 0}); } end =
//		 * System.currentTimeMillis(); System.out.println("Time: " + (end -
//		 * start));
//		 */
//
//		g = new GF3n(f);
//		System.out.println("f: " + f.toString());
//		start = System.currentTimeMillis();
//		for (int i = 0; i < t; i++) {
//			g = new GF3n(f);
//		}
//		end = System.currentTimeMillis();
//		System.out.println("GF3Polynomial() init time: " + (end - start));
//		
//		//
//		
//		System.out.println("f: " + f.toString());
//		start = System.currentTimeMillis();
//		for (int i = 0; i < t; i++) {
//			f = new GF3n(a);
//		}
//		end = System.currentTimeMillis();
//		System.out.println("GF3Polynomial(,) init time: " + (end - start));
//		
//		//
//
//		int c[] = new int[256];
//		int d[] = new int[32];
//		for (int i = 0; i < 256; i++) {
//			c[i] = 1;
//		}
//		for (int i = 0; i < 32; i++) {
//			d[i] = 0;
//		}
//		start = System.currentTimeMillis();
//		for (int i = 0; i < t; i++) {
//			P = new Polynomial(c);
//		}
//		end = System.currentTimeMillis();
//		System.out.println("Polynomial init time: " + (end - start));
//
//		/*********************************************** POLYNOMIAL SUM *************************************************/
//
//		/*
//		 * G = new F3Polynomial(new int[] {0xffffffff, 0xffffffff, 0xffffffff,
//		 * 0xffffffff}, new int[] {0, 0, 0, 0}); start =
//		 * System.currentTimeMillis(); for (int i = 0; i < t; i++) {
//		 * F[i].sum(G); } end = System.currentTimeMillis();
//		 * System.out.println("Time: " + (end - start));
//		 * System.out.println(F[0].toString());
//		 */
//
//		//
//		f = new GF3n(a);
//		g = new GF3n(a);
//		start = System.currentTimeMillis();
//		for (int i = 0; i < t; i++) {
//			f.sum(g);
//		}
//		end = System.currentTimeMillis();
//		System.out.println("GF3Polynomial sum time: " + (end - start));
//		System.out.println(f.sum(g).toString());
//
//		//
//
//		P = new Polynomial(c);
//		Q = new Polynomial(c);
//		Polynomial R = Polynomial.Sum(P, Q);
//		start = System.currentTimeMillis();
//		for (int i = 0; i < t; i++) {
//			Polynomial.Sum(P, Q);
//		}
//		end = System.currentTimeMillis();
//		System.out.println("Polynomial sum time: " + (end - start));
//		System.out.println(Arrays.toString(R.AllCoefficients()));
//
//		/***************************************** MULTIPLICATION *********************************************/
//
//		/*
//		 * G = new F3Polynomial(new int[] {0xffffffff, 0xffffffff, 0xffffffff,
//		 * 0xffffffff}, new int[] {0, 0, 0, 0}); start =
//		 * System.currentTimeMillis(); for (int i = 0; i < t; i++) {
//		 * F[i].multiply(G); } end = System.currentTimeMillis();
//		 * System.out.println("Time: " + (end - start));
//		 * System.out.println(F[0].toString());
//		 */
//
//		//
//		t = 1;
//		f = new GF3n(a);
//		g = new GF3n(a);
//		//t = 100000;
//		start = System.currentTimeMillis();
//		for (int i = 0; i < t; i++) {
//			f.multiply(g);
//		}
//		end = System.currentTimeMillis();
//		System.out.println("GF3Polynomial mult time: " + (end - start));
//		System.out.println(f.multiply(g).toString());
//		
//		//
//
//		P = new Polynomial(c);
//		Q = new Polynomial(c);
//		R = Polynomial.Multiplication(P, Q);
//		start = System.currentTimeMillis();
//		for (int i = 0; i < t; i++) {
//			Polynomial.Multiplication(P, Q);
//		}
//		end = System.currentTimeMillis();
//		System.out.println("Polynomial mult time: " + (end - start));
//		System.out.println(Arrays.toString(R.AllCoefficients()));
//		
//		/***************************************** DIVISION ********************************************************************/
//		
//		c = new int[512];
//		d = new int[256];
//		for (int i = 0; i < 512; i++) {
//			c[i] = 1;
//			if (i < 256) {
//				d[i] = 1;
//				c[i] = 0;
//			}
//		}
//		P = new Polynomial(c);
//		Q = new Polynomial(d);
//		Polynomial[] QR = Polynomial.QuotientAndRemainder(P, Q);
//		start = System.currentTimeMillis();
//		for (int i = 0; i < 1; i++) {
//			QR = Polynomial.QuotientAndRemainder(P, Q);
//		}
//		end = System.currentTimeMillis();
//		System.out.println("Polynomial Division: " +  (end - start));
//		System.out.println("Q= " + Arrays.toString(QR[0].AllCoefficients()) + "\nR =" + Arrays.toString(QR[1].AllCoefficients()));
//		
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
		int t[] = {-1, 24, 239};
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
//		int[][] a = BinaryHelper.randomPolynomial(180);
//		GF3Poly f = new GF3Poly(a);
//		GF3n K = new GF3n(t);
//		try {
//			for (int i = 0; i < 10; i++) {
//				start = System.nanoTime();
//				GF3nElement alpha = new GF3nElement(f, K);
//				end = System.nanoTime();				
//				System.out.println("create: " + (float)((end - start)/1000));
////				System.out.println(alpha.toString());
//				start = System.nanoTime();
//				alpha.invert();
//				end = System.nanoTime();
//				System.out.println("invert: " + (float)((end - start)/1000));
//				GF3nElement beta = alpha.invert();
////				System.out.println(beta.toString());
//				start = System.nanoTime();
//				alpha.multiply(beta);
//				end = System.nanoTime();
//				System.out.println("multiply: " + (float)((end - start)/1000));
////				System.out.println(prod.toString());
//				start = System.nanoTime();
//				alpha.sum(beta);
//				end = System.nanoTime();
//				System.out.println("sum: " + (float)((end - start)/1000));
//			}
//			
//		}
//		catch(NotInFiniteFieldException e){System.out.println("valió");}
//		catch(NoCommonFiniteFieldException e1){System.out.println("valió");}
		int[][] a = new int[][]{new int[]{0x7}, new int[]{0}};
		int[][] b = new int[][]{new int[]{0}, new int[]{0x2}};
		ThreeAdicInt alpha = new ThreeAdicInt(a);
		ThreeAdicInt beta = new ThreeAdicInt(b);
		ThreeAdicInt delta = alpha.multiply(beta);
	}
}