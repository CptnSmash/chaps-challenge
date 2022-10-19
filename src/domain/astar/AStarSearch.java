package domain.astar;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.PriorityQueue;

import domain.game.Game;
import domain.game.Position;
import domain.tiles.Tile;

/**
 * The AStarSeach class is used to perform A* searches across the Board. Methods in this
 * class are used by NonPlayerActors to help them move between Tiles.
 * 
 * <p>
 * This class is declared abstract to prevent creation of instances. All methods are static
 * and should be accessed that way.
 * </p>
 *
 * @author Michael Dasan 300130610
 */
public abstract class AStarSearch {
	
	/**
	 * Finds the distance between two Positions. Used for pathfinding and determining if Chap is 
	 * within vision range of a NonPlayerActor.
	 * 
	 * @param start 
	 * @param end
	 * @return The distance between start and end.
	 */
	public static double getDistBetween(Position start, Position end) {
		int x = end.getX() - start.getX();
		int y = end.getY() - start.getY();
		return Math.abs(Math.hypot(x, y));
	}
	
	/**
	 * Finds the distance between two Tiles. See getDistBetween(Position start, Position end) for
	 * more details.
	 *
	 * @param start
	 * @param end
	 * @return The distance between start and end.
	 */
	public static double getDistBetween(Tile start, Tile end) {
		return getDistBetween(start.getPosition(), end.getPosition());
	}
	
	/**
	 * Attempts to find a path of Tiles between start and end. If direct is true, this will ignore
	 * blockages such as Walls and Gates. If direct is false, this will path around them.
	 *
	 * @param start
	 * @param end
	 * @param direct True if this search is to ignore Walls and Gates
	 * @return A stack of tiles representing a path from start to end
	 * @throws NoPathFoundException
	 */
	public static ArrayDeque<Tile> pathfind(Position start, Position end, boolean direct) throws NoPathFoundException {
		PriorityQueue<AStarElement> fringe = new PriorityQueue<AStarElement>();
		fringe.offer(new AStarElement(Game.getLevel().getTileAt(start), null, 0d, getDistBetween(start, end)));
		HashSet<Tile> visited = new HashSet<Tile>();
		while(fringe.peek() != null) {
			AStarElement currFringe = fringe.poll();
			Tile currTile = currFringe.start;
			if(!visited.contains(currTile)) {
				//System.out.println("Visiting tile...");
				visited.add(currTile);
				//System.out.println("Setting prev...");
				currTile.setPrev(currFringe.prev);
				if(currTile.equals(Game.getLevel().getTileAt(end))) {
					//System.out.println("Path found, calculating...");
					return calculatePath(currTile);
				}
				for(Tile t : Game.getLevel().getBoard().getAdjacentTiles(currTile.getPosition())) {
					if(!direct && t.blocksVision()) {
						break;
					}
					if(!visited.contains(t)) {
						//System.out.println("Tile unvisited, adding to fringe...");
						double g = currFringe.g + 1; //Constant +1 as adjacent tiles are 1 distance away
						double f = g + getDistBetween(t, Game.getLevel().getTileAt(end));
						fringe.offer(new AStarElement(t, currTile, g, f));
					}
				}
			}
		}
		throw new NoPathFoundException("No path found.");
	}
	
	/**
	 * Traverses the prev field of Tiles to build a stack leading to goal.
	 *
	 * @param goal The end destination.
	 * @return A stack of tiles leading from the location of whatever object initiated this A* search,
	 * 			to goal.
	 */
	private static ArrayDeque<Tile> calculatePath(Tile goal) {
		ArrayDeque<Tile> path = new ArrayDeque<Tile>();
		//System.out.println("Adding initial tile to path...");
		path.push(goal);
		while(path.peek().getPrev() != null) {
			//System.out.println("Adding tile to path...");
			path.push(path.peek().getPrev());
		}
		//System.out.println("Returning path...");
		return path;
	}

}
