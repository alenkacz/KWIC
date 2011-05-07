package pipeandfilter;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vasek
 */
public class Input implements Runnable {

	private String mPath;
	private Pipe mOutput;

	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////

	public Input(String path, Pipe output) {
		mPath = path;
		mOutput = output;
	}

	public void run() {
		FileInputStream fstream = null;
		try {
			fstream = new FileInputStream(mPath);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = br.readLine()) != null) {
				//System.out.println("R: " + line);
				if (!line.equals("")) mOutput.write(line + '\n');
			}
			mOutput.write("\n"); // signal ending input
			in.close();
		} catch (IOException ex) {
			Logger.getLogger(Input.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				fstream.close();
			} catch (IOException ex) {
				Logger.getLogger(Input.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
	
}
