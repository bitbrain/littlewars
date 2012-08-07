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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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
import de.myreality.dev.littlewars.components.helpers.ContextMenuHelper.ContextMenuEvent;
import de.myreality.dev.littlewars.components.helpers.FlashHelper;
import de.myreality.dev.littlewars.components.helpers.UnitInfoHelper;
import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.components.statistic.RoundTracker;
import de.myreality.dev.littlewars.game.phases.BasicGamePhase;
import de.myreality.dev.littlewars.game.phases.BattlePhase;
import de.myreality.dev.littlewars.game.phases.InitializationPhase;
import de.myreality.dev.littlewars.game.phases.PreperationPhase;
import de.myreality.dev.littlewars.gui.PhaseInfo;
import de.myreality.dev.littlewars.gui.TopMenu;
import de.myreality.dev.littlewars.gui.UnitTileInfo;
import de.myreality.dev.littlewars.gui.bottommenu.BottomMenu;
import de.myreality.dev.littlewars.ki.Player;
import de.myreality.dev.littlewars.objects.ArmyUnit;
import de.myreality.dev.littlewars.world.Difficulty;
import de.myreality.dev.littlewars.world.GameWorld;
import de.myreality.dev.littlewars.world.Weather;

public class IngameState extends CustomGameState implements Serializable {
	
	private static final long serialVersionUID = 1L;

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
	
	private boolean previewSelected, closed;
	
	private int phase = -1;
	
	// Phases
	Map<Integer, BasicGamePhase> phases;
	
	// GameTracker
	private RoundTracker tracker;
	
	public IngameState(int stateID) {
		super(stateID);
		closed = true;
	}
	
	
	
	public boolean isClosed() {
		return closed;
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
		ArmyUnit.emitterAdded = false;
		if (!isClosed()) {
			try {
				updateAll(gc, sbg, delta);
			} catch (SlickException e) {
				e.printStackTrace();
			}
			
			if (phases.get(phase) != null) {
				phases.get(phase).update(gc, sbg, delta);
			}
			
			for (Player p: getPlayers()) {
				if (p.isDefeated()) {				
					FlashHelper.getInstance().flash(p.getName() + " " + ResourceManager.getInstance().getText("TXT_INFO_KILLED"), 1000, gc); 
					if (p.equals(currentPlayer)) {
						Player next = getNextPlayer(p);
						next.getMoney().addCredits(500);				
						setCurrentPlayer(next, gc);
						if (next.isClientPlayer()) {
							getTracker().record();
						}
					}
					removePlayer(p);
					break;
				}
				
				if (getPlayers().size() == 1) {
					endGame(sbg, gc, p);
				}
			}
		}
		
		ArmyUnit.updateParticles(delta);		
	}

	public List<Player> getPlayers() {
		return players;
	}
	
	
	public Player getNextPlayer(Player current) {

		Player player = null;
		boolean found = false;
		
		for (Player p: players) {
			if (found) {
				player = p;
				break;
			}

			if (p.equals(current)) {
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
		if (!isClosed()) {
			if (world != null) {
				world.update(gc, delta);
			}
			
			if (gc.getInput().isKeyPressed(Input.KEY_SPACE) && clientPlayer.isCurrentPlayer()) {
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
			
			if (topMenu.getBtnQuit().onMouseClick()) {
				ContextMenuHelper.getInstance().show(gc, ResourceManager.getInstance().getText("TXT_GAME_WARNING"), 
												     ResourceManager.getInstance().getText("TXT_INFO_LEAVE"), new ContextMenuEvent() { 
	
					@Override
					public void onAbort(GameContainer gc, StateBasedGame sbg,
							int delta) {
						
					}
	
					@Override
					public void onAccept(GameContainer gc, StateBasedGame sbg,
							int delta) {
						endGame(sbg, gc);					
					}				
				});
			}
		}
	}
	
	public void renderAll(GameContainer gc, StateBasedGame sbg, Graphics g) {
		if (world != null) {
			world.render(gc, g);
			ArmyUnit.renderParticles(world.getCamera());
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
		closed = false;
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
		
		// Set the current game phase
		phase = PREPERATION;
		setCurrentPlayer(currentPlayer, container, true);
	}
	
	
	public void addUnitInfo(ArmyUnit unit, GameContainer gc) {
		UnitTileInfo info = new UnitTileInfo(unit, world.getCamera(), 0, 0, gc);
		info.attachTo(unit);
		tileInfos.add(info);
	}	
	
	
	public void setCurrentPlayer(Player player, GameContainer gc) {
		setCurrentPlayer(player, gc, false);
	}
	
	public void setCurrentPlayer(Player player, GameContainer gc, boolean instant) {
		currentPlayer = player;
		if (!currentPlayer.getUnits().isEmpty()) {
			ArmyUnit unit = currentPlayer.getUnits().get(0);
			world.focusCameraOnObject(unit, gc, instant);
		}
		
		// Flash Message
		String message = "";
		
		if (currentPlayer.isClientPlayer()) {
			message = "Baue deine Einheiten!";
		}
		
		FlashHelper.getInstance().flash(new PhaseInfo(currentPlayer, getPhase(), message, gc), 1200, gc);		
	}
	
	
	public void removeUnitInfo(ArmyUnit unit) {
		for (UnitTileInfo info : tileInfos) {
			if (info.getUnit().equals(unit)) {
				tileInfos.remove(info);
				break;
			}
		}
	}
	
	
	public int getPhaseID() {
		return phase;
	}
	
	public BasicGamePhase getPhase() {
		return phases.get(phase);
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
		
		for (Player p : players) {
			p.clear();
		}
		
		world.close();		
		tileInfos.clear();
		world = null;	
		ArmyUnit.freeParticleSystem();
	}
	
	
	public boolean isPreviewSelected() {
		return previewSelected;
	}
	
	public void setPreviewSelected(boolean value) {
		previewSelected = value;
	}
	
	
	/**
	 * Function in order to save the game
	 */
	public boolean saveToFile(String filePath) {
		// TODO: Fix serialization problem
		final String FOLDER = "saves/";
		// Check if folder exists
		File folder = new File(FOLDER);	
		File file = new File(FOLDER + filePath);	
		try {
			if (!folder.exists()) {
				folder.mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fileOut = new FileOutputStream(FOLDER + filePath);
	        ObjectOutputStream out = new ObjectOutputStream(fileOut);
	        out.writeObject(world);
	        out.close();
	        fileOut.close();
	        return true;
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		return false;
	}
	
	/**
	 * Function in order to load a game
	 */
	public boolean loadFromFile(String file) {
		return true;
	}
	
	public void endGame(StateBasedGame game, GameContainer gc, Player winner) {
		closed = true;
		if (winner != null) {
			FlashHelper.getInstance().flash(winner.getName() + " " + ResourceManager.getInstance().getText("TXT_INFO_WIN"), 1000, gc);
		} 
		GameSettings.getInstance().clear();			
		game.addState(new StatisticState(LittleWars.STATISTIC_STATE, tracker));
		try {
			game.getState(LittleWars.STATISTIC_STATE).init(gc, game);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		close();
		game.enterState(LittleWars.STATISTIC_STATE);
		
	}
	
	public void endGame(StateBasedGame game, GameContainer gc) {
		endGame(game, gc, null);
	}
	
	public void removePlayer(Player player) {
		for (int i = 0; i < players.size(); ++i) {
			if (players.get(i).equals(player)) {			
				for (ArmyUnit unit : players.get(i).getUnits()) {
					removeUnitInfo(unit);
				}
				players.get(i).clear();				
				players.remove(i);
				break;
			}
		}
	}
	
}
