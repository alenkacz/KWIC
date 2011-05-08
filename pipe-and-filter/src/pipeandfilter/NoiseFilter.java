package pipeandfilter;

import java.io.IOException;

/**
 *
 * @author Vasek
 */
public class NoiseFilter extends Filter {

	private static final char[] mSeparators = {',', '.', ':', '-'};
	private static final String[] mNoiseWords = {"a", "the", "is"};

	NoiseFilter(Pipe input, Pipe output) {
		super(input, output);
	}

	protected void transform() throws IOException {
		String line = readLine();
		if (line.equals("\n")) {
			write("\n");
		} else {
			int pos = line.indexOf(' ');
			if (pos >= 0) {
				String firstWord = line.substring(0, pos);
				firstWord = cleanWord(firstWord);
				if (!isNoiseWord(firstWord)) {
					line = firstWord + line.substring(pos);
					write(line);
				}
			}
		}
	}

	private String cleanWord(String word) {
		char lastChar = word.charAt(word.length() - 1);
		for (char sep : mSeparators) {
			if (sep == lastChar) return word.substring(0, word.length() - 1);
		}

		return word;
	}

	private boolean isNoiseWord(String word) {
		for (String noise : mNoiseWords) {
			if (noise.equalsIgnoreCase(word)) return true;
		}

		return false;
	}
	
}
