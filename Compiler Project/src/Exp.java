
public class Exp
{
	int type; // the data type of an expression
	int value; // symbol table address or numerical value
	boolean number; //Is the expression a number such as 246
	boolean bool;


public Exp() {
	type = -1;
	value = -1;
	number = false; 
	bool = false;
}


}