package GeneratorChallenge;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import javax.persistence.*;


public class Generator {
	
	private static final String CLASS_ERROR = "Given class does not have entity annotation or specified table name";
	
	private static Form form = null;
	
	/**
	 * Generates and prints out XML output of form for received entity
	 */
	public static void generate(Class c) {
		getFormId(c);
		
		Method[] methods = c.getDeclaredMethods();
		
		for( Method m : methods ) {
			if( m.getName().contains("get") ) // not interested in setter methods
				parseMethod(m);
		}
		
		System.out.println(form.toXml());
	}

	/**
	 * Checks validity of received entity and retrieves form id from Annotation
	 */
	private static void getFormId(Class c) {
		Annotation[] annotations = c.getDeclaredAnnotations();
		boolean valid = false;
		
		if( annotations.length < 2 ) printClassErrorAndExit(CLASS_ERROR); // validation
		
		for( Annotation a : annotations ) {
			if( a instanceof Table ) {
				Table t = (Table) a;
				form = new Form(t.name());
			}
			
			if( a instanceof Entity ) {
				valid = true;
			}
		}
		
		if( !valid || form == null ) printClassErrorAndExit(CLASS_ERROR); // validation
	}

	/**
	 * Parses single method with annotations and fills information into newly created Input object
	 */
	private static void parseMethod(Method m) {
		Annotation[] annotations = m.getDeclaredAnnotations();
		Class returnType = m.getReturnType();
		String name = m.getName().substring(3); // ignoring first three letters - get
		
		Input i = new Input(decideType(returnType),name);
		
		for( Annotation a : annotations ) {
			if( a instanceof Column ) {
				i = parseColumn(i, a);
			} else if( a instanceof Id ) {
				i = parseId(i,a);
			} else if( a instanceof Temporal ) {
				i = parseTemporal(i,a);
			} else if( a instanceof Enumerated ) {
				i = parseEnumerated(i,a);
			}
		}
		
		form.addInput(i);
	}
	
	private static Input parseColumn(Input i, Annotation a) {
		Column c = (Column) a;
		
		i.setRequired(!c.nullable());
		i.setLength(c.length());
		
		return i;
	}
	
	private static Input parseId(Input i, Annotation a) {
		return i;
	}
	
	private static Input parseTemporal(Input i, Annotation a) {
		return i;
	}
	
	private static Input parseEnumerated(Input i, Annotation a) {
		return i;
	}

	/**
	 * Checks Class of return type and decides which type of input to use
	 */
	private static String decideType(Class returnType) {
		if( returnType.getName().equals("java.util.Date")) {
			return "Date";
		} else {
			return "Text";
		}
	}

	/**
	 * Prints out error and exits
	 */
	private static void printClassErrorAndExit(String message) {
		System.err.println(message);
		System.exit(1);
	}
}
