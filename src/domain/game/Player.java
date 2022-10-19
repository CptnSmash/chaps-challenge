package domain.game;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import domain.actors.Chap;
import domain.game.GameObject.Colour;
import domain.game.Game.Direction;
import domain.tiles.TeleportTile;
import domain.tiles.Tile;

/**
 * Player objects store information about the Chap the Player controls, as well as 
 * Items they have collected. The Player object partners with the Game class to move
 * Chap.
 *
 * @author Michael Dasan 300130610
 */
public class Player {
	
	//===================================================================
	// Fields
	//===================================================================
	
	/**
	 * A count of the number of Treasures the Player has collected.
	 */
	private int treasuresCollected = 0;
	
	/**
	 * The Chap the Player controls.
	 */
	private Chap chap;
	
	/**
	 * A Map storing the count of each colour of key the player has collected.
	 */
	private HashMap<Colour, Integer> keychain;
	
	//===================================================================
	// Constructors
	//===================================================================
	
	/**
	 * Constructor for Player class. Creates the Chap the player will control at the given tile.
	 *
	 * @param t 
	 * 		The tile where the Chap will be constructed.
	 */
	public Player(Tile t) {
		this.chap = new Chap(t);
		this.keychain = new HashMap<Colour, Integer>();
		for(Colour colour : Colour.values()) {
			this.keychain.put(colour, 0);
		}
		
	}
	
	//===================================================================
	// Movement controls
	//===================================================================
	
	/**
	 * Moves chap in the given direction.
	 *
	 * @param d
	 * 		The direction of the move. Chap will be facing in this direction after the move.
	 * @throws IllegalArgumentException
	 * 		If the given Tile is not a valid move.
	 * @throws IllegalStateException
	 * 		If chap has entered a tile it should not be able to.
	 */
	public void move(Direction d) throws IllegalArgumentException, IllegalStateException {
		Tile t = Game.getLevel().getTileInDirection(getPosition(), d);
		if(t.canMoveOnto()) {
			chap.move(t, d);
			assert getTile().equals(t);
			t.onEntry();
		}
	}
	
	/**
	 * Used to move Chap onto a Tile he is not next to. Primarily used by TeleportTile to facilitate
	 * the teleportation.
	 *
	 * @param t
	 * 		The Tile to move onto.
	 * @throws IllegalArgumentException
	 * 		If the given Tile is not a valid move.
	 * @throws IllegalStateException
	 * 		If Chap has entered a Tile it should not be able to.
	 */
	public void teleportMove(Tile t) throws IllegalArgumentException, IllegalStateException {
		if(t.canMoveOnto()) {
			chap.move(t, chap.getDirection());
			assert getTile().equals(t);
			if(!(t instanceof TeleportTile)) {
				t.onEntry();
			}
		}
	}
	
	//===================================================================
	// Location controls
	//===================================================================
		
	/**
	 * Returns the player's position.
	 *
	 * @return 
	 * 		The Position of the chap the player controls.
	 */
	public Position getPosition() {
		return getTile().getPosition();
	}
	
	/**
	 * Checks if the Player is at the specified Position.
	 *
	 * @param p 
	 * 		The Position to be checked.
	 * @return 
	 * 		True if the Chap the Player controls is at the specified Position.
	 */
	public boolean isAtPosition(Position p) {
		return chap.getPosition().equals(p);
	}
	
	/**
	 * Returns the Tile the Player is in.
	 *
	 * @return
	 * 		The Tile containing the Chap the Player controls.
	 */
	public Tile getTile() {
		Tile t = chap.getTile();
		assert t.getActor().equals(chap);
		return t;
	}
	
	/**
	 * Checks if the Player is in the specified Tile.
	 *
	 * @param t
	 * 		The Tile to be checked.
	 * @return
	 * 		True if t contains the Chap the Player controls.
	 */
	public boolean isInTile(Tile t) {
		return chap.getTile().equals(t);
	}
	
	//===================================================================
	// Treasure controls
	//===================================================================
		
	/**
	 * Gets the number of Treasures the Player has collected.
	 *
	 * @return
	 * 		The number of Treasures the Player has collected.
	 */
	public int getNumTreasures() {
		return treasuresCollected;
	}
	
	/**
	 * Increments the count of Treasures the player has collected.
	 */
	public void addTreasure() {
		int numBefore = treasuresCollected;
		treasuresCollected++;
		assert numBefore + 1 == treasuresCollected;
	}
	
	/**
	 * Decrements the count of Treasures the player has collected, minimum 0.
	 */
	public void takeTreasure() {
		int numBefore = treasuresCollected;
		treasuresCollected--;
		assert numBefore - 1 == treasuresCollected;
		if(treasuresCollected < 0) {
			treasuresCollected = 0;
			assert treasuresCollected == 0;
		}
	}
	
	/**
	 * Checks if the Player has at least one Treasure.
	 *
	 * @return 
	 * 		True if the player holds any treasures.
	 */
	public boolean hasTreasure() {
		return treasuresCollected > 0;
	}
	
	/**
	 * Confirms if the player has collected enough treasures to pass the TreasureGate.
	 * 
	 * @param treasuresNeeded 
	 * 		The number of treasures needed to pass the TreasureGate.
	 * @return 
	 * 		True if the player has or exceeds the required number, false otherwise.
	 */
	public boolean hasEnoughTreasures(int treasuresNeeded) {
		return treasuresCollected >= treasuresNeeded;
	}
	
	//===================================================================
	// Key controls
	//===================================================================
	
	/**
	 * Adds a key of the given colour to the player's inventory.
	 *
	 * @param c 
	 * 		The colour of the key to be added.
	 */
	public void addKey(Colour c) {
		int keysBefore = keychain.get(c);
		keychain.put(c, keychain.get(c) + 1);
		assert keysBefore + 1 == keychain.get(c);
	}
	
	/**
	 * Removes a key of the given colour from the player's inventory.
	 *
	 * @param c 
	 * 		The colour of key to remove.
	 */
	public void removeKey(Colour c) {
		if(hasKey(c)) {
			int keysBefore = keychain.get(c);
			keychain.put(c, keychain.get(c) - 1);
			assert keysBefore - 1 == keychain.get(c);
		}
	}
	
	/**
	 * Checks if the player holds a key of the given colour.
	 *
	 * @param c 
	 * 		The colour of key that is being checked for.
	 * @return 
	 * 		True if the player holds a key of this colour, false otherwise.
	 */
	public boolean hasKey(Colour c) {
		return keychain.get(c) > 0;
	}
	
	/**
	 * Returns the number of keys the Player holds of the given Colour.
	 *
	 * @param c 
	 * 		The Colour of keys to be counted.
	 * @return 
	 * 		The number of keys of that colour the player holds.
	 */
	public int getNumberOfKeys(Colour c) {
		return keychain.get(c);
	}
	
	/**
	 * Returns the Player's keychain - a Map of Colours to the number of keys of that Colour
	 * the Player holds.
	 *
	 * @return 
	 * 		A Map of Colours to the number of keys of that Colour.
	 */
	public HashMap<Colour, Integer> getAllKeys() {
		return keychain;
	}
	
	//===================================================================
	// Image controls
	//===================================================================
	
	/**
	 * Returns the image representing the Chap this Player controls.
	 *
	 * @return 
	 * 		A BufferedImage of Chap.
	 */
	public BufferedImage getPlayerImage() {
		return chap.getImage();
	}

}
