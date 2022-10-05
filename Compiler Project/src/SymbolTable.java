
public class SymbolTable {
	public SymbolTableObject[] symboltable;
	
	
	public SymbolTable() {
		symboltable = new SymbolTableObject[100];
		for(int i=0;i<symboltable.length;i++) {
			symboltable[i] = new SymbolTableObject();
		}
	}
	
	public int search(String name) { //that returns the index of name or -1 if not found
		for(int i=0;i<symboltable.length;i++) {
			if(symboltable[i].name.equals(name)) {
				return i;
			}
				
		}
		return -1;
	}
	
	public int insert(String name, int sco) { //that inserts a new name into the table and returns the address
		int i=0;
		while(!symboltable[i].name.equals("")) {
			i++;
		}
		symboltable[i].name=name;
		symboltable[i].scope=sco;
		return i;
	}
	
	public void printSymbolTable() {
		System.out.println("<---Symbol Table--->");
		for(int i=0;i<symboltable.length;i++) {
			if(symboltable[i].name.equals("")) {
				break;
			}
			System.out.println(i+": "+symboltable[i]);
		}
	}
	
	public static void main(String[] args) {
		SymbolTable toby = new SymbolTable();
		//toby.insert("Program");
		//toby.insert("Integer");
		//toby.insert("Token");
		toby.printSymbolTable();
		System.out.println(toby.search("Token"));
	}
}
