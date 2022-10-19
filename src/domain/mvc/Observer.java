package domain.mvc;

import javax.swing.JFrame;

/**
 * Observers "monitor" Subjects for changes in their state, and react accordingly.
 * 
 * <p>
 * "Monitor" is in quotes because in actuality Subjects alert Observers to changes, and there is
 * no active monitoring by the Observer. Observers are alerted when the Subject calls the update()
 * method - this is where redrawing code should be placed.
 * </p>
 *
 * @author Michael Dasan 300130610
 */
public abstract class Observer extends JFrame{
	
	@SuppressWarnings("javadoc")
	private static final long serialVersionUID = 1L;
	
	/**
	 * The Subject this Observer is monitoring.
	 */
	protected Subject subject;
	
	/**
	 * Called by the Subject when its state has changed, alerting the Observer.
	 * <p>
	 * This method is where you should place code you want to run whenever the Subjects state changes.
	 * </p>
	 */
	public abstract void update();
	
	/**
	 * Called by the Subject when it needs to display text on screen, such as when a HelpTile is entered.
	 *
	 * @param message The message to be displayed on screen.
	 */
	public abstract void updateWithMessage(String message);
	
	/**
	 * Links this Observer and the given Subject.
	 *
	 * @param s The subject this Observer will monitor.
	 */
	protected void attachTo(Subject s) {
		this.subject = s;
		s.attach(this);
	}
	
}
