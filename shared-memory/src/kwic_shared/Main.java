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
	private static final Action action = Action.search;
	private static final String keyword = "deaths";
	private static final int context = 3;
	
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
	            // TODO do normalization - clean text from separators and devide it only with one space
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
		switch(action) {
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
		List<Integer> indexes = findWordIndexes();
		List<String> res = new ArrayList<String>();
		
		//for( Integer i : indexes ) {
			res.add(getContextForIndex(8));
		//}
		
		return res;
	}

	private static List<Integer> findWordIndexes() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private static String getContextForIndex(int i) {
		int line = _shiftedIndex[i][0];
		int index = _shiftedIndex[i][1];
		String word =  getWord(line,index);
		String res = getLeftContext(line,index,context);
		res += word + " ";
		res += getRightContext(line,index+word.length()+1,context);
		
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
		String[] left = _linesOfWords.get(line).substring(0,index).split("\\s");
		String res = "";
		int counter = 0;
		
		for( String s : left ) {
			if( counter < context ) {
				res += s + " ";
				counter++;
			}
		}
		
		if( left.length < context && (line-1) >= 0 ) {
			//not enough words on this line
			res += getLeftContext(line-1,_linesOfWords.get(line-1).split("\\s").length,context-left.length);
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
	
	private static String cleanWord(String s) {
		String res = "";
		String[] parts = s.split("\\s");
		boolean first = true;
		
		for( String part : parts ) {
			String temp = part;
			for( char sep : _separators ) {
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
				if( !isNoiseWord(words[j]) )++_wordsCount;
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
