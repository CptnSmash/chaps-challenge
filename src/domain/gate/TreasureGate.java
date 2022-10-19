package domain.gate;

import domain.game.Game;
import domain.tiles.Tile;

/**
 * TreasureGates are used to block the Player from accessing the exit to the maze
 * until they have collected enough Treasures.
 *
 * @author Michael Dasan
 */
public class TreasureGate extends Gate {
	
	//===================================================================
	// Fields
	//===================================================================
	
	/**
	 * The number of Treasures the Player must collect before they can pass this Gate.
	 */
	private int treasuresNeeded;
	
	//===================================================================
	// Constructors
	//===================================================================

	/**
	 * Basic constructor. Instantiates a new TreasureGate in the given Tile, that 
	 * requires the given number of Treasures to pass.
	 *
	 * @param t The Tile that will contain this Gate.
	 * @param treasuresNeeded The number of Treasures that must be collected to pass.
	 */
	public TreasureGate(Tile t, int treasuresNeeded) {
		super(t);
		this.treasuresNeeded = treasuresNeeded;
		filename = "gate_closed";
		setImage();
	}
	
	public TreasureGate(int treasuresNeeded) {
		super();
		this.treasuresNeeded = treasuresNeeded;
		filename = "gate_closed";
		setImage();
	}
	
	//===================================================================
	// Opening controls
	//===================================================================

	@Override
	public boolean open() {
		if(Game.getPlayer().getNumTreasures() >= treasuresNeeded) {
			destruct();
			return true;
		}
		return false;
	}
	
	//===================================================================
	// Treasure controls
	//===================================================================
	
	/**
	 * Returns the number of Treasures needed to pass this Gate.
	 *
	 * @return The number of Treasures needed to pass.
	 */
	public int getTreasuresNeeded() {
		return treasuresNeeded;
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
		if (!(obj instanceof TreasureGate)) {
			return false;
		}
		TreasureGate other = (TreasureGate) obj;
		if (treasuresNeeded != other.treasuresNeeded) {
			return false;
		}
		return true;
	}

}