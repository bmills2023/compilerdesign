import java.io.*;
import java.util.*;
public class Scan
{
  private int[][] fsm;
  private String[] reserved={"program", "var", "integer", "bool", "procedure", "call", "begin", "end", "if", "then", "else","while", "do", "and",
    "or", "not", "read", "write", "writeln"};
  File f;
  FileReader fileReader;
  BufferedReader br;
  PrintWriter pw;
  int line=1;
  int state=0;
  char ch=' ';
  int tempNum;
  SymbolTable st = new SymbolTable();
  StringTable strt = new StringTable();
  
  
  public Scan(String filename, int num)throws IOException
  {
	tempNum = 0;
    f=new File(filename);
    fileReader=new FileReader(f);
    br = new BufferedReader(fileReader);
    if(num==1) {
    	pw = new PrintWriter("output.txt");
    }
    fsm=new int[15][11];
    Scanner input=new Scanner(new File("FSM.txt"));
    for(int row=0; row<15; ++row)
    {
      for(int col=0; col<11;++col)
      {
        fsm[row][col]=input.nextInt();
      }//end inner for loop
    }//end for loop
    
  }//end constructor
  
  public int removeSpaces()throws IOException
  {
   int num=0;
    while(ch==' '||ch=='\n'||ch=='\r' ||ch=='\t' ||ch=='!')
    {
      if(ch=='!'){
        while(ch!='\n'){
          //System.out.println("Inside comment");
         ch=(char)br.read(); 
        }
      }
      
      if(ch=='\n') {//checks if going to next line...if so, change return to 1 so it increase line number variable
      num=1;
      }
      
      ch=(char)br.read();
    }//end while loop
    return num;
  }//end method
  
  
  
  public int getCharClass(char ch)//gives proper column in fsm
  {
    if(ch>='A'&&ch<='Z'||ch>='a'&&ch<='z')
      return 0;
    if(ch>='0'&&ch<='9')
      return 1;
    else
      return 2;
  }//end method
  
  public Token nextToken(int sco) throws Exception
  {
	  
    boolean blank=false;
    line+=removeSpaces();
    int state=0;
    int inchar=getCharClass(ch);
    String buf="";
    int strNum=-1;
    //System.out.println("InChar: "+inchar);
    if(inchar==2)
    {
      int temp=0;
      buf=buf+ch;
      //System.out.print(buf+"\t");
      ch=(char)br.read();
      if(buf.equals("<")){
        if(ch=='='){
          temp=TokenInterface.LE;
          buf=buf+ch;
          ch=(char)br.read();
        }
        else if(ch=='>'){
          temp=TokenInterface.NE;
          buf=buf+ch;
          ch=(char)br.read();
        }
        else
          temp=TokenInterface.LT;
      }
      else if(buf.equals(">"))
      {
        if(ch=='='){
          temp=TokenInterface.GE;
          buf=buf+ch;
          ch=(char)br.read();
        }
        else
          temp=TokenInterface.GT;
      }
      else if(buf.equals("+"))
        temp=TokenInterface.PLUS;
      else if(buf.equals("-"))
        temp=TokenInterface.MINUS;
      else if(buf.equals("/"))
        temp=TokenInterface.DIV;
      else if(buf.equals("*"))
        temp=TokenInterface.TIMES;
      else if(buf.equals("%"))
        temp=TokenInterface.MOD;
      else if(buf.equals(","))
        temp=TokenInterface.COMMA;
      else if(buf.equals(";"))
        temp=TokenInterface.SEMI;
      else if(buf.equals("("))
        temp=TokenInterface.LPAREN;
      else if(buf.equals(")"))
        temp=TokenInterface.RPAREN;
      else if(buf.equals("\'")){
        temp=TokenInterface.STRING;
        while(ch!='\''){
          //System.out.println(ch);
          buf=buf+ch;
          ch=(char)br.read();
          if(ch=='\n'){
           throw new StringNotTerminatedException(buf,line); 
          }
        }
        buf=buf+ch;
        buf=buf.substring(1, buf.length()-1);
        
        
        Boolean bool=false;
        for(int z=0;z<strt.stringtable.length;z++){
      	  if(strt.stringtable[z].str.equals(buf)) {
      		  bool=true;
      		  strNum=z;
      		  break;
      	  }
        }
        if(!bool) {
        strNum = strt.insert(buf);
        
        }
        
        
        //strNum = strt.insert(buf);
        ch=(char)br.read();
        //return new Token(TokenInterface.STRING,num);
      }
      else if(buf.equals("."))
        temp=TokenInterface.PERIOD;
      else if(buf.equals(":")){
        if(ch=='='){
          temp=TokenInterface.ASSIGN;
          buf=buf+ch;
          ch=(char)br.read();
        }
        else
          temp=TokenInterface.COLON;
      }
      else if(buf.equals("="))
        temp=TokenInterface.EQUAL;
      else if(buf.equals("\t"))
        blank=true;
      else if(buf.equals("\n"))
        blank=true;
      else if(buf.equals("!"))
        blank=true;
      else{
        throw new InvalidCharException(buf,line);
      }
      System.out.print(buf+"\t");
      Token t = new Token(buf, temp, strNum, line);
      return t;
    }//end if inchar is not a letter or number
    else{
      while(fsm[state][inchar]>0)
      {
        buf=buf+ch;
        state=fsm[state][inchar];
        ch=(char)br.read();
        inchar=getCharClass(ch);
      }//end while loop
      System.out.print(buf+"\t");
      //int temp= finalState(state, buf);
      Token t = finalState(state, buf, sco);//passing in 0 as value right now, since not worried about symbol table yet
      t.lineNumber=line;
      return t;
    }//end else
  }//end method
  
  public Token finalState(int state, String buf, int sco)
  {
    Token t = new Token();
    t.buffy = buf;
    t.tokenType=-1;
    if(state==1){
      for(int i=0; i<reserved.length; ++i){
        if(buf.equals(reserved[i])){
          //System.out.println("buf: "+buf+" res word: "+reserved[i]);
          t.tokenType=i;
          return t;
        }      
      }//end for loop going through reserved words list
      t.tokenType=TokenInterface.IDENTIFIER;
      Boolean bool=false;
      for(int z=0;z<st.symboltable.length;z++){
    	  if((st.symboltable[z].name.equals(buf))&&(st.symboltable[z].scope==sco)) {
    		  bool=true;
    		  t.value=z;
    		  break;
    	  }
      }
      if(bool==false) {
      int num = st.insert(buf,sco);
      t.value=num;
      }
      
    }else if(state==2) {
      t.tokenType=TokenInterface.NUMBER;
      t.value=Integer.parseInt(buf);
    }else {
      t.tokenType=-2;
    }
    return t;
  }//end method
  
  public int getTemp(int sco) {//creates temp and returns address in symbol table
	  tempNum++;
	  return st.insert("@T"+tempNum, sco);
	  
  }
  
  public static void main(String[] args)throws Exception
  {
    Scanner input=new Scanner(System.in);
    System.out.print("Enter file name:");
    String filename=input.nextLine();
    Scan s = new Scan(filename,1);
    //s.pw=new PrintWriter("output.txt");
    Token t= new Token();
    String error="";
    //String t="";
//    while(s.br.ready())
//    {
//      t=s.nextToken();
//      System.out.println(t);
//    }//end while loop
    while(t.tokenType!=TokenInterface.PERIOD)
    {
     try {
      t=s.nextToken(0);
     }catch(Exception e) {
      error = e.getMessage();
      break;
     }
     //System.out.println();
     String tempy = "";
     if(t.value>=0)
    	 tempy="    "+t.value;
     System.out.println(t.tokenType+" "+tempy);
     //if(t.value!=0)
     //System.out.print(t.value);
    }
    s.st.printSymbolTable();
    s.strt.printStringTable();
    //end while loop
    //s.pw.write(s.outputText);
    //createOutputFile();
    Scanner inputFromFile=new Scanner(new File(filename));
    int lineNumber=1;
    while(inputFromFile.hasNextLine()){
      s.pw.write(lineNumber+"  ");
      s.pw.write(inputFromFile.nextLine());
      s.pw.write("\n");
      lineNumber++;
    }
    //System.out.println("Error: "+error);
    s.pw.write(error);
    s.pw.close();
    
   
    
    
  }//end main
  
  
  
}//end class