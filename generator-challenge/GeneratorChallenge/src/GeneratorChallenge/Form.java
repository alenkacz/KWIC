package GeneratorChallenge;

import java.util.ArrayList;
import java.util.List;


public class Form {
	private String id;
	
	private List<Input> inputs = new ArrayList<Input>();
	
	public Form(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public void addInput(Input i) {
		inputs.add(i);
	}
	
	public String toXml() {
		XMLBuilder b = new XMLBuilder();
		return b.generate(this);
	}

	public List<Input> getInputs() {
		return inputs;
	}
}
