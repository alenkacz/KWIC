package kwic_adt;

import java.util.ArrayList;
import java.util.List;

public class LineStorage {
	
	List<Line> lines = new ArrayList<Line>();

	public void addLine(String s) {
		
	}
	
	public void addWord(String s) {
		
	}
	
	public String getLine() {
		return "";
	}
	
	public String getWord() {
		return "";
	}

	public void addLine(String[] words) {
		lines.add(new Line(words));
	}
}
