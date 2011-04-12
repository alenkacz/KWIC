package kwic_shared;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.sun.tools.javac.util.Pair;

public class Main {
	
	private static String _path = "";
	private static List<String> _linesOfWords = new ArrayList<String>();
	private static List<String> _result = new ArrayList<String>();
	private static int _wordsCount = 0;
	private static int[][] _index;
	private static int[][] _shiftedIndex;
	private static String[] _keywords;
	private static Integer[] _alphaIndex;
	private static final String[] _noiseWords = {"a","the","is"};
	private static final char[] _separators = {',','.',':','-'};
	
	private enum Action {search, print};
	
	///////////////// Settings
	private static final Action _action = Action.search;
	private static final String _keyword = "into";
	private static final int _context = 3;
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////  MODULES ///////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) {
		
		if( args.length != 1 ) printErrorAndExit();
		_path = args[0];
		
		manageInput();
		doCircularShift();
		doAlphabetization();
		manageOutput();
	}

	private static void manageInput() {
		BufferedReader reader = null;
		
		try {
			reader =  new BufferedReader(new FileReader(_path));
		} catch( FileNotFoundException e ) {
			printErrorAndExit();
		}
		
		String line = "";
		try {
			while ((line = reader.readLine()) != null) {
	            if( !line.equals("") ) { // skip empty lines 
	            	_linesOfWords.add(cleanWord(line.trim())); // and adding it to the list 
	            }
			}
		} catch( IOException e ) {
			printErrorAndExit();
		}
		
		buildIndex();
		
	}

	private static void doCircularShift() {
		_shiftedIndex = new int[_wordsCount][2];
		_keywords = new String[_wordsCount];
		
		int counter = 0;
		
		for( int i = 0; i < _index.length; i++ ) {
			for( int j = 0; j < _index[i].length; j++ ) {
				// pair of a line number and word index
				String word = getWord(i,_index[i][j]);
				if( !isNoiseWord(word) ) { // filter noise words
					_shiftedIndex[counter][0] = i;
					_shiftedIndex[counter][1] = _index[i][j];
					_keywords[counter] = word;
					++counter;
				}
			}
		}
	}

	private static void doAlphabetization() {
		_alphaIndex = new Integer[_wordsCount];
		for( int i = 0; i < _wordsCount; i++ ) { _alphaIndex[i] = i; }
		
		Arrays.sort(_alphaIndex,new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				return _keywords[o1].compareToIgnoreCase(_keywords[o2]);
			}
			
		});
	}

	private static void manageOutput() {
		switch(_action) {
			case print:
				printOutAllLines();
				break;
			case search:
				printOutContext();
				break;
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////  OUTPUT ///////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static void printOutAllLines() {
		int index = 0;
		for( int i = 0; i < _alphaIndex.length; i++ ) {
			index = _alphaIndex[i];
			System.out.println(getShiftedLine(_shiftedIndex[index][0],_shiftedIndex[index][1]));
		}	
	}
	
	private static void printOutContext() {
		List<String> res = doSearch();
		
		for( String s : res ) {
			System.out.println(s);
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////  SEARCH  ///////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static List<String> doSearch() {
		List<Integer> indexes = findWordIndexes(_keyword,0,_alphaIndex.length);
		List<String> res = new ArrayList<String>();
		
		if( indexes != null ) {
			for( Integer i : indexes ) {
				res.add(getContextForIndex(i));
			}
		} else {
			// nothing found
			res.add("Nothing found");
		}
		
		return res;
	}

	private static List<Integer> findWordIndexes(String text,int left, int right) {
		List<Integer> indexes = new ArrayList<Integer>();
		int index = findIndexOfString(text,left,right);
		
		if( index == -1 ) {
			return null; // not found
		} else {
			indexes.add(index);
			List<Integer> around = searchPreviousAndNext(text,index);
			
			for( int i : around ) {
				indexes.add(i);
			}
		}
		
		return indexes;
	}
	
	private static List<Integer> searchPreviousAndNext(String text, int index) {
		List<Integer> res = new ArrayList<Integer>();
		int start = index;
		
		while( index != 0 && getWord(_alphaIndex[--start]).toLowerCase().equals(text.toLowerCase()) ) {
			res.add(start);
		}
		
		start = index;
		
		while( index != (_alphaIndex.length - 1) && getWord(_alphaIndex[++start]).toLowerCase().equals(text.toLowerCase()) ) {
			res.add(start);
		}
		
		return res;
	}

	/**
	 * Binary search
	 */
	private static int findIndexOfString( String text, int left, int right ) {
		if( left >= right ) return -1;
		int middle = (left + right)/2;
		
		if( text.toLowerCase().compareTo(getWord(_alphaIndex[middle]).toLowerCase()) > 0 ) { // greater
			return findIndexOfString(text,middle+1,right);
		} else if( text.toLowerCase().compareTo(getWord(_alphaIndex[middle]).toLowerCase()) < 0 ) { // lower
			return findIndexOfString(text,left,middle-1);
		} else { // the same
			return middle;
		}
	}
	
	private static String getContextForIndex(int i) {
		int line = _shiftedIndex[_alphaIndex[i]][0];
		int index = _shiftedIndex[_alphaIndex[i]][1];
		String word =  getWord(line,index);
		String res = getLeftContext(line,index,_context);
		res += word + " ";
		res += getRightContext(line,index+word.length()+1,_context);
		
		return res;
	}
	
	private static String getRightContext(int line, int index, int context) {
		String res = "";
		if( index <  _linesOfWords.get(line).length()) {
			String[] right = _linesOfWords.get(line).substring(index).split("\\s");
			int counter = 0;
			
			for( String s : right ) {
				if( counter < context ) {
					res += s + " ";
					counter++;
				}
			}
			
			if( right.length < context && (line+1) < _linesOfWords.size() ) {
				//not enough words on this line
				res += getRightContext(line+1,0,context-right.length);
			}
		} else { // started at the end of line
			if ((line+1) < _linesOfWords.size()) {
				res += getRightContext(line+1,0,context);
			}
		}
		
		return res;
	}

	private static String getLeftContext(int line, int index, int context) {
		String res = "";
		if( index != 0 ) {
			String[] left = _linesOfWords.get(line).substring(0,index).split("\\s");
			int counter = 0;
			
			for( int j = (left.length-1); j >= 0; j-- ) {
				if( counter < context ) {
					res = left[j] + " " + res;
					counter++;
				}
			}
			
			if( left.length < context && (line-1) >= 0 ) {
				//not enough words on this line
				res = getLeftContext(line-1,_linesOfWords.get(line-1).length(),context-left.length) 
					+ res;
			}
		} else {
			if( (line-1) >= 0 ) {
				res = getLeftContext(line-1,_linesOfWords.get(line-1).length(),context);
			}
		}

		return res;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////  HELPERS ///////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////

	private static boolean isNoiseWord(String word) {
		for( String s : _noiseWords ) {
			if( word.toLowerCase().equals(s) ) return true;
		}
		return false;
	}
	
	/*
	 * Cleans line from separators (removes commas etc.) and separates it only with one space
	 */
	private static String cleanWord(String s) {
		String res = "";
		String[] parts = s.split("\\s"); // split with whitespaces
		boolean first = true;
		
		for( String part : parts ) {
			String temp = part;
			for( char sep : _separators ) {
				// looking for separators in the beginning and in the end
				if( part.charAt(0) == sep ) { //first one
					temp = part.substring(1);
				}
				
				if( part.charAt(part.length()-1) == sep ) { //last one
					temp = part.substring(0,part.length()-1);
				}
			}
			if( !first ) { res += " "; }
			first = false;
			res += temp;
		}
		return res;
	}
	
	private static String getWord( int index ) {
		int line = _shiftedIndex[index][0];
		int i = _shiftedIndex[index][1];
		return getWord(line,i);
	}
	
	private static String getWord( int line, int index ) {
		String l = _linesOfWords.get(line);
		String substr = l.substring(index);
		return substr.split("\\s")[0];
	}
	
	private static String getShiftedLine(int line, int startIndex) {
		String l = _linesOfWords.get(line);
		String res = "";
		
		res = l.substring(startIndex);
		res += " "; 
		res += l.substring(0,startIndex);
		
		return res;
	}
	
	/**
	 * Builds index of original input text - each line has a set of word indexes, which indicates
	 * indexes on each line where that word starts
	 */
	private static void buildIndex() {
		_index = new int[_linesOfWords.size()][];
		for( int i = 0; i < _linesOfWords.size(); ++i ) {
			String[] words = _linesOfWords.get(i).split("\\s");
			_index[i] = new int[words.length];
			
			_index[i][0] = 0; // first word is always on position 0 on the row
			if( !isNoiseWord(words[0]) )++_wordsCount; // counter of words in the whole document
			for( int j = 1; j < words.length; ++j ) {
				// previous index + length of previous word + space
				_index[i][j] = _index[i][j-1]+words[j-1].length()+1;
				if( !isNoiseWord(words[j]) ) {
					++_wordsCount;
				}
			}
		}
	}

	private static void printErrorAndExit() {
		System.err.println("Run program with one parameter - path of the input file");
        System.exit(1);	
	}
}

class IgnoreCaseComparator implements Comparator<String> {
  public int compare(String s1, String s2) {
    return s1.compareToIgnoreCase(s2);
  }
}
