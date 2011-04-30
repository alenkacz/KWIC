package kwic_adt;

import java.util.ArrayList;
import java.util.List;

public class Search {
	
	Alphabetizer _alpha;
	String _keyword;
	int _context;
	
	public Search( Alphabetizer alpha ) {
		_alpha = alpha;
	}

	public List<String> doSearch(String keyword, int context) {
		_keyword = keyword;
		_context = context;
		
		List<Integer> indexes = findWordIndexes(keyword,0,_alpha.getLineCount());
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
		
		while( index != 0 && start != 0 && _alpha.getWord(--start).toLowerCase().equals(text.toLowerCase()) ) {
			res.add(start);
		}
		
		start = index;
		
		while( index != (_alpha.getLineCount() - 1) && 
				_alpha.getWord(++start).toLowerCase().equals(text.toLowerCase()) ) {
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
		
		if( text.toLowerCase().compareTo(_alpha.getWord(middle).toLowerCase()) > 0 ) { // greater
			return findIndexOfString(text,middle+1,right);
		} else if( text.toLowerCase().compareTo(_alpha.getWord(middle).toLowerCase()) < 0 ) { // lower
			return findIndexOfString(text,left,middle-1);
		} else { // the same
			return middle;
		}
	}
	
	private String getContextForIndex(int i) {
		int line = _alpha.getLineNumber(i);
		int index = _alpha.getWordIndex(i);
		String word =  _alpha.getWord(i);
		String res = _alpha.getLeftContext(line,index,_context);
		res += word + " ";
		res += _alpha.getRightContext(line,index,_context);
		
		return res;
	}

}
