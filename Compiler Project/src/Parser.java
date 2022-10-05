//RUN THIS FILE TO RUN EVERYTHING...COMPILES AND RUNS TXT FILES IN THE LANGUAGE MADE UP BY PROFESSOR RALPH BRAVACO 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Parser {
	
	Token symbol;
	Scan s;
	Scan s2;//use this for when parsing shit
	PrintWriter pw2;
	Quads quads;
	int sco = 0;
	int[] locals = new int[20];
	
	
	
	public Parser() throws Exception {
		
		
		
		//SCANNER STUFF
		Scanner input=new Scanner(System.in);
	    System.out.print("Enter file name:");
	    String filename=input.nextLine();
		
		s = new Scan(filename,1);
		
		Token t= new Token();
	    String error="";
	    
	    while(t.tokenType!=TokenInterface.PERIOD) {	    
	     try {
	      t=s.nextToken(sco);
	     }catch(Exception e) {
	      error = e.getMessage();
	      break;
	     }
	     String tempy = "";
	     if(t.value>=0)
	    	 tempy="    "+t.value;
	     System.out.println(t.tokenType+" "+tempy);
	     
	    }
	    //s.st.printSymbolTable();
	    //s.strt.printStringTable();
	    
	    Scanner inputFromFile=new Scanner(new File(filename));
	    int lineNumber=1;
	    while(inputFromFile.hasNextLine()){
	      s.pw.write(lineNumber+"  ");
	      s.pw.write(inputFromFile.nextLine());
	      s.pw.write("\n");
	      lineNumber++;
	    }
	    
	    s.pw.write(error);
	    s.pw.close();
	    
		
		
	    //PARSER STUFF//DO NOT RUN THE PARSER UNLESS THERE ARE NO SYNTAX ERRORS
	    if(error.equals("")) {//if there are no syntax errors.....
	    	quads = new Quads();
	    	pw2 =new PrintWriter("parsetree.txt");
	    	pw2.write("<--PARSE TREE FOR: "+filename+"-->");
	    	System.out.println();
	    	System.out.println("<--PARSING BEGINS NOW-->");
	    	s2 = new Scan(filename,0);
	    	//basically resets the buffered reader to the beginning of the file so we can parse now
	    	symbol = s2.nextToken(sco);
	    	program();//begin parsing now
	    	System.out.println("SUCCESS!");
	    	pw2.write("\nSUCCESS!");
	    	pw2.close();
	    	s2.st.printSymbolTable();
		    s2.strt.printStringTable();
	    	System.out.println("\n<--Quads-->");
	    	quads.printQuads();
	    	interpret();
	    	
	    }else {
	    	System.out.println("Sorry bud...can't parse this. Fix your syntax first!");
	    }
	}




	public void program() throws Exception {
		
		pw2.write("\nProgram -> program id; variable_declarations subprogram_declarations compound_statement.");
		if(symbol.tokenType!=0) {//program
			error("missing program");
		}
		symbol = s2.nextToken(sco);
		if(symbol.tokenType!=35) {//id
			error("missing or invalid id name");
		}else {
		int i = symbol.value;
		s2.st.symboltable[i].dataType=TokenInterface.PROGRAM;
		if(s2.st.symboltable[i].declared==false) {
			s2.st.symboltable[i].declared=true;
		}else {
			error("ID has already been declared");
		}
			s2.st.symboltable[i].kind=TokenInterface.PROGRAM;
		}
		symbol = s2.nextToken(sco);
		if(symbol.tokenType!=32) {//semi colon
			error("missing semi colon");
		}
		symbol = s2.nextToken(sco);
		
		
		
		
		variable_declarations();
		int loc1 = quads.quadSize;
		quads.insertQuad("BR", "-", "-", "0");
		subprogram_declarations();
		sco = 0;
		int loc2 = quads.quadSize;
		quads.setResult(loc1, loc2);
		compound_statement();
		
		if(symbol.tokenType!=38) {//period
			error("missing period");
		}else {
			quads.insertQuad("END", "-", "-", "-");
		}
		
		
	}

	public void identifier_list(Semantics sem)throws Exception {
		sem.count=0;
		pw2.write("\nidentifier_list -> id {,id}");
		
		if(symbol.tokenType!=35) {//id
			error("missing or invalid id");
		}
		sem.start = symbol.value;
		sem.count = sem.count + 1;
		symbol = s2.nextToken(sco);
		

		
		
		
		while(symbol.tokenType==31) {//comma
			symbol = s2.nextToken(sco);
			if(symbol.tokenType!=35) {
				error("missing or invalid id");
			}	
			sem.count = sem.count + 1;
			symbol = s2.nextToken(sco);
			
		}
	}
	
	public void variable_declarations()throws Exception {//////////unfinished////////////definitely wrong//////this is whack
		pw2.write("\nvariable_declarations -> var variable_declaration ; {variable_declaration;} | ε");
		Semantics sem = new Semantics();
		if(symbol.tokenType==1) {//var
			
			symbol = s2.nextToken(sco);
			
			variable_declaration(sem);
			
			
			
			
			for(int j=sem.start;j<sem.start+sem.count;j++) {
				quads.insertQuad("DCL", "-", "-", j+"");
				s2.st.symboltable[j].dataType=sem.type;
				//s2.st.symboltable[j].dataType=666;
				if(s2.st.symboltable[j].declared==false) {
					s2.st.symboltable[j].declared=true;
					locals[sco]++;
					s2.st.symboltable[j].offset=locals[sco];
				}else {
					error("ID has already been declared");
				}
				s2.st.symboltable[j].kind=TokenInterface.LOCAL;
			}
			
			if(symbol.tokenType!=32) {
				error("missing semi colon");
			}			
			symbol = s2.nextToken(sco);
			
			
			while(symbol.tokenType==35) {//id
				variable_declaration(sem);
				for(int j=sem.start;j<sem.start+sem.count;j++) {
					quads.insertQuad("DCL", "-", "-", j+"");
					s2.st.symboltable[j].dataType=sem.type;
					//s2.st.symboltable[j].dataType=666;
					if(s2.st.symboltable[j].declared==false) {
						s2.st.symboltable[j].declared=true;
						locals[sco]++;
						s2.st.symboltable[j].offset=locals[sco];
					}else {
						error("ID has already been declared");
					}
					s2.st.symboltable[j].kind=TokenInterface.LOCAL;
				}
				if(symbol.tokenType!=32) {
					error("missing semi colon");
				}
				symbol = s2.nextToken(sco);
	
			}
			
			
			
			
			
		}
		
		
		
		
	}
	public void variable_declaration(Semantics sem) throws Exception{
		pw2.write("\nvariable_declaration -> identifier_list : type");
		identifier_list(sem);
		
		if(symbol.tokenType!=39) {//colon
			error("missing colon");
		}
		symbol = s2.nextToken(sco);
		type(sem);
	}
	public void type(Semantics sem)throws Exception {
		pw2.write("\ntype -> integer");
		if(symbol.tokenType!=2) {//integer
			error("missing or invalid integer");
		}
		sem.type = TokenInterface.INTEGER;
		symbol = s2.nextToken(sco);
	}
	public void subprogram_declarations() throws Exception{////////////definitely wrong...not anymore

		pw2.write("\nsubprogram_declarations -> subprogram_declaration ; subprogram_declarations  | ε");
		
		if(symbol.tokenType==4) {//procedure
		
		subprogram_declaration();
		
		if(symbol.tokenType!=32) {//semi colon
			error("missing semi colon");
		}
		symbol = s2.nextToken(sco);
		
		subprogram_declarations();
		
		}//else: go to epsilon
		
	}
	public void subprogram_declaration() throws Exception{
		pw2.write("\nsubprogram_declaration -> subprogram_head variable_declarations compound_statement");
		
		subprogram_head();
		variable_declarations();
		compound_statement();
		quads.insertQuad("EXIT", "-", "-", "-");
		
	}
	public void subprogram_head() throws Exception{
		int place = 0;
		int loc1 = 0;
		pw2.write("\nsubprogram_head -> procedure id arguments ;");
		if(symbol.tokenType!=4) {//procedure
			error("missing procedure");
		}
		symbol = s2.nextToken(sco);
		if(symbol.tokenType!=35) {//id
			error("missing or invalid id");
		}
		
		
		place = symbol.value;
		loc1 = quads.quadSize;
		s2.st.symboltable[place].start=loc1;
		quads.insertQuad("PRODEC", "-", "-", place+"");
		s2.st.symboltable[place].dataType = TokenInterface.PROCEDURE;
		s2.st.symboltable[place].kind = TokenInterface.PROCEDURE;
		//s2.st.symboltable[place].dataType=TokenInterface.PROGRAM;
		if(s2.st.symboltable[place].declared==false) {
			s2.st.symboltable[place].declared=true;
		}else {
			error("ID has already been declared");
		}
		sco = sco + 1;
		symbol = s2.nextToken(sco);
	
		arguments(place);
		//System.out.println();
		//s2.st.printSymbolTable();
		
		if(symbol.tokenType!=32) {//semi colon
			error("missing semi colon");
		}
		symbol = s2.nextToken(sco);
		
	}

	public void arguments(int place) throws Exception{
		pw2.write("\n arguments -> (parameter_list) ");
		if(symbol.tokenType!=33) {//leftparen
			error("missing left paren");
		}
		symbol = s2.nextToken(sco);
		
		parameter_list(place);
		
		if(symbol.tokenType!=34) {//rightparen
			error("missing right paren");
		}
		symbol = s2.nextToken(sco);
	}
	
	public void parameter_list(int place) throws Exception{
		Semantics sem = new Semantics();
		pw2.write("\nparameter_list -> identifier_list : type { ; identifier_list : type} ");
		identifier_list(sem);
		
		if(symbol.tokenType!=39) {//colon
			error("missing colon");
		}
		symbol = s2.nextToken(sco);
		
		type(sem);
		
		
		for(int j=sem.start;j<(sem.start+sem.count);j++) {
			//s2.st.insert(symbol.buffy);//work here
			s2.st.symboltable[place].numArgs++;
			quads.insertQuad("PARAM", "-", "-", j+"");
			s2.st.symboltable[j].dataType = sem.type;
			s2.st.symboltable[j].kind = TokenInterface.PARM;
			s2.st.symboltable[j].offset=s2.st.symboltable[place].numArgs;
			if(s2.st.symboltable[j].declared==false) {
				s2.st.symboltable[j].declared=true;
			}else {
				error("ID has already been declared");
			}
		}
		
		while(symbol.tokenType==32) {//semi colon
			symbol = s2.nextToken(sco);
			identifier_list(sem);
			if(symbol.tokenType!=39) {//colon
				error("missing colon");
			}
			symbol = s2.nextToken(sco);
			type(sem);
			for(int j=sem.start;j<sem.start+sem.count;j++) {
				s2.st.symboltable[place].numArgs++;
				quads.insertQuad("PARAM", "-", "-", j+"");
				s2.st.symboltable[j].dataType = sem.type;
				s2.st.symboltable[j].kind = TokenInterface.PARM;
				s2.st.symboltable[j].offset=s2.st.symboltable[place].numArgs;
				if(s2.st.symboltable[j].declared==false) {
					s2.st.symboltable[j].declared=true;
				}else {
					error("ID has already been declared");
				}
			}
			
		}
		
	}
	
	public void compound_statement()throws Exception {
		pw2.write("\ncompound_statement -> begin statement_list end");
		if(symbol.tokenType!=6) {//begin
			error("missing begin");
		}
		symbol = s2.nextToken(sco);
				
		statement_list();
		
		if(symbol.tokenType!=7) {//end
			error("missing end");
		}
		symbol = s2.nextToken(sco);
	}
	
	public void statement_list() throws Exception{/////UHHHHHHH WHAT?! SEMI COLON ISSUE
		pw2.write("\nstatement_list -> statement  {; statement }");
		statement();
		while(symbol.tokenType==32) {//semi colon
			symbol = s2.nextToken(sco);
			statement();
		}
	}
	public void statement() throws Exception{////////////maybe wrong///how to know which path to take???
		pw2.write("\nstatement -> assignment_statement | procedure_statement | compound_statement | if_statement | while_statement | read_statement | write_statement | ε");
		
		if(symbol.tokenType==32) {
			symbol = s2.nextToken(sco);////adam change
		}
		
		
		if(symbol.tokenType==35) {//id
		assignment_statement();
		
		}else if(symbol.tokenType==5) {//call
		procedure_statement();
		
		}else if(symbol.tokenType==6) {//begin
		compound_statement();
		
		}else if(symbol.tokenType==8) {//if
		if_statement();
		
		}else if(symbol.tokenType==11) {//while
		while_statement();
		
		}else if(symbol.tokenType==16) {//read
		read_statement();
		
		}else if(symbol.tokenType==17) {//write
		write_statement();
		
		}else if(symbol.tokenType==18) {//writeln
		writeln_statement();
		}
	}
	public void assignment_statement() throws Exception{
		pw2.write("\nassignment_statement -> id assignop expression");
		
		if(symbol.tokenType!=35) {//id
			error("missing or invalid id");
		}
		int place = symbol.value;
		//System.out.println("ADMIN PLACE:"+place);
		if(s2.st.symboltable[place].declared==false) {
			error("undeclared id");
		}
		symbol = s2.nextToken(sco);
		
		
		if(symbol.tokenType!=30) {//assign op :=
			error("missing assign op");
		}
		symbol = s2.nextToken(sco);
		Exp exp = new Exp();
		
		expression(exp);
		if(s2.st.symboltable[place].dataType!=exp.type) {
			error("type mismatch in assignment");//left off here
		}else {
			String expString = "";
			if(exp.number==true) {
				expString = "*"+exp.value;
			}else {
				expString = ""+exp.value;
			}
			quads.insertQuad("ASSIGN", expString, "-", place+"");
		}
		
	}
	public void if_statement()throws Exception {//this is wrong so badly wrong
		pw2.write("\nif_statement -> if  expression then statement [else statement]");
		int loc1, loc2, LC;//increment LC every time a quad gets inserted...
		
		
		if(symbol.tokenType!=8) {//if
			error("missing if");
		}
		symbol = s2.nextToken(sco);
		Exp exp = new Exp();
		expression(exp);
		if(exp.type!=TokenInterface.BOOL) {
			error("error");
		}
		loc1 = quads.quadSize;
		quads.insertQuad("BR0", exp.value+"", "-", "0");
		if(symbol.tokenType!=9) {//then
			error("missing then");
		}
		symbol = s2.nextToken(sco);
		statement();
		
		if(symbol.tokenType==32) {
			symbol = s2.nextToken(sco);//adam change
		}
		
		if(symbol.tokenType==10) {//else
			symbol = s2.nextToken(sco);
			loc2 = quads.quadSize;
			quads.insertQuad("BR", "-", "-", "-");
			quads.qs[loc1].result = quads.quadSize+"";
			statement();
			quads.qs[loc2].result = quads.quadSize+"";
		}else {
			quads.qs[loc1].result = quads.quadSize+"";
		}
		
	}
	public void while_statement()throws Exception{
		pw2.write("\nwhile_statement -> while expression do statement");
		int loc1;
		int loc2;
		
		if(symbol.tokenType!=11) {//while
			error("missing while");
		}
		symbol = s2.nextToken(sco);
		loc1 = quads.quadSize;
		Exp exp = new Exp();
		expression(exp);
		if(exp.type!=TokenInterface.BOOL) {
			error("uhhhh some type of error");
		}
		loc2 = quads.quadSize;
		quads.insertQuad("BR0", exp.value+"", "-", "0");
		if(symbol.tokenType!=12) {//do
			error("missing do");
		}
		symbol = s2.nextToken(sco);
		//int quadCntr = statement();
		statement();
		quads.insertQuad("BR", "-", "-", loc1+"");
		quads.qs[loc2].result = quads.quadSize+"";
		//quads.setResult(loc1, loc2);//wrong??
	}
	public void procedure_statement()throws Exception {
		pw2.write("\nprocedure_statement ->	call id (expression_list)");
		int parameterCount = 0;
		int start = 0;
		if(symbol.tokenType!=5) {//call
			error("missing call");
		}
		symbol = s2.nextToken(sco);
		if(symbol.tokenType!=35) {//id
			error("missing or invalid id");
		}
		start = symbol.value; 
		//DEAR BRANDON...THIS IS FUCKING EVERY THING UP!
		System.out.print("ADMIN START:"+start);
		for(int i=0;i<s2.st.symboltable.length;i++) {
			//System.out.print("ADMIN LOOP:"+i+"["+s2.st.symboltable[i].name+"]");
			if(s2.st.symboltable[i].name.equals(s2.st.symboltable[start].name)&&s2.st.symboltable[start].declared==false) {//if the procedure name is already in the symbol table, wipe this second occurence of the name 
				s2.st.symboltable[start] = new SymbolTableObject();//brand new fresh empty symbol table object
//				s2.st.symboltable[start].name="";
//				s2.st.symboltable[start].scope=0;
//				s2.st.symboltable[start].dataType=0;
//				s2.st.symboltable[start].numArgs=0;
//				s2.st.symboltable[start].offset=0;
				//s2.st.symboltable[start].dataType = TokenInterface.CALL;
				
				start = i ; 
				//System.out.println("ADMIN: "+symbol.buffy);
				System.out.println("IM HERE");
				break;
			}
			
		}
		
		symbol = s2.nextToken(sco);
		if(symbol.tokenType!=33) {//left paren
			error("missing left paren");
		}
		symbol = s2.nextToken(sco);
		
		parameterCount = expression_list();
		s2.st.symboltable[start].numArgs = parameterCount;
		
		if(symbol.tokenType!=34) {//right paren
			error("missing right paren");
		}
		symbol = s2.nextToken(sco);
		//System.out.println("ADMIN PARAM COUNT:"+start);
		if(s2.st.symboltable[start].numArgs!=parameterCount) {
			error("procedure error with parameter count");
		}
		

		quads.insertQuad("CALL", start+"", parameterCount+"", "-");
	}
	public int expression_list() throws Exception{
		pw2.write("\nexpression_list -> expression { , expression }");
		int count = 0;
		Exp x = new Exp();
		expression(x);
		quads.insertQuad("ARG", "-", "-", x.value+"");
		if(x.type!=TokenInterface.INTEGER) {
			error("type mismtach");
		}
		count++;
		while(symbol.tokenType==31) {//comma
			symbol = s2.nextToken(sco);
			x = new Exp();
			expression(x);
			quads.insertQuad("ARG", "-", "-", x.value+"");
			if(x.type!=TokenInterface.INTEGER) {
				error("type mismtach");
			}
			count++;
		}
		return count;
	}
	public void expression(Exp x) throws Exception{
		pw2.write("\nexpression -> simple_expression [ relop simple_expression]");
		simple_expression(x);
		if(symbol.tokenType==19||symbol.tokenType==20||symbol.tokenType==21||symbol.tokenType==22||symbol.tokenType==23||symbol.tokenType==24) {
			String opCode = "";
			if(symbol.tokenType==19) {
				opCode = "EQUAL";
			}
			if(symbol.tokenType==20) {
				opCode="LT";
			}
			if(symbol.tokenType==21) {
				opCode="LE";
			}
			if(symbol.tokenType==22) {
				opCode="GT";
			}
			if(symbol.tokenType==23) {
				opCode="GE";
			}
			if(symbol.tokenType==24) {
				opCode="NE";
			}
			symbol = s2.nextToken(sco);
			Exp w = new Exp();
			simple_expression(w);
			int t = s2.getTemp(sco);
			locals[sco]++;
			s2.st.symboltable[t].offset=locals[sco];
			s2.st.symboltable[t].dataType = TokenInterface.BOOL;
			s2.st.symboltable[t].kind = TokenInterface.TEMP;

			//You must be careful here if x,NUMBER or W.NUMBER is true you
			//should indicate that in the quad, maybe prefix the value with a *
			String xStr=""+x.value;
			String wStr=""+w.value;
			if(x.number==true) {
				xStr = "*"+xStr;
			}
			if(w.number==true) {
				wStr = "*"+wStr;
			}
			quads.insertQuad(opCode, xStr, wStr, t+"");
			x.type = TokenInterface.BOOL;
			x.value = t;
			x.number = false;
			
		}
	}


	
	
	public void simple_expression(Exp x) throws Exception{
		pw2.write("\nsimple_expression ->	[-]term {addop term}");

		int type1 = 0; //is the type of the first operand, 
		int type2 = 0; // is the type of the second operand;
		Exp y = new Exp();
		Exp z = new Exp();
		int t = 0;

		if(symbol.tokenType==26) {//optional minus
			symbol = s2.nextToken(sco);
		}
		term(y);

		while(symbol.tokenType==25||symbol.tokenType==26||symbol.tokenType==14){//addops
			String op = "";
			//String op = symbol.buffy.toUpperCase();
			if(symbol.tokenType==25) {
				op = "ADD";
			}else if(symbol.tokenType==26) {
				op = "SUB";
			}else if(symbol.tokenType==14) {
				op ="OR";
			}else {
				op="error in simple expression method";
			}
			symbol = s2.nextToken(sco);
			term(z);
			type1 = y.type;
			type2 = z.type;
			if (type1 != type2) {
				error("Type mismatch");      	
			}
			t = s2.getTemp(sco);
			locals[sco]++;
			s2.st.symboltable[t].offset=locals[sco];
			s2.st.symboltable[t].dataType = TokenInterface.BOOL;//this line might be wrong
			s2.st.symboltable[t].kind = TokenInterface.TEMP;
			String yStr=""+y.value;
			String zStr=""+z.value;
			if(y.number==true) {
				yStr = "*"+yStr;
			}
			if(z.number==true) {
				zStr = "*"+zStr;
			}
			quads.insertQuad(op, yStr,zStr,t+"");
			y.type = type1;
			y.number = false;
			y.value = t;

		}
		x.type  = y.type;
		x.value = y.value;
		x.number = y.number; 

	}	
	public void term(Exp x) throws Exception{
		pw2.write("\nterm -> factor {mulop  factor }");
		int type1 = 0; //is the type of the first operand, 
		int type2 = 0; // is the type of the second operand;
		Exp y = new Exp();
		Exp z = new Exp();
		int t = 0;
		
		factor(y);

		while(symbol.tokenType==28||symbol.tokenType==27||symbol.tokenType==29||symbol.tokenType==13){//mulops
			String op = symbol.buffy.toUpperCase();
			if(symbol.tokenType==28) {
				op = "MUL";
			}else if(symbol.tokenType==27) {
				op = "DIV";
			}else if(symbol.tokenType==29) {
				op = "MOD";
			}else if(symbol.tokenType==13) {
				op = "AND";
			}else {
				op  = "error in term method";
			}
			symbol = s2.nextToken(sco);
			factor(z);
			type1 = y.type;
			type2 = z.type;
			if (type1 != type2) {
				error("Type mismatch");      	
			}
			t = s2.getTemp(sco);
			locals[sco]++;
			s2.st.symboltable[t].offset=locals[sco];
			s2.st.symboltable[t].dataType = TokenInterface.BOOL;//this line might be wrong
			s2.st.symboltable[t].kind = TokenInterface.TEMP;
			String yStr=""+y.value;
			String zStr=""+z.value;
			if(y.number==true) {
				yStr = "*"+yStr;
			}
			if(z.number==true) {
				zStr = "*"+zStr;
			}
			quads.insertQuad(op, yStr,zStr,t+"");
			y.type = type1;
			y.number = false;
			y.value = t;

		}
		x.type  = y.type;
		x.value = y.value;
		x.number = y.number; 
	}
	public void factor(Exp x)throws Exception {//definitely not right!
		pw2.write("\nfactor -> id | num | true | false | (expression) | not factor");

		if(symbol.tokenType==TokenInterface.IDENTIFIER) {
			if(s2.st.symboltable[symbol.value].declared==false) {
				error("variable not declared");
			}
			x.type = s2.st.symboltable[symbol.value].dataType;
			x.value = symbol.value;
			x.number = false;
			symbol = s2.nextToken(sco);		
		}else if(symbol.tokenType==TokenInterface.NUMBER){
			x.type = TokenInterface.INTEGER;
			x.value = symbol.value;
			x.number = true;
			symbol = s2.nextToken(sco);
		}else if(symbol.tokenType==TokenInterface.LPAREN) {
			symbol = s2.nextToken(sco);
			expression(x);
			if(symbol.tokenType==TokenInterface.RPAREN) {
				symbol = s2.nextToken(sco);
			}else {
				error("missing right paren");
			}
			
			
		}else if(symbol.tokenType==TokenInterface.NOT) {
			symbol = s2.nextToken(sco);
			factor(x);
			if(x.type==TokenInterface.INTEGER) {
				error("type mismatch");
			}
            int t = s2.getTemp(sco);
            locals[sco]++;
			s2.st.symboltable[t].offset=locals[sco];
            s2.st.symboltable[t].dataType=TokenInterface.BOOL;
            s2.st.symboltable[t].kind=TokenInterface.TEMP;	
            quads.insertQuad("NEG", x.value+"", "-", t+"");
            x.type =TokenInterface.BOOL;
            x.value = t;
            x.number = false;

		}


		
		
		
		
		
		
		
		
//		if(symbol.tokenType==33) {//left paren
//			symbol = s2.nextToken(sco);
//			expression();
//			if(symbol.tokenType!=34) {//right paren
//				error("missing right paren");
//			}
//		}else if(symbol.tokenType==15) {//not
//			symbol = s2.nextToken(sco);
//			factor();
//		}else if((symbol.tokenType==35)||(symbol.tokenType==36)||(symbol.tokenType==3)) {//id,num,or boolean
//			//System.out.println("ADMIN: TOKEN TYPE: "+symbol.tokenType);
//			symbol = s2.nextToken(sco);
//			
//		}else {
//			error("invalid factor");
//		}
		
		
		

	}
	public void read_statement()throws Exception {
		pw2.write("\nread_statement -> read ( input_list)");
		if(symbol.tokenType!=16) {//read
			error("missing read");
		}
		symbol = s2.nextToken(sco);
		if(symbol.tokenType!=33) {//left paren
			error("missing left paren");
		}
		symbol = s2.nextToken(sco);
		
		
		input_list();
		
		
		if(symbol.tokenType!=34) {//right paren
			error("missing right paren");
		}
		symbol = s2.nextToken(sco);
	}
	public void write_statement()throws Exception {
		pw2.write("\nwrite_statement -> write(output_item)");
		if(symbol.tokenType!=17) {//write
			error("missing write");
		}
		symbol = s2.nextToken(sco);
		if(symbol.tokenType!=33) {//left paren
			error("missing left paren");
		}
		symbol = s2.nextToken(sco);
		
		
		Exp exp = output_item();
		quads.insertQuad("WRITE", "-", "-", exp.value+"");
		
		if(symbol.tokenType!=34) {//right paren
			error("missing right paren");
		}
		symbol = s2.nextToken(sco);
	}
	public void writeln_statement()throws Exception {
		pw2.write("\nwriteln_statement -> writeln(output_item)");
		if(symbol.tokenType!=18) {//write ln
			error("missing writeln");
		}
		symbol = s2.nextToken(sco);
		if(symbol.tokenType!=33) {//left paren
			error("missing left paren");
		}
		symbol = s2.nextToken(sco);
		
		
		Exp exp = output_item();
		quads.insertQuad("WRITELN", "-", "-", exp.value+"");
		
		if(symbol.tokenType!=34) {//right paren
			error("missing right paren");
		}
		symbol = s2.nextToken(sco);
	}
	public Exp output_item() throws Exception{
		Exp exp = new Exp();
		pw2.write("\noutput_item -> string | expression");
			if(symbol.tokenType==37) {//string
				exp.value = symbol.value;
				symbol = s2.nextToken(sco);
			}else {
				
				expression(exp);
				
			}
			return exp;
	}
	public void input_list() throws Exception{
		pw2.write("\ninput_list -> id {,id}");
		if(symbol.tokenType!=35) {//id
			error("missing or invalid id");
		}
		quads.insertQuad("INPUT", "-", "-", symbol.value+"");
		symbol = s2.nextToken(sco);
		while(symbol.tokenType==31) {//comma
			symbol = s2.nextToken(sco);
			if(symbol.tokenType!=35) {
				error("missing or invalid id");
			}	
			quads.insertQuad("INPUT", "-", "-", symbol.value+"");
			symbol = s2.nextToken(sco);
		}
	}



	public void error(String s){
		System.out.println("\nERROR: "+s + " on line "+symbol.lineNumber);
		System.out.println("ERROR TOKEN VALUE: "+symbol.tokenType);
		System.out.println("ERROR TOKEN BUFF: "+symbol.buffy);
		pw2.write("\nERROR: "+s + " on line "+symbol.lineNumber);
		pw2.close();
		s2.st.printSymbolTable();
		System.exit(0);
	}
	
	
	
	
	public void interpret() throws FileNotFoundException {
		//prints quads to file
		String fileName = "quads.txt";
		PrintWriter pw = new PrintWriter(fileName);
		for(int i=0;i<quads.quadSize;i++) {
			pw.write(quads.qs[i].toString()+"\n");
		}
		pw.close();
		//try {
		Interpreter peter = new Interpreter(quads.qs,fileName,s2.strt.stringtable,s2.st.symboltable);
		//}catch(Exception e) {
			//System.out.println("The road ends here");
			//System.exit(0);
		//}
		}
	
	
	
	
	

	public static void main(String[] args) throws Exception {
		
		//41 is local
		Parser patty = new Parser();
		
	    

	}
	
	

}
