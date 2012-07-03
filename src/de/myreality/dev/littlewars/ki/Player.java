/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mail to info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Player of one army
 * 
 * @version 	0.2
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.ki;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Sound;

import de.myreality.dev.littlewars.components.SpawnArea;
import de.myreality.dev.littlewars.components.UnitGenerator;
import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.game.IngameState;
import de.myreality.dev.littlewars.objects.ArmyUnit;
import de.myreality.dev.littlewars.objects.CommandoCenter;
import de.myreality.dev.littlewars.world.Difficulty;
import de.myreality.dev.littlewars.world.Fraction;
import de.myreality.dev.littlewars.world.GameWorld;

public class Player implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int PL1 = 1, PL2 = 2, PL3 = 3, PL4 = 4, PL5 = 5, PL6 = 6, NONE = 0;	
	
	private int id;
	
	protected Difficulty difficulty;
	
	protected Fraction fraction;
	
	protected Color color;
	
	protected List<ArmyUnit> units;
	
	protected List<CommandoCenter> centers;
	
	protected SpawnArea spawnArea;
	
	protected GameContainer gc;
	
	protected IngameState game;
	
	protected String name;
	
	protected Money money;
	
	public Player(int id, GameContainer gc) {
		this.setId(id);
		this.color = Color.white;
		this.gc = gc;
		units = new ArrayList<ArmyUnit>();	
		centers = new ArrayList<CommandoCenter>();
		money = new Money();
		spawnArea = null;
	}
	
	public void setGame(IngameState game) {
		this.game = game;
		spawnArea = new SpawnArea(game, this);		
	}	
	
	public SpawnArea getSpawnArea() {
		return spawnArea;
	}
	
	public GameContainer getGameContainer() {
		return gc;
	}
	
	public boolean isCPU() {
		return difficulty.getState() != Difficulty.NONE && difficulty.getState() != Difficulty.PLAYER;
	}
	
	public boolean isPlayer() {
		return difficulty.getState() != Difficulty.NONE && difficulty.getState() == Difficulty.PLAYER;
	}
	
	public boolean isUndefined() {
		return !isCPU() && !isPlayer();
	}
	
	public void addArmyUnit(ArmyUnit unit) {
		if (game != null && game.getWorld() != null) {
			units.add(unit);
			game.getWorld().addRenderTarget(unit, gc);
			unit.setPlayer(this);	
			
			// Focus, when it's not client players turn
			if (!game.getClientPlayer().isCurrentPlayer()) {
				game.getWorld().focusCameraOnObject(unit, gc);
			}
			if (unit.getID().equals(UnitGenerator.UNIT_CENTER)) {				
				addCommandoCenter((CommandoCenter)unit);
			}
		}
	}
	
	public void removeArmyUnit(ArmyUnit unit) {
		if (game.getWorld() != null) {
			units.remove(unit);
			game.getWorld().removeRenderTarget(unit);
			
			// Delete commando center as well
			if (unit instanceof CommandoCenter) {
				removeCommandoCenter((CommandoCenter) unit);
			}
		}
	}
	
	public static Color getDefaultColor(int playerID) {
		
		switch (playerID) {
			case PL1:
				return ResourceManager.getInstance().getColor("COLOR_PLAYER1");
			case PL2:
				return ResourceManager.getInstance().getColor("COLOR_PLAYER2");
			case PL3:
				return ResourceManager.getInstance().getColor("COLOR_PLAYER3");
			case PL4:
				return ResourceManager.getInstance().getColor("COLOR_PLAYER4");
			case PL5:
				return ResourceManager.getInstance().getColor("COLOR_PLAYER5");
			case PL6:
				return ResourceManager.getInstance().getColor("COLOR_PLAYER6");	
		}
		return null;		
	}

	public int getId() {
		return id;
	}	


	public void setId(int id) {
		this.id = id;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}

	public Fraction getFraction() {
		return fraction;
	}

	public void setFraction(Fraction fraction) {
		this.fraction = fraction;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return "Player [id=" + id + ", difficulty=" + difficulty
				+ ", fraction=" + fraction + ", color=" + color + "]";
	}

	public List<ArmyUnit> getUnits() {
		return units;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	public void clear() {
		units.clear();
	}
	

	public boolean isClientPlayer() {
		return equals(game.getClientPlayer());
	}
	
	public boolean isCurrentPlayer() {
		return equals(game.getCurrentPlayer());
	}
	
	public void update(int delta) {
		money.update(delta);
	}
	
	public Money getMoney() {
		return money;
	}
	
	
	/**
	 * Get the next unit that's selected.
	 */
	public void selectNextUnit(GameWorld world, GameContainer gc) {
		if (world != null) {
			ArmyUnit select = null;
			boolean focusFound = false;
			ArmyUnit focused = (ArmyUnit)world.getFocusObject();
			for (ArmyUnit unit: units) {
				if (focusFound) {
					select = unit;
					break;
				} // TODO: Select only next, that can move
				if (unit.equals(focused)) {
					focusFound = true;
					continue;
				}
			}
			
			if (focusFound && select == null) {
				select = units.get(0);
			}
			
			world.setFocusObject(select);
			world.setClickedObject(select);
			if (select != null) {
				select.setOnClicked(true);
				world.focusCameraOnObject(select, gc);
			}
		}
	}

	public List<CommandoCenter> getCommandoCenters() {
		return centers;
	}

	public void addCommandoCenter(CommandoCenter commandoCenter) {
		for (CommandoCenter center: centers) {
			if (center.equals(commandoCenter)) {
				return;
			}
		}
		centers.add((CommandoCenter)commandoCenter);
	}
	
	public void removeCommandoCenter(CommandoCenter commandoCenter) {
		spawnArea.removeArea(commandoCenter);
		for (int i = 0; i < centers.size(); ++i) {
			if (centers.get(i).equals(commandoCenter)) {
				centers.remove(i);
			}
		}
	}
	
	public boolean hasAvailableUnits() {		
		for (ArmyUnit unit : units) {
			if (unit.getRemainingSpeed() > 0 && !(unit instanceof CommandoCenter)) {
				return true;
			}
		}
		return false;
	}
	
	public void activateUnits() {
		for (ArmyUnit unit : units) {
			unit.activate();
		}
	}
	
	public boolean isUnitMoving() {
		
		for (ArmyUnit unit : units) {
			if (!unit.isTargetArrived()) {
				return true;
			}
		}		
		return false;		
	}
	
	public void setCenterRadiusEnabled(boolean value) {
		spawnArea.setVisible(value);
	}
	
	public class Money {
		private Sound moneySound;		
		private int credits, creditValue, changeFactor, lastAdd;
		
		public Money() {
			credits = 0;
			creditValue = credits;
			changeFactor = 0;
			moneySound = ResourceManager.getInstance().getSound("SOUND_MONEY");
		}
		
		
		
		public int getCredits() {
			return credits;
		}
		
		public void addCredits(int value) {
			creditValue += value;
			changeFactor += value;
			lastAdd = value;
		}
		
		public boolean reduceCredits(int value) {
			creditValue -= value;
			changeFactor += -value;
			lastAdd = value;
			if (creditValue < 0) {
				creditValue = 0;
				changeFactor = 0;
				return false;
			}
			return true;
		}
		
		public int getRealCredits() {
			return creditValue;
		}
		
		
		public void update(int delta) {		
			if (isClientPlayer()) {
				if (changeFactor != 0) {
					float value = 0;
					if (changeFactor < 0) {
						value = Math.round(-1f * Math.ceil(1 + (lastAdd / 1000)) * delta);
					} else {
						value = Math.round(1.5f * Math.ceil(1 + (lastAdd / 1000)) * delta);
					}

					int lastFactor = changeFactor;
					changeFactor -= value;
					
					if (lastFactor < 0 && changeFactor < 0 ||
						lastFactor > 0 && changeFactor > 0) {
						credits += value;
						moneySound.play(4.5f, 0.4f);
					} else {
						changeFactor = 0;
						credits = getRealCredits();
						moneySound.play(4.5f, 0.4f);
					}
				}
			}
		}
	}
	
	public ArmyUnit getNextUnit(ArmyUnit current) {
		
		if (units.isEmpty()) {
			return null;
		}
		
		boolean found = false;
		ArmyUnit next = null;
		for (ArmyUnit unit: units) {
			if (found) {
				return unit;
			}
			if (unit.equals(current)) {
				found = true;
				continue;
			}
		}
		
		if (!found) {
			return units.get(0);
		}
			
		return next;
	}
	
	// Empty methods in order to overwrite it
	public void doPreperation(int delta) {
		
	}
	
	public void doInitialisation(int delta) {
		
	}
	
	public void doBattle(int delta) {
		
	}
	
	public boolean isPrepared() {
		return true;
	}
	
	public boolean isDefeated() {
		return getCommandoCenters().isEmpty();
	}
}
