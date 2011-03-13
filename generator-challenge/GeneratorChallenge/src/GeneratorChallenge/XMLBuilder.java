package GeneratorChallenge;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

import com.sun.org.apache.xerces.internal.dom.DocumentImpl;

public class XMLBuilder {
	
	Document doc = new DocumentImpl();
	Transformer transformer = null;
	Form form = null;

	public String generate(Form form) {
		this.form = form;
		Element root = doc.createElement("h:form");
		root.setAttribute("id", "form" + form.getId());
		
		for( Input i : form.getInputs() ) {
			root.appendChild(parseInput(i));
		}
		
		doc.appendChild(root);
		
		return getXmlAsString();
	}
	
	private Element parseInput(Input i) {
		Element res = doc.createElement("input" + i.getType());
		
		res.setAttribute("label", i.getName());
		res.setAttribute("value","#{bean." + i.getName().toLowerCase() + "}");
		res.setAttribute("title","#{text[t." + form.getId().toLowerCase() + "." + i.getName());
		res.setAttribute("renderer","#{empty " + i.getName() + "Render ? 'true' : " + i.getName() + "Render }");
		res.setAttribute("id","#{prefix}" + i.getName().toLowerCase());
		if( i.isRequired() != null ) res.setAttribute("required", String.valueOf(i.isRequired()));
		if( i.getType().toLowerCase().equals("text") ) { 
			res.setAttribute("size", "30");
			if( i.getLength() != Input.LENGTH_EMPTY )  {
				res.setAttribute("minlength", "0");
				res.setAttribute("maxlength", String.valueOf(i.getLength()));
			}
		}
		
		return res;
	}

	private String getXmlAsString() {
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		StreamResult result = new StreamResult(new StringWriter());
		DOMSource source = new DOMSource(doc);
		
		try {
			transformer.transform(source, result);
		} catch( Exception e ) {
			e.printStackTrace();
		}
		
		return result.getWriter().toString();
	}

}
