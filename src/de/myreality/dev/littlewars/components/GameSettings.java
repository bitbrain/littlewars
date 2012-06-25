/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mailto info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Ingame Setup
 * 
 * @version 	0.1.5
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.components;

import java.util.ArrayList;
import java.util.List;

import de.myreality.dev.littlewars.gui.MapSelector.MapConfig;
import de.myreality.dev.littlewars.ki.Player;
import de.myreality.dev.littlewars.world.Weather;

public class GameSettings {

	// Instance of a single object
	private static GameSettings _instance;
	
	// Map configuration
	private MapConfig mapConfig;
	
	// Weather on the map
	private Weather weather;
	
	// Players
	private List<Player> players;
	
	static {
		// Create a new instance at the beginning
		_instance = new GameSettings();
	}
	


	/**
	 * Constructor of the settings
	 */
	private GameSettings() {
		players = new ArrayList<Player>();
		clear();
	}	
	
	
	public Weather getWeather() {
		return weather;
	}


	public void setWeather(Weather weather) {
		if (weather.getState() == Weather.RAINING ||
		    weather.getState() == Weather.NORMAL ||
		    weather.getState() == Weather.SNOWING || 
		    weather.getState() == Weather.STORMING) {
			this.weather = weather;
		}
	}

	
	public MapConfig getMapConfig() {
		return mapConfig;
	}
	
	
	public void setMapConfig(MapConfig newConfig) {
		mapConfig = newConfig;
	}
	

	/**
	 * @return Get a current single GameSettings instance
	 */
	public static GameSettings getInstance() {
		return _instance;
	}
	
	
	/**
	 * Clears the current setting
	 */
	public void clear() {
		players.clear();
		this.mapConfig = null;
		weather = new Weather(Weather.NORMAL);
	}


	public List<Player> getPlayers() {
		return players;
	}


	public void setPlayers(List<Player> players) {
		this.players = players;
	}


	@Override
	public String toString() {
		String data = "GameSettings\n";
		
		data += "World\n";
		data += "Weather: " + weather + "\n";
		data += mapConfig + "\n";
		
		data += "\nPlayers\n";
		for (Player p: players) {
			data += p + "\n";
		}
				
		return data;
	}
	
	

}
