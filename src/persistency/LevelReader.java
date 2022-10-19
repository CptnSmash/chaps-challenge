package persistency;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import domain.actors.BugEnemy;
import domain.game.Board;
import domain.game.Game;
import domain.game.GameObject.Colour;
import domain.game.Level;
import domain.game.Player;
import domain.gate.KeyGate;
import domain.gate.TreasureGate;
import domain.item.Key;
import domain.item.Treasure;
import domain.tiles.ExitTile;
import domain.tiles.FreeTile;
import domain.tiles.HelpTile;
import domain.tiles.Tile;
import domain.tiles.WallTile;

/**
 * @author Michael Dasan
 */
public class LevelReader {
	
	private final String resourcePath = "levels/";
	private Element root;
	private Level level;
	private Game game;
	
	/**
	 * @param filename
	 * @throws IllegalArgumentException
	 * @author 
	 * 		Sam JR Milburn, Michael Dasan
	 */
	public LevelReader(String filename) throws IllegalArgumentException {
		if (filename == null || filename.isEmpty()) {
			throw new IllegalArgumentException("Error - Filename must be specified.");
		}
		filename = filename.trim();
		if (!filename.startsWith(resourcePath)) {
			filename = resourcePath + filename;
		}
		File file = new File(filename);
	    assert file.exists();
		file.setReadOnly();
	    SAXBuilder xmlBuilder = new SAXBuilder();
	    Document doc = null;
	    try {
	      doc = xmlBuilder.build(file);
	    } catch (IOException ioe) {
	      throw new IllegalArgumentException("Error - Input Exception on File building.");
	    } catch (JDOMException je) {
	      throw new IllegalArgumentException("Error - JDOMException from File input.");
	    }
	    root = doc.getRootElement();
	    game = createGame();
	}
	
	private Board createBoard() {
		Element head = root.getChild("Header");
		Board board = null;
		Scanner scan = new Scanner(head.getChildText("Dimensions"));
		try {
			board = new Board(scan.nextInt(), scan.nextInt());
		}catch (NoSuchElementException e){
			throw new IllegalArgumentException("Error when constructing board - XML file improperly formatted.");
		}finally {
			scan.close();
		}
		scan.close();
		assert board != null;
		
		//populate board with tiles
		populateBoard(board);
		Element tiles = root.getChild("Tiles");
		createExitTiles(board, tiles);
		createHelpTiles(board, tiles);
		createWallTiles(board, tiles);
		
		//populate board with items
		Element items = root.getChild("Items");
		createKeys(board, items);
		createTreasures(board, items);
		
		//populate board with gates
		Element gates = root.getChild("Gates");
		createKeyGates(board, gates);
		createTreasureGates(board, gates);
		
		//populate board with enemies
		Element npa = root.getChild("NonPlayerActors");
		createBugEnemies(board, npa);
		
		return board;
	}
	
	/**
	 * Performs initial population of the Board by filling it with empty FreeTiles.
	 *
	 * @param b
	 * 		The Board to be populated.
	 */
	private void populateBoard(Board b) {
		for(int x = 0; x < b.getWidth(); x++) {
			for(int y = 0; y < b.getHeight(); y++) {
				b.put(new FreeTile(x, y));
			}
		}
	}
	
	/**
	 * 
	 *
	 * @param b
	 * @param t
	 */
	private void createExitTiles(Board b, Element t) {
		List<Element> exits = t.getChild("Exits").getChildren();
		Scanner scan = null;
		for(Element elem : exits) {
			scan = new Scanner(elem.getText());
			try {
				b.put(new ExitTile(scan.nextInt(), scan.nextInt(), scan.nextInt()));
			}catch(NoSuchElementException e) {
				throw new IllegalArgumentException("Error when constructing ExitTile - XML file improperly formatted.");
			}finally {
				scan.close();
			}
		}
		if(scan != null) {
			scan.close();
		}
	}
	
	private void createHelpTiles(Board b, Element t) {
		List<Element> helps = t.getChild("Helps").getChildren();
		Scanner scan = null;
		for(Element elem : helps) {
			scan = new Scanner(elem.getText());
			try {
				int x = scan.nextInt();
				int y = scan.nextInt();
				StringBuilder s = new StringBuilder();
				while(scan.hasNext()) {
					s.append(scan.next());
					if(scan.hasNext()) {
						s.append(" ");
					}
				}
				b.put(new HelpTile(x, y, s.toString()));
			}catch(NoSuchElementException e) {
				throw new IllegalArgumentException("Error when constructing HelpTile - XML file improperly formatted.");
			}finally {
				scan.close();
			}
		}
		if(scan != null) {
			scan.close();
		}
	}
	
	private void createWallTiles(Board b, Element t) {
		List<Element> walls = t.getChild("Walls").getChildren();
		Scanner scan = null;
		for(Element elem : walls) {
			scan = new Scanner(elem.getText());
			try {
				b.put(new WallTile(scan.nextInt(), scan.nextInt()));
			}catch(NoSuchElementException e) {
				throw new IllegalArgumentException("Error when constructing WallTile - XML file improperly formatted.");
			}finally {
				scan.close();
			}
		}
		if(scan != null) {
			scan.close();
		}
	}
	
	private void createKeys(Board b, Element i) {
		List<Element> keys = i.getChild("Keys").getChildren();
		Scanner scan = null;
		for(Element elem : keys) {
			scan = new Scanner(elem.getText());
			try {
				Tile t = b.getTileAt(scan.nextInt(), scan.nextInt());
				if(!t.setItem(new Key(Colour.valueOf(scan.next())))) {
					throw new IllegalArgumentException("Error when constructing Key - Attempting to assign to invalid Tile.");
				}
			}catch(NoSuchElementException e) {
				throw new IllegalArgumentException("Error when constructing Key - XML file improperly formatted.");
			}finally {
				scan.close();
			}
		}
		if(scan != null) {
			scan.close();
		}
	}
	
	private void createTreasures(Board b, Element i) {
		List<Element> treasures = i.getChild("Treasures").getChildren();
		Scanner scan = null;
		for(Element elem : treasures) {
			scan = new Scanner(elem.getText());
			try {
				Tile t = b.getTileAt(scan.nextInt(), scan.nextInt());
				if(!t.setItem(new Treasure())) {
					throw new IllegalArgumentException("Error when constructing Treasure - Attempting to assign to invalid Tile.");
				}
			}catch(NoSuchElementException e) {
				throw new IllegalArgumentException("Error when constructing Treasure - XML file improperly formatted.");
			}finally {
				scan.close();
			}
		}
		if(scan != null) {
			scan.close();
		}
	}
	
	private void createKeyGates(Board b, Element g) {
		List<Element> keygates = g.getChild("KeyGates").getChildren();
		Scanner scan = null;
		for(Element elem : keygates) {
			scan = new Scanner(elem.getText());
			try {
				Tile t = b.getTileAt(scan.nextInt(), scan.nextInt());
				if(!t.setGate(new KeyGate(Colour.valueOf(scan.next())))) {
					throw new IllegalArgumentException("Error when constructing KeyGate - Attempting to assign to invalid Tile.");
				}
			}catch(NoSuchElementException e) {
				throw new IllegalArgumentException("Error when constructing KeyGate - XML file improperly formatted.");
			}finally {
				scan.close();
			}
		}
		if(scan != null) {
			scan.close();
		}
	}
	
	private void createTreasureGates(Board b, Element g) {
		List<Element> treasuregates = g.getChild("TreasureGates").getChildren();
		Scanner scan = null;
		for(Element elem : treasuregates) {
			scan = new Scanner(elem.getText());
			try {
				Tile t = b.getTileAt(scan.nextInt(), scan.nextInt());
				if(!t.setGate(new TreasureGate(scan.nextInt()))) {
					throw new IllegalArgumentException("Error when constructing TreasureGate - Attempting to assign to invalid Tile.");
				}
			}catch(NoSuchElementException e) {
				throw new IllegalArgumentException("Error when constructing TreasureGate - XML file improperly formatted.");
			}finally {
				scan.close();
			}
		}
		if(scan != null) {
			scan.close();
		}
	}
	
	private void createBugEnemies(Board b, Element npa) {
		List<Element> bugEnemies = npa.getChild("BugEnemies").getChildren();
		Scanner scan = null;
		for(Element elem : bugEnemies) {
			scan = new Scanner(elem.getText());
			try {
				Tile t = b.getTileAt(scan.nextInt(), scan.nextInt());
				if(!t.setActor(new BugEnemy(t))) {
					throw new IllegalArgumentException("Error when constructing BugEnemy - Attempting to assign to invalid Tile.");
				}
			}catch(NoSuchElementException e) {
				throw new IllegalArgumentException("Error when constructing BugEnemy - XML file improperly formatted.");
			}finally {
				scan.close();
			}
		}
		if(scan != null) {
			scan.close();
		}
	}
	
	private Level createLevel() {
		Element header = root.getChild("Header");
		int time;
		int stage;
		try {
			time = Integer.parseInt(header.getChildText("Time"));
			stage = Integer.parseInt(header.getChildText("Stage"));
		}catch (NumberFormatException e) {
			throw new IllegalArgumentException("Error when constructing Level - Time element and/or Stage element is invalid.");
		}catch (NoSuchElementException e) {
			throw new IllegalArgumentException("Error when constructing Level - XML file improperly formatted.");
		}
		return new Level(stage, time, createBoard());
	}
	
	private Player createPlayer() {
		Element header = root.getChild("Header");
		Tile t = null;
		try {
			Scanner scan = new Scanner(header.getChildText("Player"));
			t = level.getTileAt(scan.nextInt(), scan.nextInt());
			scan.close();
		}catch(NoSuchElementException e) {
			throw new IllegalArgumentException("Error when constructing Player - XML file improperly formatted.");
		}
		return new Player(t);
	}
	
	private Game createGame() {
		level = createLevel();
		return new Game(level, createPlayer());
	}
	
	public Game getGame() {
		return game;
	}

}
