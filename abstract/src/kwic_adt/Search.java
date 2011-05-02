package kwic_adt;

import java.util.ArrayList;
import java.util.List;

public class Search {
	LineStorage _lines;
	IndexStorage _indexStorage;
	String _keyword;
	int _context;
	
	public Search( LineStorage lines, IndexStorage indexes ) {
		_lines = lines;
		_indexStorage = indexes;
	}

	public List<String> doSearch(String keyword, int context) {
		_keyword = keyword;
		_context = context;
		
		List<Integer> indexes = findWordIndexes(keyword,0,_indexStorage.getSize());
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

	private List<Integer> findWordIndexes(String text,int left, int right) {
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
	
	private List<Integer> searchPreviousAndNext(String text, int index) {
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
	private int findIndexOfString( String text, int left, int right ) {
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
	
	private String getContextForIndex(int i) {
		int line = _indexStorage.get(i).getLineIndex();
		int index = _indexStorage.get(i).getWordIndex();
		String word =  _indexStorage.get(i).getKeyword();;
		String res = getLeftContext(line,index,_context);
		res += word + " ";
		res += getRightContext(line,index,_context);
		
		return res;
	}
	
	public String getRightContext(int line, int index, int context) {
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

	public String getLeftContext(int line, int index, int context) {
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

}
