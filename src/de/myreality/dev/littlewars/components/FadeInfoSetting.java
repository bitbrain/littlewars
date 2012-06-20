package de.myreality.dev.littlewars.components;

import org.newdawn.slick.Color;

public class FadeInfoSetting {
	
	private String text;
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		Color textColor = new Color(0, 0, 0, 255);
		textColor.r = color.r;
		textColor.g = color.g;
		textColor.b = color.b;
		this.color = textColor;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getFactor() {
		return factor;
	}

	public void setFactor(float factor) {
		this.factor = factor;
	}

	private Color color;
	private float speed;
	private float factor;
	
	public FadeInfoSetting() {
		text = "(empty)";
		color = Color.white;
		speed = 0.3f;
		factor = 0.007f;
	}
}