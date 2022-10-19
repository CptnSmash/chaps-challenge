package domain.item;

import domain.game.GameObject;
import domain.tiles.Tile;

/**
 * Items are any object in the maze that Chap can pick up and carry with him.
 * Currently there are two types of Item - Keys and Treasure.
 *
 * @author Michael Dasan 300130610
 */
public abstract class Item extends GameObject{
	
	//===================================================================
	// Fields
	//===================================================================
	
	/**
	 * The Tile this Item is located in.
	 */
	protected Tile tile;
	
	//===================================================================
	// Constructors
	//===================================================================
	
	/**
	 * Basic constructor.
	 */
	public Item() {	}
	
	/**
	 * Creates a new Item in the given Tile.
	 *
	 * @param t The Tile this Item will be located in.
	 */
	public Item(Tile t) {
		this.tile = t;
		t.setItem(this);
		assert t.getItem().equals(this);
	}
	
	//===================================================================
	// Tile controls
	//===================================================================
	
	/**
	 * Returns the Tile this Item is in.
	 *
	 * @return The Tile this Item is in.
	 */
	public Tile getTile() {
		return tile;
	}
	
	/**
	 * Sets the Tile this Item is in.
	 *
	 * @param t The Tile this Item will be placed into.
	 */
	public void setTile(Tile t) {
		tile = t;
		assert tile.equals(t);
	}
	
	/**
	 * Removes this Item from the game, by removing itself from the Tile containing it.
	 */
	public void destruct() {
		tile.setItem(null);
		assert tile.getItem() == null;
		tile = null;
		assert tile == null;
	}
	
}
