
class SequenceIterator{
	private Element elm;
	private Sequence sq;

	
	public SequenceIterator() {}
	public SequenceIterator(Element elm, Sequence sq) {
		super();
		this.elm = elm;
		this.sq = sq;
	}
	public SequenceIterator(SequenceIterator SqIt) {
		setElm(SqIt.get());
		setSq(SqIt.getSq());
	}
	
	public Element get() {
		return elm;
	}
	public void setElm(Element elm) {
		this.elm = elm;
	}
	public Sequence getSq() {
		return sq;
	}
	public void setSq(Sequence sq) {
		this.sq = sq;
	}
	public boolean equal (SequenceIterator other) {return (other.get()==elm);}
	public SequenceIterator advance() {
		if(sq.rest() == null)
			return null;
		elm = sq.rest().first();
		sq = sq.rest();
		return (new SequenceIterator(elm,sq));
	}
		
}
