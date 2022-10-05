
public class StringTable {
	//left off here: need to make string table
	public StringTableObject[] stringtable;
	
	
	public StringTable() {
		stringtable = new StringTableObject[200];
		for(int i=0;i<stringtable.length;i++) {
			stringtable[i] = new StringTableObject();
		}
	}
	
	public int search(String name) { //that returns the index of name or -1 if not found
		for(int i=0;i<stringtable.length;i++) {
			if(stringtable[i].str.equals(name)) {
				return i;
			}
				
		}
		return -1;
	}
	
	public int insert(String name) { //that inserts a new name into the table and returns the address
		int i=100;
		while(!stringtable[i].str.equals("")) {
			i++;
		}
		stringtable[i].str=name;
		return i;
	}
	
	public void printStringTable() {
		System.out.println("<---String Table--->");
		for(int i=100;i<stringtable.length;i++) {
			if(stringtable[i].str.equals("")) {
				break;
			}
			System.out.println(i+": "+stringtable[i].str);
		}
	}
	
	public static void main(String[] args) {
		StringTable toby = new StringTable();
		toby.insert("Adam");
		toby.insert("Is");
		toby.insert("Lame");
		toby.printStringTable();
		System.out.println(toby.search("Lame"));
	}
}
