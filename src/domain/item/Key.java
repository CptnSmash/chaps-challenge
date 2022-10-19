package domain.item;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import domain.tiles.Tile;

/**
 * Keys are Items that allow the player to progress past KeyGates - provided that the
 * key is the same colour as the gate.
 *
 * @author Michael Dasan 300130610
 */
public class Key extends Item {
	
	//===================================================================
	// Fields
	//===================================================================
	
	/**
	 * The colour of this key.
	 */
	private final Colour COLOUR;
	
	//===================================================================
	// Constructors
	//===================================================================
	
	/**
	 * Basic constructor. Creates a Key of the given colour.
	 *
	 * @param c The Colour this Key will be.
	 */
	public Key(Colour c) {
		super();
		this.COLOUR = c;
		filename = "key";
		setImage();
	}
	
	/**
	 * Creates a new Key of the given colour, in the given Tile.
	 *
	 * @param t The Tile where this key will be found.
	 * @param c The colour of the Key.
	 */
	public Key(Tile t, Colour c) {
		super(t);
		this.COLOUR = c;
		filename = "key";
		setImage();
	}
	
	//===================================================================
	// Image controls
	//===================================================================
	
	@Override
	protected void setImage(){
		File image = new File(resourcePath + filename + COLOUR.getFilename() + filetype);
		try {
			img = ImageIO.read(image);
		} catch(IOException e) {
			System.out.println("Error setting image on " + this.getClass().toString() + ": " + e);
		}
	}
	
	//===================================================================
	// Colour controls
	//===================================================================
	
	/**
	 * @return The colour of this key.
	 */
	public Colour getColour() {
		return COLOUR;
	}
	
	//===================================================================
	// Comparison methods
	//===================================================================

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((COLOUR == null) ? 0 : COLOUR.hashCode());
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
		if (!(obj instanceof Key)) {
			return false;
		}
		Key other = (Key) obj;
		if (COLOUR != other.COLOUR) {
			return false;
		}
		return true;
	}
	
	
	
}
