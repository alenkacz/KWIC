package kwic_shared;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
	
	private static String _path = "";

	private static LineStorage _lines = new LineStorage();
	private static IndexStorage _indexStorage = new IndexStorage();
	
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
	            	_lines.addLine(line.trim()); // and adding it to the list 
	            }
			}
		} catch( IOException e ) {
			printErrorAndExit();
		}
	}

	private static void doCircularShift() {
		for( int i = 0; i < _lines.getLineCount(); i++ ) {
			for( int j = 0; j < _lines.getTotalWordsCount(i); j++ ) {
				// pair of a line number and word index
				String word = _lines.getWord(i,j);
				if( !LineStorage.isNoiseWord(word) ) { // filter noise words
					addNewKeyword(word, i, j);
				}
			}
		}
	}

	private static void doAlphabetization() {
		for( int j = 0; j < _indexStorage.getSize(); j++ ) {
			for( int i = 0; i < _indexStorage.getSize(); i++ ) {
				if( _indexStorage.get(j).getKeyword().compareToIgnoreCase(_indexStorage.get(i).getKeyword()) <= 0) {
					_indexStorage.insert(i,_indexStorage.get(j));
					_indexStorage.delete(j+1);
					break;
				}
			} 
		}
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
	////////////////////////////////////////  SHIFTER ///////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static void addNewKeyword(String word, int line, int wordIndex) {
		_indexStorage.add(word,line,wordIndex);
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////  OUTPUT ///////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static void printOutAllLines() {
		int size = _indexStorage.getSize();
		
		for( int i = 0; i < size; ++i ) {
			// printing each line
			System.out.println(getLine(_indexStorage.get(i)));
		}
	}
	
	private static void printOutContext() {
		List<String> res = doSearch();
		
		for( String s : res ) {
			System.out.println(s);
		}
	}
	
	private static String getLine(Index index) {
		String res = "";
		int wordIndex = index.getWordIndex();
		
		String[] words = _lines.getWords(index.getLineIndex());

		res += words[wordIndex] + " ";
		// from that word till the end
		for( int i = wordIndex+1; i < words.length; i++ ) {
			res += words[i] + " ";
		}
		// from start till that word
		for( int i = 0; i < wordIndex; i++ ) {
			res += words[i] + " ";
		}
		
		return res;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////  SEARCH  ///////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static List<String> doSearch() {
		List<Integer> indexes = findWordIndexes(_keyword,0,_indexStorage.getSize());
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
		
		while( index != 0 && start != 0 && _indexStorage.get(--start).getKeyword().toLowerCase()
				.equals(text.toLowerCase()) ) {
			res.add(start);
		}
		
		start = index;
		
		while( index != (_indexStorage.getSize() - 1) && 
				_indexStorage.get(++start).getKeyword().toLowerCase().equals(text.toLowerCase()) ) {
			res.add(start);
		}
		
		return res;
	}

	/**
	 * Binary search
	 */
	private static int findIndexOfString( String text, int left, int right ) {
		if( left > right ) return -1;
		int middle = (left + right)/2;
		
		if( text.toLowerCase().compareTo(_indexStorage.get(middle).getKeyword().toLowerCase()) > 0 ) { // greater
			return findIndexOfString(text,middle+1,right);
		} else if( text.toLowerCase().compareTo(_indexStorage.get(middle).getKeyword().toLowerCase()) < 0 ) { // lower
			return findIndexOfString(text,left,middle-1);
		} else { // the same
			return middle;
		}
	}
	
	private static String getContextForIndex(int i) {
		int line = _indexStorage.get(i).getLineIndex();
		int index = _indexStorage.get(i).getWordIndex();
		String word =  _indexStorage.get(i).getKeyword();;
		String res = getLeftContext(line,index,_context);
		res += word + " ";
		res += getRightContext(line,index,_context);
		
		return res;
	}
	
	private static String getRightContext(int line, int index, int context) {
		String res = "";
		if( index <  _lines.getLine(line).length()) {
			String[] right = _lines.getWordsToRightOf(line, index);
			int counter = 0;
			
			for( String s : right ) {
				if( counter < context ) {
					res += s + " ";
					counter++;
				}
			}
			
			if( right.length < context && (line+1) < _lines.getLineCount() ) {
				//not enough words on this line
				res += getRightContext(line+1,0,context-right.length);
			}
		} else { // started at the end of line
			if ((line+1) < _lines.getLineCount()) {
				res += getRightContext(line+1,0,context);
			}
		}
		
		return res;
	}

	private static String getLeftContext(int line, int index, int context) {
		String res = "";
		if( index != 0 ) {
			String[] left = _lines.getWordsToLeftOf(line, index);
			int counter = 0;
			
			for( int j = (left.length-1); j >= 0; j-- ) {
				if( counter < context ) {
					res = left[j] + " " + res;
					counter++;
				}
			}
			
			if( left.length < context && (line-1) >= 0 ) {
				//not enough words on this line
				res = getLeftContext(line-1,_lines.getWords(line-1).length,context-left.length) 
					+ res;
			}
		} else {
			if( (line-1) >= 0 ) {
				res = getLeftContext(line-1,_lines.getWords(line-1).length,context);
			}
		}

		return res;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////  HELPERS ///////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////

	private static void printErrorAndExit() {
		System.err.println("Run program with one parameter - path of the input file");
        System.exit(1);	
	}
}
