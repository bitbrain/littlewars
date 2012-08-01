/**
; * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mail to info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Element to select a map
 * 
 * @version 	0.1.5
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.gui;

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
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Ellipse;
import org.newdawn.slick.geom.Rectangle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.myreality.dev.littlewars.components.helpers.PopupHelper;
import de.myreality.dev.littlewars.components.resources.ResourceManager;

public class MapSelector extends GUIObject {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	// Selected map
	private MapConfig selected, hover;	
	

	// All map configurations
	private List<WorldConfig> worlds;
	
	private String sectorName;

	
	/**
	 * Constructor of MapSelector
	 * 
	 * @param x x-coordinate on the screen
	 * @param y y-coordinate on the screen
	 * @param gc GameContainer
	 */
	public MapSelector(int x, int y, GameContainer gc) {
		super(x, y, gc);
		selected = null;
		worlds = new ArrayList<WorldConfig>();
	}
	
	
	
	
	/**
	 * Load the XML data into the game
	 * 
	 * @throws FileNotFoundException
	 * @throws SlickException
	 */
	public void load(String path) throws FileNotFoundException, SlickException {
		worlds.clear();
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        InputStream is;
		is = new FileInputStream(path);
		
        try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new SlickException("Could not load map settings", e);
		}
		Document doc = null;
        try {
			doc = docBuilder.parse (is);
		} catch (SAXException e) {
			throw new SlickException("Could not load map settings", e);
		} catch (IOException e) {
			throw new SlickException("Could not load map settings", e);
		}
 
		// normalize text representation
        doc.getDocumentElement ().normalize(); 
        NodeList listResources = doc.getElementsByTagName("sector");        
        int totalResources = listResources.getLength();

        // Load the content
        for(int resourceIdx = 0; resourceIdx < totalResources; resourceIdx++){
        	 
        	Node resourceNode = listResources.item(resourceIdx);
 
        	if(resourceNode.getNodeType() == Node.ELEMENT_NODE){          		
        		// Fetch each single resource and create the map config
        		Element resourceElement = (Element)resourceNode;   
        		sectorName = resourceElement.getAttribute("name");
        		
        		// Load world configs
        		NodeList worldNodes = resourceElement.getChildNodes();
        		for (int nodeID = 0; nodeID < worldNodes.getLength(); ++nodeID) {
        			Node worldNode = worldNodes.item(nodeID);
        			 
                	if(worldNode.getNodeType() == Node.ELEMENT_NODE){      
                		Element worldElement = (Element)worldNode; 
                		WorldConfig singleWorld = new WorldConfig(worldElement, worldNode, gc);
                		singleWorld.attachTo(this);
                		worlds.add(singleWorld);
                	}
        		}
        	}
        }
	}
	
	/**
	 * Reset the selector
	 */
	public void reset() {
		selected = null;
	}

	@Override
	public void draw(Graphics g) {
		if (isVisible()) {
			for (WorldConfig world : worlds) {
				world.draw(g);
			}
		}
	}
	
	
	
	@Override
	public void update(int delta) {
		// TODO: Fix selection bug
		for (WorldConfig world : worlds) {
			world.update(delta);
		}
	}


	public MapConfig getSelected() {		
		return selected;
	}
	
	public MapConfig getHover() {
		return hover;
	}

	public String getSectorName() {
		return sectorName;
	}
	
	
	public class WorldConfig extends Button {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private List<MapConfig> maps;
		private MapConfig clicked;
		private String name;
		
		public WorldConfig(Element xmlElement, Node resourceNode, GameContainer gc) throws SlickException {
			super(0, 0, 0, 0, "", gc);
			enableSound(false);
			maps = new ArrayList<MapConfig>();			
			setHoverColor(Color.white);
			setDefaultColor(ResourceManager.getInstance().getColor("COLOR_MAP_HOVER"));
			loadFromXML(xmlElement, resourceNode);
		}
		
		public boolean hasSelectedMap() {
			for (MapConfig config : maps) {
				if (config == selected) {
					return true;
				}
			}
			return false;
		}

		@Override
		public void draw(Graphics g) {
			super.draw(g);
			if (isVisible()) {
				for (MapConfig map: maps) {
					map.draw(g);
				}
			}
		}
		
		@Override
		public void update(int delta) {
			super.update(delta);
			clicked = null;
			boolean hoverElement = false;
			for (MapConfig config: maps) {
				config.update(delta);
				
				if (config.isHover()) {
					hover = config;
					hoverElement = true;					
				}
				
				if (config.onClick()) {
					selected = config;					
					clicked = config;					
				} 	
			}
			
			if (hover != null && !hoverElement) {
				hover = null;
			}
			
			if (hasSelectedMap()) {
				setClrFocus(ResourceManager.getInstance().getColor("COLOR_MAIN"));
			} else {
				setClrFocus(null);
			}
		}
		
		
		public boolean onMapClick() {
			return clicked != null;
		}
		
		public boolean isMapHover() {
			return hover != null;
		}
		
		public MapConfig getHover() {
			return hover;
		}
		
		private void loadFromXML(Element xmlElement, Node resourceNode) {
			// Load general settings
			name = xmlElement.getAttribute("name");
			x = Integer.parseInt(xmlElement.getAttribute("x"));
			y = Integer.parseInt(xmlElement.getAttribute("y"));
			
			// Initialize the elements
			NodeList items = resourceNode.getChildNodes();
			for (int i = 0; i != items.getLength(); ++i) {
			    Node item = items.item(i);
			    			
			    if (item.getNodeType() == Node.ELEMENT_NODE){
			           Element elem = (Element)item;
			            		
			           if (elem.getTagName().equals("image")) {
			        	   background = ResourceManager.getInstance().getImage(elem.getTextContent());
			        	   if (background != null) {
			        		   width = background.getWidth();
			        		   height = background.getHeight();
			        	   }
			           } else if (elem.getTagName().equals("maps")) {
			        	   NodeList mapList = elem.getChildNodes();
			        	   // Loading maps
			        	   for (int mapID = 0; mapID < mapList.getLength(); ++mapID) {
			        		   Node mapNode = mapList.item(mapID);		        		   
			        		   
			        		   if (mapNode.getNodeType() == Node.ELEMENT_NODE){
			        			   Element mapElement = (Element)mapNode;
			        			   MapConfig mapConfig;
								   try {
									   mapConfig = new MapConfig(this, mapElement, mapNode, gc);
									   mapConfig.attachTo(this);
						        	   maps.add(mapConfig);
								   } catch (SlickException e) {
									   e.printStackTrace();
								   }
			        			  
			        		   }
			        	   }
			           }
			    }
			}			
			
			area = new Ellipse(getX() + getWidth() / 2, getY() + getHeight() / 2, getWidth() / 2, getHeight() / 2);
		}

		public String getName() {
			return name;
		}
	}


	public class MapConfig extends Button {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		// Source of the tilemap
		private String mapSource;
		
		// Name of the map
		private String mapName;
		
		// Number of Players
		private String playerCount;
		
		// Number of Slots
		private String slotCount;
		
		// Music of the map
		private String mapMusic;
		
		// Sound of the map
		private String mapSound;
		
		private WorldConfig world;		
		
		// Check icon
		@SuppressWarnings("unused")
		private Image iconCheck;	

		public MapConfig(WorldConfig parent, Element xmlElement, Node resourceNode, GameContainer gc) throws SlickException {
			super(0, 0, 0, 0, "", gc);			
			// Load the data
			background = ResourceManager.getInstance().getImage("GUI_MAP_SELECTED");
			setPadding(2);
			iconCheck = ResourceManager.getInstance().getImage("GUI_ICON_CHECK");
			width = 20;
			height = 20;
			world = parent;
			attachTo(parent);
			loadFromXML(xmlElement, resourceNode);			
			font = null;
			setHoverColor(ResourceManager.getInstance().getColor("COLOR_MAIN"));
			setDefaultColor(Color.white);
			PopupHelper.getInstance().addPopup(this, mapName, gc);
		}
		
		private void loadFromXML(Element xmlElement, Node resourceNode) {
			// Load general settings
			mapSource = xmlElement.getAttribute("src");
			playerCount = xmlElement.getAttribute("players");
			slotCount = xmlElement.getAttribute("slots");
			mapName = ResourceManager.getInstance().getText(xmlElement.getAttribute("name"));			
			x = Integer.parseInt(xmlElement.getAttribute("x"));
			y = Integer.parseInt(xmlElement.getAttribute("y"));	
			// Initialize the elements
			NodeList items = resourceNode.getChildNodes();
			for (int i = 0; i != items.getLength(); ++i) {
    			Node item = items.item(i);
    			
    			if(item.getNodeType() == Node.ELEMENT_NODE){
            		Element elem = (Element)item;
            		
            		if (elem.getTagName().equals("music")) {
            			mapMusic = elem.getTextContent();
            			if (mapMusic == null) {
            				System.out.println("Error: Can't find music resource with ID '" + elem.getTextContent() + "'");
            			}
            		} else if(elem.getTagName().equals("sound")) {
            			mapSound = elem.getTextContent(); 
            		} 
    			}
    		}
			
			area = new Rectangle(getX(), getY(), getWidth(), getHeight());
		}
		
		
		


		@Override
		public void draw(Graphics g) {	
			super.draw(g);
		}

		@Override
		public void update(int delta) {
			super.update(delta);
			
			if (isSelected()) {
				background = ResourceManager.getInstance().getImage("GUI_ICON_CHECK");
			} else {
				background = ResourceManager.getInstance().getImage("GUI_MAP_SELECTED");
			} 
		}

		public String getMapSource() {
			return mapSource;
		}

		public String getMapName() {
			return mapName;
		}
		
		public String getPlayerCount() {
			return playerCount;
		}
		
		public String getSlotCount() {
			return slotCount;
		}
		
		public boolean isSelected() {
			return selected == this;
		}

		public String getMapMusic() {
			return mapMusic;
		}

		public String getMapSound() {
			return mapSound;
		}
		
		
		public WorldConfig getWorldConfig() {
			return world;
		}
	}

}
