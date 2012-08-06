package de.myreality.dev.littlewars.gui.unit;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import de.myreality.dev.littlewars.components.FadeInfoSetting;
import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.game.IngameState;
import de.myreality.dev.littlewars.gui.GUIObject;
import de.myreality.dev.littlewars.gui.GameText;
import de.myreality.dev.littlewars.objects.GameObject;

public class FadeInfo extends GUIObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Text
	GameText content;
	
	// Setting
	FadeInfoSetting setting;
	
	GameObject target;
	

	public FadeInfo(GameObject target, FadeInfoSetting setting, GameContainer gc, FadeInfo last, IngameState game) {
		super((int)target.getX(), (int)target.getY(), gc);	
		this.target = target;
		this.setting = setting;
		content = new GameText(0, 0, setting.getText(), ResourceManager.getInstance().getFont("FONT_SMALL"), gc, game.getWorld().getCamera());
		
		content.setX(-(content.getWidth() / 2) + target.getWidth() / 2);
		if (last != null && last.getTarget().equals(target)) {
			setY(last.getY() + last.getHeight());
		}
		content.attachTo(this);	
		// Set Color		
		content.setColor(setting.getColor());
	}
	
	public GameObject getTarget() {
		return target;
	}
	
	public void clear() {
		content.detach();
		detach();
	}

	@Override
	public void update(int delta) {
		super.update(delta);
		content.setY(content.getY() - getY() - setting.getSpeed());
		content.update(delta);
		if (content.getColor().a > 0) {
			content.getColor().a -= setting.getFactor();
		}		
	}
	
	public boolean isDone() {
		return content.getColor().a <= 0;
	}
	
	

	@Override
	public int getWidth() {
		if (content != null) {
			return content.getWidth();
		} else {
			return super.getWidth();
		}
	}

	@Override
	public int getHeight() {
		if (content != null) {
			return content.getHeight();
		} else {
			return super.getHeight();
		}
	}

	@Override
	public void draw(Graphics g) {
		content.draw(g);
	}
}
