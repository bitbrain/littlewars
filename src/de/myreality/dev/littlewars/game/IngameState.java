/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mailto info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Provides a gamestate of showing the game's ingame logic
 * 
 * @version 	0.2
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.StateBasedGame;

import de.myreality.dev.littlewars.components.GameSettings;
import de.myreality.dev.littlewars.components.helpers.ContextMenuHelper;
import de.myreality.dev.littlewars.components.helpers.UnitInfoHelper;
import de.myreality.dev.littlewars.components.helpers.ContextMenuHelper.ContextMenuEvent;
import de.myreality.dev.littlewars.components.helpers.FlashHelper;
import de.myreality.dev.littlewars.components.statistic.RoundTracker;
import de.myreality.dev.littlewars.game.phases.BasicGamePhase;
import de.myreality.dev.littlewars.game.phases.BattlePhase;
import de.myreality.dev.littlewars.game.phases.InitializationPhase;
import de.myreality.dev.littlewars.game.phases.PreperationPhase;
import de.myreality.dev.littlewars.gui.TopMenu;
import de.myreality.dev.littlewars.gui.bottommenu.BottomMenu;
import de.myreality.dev.littlewars.gui.unit.UnitTileInfo;
import de.myreality.dev.littlewars.ki.Player;
import de.myreality.dev.littlewars.objects.ArmyUnit;
import de.myreality.dev.littlewars.world.Difficulty;
import de.myreality.dev.littlewars.world.GameWorld;
import de.myreality.dev.littlewars.world.Weather;

public class IngameState extends CustomGameState {
	
	// Game phases
	public static final int PREPERATION = 0, INIT = 1, BATTLE = 2;
	
	private List<Player> players;
	
	private Player clientPlayer, currentPlayer;
	
	private List<UnitTileInfo> tileInfos;
	
	// World
	private GameWorld world;
	
	// Bottom menu
	private BottomMenu bottomMenu;
	
	// Top menu
	private TopMenu topMenu;	
	
	private boolean previewSelected;
	
	private int phase = -1;
	
	// Phases
	Map<Integer, BasicGamePhase> phases;
	
	// GameTracker
	private RoundTracker tracker;
	
	public IngameState(int stateID) {
		super(stateID);
	}
	
	

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);	
		if (phase == -1) {
			newGame(container);
		}
	}


	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		super.init(gc, sbg);
		world = null;
		bottomMenu = new BottomMenu(150, this, gc);
		topMenu = new TopMenu(40, this, gc);
		players = new ArrayList<Player>();		
	}

	@Override
	public void renderContent(GameContainer gc, StateBasedGame sbg, Graphics g) {
		renderAll(gc, sbg, g);
	}

	@Override
	public void updateContent(GameContainer gc, StateBasedGame sbg, int delta) {
		try {
			updateAll(gc, sbg, delta);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
		if (phases.get(phase) != null) {
			phases.get(phase).update(gc, delta);
		}
	}

	public List<Player> getPlayers() {
		return players;
	}
	
	
	public Player getNextPlayer() {

		Player player = null;
		boolean found = false;
		
		for (Player p: players) {
			if (found) {
				player = p;
				break;
			}

			if (p.equals(currentPlayer)) {
				found = true;				
				continue;
			}
		}
		if (found && player == null) {
			player = players.get(0);
		}
		
		return player;
	}
	
	
	public void updateAll(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if (world != null) {
			world.update(gc, delta);
		}
		
		if (gc.getInput().isKeyPressed(Input.KEY_SPACE)) {
			clientPlayer.selectNextUnit(world, gc);
		}	
		
		for (UnitTileInfo info : tileInfos) {
			info.update(delta);			
		}
		
		
		for (Player p: players) {
			p.update(delta);
		}
		
		UnitInfoHelper.getInstance().update(delta);
		
		bottomMenu.update(delta);
		topMenu.update(delta);
		
		if (topMenu.getBtnQuit().onClick()) {
			ContextMenuHelper.getInstance().show(gc, "Achtung", "Sie verlassen nun das Spiel.", new ContextMenuEvent() {

				@Override
				public void onAbort(GameContainer gc, StateBasedGame sbg,
						int delta) {
					
				}

				@Override
				public void onAccept(GameContainer gc, StateBasedGame sbg,
						int delta) {
					world.close();
					GameSettings.getInstance().clear();	
					sbg.addState(new StatisticState(LittleWars.STATISTIC_STATE, tracker));
					try {
						sbg.getState(LittleWars.STATISTIC_STATE).init(gc, sbg);
					} catch (SlickException e) {
						e.printStackTrace();
					}
					sbg.enterState(LittleWars.STATISTIC_STATE);					
				}				
			});
		}
	}
	
	public void renderAll(GameContainer gc, StateBasedGame sbg, Graphics g) {
		if (world != null) {
			world.render(gc, g);
		}
		
		// Render infos
		for (UnitTileInfo info : tileInfos) {
			Rectangle rect = (Rectangle) world.getCamera().getArea();
			rect.setX(0);
			rect.setY(0);
			Shape objArea = info.getParent().getArea();
			float oldX = objArea.getX(), oldY = objArea.getY();
			
			objArea.setLocation(objArea.getX() - world.getCamera().getX(), objArea.getY() -  world.getCamera().getY());
			if (rect.intersects(objArea)) {
				info.draw(g);
			}
			
			objArea.setLocation(oldX, oldY);
		}
		UnitInfoHelper.getInstance().render(g);
		bottomMenu.draw(g);	
		topMenu.draw(g);
	
	}



	public GameWorld getWorld() {
		return world;
	}



	public Player getClientPlayer() {
		return clientPlayer;
	}
	
	
	
	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	
	public RoundTracker getTracker() {
		return tracker;
	}

	public void newGame(GameContainer container) throws SlickException {
		// Load the map
		players.clear();		
		String mapPath = GameSettings.getInstance().getMapConfig().getMapSource();
		Weather mapWeather = GameSettings.getInstance().getWeather();
		List<Player> plList = GameSettings.getInstance().getPlayers();
		String name = GameSettings.getInstance().getMapConfig().getMapName();
		
		// Load the map
		world = new GameWorld(name, mapPath, container, GameSettings.getInstance().getMapConfig().getMapMusic(), 
				              GameSettings.getInstance().getMapConfig().getMapSound(), this);
		world.setWeather(mapWeather);
		world.getCamera().setBottomMenu(bottomMenu);
		world.getCamera().setTopMenu(topMenu);
		world.setBottomMenu(bottomMenu);
		//world.setDebug(true);
		bottomMenu.setGameWorld(world);
		tileInfos = new ArrayList<UnitTileInfo>();
		
		for (Player p : plList) {
			if (bottomMenu.getPlayer() == null) {
				// Single Player Mode
				if (p.getDifficulty().getState() == Difficulty.PLAYER) {
					bottomMenu.setPlayer(p);
					clientPlayer = p;
					currentPlayer = clientPlayer;
				}
			}
			players.add(p);	
			p.setGame(this);
		}
		
		world.loadConfiguration(container);

		// Clear the settings
		GameSettings.getInstance().clear();	
		
		// New game Tracker
		tracker = new RoundTracker(this);
		
		// Add Game phases
		phases = new HashMap<Integer, BasicGamePhase>();
		phases.put(PREPERATION, new PreperationPhase(this));
		phases.put(INIT, new InitializationPhase(this));
		phases.put(BATTLE, new BattlePhase(this));		
		setCurrentPlayer(currentPlayer, container);
		// Set the current game phase
		phase = PREPERATION;
		FlashHelper.getInstance().flash("Phase: Vorbereitung", 2500, container);
		setCurrentPlayer(currentPlayer, container);
	}
	
	
	public void addUnitInfo(ArmyUnit unit, GameContainer gc) {
		UnitTileInfo info = new UnitTileInfo(unit, world.getCamera(), 0, 0, gc);
		info.attachTo(unit);
		tileInfos.add(info);
	}	
	
	
	public void setCurrentPlayer(Player player, GameContainer gc) {
		currentPlayer = player;
		if (!currentPlayer.getUnits().isEmpty()) {
			ArmyUnit unit = currentPlayer.getUnits().get(0);
			world.focusCameraOnObject(unit, gc);
		}
		
		FlashHelper.getInstance().flash(currentPlayer.getName() + " ist an der Reihe.", 500, gc);		
	}
	
	
	public void removeUnitInfo(ArmyUnit unit) {
		for (UnitTileInfo info : tileInfos) {
			if (info.getUnit().equals(unit)) {
				tileInfos.remove(info);
				break;
			}
		}
	}
	
	
	public int getPhase() {
		return phase;
	}
	
	public void setPhase(int phase) {
		this.phase = phase;
	}	
	
	public TopMenu getTopMenu() {
		return topMenu;
	}
	
	public BottomMenu getBottomMenu() {
		return bottomMenu;
	}
	
	public boolean isPlayerPrepared(Player player) {
		return true;
	}
	
	public void close() {
		world.close();
		players.clear();
		tileInfos.clear();
		world = null;	
	}
	
	
	public boolean isPreviewSelected() {
		return previewSelected;
	}
	
	public void setPreviewSelected(boolean value) {
		previewSelected = value;
	}
	
}
