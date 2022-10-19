package domain.game;

import domain.game.Game.Direction;

/**
 * Represents the position of an Actor, Item, Gate or Tile on the game board.
 *
 * @author Michael Dasan 300130610
 */
public class Position {
	
	/**
	 * The X coordinate of this position.
	 */
	private final int x;
	
	/**
	 * The Y coordinate of this position.
	 */
	private final int y;
	
	/**
	 * Creates a new Position representing the given x- and y-coordinates.
	 *
	 * @param x
	 * @param y
	 */
	public Position(int x, int y) {
		if(x < 0) {
			throw new IllegalArgumentException("Error - X value less than 0.");
		}
		if(y < 0) {
			throw new IllegalArgumentException("Error - Y value less than 0.");
		}
		this.x = x;
		this.y = y;
	}
	
	/**
	 * @return The X coordinate of this position.
	 */
	public int getX() {
		return this.x;
	}
	
	/**
	 * @return The Y coordinate of this position.
	 */
	public int getY() {
		return this.y;
	}
	
	/**
	 * Finds what direction another Tile is in. As there are no diagonal directions, attempting to find
	 * one will throw an exception.
	 * <p>
	 * Best use scenario is finding what direction an orthogonally adjacent position is from this one.
	 * </p>
	 *
	 * @param other The other position being checked.
	 * @return What direction to move to get from this Position to other.
	 * @throws IllegalArgumentException If other is diagonally away from this position.
	 */
	public Direction getDirectionTo(Position other) throws IllegalArgumentException{
		if(x != other.getX() && y != other.getY()) {
			throw new IllegalArgumentException("Other Position diagonally displaced from this.");
		}
		if( x != other.getX()) {
			if(x - other.getX() < 0) {
				return Direction.RIGHT;
			}else {
				return Direction.LEFT;
			}
		}else {
			if(y - other.getY() < 0) {
				return Direction.UP;
			}else {
				return Direction.DOWN;
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Position)) {
			return false;
		}
		Position other = (Position) obj;
		if (x != other.x) {
			return false;
		}
		if (y != other.y) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return "X: " + x + ", Y: " + y;
	}

}
