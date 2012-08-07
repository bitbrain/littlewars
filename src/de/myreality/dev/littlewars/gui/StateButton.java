/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mail to info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Provides a button with pre-defined states
 * 
 * @version 	0.0.1
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.gui;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import de.myreality.dev.littlewars.components.Pair;

public class StateButton<T> extends ZoomButton {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Current state
	protected Entry<Integer, Pair<String, T>> currentEntry;
	
	// All states
	protected Map<Integer, Pair<String, T> > states;
	
	protected int key;
	
	
	/**
	 * Constructor of StateButton
	 * 
	 * @param x x-coordinate on the screen
	 * @param y y-coordinate on the screen
	 * @param width width of the button
	 * @param height height of the button
	 * @param gc GameContainer
	 * @throws SlickException
	 */
	public StateButton(int x, int y, int width, int height,
			GameContainer gc) throws SlickException {
		super(x, y, width, height, "", gc);
		currentEntry = null;
		states = new TreeMap<Integer, Pair<String, T> >();
		key = 0;
	}
	
	
	/**
	 * @return The next state entry
	 */
	private Entry<Integer, Pair<String, T>> getNextEntry() {
		boolean found = false;
		for (Entry<Integer, Pair<String, T>> entry : states.entrySet()) {
			if (!found) {
				if (entry.equals(currentEntry)) {
					found = true;			
					
					if (entry.equals(getLastEntry())) {
						return getFirstEntry();
					} else {							
						continue;
					}
				}
			} else {
				return entry;
			}
		}
		return null;
	}
	
	/**
	 * @param ID id of the state
	 * @return the generic value
	 */
	public T getValue(Integer ID) {
		return states.get(ID).getSecond();
	}
	
	
	/**
	 * Set the current state to a new one
	 * 
	 * @param state new state
	 */
	public void setState(T state) {
		for (Entry<Integer, Pair<String, T>> entry : states.entrySet()) {
			if (entry.getValue().getSecond().equals(state)) {
				currentEntry = entry;
				break;
			} 
		}
	}
	
	
	/**
	 * @return Gets the current value
	 * @throws SlickException
	 */
	public T getCurrentValue() throws SlickException {
		if (currentEntry != null) {
			return currentEntry.getValue().getSecond();
		} else {
			throw new SlickException("Error: StateButton has no values!");
		}
	}
	
	
	/**
	 * @param ID
	 * @return check, if current state is active
	 */
	public boolean isCurrentState(Integer ID) {
		if (currentEntry != null) {
			return currentEntry.getKey() == ID;
		} else {
			return false;
		}
	}
	
	/**
	 * @return The last state entry
	 */
	private Entry<Integer, Pair<String, T>> getLastEntry() {
		int count = 0;
		for (Entry<Integer, Pair<String, T>> entry : states.entrySet()) {
			count++;
			if (count == states.entrySet().size()) {
				return entry;
			}
		}
		
		return null;
	}
	
	
	/**
	 * @return The first state entry
	 */
	private Entry<Integer, Pair<String, T>> getFirstEntry() {
		for (Entry<Integer, Pair<String, T>> entry : states.entrySet()) {
			return entry;
		}
		
		return null;
	}
	
	@Override
	public void update(int delta) {			
		super.update(delta);
		
		if (currentEntry != null) {
			if (onMouseClick()) {
				currentEntry = getNextEntry();
			}
			
			if (!text.equals(currentEntry.getValue().getFirst())) {
				text = currentEntry.getValue().getFirst();
			}	
		}
	}
	
	
	
	/**
	 * Adds a new state to the button
	 * 
	 * @param key string key
	 * @param caption displayed text on the button
	 * @param stateValue value to store
	 */
	public void addState(String caption, T stateValue) {	
		states.put(key++, new Pair<String, T>(caption, stateValue));
		if (states.size() == 1) {
			for (Entry<Integer, Pair<String, T>> entry : states.entrySet()) {
				currentEntry = entry;					
				break;
			}				
		}		
	}

}
