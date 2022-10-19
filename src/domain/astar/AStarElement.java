package domain.astar;

import domain.tiles.Tile;

/**
 * AStarElements are used to create a path from one Position to another. They are used
 * when pathfinding from a BugEnemy to its target.
 *
 * @author Michael Dasan 300130610
 */
public class AStarElement implements Comparable<AStarElement> {
	
	public final Tile start;
	public final Tile prev;
	public final double g, f;
	
	/**
	 * Creates a new AStarElement with the given values.
	 *
	 * @param start
	 * @param prev
	 * @param g
	 * @param f
	 */
	public AStarElement(Tile start, Tile prev, double g, double f) {
		this.start = start;
		this.prev = prev;
		this.g = g;
		this.f = f;
	}

	@Override
	public int compareTo(AStarElement o) {
		if(this.f < o.f) {
			return -1;
		}else if(this.f > o.f) {
			return 1;
		}else {
			return 0;
		}
	}

}
