import java.io.*;
import java.util.*;

public class Interpreter {//possibly problem...differnce between *numbers and symbol table addresses everywhere

	public QuadType[] quads;
	public StringTableObject[] stringTable;
	public SymbolTableObject[] symbolTable;
	public int[] mem;
	public int top,SP,PC;
	public String boyo;
	public Scanner sam;
	public int argCount = 0;
	//,String[] stringTable,SymbolTableObject[] symbolTable
	public Interpreter(QuadType[] quads,String quadFileName,StringTableObject[] stringTable,SymbolTableObject[] symbolTable) throws FileNotFoundException {
		this.quads=quads;
		this.stringTable=stringTable;
		this.symbolTable=symbolTable;
		mem = new int[501];
		for(int i=0;i<mem.length;i++) {
			mem[i]=-7;
		}
		sam = new Scanner(System.in);
		top = mem.length;
		SP = mem.length;
		PC = 0;
		boyo = "";
		System.out.println("<---Program Running--->");
		switcher();

	}

	public void printMem() {
		System.out.println("<---Memory Table--->");
		for(int i=0;i<mem.length;i++) {

			System.out.println(i+": "+mem[i]);

		}
		System.out.println("<------------------>");
	}

	public void switcher() {
		boyo = quads[PC].op;
		//System.out.println("SP:"+SP);
		//System.out.println("TOP:"+top);
		//printMem();
		
		//System.out.println(quads[PC].toString());
		switch(boyo) {
		case "DCL":
			declare();
			break;
		case "BR":
			branch();
			break;
		case "PRODEC":
			prodec();
			break;
		case "PARAM":
			param();
			break;
		case "LT":
			LT();
			break;
		case "LE":
			LE();
			break;
		case "GT":
			GT();
			break;
		case "GE":
			GE();
			break;
		case "NE":
			NE();
			break;
		case "EQUAL":
			equal();
			break;
		case "BR0":
			branchZero();
			break;
		case "ASSIGN":
			assign();
			break;
		case "ADD":
			add();
			break;
		case "SUB":
			sub();
			break;
		case "MUL":
			mult();
			break;
		case "DIV":
			divide();
			break;
		case "MOD":
			mod();
			break;
		case "WRITE":
			write();
			break;
		case "WRITELN":
			writeln();
			break;
		case "EXIT":
			exit();
			break;
		case "INPUT":
			input();
			break;
		case "ARG":
			arg();
			break;
		case "CALL":
			call();
			break;
		case "END":
			end();
			break;
		default:
			System.out.println("error in switch statement");
		}
	}


	public void declare() {
		int num = Integer.parseInt(quads[PC].result);
		SymbolTableObject obj = symbolTable[num];
		if(obj.kind==TokenInterface.LOCAL) {
			top = SP - obj.offset;
		}else if(obj.kind==TokenInterface.PARM){
			top = SP + 3 + obj.numArgs - obj.offset + 1;
		}else {
			System.out.println("Error in kind in delcare method");
		}

		PC++;
		switcher();
	}
	public void branch() {

		int num = Integer.parseInt(quads[PC].result);
		PC = num;
		switcher();
	}
	public void prodec() {
		SP = top;
		PC++;
		switcher();
	}
	public void param() {
		PC++;
		switcher();
	}

	public void equal() {
		//int argCount = mem[SP+3];
		int R = -1;
		boolean RNum = false;
		int S = -1;
		boolean SNum = false;
		int T = -1;

		if(quads[PC].arg1.indexOf('*')<0) {
			int arg1 = Integer.parseInt(quads[PC].arg1);
			SymbolTableObject obj = symbolTable[arg1];
			if(obj.kind==TokenInterface.PARM) {
				R = SP + 3 +  argCount - obj.offset + 1;
			}else if(obj.kind==TokenInterface.LOCAL||obj.kind==TokenInterface.TEMP) {
				R = SP - obj.offset;	
			}else {
				System.out.println("Error with finding kind from symbol table");
			}
		}else {
			RNum = true;
		}

		if(quads[PC].arg2.indexOf('*')<0) {
			int arg2 = Integer.parseInt(quads[PC].arg2);
			SymbolTableObject obj2 = symbolTable[arg2];
			if(obj2.kind==TokenInterface.PARM) {
				S = SP + 3 + argCount - obj2.offset + 1;
			}else if(obj2.kind==TokenInterface.LOCAL||obj2.kind==TokenInterface.TEMP) {
				S = SP - obj2.offset;	
			}else {
				System.out.println("Error with finding kind from symbol table");
			}
		}else {
			SNum = true;
		}


		int result = Integer.parseInt(quads[PC].result);
		SymbolTableObject obj3 = symbolTable[result];
		if(obj3.kind==TokenInterface.TEMP) {
			top--;
			T = SP - obj3.offset;
		}else if(obj3.kind==TokenInterface.PARM) {
			top--;
			T = SP + 3 + argCount - obj3.offset + 1;
		}else {
			System.out.println("Error with kind");
		}


		if(RNum) {//if arg1 is a final number with a *
			R = Integer.parseInt(quads[PC].arg1.substring(1,quads[PC].arg1.length()));
		}else {//if arg1 is an addy
			R = mem[R];
		}
		if(SNum) {//if arg2 is a final number with a *
			S = Integer.parseInt(quads[PC].arg2.substring(1,quads[PC].arg2.length()));
		}else {//if arg2 is an addy
			S = mem[S];
		}


		if(R==S) {
			mem[T]=1;
		}else {
			mem[T]=0;
		}

		PC++;
		switcher();
	}


	public void LT() {
		//int argCount = mem[SP+3];

		//System.out.println("ADMIN SP:"+SP);
		int R = -1;
		boolean RNum = false;
		int S = -1;
		boolean SNum = false;
		int T = -1;

		if(quads[PC].arg1.indexOf('*')<0) {
			int arg1 = Integer.parseInt(quads[PC].arg1);
			SymbolTableObject obj = symbolTable[arg1];
			if(obj.kind==TokenInterface.PARM) {
				R = SP + 3 + argCount - obj.offset + 1;
			}else if(obj.kind==TokenInterface.LOCAL||obj.kind==TokenInterface.TEMP) {
				R = SP - obj.offset;	
			}else {
				System.out.println("Error with finding kind from symbol table");
			}
		}else {
			RNum = true;
		}

		if(quads[PC].arg2.indexOf('*')<0) {
			int arg2 = Integer.parseInt(quads[PC].arg2);
			SymbolTableObject obj2 = symbolTable[arg2];
			if(obj2.kind==TokenInterface.PARM) {
				S = SP + 3 + argCount - obj2.offset + 1;
			}else if(obj2.kind==TokenInterface.LOCAL||obj2.kind==TokenInterface.TEMP) {
				S = SP - obj2.offset;	
			}else {
				System.out.println("Error with finding kind from symbol table");
			}
		}else {
			SNum = true;
		}


		int result = Integer.parseInt(quads[PC].result);
		SymbolTableObject obj3 = symbolTable[result];
		if(obj3.kind==TokenInterface.TEMP) {
			top--;
			T = SP - obj3.offset;
		}else if(obj3.kind==TokenInterface.PARM) {
			top--;
			T = SP + 3 + argCount - obj3.offset + 1;
		}else {
			System.out.println("Error with kind");
		}


		if(RNum) {//if arg1 is a final number with a *
			R = Integer.parseInt(quads[PC].arg1.substring(1,quads[PC].arg1.length()));
		}else {//if arg1 is an addy
			R = mem[R];
		}
		if(SNum) {//if arg2 is a final number with a *
			S = Integer.parseInt(quads[PC].arg2.substring(1,quads[PC].arg2.length()));
		}else {//if arg2 is an addy
			S = mem[S];
		}


		if(R<S) {
			mem[T]=1;
		}else {
			mem[T]=0;
		}

		PC++;

		switcher();


	}

	public void LE() {
		//int argCount = mem[SP+3];
		int R = -1;
		boolean RNum = false;
		int S = -1;
		boolean SNum = false;
		int T = -1;

		if(quads[PC].arg1.indexOf('*')<0) {
			int arg1 = Integer.parseInt(quads[PC].arg1);
			SymbolTableObject obj = symbolTable[arg1];
			if(obj.kind==TokenInterface.PARM) {
				R = SP + 3 + argCount - obj.offset + 1;
			}else if(obj.kind==TokenInterface.LOCAL||obj.kind==TokenInterface.TEMP) {
				R = SP - obj.offset;	
			}else {
				System.out.println("Error with finding kind from symbol table");
			}
		}else {
			RNum = true;
		}

		if(quads[PC].arg2.indexOf('*')<0) {
			int arg2 = Integer.parseInt(quads[PC].arg2);
			SymbolTableObject obj2 = symbolTable[arg2];
			if(obj2.kind==TokenInterface.PARM) {
				S = SP + 3 + argCount - obj2.offset + 1;
			}else if(obj2.kind==TokenInterface.LOCAL||obj2.kind==TokenInterface.TEMP) {
				S = SP - obj2.offset;	
			}else {
				System.out.println("Error with finding kind from symbol table");
			}
		}else {
			SNum = true;
		}


		int result = Integer.parseInt(quads[PC].result);
		SymbolTableObject obj3 = symbolTable[result];
		if(obj3.kind==TokenInterface.TEMP) {
			top--;
			T = SP - obj3.offset;
		}else if(obj3.kind==TokenInterface.PARM) {
			top--;
			T = SP + 3 + argCount - obj3.offset + 1;
		}else {
			System.out.println("Error with kind");
		}


		if(RNum) {//if arg1 is a final number with a *
			R = Integer.parseInt(quads[PC].arg1.substring(1,quads[PC].arg1.length()));
		}else {//if arg1 is an addy
			R = mem[R];
		}
		if(SNum) {//if arg2 is a final number with a *
			S = Integer.parseInt(quads[PC].arg2.substring(1,quads[PC].arg2.length()));
		}else {//if arg2 is an addy
			S = mem[S];
		}


		if(R<=S) {
			mem[T]=1;
		}else {
			mem[T]=0;
		}

		PC++;
		switcher();
	}
	public void GT() {
		//int argCount = mem[SP+3];
		int R = -1;
		boolean RNum = false;
		int S = -1;
		boolean SNum = false;
		int T = -1;

		if(quads[PC].arg1.indexOf('*')<0) {
			int arg1 = Integer.parseInt(quads[PC].arg1);
			SymbolTableObject obj = symbolTable[arg1];
			if(obj.kind==TokenInterface.PARM) {
				R = SP + 3 + argCount - obj.offset + 1;
			}else if(obj.kind==TokenInterface.LOCAL||obj.kind==TokenInterface.TEMP) {
				R = SP - obj.offset;	
			}else {
				System.out.println("Error with finding kind from symbol table");
			}
		}else {
			RNum = true;
		}

		if(quads[PC].arg2.indexOf('*')<0) {
			int arg2 = Integer.parseInt(quads[PC].arg2);
			SymbolTableObject obj2 = symbolTable[arg2];
			if(obj2.kind==TokenInterface.PARM) {
				S = SP + 3 + argCount - obj2.offset + 1;
			}else if(obj2.kind==TokenInterface.LOCAL||obj2.kind==TokenInterface.TEMP) {
				S = SP - obj2.offset;	
			}else {
				System.out.println("Error with finding kind from symbol table");
			}
		}else {
			SNum = true;
		}


		int result = Integer.parseInt(quads[PC].result);
		SymbolTableObject obj3 = symbolTable[result];
		if(obj3.kind==TokenInterface.TEMP) {
			top--;
			T = SP - obj3.offset;
			//System.out.println("ADMIN SP:"+SP);
			
		}else if(obj3.kind==TokenInterface.PARM) {
			top--;
			T = SP + 3 + argCount - obj3.offset + 1;
		}else {
			System.out.println("Error with kind");
		}


		if(RNum) {//if arg1 is a final number with a *
			R = Integer.parseInt(quads[PC].arg1.substring(1,quads[PC].arg1.length()));
		}else {//if arg1 is an addy
			R = mem[R];
		}
		if(SNum) {//if arg2 is a final number with a *
			S = Integer.parseInt(quads[PC].arg2.substring(1,quads[PC].arg2.length()));
		}else {//if arg2 is an addy
			S = mem[S];
		}


		if(R>S) {
			mem[T]=1;
		}else {
			mem[T]=0;
		}

		PC++;
		switcher();
	}
	public void GE() {
		//int argCount = mem[SP+3];
		int R = -1;
		boolean RNum = false;
		int S = -1;
		boolean SNum = false;
		int T = -1;

		if(quads[PC].arg1.indexOf('*')<0) {
			int arg1 = Integer.parseInt(quads[PC].arg1);
			SymbolTableObject obj = symbolTable[arg1];
			if(obj.kind==TokenInterface.PARM) {
				R = SP + 3 + argCount - obj.offset + 1;
			}else if(obj.kind==TokenInterface.LOCAL||obj.kind==TokenInterface.TEMP) {
				R = SP - obj.offset;	
			}else {
				System.out.println("Error with finding kind from symbol table");
			}
		}else {
			RNum = true;
		}

		if(quads[PC].arg2.indexOf('*')<0) {
			int arg2 = Integer.parseInt(quads[PC].arg2);
			SymbolTableObject obj2 = symbolTable[arg2];
			if(obj2.kind==TokenInterface.PARM) {
				S = SP + 3 + argCount - obj2.offset + 1;
			}else if(obj2.kind==TokenInterface.LOCAL||obj2.kind==TokenInterface.TEMP) {
				S = SP - obj2.offset;	
			}else {
				System.out.println("Error with finding kind from symbol table");
			}
		}else {
			SNum = true;
		}


		int result = Integer.parseInt(quads[PC].result);
		SymbolTableObject obj3 = symbolTable[result];
		if(obj3.kind==TokenInterface.TEMP) {
			top--;
			T = SP - obj3.offset;
		}else if(obj3.kind==TokenInterface.PARM) {
			top--;
			T = SP + 3 + argCount - obj3.offset + 1;
		}else {
			System.out.println("Error with kind");
		}


		if(RNum) {//if arg1 is a final number with a *
			R = Integer.parseInt(quads[PC].arg1.substring(1,quads[PC].arg1.length()));
		}else {//if arg1 is an addy
			R = mem[R];
		}
		if(SNum) {//if arg2 is a final number with a *
			S = Integer.parseInt(quads[PC].arg2.substring(1,quads[PC].arg2.length()));
		}else {//if arg2 is an addy
			S = mem[S];
		}


		if(R>=S) {
			mem[T]=1;
		}else {
			mem[T]=0;
		}

		PC++;
		switcher();
	}
	public void NE() {
		//int argCount = mem[SP+3];
		int R = -1;
		boolean RNum = false;
		int S = -1;
		boolean SNum = false;
		int T = -1;

		if(quads[PC].arg1.indexOf('*')<0) {
			int arg1 = Integer.parseInt(quads[PC].arg1);
			SymbolTableObject obj = symbolTable[arg1];
			if(obj.kind==TokenInterface.PARM) {
				//System.out.println("ADMIN SP:"+SP);
				R = SP + 3 + argCount - obj.offset + 1;
			}else if(obj.kind==TokenInterface.LOCAL||obj.kind==TokenInterface.TEMP) {
				R = SP - obj.offset;	
			}else {
				System.out.println("Error with finding kind from symbol table");
			}
		}else {
			RNum = true;
		}

		if(quads[PC].arg2.indexOf('*')<0) {
			int arg2 = Integer.parseInt(quads[PC].arg2);
			SymbolTableObject obj2 = symbolTable[arg2];
			if(obj2.kind==TokenInterface.PARM) {
				S = SP + 3 + argCount - obj2.offset + 1;
			}else if(obj2.kind==TokenInterface.LOCAL||obj2.kind==TokenInterface.TEMP) {
				S = SP - obj2.offset;	
			}else {
				System.out.println("Error with finding kind from symbol table");
			}
		}else {
			SNum = true;
		}


		int result = Integer.parseInt(quads[PC].result);
		SymbolTableObject obj3 = symbolTable[result];
		if(obj3.kind==TokenInterface.TEMP) {
			top--;
			T = SP - obj3.offset;
		}else if(obj3.kind==TokenInterface.PARM) {
			T = SP + 3 + argCount - obj3.offset + 1;
		}else {
			System.out.println("Error with kind");
		}


		if(RNum) {//if arg1 is a final number with a *
			R = Integer.parseInt(quads[PC].arg1.substring(1,quads[PC].arg1.length()));
		}else {//if arg1 is an addy
			R = mem[R];
		}
		if(SNum) {//if arg2 is a final number with a *
			S = Integer.parseInt(quads[PC].arg2.substring(1,quads[PC].arg2.length()));
		}else {//if arg2 is an addy
			S = mem[S];
		}

		//System.out.println("R:"+R);
		//System.out.println("S:"+S);
		if(R!=S) {
			//System.out.println("ADMIN NOT EQUAL IS TRUE");
			mem[T]=1;
		}else {
			//System.out.println("ADMIN NOT EQUAL IS NOT TRUE");
			mem[T]=0;
		}
		

		PC++;
		switcher();
	}

	public void branchZero() {
		int T = -1;
		int arg1 = Integer.parseInt(quads[PC].arg1);
		SymbolTableObject obj = symbolTable[arg1];
		if(obj.kind==TokenInterface.TEMP) {
			T = SP - obj.offset;
		}else if(obj.kind==TokenInterface.PARM) {
			T = SP + 3 + argCount - obj.offset + 1;
		}else {
			System.out.println("Error with kind");
		}

		if(mem[T]==0) {
			PC = Integer.parseInt(quads[PC].result);
		}else if(mem[T]==1){
			PC++;
		}else {
			System.out.println("error in branch zero");
		}
		switcher();



	}
	public void assign() {//prob wrong cuz of *numbers
		//int argCount = mem[SP+3];
		int R = -1;
		boolean RNum = false;
		int T = -1;

		if(quads[PC].arg1.indexOf('*')<0) {
			int arg1 = Integer.parseInt(quads[PC].arg1);
			SymbolTableObject obj = symbolTable[arg1];
			if(obj.kind==TokenInterface.PARM) {
				R = SP + 3 + argCount - obj.offset + 1;
			}else if(obj.kind==TokenInterface.LOCAL||obj.kind==TokenInterface.TEMP) {
				R = SP - obj.offset;	
			}else {
				System.out.println("Error with finding kind from symbol table");
			}
		}else {
			RNum = true;
		}



		int result = Integer.parseInt(quads[PC].result);
		SymbolTableObject obj2 = symbolTable[result];
		if(obj2.kind==TokenInterface.LOCAL) {
			T = SP - obj2.offset;
		}else if(obj2.kind==TokenInterface.PARM){
			T = SP + 3 + argCount - obj2.offset + 1;
		}else {
			System.out.println("Error in assign method");
		}

		if(RNum) {//if arg1 is a final number with a *
			R = Integer.parseInt(quads[PC].arg1.substring(1,quads[PC].arg1.length()));
		}else {//if arg1 is an addy
			R = mem[R];
		}


		mem[T]=R;




		PC++;
		switcher();
	}
	public void write() {
		int result = Integer.parseInt(quads[PC].result);

		if(result>=100) {//string table address
			System.out.print(stringTable[result].str);
		}else {//non string table
			SymbolTableObject obj = symbolTable[result];
			if(obj.kind==TokenInterface.LOCAL||obj.kind==TokenInterface.TEMP) {
				System.out.print(mem[SP - obj.offset]);
			}else if(obj.kind==TokenInterface.PARM) {
				System.out.print(mem[SP + 3 + argCount - obj.offset + 1]);	
			}else {
				System.out.println("Error in writeln kind method");
			}
		}	
		PC++;
		switcher();
	}

	public void writeln() {
		int result = Integer.parseInt(quads[PC].result);

		if(result>=100) {//string table address
			System.out.println(stringTable[result].str);
		}else {//non string table
			SymbolTableObject obj = symbolTable[result];
			if(obj.kind==TokenInterface.LOCAL||obj.kind==TokenInterface.TEMP) {
				System.out.println(mem[SP - obj.offset]);
			}else if(obj.kind==TokenInterface.PARM) {
				System.out.println(mem[SP + 3 + argCount - obj.offset + 1]);	
			}else {
				System.out.println("Error in writeln kind method");
			}
		}	
		PC++;
		switcher();
	}

	public void exit() {

		top = SP + 2;//uhhhhhhhh idk about ralphs instruction on this line
		int RA = mem[top];
		SP = mem[SP];
		top++;
		//int argCount = mem[top];
		for(int i=1;i<argCount+1;i++) {//this loop might be wrong...might be less than or equal to idk
			top++;
		}

		PC = RA;
		//System.out.println("PC:"+PC);
		switcher();



	}
	public void input() {
		//System.out.println("here");

		int num = Integer.parseInt(quads[PC].result);
		SymbolTableObject obj = symbolTable[num];
		int userInput = sam.nextInt();
		mem[SP-obj.offset]=userInput;
		PC++;
		switcher();
	}


	public void add() {
		//int argCount = mem[SP+3];
		int R = -1;
		boolean RNum = false;
		int S = -1;
		boolean SNum = false;
		int T = -1;

		if(quads[PC].arg1.indexOf('*')<0) {
			int arg1 = Integer.parseInt(quads[PC].arg1);
			SymbolTableObject obj = symbolTable[arg1];
			if(obj.kind==TokenInterface.PARM) {
				R = SP + 3 + argCount - obj.offset + 1;
			}else if(obj.kind==TokenInterface.LOCAL||obj.kind==TokenInterface.TEMP) {
				R = SP - obj.offset;	
			}else {
				System.out.println("Error with finding kind from symbol table");
			}
		}else {
			RNum = true;
		}

		if(quads[PC].arg2.indexOf('*')<0) {
			int arg2 = Integer.parseInt(quads[PC].arg2);
			SymbolTableObject obj2 = symbolTable[arg2];
			if(obj2.kind==TokenInterface.PARM) {
				S = SP + 3 + argCount - obj2.offset + 1;
			}else if(obj2.kind==TokenInterface.LOCAL||obj2.kind==TokenInterface.TEMP) {
				S = SP - obj2.offset;	
			}else {
				System.out.println("Error with finding kind from symbol table");
			}
		}else {
			SNum = true;
		}


		int result = Integer.parseInt(quads[PC].result);
		SymbolTableObject obj3 = symbolTable[result];
		if(obj3.kind==TokenInterface.TEMP) {
			top--;
			T = SP - obj3.offset;
		}else if(obj3.kind==TokenInterface.PARM) {
			top--;
			T = SP + 3 + argCount - obj3.offset + 1;
		}else {
			System.out.println("Error with kind");
		}
	


		if(RNum) {//if arg1 is a final number with a *
			R = Integer.parseInt(quads[PC].arg1.substring(1,quads[PC].arg1.length()));
		}else {//if arg1 is an addy
			R = mem[R];
		}
		if(SNum) {//if arg2 is a final number with a *
			S = Integer.parseInt(quads[PC].arg2.substring(1,quads[PC].arg2.length()));
		}else {//if arg2 is an addy
			S = mem[S];
		}

		mem[T] = R + S;
		//System.out.println("ADMINR:"+RNum);
		//System.out.println("ADMINS:"+SNum);

		PC++;
		switcher();
	}
	public void sub() {
		//int argCount = mem[SP+3];
		int R = -1;
		boolean RNum = false;
		int S = -1;
		boolean SNum = false;
		int T = -1;

		if(quads[PC].arg1.indexOf('*')<0) {
			int arg1 = Integer.parseInt(quads[PC].arg1);
			SymbolTableObject obj = symbolTable[arg1];
			if(obj.kind==TokenInterface.PARM) {
				R = SP + 3 + 2 - obj.offset + 1;
			}else if(obj.kind==TokenInterface.LOCAL||obj.kind==TokenInterface.TEMP) {
				R = SP - obj.offset;	
			}else {
				System.out.println("Error with finding kind from symbol table");
			}
		}else {
			RNum = true;
		}

		if(quads[PC].arg2.indexOf('*')<0) {
			int arg2 = Integer.parseInt(quads[PC].arg2);
			SymbolTableObject obj2 = symbolTable[arg2];
			if(obj2.kind==TokenInterface.PARM) {
				S = SP + 3 + 2 - obj2.offset + 1;
			}else if(obj2.kind==TokenInterface.LOCAL||obj2.kind==TokenInterface.TEMP) {
				S = SP - obj2.offset;	
			}else {
				System.out.println("Error with finding kind from symbol table");
			}
		}else {
			SNum = true;
		}


		int result = Integer.parseInt(quads[PC].result);
		SymbolTableObject obj3 = symbolTable[result];
		if(obj3.kind==TokenInterface.TEMP) {
			top--;
			T = SP - obj3.offset;
		}else if(obj3.kind==TokenInterface.PARM) {
			top--;
			T = SP + 3 + argCount - obj3.offset + 1;
		}else {
			System.out.println("Error with kind");
		}


		if(RNum) {//if arg1 is a final number with a *
			R = Integer.parseInt(quads[PC].arg1.substring(1,quads[PC].arg1.length()));
		}else {//if arg1 is an addy
			R = mem[R];
		}
		if(SNum) {//if arg2 is a final number with a *
			S = Integer.parseInt(quads[PC].arg2.substring(1,quads[PC].arg2.length()));
		}else {//if arg2 is an addy
			S = mem[S];
		}

		mem[T] = R - S;

		PC++;
		switcher();
	}
	public void mult() {
		//int argCount = mem[SP+3];
		int R = -1;
		boolean RNum = false;
		int S = -1;
		boolean SNum = false;
		int T = -1;

		if(quads[PC].arg1.indexOf('*')<0) {
			int arg1 = Integer.parseInt(quads[PC].arg1);
			SymbolTableObject obj = symbolTable[arg1];
			if(obj.kind==TokenInterface.PARM) {
				R = SP + 3 + 2 - obj.offset + 1;
			}else if(obj.kind==TokenInterface.LOCAL||obj.kind==TokenInterface.TEMP) {
				R = SP - obj.offset;	
			}else {
				System.out.println("Error with finding kind from symbol table");
			}
		}else {
			RNum = true;
		}

		if(quads[PC].arg2.indexOf('*')<0) {
			int arg2 = Integer.parseInt(quads[PC].arg2);
			SymbolTableObject obj2 = symbolTable[arg2];
			if(obj2.kind==TokenInterface.PARM) {
				S = SP + 3 + 2 - obj2.offset + 1;
			}else if(obj2.kind==TokenInterface.LOCAL||obj2.kind==TokenInterface.TEMP) {
				S = SP - obj2.offset;	
			}else {
				System.out.println("Error with finding kind from symbol table");
			}
		}else {
			SNum = true;
		}


		int result = Integer.parseInt(quads[PC].result);
		SymbolTableObject obj3 = symbolTable[result];
		if(obj3.kind==TokenInterface.TEMP) {
			top--;
			T = SP - obj3.offset;
		}else if(obj3.kind==TokenInterface.PARM) {
			top--;
			T = SP + 3 + argCount - obj3.offset + 1;
		}else {
			System.out.println("Error with kind");
		}
	


		//System.out.println("ADMIN R:"+R);
		//System.out.println("ADMIN S:"+S);



		if(RNum) {//if arg1 is a final number with a *
			R = Integer.parseInt(quads[PC].arg1.substring(1,quads[PC].arg1.length()));
		}else {//if arg1 is an addy
			R = mem[R];
		}
		if(SNum) {//if arg2 is a final number with a *
			S = Integer.parseInt(quads[PC].arg2.substring(1,quads[PC].arg2.length()));
		}else {//if arg2 is an addy
			S = mem[S];
		}


		mem[T] = R * S;

		PC++;
		switcher();
	}
	public void divide() {
		//int argCount = mem[SP+3];
		int R = -1;
		boolean RNum = false;
		int S = -1;
		boolean SNum = false;
		int T = -1;

		if(quads[PC].arg1.indexOf('*')<0) {
			int arg1 = Integer.parseInt(quads[PC].arg1);
			SymbolTableObject obj = symbolTable[arg1];
			if(obj.kind==TokenInterface.PARM) {
				R = SP + 3 + argCount - obj.offset + 1;
			}else if(obj.kind==TokenInterface.LOCAL||obj.kind==TokenInterface.TEMP) {
				R = SP - obj.offset;	
			}else {
				System.out.println("Error with finding kind from symbol table");
			}
		}else {
			RNum = true;
		}

		if(quads[PC].arg2.indexOf('*')<0) {
			int arg2 = Integer.parseInt(quads[PC].arg2);
			SymbolTableObject obj2 = symbolTable[arg2];
			if(obj2.kind==TokenInterface.PARM) {
				S = SP + 3 + argCount - obj2.offset + 1;
			}else if(obj2.kind==TokenInterface.LOCAL||obj2.kind==TokenInterface.TEMP) {
				S = SP - obj2.offset;	
			}else {
				System.out.println("Error with finding kind from symbol table");
			}
		}else {
			SNum = true;
		}


		int result = Integer.parseInt(quads[PC].result);
		SymbolTableObject obj3 = symbolTable[result];
		if(obj3.kind==TokenInterface.TEMP) {
			top--;
			T = SP - obj3.offset;
		}else if(obj3.kind==TokenInterface.PARM) {
			T = SP + 3 + argCount - obj3.offset + 1;
		}else {
			System.out.println("Error with kind");
		}


		if(RNum) {//if arg1 is a final number with a *
			R = Integer.parseInt(quads[PC].arg1.substring(1,quads[PC].arg1.length()));
		}else {//if arg1 is an addy
			R = mem[R];
		}
		if(SNum) {//if arg2 is a final number with a *
			S = Integer.parseInt(quads[PC].arg2.substring(1,quads[PC].arg2.length()));
		}else {//if arg2 is an addy
			S = mem[S];
		}

		mem[T] = R / S;

		PC++;
		switcher();
	}
	public void mod() {
		//int argCount = mem[SP+3];
		int R = -1;
		boolean RNum = false;
		int S = -1;
		boolean SNum = false;
		int T = -1;

		if(quads[PC].arg1.indexOf('*')<0) {
			int arg1 = Integer.parseInt(quads[PC].arg1);
			SymbolTableObject obj = symbolTable[arg1];
			if(obj.kind==TokenInterface.PARM) {
				R = SP + 3 + argCount - obj.offset + 1;
			}else if(obj.kind==TokenInterface.LOCAL||obj.kind==TokenInterface.TEMP) {
				R = SP - obj.offset;	
			}else {
				System.out.println("Error with finding kind from symbol table");
			}
		}else {
			RNum = true;
		}

		if(quads[PC].arg2.indexOf('*')<0) {
			int arg2 = Integer.parseInt(quads[PC].arg2);
			SymbolTableObject obj2 = symbolTable[arg2];
			if(obj2.kind==TokenInterface.PARM) {
				S = SP + 3 + argCount - obj2.offset + 1;
			}else if(obj2.kind==TokenInterface.LOCAL||obj2.kind==TokenInterface.TEMP) {
				S = SP - obj2.offset;	
			}else {
				System.out.println("Error with finding kind from symbol table");
			}
		}else {
			SNum = true;
		}


		int result = Integer.parseInt(quads[PC].result);
		SymbolTableObject obj3 = symbolTable[result];
		if(obj3.kind==TokenInterface.TEMP) {
			top--;
			T = SP - obj3.offset;
		}else if(obj3.kind==TokenInterface.PARM) {
			T = SP + 3 + argCount - obj3.offset + 1;
		}else {
			System.out.println("Error with kind");
		}


		if(RNum) {//if arg1 is a final number with a *
			R = Integer.parseInt(quads[PC].arg1.substring(1,quads[PC].arg1.length()));
		}else {//if arg1 is an addy
			R = mem[R];
		}
		if(SNum) {//if arg2 is a final number with a *
			S = Integer.parseInt(quads[PC].arg2.substring(1,quads[PC].arg2.length()));
		}else {//if arg2 is an addy
			S = mem[S];
		}

		mem[T] = R % S;

		PC++;
		switcher();
	}
	public void arg() {
		int num = Integer.parseInt(quads[PC].result);
		SymbolTableObject obj = symbolTable[num];

		top--;
		if(obj.kind==TokenInterface.LOCAL||obj.kind==TokenInterface.TEMP) {
			mem[top]=mem[SP-obj.offset];
		}else if(obj.kind==TokenInterface.PARM) {
			mem[top]=mem[SP + 3 + argCount - obj.offset + 1];
		}else {
			System.out.println("error with kind in arg method");
		}
		PC++;
		switcher();
	}
	public void call() {//error here?
		//System.out.println("ADMIN NUMARGS:"+argCount);
		int numArgs = Integer.parseInt(quads[PC].arg2);
		//int addy = Integer.parseInt(quads[PC].arg1);

		top--;
		mem[top]=numArgs;
		argCount=numArgs;

		top--;
		mem[top]=PC+1;
		//System.out.println("ADMIN:"+(PC+1));

		top--;
		mem[top]=666;
		//space

		top--;
		mem[top]=SP;
		//System.out.println("ADMIN:"+quads[PC].arg1);
		int num = Integer.parseInt(quads[PC].arg1);
		SymbolTableObject obj = symbolTable[num];
		PC = obj.start;


		switcher();


	}
	public void end() {
		//System.out.println();
		//printMem();
		System.exit(0);
	}



}
