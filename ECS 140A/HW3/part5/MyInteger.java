
public class MyInteger extends Element{
	private int val;
	
	public MyInteger() {val = 0;}
	public MyInteger(int val) {
		this.val = val;
	}

	public int Get() {
		return val;
	}

	public void Set(int val) {
		this.val = val;
	}
	public void Print(){
		System.out.print(" "+val);
	}
}
