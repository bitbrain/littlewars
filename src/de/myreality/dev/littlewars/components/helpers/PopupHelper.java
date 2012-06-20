package de.myreality.dev.littlewars.components.helpers;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.GameState;

import de.myreality.dev.littlewars.game.CustomGameState;
import de.myreality.dev.littlewars.gui.PopupBox;
import de.myreality.dev.littlewars.objects.GameObject;

public class PopupHelper {

	private static PopupHelper _instance;
	private GameState currentState;
	
	// Store all objects with a popup functionality
	Map<GameState, Map<GameObject, PopupBox> > targets;
	
	static {
		_instance = new PopupHelper();		
	}
	
	public static PopupHelper getInstance() {
		return _instance;
	}
	
	private PopupHelper() {
		targets = new HashMap<GameState, Map<GameObject, PopupBox> >();
		currentState = null;
	}
	
	public void clear() {
		
	}
	
	public void setCurrentState(GameState state) {
		currentState = state;
	}
	
	
	/**
	 * Add a popup menu to the game object
	 */
	public void addPopup(GameObject target, String text, GameContainer gc) {
		GameState state = CustomGameState.current;
		// Check gamestate
		if (targets.get(state) == null) {
			targets.put(state, new HashMap<GameObject, PopupBox>());
		}
		if (targets.get(state).get(target) == null) {
			PopupBox box = new PopupBox(text, gc);
			box.attachTo(target);
			targets.get(state).put(target, box);
		}
	}
	
	public void update(int delta) {
		if (currentState != null && targets.get(currentState) != null) {
			for (Entry<GameObject, PopupBox> target : targets.get(currentState).entrySet()) {
				target.getValue().update(delta);
			}
		}
	}
	
	public void render(Graphics g) {
		if (currentState != null && targets.get(currentState) != null) {
			for (Entry<GameObject, PopupBox> target : targets.get(currentState).entrySet()) {
				if (target.getKey().isVisible()) {
					target.getValue().draw(g);
				}
			}
		}
	}
	
	
	/**
	 * Remove a popup menu from the game object
	 */
	public void removePopup(GameObject target) {
		if (targets.get(target) != null) {
			targets.remove(target);
		}
	}
}
