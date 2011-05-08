package pipeandfilter;

import java.io.IOException;

/**
 *
 * @author Vasek
 */
abstract class Filter implements Runnable {

	private Pipe mInput;
	private Pipe mOutput;

	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////

	public Filter(Pipe input, Pipe output) {
		this.mInput = input;
		this.mOutput = output;
	}

	public void run() {
		try {
			while (true) {
				transform();
			}
		} catch (IOException ex) {
			// input stream closed
		}
	}

	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////

	abstract protected void transform() throws IOException;

	protected char read() throws IOException {
		return (char) mInput.read();
	}

	protected String readLine() throws IOException {
		String out = "";
		int c = mInput.read();
		while(true) {
			char ch = (char) c;
			out += ch;
			if (ch == '\n') break;
			c = mInput.read();
		}

		return out;
	}

	protected void write(String str) throws IOException {
		mOutput.write(str);
	}

}
