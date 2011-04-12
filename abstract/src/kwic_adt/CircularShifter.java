package kwic_adt;

import java.util.ArrayList;
import java.util.List;

public class CircularShifter {
	private LineStorage _input;
	String[] _keywords;
	int[][] _shiftedIndex;

	public void shift(LineStorage storage) {
		_input = storage;
		_shiftedIndex = new int[storage.getWordsCount()][2];
		_keywords = new String[storage.getWordsCount()];
		
		int counter = 0;
		
		for( int i = 0; i < storage.getLineCount(); i++ ) {
			for( int j = 0; j < storage.getTotalWordsCount(i); j++ ) {
				// pair of a line number and word index
				String word = storage.getWord(i,j);
				if( !LineStorage.isNoiseWord(word) ) { // filter noise words
					_shiftedIndex[counter][0] = i;
					_shiftedIndex[counter][1] = j;
					_keywords[counter] = word;
					++counter;
				}
			}
		}
	}
	
	public int getWordsCount() {
		return _input.getWordsCount();
	}
	
	public String getKeyword(int index) {
		return _keywords[index];
	}

	public String getShiftedLine(Integer index) {
		return createShiftedLine(_shiftedIndex[index][0],_shiftedIndex[index][1]);
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
}
