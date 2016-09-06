package crypto.ecc;

@SuppressWarnings("serial")
public class NoCommonECException extends Exception {
	public String getErrorString(){
		return "NoCommonEllipticCurve";
	}
}
