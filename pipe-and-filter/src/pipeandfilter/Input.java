package pipeandfilter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 * @author Vasek
 */
public class Input {

	private String _path;
	private Pipe _output;

	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////

	public Input(String path, Pipe output) {
		_path = path;
		_output = output;
	}

	public void run() throws IOException {
		_output.write(readFileAsString(_path));
	}

	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////

	private static String readFileAsString(String filePath) throws java.io.IOException {
		FileInputStream f = null;
		byte[] buffer = new byte[(int) new File(filePath).length()];
		try {
			f = new FileInputStream(filePath);
			f.read(buffer);
		} finally {
			if (f != null) {
				try {
					f.close();
				} catch (IOException ignored) {
				}
			}
		}
		return new String(buffer);
	}
}
