
public class QuadType {
String op, arg1, arg2, result;


public QuadType() {
	op="666";
	arg1="666";
	arg2="666";
	result="666";
}
public QuadType(String op, String arg1, String arg2, String result) {
	this.op=op;
	this.arg1=arg1;
	this.arg2=arg2;
	this.result=result;
}
public String toString() {
	return (op+" "+arg1+" "+arg2+" "+result);
}



}
