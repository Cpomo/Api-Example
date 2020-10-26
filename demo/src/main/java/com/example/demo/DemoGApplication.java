package com.example.demo;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

@SpringBootApplication
@RestController
public class DemoGApplication {
	public static int PRETTY_PRINT_INDENT_FACTOR = 4; //indent factor for JSON objects.
	public String jsonPrettyPrintString; // indented JSON String.
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		SpringApplication.run(DemoGApplication.class, args);
	}

	//Input: 2 parameters ICD and enterprise number
	//Output: Business card for the enterprise as a JSON.
	@GetMapping("/Businesscard/getById")
	public String getById(@RequestParam(value = "ICD") String ICD,
			@RequestParam(value = "enterpriseNumber") String enterpriseNumber)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {

		Document doc = xmlReader.xmlread("test.xml");
		NodeList participantList = doc.getElementsByTagName("participant");
		for (int i = 0; i < participantList.getLength(); i++) {
			Node p = participantList.item(i);
			if (p.getNodeType() == Node.ELEMENT_NODE) {
				Element participant = (Element) p;
				String id = participant.getAttribute("value");
				if (id.equals(ICD + ":" + enterpriseNumber)) {
					DOMSource source = new DOMSource(p.getParentNode());
					JSONObject xmlJSONObj = (JSONObject) jsonConverter.jsonConvert(source);
					jsonPrettyPrintString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
					return String.format(jsonPrettyPrintString);
				}
			}
		}
		return "";
	}
	
	//Input: Name to search for
	//Output: JSON containing Name, Enterprise number and country code.
	@GetMapping("/Businesscard/searchByName")
	public String searchByName(@RequestParam(value = "name") String name)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {
		Document doc = xmlReader.xmlread("test.xml");
		NodeList nameList = doc.getElementsByTagName("name");
		ArrayList<String> sList = new ArrayList<String>();

		for (int i = 0; i < nameList.getLength(); i++) {
			Node e = nameList.item(i);
			if (e.getNodeType() == Node.ELEMENT_NODE) {
				Element namenode = (Element) e;
				String id = namenode.getAttribute("name");
				if (id.contains(name)) {

					DOMSource source = new DOMSource(e.getParentNode().getParentNode());
					JSONObject xmlJSONObj = (JSONObject) jsonConverter.jsonConvert(source);
	
					Object jsonclass = xmlJSONObj.getJSONObject("businesscard").get("entity").getClass();
					if (jsonclass == org.json.JSONObject.class) {
						String countrycode = xmlJSONObj.getJSONObject("businesscard").getJSONObject("entity")
								.getString("countrycode");
						String ICDandEnterprisenumber = xmlJSONObj.getJSONObject("businesscard").getJSONObject("participant")
								.getString("value");
						String Enterprisenumber = ICDandEnterprisenumber.substring(ICDandEnterprisenumber.indexOf(":") + 1);
						Enterprisenumber.trim();

						String xmlString = "<name>" + escapeMetaCharacters.EscapeMetaCharacters(id) + "</name> <countrycode>" + countrycode
								+ "</countrycode> <entitycode>" + Enterprisenumber + "</entitycode>";
						JSONObject jsonObj = XML.toJSONObject(xmlString);

						jsonPrettyPrintString = jsonObj.toString(PRETTY_PRINT_INDENT_FACTOR);
						if (!sList.contains(jsonPrettyPrintString)) {

							sList.add(jsonPrettyPrintString);
						}
					} else {
						JSONArray entityArray = xmlJSONObj.getJSONObject("businesscard").getJSONArray("entity");
						for (int l = 0; l < entityArray.length(); l++) {
							String ICDandEnterprisenumber = xmlJSONObj.getJSONObject("businesscard").getJSONObject("participant")
									.getString("value");
							String Enterprisenumber = ICDandEnterprisenumber.substring(ICDandEnterprisenumber.indexOf(":") + 1);
							Enterprisenumber.trim();
							String countrycode = entityArray.getJSONObject(l).getString("countrycode");
							// System.out.println(post_id);
							String xmltest = "<name>" + escapeMetaCharacters.EscapeMetaCharacters(id) + "</name> <countrycode>" + countrycode
									+ "</countrycode> <entitycode>" + Enterprisenumber + "</entitycode>";
							JSONObject Jsontest = XML.toJSONObject(xmltest);

							jsonPrettyPrintString = Jsontest.toString(PRETTY_PRINT_INDENT_FACTOR);
							if (!sList.contains(jsonPrettyPrintString)) {
								sList.add(jsonPrettyPrintString);

							}
						}

					}

				}

			}

		}
		return sList.toString();
	}

	//Input: Name to search for
	//Output: JSON representing the value in one /root/businesscard/.
	@GetMapping("/Businesscard/getByName")
	public String getByName(@RequestParam(value = "name") String name)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {

		Document doc = xmlReader.xmlread("test.xml");
		NodeList nameList = doc.getElementsByTagName("name");
		ArrayList<String> sList = new ArrayList<String>();

		for (int i = 0; i < nameList.getLength(); i++) {
			Node e = nameList.item(i);
			if (e.getNodeType() == Node.ELEMENT_NODE) {
				Element entityNode = (Element) e;
				String id = entityNode.getAttribute("name");
				if (id.equals(name)) {

					DOMSource source = new DOMSource(e.getParentNode().getParentNode());

					JSONObject xmlJSONObj = (JSONObject) jsonConverter.jsonConvert(source);
					jsonPrettyPrintString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
					if (!sList.contains(jsonPrettyPrintString)) {
						sList.add(jsonPrettyPrintString);
					}
				}
			}
		}
		return sList.toString();
	}
}

