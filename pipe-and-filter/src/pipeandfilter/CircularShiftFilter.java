package pipeandfilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vasek
 */
public class CircularShiftFilter extends Filter {

	CircularShiftFilter(Pipe input, Pipe output) {
		super(input, output);
	}

	protected void transform() throws IOException {
		List<String> lines = explodeString(getData());
		List<String> result = doCircularShift(lines);
		setData(implodeString(result));
	}

	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////

	private List<String> doCircularShift(List<String> lines) {
		List<String> result = new ArrayList<String>();		
		for (String line : lines) {
			result.addAll(getCircularShifts(line));
		}

		return result;
	}

	private List<String> getCircularShifts(String line) {
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
