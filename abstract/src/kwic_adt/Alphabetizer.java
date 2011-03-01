package kwic_adt;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Alphabetizer {
	
	CircularShifter _shifter = null;
	LineStorage _storage = new LineStorage();
	
	public Alphabetizer( CircularShifter c ) {
		_shifter = c;
	}
	
	public void alpha() {
		List<String> lines = _shifter.getLines();
		Collections.sort(lines, new IgnoreCaseComparator());
		
		_storage.addLines(lines);
	}
	
	public String getLine(int index) {
		return _storage.getLine(index);
	}
	
	public int getLineCount() {
		return _storage.getLineCount();
	}
	
	
}

class IgnoreCaseComparator implements Comparator<String> {

	public int compare(String s1, String s2) {
		return s1.compareToIgnoreCase(s2);
	}
}
