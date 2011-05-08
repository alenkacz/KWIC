package pipeandfilter;

import java.io.IOException;

/**
 *
 * @author Vasek
 */
public class SearchFilter extends Filter {

	private String searchWord;

	private int maxWordCount;

	SearchFilter(Pipe input, Pipe output, String search, int wordsCount) {
		super(input, output);		
		searchWord = search;
		maxWordCount = wordsCount;
	}

	protected void transform() throws IOException {
		String line = readLine();
		//System.out.print("S: " + line);
		if (line.equals("\n")) {
			write("\n");
		} else {
			int pos = line.indexOf(' ');
			if (pos >= 0) {
				String firstWord = line.substring(0, pos);
				if (firstWord.equalsIgnoreCase(searchWord)) {
					int lineBreak = Integer.parseInt(line.substring(line.lastIndexOf(' ')).trim());
					String newLine = "";
					String[] words = line.split("\\s");
					int wordsCount = words.length - 1; // without index number
					int before = wordsCount - lineBreak;
					if (wordsCount - before > maxWordCount) before = wordsCount - maxWordCount;
					for (int i = before; i < words.length - 1; i++) {
						newLine += words[i] + ' ';
					}
					int after = wordsCount - lineBreak;
					if (after > maxWordCount) after = maxWordCount;
					for (int i = 0; i <= after; i++) {
						newLine += words[i] + ' ';
					}
					write(newLine + '\n');
				}
			}
		}
	}
	
}
