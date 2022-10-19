package domain.item;

import java.awt.image.BufferedImage;

import domain.tiles.Tile;

/**
 * Treasures are Items the player must collect in order to complete the level.
 *
 * @author Michael Dasan 300130610
 */
public class Treasure extends Item {
	
	//===================================================================
	// Constructors
	//===================================================================
	
	/**
	 * Basic constructor.
	 */
	public Treasure() {
		super();
	}

	/**
	 * Creates a new Treasure in the given Tile.
	 *
	 * @param t The Tile that will contain this Treasure.
	 */
	public Treasure(Tile t) {
		super(t);
		filename = "treasure";
		setImage();
	}
	
	//===================================================================
	// Image controls
	//===================================================================

	@Override
	public BufferedImage getImage() {
		return img;
	}
}
