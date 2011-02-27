package pipeandfilter;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vasek
 */
public class Main {

	private static String _path = "input.txt";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

		/*
			-proč nejde dát na abstract class throws IOException ?
			- nebylo by lepší, kdyby se všechno spouštělo na konci konstruktoru ->
			nepsalo by se run a bylo by zajištěný správný pořadí
		 */

		Pipe p1 = new Pipe();
		Pipe p2 = new Pipe();
		Pipe p3 = new Pipe();

		Input input = new Input(_path, p1);
		CircularShiftFilter c = new CircularShiftFilter(p1, p2);
		AlphabetizeFilter a = new AlphabetizeFilter(p2, p3);
		Output output = new Output(p3);
		
		try {
			input.run();
			c.run();
			a.run();
			output.run();
		} catch (IOException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}

    }

}
