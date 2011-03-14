package GeneratorChallenge;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import javax.persistence.*;

public class Generator {

    private static final String CLASS_ERROR = "Given class does not have entity annotation or specified table name";

    /**
     * Generates and prints out XML output of form for received entity
     */
    public static void generate(Class c) {
        try {
            String formId = getFormId(c);
            Form form = new Form(formId);
            addInputs(c, form);
            System.out.println(form.toXml());
        } catch (IllegalArgumentException e) {
            printClassErrorAndExit(e.getMessage());
        }
    }

    /**
     * Checks validity of received entity and retrieves form id from Annotation
     */
    private static String getFormId(Class c) throws IllegalArgumentException {
        Annotation[] annotations = c.getDeclaredAnnotations();
        boolean isEntity = false;
        String formId = null;

        if (annotations.length < 2) { // validation
            throw new IllegalArgumentException(CLASS_ERROR);
        }

        for (Annotation a : annotations) {
            if (a instanceof Table) {
                Table t = (Table) a;
                formId = t.name();
            }

            if (a instanceof Entity) {
                isEntity = true;
            }
        }

        if (!isEntity || formId == null) { // validation
            throw new IllegalArgumentException(CLASS_ERROR);
        }

        return formId;
    }

    private static void addInputs(Class c, Form form) {
        Method[] methods = c.getDeclaredMethods();
        for (Method m : methods) {
            if (m.getName().contains("get")) { // not interested in setter methods
                Input i = processMethod(m, m.getReturnType());
                if (i != null) {
                    form.addInput(i);
                }
            }
        }
    }

    /**
     * Parses single method with annotations and fills information into newly created Input object
     */
    private static Input processMethod(Method m, Class c) {
        Annotation[] annotations = m.getDeclaredAnnotations();
        Class returnType = m.getReturnType();
        String name = m.getName().substring(3); // ignoring first three letters - get

        Input i = new Input(decideType(returnType), name);

        for (Annotation a : annotations) {
            if (a instanceof Column) {
                i = processColumn(i, (Column) a);
            } else if (a instanceof Id) {
                //i = parseId(i,a);
                i = null;
            } else if (a instanceof Temporal) {
                i = processTemporal(i, (Temporal) a);
            } else if (a instanceof Enumerated) {
                i = processEnumerated(i, (Enumerated) a, c);
            }
        }

        return i;
    }

    private static Input processColumn(Input i, Column c) {
        i.setRequired(!c.nullable());
        i.setLength(c.length());

        return i;
    }

    private static Input processId(Input i, Id a) {
        return i;
    }

    private static Input processTemporal(Input i, Temporal a) {
        return i;
    }

    private static Input processEnumerated(Input i, Enumerated a, Class c) {
        if (c.isEnum()) {
            Object[] values = c.getEnumConstants();

            for (Object o : values) {
                i.addValue(o.toString());
            }
        }
        return i;
    }

    /**
     * Checks Class of return type and decides which type of input to use
     */
    private static String decideType(Class returnType) {
        String returnName = returnType.getName().replace(".", "#");
        returnName = returnName.replace("$", "#");
        String[] returnParts = returnName.split("#");
        String res = returnParts[returnParts.length - 1];

        if (res.equals("String")) {
            res = "Text";
        } else if (returnType.getName().indexOf("$") != -1) {
            res = "SelectOneMenu";
        }

        return res; // returning last one
    }

    /**
     * Prints out error and exits
     */
    private static void printClassErrorAndExit(String message) {
        System.err.println(message);
        System.exit(1);
    }
}
