package com.example.demo;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.JSONObject;
import org.json.XML;

//Class to convert from xml to json returns a JSON Object
public class jsonConverter {
	public static String jsonPrettyPrintString;
	public static int PRETTY_PRINT_INDENT_FACTOR = 4;
	public static Object jsonConvert(DOMSource s) throws TransformerFactoryConfigurationError, TransformerException {
		javax.xml.transform.Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");

		StreamResult result = new StreamResult(new StringWriter());
		transformer.transform(s, result);

		String xmlString = result.getWriter().toString();

		JSONObject xmlJSONObj = XML.toJSONObject(xmlString);
		return xmlJSONObj;
	}
}
