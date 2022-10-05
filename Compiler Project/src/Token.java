public class Token
{
  public String buffy;
  public int tokenType;
  public int value;//address in symbol table or numerical value
  public int lineNumber;
  
  public Token()
  {
	buffy = "";
    tokenType=0;
    value=-1;
    lineNumber=1;
    
  }//end default constructor
  
  public Token(String buffy, int tokenType, int value, int lineNumber)
  {
	this.buffy=buffy;
    this.tokenType=tokenType;
    this.value=value;
    this.lineNumber=lineNumber;
  }//end two arg constructor
  
}//end class