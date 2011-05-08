package pipeandfilter;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vasek
 */
public class Main {

	private static String mPath = "input.txt";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

		/*
			- nebylo by lepší, kdyby se všechno spouštělo na konci konstruktoru ->
			nepsalo by se run a bylo by zajištěný správný pořadí
		 */
		
		try {
			Pipe p1 = new Pipe();
			Pipe p2 = new Pipe();
			Pipe p3 = new Pipe();
			Pipe p4 = new Pipe();

			new Thread(new Input(mPath, p1)).start();
			new Thread(new CircularShiftFilter(p1, p2)).start();
			new Thread(new NoiseFilter(p2, p3)).start();
			new Thread(new AlphabetizeFilter(p3, p4)).start();
			new Thread(new Output(p4)).start();
		} catch (IOException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		} 

    }

}
