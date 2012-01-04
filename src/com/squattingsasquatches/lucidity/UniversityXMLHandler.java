package com.squattingsasquatches.lucidity;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.squattingsasquatches.lucidity.objects.University;

public class UniversityXMLHandler extends DefaultHandler {

	private ArrayList<University> universities;

	public UniversityXMLHandler() {
		universities = new ArrayList<University>();
	}

	public void startDocument() {
		universities.clear();
	}

	public void endDocument() {

	}

	public ArrayList<University> getUniversities() {
		return universities;
	}

	public void startElement(String uri, String localName, String qName,
			Attributes atts) {
		Log.i("startElement",
				"element: " + localName + " id: " + atts.getValue("id")
						+ " name: " + atts.getValue("name")
						+ " serveraddress: " + atts.getValue("serveraddress")
						+ " port: " + atts.getValue("port"));

		if (localName.equals("university")) {
			universities.add(new University(atts.getValue("id"), atts
					.getValue("name"), atts.getValue("serveraddress"), atts
					.getValue("port"), 0));
		}

	}

	@Override
	public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

	}

	@Override
	public void endPrefixMapping(String arg0) throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
			throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void processingInstruction(String arg0, String arg1)
			throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDocumentLocator(Locator arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void skippedEntity(String arg0) throws SAXException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startPrefixMapping(String arg0, String arg1)
			throws SAXException {
		// TODO Auto-generated method stub

	}
}