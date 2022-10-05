public class StringNotTerminatedException extends Exception { 
    public StringNotTerminatedException(String buf,int line) {
    	super("Error: "+buf+" was not terminated on line "+line);
        System.out.println("Error: "+buf+" was not terminated on line "+line);
    }
}