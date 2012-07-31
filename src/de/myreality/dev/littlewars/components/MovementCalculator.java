package de.myreality.dev.littlewars.components;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.util.pathfinding.Path;

import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.game.IngameState;
import de.myreality.dev.littlewars.objects.ArmyUnit;
import de.myreality.dev.littlewars.objects.Movable;

public class MovementCalculator {
	
	// Current path
	private Path path;
	
	// Current unit to move
	private ArmyUnit target;
	
	// Map full of directions
	Map<Integer, Integer> movements;
	
	// Current Game State
	private IngameState game;
	
	// Selected enemy in order to attack
	private ArmyUnit enemy;
	
	// Length
	private int length;

	public MovementCalculator(ArmyUnit target, IngameState game) {
		movements = new TreeMap<Integer, Integer>();
		this.target = target;
		this.game = game;
		this.enemy = null;
	}
	
	
	public ArmyUnit getEnemy() {
		return enemy;
	}
	
	public Integer getCurrentPosition() {
		if (path != null) {
			return length - movements.size();
		} else {
			return -1;
		}
	}
	
	public void setMovement(Path path) {
		reset();
		if (path != null) {
			this.path = path;
			int lastX = -1, lastY = -1;

			// Calculate path
			for (int i = 0; i < path.getLength(); ++i) {
				if (i > target.getRemainingSpeed()) {
					break;
				}
				
				if (game.getWorld().isEnemyUnit(game.getCurrentPlayer(), path.getX(i), path.getY(i))) {
					enemy = game.getWorld().getArmyUnitAt(path.getX(i), path.getY(i));
					break;
				}
				
				if (lastX > -1 && lastY > -1) {					
					movements.put(movements.size(), movementToDirection(lastX, lastY, path.getX(i), path.getY(i)));
				}
				
				lastX = path.getX(i);
				lastY = path.getY(i);
			}
		}
	}
	
	
	public boolean isDone() {
		return movements.isEmpty();
	}

	
	
	public void reset() {
		movements.clear();
		enemy = null;
		length = 0;
	}
	
	
	
	/**
	 * Function that convert two points to a direction
	 * 
	 * @param sX source X
	 * @param sY source Y
	 * @param tX target X
	 * @param tY target Y
	 * @return Direction
	 */
	public static int movementToDirection(int sX, int sY, int tX, int tY) {
		
		// Left
		if (sX > tX) {
			return Movable.LEFT;
		} 
		// Right
		if (sX < tX) {
			return Movable.RIGHT;
		} 
		// Top
		if (sY > tY) {
			return Movable.TOP;
		}
		// Bottom
		if (sY < tY) {
			return Movable.BOTTOM;
		}
		
		return -1;
	}

	
	public int getLength() {
		return movements.size();
	}
		
	/**
	 * Draws a fancy path
	 * 
	 * @param delta
	 */
	public static void drawUnitPath(Graphics g, ArmyUnit target, Path path, IngameState game) {
		if (target.getSpeed() > 0 && target.isTargetArrived() && target.canMove() && path.getLength() > 0) {
			Integer speed = target.getRemainingSpeed();
			int lastX = path.getX(0), lastY = path.getY(0), nextX = lastX, nextY = lastY; 
			for (Integer i = 0; i < path.getLength(); ++i) {				
				
				try {
				nextX = path.getX(i + 1);
				nextY = path.getY(i + 1);
				} catch (IndexOutOfBoundsException e) {
					nextX = path.getX(i);
					nextY = path.getY(i);
				}
				if (i <= speed && i > 0) {					
					boolean isLast = i.equals(speed) || ((path.getLength() <= speed && i.equals(path.getLength() - 1)) ? true : false);	
					
					// Check, if enemy unit is focused
					if (game.getWorld().isEnemyUnit(game.getCurrentPlayer(), nextX, nextY)) {
						isLast = true;
					}
					
					int lastDir = movementToDirection(lastX, lastY, path.getX(i), path.getY(i));
					int nextDir = movementToDirection(path.getX(i), path.getY(i),  nextX,  nextY);
					Image pathImage = getPathImage(lastDir, nextDir, isLast);
					
					if (isLast) {				
						//g.fillRect(32 * path.getX(i) - camera.getX(), 32 * path.getY(i) - camera.getY(), 32, 32);
					}
					
					if (pathImage != null) {
						pathImage.setAlpha(0.7f);						
						pathImage.draw(32 * path.getX(i) - game.getWorld().getCamera().getX(), 32 * path.getY(i) - game.getWorld().getCamera().getY(), 32, 32);
					}	
					
					if (isLast) {
						break;
					}
				}					
				lastX = path.getX(i);
				lastY = path.getY(i);
			}
		}
	}
	
	
	
	/** 
	 * Returns an valid path image
	 * @param lastDir last direction
	 * @param nextDir next direction
	 * @param end true, if it's the last path index
	 * 
	 * @return Image
	 */
	public static Image getPathImage(int lastDir, int nextDir, boolean end) {
		
		Image image = null;
		
		// Image has to be an arrow
		if (end) {
			image = ResourceManager.getInstance().getImage("GUI_PATH_ARROW");
			switch (lastDir) {
				case Movable.LEFT:
					image.setRotation(180f);
					break;
				case Movable.RIGHT:
					image.setRotation(0.0f);
					break;
				case Movable.BOTTOM:
					image.setRotation(90.0f);
					break;
				case Movable.TOP:
					image.setRotation(270.0f);	
					break;
			}
		} else if (lastDir != nextDir) {
			// Curve
			image = ResourceManager.getInstance().getImage("GUI_PATH_CURVE");
			if (lastDir == Movable.RIGHT && nextDir == Movable.TOP || lastDir == Movable.BOTTOM && nextDir == Movable.LEFT) {
				image.setRotation(0f);
			} else if (lastDir == Movable.BOTTOM && nextDir == Movable.RIGHT || lastDir == Movable.LEFT && nextDir == Movable.TOP) {
				image.setRotation(90f);
			} else if (lastDir == Movable.TOP && nextDir == Movable.RIGHT || lastDir == Movable.LEFT && nextDir == Movable.BOTTOM) {
				image.setRotation(180f);
			} else if (lastDir == Movable.RIGHT && nextDir == Movable.BOTTOM || lastDir == Movable.TOP && nextDir == Movable.LEFT) {
				image.setRotation(270f);
			}
		} else {
			image = ResourceManager.getInstance().getImage("GUI_PATH_STRAIGHT");
			// Straight
			if (lastDir == Movable.TOP || lastDir == Movable.BOTTOM) {
				image.setRotation(90f);
			} else {
				image.setRotation(0f);
			}
		}
		
		
		return image;
	}
	
	
	
	public void update(int delta) {
		correctUnitPosition();
		if (!movements.isEmpty() && path != null && target != null) {
			if (target.isTargetArrived()) { 
				for (Entry<Integer, Integer> entry : movements.entrySet()) {
					target.move(entry.getValue(), delta);
					movements.remove(entry.getKey());
					break;
				}				
			}			
		} else if (movements.isEmpty() && path != null) {
			path = null;
			target.setRemainingSpeed(0);
		} else if (path == null) {
			movements.clear();
			length = 0;
		}
	}
	
	
	/**
	 * Correct the positions at the corners of the path
	 */
	private void correctUnitPosition() {
					
		switch (target.getCurrentDirection()) {
			case Movable.LEFT:
				if (target.getX() < target.getTargetX()) {
					target.setX(target.getTargetX());	
					target.setToTarget();
				}
				break;
			case Movable.RIGHT:
				if (target.getX() > target.getTargetX()) {
					target.setX(target.getTargetX());
					target.setToTarget();
				}
				break;
			case Movable.BOTTOM:
				if (target.getY() > target.getTargetY()) {
					target.setY(target.getTargetY());
					target.setToTarget();
				}
				break;
			case Movable.TOP:
				if (target.getY() < target.getTargetY()) {
					target.setY(target.getTargetY());
					target.setToTarget();
				}
				break;
		}			
		
	}
	
}
