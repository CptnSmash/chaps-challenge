package domain.tiles;

import java.awt.image.BufferedImage;
import domain.actors.Actor;
import domain.game.Game;
import domain.game.GameObject;
import domain.game.Position;
import domain.item.Item;
import domain.item.Key;
import domain.item.Treasure;
import domain.gate.Gate;

/**
 * FreeTiles are the most basic Tile. Empty by default, they serve as containers for most other maze components.
 * For example, our implementation of the design document does not include KeyTiles or GateTiles, rather Keys 
 * and Gates are held within FreeTiles, to make removing them from the maze simpler.
 *
 * @author Michael Dasan 300130610
 */
public class FreeTile extends Tile {
	
	//===================================================================
	// Constructors
	//===================================================================
	
	//**Position-based constructors**

	/**
	 * Basic constructor. Instantiates a new FreeTile at the given Position.
	 *
	 * @param pos The Position this Tile will be located at.
	 */
	public FreeTile(Position pos) {
		super(pos);
		filename = "open_tile";
		setImage();
	}
	
	/**
	 * Instantiates a new FreeTile containing the given GameObject.
	 *
	 * @param pos The Position this Tile will be located at.
	 * @param obj The GameObject this Tile will contain. Must be an Actor, Gate or Item.
	 * @throws IllegalArgumentException If obj is not an Actor, Gate or Item.
	 */
	public FreeTile(Position pos, GameObject obj) throws IllegalArgumentException {
		super(pos, obj);
		filename = "open_tile";
		setImage();
	}
	
	//**Coordinate-based constructors**
	
	/**
	 * Basic constructor. Converts x and y into a Position, then instantiates a new FreeTile
	 * at that Position.
	 *
	 * @param x X-coordinate for the Position this Tile will be located at.
	 * @param y Y-coordinate for the Position this Tile will be located at.
	 */
	public FreeTile(int x, int y) {
		super(x, y);
		filename = "open_tile";
		setImage();
	}
	
	/**
	 * Converts x and y into a Position, then instantiates a new FreeTile containing the given 
	 * GameObject at that Position.
	 *
	 * @param x X-coordinate for the Position this Tile will be located at.
	 * @param y Y-coordinate for the Position this Tile will be located at.
	 * @param obj The GameObject this Tile will contain. Must be an Actor, Gate or Item.
	 * @throws IllegalArgumentException If obj is not an Actor, Gate or Item.
	 */
	public FreeTile(int x, int y, GameObject obj) throws IllegalArgumentException {
		super(x, y, obj);
		filename = "open_tile";
		setImage();
	}
	
	//===================================================================
	// Movement controls
	//===================================================================

	@Override
	public boolean canMoveOnto() {
		if(this.actor != null) {
			return false;
		}
		if(this.gate != null) {
			if(gate.open()) {
				assert gate == null;
			}else {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void onEntry() {
		if(gate != null) {
			throw new IllegalStateException("Actor overlapping with gate.");
		}
		if(hasItem()) {
			if(item instanceof Key) {
				Key k = (Key) item;
				Game.getPlayer().addKey(k.getColour());
				removeItem();
			}
			if(item instanceof Treasure) {
				Game.getPlayer().addTreasure();
				removeItem();
			}
		}
	}
	
	//===================================================================
	// Field controls
	//===================================================================

	@Override
	public boolean setActor(Actor a) {
		if(this.actor == null && this.gate == null) {
			this.actor = a;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean setItem(Item i) {
		if(item != null && actor != null) {
			return false;
		}
		item = i;
		i.setTile(this);
		assert i.getTile().equals(this);
		return true;
	}

	@Override
	public boolean setGate(Gate g) {
		if(gate != null && actor != null) {
			return false;
		}
		gate = g;
		return true;
	}
	
	//===================================================================
	// Image controls
	//===================================================================
	
	@Override
	public BufferedImage getImage() {
		if(gate != null) {
			return gate.getImage();
		}
		if(item != null) {
			return item.getImage();
		}
		return img;
	}

	

}
