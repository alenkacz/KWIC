package kwic_event;
import java.util.Observable;
import java.util.Observer;


public class CircularShifter implements Observer {
	
	IndexStorage _indexes;

	public CircularShifter(IndexStorage indexes) {
		_indexes = indexes;
	}

	@Override
	public void update(Observable o, Object arg) {
		LineStorage storage = (LineStorage) o;
		EventType type = (EventType) arg;
		
		switch( type ) {
			case ADD: 
				break;
		}
	}

}
