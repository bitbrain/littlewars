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

import org.newdawn.slick.GameContainer;

public class CPU extends Player {


	public CPU(int id, GameContainer gc) {
		super(id, gc);
	}

	@Override
	public void doPreperation(int delta) {
		super.doPreperation(delta);
	}

	@Override
	public void doInitialisation(int delta) {
		super.doInitialisation(delta);
	}

	@Override
	public void doBattle(int delta) {
		super.doBattle(delta);
	}

	@Override
	public boolean isPrepared() {
		return false; // TODO: Write intern unit generator
	}
	
	
	
}
