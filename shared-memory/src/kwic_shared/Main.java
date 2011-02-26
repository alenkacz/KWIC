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
	private static List<String[]> _linesOfWords = new ArrayList<String[]>();
	private static List<String> _result = new ArrayList<String>();
	
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
	            String[] words = line.split("\\s"); // spliting line with whitespaces
	            _linesOfWords.add(words); // and adding it to the list 
			}
		} catch( IOException e ) {
			printErrorAndExit();
		}
		
	}

	private static void doCircularShift() {
		int length = _linesOfWords.size();
		List<String> helper = null;
		
		for( int i = 0; i < length; ++i ) {
			// iterating through all lines
			helper = new ArrayList<String>();
			
			for( int j = 0; j < _linesOfWords.get(i).length; ++j ) {
				// array to arraylist
				helper.add(j,_linesOfWords.get(i)[j]);
			}
			
			for( int j = 0; j < _linesOfWords.get(i).length; ++j ) {
				// do shifting and saving it
				String helperWord = helper.get(0); // save first word
				helper.remove(0);
				helper.add(helper.size(), helperWord);
				
				// building result string
				String res = "";
				for( int k = 0; k < helper.size(); k++ ) {
					res += helper.get(k);
					if( k != helper.size() - 1 ) { res += " "; } // adding space, not after last one
				}
				_result.add(res);
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
