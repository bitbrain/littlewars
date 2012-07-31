/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mail to info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * A special emitter for playing animations once and default emitting
 * 
 * @version 	0.6.12
 * @author 		Miguel Gonzalez		
 */
package de.myreality.dev.littlewars.components;

import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleSystem;

import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.objects.ArmyUnit;

public class UnitEmitter {
	
	// Default emitter
	private ConfigurableEmitter emitter;
	
	// Offset on the screen that depends on target position
	private int offsetX, offsetY;
	
	// Target for the emitter
	private ArmyUnit target;
	
	// Target system
	private ParticleSystem system;

	public UnitEmitter(ArmyUnit target, String ID, ParticleSystem system) {
		this.emitter = ResourceManager.getInstance().getNewEmitter(ID);
		this.target = target;
		this.system = system;
		system.addEmitter(this.emitter);
	}

	public void update(int delta) {
		if (emitter != null) {
			emitter.setPosition(target.getX() + offsetX, target.getY() + offsetY, false);
		}
	}
	
	public ConfigurableEmitter getInner() {
		return emitter;
	}
	
	public void setEnabled(boolean enabled) {
		if (emitter != null) {
			emitter.setEnabled(enabled);
		}
	}

	public int getOffsetX() {
		return offsetX;
	}

	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}
	
	public void releaseFromSystem() {
		system.removeEmitter(emitter);
		emitter = null;
	}
	
	
}
