package kwic_adt;

import java.util.ArrayList;
import java.util.List;

public class LineStorage {
	
	List<String> _lines = new ArrayList<String>();
	
	public String getLine(int pos) {
		return _lines.get(pos);
	}

	public void addLine(String[] words) {
		boolean first = true;
		String res = "";
		
		for( String s : words ) {
			if( !first ) res += " ";
			else first = false;
			
			res += words;
		}
	}
	
	public void addLines(List<String> l) {
		_lines = l;
	}
	
	public int getLineCount() {
		return _lines.size();
	}

	public void addLine(String l) {
		_lines.add(l);
	}
	
	public List<String> getList() {
		return _lines;
	}
}
