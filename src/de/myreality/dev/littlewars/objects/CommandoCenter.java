package de.myreality.dev.littlewars.objects;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import de.myreality.dev.littlewars.components.UnitGenerator;
import de.myreality.dev.littlewars.game.IngameState;
import de.myreality.dev.littlewars.world.Player;

public class CommandoCenter extends ArmyUnit {
	
	protected CommandoCenter(String resourceID, int x, int y,
			GameContainer gc, Camera cam, IngameState game) throws SlickException {
		super(UnitGenerator.UNIT_CENTER, resourceID, x, y, gc, cam, game);
		this.area = new Rectangle(getX(), getY(), getWidth(), getHeight());
	}
	
	

	@Override
	public void draw(Graphics g) {
		super.draw(g);
	}

	@Override
	public void setPlayer(Player player) {		
		if (!player.equals(this.player)) {
			if (this.player != null) {
				this.player.getSpawnArea().removeArea(this);
			}		
			super.setPlayer(player);
			if (this.player != null && this.player.getSpawnArea() != null) {
				this.player.getSpawnArea().writeArea(this);
			}		
		}
	}



	@Override
	public void update(int delta) {
		super.update(delta);		
	}
	
	
	@Override
	protected int getRankStrength(int rank) {
		return 0;
	}

	@Override
	protected int getRankLife(int rank) {
		return 0;
	}

	@Override
	protected int getRankDefense(int rank) {
		return 0;
	}

	@Override
	protected int getRankSpeed(int rank) {
		return 0;
	}

	@Override
	protected int getRankExperience(int rank) {
		return 0;
	}

	@Override
	protected int getRankExperienceValue(int rank) {
		return rank * 1500;
	}
	
	public int getSpawnRange() {
		// TODO: Implement spawn range algorithm
		return 9;		
	}
}
