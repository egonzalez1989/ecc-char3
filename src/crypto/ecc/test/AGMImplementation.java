package crypto.ecc.test;

import math.algebra.polynomial.ThreeAdicExtensionPolynomial;
import math.algebra.polynomial.ThreeAdicPolynomial;
import math.algebra.ring.ThreeAdicInt;
import math.algebra.ring.ThreeAdicIntExtensionElement;

public class AGMImplementation {
	public static final ThreeAdicPolynomial irr = new ThreeAdicPolynomial(new ThreeAdicInt[]{
			new ThreeAdicInt(2),
			new ThreeAdicInt(0),
			new ThreeAdicInt(1),
			new ThreeAdicInt(0),
			new ThreeAdicInt(1)});
	
	// Polynomial (z+6)
	private static final ThreeAdicExtensionPolynomial polA;
	//Polynomial (z^2+3z+9)
	private static final ThreeAdicExtensionPolynomial polB;
	//Polynomial (2z+3)
	private static final ThreeAdicExtensionPolynomial polC;	
	

	private static final ThreeAdicIntExtensionElement t = new ThreeAdicIntExtensionElement(
			new ThreeAdicPolynomial(new ThreeAdicInt[]{new ThreeAdicInt(3)}), irr);
	
	static{
		// Coefficients for polynomial z+6
				ThreeAdicIntExtensionElement a0 = new ThreeAdicIntExtensionElement(
						new ThreeAdicPolynomial(new ThreeAdicInt[]{new ThreeAdicInt(6)}), irr);
				ThreeAdicIntExtensionElement a1 = new ThreeAdicIntExtensionElement(
						new ThreeAdicPolynomial(new ThreeAdicInt[]{new ThreeAdicInt(1)}), irr);
				
				//Coefficients for polynomial z^2+3z+9
				ThreeAdicIntExtensionElement b0 = new ThreeAdicIntExtensionElement(
						new ThreeAdicPolynomial(new ThreeAdicInt[]{new ThreeAdicInt(9)}), irr);
				ThreeAdicIntExtensionElement b1 = new ThreeAdicIntExtensionElement(
						new ThreeAdicPolynomial(new ThreeAdicInt[]{new ThreeAdicInt(3)}), irr);
				ThreeAdicIntExtensionElement b2 = new ThreeAdicIntExtensionElement(
						new ThreeAdicPolynomial(new ThreeAdicInt[]{new ThreeAdicInt(1)}), irr);
				
				//Coefficients for polynomial 2z+3
				ThreeAdicIntExtensionElement c0 = new ThreeAdicIntExtensionElement(
						new ThreeAdicPolynomial(new ThreeAdicInt[]{new ThreeAdicInt(3)}), irr);
				ThreeAdicIntExtensionElement c1 = new ThreeAdicIntExtensionElement(
						new ThreeAdicPolynomial(new ThreeAdicInt[]{new ThreeAdicInt(2)}), irr);
				
				polA = new ThreeAdicExtensionPolynomial(new ThreeAdicIntExtensionElement[]{a0, a1});
				polB = new ThreeAdicExtensionPolynomial(new ThreeAdicIntExtensionElement[]{b0, b1, b2});
				polC = new ThreeAdicExtensionPolynomial(new ThreeAdicIntExtensionElement[]{c0, c1});
	}
	
	public static ThreeAdicIntExtensionElement newtonSolve(ThreeAdicIntExtensionElement x) {//, ThreeAdicExtensionPolynomial f, ThreeAdicExtensionPolynomial df) {
		 // D_(i+1)=D_i-(f(D_i)/f'(D_i))
		// f(z) = polA(z)^3-polB(z)*D_i^3
		// f'(z) = 3*polA(z)^2-polC(z)*D_i^3
		

		ThreeAdicIntExtensionElement u;
		ThreeAdicIntExtensionElement u2;
		ThreeAdicIntExtensionElement u3;
		ThreeAdicIntExtensionElement r;
		ThreeAdicIntExtensionElement s;
		ThreeAdicIntExtensionElement x3;
		int i = 0;
		System.out.println("D_" + i + " = " + x);
		do {
			i++;
			x3 = x.pow(3);
			u = polA.evaluate(x);
			u2 = u.multiply(u);
			
			// s=f'(D_i)=3*(D_i+6)^2-(2z+3z)D_i^3
			s = t.multiply(u2);
			s = s.sum(polC.evaluate(x).multiply(x3).negate());
			
			// r=f(D_i)=(D_i+6)^3-(z^2+3z+9)D_i^3
			u3 = u2.multiply(u);
			r = u3.sum(polB.evaluate(x).multiply(x3).negate());
			
			// D_i+1=D_i-f(D_i)/f'(D_i)
			x = x.sum(r.multiply(s.invert()).negate());
			System.out.println("D_" + i + " = " + x);
		} while(true);
//		return x;
	}
}
