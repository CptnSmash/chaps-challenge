package domain.gate;

import domain.game.Game;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import domain.tiles.Tile;

/**
 * A KeyGate is a blockage in a Tile that can be overcome if the player has a 
 * key of the matching colour when they try to move onto it. This causes the 
 * gate to disappear.
 *
 * @author Michael Dasan
 */
public class KeyGate extends Gate {
	
	/**
	 * The colour of the gate. If the player holds a key of the matching colour,
	 * they can open the gate.
	 */
	private final Colour COLOUR;
	
	/**
	 * Instantiates a KeyGate of the given colour in the given Tile.
	 *
	 * @param t The Tile that will contain this gate.
	 * @param c The colour this gate will be.
	 */
	public KeyGate(Tile t, Colour c) {
		super(t);
		this.COLOUR = c;
		filename = "lock";
		setImage();
	}
	
	public KeyGate(Colour c) {
		super();
		this.COLOUR = c;
		filename = "lock";
		setImage();
	}
	
	@Override
	protected void setImage() {
		File image = new File(resourcePath + filename + COLOUR.getFilename() + filetype);
		try {
			img = ImageIO.read(image);
		} catch(IOException e) {
			System.out.println("Error setting image on KeyGate: " + e);
		}
	}

	@Override
	public boolean open() {
		if(Game.getPlayer().hasKey(COLOUR)) {
			Game.getPlayer().removeKey(COLOUR);
			destruct();
			return true;
		}
		return false;
	}
	
	/**
	 * Returns the colour of this Gate.
	 *
	 * @return The colour of this gate.
	 */
	public Colour getColour() {
		return COLOUR;
	}

	@Override
	public BufferedImage getImage() {
		return img;
	}

}
