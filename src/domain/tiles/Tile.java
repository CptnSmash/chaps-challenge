package domain.tiles;

import java.util.HashSet;

import domain.actors.Actor;
import domain.game.Game;
import domain.game.Game.Direction;
import domain.game.GameObject;
import domain.game.Position;
import domain.item.Item;
import domain.gate.Gate;


/**
 * A Tile is a square on the game board. It controls what can move onto it, and what happens when 
 * something does. Tiles are containers for other game objects, such as Actors, Items and Gates.
 *
 * @author Michael Dasan 300130610
 */
public abstract class Tile extends GameObject{
	
	//===================================================================
	// Fields
	//===================================================================
	
	/**
	 * The Actor currently in this Tile, if any.
	 */
	protected Actor actor;
	
	/**
	 * The Item in this Tile, if any.
	 */
	protected Item item;
	
	/**
	 * The Gate in this Tile, if any.
	 */
	protected Gate gate;
	
	/**
	 * The 2d coordinates of where this Tile is located in the maze.
	 */
	protected final Position pos;
	
	/**
	 * The previous Tile in an A* search.
	 */
	protected Tile prev = null;
	
	//===================================================================
	// Constructors
	//===================================================================
	
	//**Position-based constructors**
	
	/**
	 * Basic constructor. Instantiates a new Tile at the given Position.
	 *
	 * @param pos The Position this Tile will be located at.
	 */
	public Tile(Position pos){
		this.pos = pos;
		assert this.pos.equals(pos);
	}
	
	/**
	 * Instantiates a new Tile at the given Position, containing the given GameObject.
	 *
	 * @param pos The Position this Tile will be located at.
	 * @param obj The GameObject to be added to this Tile. Must be an Actor, Item or Gate.
	 * @throws IllegalArgumentException If obj is not an Actor, Gate or Item.
	 */
	public Tile(Position pos, GameObject obj) throws IllegalArgumentException {
		this.pos = pos;
		assert this.pos.equals(pos);
		if(obj instanceof Actor) {
			Actor a = (Actor) obj;
			actor = a;
			a.setTile(this);
			assert actor.equals(a);
		}else if(obj instanceof Gate) {
			Gate g = (Gate) obj;
			gate = g;
			g.setTile(this);
			assert gate.equals(g);
		}else if(obj instanceof Item) {
			Item i = (Item) obj;
			item = i;
			i.setTile(this);
			assert item.equals(i);
		}else {
			throw new IllegalArgumentException("obj argument must be an Actor, Gate or Item.");
		}
	}
	
	//**Coordinate-based constructors**
	
	/**
	 * Basic constructor. Converts x and y into a Position, then instantiates a new Tile
	 * at that Position.
	 *
	 * @param x X-coordinate for the Position this Tile will be located at.
	 * @param y Y-coordinate for the Position this Tile will be located at.
	 */
	public Tile(int x, int y) {
		Position pos = new Position(x, y);
		this.pos = pos;
	}
	
	/**
	 * Converts x and y into a Position, then instatiates a new Tile at the Position, containing
	 * the given GameObject.
	 *
	 * @param x X-coordinate for the Position this Tile will be located at.
	 * @param y Y-coordinate for the Position this Tile will be located at.
	 * @param obj The GameObject to be added to this Tile. Must be an Actor, Item or Gate.
	 * @throws IllegalArgumentException If obj is not an Actor, Gate or Item.
	 */
	public Tile(int x, int y, GameObject obj) throws IllegalArgumentException {
		this.pos = new Position(x, y);;
		assert this.pos.equals(pos);
		if(obj instanceof Actor) {
			Actor a = (Actor) obj;
			actor = a;
			a.setTile(this);
			assert actor.equals(a);
		}else if(obj instanceof Gate) {
			Gate g = (Gate) obj;
			gate = g;
			g.setTile(this);
			assert gate.equals(g);
		}else if(obj instanceof Item) {
			Item i = (Item) obj;
			item = i;
			i.setTile(this);
			assert item.equals(i);
		}else {
			throw new IllegalArgumentException("obj argument must be an Actor, Gate or Item.");
		}
	}
	
	//===================================================================
	// Movement controls
	//===================================================================
	
	/**
	 * Called when an actor tries to move onto this space. Used to determine if the move is possible.
	 *
	 * @return True if the actor can move onto the space, false otherwise.
	 */
	public abstract boolean canMoveOnto();
	
	/**
	 * Determines what happens when the Player enters this Tile.
	 *
	 * @throws IllegalStateException If the Player has entered a Tile they shouldn't be able to.
	 */
	public abstract void onEntry() throws IllegalStateException;
	
	//===================================================================
	// Actor controls
	//===================================================================
	
	/**
	 * @return The actor in this tile, if any.
	 */
	public Actor getActor() {
		return actor;
	}
	
	/**
	 * Adds the given Actor to this Tile, if possible.
	 *
	 * @param a The Actor to be added.
	 * @return True if the operation was successful, false otherwise.
	 */
	public abstract boolean setActor(Actor a);
	
	/**
	 * Used for checking if the Tile contains an Actor;
	 * 
	 * @return True if there is an Actor in this Tile, false otherwise;
	 */
	public boolean hasActor() {
		return actor != null;
	}
	
	//===================================================================
	// Item controls
	//===================================================================
	
	/**
	 * @return The item contained in this Tile. Can be null.
	 */
	public Item getItem() {
		return item;
	}
	
	/**
	 * Adds the given Item to this Tile, if possible.
	 *
	 * @param i The Item to be added.
	 * @return True if the operation was successful, false otherwise.
	 */
	public abstract boolean setItem(Item i);
	
	/**
	 * Removes the Item in this Tile by setting the item field to be null.
	 */
	public void removeItem() {
		item.destruct();
		assert item == null;
	}
	
	/**
	 * Used for checking if the Tile contains an Item;
	 * 
	 * @return True if there is an Item in this Tile, false otherwise;
	 */
	public boolean hasItem() {
		return item != null;
	}
	
	//===================================================================
	// Gate controls
	//===================================================================
	
	/**
	 * @return The gate in this Tile, if any.
	 */
	public Gate getGate() {
		return gate;
	}
	
	/**
	 * Adds the given gate to this Tile, if possible.
	 *
	 * @param g The gate to be added.
	 * @return True if the operation was successful, false otherwise.
	 */
	public abstract boolean setGate(Gate g);
	
	/**
	 * Used for checking if the Tile contains a Gate;
	 * 
	 * @return True if there is a Gate in this Tile, false otherwise;
	 */
	public boolean hasGate() {
		return gate != null;
	}
	
	//===================================================================
	// Position controls
	//===================================================================
	
	/**
	 * @return The Position this Tile is located at.
	 */
	public Position getPosition() {
		return this.pos;
	}
	
	//===================================================================
	// A* controls
	//===================================================================
	
	/**
	 * Used for checking if NonPlayerActors can see through this Tile.
	 *
	 * @return True if this Tile is a WallTile or contains a Gate, otherwise false.
	 */
	public boolean blocksVision() {
		return this instanceof WallTile || hasGate();
	}
	
	/**
	 * Sets the given Tile as the previous one visited in an A* search.
	 *
	 * @param prev
	 */
	public void setPrev(Tile prev) {
		this.prev = prev;
	}
	
	/**
	 * @return The previously visited Tile in an A* search.
	 */
	public Tile getPrev() {
		return prev;
	}
	
	/**
	 * @return A HashSet of each Tile orthogonally adjacent to this one.
	 */
	public HashSet<Tile> getAllAdjacentTiles() {
		HashSet<Tile> adj = new HashSet<Tile>();
		for(Direction d : Direction.values()) {
			Tile t = Game.getLevel().getTileInDirection(pos, d);
			if(t != null) {
				adj.add(t);
			}
		}
		return adj;
	}
	
	//===================================================================
	// Comparison methods
	//===================================================================

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((actor == null) ? 0 : actor.hashCode());
		result = prime * result + ((gate == null) ? 0 : gate.hashCode());
		result = prime * result + ((item == null) ? 0 : item.hashCode());
		result = prime * result + ((pos == null) ? 0 : pos.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof Tile)) {
			return false;
		}
		Tile other = (Tile) obj;
		if (actor == null) {
			if (other.actor != null) {
				return false;
			}
		} else if (!actor.equals(other.actor)) {
			return false;
		}
		if (gate == null) {
			if (other.gate != null) {
				return false;
			}
		} else if (!gate.equals(other.gate)) {
			return false;
		}
		if (item == null) {
			if (other.item != null) {
				return false;
			}
		} else if (!item.equals(other.item)) {
			return false;
		}
		if (pos == null) {
			if (other.pos != null) {
				return false;
			}
		} else if (!pos.equals(other.pos)) {
			return false;
		}
		return true;
	}
	
}
