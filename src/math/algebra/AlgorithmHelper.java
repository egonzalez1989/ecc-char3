package math.algebra;

import java.util.ArrayList;

public class AlgorithmHelper {
	/**
	 * Non-adjacent form of an integer (see A guide to elliptic curve cryptography, Menezes)
	 * @param k
	 * @return
	 */
	public static ArrayList<Byte> naf(int k) {
		ArrayList<Byte> nafArray = new ArrayList<>();
		byte ki;
		while (k>= 1) {
			ki = (byte)(2 - k % 4);
			if (ki == 1 || ki == -1) {
				nafArray.add(ki);
				k = k - ki;
			}
			else
				nafArray.add((byte)0);
			k = k/2;
		}
		return nafArray;
	}
	
	public static ArrayList<Integer> ternaryRepresentation(int n) {
		if (n < 0)
			return new ArrayList<Integer>();
		// Express k as a sum of powers of 3 with coefficients in {0, 1, 2}
		// list of coefficient of the ternary representation of k
		ArrayList<Integer> c = new ArrayList<Integer>();
		while(n > 0) {
			c.add(n & 0X03);
			n = n / 3;
		}
		c.add(n);
		return c;
	}
	
	public static ArrayList<Integer> binaryRepresentation(int n) {
		if (n < 0)
			return new ArrayList<Integer>();
		// Express k as a sum of powers of 3 with coefficients in {0, 1, 2}
		// list of coefficient of the ternary representation of k
		ArrayList<Integer> c = new ArrayList<Integer>();
		while(n > 0) {
			c.add(n & 1);
			n = n >>> 1;
		}
		c.add(n);
		return c;
	}
}
