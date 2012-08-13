/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mail to info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * General game object
 * 
 * @version 	0.1
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 * General game object
 * 
 * @version 0.1
 * @author Miguel Gonzalez
 *
 */
public abstract class GameObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// coordinates
	protected float x, y, lastX, lastY; 
	
	// width and height
	protected int width, height;

	// Parent game object
	protected GameObject parent;
	
	// Children
	protected List<GameObject> children;
	
	// Mouse states
	boolean isHover, wasHover, onClicked, wasClicked, onHover, onOut, wasOut, instantClick, visible;
	
	// Collision area
	protected Shape area;
	
	// Game Container
	protected GameContainer gc;
	
	// Camera
	protected Camera camera;
	
	// Last state of the mouse in order to solve pressing bug
	static private boolean lastMouseState;
	
	// Focus Object
	protected static GameObject focusObject;
		
	static {
		lastMouseState = false;
		focusObject = null;
	}
	
	public static void calculateMouseState(GameContainer gc) {
		lastMouseState = gc.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON);
	}
	
	
	// TODO Create javadoc here
	public GameObject(float x, float y, GameContainer gc) {
		this.x = x;
		this.y = y;
		lastX = x;
		lastY = y;
		parent = null;
		children = new ArrayList<GameObject>();
		this.gc = gc;
		area = new Rectangle(x, y, getWidth(), getHeight());
		camera = null;
		visible = true;
	}
	
	// TODO Create javadoc here
	public GameObject(float x, float y, GameContainer gc, Camera camera) {
		this(x, y, gc);
		this.camera = camera;
	}
	
	// TODO Create javadoc here
	public void finalize() {
		if (hasParent()) {
			detach();
			parent.removeChild(this);
		}
		if (childrenCount() > 0) {
			for (GameObject o: children) {
				o.detach();
			}
		}
	}
	
	
	public void setVisible(boolean value) {
		visible = value;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	// TODO Create javadoc here
	public boolean collidateWith(GameObject object) {
		return area.contains(object.area);
	}

	// TODO Create javadoc here
	public void attachTo(GameObject object) {
		// At first, detach if parent exists
		if (hasParent()) {
			this.detach();
		}
		
		// Only attach if parameter is not already parent
		if (!object.hasChild(this)) {
			parent = object;
			object.addChild(this);
		}
		
		area = new Rectangle(getX(), getY(), getWidth(), getHeight());
	}
	
	// TODO Create javadoc here
	public void detach() {
		if (hasParent()) {
			parent.removeChild(this);
			parent = null;
		}
	}
	
	// TODO Create javadoc here
	public boolean hasParent() {
		return parent != null;
	}
	
	
	// TODO Create javadoc here
	public int childrenCount() {
		return children.size();
	}
	
	
	// TODO Create javadoc here
	public boolean hasChild(GameObject child) {
		for (GameObject o : children) {
			if (o.equals(child)) {
				return true;
			}
		}		
		return false;
	}
	
	
	// TODO Create javadoc here
	protected void drawChildren(Graphics g) {
		for (GameObject o : children) {
			o.draw(g);
		}
	}
	
	
	// TODO Create javadoc here
	protected void updateChildren(int delta) {
		for (GameObject o : children) {
			o.update(delta);
		}
	}
	
	
	// TODO Create javadoc here
	public GameObject getChild(String id) {
		//for (GameObject g: children) {
		//	if (g.id.equals(id)) {
		//		return g;
		//	}
		//}		
		return null;
	}
	
	
	// TODO Create javadoc here
	protected void addChild(GameObject child) {
		children.add(child);
	}
	
	protected void removeChild(GameObject child) {
		children.remove(child);
	}
	
	public GameObject(GameContainer gc) {
		this(0, 0, gc);
	}

	public float getX() {
		if (hasParent()) {
			return x + parent.getX();
		}

		return x;
	}
	
	public float getY() {
		if (hasParent()) {
			return y + parent.getY();
		}
		
		
		return y;		
	}

	public void setX(float x) {
		this.x = x;
		calculatePosition();
	}
	

	public void setY(float y) {
		this.y = y;
		calculatePosition();
	}
	
	public boolean onMouseClick() {
		return onClicked && !lastMouseState;
	}

	public boolean onMouseOver() {
		return onHover;
	}
	
	public boolean isMouseOver() {
		return isHover;
	}	
	
	public boolean onMouseOut() {
		return onOut;
	}
	
	public boolean isMouseOut() {
		return !isMouseOver();
	}

	
	// TODO Create javadoc here
	protected void calculatePosition() {
		lastX = x;
		lastY = y;
		if (area.getX() != getX()) {
			area.setX(getX());		
		}
			
		if (area.getY() != getY()) {
			area.setY(getY());	
		}		
	}
	
	public abstract int getWidth();
	public abstract int getHeight();
	
	public abstract void draw(Graphics g);
	
	
	public Shape getArea() {
		return area;
	}


	public void update(int delta) {	
		calculatePosition();
		if (area.contains(gc.getInput().getMouseX(), gc.getInput().getMouseY())) {
			if (!isHover) {
				isHover = true;
			}
		} else {
			if (isHover) {
				isHover = false;
			}
		}
		
		if (!gc.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
			if (onClicked) {
				onClicked = false;
			}
			
			if (wasClicked) {
				wasClicked = false;
			}
		}
		
		if (!isMouseOver()) {
			if (wasHover) {
				wasHover = false;
			}
			if (onHover) {
				onHover = false;
			}
		}
		
		if (!isMouseOut()) {
			if (wasOut) {
				wasOut = false;
			}
			if (onOut) {
				onOut = false;
			}
		}
		
		if (!wasClicked && isMouseOver() && gc.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
			if (!wasClicked) {
				wasClicked = true;
			}
			if (!onClicked) {
				onClicked = true;
			}
		} else if (wasClicked) {
			if (onClicked) {
				onClicked = false;
			}
		}	
		
		if (!wasHover && !onHover && isHover) {
			onHover = true;
			wasHover = true;
		} else if (wasHover) {
			if (onHover) {
				onHover = false;
			}
		}
		
		if (!wasOut && !onOut && isMouseOut()) {
			onOut = true;
			wasOut = true;
		} else if (wasOut) {
			if (onOut) {
				onOut = false;
			}
		}
		
		if (onMouseClick()) {
			setFocused();
		}
		
		if (gc.getInput().isKeyDown(Input.KEY_ESCAPE)) {
			focusObject = null;
		}
		
		if (focusObject != null) {
			if (!focusObject.isMouseOver() && gc.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
				focusObject = null;
			}
		}
		
		
		calculatePosition();			
	}
	
	public void setFocused() {
		if (hasParent()) {
			focusObject = parent;
		} else {
			focusObject = this;
		}
	}
	
	
	public GameObject getParent() {
		return parent;
	}


	public void setParent(GameObject parent) {
		this.parent = parent;
	}


	public List<GameObject> getChildren() {
		return children;
	}


	public void setChildren(List<GameObject> children) {
		this.children = children;
	}
	
	public void setOnClicked(boolean value) {
		instantClick = value;
	}	
	
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
		area = new Rectangle(getX(), getY(), getWidth(), getHeight());
	}
	
	
	public boolean hasMoved() {
		return lastX != x || lastY != y;
	}


	public float getLastX() {
		return lastX;
	}

	public float getLastY() {
		return lastY;
	}
	
	
	public boolean isFocused() {
		return equals(focusObject);
	}
	
}
