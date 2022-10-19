package domain.game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * GameObjects are any object that can be in the maze of Chap's Challenge.
 * 
 * <p>
 * Though the Colour enum is declared here, not all subclasses of GameObject will make use of it.
 * </p>
 *
 * @author Michael Dasan 300130610
 */
public abstract class GameObject {
	
	//===================================================================
	// Enums
	//===================================================================
	
	/**
	 * The colours that Keys and KeyGates can be.
	 *
	 * @author Michael Dasan 300130610
	 */
	public enum Colour {
		
		@SuppressWarnings("javadoc")
		RED("_red"),
		
		@SuppressWarnings("javadoc")
		GREEN("_green"),
		
		@SuppressWarnings("javadoc")
		BLUE("_blue");
		
		/**
		 * Adds on to the filename when setting an image to determine its colour.
		 */
		private String filename;
		
		/**
		 * Creates a new colour, which directs to image files of that colour.
		 *
		 * @param filename A substring of filenames of this colour.
		 */
		private Colour(String filename) {
			this.filename = filename;
		}
		
		/**
		 * Gets the substring of the name of the image of this colour.
		 *
		 * @return A substring used to direct to image files of this colour.
		 */
		public String getFilename() {
			return filename;
		}
	}
	
	//===================================================================
	// Fields
	//===================================================================
	
	/**
	 * The image representing this GameObject.
	 */
	protected BufferedImage img;
	
	/**
	 * Refers to the folder containing the image files.
	 */
	protected final String resourcePath = "data/";
	
	/**
	 * Refers to the file type of the images.
	 */
	protected final String filetype = ".png";
	
	/**
	 * The name of the image representing this object, unique to each GameObject subclass.
	 */
	protected String filename;
	
	//===================================================================
	// Image controls
	//===================================================================
	
	/**
	 * Sets the image associated with this GameObject.
	 * 
	 * <p>
	 * NOTE: This does not account for if this GameObject has a colour. If it does (Keys, KeyGates),
	 * this method must be overridden to account for this.
	 * </p>
	 */
	protected void setImage(){
		File image = new File(resourcePath + filename + filetype);
		assert image.exists();
		try {
			img = ImageIO.read(image);
			assert img != null;
		} catch(IOException e) {
			System.out.println("Error setting image on " + this.getClass().toString() + ": " + e);
		}
	}
	
	/**
	 * Returns the image associated with this GameObject. If this GameObject is a Tile, and it contains 
	 * a Gate or Item, this method should instead return the image associated with those. If the Tile 
	 * contains both a Gate and an Item, this method should return the image associated with the Gate.
	 *
	 * @return The image associated with this GameObject.
	 */
	public BufferedImage getImage() {
		return img;
	}
	
	//===================================================================
	// Comparison methods
	//===================================================================

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((filename == null) ? 0 : filename.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof GameObject)) {
			return false;
		}
		GameObject other = (GameObject) obj;
		if (filename == null) {
			if (other.filename != null) {
				return false;
			}
		} else if (!filename.equals(other.filename)) {
			return false;
		}
		return true;
	}
	
}
