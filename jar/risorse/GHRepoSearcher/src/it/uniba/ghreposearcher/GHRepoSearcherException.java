package it.uniba.ghreposearcher;

public class GHRepoSearcherException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GHRepoSearcherException(String message) {
		super(message);
	}

	public GHRepoSearcherException(Exception exception) {
		super(exception);
	}

}
