/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mail to info@my-reality.de
 * or visit the game page: http://dev.my-reality.de/littlewars
 * 
 * Contains game information like version and author
 * 
 * @version 	0.1.3
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.objects;

import java.util.List;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.util.pathfinding.Path;

import de.myreality.dev.littlewars.components.FadeInfoSetting;
import de.myreality.dev.littlewars.components.MovementCalculator;
import de.myreality.dev.littlewars.components.UnitGenerator;
import de.myreality.dev.littlewars.components.helpers.UnitInfoHelper;
import de.myreality.dev.littlewars.components.resources.ResourceManager;
import de.myreality.dev.littlewars.components.resources.SpriteAnimationData;
import de.myreality.dev.littlewars.components.resources.UnitResource;
import de.myreality.dev.littlewars.game.IngameState;
import de.myreality.dev.littlewars.ki.Player;

public abstract class ArmyUnit extends TileObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Percent of life loosing / Experience changing
	protected static float changeFactor = 0.05f;
	
	// Basic Damage
	protected static final int BASIC_DAMAGE = 100;
	
	// Current player that the object belongs to
	protected Player player;
	
	// Game
	protected IngameState game;
	
	protected Sound expSound;
	
	// Life of the unit
	protected int currentLife;
	
	// Current speed
	protected int remainingSpeed;
	
	// Experience of the unit
	protected int currentExperience;
	
	// Life and experience sequence
	protected int lifeSeq, expSeq;
	
	// Rank of the unit
	protected int rank;	
	
	// Element name
	protected String name;
	
	protected int id;	
	
	// Sounds of the unit
	protected List<Sound> dyingSounds;	
	protected List<Sound> clickSounds;
	protected List<Sound> attackSounds;
	
	protected boolean dead, deadFirst;	
	
	protected MovementCalculator movementCalculator;	
	
	// Additional values to strengthen the unit
	private int lifeAdd, strengthAdd, defenseAdd, speedAdd;	

	/**
	 * Constructor of ArmyUnit
	 * 
	 * @param x x-coordinate on the screen
	 * @param y y-coordinate on the screen
	 * @param name string name of the unit
	 * @param type type of the unit
	 * @param gc GameContainer
	 * @param cam
	 * @param map
	 * @throws SlickException
	 */
	protected ArmyUnit(int id, String resourceID, int x, int y, GameContainer gc,
			Camera cam, IngameState game) throws SlickException {
		super(x, y, gc, cam, game.getWorld());
		this.id = id;
		this.game = game;
		rank = 1;
		currentLife = getLife();
		currentExperience = 0;
		updatePlayerColor();	
		loadFromResource(resourceID);
		dead = false;
		deadFirst = false;
		lifeSeq = 0;
		expSeq = 0;
		remainingSpeed = getSpeed();
		expSound = ResourceManager.getInstance().getSound("SOUND_EXP");
		//info = new UnitTileInfo(this, cam, 0, 0, gc);
		//info.attachTo(this);
		movementCalculator = new MovementCalculator(this, game);
		if (animations[SpriteAnimationData.DIE] != null) {
			for (int i = 0; i < animations[SpriteAnimationData.DIE].length; ++i) {
				animations[SpriteAnimationData.DIE][i].setLooping(false);
			}
		}
	}
	
	/**
	 * Change the player that it belongs to
	 * 
	 * @param player new player
	 */
	public void setPlayer(Player player) {
		this.player = player;	
		updatePlayerColor();
	}


	public Player getPlayer() {
		return player;
	}
	
	private void updatePlayerColor() {
		if (player != null) {
			color = player.getColor();
		}
		
	}
	
	
	public boolean onDead() {
		return !dead && isDead();
	}	
	
	
	@Override
	public void move(int direction, int delta) {
		if (!isDead() && canMove()) {
			if (isTargetArrived()) {
				remainingSpeed--;
			}
			super.move(direction, delta);
		}
	}

	protected void loadFromResource(String resourceID) {
		
		UnitResource resource = ResourceManager.getInstance().getUnitResource(resourceID);
		
		// Load the animations
		animations = resource.getAnimationData().generateAnimations();
		
		// Load the avatar
		imgAvatar = ResourceManager.getInstance().getImage(resource.getAvatar());
		imgAvatarID = resource.getAvatar();
	
		// Set the name
		name = resource.getName();
		
		// Load the sounds
		dyingSounds = resource.generateSoundList("onDead");	
		clickSounds = resource.generateSoundList("onClick");
		attackSounds = resource.generateSoundList("onAttack");
	}
	
	
	public int getCurrentLife() {
		return currentLife;
	}
	
	public void playRandomSound(String key) {
		if (key == "onDead" && !dyingSounds.isEmpty()) {
			int index = (int) (Math.random() * dyingSounds.size());
			dyingSounds.get(index).play();			
		} else if (key == "onClick" && !clickSounds.isEmpty()) {
			int index = (int) (Math.random() * clickSounds.size());
			clickSounds.get(index).play();	
		} else if (key == "onAttack" && !attackSounds.isEmpty()) {
			int index = (int) (Math.random() * attackSounds.size());
			attackSounds.get(index).play();	
		}
	}
	
	public boolean isSoundPlaying(String key) {
		if (key == "onDead" && !dyingSounds.isEmpty()) {
			for (Sound s : dyingSounds) {
				if (s.playing()) {
					return true;
				}
			}
		} else if (key == "onClick" && !clickSounds.isEmpty()) {
			for (Sound s : clickSounds) {
				if (s.playing()) {
					return true;
				}
			}
		} 
		
		return false;
	}
	
	
	public boolean stopSound(String key) {
		if (key == "onDead" && !dyingSounds.isEmpty()) {
			for (Sound s : dyingSounds) {
				if (s.playing()) {
					s.stop();
					break;
				}
			}
		} else if (key == "onClick" && !clickSounds.isEmpty()) {
			for (Sound s : clickSounds) {
				if (s.playing()) {
					s.stop();
					break;
				}
			}
		} 
		
		return false;
	}

	public void setCurrentLife(int currentLife) {
		this.currentLife = currentLife;
	}

	public int getCurrentExperience() {
		return currentExperience;
	}

	public void setCurrentExperience(int currentExperience) {
		this.currentExperience = currentExperience;
	}
	
	public void setRemainingSpeed(int speed) {
		remainingSpeed = speed;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	

	public void setLifeAdd(int lifeAdd) {
		this.lifeAdd = lifeAdd;
	}

	public void setStrengthAdd(int strengthAdd) {
		this.strengthAdd = strengthAdd;
	}

	public void setSpeedAdd(int speedAdd) {
		this.speedAdd = speedAdd;
	}

	// State Functions
	protected abstract int getRankStrength(int rank);
	protected abstract int getRankLife(int rank);
	protected abstract int getRankDefense(int rank);
	protected abstract int getRankSpeed(int rank);
	protected abstract int getRankExperience(int rank);
	
	
	public int getStrength() {
		return getRankStrength(rank) + strengthAdd;
	}
	
	
	public int getTotalExperience() {
		return getRankExperience(rank);
	}
	
	
	public int getLife() {
		return getRankLife(rank) + lifeAdd;
	}
	
	
	public int getRemainingSpeed() {
		return remainingSpeed;
	}
	
	protected abstract int getRankExperienceValue(int rank);
	
	protected int getExperienceValue() {
		return getRankExperienceValue(rank);
	}

	@Override
	public void draw(Graphics g) {
		if (isDead()) {
			if (animations[SpriteAnimationData.DIE][currentDirection] != null) {
				animations[SpriteAnimationData.DIE][currentDirection].draw(-camera.getX() + getX(), -camera.getY() + getY(), getWidth(), getHeight(), color);
			}
		} else {
			super.draw(g);
		}
	}
	
	public boolean isDying() {
		Animation animDead = animations[SpriteAnimationData.DIE][currentDirection];
		return isDead() && !animDead.isStopped();
	}

	@Override
	public void update(int delta) {
		super.update(delta);

		// Update animation
		if (!isTargetArrived()) {
			if (animations[SpriteAnimationData.MOVE][currentDirection] != null) {
				animations[SpriteAnimationData.MOVE][currentDirection].update(delta);
			}
		} else if (isDead()) {			
			Animation animDead = animations[SpriteAnimationData.DIE][currentDirection];
			if (animDead.isStopped()) {
				player.removeArmyUnit(this);
			} else if (animDead != null) {
				animDead.update(delta);
			}			
		} 
	
		if (lifeSeq > 0 && !isDead()) {
			if (lifeSeq >= Math.ceil((getLife() * (changeFactor / 100) * delta))) {
				currentLife -= Math.ceil((getLife() * (changeFactor / 100) * delta));
			} else {
				currentLife -= lifeSeq;
			}
			lifeSeq -= Math.ceil((getLife() * (changeFactor / 100) * delta));
			if (lifeSeq < 0) {
				lifeSeq = 0;
			}
		}
		
		if (expSeq > 0 && !isDead()) {
			if (expSeq >=  Math.ceil((getTotalExperience() * (changeFactor / 100) * delta))) {
				currentExperience += Math.ceil((getTotalExperience() * (changeFactor / 100) * delta));
			} else {
				currentExperience += expSeq;
			}
			expSound.stop();
			expSound.play(1f * (getExperiencePercent() / 100), 0.05f);
			expSeq -= Math.ceil((getTotalExperience() * (changeFactor / 100) * delta));	
			if (expSeq < 0) {
				expSeq = 0;
			}
		} 
		
		if (currentLife < 0) {
			currentLife = 0;
		}
		
		if (currentExperience >= getTotalExperience()) {
			expSeq += currentExperience - getTotalExperience();
			currentExperience = 0;
			rankUp();
		}
		
		if (isDead()) {
			expSeq = 0;
			lifeSeq = 0;
		} 
		
		if (deadFirst && isDead()) {
			dead = true;
		}
		
		if (isDead() && !deadFirst) {
			deadFirst = true;
		}
		
		if (!isDead()) {
			deadFirst = false;
			dead = false;
		}
		
		if (onDead()) {
			playRandomSound("onDead");
			FadeInfoSetting setting = new FadeInfoSetting();
			setting.setText("Kill!");
			setting.setColor(Color.red);
			UnitInfoHelper.getInstance().addInfo(this, setting, gc, game);
		}
		
		if (player.isClientPlayer() && !isDead() && (instantClick || onClick())) {
			stopSound("onClick");
			playRandomSound("onClick");
		}
		
		if (instantClick) {
			instantClick = false;
		}
		
		if (!canMove() && isTargetArrived() && !getID().equals(UnitGenerator.UNIT_CENTER) && game.getCurrentPlayer().isClientPlayer()) {
			color = Color.gray;
		} else {
			color = player.getColor();
		}
		
		movementCalculator.update(delta);
		
		if (movementCalculator.isDone() && isTargetArrived()) {
			ArmyUnit enemy = movementCalculator.getEnemy();
			if (enemy != null) {
				attack(enemy);
				if (!enemy.isDead()) {
					enemy.attack(this);
				}				
			}			
		}
	}

	public int getDefense() {
		return getRankDefense(rank) + defenseAdd;
	}
	
	
	
	public int getSpeed() {
		return getRankSpeed(rank) + speedAdd;
	}
	
	
	
	public int getRankExperience() {
		return getRankExperience(rank);
	}

	public int getLifeAdd() {
		return lifeAdd;
	}

	public void setLifeAdd(Integer lifeAdd) {
		this.lifeAdd = lifeAdd;
	}

	public int getStrengthAdd() {
		int add = 0;
		if (player != null) {
			for (ArmyUnit center : player.getCommandoCenters()) {
				if (center != null && !equals(center)) {
					add += center.getStrength();
				}
			}
		}
		
		return strengthAdd + add;
	}

	public void setStrengthAdd(Integer strengthAdd) {
		this.strengthAdd = strengthAdd;
	}

	public int getDefenseAdd() {
		int add = 0;
		if (player != null) {
			for (ArmyUnit center : player.getCommandoCenters()) {
				if (center != null && !equals(center)) {
					add += center.getDefense();
				}
			}
		}
		
		return defenseAdd + add;
	}

	public void setDefenseAdd(Integer defenseAdd) {
		this.defenseAdd = defenseAdd;
	}

	public int getSpeedAdd() {
		return speedAdd;
	}

	public void setSpeedAdd(Integer speedAdd) {
		this.speedAdd = speedAdd;
	}
	
	public void rankUp() {
		++rank;
		currentLife = getLife();
		FadeInfoSetting setting = new FadeInfoSetting();
		setting.setText("Level up!");
		setting.setColor(ResourceManager.getInstance().getColor("COLOR_MAIN"));
		UnitInfoHelper.getInstance().addInfo(this, setting, gc, game);
	}
	
	
	public void addExperience(int exp) {
		if (!isDead() && isTargetArrived()) {
			expSeq += exp;
			FadeInfoSetting setting = new FadeInfoSetting();
			setting.setText("+" + String.valueOf(exp) + " EXP");
			setting.setColor(ResourceManager.getInstance().getColor("UNIT_EXP"));
			UnitInfoHelper.getInstance().addInfo(this, setting, gc, game);
		}		
	}
	
	
	public float getLifePercent() {		
		return getCurrentLife() * 100 / getLife();
	}
	
	public float getExperiencePercent() {
		return getCurrentExperience() * 100 / getTotalExperience();
	}
	
	public boolean addDamage(int value) {
		
		if (isDead() || !isTargetArrived()) {
			return false;
		}
		
		lifeSeq += value;
		
		FadeInfoSetting setting = new FadeInfoSetting();
		setting.setText(String.valueOf(value) + " " + ResourceManager.getInstance().getText("TXT_GAME_DAMAGE"));
		setting.setColor(Color.red);
		UnitInfoHelper.getInstance().addInfo(this, setting, gc, game);
		
		return true;
	}
	
	public boolean isDead() {
		return currentLife <= 0;
	}
	
	public void attack(ArmyUnit target) {
		if (!equals(target)) {
			// Play attack sound
			playRandomSound("onAttack");
			
			// Damage calculation 
			int damage = (int) ((int) (11 * (Math.random() * 1124 + 1) - target.getDefense()) * getStrength() * (float)(getRank() + getStrength()) / 256.0f);
			target.addDamage(damage);
			if (target.isDead()) {
				addExperience(target.getExperienceValue());
			} else {
				addExperience((damage / 10) * (target.getRank() / getRank()));
			}
			movementCalculator.reset();
		}
	}
	
	
	public boolean isReady() {
		return lifeSeq == 0 && expSeq == 0;
	}
	
	
	public boolean canMove() {
		return remainingSpeed > 0;
	}
	
	public void activate() {
		remainingSpeed = getSpeed();
	}
	
	public Integer getID() {
		return id;
	}
	
	public void moveAlongPath(Path path) {
		if (!player.isUnitMoving()) {
			movementCalculator.setMovement(path);
		}
	}
	
	public IngameState getGame() {
		return game;
	}
	
	public float getTileVelocity(int delta) {
		// 1. Get current direction length		
		//float speed = delta * velocity;

		//int directionLength = movementCalculator.getDirectionLength(movementCalculator.getCurrentPosition());
		
		// 2. Calculate specific velocity
		float tileVelocity = 1;//(float)(speed) / (float) directionLength; // TODO: Find algorithm

		return tileVelocity;
	}
}
