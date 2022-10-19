package domain.gate;

import domain.game.GameObject;
import domain.tiles.Tile;

/**
 * A Gate is a blockage in a Tile that requires the Player to meet some condition to overcome.
 * Gates are split into two subcategories - KeyGates, which require the PLayer to hold a Key of 
 * the matching colour to progress, and TreasureGates, which require the Player to hold a certain 
 * number to Treasures to progress.
 *
 * @author Michael Dasan
 */
public abstract class Gate extends GameObject{
	
	//===================================================================
	// Fields
	//===================================================================
	
	/**
	 * The tile this Gate is in.
	 */
	protected Tile tile;
	
	//===================================================================
	// Constructors
	//===================================================================
	
	/**
	 * Basic constructor.
	 */
	public Gate() {}
	
	/**
	 * Instantiates a new Gate in the given Tile.
	 *
	 * @param t The Tile that will hold this Gate.
	 */
	public Gate(Tile t) {
		this.tile = t;
		t.setGate(this);
		assert t.getGate().equals(this);
	}
	
	//===================================================================
	// Movement controls
	//===================================================================
	
	/**
	 * Allows the player to open the gate and progress, if they meet the gates condition
	 * (a key of the matching colour for KeyGates, having enough treasures for TreasureGates).
	 *
	 * @return True if the gate was opened, otherwise false;
	 */
	public abstract boolean open();
	
	//===================================================================
	// Tile controls
	//===================================================================
	
	/**
	 * Places this Gate into the given Tile.
	 *
	 * @param t The Tile that will contain this Gate.
	 */
	public void setTile(Tile t) {
		tile = t;
		assert tile.equals(t);
	}
	
	/**
	 * Returns the Tile containing this Gate.
	 *
	 * @return The Tile this Gate is in.
	 */
	public Tile getTile() {
		return tile;
	}
	
	/**
	 * Removes this gate from the Tile containing it.
	 */
	protected void destruct() {
		tile.setGate(null);
		assert tile.getGate() == null;
		tile = null;
		assert tile == null;
	}
	
	//===================================================================
	// Comparison methods
	//===================================================================

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tile == null) ? 0 : tile.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Gate)) {
			return false;
		}
		Gate other = (Gate) obj;
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
