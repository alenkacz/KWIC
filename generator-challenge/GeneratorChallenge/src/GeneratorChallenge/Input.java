package GeneratorChallenge;

import java.util.ArrayList;
import java.util.List;

public class Input {

    public static final int LENGTH_EMPTY = -1;
    private String type;
    private String name;
    private Boolean required = null;
    private int length = LENGTH_EMPTY;
    private List<String> values = new ArrayList<String>();

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

    public void setValues(List<String> values) {
        this.values = values;
    }

    public List<String> getValues() {
        return this.values;
    }

    public void addValue(String s) {
        this.values.add(s);
    }
}
