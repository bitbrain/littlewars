/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mailto info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * A thread bases timer for counting in miliseconds
 * 
 * @version 	0.0.3
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.components.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.Sound;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class UnitResource {
	
	private String fraction;
	private String name;
	private String avatar;
	private Map<String, List<String> > sounds;
	private SpriteAnimationData animationData;
	
	public UnitResource(Element element) {
		sounds = new HashMap<String, List<String> >();
		sounds.put("onDead", new ArrayList<String>());
		sounds.put("onClick", new ArrayList<String>());
		sounds.put("onDamage", new ArrayList<String>());
		sounds.put("onAttack", new ArrayList<String>());
		
		loadFromXML(element);
	}
	
	
	
	private void loadFromXML(Element element) {
		
		for (int i = 0; i < sounds.size(); ++i) {
			if (sounds.get(i) != null) {
				sounds.get(i).clear();
			}
		}
		
		fraction = element.getAttribute("fraction");
		NodeList childs = element.getChildNodes();
		for (int i = 0; i < childs.getLength(); ++i) {
			Node resourceNode = childs.item(i);			 
        	if(resourceNode.getNodeType() == Node.ELEMENT_NODE){
        		Element tag = (Element) resourceNode;
        		
        		if (tag.getTagName() == "imageAvatar") {
        			avatar = tag.getTextContent();
        		} else if (tag.getTagName() == "spriteAnimation") {
        			animationData = new SpriteAnimationData(tag);
        		} else if (tag.getTagName() == "name") {
        			name = ResourceManager.getInstance().getText(tag.getTextContent());
        		} if (tag.getTagName() == "sounds") {        			
        			NodeList soundChilds = tag.getChildNodes();
        			for (int t = 0; t < soundChilds.getLength(); ++t) {
        				Node soundNode = soundChilds.item(t);			 
        	        	if(soundNode.getNodeType() == Node.ELEMENT_NODE){
        	        		Element soundElem = (Element) soundNode;
        	        		
        	        		if (soundElem.getAttribute("type").equals("onClick") ||
        	        			soundElem.getAttribute("type").equals("onAttack") ||
        	        			soundElem.getAttribute("type").equals("onDead") ||
        	        			soundElem.getAttribute("type").equals("onDamage")) {
        	        			sounds.get(soundElem.getAttribute("type")).add(soundElem.getTextContent());
        	        		}
        	        	}
        			}
        			
        		}
        	}
		}
	}

	public String getFraction() {
		return fraction;
	}

	public void setFraction(String fraction) {
		this.fraction = fraction;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public List<Sound> generateSoundList(String key) {
		List<Sound> tmpSounds = new ArrayList<Sound>();
		for (String ID : sounds.get(key)) {
			tmpSounds.add(ResourceManager.getInstance().getSound(ID));
		}
		
		return tmpSounds;
	}

	public void setSounds(String key, List<String> sounds) {
		this.sounds.get(key).clear();
		
		for (String s : sounds) {
			this.sounds.get(key).add(s);
		}
	}

	public SpriteAnimationData getAnimationData() {
		return animationData;
	}

	public void setAnimationData(SpriteAnimationData animationData) {
		this.animationData = animationData;
	}

}
