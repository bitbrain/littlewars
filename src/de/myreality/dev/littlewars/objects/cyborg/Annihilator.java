/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mail to info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * A cyborg is a regular unit with medium values
 * 
 * @version 	0.2
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.objects.cyborg;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import de.myreality.dev.littlewars.components.Debugger;
import de.myreality.dev.littlewars.components.UnitEmitter;
import de.myreality.dev.littlewars.components.resources.SpriteAnimationData;
import de.myreality.dev.littlewars.game.IngameState;
import de.myreality.dev.littlewars.objects.ArmyUnit;
import de.myreality.dev.littlewars.objects.Camera;

public class Annihilator extends ArmyUnit {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of Redeemer
	 * 
	 * @param x
	 * @param y
	 * @param gc
	 * @param cam
	 * @param map
	 * @throws SlickException
	 */
	public Annihilator(int x, int y, GameContainer gc, Camera cam, IngameState game) throws SlickException {
		super(CyborgGenerator.UNIT_1, "ANNIHILATOR", x, y, gc, cam, game);
		velocity = 0.4f;
		this.area = new Rectangle(getX(), getY(), getWidth(), getHeight());
		addUnitEmitter(SpriteAnimationData.DAMAGED, "EXPLOSION_SMALL");
		addUnitEmitter(SpriteAnimationData.DIE, "EXPLOSION_BIG");
		UnitEmitter damageEmitter = getUnitEmitter(SpriteAnimationData.DAMAGED);
		if (damageEmitter != null) {
			damageEmitter.setOffsetX(getWidth() / 2);
			damageEmitter.setOffsetY(getHeight() / 2);
			damageEmitter.setEnabled(false);
		} else {
			Debugger.getInstance().write("Damage emitter does not exist");
		}
		
		/*UnitEmitter dieEmitter = getUnitEmitter(SpriteAnimationData.DIE);
		if (dieEmitter != null) {
			dieEmitter.setOffsetX(getWidth() / 2);
			dieEmitter.setOffsetY(getHeight() / 2);
		} else {
			Debugger.getInstance().write("Die emitter does not exist");
		}*/
	}

	@Override
	public void update(int delta) {
		super.update(delta);		
	}

	@Override
	protected int getRankStrength(int rank) {
		return (int) (4.3 * Math.pow((float)rank, 1.5) + 4.5);
	}

	@Override
	protected int getRankLife(int rank) {
		return (int) (35 * Math.pow((float)rank, 1.5) + 200);
	}

	@Override
	protected int getRankDefense(int rank) {
		return (int) (2.3 * Math.pow((float)rank, 1.5) + 3);
	}

	@Override
	protected int getRankSpeed(int rank) {
		return (int) (2.5 * Math.pow((float)rank, 1.5) + 4);
	}

	@Override
	protected int getRankExperience(int rank) {
		return (int) (45 * Math.pow((float)rank, 1.5) + 400);
	}

	@Override
	protected int getRankExperienceValue(int rank) {
		return rank * 150;
	}
}
