/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mail to info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * World's weather
 * 
 * @version 	0.2
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.world;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

import de.myreality.dev.littlewars.components.Pair;
import de.myreality.dev.littlewars.components.config.Configuration;
import de.myreality.dev.littlewars.components.resources.ResourceManager;

public class Weather implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int NORMAL = 0, RAINING = 1, SNOWING = 2, STORMING = 3;
	
	public static Weather normal, raining, snowing, storming;
	
	// Particle System
	private Map<Integer, ParticleSystem> particleSystems;
	private Map<Integer, Pair<Integer, Integer> > positions;
	// Strength
	private int strength;
	
	static {
		normal = new Weather(NORMAL);
		raining = new Weather(RAINING);
		snowing = new Weather(SNOWING);
		storming = new Weather(STORMING);
	}
	
	private int state;
	
	public Weather(int state) {
		this.state = state;
		this.strength = 30;
		particleSystems = new HashMap<Integer, ParticleSystem>();	
		positions = new HashMap<Integer, Pair<Integer, Integer> >();
		// TODO: Fix weather problem
		//loadWeather();
	}
	
	@SuppressWarnings("unused")
	private void loadWeather() {
		
		for (int i = 0; i < strength; ++i) {
			ParticleSystem system = null;
			try {
				system = ParticleIO.loadConfiguredSystem("res/particles/system/default.xml");
				ConfigurableEmitter emitter = null;
				switch (state) {
					case NORMAL:				
						break;
					case RAINING:				
						emitter = ResourceManager.getInstance().getNewEmitter("PARTICLE_RAIN");
						break;
					case SNOWING:
						emitter = ResourceManager.getInstance().getNewEmitter("PARTICLE_SNOW");
						break;
					case STORMING:
						emitter = ResourceManager.getInstance().getNewEmitter("PARTICLE_STORM");
						break;				
				}
				
				system.addEmitter(emitter);			
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (system != null) {
				int resX = Configuration.getInstance().getResolution().getX();
				int resY = Configuration.getInstance().getResolution().getY();
				Pair<Integer, Integer> pos = new Pair<Integer, Integer>((int) (Math.random() * resX), (int) (Math.random() * resY));
				positions.put(i, pos);
				particleSystems.put(i,system);
			}
		}	
	}
	
	private void unloadWeather() {
		for (int i = 0; i != particleSystems.size(); i++) {
			particleSystems.get(i).removeAllEmitters();
		}
	}
	
	public void finalize() {
		unloadWeather();
	}
	
	public int getState() {
		return state;
	}

	@Override
	public boolean equals(Object obj) {
		return state == ((Weather)obj).state;
	}
	
	public String getName() {
		
		switch (state) {
			case NORMAL:
				return ResourceManager.getInstance().getText("TXT_WEATHER_NORMAL");
			case RAINING:
				return ResourceManager.getInstance().getText("TXT_WEATHER_RAINING");
			case SNOWING:
				return ResourceManager.getInstance().getText("TXT_WEATHER_SNOWING");
			case STORMING:
				return ResourceManager.getInstance().getText("TXT_WEATHER_STORMING");
		}
		
		return "";
	}

	@Override
	public String toString() {
		return getName();
	}
	
	public void draw(GameContainer gc, Graphics g) {
		// Zufaellig zeichnen
		for (int i = 0; i != particleSystems.size(); i++) {
			//Pair<Integer, Integer> pos = positions.get(i);
			
			//particleSystems.get(i).render(pos.getFirst(), pos.getSecond());
		}
	}
	
	public void update(GameContainer gc, int delta) {
		for (int i = 0; i != particleSystems.size(); i++) {
			//particleSystems.get(i).update(delta);
		}
	}
	
	
}
