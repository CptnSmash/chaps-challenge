package domain.game;

import java.util.HashSet;

import domain.game.Game.Direction;
import domain.tiles.Tile;

/**
 * Boards represent the maze of each level. The maze is stored as a 2d array of Tiles,
 * where [0, 0] is the top-left corner. Thus, width-1 refers to the right edge of the 
 * maze, and height-1 refers to the bottom of the maze.
 *
 * @author Michael Dasan 300130610
 */
public class Board {
	
	//===================================================================
	// Fields
	//===================================================================
	
	/**
	 * How wide the board is, in Tiles.
	 */
	private final int width;
	
	/**
	 * How tall the maze is, in Tiles.
	 */
	private final int height;
	
	
	/**
	 * A 2d array of Tiles representing the maze.
	 */
	private Tile[][] board;
	
	//===================================================================
	// Constructors
	//===================================================================
	
	/**
	 * Creates a new Board with the given dimensions.
	 *
	 * @param width
	 * @param height
	 */
	public Board(int width, int height) {
		this.width 		= width;
		this.height 	= height;
		this.board 		= new Tile[width][height];
	}
	
	//===================================================================
	// Tile setters
	//===================================================================
	
	/**
	 * Adds an already instantiated Tile to its position in the board.
	 *
	 * @param t The Tile to be added.
	 */
	public void put(Tile t) {
		Position pos = t.getPosition();
		if(checkPos(pos)) {
			board[pos.getX()][pos.getY()] = t;
			assert board[pos.getX()][pos.getY()].equals(t);
		}
	}
	
	/**
	 * Adds the specified Tile to the maze at the specified position.
	 *
	 * @param pos
	 * @param t
	 */
	public void put(Position pos, Tile t) {
		if(checkPos(pos)) {
			board[pos.getX()][pos.getY()] = t;
			assert board[pos.getX()][pos.getY()].equals(t);
		}
	}
	
	/**
	 * Convenience method to smooth access to put(Position, Tile).
	 *
	 * @param x
	 * @param y
	 * @param t
	 */
	public void put(int x, int y, Tile t) {
		Position pos = new Position(x, y);
		if(checkPos(pos)) {
			board[pos.getX()][pos.getY()] = t;
			assert board[pos.getX()][pos.getY()].equals(t);
		}
	}
	
	//===================================================================
	// Tile getters
	//===================================================================
	
	/**
	 * Finds and returns the Tile at a given Position.
	 *
	 * @param pos The Position where the Tile is to be found.
	 * @return The Tile at that location. Throws an exception if there is no Tile there.
	 * @throws IllegalArgumentException From checkPos(Position) method.
	 */
	public Tile getTileAt(Position pos) throws IllegalArgumentException {
		if(checkPos(pos)) {
			return board[pos.getX()][pos.getY()];
		}
		return null; //should be unreachable
	}
	
	/**
	 * Convenience method to smooth access to getTileAt(Position).
	 *
	 * @param x
	 * @param y
	 * @return The Tile at Position(x, y)
	 */
	public Tile getTileAt(int x, int y) {
		Position pos = new Position(x, y);
		return getTileAt(pos);
	}
	
	/**
	 * Attempts to find the next Tile over in a given Direction from a given Position. 
	 * If there is no file in that Direction, throws an exception.
	 *
	 * @param pos The position to find from.
	 * @param d The direction to look in.
	 * @return The next Tile over in the given direction if there is one.
	 * @throws IllegalArgumentException If there is no tile in the given direction.
	 */
	public Tile getTileInDirection(Position pos, Direction d) throws IllegalArgumentException {
		Tile t = null;
		switch(d) {
			case UP:
				if(pos.getY() <= 0) {
					throw new IllegalArgumentException("No tile above " + pos.toString());
				}
				t = getTileAt(new Position(pos.getX(), pos.getY() - 1));
				assert t != null;
				return t;
			case DOWN:
				if(pos.getY() >= height) {
					throw new IllegalArgumentException("No tile below " + pos.toString());
				}
				t = getTileAt(new Position(pos.getX(), pos.getY() + 1));
				assert t != null;
				return t;
			case LEFT:
				if(pos.getX() <= 0) {
					throw new IllegalArgumentException("No tile to left of " + pos.toString());
				}
				t = getTileAt(new Position(pos.getX() - 1, pos.getY()));
				assert t != null;
				return t;
			case RIGHT:
				if(pos.getX() >= width) {
					throw new IllegalArgumentException("No tile to the right of " + pos.toString());
				}
				t = getTileAt(new Position(pos.getX() + 1, pos.getY()));
				assert t != null;
				return t;
			default:
				throw new IllegalArgumentException(d + " is not a valid direction."); //should never be reached
		}
	}
	
	/**
	 * Finds and returns each Tile that is orthogonally adjacent to the Tile at the 
	 * given Position, as a HashSet.
	 *
	 * @param pos 
	 * @return A HashSet of Tiles orthogonally adjacent to the given one.
	 */
	public HashSet<Tile> getAdjacentTiles(Position pos) {
		HashSet<Tile> adjacent = new HashSet<Tile>();
		for(Direction d : Direction.values()) {
			try {
				adjacent.add(getTileInDirection(pos, d));
			}catch (IllegalArgumentException e) {
				//no Tile in direction, consume exception and move on
			}
		}
		return adjacent;
	}
	
	//===================================================================
	// Field getters
	//===================================================================
	
	/**
	 * @return The width of the maze.
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * @return The height of the maze.
	 */
	public int getHeight() {
		return height;
	}
	
	//===================================================================
	// Utility methods
	//===================================================================
	
	/**
	 * Helper method for determining if a Position is within bounds. X and/or Y values being less than 0 is not allowed,
	 * but this is checked on Position creation.
	 *
	 * @param pos The position being checked.
	 * @return True if the position is valid, otherwise throws an exception.
	 * @throws IllegalArgumentException If the Position is null, or out of bounds.
	 */
	private boolean checkPos(Position pos) throws IllegalArgumentException {
		if(pos == null) {
			throw new IllegalArgumentException("Position cannot be null.");
		}
		if(pos.getX() >= width) {
			throw new IllegalArgumentException("Error - Position X value greater than board width. (X: " + pos.getX() + ", width: " + width + ")");
		}
		if(pos.getY() >= height) {
			throw new IllegalArgumentException("Error - Position Y value greater than board width. (X: " + pos.getX() + ", width: " + width + ")");
		}
		return true;
	}

}
