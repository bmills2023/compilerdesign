
public class Quads {
	public QuadType [] qs;
	public int quadSize;

	public Quads() {
		qs = new QuadType[200];
		quadSize = 0;
		for(int i=0;i<qs.length;i++) {
			qs[i] = new QuadType();
		}
	}
	
	
	public void setResult(int loc1, int loc2) {
		qs[loc1].result=loc2+"";
		
	}
	
	public void insertQuad(String op, String arg1, String arg2, String result) {
		qs[quadSize] = new QuadType(op,arg1,arg2,result);
		quadSize++;
	}
	
	public void printQuads() {
		for(int i=0;i<quadSize;i++) {
			System.out.println(i+": "+qs[i]);
		}
	}
	
	public static void main(String[] args) {
		Quads q = new Quads();
		System.out.println(q.quadSize);
		q.insertQuad("1", "2", "3", "4");
		System.out.println(q.quadSize);
	}
}
