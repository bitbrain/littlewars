/**
 * This file was written by Miguel Gonzalez and is part of the
 * game "LittleWars". For more information mailto info@my-reality.de
 * or visit the game page: http://littlewars.my-reality.de
 * 
 * A list of fading images, each with specific time elapsing
 * 
 * @version 	0.0.1
 * @author 		Miguel Gonzalez		
 */

package de.myreality.dev.littlewars.components.fading;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

import de.myreality.dev.littlewars.components.Timer;
import de.myreality.dev.littlewars.objects.GameObject;

public class FadeShow extends GameObject {	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// List of all images to fade
	private List<FadeImage> sequences = null;
	
	// Position in the fade list
	private int position;
	
	// Current fade image
	private FadeImage currentImage = null;
	
	// State of playing
	private boolean playing;
	
	// Current fade color
	private Color color = null;
	
	// State of repeating the animation
	private boolean loop;
	
	// Time counter
	private Timer counter = null;
	
	/**
	 * Fading image with a general time of appearence. Includes
	 * a replay functionality
	 * 
	 * 
	 * @version 0.1
	 * @author Miguel Gonzalez
	 */
	class FadeImage extends GameObject implements Fadable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		// State of playing or not
		private boolean playing;
		
		// Runtime in milliseconds
		private int runtime;
		
		// Image to fade
		private Image image = null;
		
		// Width and height of the image
		private int width, height;		
		
		// Fade phases (fade in, visible, fade out)
		private int phase;
		
		// Fade color
		private Color color = null;
		
		// fade factor
		private int fading;
		
		// Timer in order to count the time
		private Timer counter;		
		
		
		/**
		 * Constructor of FadeImage
		 * 
		 * @param counter
		 * @param gc
		 * @param image
		 * @param width
		 * @param height
		 * @param color
		 * @param runtime
		 */
		public FadeImage(Timer counter, GameContainer gc, Image image, int width, int height, Color color, int runtime) {
			this(counter, gc, image, 0, 0, width, height, color, runtime);	
		}
		
		
		/**
		 * Constructor of FadeImage
		 * 
		 * TODO Create javadoc here 
		 * @param counter
		 * @param gc
		 * @param image
		 * @param x
		 * @param y
		 * @param width
		 * @param height
		 * @param color
		 * @param runtime
		 */
		public FadeImage(Timer counter, GameContainer gc, Image image, int x, int y, 
				         int width, int height, Color color, int runtime) {
			super(x, y, gc);
			this.image = image;
			this.runtime = runtime;			
			this.width = width;
			this.height = height;
			this.playing = false;
			this.color = color;		
			this.counter = counter;		
			phase = 0;
			fading = 0;
			area = new Rectangle(getX(), getY(), getWidth(), getHeight());		
		}
		

		/**
		 * Plays the fading animation
		 */
		public void play() {
			if (!playing) {
				counter.start();
				playing = true;			
			}
		}
		
		
		/**
		 * Stops the fading animation
		 */
		public void stop() {
			counter.stop();
			counter.reset();
			playing = false;
		}
		
		
		/**
		 * Change the fade out color
		 * 
		 * @param color new color for the fading effect
		 */
		public void setColor(Color color) {
			this.color = color;			
		}

		
		
		/**
		 * Change the width of the fade image
		 * 
		 * @param width new width
		 */
		public void setWidth(int width) {
			this.width = width;
		}

		
		
		/**
		 * Change the height of the fade image
		 * 
		 * @param height new height
		 */
		public void setHeight(int height) {
			this.height = height;
		}

		@Override
		public int getWidth() {
			return width;
		}

		@Override
		public int getHeight() {
			return height;
		}

		@Override
		public void draw(Graphics g) {
			if (!done() && playing) {
				if (color != null) {
					if (!g.getColor().equals(color)) {
						g.setColor(color);
					}
					g.fill(area);
				}
				image.draw(getX(), getY(), getWidth(), getHeight(), new Color(255, 255, 255, fading));
			}
		}
		
		
		
		@Override
		public void update(int delta) {

			if (!done() && playing) {			
				if (counter.getMiliseconds() < getSingleTime()) {
					if (phase != FADE_IN) {
						phase = FADE_IN;
					}
				}
				
				if (counter.getMiliseconds() > getSingleTime() && counter.getMiliseconds() < getSingleTime() * 2) {
					if (phase != VISIBLE) {
						phase = VISIBLE;
					}
				}
				
				if (counter.getMiliseconds() > getSingleTime() * 2) {
					if (phase != FADE_OUT) {
						phase = FADE_OUT;
					}
				} 
				
				switch (phase) {
					case FADE_IN:
						fadeIn(delta);
						//System.out.println("FadeIn: " + color.r);
						break;
					case VISIBLE:
						// Do nothing
						//System.out.println("Visible: " + color.r);
						break;
					case FADE_OUT:
						fadeOut(delta);	
						//System.out.println("FadeOut: " + color.r);
						break;
					default:
						break;
				}

			} 
		}
		
		
		
		/**
		 * @return Value, if animation is playing
		 */
		public boolean isPlaying() {
			return playing;
		}
		
		
		
		/**
		 * @return Value, if animation is over
		 */
		public boolean done() {
			return counter.getMiliseconds() >= runtime;
		}

		@Override
		public void fadeIn(int delta) {
			if (fading < 255) {
				fading += getFadeFactor(delta);
				if (fading > 255) 
					fading = 255;
			} else if (fading > 255) 
				fading = 255;
		}

		@Override
		public void fadeOut(int delta) {
			if (fading > 0) {
				fading -= getFadeFactor(delta);
				if (fading < 0) 
					fading = 0;
			} else if (fading < 0)
				fading = 0;
		}
		
		
		
		/**
		 * @param delta frame delta
		 * @return Specific fade factor for a fade mode
		 */
		private double getFadeFactor(int delta) {
			return 2 * delta * (255.0 / getSingleTime());
		}
		
		
		/**
		 * @return Get the single time of one mode
		 */
		private double getSingleTime() {
			return runtime / 3;
		}		
	}	
	
	
	
	/**
	 * Constructor of FadeShow
	 * 
	 * @param width width of the fade show
	 * @param height height of the fade show
	 * @param gc GameContainer
	 */
	public FadeShow(int width, int height, GameContainer gc) {
		this(0, 0, width, height, gc);
	}
	
	
	/**
	 * Constructor of FadeShow
	 * 
	 * @param x x-coordinate on the screen
	 * @param y y-coordinate on the screen
	 * @param width width of the object
	 * @param height height of the object
	 * @param gc GameContainer
	 */
	public FadeShow(int x, int y, int width, int height, GameContainer gc) {
		super(x, y, gc);
		this.width = width;
		this.height = height;
		this.playing = false;
		sequences = new ArrayList<FadeImage>();
		position = -1;
		currentImage = null;	
		counter = new Timer();
	}
	
	
	/**
	 * Enable or disable repetition after finishing the animation
	 * 
	 * @param loop 
	 */
	public void setLoop(boolean loop) {
		this.loop = loop;
	}
	
	
	
	/**
	 * Adds a new fade image sequence
	 * 
	 * @param image target image
	 * @param runtime runtime in milliseconds
	 */
	public void addSequence(Image image, int runtime) {
		sequences.add(new FadeImage(counter, gc, image, (int)getX(), (int)getY(), getWidth(), getHeight(), color, runtime));
		position = 0;
		currentImage = sequences.get(position);
	}
	
	
	
	/**
	 * Change the fade color
	 * 
	 * @param color new color
	 */
	public void setFadeColor(Color color) {
		for (FadeImage fi : sequences) {
			fi.setColor(color);
		}
		this.color = color;		
	}
	
	
	
	
	/**
	 * Reset the animation
	 */
	public void reset() {
		position = 0;
		currentImage = sequences.get(position);
	}
	
	
	
	/**
	 * Plays the animation
	 */
	public void play() {
		if (!done() && !playing) {
			playing = true;
			currentImage.play();
		}
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}
	
	public void setWidth(int width) {
		this.width = width;
		
		for (FadeImage sequence : sequences) {
			sequence.setWidth(width);
		}
	}

	public void setHeight(int height) {
		this.height = height;
		
		for (FadeImage sequence : sequences) {
			sequence.setWidth(height);
		}
	}
	
	@Override
	public void draw(Graphics g) {
		if (playing && !done()) {
			currentImage.draw(g);
		}		
	}

	@Override
	public void update(int delta) {
		counter.update(delta);
		if (!done()) {
			if (currentImage.done()) {
				currentImage.stop();
				++position;
				if (!done()) {
					currentImage = sequences.get(position);					
					currentImage.play();
				} else {
					if (loop) {
						position = 0;
						currentImage = sequences.get(position);					
						currentImage.play();
					} else {
						playing = false;
					}
				}
			}
			
			if (playing) {
				currentImage.update(delta);
			}
		}
	}
	
	
	/**
	 * @return Returns true, when animation is done
	 */
	public boolean done() {
		return position < 0 || position >= sequences.size();
	}	
}