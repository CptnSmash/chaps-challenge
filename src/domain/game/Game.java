package domain.game;

import domain.tiles.Tile;

import java.util.HashSet;

import domain.actors.BugEnemy;
import domain.actors.NonPlayerActor;
import domain.mvc.Subject;

/**
 * The Game class handles the internal logic of Chap's Challenge.
 *
 * @author Michael Dasan 300130610
 */
public class Game extends Subject implements Runnable{
	
	//===================================================================
	// Fields
	//===================================================================
	
	/**
	 * The Level containing the map that Chap will traverse.
	 */
	private static Level level;
	
	/**
	 * Represents the player of the game.
	 */
	private static Player player;
	
	/**
	 * What state the game is currently in.
	 */
	private static GameState state;
	
	//===================================================================
	// Enums
	//===================================================================
	
	/**
	 * The directions that Actors can move.
	 *
	 * @author Michael Dasan
	 */
	public enum Direction {
		@SuppressWarnings("javadoc")
		UP, 
		
		@SuppressWarnings("javadoc")
		DOWN, 
		
		@SuppressWarnings("javadoc")
		LEFT, 
		
		@SuppressWarnings("javadoc")
		RIGHT}
	
	/**
	 * Whether the game is ongoing, paused or completed.
	 *
	 * @author Michael Dasan
	 */
	public enum GameState{
		/**
		 * Indicates that the game is active.
		 */
		RUNNING, 
	
		/**
		 * Indicates that the game is not active, but not completed.
		 */
		PAUSED, 
	
		/**
		 * Indicates that the game is completed and inactive.
		 */
		FINISHED
	}
	
	//===================================================================
	// Constructors
	//===================================================================
	
	/**
	 * Instantiates a new Game containing the given Level and Player.
	 *
	 * @param l The level to be played.
	 * @param p The player who explores the maze.
	 */
	public Game(Level l, Player p) {
		level = l;
		player = p;
		state = GameState.RUNNING;
	}
	
	//===================================================================
	// Thread controls
	//===================================================================
	
	@Override
	public void run() {
		while(state != GameState.FINISHED) {
			level.moveAllEnemies();
			try {
				Thread.sleep(1000/3); //commands execute three times per second
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	//===================================================================
	// Play controls
	//===================================================================
	
	/**
	 * Resets the game to default, and initialises the given Level and Player.
	 * This can be used to change levels without having to construct a new Game object.
	 *
	 * @param l The new Level to play.
	 * @param p The new Player playing.
	 */
	public static void restart(Level l, Player p) {
		level = l;
		player = p;
		state = GameState.RUNNING;
	}
	
	/**
	 * Moves the Player one Tile in the given direction.
	 *
	 * @param d The direction to move the Player in.
	 */
	public void movePlayer(Direction d) {
		if(state == GameState.PAUSED) {
			throw new IllegalStateException("Cannot move Player while game is paused.");
		}
		if(state == GameState.FINISHED) {
			throw new IllegalStateException("Attempting to move Player when game is finished.");
		}
		try {
			player.move(d);
		} catch (IllegalStateException e) {
			//Three possible exceptions being caught here
			//1. No tile in given direction
			//2. Actor entered a Wall or Tile with a Gate (Gate was not removed)
			//3. Player reached the Exit without having enough treasures.
			System.out.println(e);
			return;
		}catch(IllegalArgumentException iae) {
			System.out.println(iae);
			return;
		}
		notifyAllObservers();
	}
	
	/**
	 * Ends the Game.
	 *
	 * @param win True if the Player has escaped the maze, false if they ran out of time.
	 * @throws IllegalStateException If the game was not running when the method was called.
	 */
	public static void gameOver(boolean win) throws IllegalStateException {
		if(state != GameState.RUNNING) {
			throw new IllegalStateException("Trying to end game that is already over.");
		}
		state = GameState.FINISHED;
		if(win) {
			notifyAllWithMessage("You escaped the maze! Congratulations!");
		}else {
			notifyAllWithMessage("You did not escape the maze in time.");
		}
	}
	
	//===================================================================
	// Board controls
	//===================================================================
	
	/**
	 * @return An array of integers, where [0] is the width of the board, and [1] is the height.
	 */
	public int[] getBoardDimension() {
		int[] dimensions = {level.getBoard().getWidth(), level.getBoard().getHeight()};
		return dimensions;
	}
	
	/**
	 * Helper method to prevent needing direct access to the Board class. Simply calls 
	 * Level.getTileAt(Position).
	 *
	 * @param pos The Position where the Tile is to be found.
	 * @return The Tile at that Position.
	 * @throws IllegalArgumentException From Board.getTileAt(Position).
	 */
	public Tile getTileAt(Position pos) throws IllegalArgumentException {
		return level.getTileAt(pos);
	}
	
	//===================================================================
	// Player controls
	//===================================================================
	
	/**
	 * @return The Player object associated with this game.
	 */
	public static Player getPlayer() {
		return player;
	}
	
	/**
	 * Associates the specified Player with this Game.
	 *
	 * @param p The Player that will be playing this Game.
	 */
	public static void setPlayer(Player p) {
		player = p;
	}
	
	//===================================================================
	// Level controls
	//===================================================================
	
	/**
	 * @return The level being played.
	 */
	public static Level getLevel() {
		return level;
	}
	
	/**
	 * Associates the specified Level with this Game.
	 *
	 * @param l The level the player will be playing.
	 */
	public static void setLevel(Level l) {
		level = l;
	}
	
	//===================================================================
	// State controls
	//===================================================================
	
	/**
	 * @return Whether the Game is running, paused or completed.
	 */
	public static GameState getState() {
		return state;
	}
	
	/**
	 * Updates the game state to be running, paused or completed
	 *
	 * @param gs The new state for the game to be in.
	 */
	public static void setState(GameState gs) {
		state = gs;
		assert state == gs;
	}
	
	//===================================================================
	// Enemy controls
	//===================================================================
	
	/**
	 * TODO: Hacky implementation - as BugEnemies are the only current type of enemies,
	 * this casts them from NonPlayerActor to BugEnemy. Will need to be revisited if 
	 * new enemies are added.
	 *
	 * @return A HashSet of each enemy in the level.
	 */
	public HashSet<BugEnemy> getAllEnemies() {
		HashSet<BugEnemy> enemies = new HashSet<BugEnemy>();
		for(NonPlayerActor npa : level.getEnemies()) {
			if(npa instanceof BugEnemy) {
				enemies.add((BugEnemy) npa);
			}
		}
		return enemies;
	}
	
	/**
	 * Adds the given NonPlayerActor to the set of enemies.
	 *
	 * @param e
	 * 		The enemy to be added.
	 */
	public static void addEnemy(NonPlayerActor e) {
		level.getEnemies().add(e);
	}

}
