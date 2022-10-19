package domain.actors;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;

import javax.imageio.ImageIO;

import domain.astar.AStarSearch;
import domain.astar.NoPathFoundException;
import domain.game.Game;
import domain.game.Position;
import domain.item.Treasure;
import domain.tiles.Tile;

/**
 * BugEnemys are simple time wasters - if they can see the player, they move to them, steal a treasure, and flee.
 * When they return to their home tile, they drop the treasure and disappear. Their objective is not to make the 
 * level impossible to complete, rather to suck up enough of the players time that the level timer runs out.
 * 
 * <p>
 * Sadly, BugEnemy is currently non-functional.
 * </p>
 *
 * @author Michael Dasan 300130610
 */
public class BugEnemy extends Actor implements NonPlayerActor {
	
	//===================================================================
	// Enums
	//===================================================================
	
	/**
	 * Represents the different AI states a BugEnemy can be in.
	 *
	 * @author Michael Dasan
	 */
	enum State {
		/**
		 * A returning BugEnemy is moving back to its home Tile, either because it has lost sight
		 * of Chap, or because it has stolen a treasure from Chap.
		 */
		RETURNING, 
		
		/**
		 * A searching BugEnemy is looking for Chap, but otherwise idle.
		 */
		SEARCHING, 
		
		/**
		 * A tracking BugEnemy has located Chap, and is moving to steal a Treasure.
		 */
		TRACKING
	}
	
	//===================================================================
	// Fields
	//===================================================================
	
	/**
	 * A BugEnemy's state controls its AI. See the State enum for more details.
	 */
	private State state = State.SEARCHING;
	
	/**
	 * A stack representing the path of tiles this Actor is currently moving on.
	 */
	private ArrayDeque<Tile> path;
	
	/**
	 * Contains an image of this actor facing up.
	 */
	private BufferedImage imgUp;
	
	/**
	 * Contains an image of this actor facing down.
	 */
	private BufferedImage imgDown;
	
	/**
	 * Contains an image of this actor facing left.
	 */
	private BufferedImage imgLeft;
	
	/**
	 * Contains an image of this actor facing right.
	 */
	private BufferedImage imgRight;
	
	/**
	 * The number of tiles away this enemy can see. Given that no more than a 9x9 chunk of the maze is
	 * ever drawn on screen at any given time, this number should never exceed 9.
	 * 
	 * <p>
	 * Also controls how far the BugEnemy can move from its home.
	 * </p>
	 */
	private int vision = 8;
	
	/**
	 * True if this is carrying a treasure.
	 */
	private boolean hasTreasure = false;
	
	/**
	 * The "home" tile for this enemy - the location it returns to if it has lost sight of its target.
	 * Initialised as the Position of the Tile it is created in.
	 */
	private Position home;
	
	//===================================================================
	// Constructors
	//===================================================================

	/**
	 * Creates a new BugEnemy in the specified Tile.
	 *
	 * @param t The Tile this BugEnemy will be initialised in.
	 */
	public BugEnemy(Tile t) {
		super(t);
		filename = "bug";
		this.home = t.getPosition();
		Game.addEnemy(this);
		setImage();
		setAllImages();
	}
	
	//===================================================================
	// Image controls
	//===================================================================
	
	/**
	 * Sets the images associated with each direction this actor can face. The standard
	 * img field is retained as a default.
	 */
	private void setAllImages() {
		//Set up image
		File image = new File(resourcePath + filename + "_up" + filetype);
		try {
			imgUp = ImageIO.read(image);
		} catch(IOException e) {
			System.out.println("Error setting image on BugEnemy: " + e);
		}
		
		//Set down image
		image = new File(resourcePath + filename + "_down" + filetype);
		try {
			imgDown = ImageIO.read(image);
		} catch(IOException e) {
			System.out.println("Error setting image on BugEnemy: " + e);
		}
		
		//Set left image
		image = new File(resourcePath + filename + "_left" + filetype);
		try {
			imgLeft = ImageIO.read(image);
		} catch(IOException e) {
			System.out.println("Error setting image on BugEnemy: " + e);
		}
		
		//Set right image
		image = new File(resourcePath + filename + "_right" + filetype);
		try {
			imgRight = ImageIO.read(image);
		} catch(IOException e) {
			System.out.println("Error setting image on BugEnemy: " + e);
		}
	}
	
	/**
	 * Allows GUI and Renderer to obtain an image of this actor facing in the direction it
	 * last moved.
	 *
	 * @return A Buffered image of this Actor facing in the direction it last moved.
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
	
	//===================================================================
	// Movement controls
	//===================================================================

	@Override
	public void move() {
		if(state == State.RETURNING) {
			returnMove();
		}else if(state == State.SEARCHING) {
			searchMove();
		}else if(state == State.TRACKING) {
			trackMove();
		}
	}
	
	/**
	 * Used when the BugEnemy is returning to its home.
	 */
	public void returnMove() {
		//A* search from current pos to home
		if(path.peek() == null) {
			try {
				path = AStarSearch.pathfind(getPosition(), home, false);
			} catch (NoPathFoundException e) {
				// This shouldn't be able to happen.
				throw new IllegalStateException("BugEnemy unable to return home.");
			}
		}
		//move to next tile in path if able
		if(!path.peek().hasActor()) {
			moveTo(path.poll());
		}
		//if at home, drop treasure and disappear (if carrying treasure)
		if(tile.getPosition().equals(home)) {
			if(hasTreasure) {
				tile.setItem(new Treasure(tile));
				destruct();
			}
			//otherwise, begin searching
			state = State.SEARCHING;
		}else {
			//not at home, see if chap is visible
			searchMove();
		}
	}
	
	/**
	 * Used in two instances:<p>
	 * 1) The BugEnemy is looking for an enemy, but otherwise idle.<p>
	 * 2) The BugEnemy is returning home, and looking for Chap as it does so.
	 * </p></p>
	 */
	public void searchMove() {
		//Identify if Chap is visible (within range and vision not blocked)
		if(canSeeChap()) {
			try {
				path = AStarSearch.pathfind(getPosition(), getChapPosition(), false);
				state = State.TRACKING;
			} catch (NoPathFoundException e) {
				// This shouldn't be able to happen.
				throw new IllegalStateException("BugEnemy unable to path to Chap.");
			}
		}
		
	}
	
	/**
	 * Used when the BugEnemy has located an enemy, and is moving to attack.
	 */
	public void trackMove() {
		//A* to target.
		if(path.peek() == null) {
			try {
				path = AStarSearch.pathfind(getPosition(), getChapPosition(), false);
			} catch (NoPathFoundException e) {
				// This shouldn't be able to happen.
				throw new IllegalStateException("BugEnemy unable to path to Chap.");
			}
		}
		//move along path
		if(!path.peek().hasActor()) {
			moveTo(path.poll());
		}
		if(path.peek().hasActor() && path.peek().getActor() instanceof Chap) {
			//Next to Chap, steal treasure
			if(Game.getPlayer().hasTreasure()) {
				takeTreasure();
			}
			returnHome();
			return;
		}else if(AStarSearch.getDistBetween(getPosition(), home) >= vision * 1.5) {
			//Next step out of range, return home instead
			returnHome();
			return;
		}
		//check if have lost sight of chap
		if(!canSeeChap()) {
			returnHome();
		}
	}
	
	/**
	 * Moves the BugEnemy from its current Tile into the given Tile.
	 *
	 * @param t The Tile to move onto.
	 */
	private void moveTo(Tile t) {
		direction = getPosition().getDirectionTo(t.getPosition());
		tile.setActor(null);
		this.tile = t;
		t.setActor(this);
	}
	
	/**
	 * Tells the BugEnemy to begin returning to its home Tile.
	 */
	private void returnHome() {
		try {
			path = AStarSearch.pathfind(getPosition(), home, false);
			this.state = State.RETURNING;
		} catch (NoPathFoundException e) {
			// This shouldn't be able to happen.
			throw new IllegalStateException("BugEnemy unable to pathfind home.");
		}
	}
	
	/**
	 * @return Chap's position.
	 */
	private Position getChapPosition() {
		return Game.getPlayer().getPosition();
	}
	
	/**
	 * Steals a treasure from the player.
	 */
	private void takeTreasure() {
		Game.getPlayer().takeTreasure();
		hasTreasure = true;
	}
	
	/**
	 * Checks if the BugEnenmy can see Chap.
	 *
	 * @return True if Chap is within vision range and not blocked by a Wall or Gate.
	 */
	private boolean canSeeChap() {
		if(chapWithinRange()) {
			return !visionBlocked();
		}
		return false;
	}
	
	/**
	 * Used to find if Chap is within vision range of the BugEnemy. Note that this does not take
	 * into account whether or not an object is blocking line of sight.
	 *
	 * @return True if Chap is within vision range, otherwise false.
	 */
	private boolean chapWithinRange() {
		return AStarSearch.getDistBetween(this.getPosition(), getChapPosition()) <= vision;
	}
	
	/**
	 * Checks if the BugEnemys vision of Chap is blocked by an object. Gates and WallTiles block 
	 * vision.
	 *
	 * @return True if vision is blocked, otherwise false.
	 */
	private boolean visionBlocked() {
		try {
			ArrayDeque<Tile> sightline = AStarSearch.pathfind(getPosition(), getChapPosition(), true);
			while(sightline.peek() != null) {
				if(sightline.pop().blocksVision()) {
					return true;
				}
			}
		}catch (NoPathFoundException e) {
			//No path to target, consume exception and idle
			return true;
		}
		return false;
	}
	
	/**
	 * Removes this Actor from the Game.
	 */
	private void destruct() {
		tile.setActor(null);
		tile = null;
		Game.getLevel().removeEnemy(this);
	}
	
	/**
	 * @return The current path the BugEnemy is on, as a stack of Tiles.
	 */
	public ArrayDeque<Tile> getPath() {
		return path;
	}

}
