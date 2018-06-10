
public class Sequence extends Element{
	private Element content;
	private Sequence next;
	//private int index = -1; // when empty no index.
	
	public Sequence() {
		this.content = null;
		this.next = null;
	}
	public Sequence(Element cont, Sequence nt) {
		this.content = cont;
		this.next = nt;
	}
	
	public Element first() {	
		return content;	
	}
	public Sequence rest() {
		return next;
	}
	
	public int length() {
		Sequence nowSq = this;
		int i = 0;
		while(nowSq!=null) {
			nowSq = nowSq.next;
			i++;
		}
		return i;
	}
	
	public void add(Element elm, int pos){
//		System.out.print("want to add ");
//		elm.Print();
//		System.out.println(" in "+pos);
		if(pos >= 0 && pos <= length()) {
			if(content == null) {
				content = elm;
				//index = 0;
//				System.out.println("already set head");
//				System.out.println("now size is "+length());	
//				System.out.println("------------------");
			}
//			else if(next == null) {
//				this.next = new Sequence();
//				this.next.content = elm;
//				//this.next.index = pos;
////				System.out.print("already add ");
////				next.content.Print();
////				System.out.println("in "+ pos);
////				System.out.print("now size is "+length());
////				System.out.println(" ");
////				System.out.println("------------------");
//			}
			//add entry in the end of sequence
			else if(length()  == pos) {
				
					Sequence nowSq = this;
					while(nowSq.next!=null) {
						nowSq = nowSq.next;
					}
					nowSq.next = new Sequence(elm,null);
					//nextSq.index =pos;
					//nowSq.next = nextSq;
//					System.out.print("already add ");
//					nowSq.next.content.Print();
//					System.out.println(" in "+ pos);
//					System.out.print("now size is "+length());
//					System.out.println(" ");
//					System.out.println("------------------");
//					System.out.println("next");
//					Element xx=next.content;
//					next.content.Print();
					
				}
			else {
				if(pos == 0) {
					Sequence oldSq = new Sequence(content,next);//copy this head content into tempSq
					this.content = elm; // set new content and next to the old one
					this.next =oldSq;
				}
				
				else {
					Sequence nowSq = this;
					for(int i = 0;i<pos-1;i++) {
						nowSq = nowSq.next;
					}
					Sequence nextSq = new Sequence(elm,nowSq.next);
					nowSq.next = nextSq;	
				}
			}
				
			}
		else {
			System.out.println("Out of range");
			System.exit(1);		
		}
	}
	public void delete(int pos) {
		if(pos >= 0 && pos <= length()) {
			if(pos == 0) {
				Sequence nowSq = this;
				this.content =nowSq.next.content;
				this.next =nowSq.next.next;
			}
			else if(pos == length()) {
				Sequence nowSq =this;
				while(nowSq.next.next!=null) {
					nowSq = nowSq.next;
				}
				nowSq.next = null;
			}
			else {
				Sequence nowSq =this;
				for(int i =0;i<pos-1;i++) {
					nowSq = nowSq.next;
				}
				nowSq.next.content = nowSq.next.next.content;
				nowSq.next.next = nowSq.next.next.next;
			}
		}
		else {
			System.out.println("Cannot delete, Out of range");
			System.exit(1);		
		}
		
	}
	
	public void Print() {
		if(content == null) {
			System.out.print(" []");
		}
		System.out.print("[ ");
		content.Print();
		System.out.print(" ");
		Sequence Iterator = next;
		while(Iterator !=null) {
			Iterator.content.Print();
			Iterator = Iterator.next;
			System.out.print(" ");
		}
		System.out.print("]");
	}
}
