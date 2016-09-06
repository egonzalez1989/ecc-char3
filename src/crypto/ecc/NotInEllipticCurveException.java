package crypto.ecc;

@SuppressWarnings("serial")
public class NotInEllipticCurveException extends Exception {
	public String getErrorString(){
		return "NoInEllipticCurve";
	}
}
