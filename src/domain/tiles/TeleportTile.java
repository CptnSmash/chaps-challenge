package domain.tiles;

import domain.actors.Actor;
import domain.game.Game;
import domain.game.GameObject;
import domain.game.Position;
import domain.gate.Gate;
import domain.item.Item;

public class TeleportTile extends Tile {
	
	private TeleportTile link;

	public TeleportTile(Position pos) {
		super(pos);
		// TODO Auto-generated constructor stub
	}

	public TeleportTile(Position pos, GameObject obj) throws IllegalArgumentException {
		super(pos, obj);
		// TODO Auto-generated constructor stub
	}

	public TeleportTile(int x, int y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}

	public TeleportTile(int x, int y, GameObject obj) throws IllegalArgumentException {
		super(x, y, obj);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canMoveOnto() {
		if(actor != null) {
			return false;
		}
		return true;
	}

	@Override
	public void onEntry() throws IllegalStateException {
		Game.getPlayer().teleportMove(link);
		assert Game.getPlayer().getTile().equals(link);
	}

	@Override
	public boolean setActor(Actor a) {
		actor = a;
		assert actor.equals(a);
		return true;
	}

	@Override
	public boolean setItem(Item i) {
		return false;
	}

	@Override
	public boolean setGate(Gate g) {
		return false;
	}

}
