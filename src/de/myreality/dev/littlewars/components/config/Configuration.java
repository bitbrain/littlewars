/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mailto info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Provide a configuration class to store, load and save specific settings
 * 
 * @version 	0.1
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.components.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import de.myreality.dev.littlewars.components.Debugger;


public class Configuration implements Serializable {
	
	// ID version for serialization
	private static final long serialVersionUID = 1;
	
	// single instance of the configuration
	private static Configuration _instance;
	
	// filename
	private String file;
	
	// resolution
	private Resolution resolution;
	
	// vsync allowed, music on, sound on
	private boolean vsync, musicOn, soundOn;	

	// language
	private String locale;
	
	// framerate
	private int fps;
	
	// Save the state "true", when something has changed
	boolean changed;
	
	static {
		// Create a single instance of the configuration
		_instance = new Configuration("config/settings.cfg");
	}
	
	
	
	/**
	 * @return Instance of the single configuration instance
	 */
	public final static Configuration getInstance(){
		return _instance;
	}
	
	
	
	/**
	 * Constructor of Configuration
	 * 
	 * @param file configuration file (*.cfg)
	 */
	public Configuration(String file) {
		this.file = file;
		setDefault();
		try {
			loadFromFile();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	/**
	 * @return Current resolution of the configuration
	 */
	public Resolution getResolution() {
		return resolution;
	}

	
	
	/**
	 * Loads a full configuration from file
	 * 
	 * @throws ClassNotFoundException
	 */
	private void loadFromFile() throws ClassNotFoundException {
		try {
			FileInputStream fileIn = new FileInputStream(file);
	        ObjectInputStream in = new ObjectInputStream(fileIn);
	        
	        Configuration tmpConfig = (Configuration) in.readObject();
	        
	        // Apply changes
	        resolution = tmpConfig.resolution;
	        vsync = tmpConfig.vsync;
	        musicOn = tmpConfig.musicOn;
	        soundOn = tmpConfig.soundOn;
	        fps = tmpConfig.fps;
	        locale = tmpConfig.locale;
	        
	        fileIn.close();
	        in.close();
	    } catch (FileNotFoundException e) {
	        Debugger.getInstance().write("File '" + file + "' doesn't exist yet. It will be created after making setting changes.");
	    } catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * Save the current configuration to file
	 */
	private void saveToFile() {
		try {
			FileOutputStream fileOut = new FileOutputStream(file);
	        ObjectOutputStream out = new ObjectOutputStream(fileOut);
	        out.writeObject(this);
	        out.close();
	        fileOut.close();
	        changed = false;
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	
	
	/**
	 * Enable or disable the vertical synchronization
	 * 
	 * @param value new state
	 * @return
	 */
	public Configuration setVSync(boolean value) {
		vsync = value;		
		return this;
	}
	
	
	
	/**
	 * Sets a new resolution
	 * 
	 * @param resolution new resolution
	 * @return current configuration (this)
	 * @throws SlickException
	 */
	public Configuration setResolution(Resolution resolution) throws SlickException {		
		
		if (resolution != null) {
			this.resolution = resolution;			
		}		
		return this;
	}

	/**
	 * Save the current setup
	 * 
	 * @throws SlickException 
	 */
	public void apply(AppGameContainer gc) throws SlickException {
		gc.setVSync(vsync);	
		gc.setMusicOn(musicOn);
		gc.setSoundOn(soundOn);
		if (changed) {
			saveToFile();
		}
	}
	
	
	
	/**
	 * Load default values
	 */
	public void setDefault() {
		fps = 60;
		vsync = false;
		musicOn = true;
		locale = "en";
		resolution = new Resolution(800, 600);
	}

	
	/**
	 * Sets the vertical synchronization
	 * 
	 * @param vsync new vsync value
	 * @return current configuration (this)
	 */
	public Configuration setVsync(boolean vsync) {
		this.vsync = vsync;
		changed = true;
		return this;
	}

	
	
	/**
	 * Sets music on or off
	 * 
	 * @param musicOn
	 * @return current configuration (this)
	 */
	public Configuration setMusicOn(boolean musicOn) {
		this.musicOn = musicOn;
		changed = true;
		return this;
	}

	
	
	/**
	 * Sets the language code
	 * 
	 * @param locale
	 * @return current configuration (this)
	 */
	public Configuration setLocale(String locale) {
		this.locale = locale;
		changed = true;
		return this;
	}

	
	
	/**
	 * Sets the framerate
	 * 
	 * @param fps
	 * @return current configuration (this)
	 */
	public Configuration setFps(int fps) {
		this.fps = fps;
		changed = true;
		return this;
	}
	
	
	
	/** 
	 * @return current sound state
	 */
	public boolean isSoundOn() {
		return soundOn;
	}

	
	
	/**
	 * Enable or disable the sound
	 * 
	 * @param soundOn
	 * @return current configuration (this)
	 */
	public Configuration setSoundOn(boolean soundOn) {
		this.soundOn = soundOn;
		changed = true;
		return this;
	}

	
	
	/**
	 * @return state of vertical synchronization
	 */
	public boolean isVsync() {
		return vsync;
	}

	
	
	/**
	 * @return state of music
	 */
	public boolean isMusicOn() {
		return musicOn;
	}

	
	/**
	 * @return current language code
	 */
	public String getLocale() {
		return locale;
	}

	
	
	/**
	 * @return current fps
	 */
	public int getFps() {
		return fps;
	}
}
