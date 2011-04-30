package kwic_event;
import java.util.Observable;
import java.util.Observer;


public class Alphabetizer implements Observer {

	IndexStorage _indexStorage;
	
	@Override
	public void update(Observable o, Object arg) {
		_indexStorage = (IndexStorage) o;
		EventType type = (EventType) arg;
		
		switch( type ) {
			case ADD:
				doAlphabetize();
				break;
		}
	}

	private void doAlphabetize() {
		int lastIndex = _indexStorage.getLastIndex();
		Index indexObject = _indexStorage.get(lastIndex);
		for( int i = 0; i < lastIndex; i++ ) {
			if( indexObject.getKeyword().compareToIgnoreCase(_indexStorage.get(i).getKeyword()) <= 0) {
				_indexStorage.insert(i,indexObject);
				_indexStorage.delete(lastIndex+1);
				break;
			}
		}        
	}

}
