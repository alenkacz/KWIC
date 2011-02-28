package kwic_adt;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Line {
	private LinkedList<String> _words = new LinkedList<String>();
	
	public Line( String[] words ) {
		for( String s : words ) {
			_words.addLast(s);
		}
	}
	
	public List<Line> generateAllShifts() {
		List<Line> res = new ArrayList<Line>();
		for( int i = 0; i < _words.size(); ++i ) {
			String first = _words.removeFirst();
			_words.addLast(first);
			res.add(new Line((String[])_words.toArray()));
		}
		
		return res;
	}
}
