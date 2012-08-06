/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mail to info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * CPU class with KI logic
 * 
 * @version 	0.6.2
 * @author 		Miguel Gonzalez		
 */
package de.myreality.dev.littlewars.ki;

import java.util.List;
import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.util.pathfinding.Path;

import de.myreality.dev.littlewars.components.Pair;
import de.myreality.dev.littlewars.components.PathLine;
import de.myreality.dev.littlewars.components.Timer;
import de.myreality.dev.littlewars.components.UnitGenerator;
import de.myreality.dev.littlewars.objects.ArmyUnit;
import de.myreality.dev.littlewars.objects.CommandoCenter;
import de.myreality.dev.littlewars.objects.cyborg.CyborgGenerator;
import de.myreality.dev.littlewars.world.Fraction;

public class CPU extends Player {


	private static final long serialVersionUID = 1L;
	
	// Unit generator
	private UnitGenerator generator;
	
	// Timer for better view control
	private Timer timer;
	
	// Wait time
	private final static long WAIT = 400;
	
	// StartUnits
	private List<Pair<ArmyUnit, Integer> > startUnits, defaultUnits;
	
	// Current opponent
	private Player opponent; // TODO: Fix opponent bug
	
	// Current unit
	private ArmyUnit currentUnit;

	public CPU(int id, GameContainer gc) {
		super(id, gc);
		timer = new Timer();
		startUnits = null;
	}

	@Override
	public void setFraction(Fraction fraction) {
		super.setFraction(fraction);		
	}
	
	/**
	 * Buys the best unit
	 */
	public ArmyUnit buyUnit() {
		// 1. Select the best unit
		Pair<ArmyUnit, Integer> selected = null;
		for (int i = 0; i < defaultUnits.size(); ++i) {
			if (selected == null) {
				selected = defaultUnits.get(i);
				continue;
			} else {
				if (defaultUnits.get(i).getSecond() > selected.getSecond() && getMoney().getRealCredits() - defaultUnits.get(i).getSecond() >= 0) {
					selected = defaultUnits.get(i);
				}
			}
		}
		ArmyUnit unit = null;
		// 2. Buy the unit		
		if (getMoney().reduceCredits(selected.getSecond())) {
			unit = generator.generateUnitByID(selected.getFirst().getID(), 0, 0);
			unit.setX(0);
			unit.setY(0);
			addArmyUnit(unit);
		} else return null;
		
		return unit;
	}

	@Override
	public void doPreperation(int delta) {
		super.doPreperation(delta);		
		if (!isComplete()) {
			timer.start();
			Random randomGenerator = new Random();
			// Select a random opponent
			while (opponent == null || equals(opponent)) {
				int randomIndex = randomGenerator.nextInt(game.getPlayers().size());
				opponent = game.getPlayers().get(randomIndex);
			}
			switch (fraction.getType()) {
				case Fraction.CYBORG:
					generator = new CyborgGenerator(gc, game, this);
					startUnits = generator.generateStartUnits();
					defaultUnits = generator.generateDefaultUnits();
					break;
				case Fraction.HUMAN:
					break;
			}
		}
		timer.update(delta);
		if (timer.getMiliseconds() > WAIT) {
			timer.reset();
			if (isComplete() && !isPrepared()) {
				// Get one single unit
				ArmyUnit currentUnit = addUnitFromPool();
				
				// Set the position near of the current opponent
				setUnitPosition(currentUnit);
				
				// Add the unit to the entire system
				addArmyUnit(currentUnit);
			}
		}

	}
	
	
	/**
	 * Set the position of the current unit by considering the current opponent
	 */
	private void setUnitPosition(ArmyUnit unit) {		
		
		// Center of the opponent
		CommandoCenter opponentCenter = null;
		for (CommandoCenter center: opponent.getCommandoCenters()) {
			opponentCenter = center;
			break;
		}
		
		// Find the nearest command center
		CommandoCenter nearestCenter = null;
		for (CommandoCenter center: centers) {
			if (nearestCenter == null) {
				nearestCenter = center;
				continue;
			}
			if (center.distanceTo(opponentCenter) < nearestCenter.distanceTo(opponentCenter) && center.isFinalPosition()) {
				nearestCenter = center;
			}
		}
		
		// Create a straight line between both centers and find the nearest cell
		if (nearestCenter != null && opponentCenter != null) {
			PathLine line = new PathLine(game, this, nearestCenter, opponentCenter);
			line.findPathPosition(unit);
			
			if (unit instanceof CommandoCenter) {
				((CommandoCenter)unit).setFinalPosition(true);
			}
		}
	}
	
	/**
	 * Removes the first unit of the pool and returns it
	 */
	public ArmyUnit addUnitFromPool() {
		ArmyUnit unit = null;
		if (!startUnits.isEmpty()) {
			unit = startUnits.get(0).getFirst();
			startUnits.remove(0);
		}
		return unit;
	}
	
	public boolean isComplete() {
		return opponent != null && generator != null && startUnits != null;
	}
	
	public void attackNearestUnit() {
		// Select next unit
		while (currentUnit instanceof CommandoCenter || currentUnit == null || currentUnit.getRemainingSpeed() < 1) {
			if (currentUnit == null) {
				currentUnit = units.get(0);
			} else {
				currentUnit = getNextUnit(currentUnit);
			}
		}
		
		// Detect nearest enemy
		if (!opponent.isDefeated()) {
			ArmyUnit enemyUnit = opponent.getUnits().get(0);
			for (ArmyUnit unit : opponent.getUnits()) {
				if (currentUnit.distanceTo(unit) < currentUnit.distanceTo(enemyUnit)) {
					enemyUnit = unit;
				}
			}
			Path movePath = game.getWorld().getPathFinder().findPath(currentUnit, currentUnit.getTileX(), currentUnit.getTileY(), enemyUnit.getTileX(), enemyUnit.getTileY());
			
			if (movePath != null && movePath.getLength() > 0) {
				currentUnit.moveAlongPath(movePath);	
				
				if (currentUnit.getRealPathLength() == 0) {
					currentUnit.attackTargetEnemy();
					game.getWorld().focusCameraOnObject(currentUnit, gc);
				}
			} else {
				// Enemy is not reachable, wait at the current position
				currentUnit.setRemainingSpeed(0);
			}
		} else {
			currentUnit.setRemainingSpeed(0);
		}
	}

	@Override
	public boolean doInitialisation(int delta) {
		super.doInitialisation(delta);
		timer.update(delta);
		if (timer.getMiliseconds() > WAIT) {
			timer.reset();
			
			ArmyUnit bought = buyUnit();
			
			if (bought == null && !ArmyUnit.isUnitLoosingLife() && !ArmyUnit.isUnitMoving() && hasAvailableUnits() && !ArmyUnit.isUnitDying() && !ArmyUnit.isUnitBusy()) {				
				attackNearestUnit();	
			} else if (bought != null) {
				setUnitPosition(bought);
			}
			
			if (bought == null && getCommandoCenters().size() == getUnits().size()) {
				return false;
			}
		}
		if (!hasAvailableUnits()) {
			return false;
		}
		
		if (currentUnit != null && !currentUnit.isTargetArrived()) {
			game.getWorld().setFocusObject(currentUnit);
		}
		
		return true;
	}

	@Override
	public void doBattle(int delta) {
		if (!ArmyUnit.isUnitBusy()) {
			super.doBattle(delta);
			timer.update(delta);
			if (timer.getMiliseconds() > WAIT) {
				timer.reset();
				
				if (!ArmyUnit.isUnitLoosingLife() && !ArmyUnit.isUnitMoving() && hasAvailableUnits() && !ArmyUnit.isUnitDying() &&  !ArmyUnit.isUnitBusy()) {
					attackNearestUnit();				
				}
			}
		}
		
		if (currentUnit != null && !currentUnit.isTargetArrived()) {
			game.getWorld().focusCameraOnObject(currentUnit, gc, true);
		}

	}

	@Override
	public boolean isPrepared() {
		return isComplete() && startUnits.isEmpty();
	}
	
	public void setOpponent(Player newOpponent) {
		opponent = newOpponent;
	}
	
		
}
