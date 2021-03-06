package pipeandfilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Vasek
 */
public class AlphabetizeFilter extends Filter {

	List<String> lines = new ArrayList<String>();

	AlphabetizeFilter(Pipe input, Pipe output) {
		super(input, output);
	}

	protected void transform() throws IOException {
		String line = readLine();
		if (line.equals("\n")) {
			Collections.sort(lines, new IgnoreCaseComparator());
			for (String out : lines) {
				write(out);
			}
			write("\n");
		} else {
			lines.add(line);
		}
	}

}

class IgnoreCaseComparator implements Comparator<String> {

	public int compare(String s1, String s2) {
		return s1.compareToIgnoreCase(s2);
	}
}
