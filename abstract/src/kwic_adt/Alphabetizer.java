package kwic_adt;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Alphabetizer {
	
	IndexStorage _indexes = null;
	
	public void alpha(IndexStorage index) {
		_indexes = index;
		
		for( int j = 0; j < _indexes.getSize(); j++ ) {
			for( int i = 0; i < _indexes.getSize(); i++ ) {
				if( _indexes.get(j).getKeyword().compareToIgnoreCase(_indexes.get(i).getKeyword()) <= 0) {
					_indexes.insert(i,_indexes.get(j));
					_indexes.delete(j+1);
					break;
				}
			} 
		}
	}
}
