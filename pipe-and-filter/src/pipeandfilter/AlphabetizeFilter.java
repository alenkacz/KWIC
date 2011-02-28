package pipeandfilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Vasek
 */
public class AlphabetizeFilter extends Filter {

	AlphabetizeFilter(Pipe input, Pipe output) {
		super(input, output);
	}

	protected void transform() throws IOException {
		List<String> lines = explodeString(getData());
		Collections.sort(lines, new IgnoreCaseComparator());
		setData(implodeString(lines));
	}

}

class IgnoreCaseComparator implements Comparator<String> {

	public int compare(String s1, String s2) {
		return s1.compareToIgnoreCase(s2);
	}
}
