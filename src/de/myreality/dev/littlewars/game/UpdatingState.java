package de.myreality.dev.littlewars.game;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import de.myreality.dev.littlewars.components.Updater;
import de.myreality.dev.littlewars.components.helpers.FlashHelper;
import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.gui.GameText;
import de.myreality.dev.littlewars.gui.LoadingBar;

public class UpdatingState extends CustomGameState {

	private GameText text, percentText, sizeText;
	private LoadingBar bar;
	private Image background;
	private ExecutorService executor;
	private Updater updater;
	private boolean isUpdating;
	
	public UpdatingState(int stateID) {
		super(stateID);
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		super.init(gc, sbg);
		text = new GameText(0, 40, ResourceManager.getInstance().getText("TXT_GAME_SEARCHING") + "...", ResourceManager.getInstance().getFont("FONT_MENU"), gc);
		text.setX(gc.getWidth() / 2 - text.getWidth() / 2);
		text.setY(gc.getHeight() / 2 - text.getHeight() / 2 - 50);
		percentText = new GameText(0, 0, "", ResourceManager.getInstance().getFont("FONT_MENU"), gc);
		percentText.setColor(ResourceManager.getInstance().getColor("COLOR_MAIN"));
		bar = new LoadingBar(0, 0, (int) (gc.getWidth() / 3 * 2), 20, gc);
		bar.setX(gc.getWidth() / 2 - bar.getWidth() / 2);
		bar.setY(gc.getHeight() / 2 - bar.getHeight() / 2);
		bar.sizable(false);
		sizeText = new GameText(0, 30, "0MB/0MB", ResourceManager.getInstance().getFont("FONT_SMALL"), gc);
		sizeText.setColor(new Color(100, 100, 100, 255));
		sizeText.attachTo(bar);
		background = ResourceManager.getInstance().getImage("MENU_BACKGROUND");
		updater = new Updater();
		executor = Executors.newFixedThreadPool(1);
		// Start searching
		executor.execute(updater);
		isUpdating = false;
	}

	@Override
	public void renderContent(GameContainer gc, StateBasedGame sbg, Graphics g) {
		background.draw(0, 0, gc.getWidth(), gc.getHeight());		
		text.draw(g);
		if (updater.hasFoundUpdate() && !updater.isPreperationPhase()) {
			percentText.draw(g);
			sizeText.draw(g);
		}
		bar.draw(g);		
	}
	
	public String getSizeString() {
		return String.valueOf(Math.round((double)updater.getCurrentSize() / 1024)) + "KB/" + String.valueOf(Math.round((double)updater.getDownloadSize() / 1024)) + "KB";
	}

	@Override
	public void updateContent(GameContainer gc, StateBasedGame sbg, int delta) {		
		bar.update(delta);
		try {
			if (updater.hasFoundUpdate()) {
				sizeText.setText(getSizeString());
				sizeText.setX(bar.getWidth() / 2 - sizeText.getWidth() / 2);
			}
			if (updater.hasFoundUpdate() && updater.isPreperationPhase()) {
				text.setText(ResourceManager.getInstance().getText("TXT_GAME_CALC_UPDATING") + "...");
				text.setX(gc.getWidth() / 2 - text.getWidth() / 2);
			} else if (!isUpdating && updater.hasFoundUpdate()) {
				bar.setPercent(0);
				bar.sizable(true);
				isUpdating = true;
				text.setText(ResourceManager.getInstance().getText("TXT_GAME_UPDATING") + "...");
				text.setX(gc.getWidth() / 2 - text.getWidth() / 2 - 40);
				percentText.setX(text.getX() + text.getWidth() + 10);
				percentText.setY(text.getY());			
			} else if (!updater.hasFoundUpdate() && updater.isDone()) {
				executor.shutdown();
				if (!updater.isReachable()) {
					FlashHelper.getInstance().flash("Error: Can't reach update server.", 1000, gc);
				}				
				sbg.addState(new IntroState(LittleWars.INTRO_STATE));
				sbg.getState(LittleWars.INTRO_STATE).init(gc, sbg);
				sbg.enterState(LittleWars.INTRO_STATE);
			} else if (updater.hasFoundUpdate() && updater.isDone()) {
				// Restart the application
				updater.openPatchnotes();
				executor.shutdown();
				Runtime.getRuntime().exec("java -jar littlewars.jar");
				gc.exit();		
			}
		} catch (SlickException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (updater.hasFoundUpdate()) {
			percentText.setText(String.valueOf(Math.round(updater.getPercent())) + "%");
			bar.setPercent(updater.getPercent());
		}			
	}
}
