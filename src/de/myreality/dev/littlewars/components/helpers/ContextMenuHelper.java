/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mailto info@my-reality.de
 * or visit the game page: http://littlewars.my-reality.de
 * 
 * Helper for displaying a context menu
 * 
 * @version 	0.4.9
 * @author 		Miguel Gonzalez		
 */
package de.myreality.dev.littlewars.components.helpers;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.gui.Button;
import de.myreality.dev.littlewars.gui.GUIObject;
import de.myreality.dev.littlewars.gui.GameText;
import de.myreality.dev.littlewars.objects.GameObject;

public class ContextMenuHelper {
	
	private ContextMenu contextMenu;
	private Image oldBackground;
	private static Color fadeColor;
	
	private static ContextMenuHelper _instance;
	
	static {
		_instance = new ContextMenuHelper();
		fadeColor = new Color(0, 0, 0, 180);
	}
	
	private ContextMenuHelper() {
		contextMenu = null;
		oldBackground = null;
	}
	
	public static ContextMenuHelper getInstance() {
		return _instance;
	}
	
	public void render(GameContainer gc, Graphics g) {		
		if (contextMenu != null) {	
			oldBackground.draw();
			g.setColor(fadeColor);
			g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
			contextMenu.draw(g);
		}
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
		if (contextMenu != null) {
			contextMenu.update(delta);
		}
		
		if (onAbort() || gc.getInput().isKeyDown(Input.KEY_ESCAPE)) {
			GameObject.calculateMouseState(gc);
			if (getCurrentEvent() != null) {
				getCurrentEvent().onAbort(gc, sbg, delta);
			}
			ContextMenuHelper.getInstance().hide();
		}
		if (onAccept() || gc.getInput().isKeyDown(Input.KEY_ENTER)) {
			GameObject.calculateMouseState(gc);
			if (getCurrentEvent() != null) {
				getCurrentEvent().onAccept(gc, sbg, delta);
			}
			ContextMenuHelper.getInstance().hide();
		}
	}
	
	public void hide() {
		contextMenu = null;
	}
	
	public void show(GameContainer gc, String caption, String text, boolean acceptButton, boolean abortButton, ContextMenuEvent event) {
		contextMenu = new ContextMenu(gc, caption, text, acceptButton, abortButton, event);
		try {
			oldBackground = new Image(gc.getWidth(), gc.getHeight());
			gc.getGraphics().copyArea(oldBackground, 0, 0);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public ContextMenu getContextMenu() {
		return contextMenu;
	}
	
	public void show(GameContainer gc, String caption, String text, boolean acceptButton, ContextMenuEvent event) {
		show(gc, caption, text, acceptButton, false, event);
	}
	
	public void show(GameContainer gc, String caption, String text, ContextMenuEvent event) {
		show(gc, caption, text, true, true, event);
	}

	public boolean isWorking() {
		return contextMenu != null;
	}
	
	public ContextMenuEvent getCurrentEvent() {
		if (contextMenu != null) {
			return contextMenu.getEvent();
		}
		return null;
	}
	
	public boolean onAccept() {
		if (contextMenu != null) {
			return contextMenu.onAccept();
		}
		return false;
	}
	
	public boolean onAbort() {
		if (contextMenu != null) {
			return contextMenu.onAbort();
		}
		return false;
	}
	
	
	public interface ContextMenuEvent {
		
		public void onAbort(GameContainer gc, StateBasedGame sbg, int delta);
		public void onAccept(GameContainer gc, StateBasedGame sbg, int delta);
	}
	
	
	public class ContextMenu extends GUIObject {		

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Button btnAbort, btnAccept;
		private GameText message, caption;
		private Color backgroundColor, borderColor;
		private ContextMenuEvent event;
		
		public ContextMenu(GameContainer gc, String captionText, String text, boolean acceptButton, boolean abortButton, ContextMenuEvent event) {
			super(0, 0, gc);
			this.event = event;
			width = 500;
			height = 200;
			int innerPadding = 10;
			x = gc.getWidth() / 2 - width / 2;
			y = gc.getHeight() / 2 - height / 2;
			area = new Rectangle(getX(), getY(), getWidth(), getHeight());	
			try {
				if (acceptButton) {					
					btnAccept = new Button(getWidth() - (220 + innerPadding), getHeight() - (60 + innerPadding), 220, 60, ResourceManager.getInstance().getText("TXT_GAME_ACCEPT"), gc);
					btnAccept.attachTo(this);
				}
				
				if (abortButton) {
					btnAbort = new Button(innerPadding, getHeight() - (60 + innerPadding), 220, 60, ResourceManager.getInstance().getText("TXT_GAME_ABORT"), gc);	
					btnAbort.attachTo(this);
				}
			} catch (SlickException e) {
				e.printStackTrace();
			}
			
			caption = new GameText(0, 0, captionText, ResourceManager.getInstance().getFont("FONT_MENU"), gc);
			caption.attachTo(this);
			caption.setColor(ResourceManager.getInstance().getColor("COLOR_MAIN"));
			caption.setX(getWidth() / 2 - caption.getWidth() / 2);
			caption.setY(innerPadding);
			
			message = new GameText(0, 0, text, ResourceManager.getInstance().getFont("FONT_SMALL"), gc);
			message.attachTo(this);
			message.setX(getWidth() / 2 - message.getWidth() / 2);
			message.setY(70);
			backgroundColor = Color.black;
			borderColor = Color.white;
		}
		
		
		public ContextMenuEvent getEvent() {
			return event;
		}

		@Override
		public void draw(Graphics g) {
			g.setColor(backgroundColor);
			g.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 5);
			g.setColor(borderColor);
			g.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 5);
			if (btnAccept != null) {
				btnAccept.draw(g);
			}
			if (btnAbort != null) {
				btnAbort.draw(g);
			}
			caption.draw(g);
			message.draw(g);
		}

		@Override
		public void update(int delta) {
			super.update(delta);
			if (btnAccept != null) {
				btnAccept.update(delta);
			}
			if (btnAbort != null) {
				btnAbort.update(delta);
			}
		}
		
		public boolean onAccept() {
			if (btnAccept != null) {
				return btnAccept.onMouseClick();
			}
			
			return false;
		}
		
		public void setAbortTextID(String ID) {
			setAbortText(ResourceManager.getInstance().getText(ID));
		}
		
		public void setAcceptTextID(String ID) {
			setAcceptText(ResourceManager.getInstance().getText(ID));
		}
		
		public void setAbortText(String text) {
			if (text.length() > 10) {
				btnAbort.setFont(ResourceManager.getInstance().getFont("FONT_SMALL"));
				btnAccept.setFont(ResourceManager.getInstance().getFont("FONT_SMALL"));
			} else if (btnAccept.getText().length() < 11) {
				btnAccept.setFont(ResourceManager.getInstance().getFont("FONT_MENU"));
				btnAbort.setFont(ResourceManager.getInstance().getFont("FONT_MENU"));
			}
			btnAbort.setText(text);			
		}
		
		public void setAcceptText(String text) {
			if (text.length() > 10) {
				btnAbort.setFont(ResourceManager.getInstance().getFont("FONT_SMALL"));
				btnAccept.setFont(ResourceManager.getInstance().getFont("FONT_SMALL"));
			} else if (btnAbort.getText().length() < 11) {
				btnAccept.setFont(ResourceManager.getInstance().getFont("FONT_MENU"));
				btnAbort.setFont(ResourceManager.getInstance().getFont("FONT_MENU"));
			}
			btnAccept.setText(text);
		}
		
		public boolean onAbort() {
			if (btnAbort != null) {
				return btnAbort.onMouseClick();
			}
			
			return false;
		}

		
	}
}
