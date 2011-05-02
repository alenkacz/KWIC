package kwic_adt;

import java.util.ArrayList;
import java.util.List;

public class CircularShifter {
	private LineStorage _input;
	IndexStorage _indexes;

	public void shift(LineStorage storage, IndexStorage index) {
		_input = storage;
		_indexes = index;
		
		for( int i = 0; i < storage.getLineCount(); i++ ) {
			for( int j = 0; j < storage.getTotalWordsCount(i); j++ ) {
				// pair of a line number and word index
				String word = storage.getWord(i,j);
				if( !LineStorage.isNoiseWord(word) ) { // filter noise words
					addNewKeyword(word, i, j);
				}
			}
		}
	}
	
	private void addNewKeyword(String word, int line, int wordIndex) {
		_indexes.add(word,line,wordIndex);
	}
	
	public int getWordsCount() {
		return _input.getWordsCount();
	}
	
	public String getKeyword(int index) {
		return _indexes.get(index).getKeyword();
	}

	public String getShiftedLine(Integer index) {
		return createShiftedLine(_indexes.get(index).getLineIndex(),_indexes.get(index).getWordIndex());
	}
	
	private String createShiftedLine(int line, int startIndex) {
		String res = "";
		
		String[] words = _input.getWords(line);

		res += words[startIndex] + " ";
		// from that word till the end
		for( int i = startIndex+1; i < words.length; i++ ) {
			res += words[i] + " ";
		}
		// from start till that word
		for( int i = 0; i < startIndex; i++ ) {
			res += words[i] + " ";
		}
		
		return res;
	}

	public String getWord(Integer index) {
		return _input.getWord(_indexes.get(index).getLineIndex(), _indexes.get(index).getWordIndex());
	}

	public int getLineNumber(Integer index) {
		return _indexes.get(index).getLineIndex();
	}

	public int getWordIndex(Integer index) {
		return _indexes.get(index).getWordIndex();
	}
	
	public String getRightContext(int line, int index, int context) {
		String res = "";
		if( index <  _input.getLine(line).length()) {
			String[] right = _input.getWordsToRightOf(line, index);
			int counter = 0;
			
			for( String s : right ) {
				if( counter < context ) {
					res += s + " ";
					counter++;
				}
			}
			
			if( right.length < context && (line+1) < _input.getLineCount() ) {
				//not enough words on this line
				res += getRightContext(line+1,0,context-right.length);
			}
		} else { // started at the end of line
			if ((line+1) < _input.getLineCount()) {
				res += getRightContext(line+1,0,context);
			}
		}
		
		return res;
	}

	public String getLeftContext(int line, int index, int context) {
		String res = "";
		if( index != 0 ) {
			String[] left = _input.getWordsToLeftOf(line, index);
			int counter = 0;
			
			for( int j = (left.length-1); j >= 0; j-- ) {
				if( counter < context ) {
					res = left[j] + " " + res;
					counter++;
				}
			}
			
			if( left.length < context && (line-1) >= 0 ) {
				//not enough words on this line
				res = getLeftContext(line-1,_input.getWords(line-1).length,context-left.length) 
					+ res;
			}
		} else {
			if( (line-1) >= 0 ) {
				res = getLeftContext(line-1,_input.getWords(line-1).length,context);
			}
		}

		return res;
	}
}
