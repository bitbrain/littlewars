package de.myreality.dev.littlewars.gui.bottommenu;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.objects.Camera;
import de.myreality.dev.littlewars.objects.GUIObject;

public class ValueBar extends GUIObject {

	private Color color;
	private float percent;
	private int border;
	private Image lightImage;
	private Camera camera;
	
	public ValueBar(int x, int y, int width, int height, GameContainer gc) {
		super(x, y, gc);
		color = Color.white;
		this.width = width;
		this.height = height;
		lightImage = ResourceManager.getInstance().getImage("GUI_VALUEBAR_LIGHT");
		setBorder(2);
		camera = null;
	}
	
	public ValueBar(int x, int y, int width, int height, GameContainer gc, Camera camera) {
		this(x, y, width, height, gc);
		this.camera = camera;
	}
	
	public int getBorder() {
		return border;
	}

	public void setBorder(int border) {
		this.border = border;
	}
	

	@Override
	public void draw(Graphics g) {
		
		int camX = 0, camY = 0;
		if (camera != null) {
			camX = (int) -camera.getX();
			camY = (int) -camera.getY();
		}
		g.setColor(Color.black);
		g.fillRect(camX + getX(), camY + getY(), getWidth(), getHeight());
		g.setColor(color);
		g.fillRect(camX + getX() + border, camY + getY() + border, (getWidth() - border * 2) * (percent / 100), getHeight() - border * 2);
		lightImage.draw(camX + getX() + border, camY + getY() + border, (getWidth() - border * 2) * (percent / 100), getHeight() - border * 2);
		
	}	
	

	public Color getColor() {
		return color;
	}


	public void setColor(Color color) {
		this.color = color;
	}


	public float getPercent() {
		return percent;
	}


	public void setPercent(float percent) {
		this.percent = percent;
	}

}
