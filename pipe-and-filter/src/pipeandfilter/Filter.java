package pipeandfilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vasek
 */
abstract class Filter {

	private String _data;

	private Pipe _input;
	private Pipe _output;

	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////

	public Filter(Pipe input, Pipe output) {
		_input = input;
		_output = output;
	}

	public void run() throws IOException {
		_data = _input.read();
		transform();
		_output.write(_data);
	}

	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////

	protected String getData() {
		return _data;
	}

	protected void setData(String data) {
		_data = data;
	}

	abstract protected void transform() throws IOException;

	protected List<String> explodeString(String str) throws IOException {
		List<String> lines = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new StringReader(str));
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.length() > 0) {
				lines.add(line);
			}
		}

		return lines;
	}

	protected String implodeString(List<String> lines) {
		String result = "";
		for (String line : lines) {
			result = result + line + "\n";
		}

		return result;
	}

}
