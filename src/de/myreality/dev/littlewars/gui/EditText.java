package de.myreality.dev.littlewars.gui;

import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;

public class EditText extends TextBox {

	public EditText(int x, int y, int width, String text, Font font, GameContainer gc) {
		super(x, y, width,  font.getLineHeight() + 40, gc);	
		this.font = font;	
		setText(text);
		// TODO: Fix display bug
	}

}
