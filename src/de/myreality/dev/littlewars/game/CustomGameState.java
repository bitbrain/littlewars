/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mailto info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Wrapper class for each game state
 * 
 * @version 	0.4
 * @author 		Miguel Gonzalez		
 */
package de.myreality.dev.littlewars.game;

import java.io.File;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.imageout.ImageOut;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

import de.myreality.dev.littlewars.components.Debugger;
import de.myreality.dev.littlewars.components.helpers.ContextMenuHelper;
import de.myreality.dev.littlewars.components.helpers.FlashHelper;
import de.myreality.dev.littlewars.components.helpers.PopupHelper;
import de.myreality.dev.littlewars.objects.GameObject;

public abstract class CustomGameState extends BasicGameState {
	
	public static GameState current;
	// State ID
	private int stateID = -1;
	private Image target;
	
	public CustomGameState(int id) {
		stateID = id;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		current = this;
	}
	

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		if (!ContextMenuHelper.getInstance().isWorking()) {
			renderContent(container, game, g);
			PopupHelper.getInstance().render(g);
		}
		ContextMenuHelper.getInstance().render(container, g);		
		FlashHelper.getInstance().render(g);
		Debugger.getInstance().drawGameInfo(container,  g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {				
		ContextMenuHelper.getInstance().update(container, game, delta);	
		if (!ContextMenuHelper.getInstance().isWorking()) {
			updateContent(container, game, delta);
		}
		PopupHelper.getInstance().update(delta);
		FlashHelper.getInstance().update(delta);
		GameObject.calculateMouseState(container);
		// F2 -> Enable/Disable Debugging
		if (container.getInput().isKeyPressed(Input.KEY_F1)) {
			Debugger.getInstance().setEnabled(!Debugger.getInstance().isEnabled());
		}
		// F3 -> Enable/Disable fullscreen
		if (container.getInput().isKeyPressed(Input.KEY_F3)) {
			// TODO: fix fullscreen bug
			container.setFullscreen(!container.isFullscreen());			
		}
		// F12 -> screenshot from current Graphics
		if (container.getInput().isKeyPressed(Input.KEY_F12)) {
			takeScreenShot(container, ".jpg");
		}
		// F5 -> Debug Report
		if (getID() != LittleWars.BUG_REPORT_STATE && container.getInput().isKeyDown(Input.KEY_F5)) {
			game.addState(new BugReportState(LittleWars.BUG_REPORT_STATE, getID()));
			game.getState(LittleWars.BUG_REPORT_STATE).init(container, game);
			game.enterState(LittleWars.BUG_REPORT_STATE);
		}
	}
	
	
	protected abstract void renderContent(GameContainer container, StateBasedGame game, Graphics g);	
	protected abstract void updateContent(GameContainer container, StateBasedGame game, int delta);

	@Override
	public int getID() {
		return stateID;
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		current = this;
		PopupHelper.getInstance().setCurrentState(current);
		super.enter(container, game);
	}


	@Override
	public void leave(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.leave(container, game);
		if (getID() == LittleWars.INGAME_STATE) {
			
		}
	}
	
	
	
	/** Takes a screenshot, fileExt can be .png, .gif, .tga, .jpg or .bmp */
	   public void takeScreenShot(GameContainer container, String fileExt) {

	      try {
	         File FileSSDir = new File("screenshots");
	         if (!FileSSDir.exists()) {
	            FileSSDir.mkdirs();
	         }

	         int number = 0;
	         String screenShotFileName = "screenshots/" + "screenshot_" + number + fileExt;
	         File screenShot = new File(screenShotFileName);

	         while (screenShot.exists()) {
	            number++;
	            screenShotFileName = "screenshots/" + "screenshot_" + number + fileExt;
	            screenShot = new File(screenShotFileName);
	         }
	         Debugger.getInstance().write("Screenshot outputting to: " + screenShotFileName);
	         FlashHelper.getInstance().flash("Screenshot has been saved successfully.", 500, container);

	         target = new Image(container.getWidth(), container.getHeight());

	         Graphics g = container.getGraphics();
	         g.copyArea(target, 0, 0);
	         ImageOut.write(target, screenShotFileName);

	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	   }
	
}
