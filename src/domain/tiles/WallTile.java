package domain.tiles;

import domain.actors.Actor;
import domain.game.Position;
import domain.gate.Gate;
import domain.item.Item;

/**
 * WallTiles are blockages in the map that cannot be overcome. They cannot be entered,
 * and they cannot hold Actors, Items, or Gates.
 *
 * @author Michael Dasan 300130610
 */
public class WallTile extends Tile {
	
	//===================================================================
	// Constructors
	//===================================================================

	/**
	 * Creates a new WallTile at the given position in the maze.
	 *
	 * @param pos
	 */
	public WallTile(Position pos){
		super(pos);
		filename = "wall";
		setImage();
	}
	
	/**
	 * Converts x and y into a Position, then creates a new WallTile at that Position.
	 *
	 * @param x
	 * @param y
	 */
	public WallTile(int x, int y) {
		super(x, y);
		filename = "wall";
		setImage();
	}
	
	//===================================================================
	// Movement controls
	//===================================================================

	@Override
	public boolean canMoveOnto() {
		return false;
	}

	@Override
	public void onEntry() {
		throw new IllegalStateException("Actor has entered a WallTile.");
	}
	
	//===================================================================
	// Field controls
	//===================================================================

	@Override
	public boolean setActor(Actor a) {
		return false;
	}

	@Override
	public boolean setItem(Item i) {
		return false;
	}

	@Override
	public boolean setGate(Gate g) {
		return false;
	}

}
