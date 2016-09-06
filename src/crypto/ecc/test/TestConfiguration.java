package crypto.ecc.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import crypto.ecc.finitefields.GF3Poly;
import crypto.ecc.finitefields.GF3n;
import crypto.ecc.finitefields.GF3nElement;
import crypto.ecc.finitefields.NotInFiniteFieldException;
import math.algebra.polynomial.PolynomialImpl;
import math.algebra.polynomial.ThreeAdicExtensionPolynomial;
import math.algebra.polynomial.ThreeAdicPolynomial;
import math.algebra.ring.ThreeAdicInt;
import math.algebra.ring.ThreeAdicIntExtensionElement;
public class TestConfiguration {
	
	public static final int WORD_SIZE;
	public static final int WORD_SIZE_EXPONENT;
	public static final int EXTENSION_DEGREE;
	
	static {
		Properties prop = new Properties();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();           
		InputStream stream = loader.getResourceAsStream("testconfig.properties");
		try {
			prop.load(stream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		EXTENSION_DEGREE = Integer.parseInt(prop.getProperty("extensiondegree"));
		WORD_SIZE = Integer.parseInt(prop.getProperty("wordsize"));
		WORD_SIZE_EXPONENT = Integer.parseInt(prop.getProperty("wordsizeexponent"));
	}
	
	public static void main(String[] args) throws NotInFiniteFieldException {
//		long end, start;
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
//		int t[] = {-1, 24, 239};
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
//		catch(NotInFiniteFieldException e){System.out.println("vali�");}
//		catch(NoCommonFiniteFieldException e1){System.out.println("vali�");}
		
//		System.out.println(intToBinString(-3));
		
//		ThreeAdicInt x = new ThreeAdicInt(31);
//		System.out.println(x);
//		System.out.println(x.pow(3));
//		
//		ThreeAdicPolynomial irr = new ThreeAdicPolynomial(new ThreeAdicInt[]{
//				new ThreeAdicInt(2),
//				new ThreeAdicInt(0),
//				new ThreeAdicInt(1),
//				new ThreeAdicInt(0),
//				new ThreeAdicInt(1)});
//		
//		ThreeAdicPolynomial c = new ThreeAdicPolynomial(new ThreeAdicInt[]{
//				new ThreeAdicInt(1),
//				new ThreeAdicInt(1),
//				new ThreeAdicInt(0),
//				new ThreeAdicInt(1)
//		});
//		
//		ThreeAdicIntExtensionElement C = new ThreeAdicIntExtensionElement(c, irr);
//		System.out.println(C);
//		System.out.println(C.pow(3));
		
//		int[][] b = new int[][]{new int[]{0}, new int[]{3}};
//		int[][] a = new int[][]{new int[]{3}, new int[]{0}};
//		ThreeAdicInt alpha = new ThreeAdicInt(a);
//		System.out.println("alpha = " + alpha.toString());
//		ThreeAdicInt beta = new ThreeAdicInt(b);
//		System.out.println("beta = " + beta.toString());
//		ThreeAdicInt delta = alpha.sum(beta);
//		System.out.println("Sum = " + delta.toString());
//		delta = alpha.negate();
//		System.out.println("negative = " + delta.toString());
//		delta = alpha.divide(beta);
//		System.out.println("Division = " + delta.toString());
//		delta = alpha.multiply(beta);
//		System.out.println("Multiply = " + delta.toString());
//		delta = alpha.invert();
//		System.out.println("Inverse = " + delta.toString());
//		delta = alpha.multiply(delta);
//		System.out.println("Multiply inverse = " + delta.toString());
		
		
		
		int[][] k = new int[][]{new int[]{1}, new int[]{20}};
		int[][] a = new int[][]{new int[]{0}, new int[]{11}};
		GF3Poly kPol = new GF3Poly(k);
		GF3Poly aPol = new GF3Poly(a);
		GF3n kField = new GF3n(kPol);
		GF3nElement alpha = new GF3nElement(aPol, kField);
			
		
		ThreeAdicInt s0 = new ThreeAdicInt(186);
		ThreeAdicInt s1 = new ThreeAdicInt(3);
		ThreeAdicInt s2 = new ThreeAdicInt(28);
		ThreeAdicInt s3 = new ThreeAdicInt(47);
		ThreeAdicPolynomial sPol = new ThreeAdicPolynomial(new ThreeAdicInt[]{s0,s1,s2,s3});
		ThreeAdicIntExtensionElement elm = new ThreeAdicIntExtensionElement(sPol, AGMImplementation.irr);
		ThreeAdicIntExtensionElement inv = elm.invert();
		System.out.println(elm.multiply(inv));
		
//		ThreeAdicPolynomial[] ee = sPol.extendedEuclid(AGMImplementation.irr);
//		System.out.println("result = " + ee[0]);
//		System.out.println("result = " + sPol.multiply(ee[0]).reduce(AGMImplementation.irr));//.sum(AGMImplementation.irr.multiply(ee[1])).reduce(AGMImplementation.irr));
		
//		ThreeAdicIntExtensionElement s = new ThreeAdicIntExtensionElement(sPol, AGMImplementation.irr);
//		System.out.println("s = " + s);
//		System.out.println("irr = " + AGMImplementation.irr);
//		ThreeAdicPolynomial[] qr = AGMImplementation.irr.divide(sPol);
//		System.out.println("q = " + qr[0]);
//		System.out.println("r = " + qr[1]);
//		System.out.println("irr = " + sPol.multiply(qr[0]).sum(qr[1]));
//		System.err.println("s*s^{-1} = " + s.multiply(s.invert()));
		
		// Element D_0 = c^3+c+1 \ in R
		ThreeAdicIntExtensionElement D0 = new ThreeAdicIntExtensionElement(new ThreeAdicPolynomial(
				new ThreeAdicInt[]{
				new ThreeAdicInt(1),
				new ThreeAdicInt(1),
				new ThreeAdicInt(0),
				new ThreeAdicInt(1)}), AGMImplementation.irr);
		
		System.out.println(D0);
		System.out.println(D0.multiply(D0));
		System.out.println(D0.multiply(D0).multiply(D0));
		AGMImplementation.newtonSolve(D0.pow(3));
		
//		new ThreeAdicPolynomial(new ThreeAdicInt[]{ThreeAdicInt(3), });
		
//		System.out.println("" + (1 << 1));
	}
	
	public static String intToBinString(int n) {
		String bits = "";
		for (int i = 0; i < 32; i++) {
			bits = ((n & (1 << i)) >>> i) + bits;
		}
		return bits;
	}
}
