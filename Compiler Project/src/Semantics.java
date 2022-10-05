
public class Semantics {

	public final int inttype = TokenInterface.INTEGER;
	public final int booleantype = TokenInterface.BOOL;
	public final int program_name = TokenInterface.PROGRAM;
	public int start, count, size, type;



	public Semantics() {
		start=0;
		count=0;
		size=0;
		type=0;
	}
	public Semantics(int start, int count, int size, int type) {
		this.start=start;
		this.count=count;
		this.size=size;
		this.type=type;
	}

}