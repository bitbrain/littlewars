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

import de.myreality.dev.littlewars.components.Pair;
import de.myreality.dev.littlewars.components.PathLine;
import de.myreality.dev.littlewars.components.Timer;
import de.myreality.dev.littlewars.components.UnitGenerator;
import de.myreality.dev.littlewars.objects.ArmyUnit;
import de.myreality.dev.littlewars.objects.CommandoCenter;
import de.myreality.dev.littlewars.objects.cyborg.CyborgGenerator;
import de.myreality.dev.littlewars.world.Fraction;

public class CPU extends Player {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// Unit generator
	private UnitGenerator generator;
	
	// Timer for better view control
	private Timer timer;
	
	// Wait time
	private final static long WAIT = 300;
	
	// StartUnits
	private List<Pair<ArmyUnit, Integer> > startUnits;
	
	// Current opponent
	private Player opponent;

	public CPU(int id, GameContainer gc) {
		super(id, gc);
		timer = new Timer();
		startUnits = null;
	}

	@Override
	public void setFraction(Fraction fraction) {
		super.setFraction(fraction);		
	}

	@Override
	public void doPreperation(int delta) {
		super.doPreperation(delta);		
		if (!isComplete()) {
			timer.start();
			Random randomGenerator = new Random();
			// Select a random opponent
			while (opponent == null) {
				for (Player player: game.getPlayers()) {
					if (!equals(player) && randomGenerator.nextInt(20) % 7 == 0) {
						opponent = player;
						break;
					}
				}
			}
			switch (fraction.getType()) {
				case Fraction.CYBORG:
					generator = new CyborgGenerator(gc, game, this);
					startUnits = generator.generateStartUnits();
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
			if (center.distanceTo(opponentCenter) < nearestCenter.distanceTo(opponentCenter)) {
				nearestCenter = center;
			}
		}
		
		// Create a straight line between both centers and find the nearest cell
		PathLine line = new PathLine(game, this, nearestCenter, opponentCenter);
		line.findPathPosition(unit);
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

	@Override
	public void doInitialisation(int delta) {
		super.doInitialisation(delta);
		timer.update(delta);
		if (timer.getMiliseconds() > WAIT) {
			timer.reset();
		}
	}

	@Override
	public void doBattle(int delta) {
		super.doBattle(delta);
		timer.update(delta);
		if (timer.getMiliseconds() > WAIT) {
			timer.reset();
		}
	}

	@Override
	public boolean isPrepared() {
		return isComplete() && startUnits.isEmpty();
	}
	
	
	
}
