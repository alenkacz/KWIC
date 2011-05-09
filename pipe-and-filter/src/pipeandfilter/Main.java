package pipeandfilter;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vasek
 */
public class Main {

	private enum Action {generateAndSearch, generate};

	// Settings:	
	private static final String mPath = "input2.txt";
	private static final Action mAction = Action.generateAndSearch;
	private static final String mKeyword = "you";
	private static final int mContext = 3;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
		try {
			Pipe p1 = new Pipe();
			Pipe p2 = new Pipe();
			Pipe p3 = new Pipe();
			Pipe p4 = new Pipe();

			new Thread(new Input(mPath, p1)).start();
			new Thread(new CircularShiftFilter(p1, p2)).start();
			new Thread(new NoiseFilter(p2, p3)).start();
			new Thread(new AlphabetizeFilter(p3, p4)).start();

			if (mAction == Action.generateAndSearch) {
				Pipe p5 = new Pipe();
				new Thread(new SearchFilter(p4, p5, mKeyword, mContext)).start();
				new Thread(new Output(p5)).start();
			} else {
				new Thread(new Output(p4)).start();
			}
		} catch (IOException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		} 

    }

}
