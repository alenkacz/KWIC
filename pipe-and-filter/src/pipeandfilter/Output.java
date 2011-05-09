package pipeandfilter;

import java.io.IOException;

/**
 *
 * @author Vasek
 */
public class Output implements Runnable {

	private Pipe mInput;

	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////

	public Output(Pipe input) {
		mInput = input;
	}

	public void run() {
		try {
			while (true) {
				System.out.print((char) mInput.read());
			}
		} catch (IOException ex) {
			// input stream closed
		}
	}

}
