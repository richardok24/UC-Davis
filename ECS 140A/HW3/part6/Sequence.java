
public class Sequence extends Element{
	protected Element content;
	protected Sequence next;
	//private int index = -1; // when empty no index.
	protected Sequence dummy; //point to the element after end;
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
	
	public SequenceIterator begin() {
		if(content ==null) return null;
		if(dummy == null) dummy = new Sequence(new MyInteger(-1),null); // if dummy is null, set dummy
		add(dummy,length());
		return (new SequenceIterator(content,this));//return this to show the content in that sequence.
	}
	public SequenceIterator end() {
		if(content == null) return null;
		Sequence nowSq = this;
		while(nowSq.next!=null) {
			nowSq = nowSq.next;
		}
		return(new SequenceIterator(nowSq.content,nowSq));
		
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
	
	public Element index(int pos) {
		Element TheContent = null;
		if(pos >=0 || pos <=length()) {
			Sequence nowSq = this;
			for(int i =0;i<pos;i++) {
				nowSq = nowSq.next;
			}
			TheContent = nowSq.content;
		}
		else {
			System.err.println("Out of range");
			System.exit(1);
		}
		return TheContent;
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

	
	
	public void appendS(Sequence Sq) {
		Sequence temp = Sq;
		while(temp!=null) {
			this.add(temp.content, this.length());
			temp = temp.next;
		}
	}
	
	public Sequence flatten() {
		if (this == null) return null;
		Sequence flattenSq = new Sequence();
		Sequence nowSq = this;
		while(nowSq!=null) {
			if(nowSq.content instanceof Sequence) {
				flattenSq.appendS(((Sequence)nowSq.content).flatten());
			}
			else
				flattenSq.add(nowSq.content,flattenSq.length() );
			
			nowSq = nowSq.next;
		}
		return flattenSq;	
	}
	
	public Sequence copy(){
		Sequence CopySq = new Sequence();
		Sequence nowSq = this;
		while(nowSq!=null) {
			if(nowSq.content instanceof Sequence) {
				CopySq.add(((Sequence) nowSq.content).copy(),CopySq.length());
			}
			else if(nowSq.content instanceof MyChar) {
				CopySq.add(new MyChar(((MyChar) nowSq.content).Get()),CopySq.length());	
			}
			else if(nowSq.content instanceof MyInteger) {
				CopySq.add(new MyInteger(((MyInteger) nowSq.content).Get()), CopySq.length());
			}
			nowSq = nowSq.next;
		}
		return CopySq;
	}

}
