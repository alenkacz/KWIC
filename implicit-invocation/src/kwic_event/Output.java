package kwic_event;

import java.util.List;

public class Output {
	
	LineStorage lines;

	public void print(LineStorage input, IndexStorage indexes) {
		lines = input;
		
		int size = indexes.getSize();
		
		for( int i = 0; i < size; ++i ) {
			// printing each line
			System.out.println(getLine(indexes.get(i)));
		}
	}
	
	public void searchAndPrint(String keyword, int context,LineStorage input, IndexStorage indexes) {
		Search search = new Search(input,indexes);
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
		
		String[] words = lines.getWords(index.getLineIndex());

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
