/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mailto info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Implements a location class for language settings
 * 
 * @version 	0.0.1
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.components.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.newdawn.slick.SlickException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Location {
	
	// Single instance of the location
	private static Location _instance;
	
	// Map that contains all language codes
	private Map<String, String> codes;
	
	static {
		reload();
	}
	
	
	public static void reload() {
		try {
			_instance = new Location("res/locations/" + Configuration.getInstance().getLocale() + "/locations.xml");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * Constructor of Location
	 * 
	 * @param file XML file of location names
	 * @throws FileNotFoundException
	 * @throws SlickException
	 */
	private Location(String file) throws FileNotFoundException, SlickException {
		codes = new HashMap<String, String>();	
		
		// Build location map
		InputStream is;
		is = new FileInputStream(file);
		
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new SlickException("Could not load locations", e);
		}
		Document doc = null;
        try {
			doc = docBuilder.parse (is);
		} catch (SAXException e) {
			throw new SlickException("Could not load locations", e);
		} catch (IOException e) {
			throw new SlickException("Could not load locations", e);
		}
 
		// normalize text representation
        doc.getDocumentElement ().normalize ();
 
        NodeList listResources = doc.getElementsByTagName("location");
 
        int totalResources = listResources.getLength();
 
        for(int resourceIdx = 0; resourceIdx < totalResources; resourceIdx++){
 
        	Node resourceNode = listResources.item(resourceIdx);
 
        	if(resourceNode.getNodeType() == Node.ELEMENT_NODE){
        		Element resourceElement = (Element)resourceNode;
 
        		String code = resourceElement.getAttribute("code");
        		codes.put(code, resourceElement.getTextContent());
        	}
        }
	}	
	
	
	/**
	 * @return Returns single instance of the location
	 */
	public static Location getInstance() {
		return _instance;
	}

	
	
	/**
	 * @param langcode
	 * @return translated location name
	 */
	public String getLocationName(String langcode) {
		return codes.get(langcode);
	}
	
	
	
	/**
	 * @return Map (key = code, value = name) of all available locations
	 */
	public Map<String, String> getCodes() {
		
		return codes;
	}
}
