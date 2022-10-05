
public class SymbolTableObject {
public String name;
public int scope;
public int dataType;
public Boolean declared;
public int numArgs;
public int kind;
public int start;
public int offset;
public int memAd;
	
	
	public SymbolTableObject() {
		name="";
		scope=0;
		dataType=0;
		declared=false;
		numArgs=0;
		kind=0;
		start=0;
		offset = 0;
		memAd = -1;
	}
	public SymbolTableObject(String name,int scope,int dataType, Boolean declared, int numArgs, int kind, int start, int offset, int memAd) {
		this.name=name;
		this.scope=scope;
		this.dataType=dataType;
		this.declared=declared;
		this.numArgs=numArgs;
		this.kind=kind;
		this.start=start;
		this.offset=offset;
		this.memAd=memAd;
	}
	public String toString() {
		return ("|Name:"+name+"|Scope:"+scope+"|Data Type:"+dataType+"|Declared:"+declared+"|Num Args:"+numArgs+"|Kind:"+kind+"|Start:"+start+"|Offset:"+offset+"|MemAd:"+memAd+"|");
	}
	
	
	
}


