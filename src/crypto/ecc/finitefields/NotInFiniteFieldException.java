package crypto.ecc.finitefields;

@SuppressWarnings("serial")
public class NotInFiniteFieldException extends RuntimeException {

    public String getErrorString(){
		return "NotInFiniteField";
    }
}
