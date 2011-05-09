package pipeandfilter;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.Reader;

/**
 *
 * @author Vasek
 */
public class Pipe {

	private PipedReader mReader;
	private PipedWriter mWriter;

	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////

	public Pipe() throws IOException {
		mReader = new PipedReader();
		mWriter = new PipedWriter();
		mWriter.connect(mReader);
	}

	public void write(String data) throws IOException {
		mWriter.write(data);
	}

	public int read() throws IOException {
		return mReader.read();
	}

	public Reader getReader() {
		return mReader;
	}
	
}
