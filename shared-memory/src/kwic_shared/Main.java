package kwic_shared;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Main {
	
	private static String _path = "";
	private static List<String> _linesOfWords = new ArrayList<String>();
	private static List<String> _result = new ArrayList<String>();
	private static int[][] _index;
	
	public static void main(String[] args) {
		
		if( args.length != 1 ) printErrorAndExit();
		_path = args[0];
		
		manageInput();
		doCircularShift();
		doAlphabetization();
		manageOutput();
	}

	private static void manageInput() {
		BufferedReader reader = null;
		
		try {
			reader =  new BufferedReader(new FileReader(_path));
		} catch( FileNotFoundException e ) {
			printErrorAndExit();
		}
		
		String line = "";
		try {
			while ((line = reader.readLine()) != null) {
	            // TODO do normalization - clean text from separators and devide it only with one space
	            if( line != "" ) // skip empty lines
	            	_linesOfWords.add(line); // and adding it to the list 
			}
		} catch( IOException e ) {
			printErrorAndExit();
		}
		
		buildIndex();
		
	}

	private static void doCircularShift() {
		int length = _linesOfWords.size();
		
		for( int u = 0; u < length; ++u ) {
			// iterating through all lines
			
			String[] words = _linesOfWords.get(u).split("\\s");
			
			for (int i = 0; i < words.length; i++) {
				String string = words[i] + " "; // "first word"
				for (int j = i+1; j < words.length; j++) { // words after "first word"
					string += words[j] + " ";
				}
				for (int j = 0; j < i; j++) { // words before "first" word
					string += words[j] + " ";
				}
				
				_result.add(string);
			}
		}
	}

	private static void doAlphabetization() {
		Collections.sort(_result, new IgnoreCaseComparator());
	}

	private static void manageOutput() {
		for( int i = 0; i < _result.size(); ++i ) {
			// printing each line
			System.out.println(_result.get(i));
		}
		
	}
	
	private static void buildIndex() {
		_index = new int[_linesOfWords.size()][];
		for( int i = 0; i < _linesOfWords.size(); ++i ) {
			String[] words = _linesOfWords.get(i).split("\\s");
			_index[i] = new int[words.length];
			
			_index[i][0] = 0; // first word is always on position 0 on the row
			for( int j = 1; j < words.length; ++j ) {
				// previous index + length of previous word + space
				_index[i][j] = _index[i][j-1]+words[j-1].length()+1;
			}
		}
	}

	private static void printErrorAndExit() {
		System.err.println("Run program with one parameter - path of the input file");
        System.exit(1);	
	}
}

class IgnoreCaseComparator implements Comparator<String> {
  public int compare(String s1, String s2) {
    return s1.compareToIgnoreCase(s2);
  }
}
