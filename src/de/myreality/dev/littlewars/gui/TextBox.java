/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mail to info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * General text box in order to writing texts
 * 
 * @version 	0.5.4
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.geom.Rectangle;

import de.myreality.dev.littlewars.components.Timer;
import de.myreality.dev.littlewars.components.resources.ResourceManager;

public class TextBox extends GUIObject implements KeyListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Colors
	protected Color textColor, backgroundColor, backgroundColorHover, borderColor, focusBorderColor, currentBackground;
	
	// Chars of the text
	protected Map<Integer, GameText> content;
	
	// Entire text, temporary text
	protected String text;
	
	// Font
	protected Font font;
	
	// Cursor
	protected Cursor cursor;
	
	// Input
	protected Input input;
	
	// Lineheight
	protected int lineHeight;
	
	

	public TextBox(int x, int y, int width, int height, GameContainer gc) {
		super(x, y, gc);
		this.width = width;
		this.height = height;
		area = new Rectangle(getX(), getY(), getWidth(), getHeight());
		
		// Colors
		textColor = Color.white;
		backgroundColor = new Color(0, 0, 0, 80);
		backgroundColorHover = new Color(0, 0, 0, 100);
		borderColor = Color.black;
		currentBackground = backgroundColor;	
		focusBorderColor = ResourceManager.getInstance().getColor("COLOR_MAIN");
		
		// Content
		content = new TreeMap<Integer, GameText>();
		font = ResourceManager.getInstance().getFont("FONT_SMALL");
		text = "";
		padding = 10;
		cursor = new Cursor(padding, padding, font, gc);
		cursor.attachTo(this);
		setInput(gc.getInput());
		input.addKeyListener(this);
		input.enableKeyRepeat();
		lineHeight = 25;
	}
	
	public void finalize() {
		input.removeKeyListener(this);
	}
	
	/**
	 * Synchronize the String data with the GUI data
	 */
	private void updateContent() {
		content.clear();		
		int lastX = padding, lastY = padding;
		boolean isWordFinished = false;
		List<GameText> word = new ArrayList<GameText>();
		for (int i = 0; i < text.length(); ++i) {
			if (text.charAt(i) == ' ') {
				isWordFinished = true;
				word.clear();
			}
			GameText element = new GameText(lastX, lastY, text.substring(i, i + 1), font, gc);
			if (text.charAt(i) != ' ') {
				word.add(element);
			}
			element.attachTo(this);
			element.setColor(textColor);			
			lastX += element.getWidth();
			
			if (lastX + element.getWidth() + padding > getWidth()) {
				lastX = padding;
				lastY += lineHeight;
				
				if (!isWordFinished && !word.isEmpty()) {
					for (GameText character : word) {
						character.setX(lastX);
						character.setY(lastY);
						lastX += character.getWidth();
					}
				}
			}
			
			if (lastY + element.getHeight() + padding >= getHeight()) {
				break;
			}
			
			isWordFinished = false;
			
			content.put(i, element);
		}
	}
	
	

	public Color getTextColor() {
		return textColor;
	}

	public void setTextColor(Color textColor) {
		this.textColor = textColor;
		for (Entry<Integer, GameText> entry : content.entrySet()) {
			entry.getValue().setColor(textColor);
		}
		
	}

	public Color getBackgroundColorHover() {
		return backgroundColorHover;
	}

	public void setBackgroundColorHover(Color backgroundColorHover) {
		this.backgroundColorHover = backgroundColorHover;
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}

	public int getLineHeight() {
		return lineHeight;
	}

	public void setLineHeight(int lineHeight) {
		this.lineHeight = lineHeight;
		updateContent();
	}

	@Override
	public void draw(Graphics g) {
		// Background
		g.setColor(currentBackground);
		g.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 5);
		if (isFocused() || isHover()) {
			g.setColor(focusBorderColor);
		} else {
			g.setColor(borderColor);
		}
		g.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 5);
		
		// Content
		for (Entry<Integer, GameText> entry : content.entrySet()) {
			entry.getValue().draw(g);
		}
		
		if (isFocused()) {
			cursor.draw(g);
		}
	}

	@Override
	public void update(int delta) {
		super.update(delta);				
		cursor.update(delta);		
		
		if (isHover()) {
			currentBackground = backgroundColorHover;
		} else {
			currentBackground = backgroundColor;
		}	
		
		// Content
		for (Entry<Integer, GameText> entry : content.entrySet()) {
			GUIObject element = entry.getValue();
			element.update(delta);	
			if (element.onClick() || element.isHover() && gc.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {				
				if (gc.getInput().getMouseX() < element.getX() + element.getWidth() / 2) {				
					cursor.setX(element.getX() - getX());
					cursor.setY(element.getY() - getY());
					cursor.setIndex(entry.getKey());
				} else {
					cursor.setX(element.getX() - getX() + element.getWidth());
					cursor.setY(element.getY() - getY());
					cursor.setIndex(entry.getKey() + 1);
				}
				
				cursor.reset();
			}
		}
	}
	
	
	public static String removeCharAt(String s, int pos) {
		StringBuffer buf = new StringBuffer( s.length() - 1 );
		buf.append( s.substring(0,pos) ).append( s.substring(pos+1) );
		return buf.toString();
	}
	
	
	/**
	 * Returns true, when element contains no content
	 */
	public boolean isEmpty() {
		return text.isEmpty();
	}
	
	
	/**
	 * Sets a new text
	 */
	public void setText(String text) {
		this.text = text;
		updateContent();
	}
	
	
	
	/**
	 * Cursor Class
	 */
	class Cursor extends GUIObject {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Timer timer;
		private long interval;
		private Color color;
		private int index;
		
		public Cursor(int x, int y, Font font, GameContainer gc) {
			super(x, y, gc);
			width = 2;
			height = font.getLineHeight();
			timer = new Timer();
			interval = 500;
			color = Color.white;
			timer.start();
			index = 0;
		}

		@Override
		public void draw(Graphics g) {
			if (isVisible()) {
				g.setColor(color);
				g.fillRect(getX(), getY(), getWidth(), getHeight());
			}
		}

		@Override
		public void update(int delta) {
			super.update(delta);
			timer.update(delta);
			if (timer.getMiliseconds() > interval) {
				timer.reset();
				setVisible(!isVisible());
			}
		}
		
		public void setIndex(int i) {
			index = i;
		}
		
		public int getIndex() {
			return index;
		}
		
		public void reset() {
			setVisible(true);
			timer.reset();
		}
		
	}



	@Override
	public void inputEnded() {
		
	}

	@Override
	public void inputStarted() {
		
	}

	@Override
	public boolean isAcceptingInput() {
		return true;
	}

	@Override
	public void setInput(Input input) {
		this.input = input;
	}

	@Override
	public void keyPressed(int key, char c) {			
		if (isFocused() && isValidKey(key, c)) {
			StringBuffer buf = new StringBuffer(text);
			setText(buf.insert(cursor.getIndex(), c).toString());
			cursor.setIndex(cursor.getIndex() + 1);				
		} else if (isFocused()) {
			
			// Controls
			switch (key) {
				case Input.KEY_SPACE:
					StringBuffer buf = new StringBuffer(text);			
					setText(buf.insert(cursor.getIndex(), ' ').toString());	
					cursor.setIndex(cursor.getIndex() + 1);		
					break;
				case Input.KEY_BACK:
					if (!content.isEmpty() && cursor.getIndex() > 0) {
						setText(removeCharAt(text, cursor.getIndex() - 1));
						cursor.setIndex(cursor.getIndex() - 1);
					}
					break;
				case Input.KEY_DELETE:
					if (!content.isEmpty() && cursor.getIndex() < content.size()) {
						setText(removeCharAt(text, cursor.getIndex()));
					}
					break;
				case Input.KEY_ENTER:
					// TODO: Add a new line
					break;
				case Input.KEY_LEFT:
					if (cursor.getIndex() > 0) {
						cursor.setIndex(cursor.getIndex() - 1);
					}
					break;
				case Input.KEY_RIGHT:
					if (cursor.getIndex() < content.size()) {
						cursor.setIndex(cursor.getIndex() + 1);
					}
					break;
				case Input.KEY_TAB:
					StringBuffer tabBuffer = new StringBuffer(text);		
					for (int i = 0; i < 4; ++i) {
						setText(tabBuffer.insert(cursor.getIndex(), ' ').toString());	
						cursor.setIndex(cursor.getIndex() + 1);
					}
					break;
			}
		}		

		if (cursor.getIndex() <= content.size() && !content.isEmpty()) {
			GameText element = content.get(cursor.getIndex() - 1);
			if (element != null) {
				cursor.setX(element.getX() - getX() + element.getWidth());
				cursor.setY(element.getY() - getY());			
				cursor.reset();
			}
		} else if (content.isEmpty()) {
			cursor.setX(padding);
			cursor.setY(padding);			
			cursor.reset();
		}
		
	}
	
	
	public boolean isValidKey(int key, char c) {
		return Input.KEY_RETURN != key      && Input.KEY_BACK != key &&
			   Input.KEY_ESCAPE != key      && Input.KEY_ADD != key &&
			   Input.KEY_ENTER != key       && Input.KEY_SPACE != key &&
			   Input.KEY_DELETE != key      && Input.KEY_BACKSLASH != key &&
			   Input.KEY_F1 != key          && Input.KEY_F2 != key &&
			   Input.KEY_F3 != key          && Input.KEY_F4 != key &&
		       Input.KEY_F5 != key          && Input.KEY_F6 != key &&
			   Input.KEY_F7 != key          && Input.KEY_F8 != key &&
			   Input.KEY_F9 != key          && Input.KEY_F10 != key &&
			   Input.KEY_F11 != key         && Input.KEY_F12 != key &&
			   Input.KEY_F13 != key         && Input.KEY_F14 != key &&					   
			   Input.KEY_NUMPADENTER != key && Input.ANY_CONTROLLER != key &&
			   Input.KEY_SCROLL != key      && Input.KEY_HOME != key &&
			   Input.KEY_LEFT != key        && Input.KEY_RIGHT != key &&
			   Input.KEY_TAB != key         && Input.KEY_DOWN != key &&
			   Input.KEY_SLEEP != key       && Input.KEY_INSERT != key &&
			   Input.KEY_SYSRQ != key       && Input.KEY_END != key &&
			   Input.KEY_GRAVE != key       && Input.KEY_KANA != key &&
			   Input.KEY_YEN != key         && Input.KEY_UNLABELED != key &&
			   Input.KEY_UNDERLINE != key   && Input.KEY_APPS != key &&
			   Input.KEY_AT != key          && Input.KEY_CONVERT != key &&
			   Input.KEY_GRAVE != key       && Input.KEY_EQUALS != key &&
			   Input.KEY_KANJI != key       && Input.KEY_F15 != key &&
			   Input.KEY_LALT != key        && Input.KEY_LSHIFT != key &&
			   Input.KEY_LBRACKET != key    && Input.KEY_LMENU != key &&
			   Input.KEY_LCONTROL != key    && Input.KEY_LWIN != key;
			   
	}

	@Override
	public void keyReleased(int key, char c) {
		
	}
	
	
	public int size() {
		return text.length();
	}
	
	
	
	public String getText() {
		return text;
	}
}
