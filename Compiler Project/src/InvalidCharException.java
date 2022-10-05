public class InvalidCharException extends Exception { 
    public InvalidCharException(String buf,int line) {
    	super("Error: "+buf+" is not a valid character on line "+line);
        System.out.println("Error: "+buf+" is not a valid character on line "+line);
    }
}