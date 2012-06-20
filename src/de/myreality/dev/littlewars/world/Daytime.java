/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mail to info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Daytime of the world
 * 
 * @version 	0.2
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.world;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import de.myreality.dev.littlewars.components.GameClock;
import de.myreality.dev.littlewars.components.config.Configuration;
import de.myreality.dev.littlewars.components.fading.FadeColor;
import de.myreality.dev.littlewars.components.resources.ResourceManager;

public class Daytime {

	public static final int MORNING = 0, DAY = 1, EVENING = 2, NIGHT = 3;
	
	public static Daytime morning, day, evening, night;
	
	private int state;

	private FadeColor color;
	private boolean onTime;
	
	private GameClock clock;
	
	static {
		morning = new Daytime(MORNING);
		day = new Daytime(DAY);
		evening = new Daytime(EVENING);
		night = new Daytime(NIGHT);
	}	
	
	public Color getColor() {
		return color;
	}
	
	public Daytime(int state) {
		setDaytime(state);		
		int format = GameClock.US;		
		if (Configuration.getInstance().getLocale().equals("de")) {
			format = GameClock.EU;
		}
		clock = new GameClock(format);
		onTime = false;
	}
	
	public void setDaytime(int state) {
		this.state = state;
		switch (state) {
			case MORNING:
				color = new FadeColor(255, 204, 0, 60);
				break;
			case DAY:
				color = new FadeColor(255, 255, 255, 0);
				break;
			case EVENING:
				color = new FadeColor(255, 114, 0, 80);
				break;
			case NIGHT:
				color = new FadeColor(10, 0, 80, 100);
				break;
		}
	}
	
	public void changeDaytime(int state) {
		this.state = state;
		switch (state) {
			case MORNING:
				color.fadeTo(new Color(255, 255, 0, 20), 10000);
				break;
			case DAY:
				color.fadeTo(new Color(255, 255, 255, 50), 10000);
				break;
			case EVENING:
				color.fadeTo(new Color(105, 50, 0, 60), 10000);
				break;
			case NIGHT:
				color.fadeTo(new Color(10, 0, 80, 100), 10000);
				break;
		}
	}
	
	public void start() {
		clock.start();
	}
	
	public int getNextState() {
		int nextState = state;
		nextState++;
		if (nextState > NIGHT) {
			nextState = MORNING;
		}
		return nextState;
		
	}
	
	public void update(GameContainer gc, int delta) {
		clock.tick(delta);
		color.update(delta);
		if (onTime && clock.getHours() % 6 == 0 && clock.getMinutes() == 0) {
			changeDaytime(getNextState());
			onTime = false;
		}
		
		if (!onTime) {
			if (!(clock.getHours() % 6 == 0 && clock.getMinutes() == 0)) {
				onTime = true;
			}
		}
	}
	
	public int getState() {
		return state;
	}

	@Override
	public boolean equals(Object obj) {
		return state == ((Daytime)obj).state;
	}
	
	
	public String getName() {
		
		switch (state) {
			case MORNING:
				return ResourceManager.getInstance().getText("TXT_DAYTIME_MORNING");
			case DAY:
				return ResourceManager.getInstance().getText("TXT_DAYTIME_DAY");
			case EVENING:
				return ResourceManager.getInstance().getText("TXT_DAYTIME_EVENING");
			case NIGHT:
				return ResourceManager.getInstance().getText("TXT_DAYTIME_NIGHT");
		}
		
		return "";
	}
	
	
	@Override
	public String toString() {
		return clock.getTimeString();
	}
	
	
	public void draw(GameContainer gc, Graphics g) {
		g.setColor(color);
		g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
	}

	
}
