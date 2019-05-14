package pl.polsl.ismoil.atajanov.model.base64exceptions;


/**
 * Exception that indicates any issues
 * with the string that we are trying to decode
 * Such us white spaces inside the string or illegal characters
 * @author Ismoil Atajanov
 * @version 1.0
 */
public class EncodedStringInvalidException extends Exception{
    /**
     * Default constructor of the class
     * Passing default error message as an arg
     */
    public EncodedStringInvalidException (){
        super("Invalid String");

    }
    /**
     * Passing a unique message as an argument to the super constructor
     * @param msg error message
     */
    public EncodedStringInvalidException(String msg){
        super(msg);
    }

}
