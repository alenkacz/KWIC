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
		String origLine = readLine();
		if (origLine.equals("\n")) {
			write("\n");
		} else {
			//System.out.println("R: " + origLine);
			for (String line : getCircularShifts(origLine)) {
				//System.out.println("C: " + line);
				write(line + '\n');
			}
		}
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
