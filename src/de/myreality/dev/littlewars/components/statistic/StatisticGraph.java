package de.myreality.dev.littlewars.components.statistic;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.ki.Player;
import de.myreality.dev.littlewars.objects.GUIObject;

public class StatisticGraph extends GUIObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RoundTracker tracker;
	private Color clrBackground;
	private static int padding = 30;
	private Font number;
	@SuppressWarnings("unused")
	private int maxCredits, maxUnits;
	
	public StatisticGraph(RoundTracker tracker, int x, int y, int width, int height, GameContainer gc) {
		super(x, y, gc);
		this.width = width;
		this.height = height;
		area = new Rectangle(getX(), getY(), getWidth(), getHeight());
		this.tracker = tracker;
		clrBackground = new Color(0, 0, 0, 100);
		number = ResourceManager.getInstance().getFont("FONT_SMALL");		
		maxCredits = tracker.getMaxMoney();
		maxUnits = tracker.getMaxUnits();
	}

	@Override
	public void draw(Graphics g) {
		g.setLineWidth(1);
		g.setColor(clrBackground);
		g.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 5);
		g.setColor(Color.black);
		g.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 5);
		drawGraph(g);
	}
	
	
	public void drawGraph(Graphics g) {
		int graphWidth = getWidth() - padding;
		int innerPadding = 20;
		
		// Horizontal
		if (tracker.getRoundCount() > 0) {
			int distanceX = (graphWidth - padding - innerPadding) / tracker.getRoundCount();	
			@SuppressWarnings("unused")
			int distanceY = 10;
			int height = 5;
			int radius = 3;
			g.setLineWidth(2);
			int xPos = (int) (getX() + padding);
			int yPos = (int) (getY() + getHeight() - padding);
			int lastX = xPos;
			 lastY = yPos;
			for (int i = 0; i <= tracker.getRoundCount(); ++i) {		
				g.setColor(Color.white);
				int numberWidth = number.getWidth(String.valueOf(i));
				g.drawLine(xPos, getY() + (getHeight() - padding) - height / 2, xPos, getY() + (getHeight() - padding) + height / 2);
				number.drawString(xPos - numberWidth / 2, getY() + (getHeight() - padding) + height, String.valueOf(i), ResourceManager.getInstance().getColor("COLOR_MAIN"));
				if (i > 0) {
					for (Player p: tracker.getPlayers()) {	
						lastY = yPos;
						if (tracker.getTrackers(p).get(i) != null) {
							yPos = (int) ((int) (getY() + getHeight() - padding) - ((float)(getHeight() - padding * 2 - innerPadding) * ((float)tracker.getTrackers(p).get(i).getUnitCount() / (float)maxUnits)));
							g.setColor(p.getColor());
							g.drawLine(lastX, lastY, xPos, yPos);		
							g.fillOval(xPos - radius, yPos - radius, radius * 2, radius * 2);
						} 				
						break;
					}
				}
				lastX = xPos;
				xPos += distanceX;
			}
		}
		
		g.setLineWidth(2);		
		g.setColor(Color.white);
		// Lines
		g.drawLine(getX() + padding, getY() + (getHeight() - padding), getX() + graphWidth, getY() + (getHeight() - padding));
		g.drawLine(getX() + padding, getY() + (getHeight() - padding), getX() + padding, getY() + padding);
		// Arrows
		
		g.setLineWidth(1);
	}

}
