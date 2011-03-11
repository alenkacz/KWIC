package GeneratorChallenge;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
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
		Element root = doc.createElement("form");
		root.setAttribute("id", "form" + form.getId());
		
		for( Input i : form.getInputs() ) {
			root.appendChild(parseInput(i));
		}
		
		doc.appendChild(root);
		
		return getXmlAsString();
	}
	
	private Element parseInput(Input i) {
		Element res = doc.createElement(i.getType());
		
		res.setAttribute("label", i.getName());
		res.setAttribute("value","#{bean." + i.getName() + "}");
		res.setAttribute("title","#{text[t." + form.getId().toLowerCase() + "." + i.getName());
		res.setAttribute("renderer","#{empty " + i.getName() + "Renderer ? 'true' : " + i.getName() + "Renderer }");
		res.setAttribute("id","#{prefix}" + i.getName());
		if( i.getLength() != -1 )  {
			res.setAttribute("minlength", "0");
			res.setAttribute("maxlength", String.valueOf(i.getLength()));
		}
		if( i.isRequired() != null ) res.setAttribute("required", String.valueOf(i.isRequired()));
		if( i.getType().toLowerCase().equals("text") ) { res.setAttribute("size", "30"); }
		
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
