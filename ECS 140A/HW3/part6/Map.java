
public class Map extends Sequence {
	
	public Map() {super();}

	//same as sequence.add. just need calculate position by getPosition function
	public void add(Pair p) {
		// TODO Auto-generated method stub
		if(p == null) {
			System.out.println("no pair item");
			System.exit(1);
		}
		add(p, getPosition(p));
			
	}
	public int getPosition(Pair p) {
		int position = 0;
		Sequence nowMap = this;
		if (nowMap.next == null) return 0;
		while(nowMap!=null) {
			
			if(p.getKey().Get()>=((Pair) nowMap.content).getKey().Get()) {
				position++;
			}
			nowMap =  nowMap.next;
		}
		return position;
	}
	
	public MapIterator begin() {
		if(content ==null) return null;
		if(dummy == null) dummy =new Sequence(new Pair(new MyChar('z'),new MyInteger(-1)),null); // if dummy is null, set dummy
		add(dummy.content,length());
		return (new MapIterator(content,this));//return this to show the content in that sequence.
	}
	public MapIterator end() {
		if(content == null) return null;
		Sequence nowSq = this;
		while(nowSq.next!=null) {
			nowSq = nowSq.next;
		}
		return(new MapIterator(nowSq.content,nowSq));	
	}
	
	public MapIterator find(MyChar key) {
		Sequence nowMp = this;
		while(nowMp!=null) {
			if(nowMp.content instanceof Pair){
				if(((Pair) nowMp.content).getKey().Get() == key.Get()) {
					return (new MapIterator(nowMp.content,nowMp));
				}
			}
			nowMp = nowMp.next;
		}
		return (new MapIterator(dummy.content,dummy));
		
	}
	

}
