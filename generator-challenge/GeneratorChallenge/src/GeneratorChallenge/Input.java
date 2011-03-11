package GeneratorChallenge;

public class Input {
	
	private String type;
	private String name;
	private Boolean required = null;
	private int length = -1;
	
	public Input(String type, String name) {
		this.type = type;
		this.name = name;
	}
	
	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
	
	public String getType() {
		return type;
	}
	public String getName() {
		return name;
	}
	public Boolean isRequired() {
		return required;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setName(String value) {
		this.name = value;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
}
