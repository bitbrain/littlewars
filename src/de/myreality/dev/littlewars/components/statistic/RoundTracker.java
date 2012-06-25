/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mailto info@my-reality.de
 * or visit the game page: http://littlewars.my-reality.de
 * 
 * Record statistics of one game
 * 
 * @version 	0.5
 * @author 		Miguel Gonzalez		
 */
package de.myreality.dev.littlewars.components.statistic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.myreality.dev.littlewars.game.IngameState;
import de.myreality.dev.littlewars.ki.Player;

public class RoundTracker {

	// Game reference
	@SuppressWarnings("unused")
	private IngameState game;
	
	// Number of entire rounds
	private int roundCount;
	
	private Map<Player, Map<Integer, PlayerTracker> > playerTrackers;
	
	public RoundTracker(IngameState game) {
		this.game = game;
		playerTrackers = new HashMap<Player, Map<Integer, PlayerTracker> >();
		for (Player p: game.getPlayers()) {
			playerTrackers.put(p, new HashMap<Integer, PlayerTracker>());
		}
	}
	
	public List<Player> getPlayers() {
		List<Player> players = new ArrayList<Player>();
		for (Entry<Player, Map<Integer, PlayerTracker> > entry : playerTrackers.entrySet()) {
			players.add(entry.getKey());
		}
		return players;
	}
	
	public void record() {
		setRoundCount(getRoundCount() + 1);
		for (Entry<Player, Map<Integer, PlayerTracker> > entry : playerTrackers.entrySet()) {
			entry.getValue().put(getRoundCount(), new PlayerTracker(entry.getKey()));
		}
	}
	
	public void reset() {
		setRoundCount(0);
		for (Entry<Player, Map<Integer, PlayerTracker> > entry : playerTrackers.entrySet()) {
			entry.getValue().clear();
		}		
	}
	
	public void clear() {
		reset();
		playerTrackers.clear();
	}
	
	
	public int getRoundCount() {
		return roundCount;
	}

	public void setRoundCount(int roundCount) {
		this.roundCount = roundCount;
	}
	
	
	public Map<Integer, PlayerTracker> getTrackers(Player player) {
		return playerTrackers.get(player);
	}


	public class PlayerTracker {
		
		private int unitCount;
		private int moneyCount;
		
		public PlayerTracker(Player p) {
			unitCount = p.getUnits().size();
			moneyCount = p.getMoney().getRealCredits();
		}

		public int getUnitCount() {
			return unitCount;
		}
		
		
		public int getMoneyCount() {
			return moneyCount;
		}
	}
	
	
	public int getMaxMoney() {
		int maxValue = 0;
		for (Entry<Player, Map<Integer, PlayerTracker> > entry : playerTrackers.entrySet()) {
			for (Entry<Integer, PlayerTracker> sub : entry.getValue().entrySet()) {
				if (sub.getValue().getMoneyCount() > maxValue) {
					maxValue = sub.getValue().getMoneyCount();
				}
			}
		}
		return maxValue;
	}
	
	public int getMaxUnits() {
		int maxValue = 0;
		for (Entry<Player, Map<Integer, PlayerTracker> > entry : playerTrackers.entrySet()) {
			for (Entry<Integer, PlayerTracker> sub : entry.getValue().entrySet()) {
				if (sub.getValue().getUnitCount() > maxValue) {
					maxValue = sub.getValue().getUnitCount();
				}
			}
		}
		return maxValue;
	}
}
