/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mailto info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Provide scrollable credits
 * 
 *
 * To load correct XML content, create a structure like this:
 * 
 * <credits>
 * 		<credit caption="caption1">
 * 			<item>Item1</item>
 * 			<item>Item2</item>
 * 		</credit>
 *		 <credit caption="caption2">
 * 			<item>Item1</item>
 * 		</credit>
 * 	</credits>
 * 
 * @version 	0.0.1
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.components;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.gui.GameText;
import de.myreality.dev.littlewars.objects.GameObject;
import de.myreality.dev.littlewars.objects.Movable;

public class CreditsShow extends GameObject {
	
	// List of all credits
	List<Credit> credits;
	
	/**
	 * Single credit (contains a caption and many content elements)
	 * 
	 * @author Miguel Gonzalez
	 */
	class Credit extends GameObject implements Movable {

		// Caption element
		Caption caption;
		
		// List of all content elements
		List<Content> names;
		
		
		/**
		 * Caption of a single credit
		 * 
		 * @author Miguel Gonzalez
		 */
		class Caption extends GameText implements Movable {

			/**
			 * Constructor of Caption
			 * 
			 * @param x x-coordinate on the screen
			 * @param y y-coordinate on the screen
			 * @param text text content of the caption
			 * @param font font of the caption
			 * @param gc GameContainer
			 */
			public Caption(int x, int y, String text, Font font,
					GameContainer gc) {
				super(x, y, text, font, gc);
				setColor(ResourceManager.getInstance().getColor("COLOR_MAIN"));
			}

			
			
			
			@Override
			public void move(int direction, int delta) {
				switch (direction) {
				case Movable.TOP:
					y -= delta * 0.05;
					break;
				case Movable.BOTTOM:
					y += delta * 0.05;
					break;
				case Movable.LEFT:
					// Do nothing
					break;
				case Movable.RIGHT:
					// Do nothing
					break;
				}
			}			
			
			@Override
			public void update(int delta) {
				super.update(delta);				
			}

			
			
			@Override
			public int getWidth() {
				return super.getWidth();
			}

			
			
			@Override
			public int getHeight() {
				return super.getHeight() + 20;
			}
		}
		
		
		
		/**
		 * Single content element of one credit
		 * 
		 * @author Miguel Gonzalez
		 */
		class Content extends GameText implements Movable {

			/**
			 * Constructor of Content
			 * 
			 * @param x x-coordinate on the screen
			 * @param y y-coordinate on the screen
			 * @param text text content
			 * @param font font element
			 * @param gc GameContainer
			 */
			public Content(int x, int y, String text, Font font,
					GameContainer gc) {
				super(x, y, text, font, gc);
				setColor(new Color(255, 255, 255, 255));
			}

			
			
			@Override
			public void move(int direction, int delta) {
				switch (direction) {
					case Movable.TOP:
						y -= delta * 0.05;
						break;
					case Movable.BOTTOM:
						y += delta * 0.05;
						break;
					case Movable.LEFT:
						// Do nothing
						break;
					case Movable.RIGHT:
						// Do nothing
						break;
				}				
			}	
			
			
			
			@Override
			public void update(int delta) {
				super.update(delta);				
			}
		}

		
		/**
		 * Constructor of credit
		 * 
		 * @param x x-coordinate on the screen
		 * @param y y-coordinate on the screen
		 * @param caption text content of the credit
		 * @param gc GameContainer
		 */
		public Credit(int x, int y, String caption, GameContainer gc) {
			super(x, y, gc);
			names = new ArrayList<Content>();
			this.caption = new Caption((int)getX() + (getWidth() / 2 - ResourceManager.getInstance().getFont("FONT_MENU").getWidth(caption) / 2), 
									   (int)getY(), caption, ResourceManager.getInstance().getFont("FONT_MENU"), gc);
		}
		
		
		
		@Override
		public void setY(float y) {
			super.setY(y);			
			caption.setY(y);				
			float offset = getY() + caption.getHeight();
			
			if (!names.isEmpty()) {
				for (Content c : names) {
					c.setY(offset);
					offset += c.getHeight();
				}
			}
			
		}
		
		
		/**
		 * Adds a new content element to the credit
		 * 
		 * @param text contained text
		 */
		public void addContent(String text) {
			float offset = getY() + caption.getHeight();
			
			if (!names.isEmpty()) {
				for (Content c : names) {
					offset += c.getHeight() + 10;
				}
			}
			names.add(new Content((int)getX() + (getWidth() / 2 - ResourceManager.getInstance().getFont("FONT_MENU").getWidth(text) / 2), 
					              (int)offset, text, ResourceManager.getInstance().getFont("FONT_MENU"), gc));
		}

		
		
		/**
		 * @return returns the width of the credit
		 */
		@Override
		public int getWidth() {
			return 100;
		}

		
		
		/**
		 * @return returns the height of the credit
		 */
		@Override
		public int getHeight() {			
			int height = 100;
			
			if (caption != null) {
				height += caption.getHeight();
			}
			
			if (names != null) {
				if (!names.isEmpty()) {
					for (Content c : names) {
						height += c.getHeight();
					}
				}
			}
			return height;
		}

		
		
		@Override
		public void draw(Graphics g) {
			caption.draw(g);
			for (Content c : names) {
				c.draw(g);
			}
		}

		
		
		@Override
		public void move(int direction, int delta) {
			caption.move(direction, delta);
			for (Content c : names) {
				c.move(direction, delta);
			}
		}

		
		
		@Override
		public void update(int delta) {
			super.update(delta);
			move(TOP, delta);
			caption.update(delta);
			for (Content c : names) {
				c.update(delta);
			}
			
			y = caption.getY();
		}
	}

	
	
	/**
	 * Constructor of CreditsShow
	 * 
	 * @param x x-coordinate on the screen
	 * @param y y-coordinate on the screen
	 * @param width complete width of the element
	 * @param height complete height of the element
	 * @param gc GameContainer
	 */
	public CreditsShow(int x, int y, int width, int height, GameContainer gc) {
		super(x, y, gc);
		this.width = width;
		this.height = height;
		credits =  new ArrayList<Credit>();
	}
	
	
	
	/**
	 * Loads new credit content from a valid XML file (see file description for more information)
	 * 
	 * @param path XML file
	 * @throws FileNotFoundException
	 * @throws SlickException
	 */
	public void load(String path) throws FileNotFoundException, SlickException {
		// Clear current setting
		credits.clear();
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        InputStream is;
		is = new FileInputStream(path);
		
        try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new SlickException("Could not load credits", e);
		}
		Document doc = null;
        try {
			doc = docBuilder.parse (is);
		} catch (SAXException e) {
			throw new SlickException("Could not load credits", e);
		} catch (IOException e) {
			throw new SlickException("Could not load credits", e);
		}
 
		// normalize text representation
        doc.getDocumentElement ().normalize(); 
        NodeList listResources = doc.getElementsByTagName("credit");        
        int totalResources = listResources.getLength();

        // Load the content
        for(int resourceIdx = 0; resourceIdx < totalResources; resourceIdx++){
        	 
        	Node resourceNode = listResources.item(resourceIdx);
 
        	if(resourceNode.getNodeType() == Node.ELEMENT_NODE){        		
        		
        		Element resourceElement = (Element)resourceNode; 
        		String caption = ResourceManager.getInstance().getText(resourceElement.getAttribute("caption"));
        		NodeList items = resourceNode.getChildNodes();
        		
        		// Set the position of each credit element
        		Credit lastCredit = getLastElement();
        		
        		float yOffset = getY();
        		
        		if (lastCredit != null) {
        			yOffset = lastCredit.getY() + lastCredit.getHeight();
        		}
        		Credit tmpCredit = new Credit((int)getX(), (int)yOffset, caption, gc);
        		for (int i = 0; i != items.getLength(); ++i) {
        			Node item = items.item(i);
        			
        			if(item.getNodeType() == Node.ELEMENT_NODE){
                		Element elem = (Element)item;
                		tmpCredit.addContent(elem.getTextContent());
        			}
        		}
        		
        		credits.add(tmpCredit);        		
        	}
        }
	}

	
	
	/**
	 * @return returns the width of the element
	 */
	@Override
	public int getWidth() {
		return width;
	}

	
	
	/**
	 * @return returns the height of the element
	 */
	@Override
	public int getHeight() {
		return height;
	}

	
	
	@Override
	public void draw(Graphics g) {
		for (Credit c : credits) {
			c.draw(g);
		}
	}
	
	
	
	/**
	 * @return last element of the credit list
	 */
	private Credit getLastElement() {
		if (credits.isEmpty()) {
			return null;
		} else {
			return credits.get(credits.size() - 1);
		}
	}
	
	
	
	/**
	 * @param index current credit index
	 * @return previous element of the given credit
	 */
	private Credit getPreviousElement(int index) {
		if (credits.isEmpty()) {
			return null;
		} else {
			if ((index - 1) < 0) {
				return getLastElement();
			} else {
				return credits.get(index - 1);
			}
		}
	}

	
	
	/**
	 * Animate the show
	 */
	@Override
	public void update(int delta) {
		super.update(delta);
		
		for (int i = 0; i < credits.size(); ++i) {
			Credit last = null;
		
			last = getPreviousElement(i);
			credits.get(i).update(delta);
			if (credits.get(i).getY() + credits.get(i).getHeight() < 0) {
				
				if (last.getY() + last.getHeight() > gc.getHeight()) {
					credits.get(i).setY(last.getY() + last.getHeight());
					
				} else {
					credits.get(i).setY(gc.getHeight());
				}
			}
		}
	}	
}
