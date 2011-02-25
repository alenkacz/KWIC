package kwic_shared;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Main {
	
	private static String _path = "";
	private static List<String[]> _linesOfWords = new ArrayList<String[]>();
	
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
		// TODO Auto-generated method stub
		
	}

	private static void doAlphabetization() {
		// TODO Auto-generated method stub
		
	}

	private static void manageOutput() {
		// TODO Auto-generated method stub
		
	}

	private static void printErrorAndExit() {
		System.err.println("Run program with one parameter - path of the input file");
        System.exit(1);	
	}
}
