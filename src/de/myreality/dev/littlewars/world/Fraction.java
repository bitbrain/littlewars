/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mail to info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Defines several fractions
 * 
 * @version 	0.2
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.world;

import java.util.ArrayList;
import java.util.List;

import de.myreality.dev.littlewars.components.resources.ResourceManager;

public class Fraction {
	
	public static final int HUMAN = 1, CYBORG = 2;
	
	private int type;
	
	public Fraction(int type) {
		this.setType(type);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public String getName() {
		switch (type) {
			case HUMAN:
				return ResourceManager.getInstance().getText("TXT_FRACTION_HUMAN");
			case CYBORG:
				return ResourceManager.getInstance().getText("TXT_FRACTION_CYBORG");
		}
		
		return "";
	}
	
	public String getKey() {
		switch (type) {
		case HUMAN:
			return "human";
		case CYBORG:
			return "cyborg";
		}
		return "";	
	}
	
	public static int getTypeFromKey(String key) {
		if (key == "human") {
			return HUMAN;
		}
		
		if (key == "cyborg") {
			return CYBORG;
		}
		
		return -1;
	}

	
	public static List<Fraction> getAsList() {
		List<Fraction> fractions = new ArrayList<Fraction>();		
		
		fractions.add(new Fraction(HUMAN));
		fractions.add(new Fraction(CYBORG));
		
		return fractions;		
	}

	@Override
	public String toString() {
		return getName();
	}
	
	

}
