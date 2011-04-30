package kwic_event;
import java.util.Observable;
import java.util.Observer;


public class CircularShifter implements Observer {
	
	IndexStorage _indexes;
	LineStorage _lines;

	public CircularShifter(IndexStorage indexes) {
		_indexes = indexes;
	}

	@Override
	public void update(Observable o, Object arg) {
		_lines = (LineStorage) o;
		EventType type = (EventType) arg;
		
		switch( type ) {
			case ADD:
				doShift();
				break;
		}
	}

	private void doShift() {
		int index = _lines.getLastIndex();
		for( int j = 0; j < _lines.getTotalWordsCount(index); j++ ) {
			// pair of a line number and word index
			String word = _lines.getWord(index,j);
			if( !LineStorage.isNoiseWord(word) ) { // filter noise words
				addNewKeyword(word, index, j);
			}
		}
	}

	private void addNewKeyword(String word, int line, int wordIndex) {
		_indexes.add(word,line,wordIndex);
	}

}
