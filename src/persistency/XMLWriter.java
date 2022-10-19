package persistency;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import domain.actors.Actor;
import domain.actors.BugEnemy;
import domain.actors.NonPlayerActor;
import domain.game.Board;
import domain.game.Game;
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
import domain.tiles.HelpTile;
import domain.tiles.Tile;
import domain.tiles.WallTile;
import persistency.XMLReader.LevelChoice;

/**
 * XMLWriter reads Game Data and saves it to a formatted XML file.
 * @author Sam JR Milburn [300509843]
 */
public class XMLWriter {
	
	/**
	 * Given a game object, XMLWriter will serialize it in to XML format.
	 * @param game
	 * @param choice
	 */
	public XMLWriter(Game game, XMLReader.LevelChoice choice) throws IllegalArgumentException{
		if(game == null || choice == null) {
			throw new IllegalArgumentException("Error - Null Arguments!");
		}
		Document document = null; String filename = null;
		XMLOutputter writer = new XMLOutputter(); writer.setFormat(Format.getPrettyFormat());
		switch(choice) {
		case LEVEL1_save: filename = XMLReader.resolveLevelFilename(choice); break;
		case LEVEL2_save: filename = XMLReader.resolveLevelFilename(choice); break;
		/* It shouldn't be possible to save to the default files. */
		case LEVEL1_default: 
			filename = XMLReader.resolveLevelFilename(XMLReader.LevelChoice.LEVEL1_save); break;
		case LEVEL2_default: 
			filename = XMLReader.resolveLevelFilename(XMLReader.LevelChoice.LEVEL2_save); break;
		}
		if(filename == null) { throw new IllegalArgumentException("Error - Invalid Level Choice."); }
		
		/* Serialize game data. */
		Element rootE 	= new Element("Board");
		document 		= new Document(rootE);
		Level level 	= game.getLevel();
		Board board		= level.getBoard();
		Player player 	= game.getPlayer();
		if(level == null || player == null) { throw new IllegalArgumentException("Error - "); }
		int Width = board.getWidth(); int Height = board.getHeight();
		
		/* Header Component (Positions are formatted: Width, Height) */
		Element headerE 	= new Element("Header");
		Element dimensionsE = new Element("Dimensions");
		dimensionsE.setText(level.getBoard().getWidth()+" "+level.getBoard().getHeight());
		Element playerE 	= new Element("Player");
		Element timeE		= new Element("Time");
		playerE.setText(player.getPosition().getX()+" "+player.getPosition().getY());
		headerE.addContent(dimensionsE);
		headerE.addContent(playerE);
		timeE.setText(String.valueOf(level.getTime()));
		document.getRootElement().addContent(headerE);
		
		/* Tiles, Items, NPCs and Gates Component (Positions are formatted: Width, Height) */
		Element tiles 	= new Element("Tiles");
		Element items 	= new Element("Items");
		Element gates 	= new Element("Gates");
		Element actors 	= new Element("Actors"); 
		for(int x = 0; x < Width; x++) {
			for(int y = 0; y < Height; y++) {
				Position pos 	= new Position(x,y);
				Tile tile 		= board.getTileAt(pos);
				if(tile == null) { continue; }
				String text 	= null;
				
				
				/* ---Tiles--- */
				if(			tile instanceof WallTile) {
					text = "Wall "+pos.getX()+" "+pos.getY();
				}else if(	tile instanceof ExitTile) {
					text = "Exit "+pos.getX()+" "+pos.getY();
				}else if(	tile instanceof HelpTile) {
					text = "Help "+pos.getX()+" "+pos.getY();
				}
				Element tmp = null;
				tmp 		= new Element("Tile");
				if(text != null) { tmp.setText(text); tiles.addContent(tmp); }
				
				
				/* ---Items--- */
				Item item 	= tile.getItem();
				text 		= null;
				if(			item instanceof Key) {
					text = "Key "+pos.getX()+" "+pos.getY()+" "+ 
								XMLReader.resolveColourToString( ((Key)item).getColour() );
				}else if(	item instanceof Treasure) {
					text = "Key "+pos.getX()+" "+pos.getY();
				}
				tmp			= new Element("Item");
				if(text != null) { tmp.setText(text); items.addContent(tmp); }
				
				
				/* ---Gates--- */
				Gate gate 	= tile.getGate();
				text		= null;
				if(			gate instanceof KeyGate) {
					text = "KeyGate "+pos.getX()+" "+pos.getY()+" "+
								XMLReader.resolveColourToString( ((KeyGate)gate).getColour() );
				}else if (	gate instanceof TreasureGate) {
					text = "TreasureGate "+pos.getX()+" "+pos.getY();
				}
				tmp			= new Element("Gate");
				if(text != null) { tmp.setText(text); gates.addContent(tmp); }
				
				
				/* ---NPCs--- */
				Actor actor = tile.getActor();
				text		= null;
				if(			actor instanceof BugEnemy) {
					text = "BugEnemy "+pos.getX()+" "+pos.getY();
				}
				tmp			= new Element("Actor");
				if(text != null) { tmp.setText(text); actors.addContent(tmp); }
			}
		}
		/* Write to file*/
		document.getRootElement().addContent(tiles);
		document.getRootElement().addContent(items);
		document.getRootElement().addContent(gates);
		document.getRootElement().addContent(actors);
		try {
			writer.output(document, new FileOutputStream(new File(filename)));
			System.out.println(filename);
		}catch(FileNotFoundException fe) {
			throw new IllegalArgumentException("Error - File doesn't exist.");
		}catch(IOException e) {
			throw new IllegalArgumentException("Error - Failure to write to file.");
		}
	}
	
}
