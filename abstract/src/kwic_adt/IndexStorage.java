package kwic_adt;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


public class IndexStorage extends Observable {
	List<Index> _indexes = new ArrayList<Index>();
	
	public void add(String word, int line, int wordIndex) {
		_indexes.add(new Index(word,line,wordIndex));
	}
	
	public int getSize() {
		return _indexes.size();
	}
	
	public int getLastIndex() {
		return _indexes.size() - 1;
	}

	public Index get(int index) {
		return _indexes.get(index);
	}

	public void insert(int i, Index indexObject) {
		_indexes.add(i,indexObject);
	}
	
	public void delete( int index ) {
		_indexes.remove(index);
	}
}
