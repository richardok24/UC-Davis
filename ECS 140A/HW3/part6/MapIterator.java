
public class MapIterator extends SequenceIterator {	
	public MapIterator() {}
	public MapIterator(Element pair, Sequence map) {
		super(pair, map);
	}
	public Pair get() {
		return (Pair)elm;
	}

}
