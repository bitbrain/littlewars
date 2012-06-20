/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mailto info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Contains Animation IDs for an animation
 * 
 * @version 	0.2.1
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.components.resources;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Animation;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SpriteAnimationData {
	
	// Animation states
	public static final int DEFAULT = 0, MOVE = 1, ATTACK = 2, DIE = 3;

	// Animation resource maps
	private Map<Integer, String> data;
	
	/**
	 * Constructor of SpriteAnimationData
	 * 
	 * @param element XML element
	 */
	public SpriteAnimationData(Element element) {
		data = new HashMap<Integer, String>();
		loadFromXML(element);
	}
	
	
	/**
	 * Read the given XML element and fill the animation maps
	 * 
	 * @param element XML element
	 */
	private void loadFromXML(Element element) {
		NodeList nodes = element.getChildNodes();
		data.clear();
		for (int i = 0; i < nodes.getLength(); ++i) {
			Node node = nodes.item(i);
			
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element tag = (Element) node;
				
				if (tag.getTagName().equals("default")) {
					data.put(DEFAULT, tag.getTextContent());
				} else if (tag.getTagName().equals("move")) {
					data.put(MOVE, tag.getTextContent());
				} else if (tag.getTagName().equals("attack")) {
					data.put(ATTACK, tag.getTextContent());
				} else if (tag.getTagName().equals("die")) {
					data.put(DIE, tag.getTextContent());
				}
			}
		}
	}	
	
	
	/**
	 * Generate an animation array with 2 dimensions from the map data
	 * 
	 * @return Animation array with 2 dimensions
	 */
	public Animation[][] generateAnimations() {
		
		Animation[][] tmpData = new Animation[4][4];

		for (int i = 0; i < 4; ++i) {
			tmpData[DEFAULT][i] = ResourceManager.getInstance().getNewAnimation(data.get(DEFAULT), i);
			tmpData[DIE][i] = ResourceManager.getInstance().getNewAnimation(data.get(DIE), i);
			tmpData[MOVE][i] = ResourceManager.getInstance().getNewAnimation(data.get(MOVE), i);
			tmpData[ATTACK][i] = ResourceManager.getInstance().getNewAnimation(data.get(ATTACK), i);
		}		
		
		return tmpData;
	}
}
