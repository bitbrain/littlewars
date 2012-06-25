package de.myreality.dev.littlewars.game;

import java.io.IOException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.DeferredResource;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.state.StateBasedGame;

import de.myreality.dev.littlewars.components.Debugger;
import de.myreality.dev.littlewars.components.GameSettings;
import de.myreality.dev.littlewars.components.config.Configuration;
import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.gui.GameText;
import de.myreality.dev.littlewars.gui.LoadingBar;
import de.myreality.dev.littlewars.ki.Player;
import de.myreality.dev.littlewars.world.Fraction;

public class LoadingState extends CustomGameState {
	
	private DeferredResource nextResource;
	private GameText text;
	private LoadingBar bar;
	private Image background;
	
	
	public LoadingState(int stateID) {
		super(stateID);
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		super.init(gc, sbg);
		text = new GameText(0, 40, ResourceManager.getInstance().getText("TXT_GAME_LOADING") + "...", ResourceManager.getInstance().getFont("FONT_MENU"), gc);
		text.setX(gc.getWidth() / 2 - text.getWidth() / 2);
		text.setY(gc.getHeight() / 2 - text.getHeight() / 2 - 50);
		bar = new LoadingBar(0, 0, (int) (gc.getWidth() / 3 * 2), 20, gc);
		bar.setX(gc.getWidth() / 2 - bar.getWidth() / 2);
		bar.setY(gc.getHeight() / 2 - bar.getHeight() / 2);
		background = ResourceManager.getInstance().getImage("MENU_BACKGROUND");
		
		// Load resources
		try {
			// Fraction resources
			boolean humanLoading = false, cyborgLoading = false;
			
			for (Player p : GameSettings.getInstance().getPlayers()) {
				if (humanLoading && cyborgLoading) {
					break;
				}
				if (p.getFraction().getType() == Fraction.HUMAN) {
					humanLoading = true;
				}
				if (p.getFraction().getType() == Fraction.CYBORG) {
					cyborgLoading = true;
				}	
			}
			
			// TODO: Fix bug of early loading
			if (humanLoading) {		
				// Images
				ResourceManager.getInstance().loadResources("res/images/units/human/resources.xml");
			}
			if (cyborgLoading) {
				// Images
				ResourceManager.getInstance().loadResources("res/images/units/cyborg/resources.xml");
			}
			// Daytime resources		
			ResourceManager.getInstance().loadResources("res/music/worlds/desert/music.xml", true);	
			ResourceManager.getInstance().loadResources("res/sounds/worlds/desert/sounds.xml");
			
			if (humanLoading) {	
				// Images
				//ResourceManager.getInstance().loadResources("res/images/units/human/resources.xml");
				// Animations
				ResourceManager.getInstance().loadResources("res/animations/human/animations.xml");		
				// Sounds
				ResourceManager.getInstance().loadResources("res/sounds/" + Configuration.getInstance().getLocale() + "/units/human/sounds.xml");
				// Units
				ResourceManager.getInstance().loadResources("res/units/human/units.xml");
			}
			if (cyborgLoading) {
				// Images
				//ResourceManager.getInstance().loadResources("res/images/units/cyborg/resources.xml");
				// Animations
				ResourceManager.getInstance().loadResources("res/animations/cyborg/animations.xml");
				// Sounds
				ResourceManager.getInstance().loadResources("res/sounds/" + Configuration.getInstance().getLocale() + "/units/cyborg/sounds.xml");
				// Units
				ResourceManager.getInstance().loadResources("res/units/cyborg/units.xml");
			}
			
		} catch (SlickException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
			Debugger.getInstance().write("Loading resources..");
	}

	@Override
	public void renderContent(GameContainer gc, StateBasedGame sbg, Graphics g) {
		background.draw(0, 0, gc.getWidth(), gc.getHeight());		
		text.draw(g);
		bar.draw(g);
	}
	
	
	private void continueGame(GameContainer gc, StateBasedGame sbg) throws SlickException {
		LoadingList.setDeferredLoading(false);
		sbg.addState(new IngameState(LittleWars.INGAME_STATE));			
		sbg.getState(LittleWars.INGAME_STATE).init(gc, sbg);		
		sbg.enterState(LittleWars.INGAME_STATE);
	}

	@Override
	public void updateContent(GameContainer gc, StateBasedGame sbg, int delta) {
		if (nextResource != null) {
			try {
				nextResource.load();
				Debugger.getInstance().write("- Load resource '" + nextResource.getDescription() + "'");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			nextResource = null;
		}
		
		if (LoadingList.get().getRemainingResources() > 0) {
			nextResource = LoadingList.get().getNext();
		} else {
			Debugger.getInstance().write("All resources have been loaded.");
			try {
				continueGame(gc, sbg);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
		
		int total = LoadingList.get().getTotalResources();
		int loaded = LoadingList.get().getTotalResources() - LoadingList.get().getRemainingResources();
		
		float percent = (float) loaded / (float) total;
		
		bar.setPercent(percent * 100);
	}

}
