/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mail to info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Game Difficulty
 * 
 * @version 	0.2
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.world;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.myreality.dev.littlewars.components.resources.ResourceManager;

public class Difficulty implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int NONE = 0, EASY = 1, MEDIUM = 2, HARD = 3, EXTREME = 4, PLAYER = 5;
	
	private int state;
	
	public Difficulty(int state) {
		this.setState(state);
	}
	
	public String getName() {
		switch (state) {
			case NONE:
				return ResourceManager.getInstance().getText("TXT_DIFFICULTY_NONE");
			case EASY:
				return ResourceManager.getInstance().getText("TXT_DIFFICULTY_EASY");
			case MEDIUM:
				return ResourceManager.getInstance().getText("TXT_DIFFICULTY_MEDIUM");
			case HARD:
				return ResourceManager.getInstance().getText("TXT_DIFFICULTY_HARD");
			case EXTREME:
				return ResourceManager.getInstance().getText("TXT_DIFFICULTY_EXTREME");
			case PLAYER:
				return ResourceManager.getInstance().getText("TXT_SETUP_PLAYER");
		}
		
		return "";
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	
	public static List<Difficulty> getAsFullList() {
		List<Difficulty> difficulties = new ArrayList<Difficulty>();		
		
		difficulties.add(new Difficulty(NONE));
		difficulties.add(new Difficulty(PLAYER));
		difficulties.add(new Difficulty(EASY));
		difficulties.add(new Difficulty(MEDIUM));
		difficulties.add(new Difficulty(HARD));
		difficulties.add(new Difficulty(EXTREME));		
		
		return difficulties;		
	}
	
	public static List<Difficulty> getAsCPUList() {
		List<Difficulty> difficulties = new ArrayList<Difficulty>();		
		
		difficulties.add(new Difficulty(NONE));
		difficulties.add(new Difficulty(EASY));
		difficulties.add(new Difficulty(MEDIUM));
		difficulties.add(new Difficulty(HARD));
		difficulties.add(new Difficulty(EXTREME));		
		
		return difficulties;		
	}

	@Override
	public String toString() {
		return getName();
	}
	
	
	

}
