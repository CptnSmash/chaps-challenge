package domain.tiles;

import java.awt.image.BufferedImage;
import domain.actors.Actor;
import domain.game.Game;
import domain.game.GameObject;
import domain.game.Position;
import domain.gate.Gate;
import domain.item.Item;
import domain.item.Key;
import domain.item.Treasure;

/**
 * A HelpTile is essentially a FreeTile, with one difference - when the Player moves onto a HelpTile,
 * a message popup is displayed onscreen.
 *
 * @author Michael Dasan 300130610
 */
public class HelpTile extends Tile {
	
	//===================================================================
	// Fields
	//===================================================================
	
	/**
	 * The message to be displayed onscreen whenever the Player enters this Tile.
	 */
	private String helptext;
	
	//===================================================================
	// Constructors
	//===================================================================
	
	/**
	 * Constructor with message. Instantiates a new HelpTile at the given Position,
	 * containing the given message.
	 *
	 * @param pos The Position this Tile will be created at.
	 * @param helptext The message that will be displayed whenever the player enters this
	 * 					Tile.
	 */
	public HelpTile(Position pos, String helptext) {
		super(pos);
		this.helptext = helptext;
		filename = "help_tile";
		setImage();
	}
	
	public HelpTile(int x, int y, String helptext) {
		super(x, y);
		this.helptext = helptext;
		filename = "help_tile";
		setImage();
	}
	
	/**
	 * Constructor with message and GameObject. Instantiates a new HelpTile at the given Position,
	 * containing the given message and GameObject.
	 *
	 * @param pos The Position where this Tile will be created at.
	 * @param obj The GameObject this Tile will contain. Must be an Actor, Gate or Item.
	 * @param helptext The message that will be displayed whenever the player enters this Tile.
	 * @throws IllegalArgumentException If obj is not an Actor, Gate or Item.
	 */
	public HelpTile(Position pos, GameObject obj, String helptext) throws IllegalArgumentException {
		super(pos, obj);
		this.helptext = helptext;
		filename = "help_tile";
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
				item.destruct();
			}else if(item instanceof Treasure) {
				Game.getPlayer().addTreasure();
				item.destruct();
			}
		}
		Game.notifyAllWithMessage(helptext);
	}
	
	//===================================================================
	// GameObject controls
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
	// HelpText controls
	//===================================================================
	
	/**
	 * Adds the given String as this Tile's helpText. As this is unlikely to change during 
	 * gameplay, this should only be called if you used the constructor that does not take 
	 * a String as an argument.
	 *
	 * @param text The message to be displayed onscreen whenever the Player enters this Tile.
	 */
	public void setHelpText(String text) {
		helptext = text;
	}
	
	/**
	 * Returns the message held within this Tile. Used to draw it to the screen.
	 *
	 * @return The message contained in the helpText field.
	 */
	public String getHelpText() {
		return helptext;
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
	
	//===================================================================
	// Comparison methods
	//===================================================================

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((helptext == null) ? 0 : helptext.hashCode());
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
		if (!(obj instanceof HelpTile)) {
			return false;
		}
		HelpTile other = (HelpTile) obj;
		if (helptext == null) {
			if (other.helptext != null) {
				return false;
			}
		} else if (!helptext.equals(other.helptext)) {
			return false;
		}
		return true;
	}

}
