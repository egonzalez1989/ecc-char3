package math.algebra.ring;

public abstract class RingElementImpl<T extends RingElementImpl<T>> implements RingElement<T> {
	/*
	 * (non-Javadoc)
	 * @see math.algebra.RingElement#pow(int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T pow(int k) {
		if (this.isOne() || this.isZero())
			return (T)this;
		T square = (T)this;
		T p = one();
		while (k > 0) {
			if ((k & 1) == 1)
				p = p.multiply(square);
			square = square.multiply(square);
			k = k >>> 1;
		}
		return p;		
	}
	
	@Override
	public T sum(T... g) {
		T s = g[0];
		for (int i = 1; i < g.length; i++) {
			s = s.sum(g[i]);
		}
		return s;
	}
	
	@Override
	public T multiply(T... g) {
		T p = g[0];
		for (int i = 1; i < g.length; i++) {
			p = p.multiply(g[i]);
		}
		return p;
	}
}
