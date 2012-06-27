/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mailto info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Sound that's compatible to the configuration of the game
 * 
 * @version 	0.1
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.components.config;

import java.io.Serializable;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;


public class ConfigSound extends Sound implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of ConfigSound
	 * 
	 * @param ref Path of the image
	 * @throws SlickException
	 */
	public ConfigSound(String ref) throws SlickException {
		super(ref);
	}

	@Override
	public void loop() {
		if (Configuration.getInstance().isSoundOn()) {
			super.loop();
		}
	}

	@Override
	public void loop(float pitch, float volume) {
		if (Configuration.getInstance().isSoundOn()) {
			super.loop(pitch, volume);
		}
	}

	@Override
	public void play() {
		if (Configuration.getInstance().isSoundOn()) {
			super.play();
		}
	}

	@Override
	public void play(float pitch, float volume) {
		if (Configuration.getInstance().isSoundOn()) {
			super.play(pitch, volume);
		}
	}

	@Override
	public void playAt(float pitch, float volume, float x, float y, float z) {
		if (Configuration.getInstance().isSoundOn()) {
			super.playAt(pitch, volume, x, y, z);
		}
	}

	@Override
	public void playAt(float x, float y, float z) {
		if (Configuration.getInstance().isSoundOn()) {
			super.playAt(x, y, z);
		}
	}

}
