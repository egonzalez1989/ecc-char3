package crypto.ecc.finitefields;

@SuppressWarnings("serial")
public class NoCommonFiniteFieldException extends RuntimeException{

    public String getErrorString(){
	return "NoCommonFiniteField";
    }
}