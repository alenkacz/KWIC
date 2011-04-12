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
		
		String a = "";
	}
	
	public int getLineCount() {
		return _shifter.getWordsCount();
	}
	
	public String getLine(int i) {
		return _shifter.getShiftedLine(_alphaIndex[i]);
	}
	
	public String getWord( int i ) {
		return _shifter.getWord(_alphaIndex[i]);
	}

	public int getLineNumber(int i) {
		return _shifter.getLineNumber(_alphaIndex[i]);
	}

	public int getWordIndex(int i) {
		return _shifter.getWordIndex(_alphaIndex[i]);
	}

	public String getLeftContext(int line, int index, int _context) {
		return _shifter.getLeftContext(line,index,_context);
	}

	public String getRightContext(int line, int i, int _context) {
		return _shifter.getRightContext(line,i,_context);
	}
}
