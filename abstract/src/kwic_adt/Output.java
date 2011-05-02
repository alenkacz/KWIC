package kwic_adt;

import java.util.List;

public class Output {
	
	LineStorage _lines;
	IndexStorage _indexes;

	public void print(LineStorage storage ,IndexStorage index) {
		_lines = storage;
		_indexes = index;
		
		int size = index.getSize();
		
		for( int i = 0; i < size; ++i ) {
			// printing each line
			System.out.println(getLine(_indexes.get(i)));
		}
	}

	public void searchAndPrint(LineStorage storage, IndexStorage index, String keyword, int context) {
		Search search = new Search(storage,index);
		List<String> res = search.doSearch(keyword, context);
		
		for( String s : res ) {
			System.out.println(s);
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////

	private String getLine(Index index) {
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
}
