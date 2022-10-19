package domain.actors;

import domain.game.Game.Direction;
import domain.game.GameObject;
import domain.game.Position;
import domain.tiles.Tile;
import domain.tiles.WallTile;

/**
 * Actors are characters in the game. They are the only game objects capable of moving 
 * around the board. Chap is the subclass for actors that are controlled by the player.
 * Actors that the player does not control must implement the NonPlayerActor interface.
 *
 * @author Michael Dasan 300130610
 */
public abstract class Actor extends GameObject{
	
	//===================================================================
	// Fields
	//===================================================================
	
	/**
	 * The Tile this Actor is located in.
	 */
	protected Tile tile;
	
	/**
	 * The direction this Actor is facing. Only useful for actors that have images
	 * for each Direction.
	 */
	protected Direction direction = Direction.RIGHT;
	
	//===================================================================
	// Constructors
	//===================================================================
	
	/**
	 * Basic constructor.
	 */
	public Actor() {}
	
	/**
	 * Constructs a new Actor in the given Tile.
	 *
	 * @param t The Tile this actor begins in. Cannot be a WallTile.
	 */
	public Actor(Tile t) {
		if(t instanceof WallTile) {
			throw new IllegalArgumentException("Cannot instantiate an Actor in a WallTile.");
		}
		this.tile = t;
		t.setActor(this);
		assert t.getActor().equals(this);
	}
	
	//===================================================================
	// Position controls
	//===================================================================
	
	/**
	 * @return The Position of the Tile this Actor is located in.
	 */
	public Position getPosition() {
		return tile.getPosition();
	}
	
	//===================================================================
	// Tile controls
	//===================================================================
	
	/**
	 * @return The Tile this Actor is located in.
	 */
	public Tile getTile() {
		return tile;
	}
	
	/**
	 * Sets the Tile this Actor is in.
	 *
	 * @param t The Tile that contains this Actor.
	 */
	public void setTile(Tile t) {
		tile = t;
		assert this.tile.equals(t);
	}
	
	//===================================================================
	// Direction controls
	//===================================================================
	
	/**
	 * @return The Direction this Actor is facing in. Only useful for actors that have images
	 * 			for each Direction.
	 */
	public Direction getDirection() {
		return direction;
	}
	
	/**
	 * Updates this Actor to be facing in the specified direction. Only useful for actors that
	 * have images for each Direction.
	 *
	 * @param d The direction for this Actor to face in.
	 */
	public void setDirection(Direction d) {
		this.direction = d;
	}
	
	//===================================================================
	// Comparison methods
	//===================================================================

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((direction == null) ? 0 : direction.hashCode());
		result = prime * result + ((tile == null) ? 0 : tile.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Actor)) {
			return false;
		}
		Actor other = (Actor) obj;
		if (direction != other.direction) {
			return false;
		}
		if (tile == null) {
			if (other.tile != null) {
				return false;
			}
		} else if (!tile.equals(other.tile)) {
			return false;
		}
		return true;
	}

}
