/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mailto info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Music that's compatible to the configuration of the game
 * 
 * @version 	0.1
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.components.config;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;


public class ConfigMusic extends Music {

	/**
	 * Constructor of ConfigMusic
	 * 
	 * @param ref Path of the image
	 * @throws SlickException
	 */
	public ConfigMusic(String ref) throws SlickException {
		super(ref);		
		this.setVolume(0.3f);
	}

	@Override
	public void loop() {
		if (Configuration.getInstance().isMusicOn()) {
			super.loop();
			this.setVolume(0.3f);
		}
	}

	@Override
	public void loop(float pitch, float volume) {
		if (Configuration.getInstance().isMusicOn()) {
			super.loop(pitch, volume);
			this.setVolume(0.3f);
		}
	}

	@Override
	public void play() {
		if (Configuration.getInstance().isMusicOn()) {
			super.play();
			this.setVolume(0.3f);
		}
	}

	@Override
	public void play(float pitch, float volume) {
		if (Configuration.getInstance().isMusicOn()) {
			super.play(pitch, volume);
			this.setVolume(0.3f);
		}
	}

	@Override
	public void resume() {
		if (Configuration.getInstance().isMusicOn()) {
			super.resume();
			this.setVolume(0.3f);
		}
	}
	
	

}
