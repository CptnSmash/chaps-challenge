package domain.mvc;

import java.util.HashSet;

/**
 * Subjects are monitored by Observers -  when a change in a Subjects state occurs, they notify their Observers, enabling the Observer
 * to re-run any code that is dependent of the Subjects state.
 *
 * @author Michael Dasan 300130610
 */
public class Subject {
	
	/**
	 * Contains each Observer that is monitoring this Subject for changes. Stored as a HashSet to prevent duplicate Observers.
	 */
	protected static HashSet<Observer> observers = new HashSet<Observer>();
	
	/**
	 * Adds the given Observer to the Set of Observers monitoring this Subject.
	 *
	 * @param obs The monitoring observer.
	 */
	public void attach(Observer obs) {
		observers.add(obs);
	}
	
	/**
	 * Called when a change in this Subjects state has occurred, notifying all Observers monitoring this Subject.
	 */
	public void notifyAllObservers() {
		for(Observer obs : observers) {
			obs.update();
		}
	}
	
	/**
	 * Passes a message to be displayed to all observers.
	 *
	 * @param message The message to be displayed.
	 */
	public static  void notifyAllWithMessage(String message) {
		for(Observer obs : observers) {
			obs.updateWithMessage(message);
		}
	}

}
