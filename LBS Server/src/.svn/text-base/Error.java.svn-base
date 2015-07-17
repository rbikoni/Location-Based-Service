/**
 * Abstrakte Klasse zur Darstellung eines Fehler in verschiedenen Objekten
 * 
 * @author rnb
 * 
 */
public abstract class Error {
	/**
	 * Fehlermeldung
	 */
	protected String message = "";
	/**
	 * Objektspezifisches Error-Tag
	 */
	protected String tag = "";

	/**
	 * 
	 * @param message
	 *            Fehlernachricht
	 */
	public Error(final String message) {
		this.message = message;
	}

	/**
	 * Ausgabe des Fehlers
	 */
	protected void print() {
		System.err.println(this.message);
	}
}
