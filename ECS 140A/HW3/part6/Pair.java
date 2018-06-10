
public class Pair extends Element {

	private MyChar key;
	private Element value;
	
	public Pair(MyChar key, Element value) {
		super();
		this.key = key;
		this.value = value;
	}

	public MyChar getKey() {
		return key;
	}

	public void setKey(MyChar key) {
		this.key = key;
	}

	public Element getValue() {
		return value;
	}

	public void setValue(Element value) {
		this.value = value;
	}

	@Override
	public void Print() {
		// TODO Auto-generated method stub
		System.out.print("(");
		key.Print();
		System.out.print(" ");
		value.Print();
		System.out.print(")");
	}

}
