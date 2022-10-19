package domain.actors;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import domain.game.Game.Direction;
import domain.tiles.Tile;

/**
 * Chap is the Player character of Chap's Challenge.
 *
 * @author Michael Dasan 300130610
 */
public class Chap extends Actor {
	
	//===================================================================
	// Fields
	//===================================================================
	
	/**
	 * Contains an image of Chap facing up.
	 */
	private BufferedImage imgUp;
	
	/**
	 * Contains an image of Chap facing down.
	 */
	private BufferedImage imgDown;
	
	/**
	 * Contains an image of Chap facing left.
	 */
	private BufferedImage imgLeft;
	
	/**
	 * Contains an image of Chap facing right.
	 */
	private BufferedImage imgRight;
	
	//===================================================================
	// Constructors
	//===================================================================
	
	/**
	 * Basic constructor. Instantiates a new Chap that is not in any Tile. The Chap must be
	 * manually assigned to a Tile using setTile().
	 */
	public Chap() {
		super();
		filename = "player";
		setImage();
		setAllImages();
	}
	
	/**
	 * Instantiates a new Chap contained in the given Tile.
	 *
	 * @param t The Tile Chap will begin the game in. Cannot be a WallTile.
	 */
	public Chap(Tile t) {
		super(t);
		filename = "player";
		setImage();
		setAllImages();
	}
	
	//===================================================================
	// Movement controls
	//===================================================================
	
	/**
	 * Moves this actor onto the specified tile.
	 *
	 * @param t The tile being moved onto.
	 * @param d The Direction Chap will move in, and be facing afterwards.
	 */
	public void move(Tile t, Direction d) {
		tile.setActor(null);
		this.tile = t;
		t.setActor(this);
		this.direction = d;
	}
	
	//===================================================================
	// Image controls
	//===================================================================
	
	/**
	 * Allows GUI and Renderer to obtain an image of Chap facing in the direction it
	 * last moved.
	 *
	 * @return A Buffered image of Chap facing in the direction it last moved.
	 */
	@Override
	public BufferedImage getImage() {
		switch(direction) {
			case UP:
				return imgUp;
			case DOWN:
				return imgDown;
			case LEFT:
				return imgLeft;
			case RIGHT:
				return imgRight;
			default:
				return img;
		}
	}
	
	/**
	 * Sets the images associated with each direction Chap can face. The standard
	 * img field is retained as a default.
	 */
	private void setAllImages() {
		//Set up image
		File image = new File(resourcePath + filename + "_up" + filetype);
		try {
			imgUp = ImageIO.read(image);
		} catch(IOException e) {
			System.out.println("Error setting image on Chap: " + e);
		}
		
		//Set down image
		image = new File(resourcePath + filename + "_down" + filetype);
		try {
			imgDown = ImageIO.read(image);
		} catch(IOException e) {
			System.out.println("Error setting image on Chap: " + e);
		}
		
		//Set left image
		image = new File(resourcePath + filename + "_left" + filetype);
		try {
			imgLeft = ImageIO.read(image);
		} catch(IOException e) {
			System.out.println("Error setting image on Chap: " + e);
		}
		
		//Set right image
		image = new File(resourcePath + filename + "_right" + filetype);
		try {
			imgRight = ImageIO.read(image);
		} catch(IOException e) {
			System.out.println("Error setting image on Chap: " + e);
		}
	}

}
