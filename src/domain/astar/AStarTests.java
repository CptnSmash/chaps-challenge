package domain.astar;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayDeque;

import org.junit.jupiter.api.Test;

import domain.game.Board;
import domain.game.Game;
import domain.game.Level;
import domain.game.Player;
import domain.game.Position;
import domain.tiles.FreeTile;
import domain.tiles.Tile;

/**
 * @author Michael Dasan 300130610
 */
class AStarTests {

	@Test
	void test() {
		Board board = new Board(3, 1);
		board.put(new Position(0, 0), new FreeTile(new Position(0, 0)));
		board.put(new Position(1, 0), new FreeTile(new Position(1, 0)));
		board.put(new Position(2, 0), new FreeTile(new Position(2, 0)));
		Level level = new Level(0, 0, board);
		Player player = new Player(board.getTileAt(0, 0));
		Game game = new Game(level, player);
		ArrayDeque<Tile> path = null;
		try {
			path = AStarSearch.pathfind(new Position(0, 0), new Position(2, 0), true);
		} catch (NoPathFoundException e) {
			fail();
		}
		if(path == null) {
			fail();
		}
		assertTrue(path.peek() != null);
	}

}
