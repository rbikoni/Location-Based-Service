/**
 * Repräsentiert einen Fehler im LbsServer
 * 
 * @author rnb
 * 
 */
public class ServerError extends Error {

	/**
	 * 
	 * @param message
	 *            Fehlernachricht
	 * @param tag
	 *            Objektspezfisches Error-Tag
	 */
	protected ServerError(final String message, final String tag) {
		super(message);
		this.tag = tag;
	}

	@Override
	protected void print() {
		System.err.print(this.tag);
		super.print();
	}

}
