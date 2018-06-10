
public class MyChar extends Element {
	private char val;
	public MyChar() {val = '0';}
	public MyChar(char val) {
		this.val =val;
	}

	public char Get() {
		return val;
	}
	public void Set(char val) {
		this.val = val;
	}
	public void Print() {
		System.out.print("'" + val +"'");
	}
}
