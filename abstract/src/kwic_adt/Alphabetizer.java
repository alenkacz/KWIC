package kwic_adt;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Alphabetizer {
	
	CircularShifter _shifter = null;
	Integer[] _alphaIndex;
	
	public Alphabetizer( CircularShifter c ) {
		_shifter = c;
	}
	
	public void alpha() {
		_alphaIndex = new Integer[_shifter.getWordsCount()];
		
		for( int i = 0; i < _shifter.getWordsCount(); i++ ) { _alphaIndex[i] = i; }
		
		Arrays.sort(_alphaIndex,new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				return _shifter.getKeyword(o1).compareToIgnoreCase(_shifter.getKeyword(o2));
			}
			
		});
	}
	
	public int getLineCount() {
		return _shifter.getWordsCount();
	}
	
	public String getLine(int i) {
		return _shifter.getShiftedLine(_alphaIndex[i]);
	}
}
