package de.myreality.dev.littlewars.components;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;

import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.game.IngameState;
import de.myreality.dev.littlewars.ki.Player;
import de.myreality.dev.littlewars.objects.ArmyUnit;
import de.myreality.dev.littlewars.world.Fraction;

public abstract class UnitGenerator {

	protected GameContainer gc;
	protected IngameState game;
	protected Player player;
	protected Fraction fraction;
	
	public static final int UNIT_1 = 0, UNIT_CENTER = 1;
	
	public UnitGenerator(Fraction fraction, GameContainer gc, IngameState game, Player player) {
		this.fraction = fraction;
		this.player = player;
		this.game = game;
		this.gc = gc;
	}
	
	public ArmyUnit generateUnitByID(int layer) {
		return generateUnitByID(layer, 0, 0);
	}
	public abstract ArmyUnit generateUnitByID(int layer, float x, float y);
	
	public int getUnitCount() {
		return ResourceManager.getInstance().getAllUnitResources(fraction.getKey()).size();
	}
	
	public static int getUnitCredits(int id) {
		switch (id) {
			case 0: 
				return 3500;
			case 1: 
				return 15000;
		}
		
		return 0;
	}
	
	public List<Pair<ArmyUnit, Integer> > generateDefaultUnits() {
		List<Pair<ArmyUnit, Integer> > units = new ArrayList<Pair<ArmyUnit, Integer> >();
		for (int i = 0; i < getUnitCount(); ++i) {			
			units.add(new Pair<ArmyUnit, Integer>(generateUnitByID(i), getUnitCredits(i)));
		}		
		
		return units;
	}
	
	
	public List<Pair<ArmyUnit, Integer> > generateStartUnits() {
		List<Pair<ArmyUnit, Integer> > units = new ArrayList<Pair<ArmyUnit, Integer> >();
		for (int i = 0; i < getUnitCount() - 1; ++i) {		
			for (int count = 0; count < 5; ++count) {
				units.add(new Pair<ArmyUnit, Integer>(generateUnitByID(i), getUnitCredits(i)));
			}
		}		
		return units;
	}
}
