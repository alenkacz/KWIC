package kwic_adt;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Input {

	public void parse(LineStorage storage, String path) {
		BufferedReader reader = null;
		
		try {
			reader =  new BufferedReader(new FileReader(path));
		} catch( FileNotFoundException e ) {
			printErrorAndExit();
		}
		
		String line = "";
		try {
			while ((line = reader.readLine()) != null) {
	            if( !line.equals("") ) { // skip empty lines 
	            	storage.addLine(line.trim()); // and adding it to the list 
	            }
			}
		} catch( IOException e ) {
			printErrorAndExit();
		}
	}
	
	private static void printErrorAndExit() {
		System.err.println("Run program with one parameter - path of the input file");
        System.exit(1);	
	}
}
