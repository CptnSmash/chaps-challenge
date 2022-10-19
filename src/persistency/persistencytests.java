package persistency;

import static org.junit.Assert.fail;
import org.junit.Test;

import domain.game.Board;
import domain.game.Game;
import domain.game.GameObject;
import domain.game.GameObject.Colour;
import domain.game.Level;
import domain.game.Player;
import domain.game.Position;
import domain.tiles.FreeTile;


public class persistencytests {
	
	/**
	 * Test 01: 
	 * Test the resolving of enums to strings.
	 */
	@Test
	public void persistency_reader_01() {
		XMLReader.LevelChoice lc = null;
		
		lc = XMLReader.LevelChoice.LEVEL1_default;
		assert XMLReader.resolveLevelFilename(lc).equals("levels/level1.xml");
		
		lc = XMLReader.LevelChoice.LEVEL1_save;
		assert XMLReader.resolveLevelFilename(lc).equals("levels/level1saved.xml");
		
		lc = XMLReader.LevelChoice.LEVEL2_default;
		assert XMLReader.resolveLevelFilename(lc).equals("levels/level2.xml");
		
		lc = XMLReader.LevelChoice.LEVEL2_save;
		assert XMLReader.resolveLevelFilename(lc).equals("levels/level2saved.xml");
		
		assert XMLReader.resolveLevelFilename(null).equals("levels/level1.xml");
	}
	/**
	 * Test 02:
	 * Test the resolving of Colours to Strings.
	 */
	@Test
	public void persistency_reader_02() {
		GameObject.Colour col = null;
		
		col = Colour.BLUE;
		assert XMLReader.resolveColourToString(col).equals("BLUE");
		
		col = Colour.GREEN;
		assert XMLReader.resolveColourToString(col).equals("GREEN");
		
		col = Colour.RED;
		assert XMLReader.resolveColourToString(col).equals("RED");
		
		assert XMLReader.resolveColourToString(null) == null;
	}
	
	/**
	 * Test 03:
	 * Test the resolving of Strings to Colours.
	 */
	@Test
	public void persistency_reader_03() {
		Colour col = null;
		
		col = XMLReader.resolveStringToColour(null);
		assert col == null;
		
		col = XMLReader.resolveStringToColour("");
		assert col == null;
		
		col = XMLReader.resolveStringToColour("Red");
		assert col == Colour.RED: "This should resolve to RED";
		
		col = XMLReader.resolveStringToColour("BluE");
		assert col == Colour.BLUE: "This should resolve to BLUE";
		
		col = XMLReader.resolveStringToColour("GrEEn");
		assert col == Colour.GREEN: "This should resolve to GREEN";
	}
	
	/**
	 * Test 04:
	 * Assert the existence of a Level, and Player.
	 */
	
	
	
	
	
	
}
