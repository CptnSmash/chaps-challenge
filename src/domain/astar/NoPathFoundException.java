package domain.astar;

/**
 * NoPathFoundExceptions are thrown the performing an A* search and a path to the target is not found.
 *
 * @author Michael Dasan 300130610
 */
public class NoPathFoundException extends Exception {

	@SuppressWarnings("javadoc")
	private static final long serialVersionUID = -4894110407193877910L;

	/**
	 * Creates a new Exception with the given error message.
	 *
	 * @param string An error message explaining why the exception is being thrown.
	 */
	public NoPathFoundException(String string) {
		super(string);
	}

}
