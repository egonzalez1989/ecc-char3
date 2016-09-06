package math.algebra.exceptions;

public class DivideByZeroException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String message = "";
	
	public DivideByZeroException() {
	}
	
	public DivideByZeroException(String message) {
		this.message = message;
	}
	
	public String getErrorString(){
		return this.message;
    }

}
