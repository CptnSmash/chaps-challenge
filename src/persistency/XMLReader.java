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

import domain.actors.Actor;
import domain.actors.BugEnemy;
import domain.actors.NonPlayerActor;
import domain.game.Board;
import domain.game.GameObject.Colour;
import domain.game.Level;
import domain.game.Player;
import domain.game.Position;
import domain.gate.Gate;
import domain.gate.KeyGate;
import domain.gate.TreasureGate;
import domain.item.Item;
import domain.item.Key;
import domain.item.Treasure;
import domain.tiles.ExitTile;
import domain.tiles.FreeTile;
import domain.tiles.HelpTile;
import domain.tiles.Tile;
import domain.tiles.WallTile;

/**
 * XMLReader reads formatted XML data and Loads it as Game Data.
 * 
 * @author Sam JR Milburn [300509843]
 */
public class XMLReader {
  public enum LevelChoice {
    LEVEL1_default, LEVEL1_save, LEVEL2_default, LEVEL2_save
  }

  /**
   * Turn the LevelChoice enum in to a filename String. Returns level 1 as a
   * default.
   * 
   * @return A String representation of the enum.
   */
  public static String resolveLevelFilename(LevelChoice choice) {
    if (choice == null) {
      return resourcePath+"level1.xml";
    }
    switch (choice) {
    case LEVEL1_default:
      return resourcePath + "level1.xml";
    case LEVEL1_save:
      return resourcePath + "level1saved.xml";
    case LEVEL2_default:
      return resourcePath + "level2.xml";
    case LEVEL2_save:
      return resourcePath + "level2saved.xml";
    default:
      return resourcePath + "level1.xml";
    }
  }

  /* Resources */
  public static final String resourcePath = "levels/";
  private Level level 			= null;
  private Player player 		= null;
  private LevelChoice LevelC 	= null;

  /**
   * resolveColour derives a Colour enum from a String.
   * 
   * @param str Text to resolve.
   * @return The derived Colour.
   */
  public static Colour resolveStringToColour(String str) {
	if(str == null) { return null; }
    str = str.toLowerCase();
    switch (str) {
    case "red":
      return Colour.RED;
    case "green":
      return Colour.GREEN;
    case "blue":
      return Colour.BLUE;
    }
    return null;
  }

  /**
   * resolveStringFromColour derives a String from a Colour enum.
   * 
   * @param col A Colour enum.
   * @return The derived String.
   */
  public static String resolveColourToString(Colour col) {
    if (col == null) {
      return null;
    }
    switch (col) {
    case RED:
      return "RED";
    case GREEN:
      return "GREEN";
    case BLUE:
      return "BLUE";
    }
    return null;
  }

  /**
   * Generate a Game Level from the XML Document.
   */
  private void generateLevel(Document document) {
    Element root;
    try {
      root = document.getRootElement();
    } catch (IllegalStateException e) {
      throw new IllegalArgumentException("");
    }
    String tmp = "";
    Scanner sScan = null;
    Board board = null;

    /* Establish Header Data (Positions are formatted: Width, Height) */
    int WIDTH = -1;
    int HEIGHT = -1;
    int time = 60;
    Element header = root.getChild("Header");
    sScan = new Scanner(header.getChildText("Dimensions"));
    try {
      WIDTH = sScan.nextInt();
      HEIGHT = sScan.nextInt();
    } catch (NoSuchElementException e) {
      sScan.close();
      return;
    }
    sScan.close();
    board = new Board(WIDTH, HEIGHT);

    /* Default Board Tiles */
    for (int x = 0; x < WIDTH; x++) {
      for (int y = 0; y < HEIGHT; y++) {
        Position pos = new Position(x, y);
        board.put(pos, new FreeTile(pos));
      }
    }
    /* Populate Board */
    List<Element> tiles = root.getChild("Tiles").getChildren();
    for (Element elem : tiles) {
      WIDTH = -1;
      HEIGHT = -1;
      sScan = new Scanner(elem.getText());
      String name;
      try {
        name = sScan.next();
        WIDTH = sScan.nextInt();
        HEIGHT = sScan.nextInt();
      } catch (NoSuchElementException e) {
        continue;
      }
      /* Assign the Tiles to the Board */
      Position pos = new Position(WIDTH, HEIGHT);
      switch (name) {
      case "Wall":
        board.put(pos, new WallTile(pos));
        break;
      case "Help":
        //board.put(pos, new HelpTile(pos));
        break;
      case "Free":
        board.put(pos, new FreeTile(pos));
        break;
      case "Exit":
       // board.put(pos, new ExitTile(pos));
        break;
      }
    }
    /* Grab Player Data */
    sScan = new Scanner(header.getChildText("Player"));
    try {
      WIDTH = sScan.nextInt();
      HEIGHT = sScan.nextInt();
    } catch (NoSuchElementException e) {
      sScan.close();
      return;
    }

    if (WIDTH < board.getWidth() && HEIGHT < board.getHeight()) {
      Position pos = new Position(WIDTH, HEIGHT);
      Tile t = null;
      if (board.getTileAt(pos) == null) {
        t = new FreeTile(pos);
      } else {
        t = board.getTileAt(pos);
      }
      this.player = new Player(t);
    } else {
      throw new IllegalStateException("Error - Invalid Player Data!");
    }
    /* Grab Time data */
    tmp = header.getChildText("Time");
    if(tmp != null) {
      sScan = new Scanner(tmp);
      try {
        time = sScan.nextInt();
      } catch (NoSuchElementException e) {
        time = 60;
      }finally {
        sScan.close();
      }
    }
    List<Element> elems = null;
    String name;
    /* Add Actors */
    Element tmpE = root.getChild("Actors");
    if (tmpE != null) {
      elems = tmpE.getChildren();
    }
    /* Actors are for Level 2 only, and must null-guard. */
    if (elems != null) {
      for (Element elem : elems) {
        sScan = new Scanner(elem.getText());
        NonPlayerActor actor = null;
        try {
          name = sScan.next();
          WIDTH = sScan.nextInt();
          HEIGHT = sScan.nextInt();
        } catch (NoSuchElementException e) {
          continue;
        }

        Position pos = new Position(WIDTH, HEIGHT);
        switch (name) {
        case "BugEnemy":
          actor = new BugEnemy(board.getTileAt(pos));
          break;
        }
      }
    }

    /* Add Gates */
    elems = root.getChild("Gates").getChildren();
    for (Element elem : elems) {
      sScan = new Scanner(elem.getText());
      Gate gate = null;
      String keytype = "";
      try {
        name = sScan.next();
        WIDTH = sScan.nextInt();
        HEIGHT = sScan.nextInt();
        if (sScan.hasNext()) {
          keytype = sScan.next();
        }
      } catch (NoSuchElementException e) {
        continue;
      }

      Position pos = new Position(WIDTH, HEIGHT);
      Colour col = null;
      if (!keytype.isEmpty()) {
        col = resolveStringToColour(keytype);
      }
      /* Instantiate the New Gate */
      switch (name) {
      case "KeyGate":
        gate = new KeyGate(board.getTileAt(pos), col);
        break;
      case "TreasureGate":
        gate = new TreasureGate(board.getTileAt(pos), 0);
        break;
      }
      if (gate != null) {
        board.getTileAt(pos).setGate(gate);
      }
    }

    /* Add Items */
    elems = root.getChild("Items").getChildren();
    for (Element elem : elems) {
      sScan = new Scanner(elem.getText());
      Item item = null;
      String colstr = "";
      try {
        name = sScan.next();
        WIDTH = sScan.nextInt();
        HEIGHT = sScan.nextInt();
        if (sScan.hasNext()) {
          colstr = sScan.next();
        }
      } catch (NoSuchElementException e) {
        continue;
      }

      Colour col = null;
      if (!colstr.isEmpty()) {
        col = resolveStringToColour(colstr);
      }
      Position pos = new Position(WIDTH, HEIGHT);
      switch (name) {
      case "Key":
        item = new Key(board.getTileAt(pos), col);
        break;
      case "Treasure":
        item = new Treasure(board.getTileAt(pos));
        break;
      }
      if (item != null) {
        board.getTileAt(pos).setItem(item);
      }
    }
    /* Assign the Level */
    if (this.LevelC != null) {
      this.level = new Level((this.LevelC.ordinal() >= 2 ? 2 : 1), time);
    } else {
      this.level = new Level(1, time);
    }

    this.level.setBoard(board);
  }

  /**
   * Grab the Generated Level from the Save File.
   * 
   * @return The Game Level.
   */
  public Level getLevel() {
    return this.level;
  }

  /**
   * Grab the Player from the XML File.
   * 
   * @return The Player Object.
   */
  public Player getPlayer() {
    return this.player;
  }

  /**
   * Assigns the XML File to be Read.
   * 
   * @param filename The Filename of the level to be loaded.
   */
  public XMLReader(String filename) throws IllegalArgumentException {
    /* Filter the pathname */
    if (filename == null || filename.isEmpty()) {
      throw new IllegalArgumentException("Error - Filename must be specified.");
    }
    /* Validate filename */
    filename.trim();
    if (!filename.startsWith(resourcePath)) {
      filename = resourcePath + filename;
    }

    /* Load the file and check for validity */
    File file = new File(filename);
    if (!file.exists()) {
      throw new IllegalArgumentException("Error - File doesn't exist.");
    }
    file.setReadOnly();
    /* Build the Document */
    SAXBuilder xmlBuilder = new SAXBuilder();
    Document document = null;
    try {
      document = xmlBuilder.build(file);
    } catch (IOException ioe) {
      throw new IllegalArgumentException("Error - Input Exception on File building.");
    } catch (JDOMException je) {
      throw new IllegalArgumentException("Error - JDOMException from File input.");
    }
    /* On Success: Assign. */
    this.generateLevel(document);
  }

  /**
   * Assigns the XML File to be Read.
   * 
   * @param choice The choice of Level to Load.
   */
  public XMLReader(LevelChoice choice) throws IllegalArgumentException{
    /* Load the file and check for validity */
    File file = new File(resolveLevelFilename(choice));
    if (!file.exists()) {
      throw new IllegalArgumentException("Error - File doesn't exist.");
    }
    file.setReadOnly();
    /* Build the Document */
    SAXBuilder xmlBuilder = new SAXBuilder();
    Document document = null;
    try {
      document = xmlBuilder.build(file);
    } catch (IOException ioe) {
      throw new IllegalArgumentException("Error - Input Exception on File building.");
    } catch (JDOMException je) {
      throw new IllegalArgumentException("Error - JDOMException from File input.");
    }
    /* On Success: Assign. */
    this.LevelC = choice;
    this.generateLevel(document);
  }

}

