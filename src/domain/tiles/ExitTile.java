package domain.tiles;

import domain.actors.Actor;
import domain.game.Game;
import domain.game.Game.GameState;
import domain.game.Position;
import domain.gate.Gate;
import domain.item.Item;

/**
 * Represents the exit from the maze. Should be locked behind the TreasureGate.
 * Should not contain Items or Gates.
 *
 * @author Michael Dasan 300130610
 */
public class ExitTile extends Tile {
	
	//===================================================================
	// Fields
	//===================================================================
	
	/**
	 * The number of Treasures the Player needs to have collected in order to beat the Level.
	 */
	private int treasuresNeeded;
	
	//===================================================================
	// Constructors
	//===================================================================

	/**
	 * Basic constructor. Instantiates a new ExitTile at the given Position.
	 *
	 * @param pos The Position this Tile will be created at.
	 * @param treasuresNeeded 
	 */
	public ExitTile(Position pos, int treasuresNeeded) {
		super(pos);
		this.treasuresNeeded = treasuresNeeded;
		filename = "exit";
		setImage();
	}
	
	/**
	 * Converts x and y into a Position, then instantiates a new ExitTile at that Position.
	 *
	 * @param x X-coordinate for the Position this Tile will be located at.
	 * @param y Y-coordinate for the Position this Tile will be located at.
	 * @param treasuresNeeded 
	 */
	public ExitTile(int x, int y, int treasuresNeeded) {
		super(x, y);
		this.treasuresNeeded = treasuresNeeded;
		filename = "exit";
		setImage();
	}
	
	//===================================================================
	// Movement controls
	//===================================================================

	@Override
	public boolean canMoveOnto() {
		if(actor != null) {
			return false;
		}
		return true;
	}

	@Override
	public void onEntry() throws IllegalStateException {
		if(Game.getPlayer().getNumTreasures() >= treasuresNeeded) {
			Game.gameOver(true);
			assert Game.getState() == GameState.FINISHED;
		}else {
			throw new IllegalStateException("Player entered exit without having collected all treasures.");
		}
	}
	
	//===================================================================
	// GameObject controls
	//===================================================================

	@Override
	public boolean setActor(Actor a) {
		if(actor != null) {
			return false;
		}
		actor = a;
		return true;
	}

	@Override
	public boolean setItem(Item i) {
		return false;
	}

	@Override
	public boolean setGate(Gate g) {
		return false;
	}
	
	//===================================================================
	// Comparison methods
	//===================================================================

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + treasuresNeeded;
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
		if (!(obj instanceof ExitTile)) {
			return false;
		}
		ExitTile other = (ExitTile) obj;
		if (treasuresNeeded != other.treasuresNeeded) {
			return false;
		}
		return true;
	}
}
