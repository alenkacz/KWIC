package kwic_adt;

import java.util.ArrayList;
import java.util.List;

public class LineStorage {
	
	List<String> _lines = new ArrayList<String>();
	private int _wordsCount = 0;
	
	private static final char[] _separators = {',','.',':','-'};
	private static final String[] _noiseWords = {"a","the","is"};
	
	public String getLine(int pos) {
		return _lines.get(pos);
	}
	
	public int getLineCount() {
		return _lines.size();
	}
	
	public int getTotalWordsCount(int index) {
		return _lines.get(index).split("\\s").length;
	}
	
	public int getWordsCount() {
		return _wordsCount;
	}
	
	public int getNotNoiseWordsCount(String s) {
		String[] words = s.split("\\s");
		int counter = 0;
		
		for( String word : words ) {
			if( !isNoiseWord(word) ) counter++;
		}
		
		return counter;
	}

	public void addLine(String l) {
		_lines.add(cleanLine(l));
		_wordsCount += getNotNoiseWordsCount(l);
	}
	
	public List<String> getList() {
		return _lines;
	}
	
	public String getWord( int line, int index ) {
		return _lines.get(line).split("\\s")[index];
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////  HELPERS ///////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////

	public static boolean isNoiseWord(String word) {
		for( String s : _noiseWords ) {
			if( word.toLowerCase().equals(s) ) return true;
		}
		return false;
	}
	
	/*
	 * Cleans line from separators (removes commas etc.) and separates it only with one space
	 */
	private String cleanLine(String s) {
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

	public String[] getWords(int line) {
		return _lines.get(line).split("\\s");
	}
}
