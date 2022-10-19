package domain.actors;

/**
 * General interface to separate out actors the player cannot control.
 *
 * @author Michael Dasan 300130610
 */
public abstract interface NonPlayerActor {
	
	/**
	 * Called to move this actor.
	 */
	public abstract void move();

}
