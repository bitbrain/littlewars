package de.myreality.dev.littlewars.game;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.components.statistic.RoundTracker;
import de.myreality.dev.littlewars.components.statistic.StatisticGraph;
import de.myreality.dev.littlewars.gui.Button;
import de.myreality.dev.littlewars.gui.GUIObject;
import de.myreality.dev.littlewars.gui.GameText;
import de.myreality.dev.littlewars.gui.ZoomButton;
import de.myreality.dev.littlewars.ki.Player;

public class StatisticState extends CustomGameState {	
	
	private RoundTracker tracker;
	
	private Image backgroundImage;
	
	private Button btnNext;
	
	private StatisticGraph graph;
	
	private List<GUIObject> playerNames;
	
	// statistic caption
	private GameText caption;

	public StatisticState(int id, RoundTracker tracker) {
		super(id);
		this.tracker = tracker;
	}
	
	

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.init(container, game);
		backgroundImage = ResourceManager.getInstance().getImage("MENU_BACKGROUND");
		btnNext         = new ZoomButton(container.getWidth() - 310, container.getHeight() - 110, 250, 70, ResourceManager.getInstance().getText("TXT_GAME_NEXT") , container);
		caption         = new GameText(60, 50, "Statistic", ResourceManager.getInstance().getFont("FONT_CAPTION"), container);
		graph           = new StatisticGraph(tracker, 60, 160, container.getWidth() - 60 * 2, container.getHeight() - 150 * 2, container);
		playerNames     = new ArrayList<GUIObject>();
		// Loading player titles
		int marginRight = 90;
		int lastRight = 10;
		for (Player p: tracker.getPlayers()) {
			GameText text = new GameText(lastRight, -40, p.getName(), ResourceManager.getInstance().getFont("FONT_SMALL"), container);
			text.attachTo(graph);
			text.setColor(p.getColor());
			playerNames.add(text);	
			lastRight += text.getWidth() + marginRight;
		}
	}



	@Override
	protected void renderContent(GameContainer gc, StateBasedGame sbg,
			Graphics g) {
		backgroundImage.draw(0, 0, gc.getWidth(), gc.getHeight());
		caption.draw(g);
		btnNext.draw(g);
		graph.draw(g);
		for (GUIObject playerName: playerNames) {
			playerName.draw(g);
		}
	}

	@Override
	protected void updateContent(GameContainer gc, StateBasedGame sbg,
			int delta) {
		btnNext.update(delta);
		graph.update(delta);
		if (btnNext.onMouseClick()) {
			tracker.clear();
			sbg.enterState(LittleWars.MAINMENU_STATE);
		}
	}

}
