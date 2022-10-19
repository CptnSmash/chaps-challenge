package domain.game;

import java.util.HashSet;

import domain.actors.NonPlayerActor;
import domain.game.Game.Direction;
import domain.game.Game.GameState;
import domain.tiles.Tile;

/**
 * Level objects store information about the current maze - How long you have to beat 
 * it, how many treasures you need to collect, presence of enemies and the like.
 *
 * @author Michael Dasan 300130610
 */
public class Level {
	
	/**
	 * What number stage this is - level one, level two, etc.
	 */
	private int stage;
	
	/**
	 * How long the player has to complete the level.
	 */
	private int time;
	
	/**
	 * The maze.
	 */
	private Board board;
	
	/**
	 * A set of all enemies present in the maze.
	 */
	private HashSet<NonPlayerActor> enemies;
	
	/**
	 * Constructor to be used if the board hasn't yet been generated. Must add the level in manually.
	 *
	 * @param stage What number level this is.
	 * @param time Amount of time allotted to complete the level.
	 */
	public Level(int stage, int time) {
		this.stage = stage;
		this.time = time;
		enemies = new HashSet<NonPlayerActor>();
	}
	/**
	 * Preferred constructor.
	 *
	 * @param stage What number level this is.
	 * @param time Amount of time allotted to complete the level.
	 * @param board The maze for this level.
	 */
	public Level(int stage, int time, Board board) {
		this.stage = stage;
		this.time = time;
		this.board = board;
		enemies = new HashSet<NonPlayerActor>();
	}
	
	/**
	 * @return The stage number of this level.
	 */
	public int getStage() {
		return stage;
	}
	
	/**
	 * Sets the board. Should only be used if the board wasn't specified at construction.
	 *
	 * @param b The maze for this level.
	 */
	public void setBoard(Board b) {
		this.board = b;
	}
	
	/**
	 * @return The board representing the maze for this level.
	 */
	public Board getBoard() {
		return this.board;
	}
	
	/**
	 * Calls on each enemy present in the maze to move. When the Game is initialised, a seperate
	 * thread will be created that handles this.
	 */
	public void moveAllEnemies() {
		if(Game.getState() == GameState.RUNNING) {
			for(NonPlayerActor enemy : enemies) {
				enemy.move();
			}
		}
	}
	
	/**
	 * @return The set of enemies in this level.
	 */
	public HashSet<NonPlayerActor> getEnemies() {
		return enemies;
	}
	
	/**
	 * Adds the specified enemy to this level.
	 *
	 * @param e The enemy to be added.
	 */
	public void addEnemy(NonPlayerActor e) {
		enemies.add(e);
	}
	
	/**
	 * Removes the specified enemy from this level.
	 *
	 * @param e The enemy to be removed.
	 */
	public void removeEnemy(NonPlayerActor e) {
		enemies.remove(e);
	}
	
	/**
	 * Returns the Tile at the given position in the Board.
	 *
	 * @param pos The position to be checked.
	 * @return The Tile at that position.
	 */
	public Tile getTileAt(Position pos) {
		return board.getTileAt(pos);
	}
	
	/**
	 * Obtains the Tile in the given direction from the given position.
	 *
	 * @param pos The position to begin at.
	 * @param d The direction to check in.
	 * @return The Tile in the given direction.
	 * @throws IllegalArgumentException If there is no Tile in the given direction.
	 */
	public Tile getTileInDirection(Position pos, Direction d) throws IllegalArgumentException {
		Tile t = board.getTileInDirection(pos, d);
		return t;
	}
	
	/**
	 * @return How long the player has to complete the level.
	 */
	public int getTime() {
		return time;
	}
	
	/**
	 * Sets how long the player has to complete the level.
	 *
	 * @param time The time allotted.
	 */
	public void setTime(int time) {
		this.time = time;
	}
	public Tile getTileAt(int x, int y) {
		return board.getTileAt(x, y);
	}

}
