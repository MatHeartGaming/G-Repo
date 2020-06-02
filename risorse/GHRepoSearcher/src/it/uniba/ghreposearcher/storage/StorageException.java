package it.uniba.ghreposearcher.storage;

public class StorageException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StorageException(String message) {
		super(message);
	}

	public StorageException(Exception exception) {
		super(exception);
	}

}
