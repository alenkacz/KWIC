package kwic_adt;

import java.util.ArrayList;
import java.util.List;

public class CircularShifter {
	LineStorage _shifted = new LineStorage();

	public void shift(LineStorage storage) {
		for( int i = 0; i < storage.getLineCount(); i++ ) {
			addLines(generateAllShifts(storage.getLine(i)));
		}
	}
	
	public void addLines(List<String> lines) {
		for( String l : lines ) {
			_shifted.addLine(l);
		}
	}
	
	public int getLineCount() {
		return _shifted.getLineCount();
	}
	
	public List<String> getLines() {
		return _shifted.getList();
	}
	
	private List<String> generateAllShifts(String line) {
		List<String> result = new ArrayList<String>();
		String[] words = line.split("\\s"); // spliting line with whitespaces
		for (int i = 0; i < words.length; i++) {
			String string = words[i] + " "; // "first word"
			for (int j = i+1; j < words.length; j++) { // words after "first word"
				string += words[j] + " ";
			}
			for (int j = 0; j < i; j++) { // words before "first" word
				string += words[j] + " ";
			}
			result.add(string);
		}

		return result;
	}
}
