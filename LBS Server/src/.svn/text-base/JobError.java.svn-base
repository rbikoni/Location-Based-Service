/**
 * Repräsentiert einen Fehler in der JobQueue
 * 
 * @author rnb
 * 
 */
public class JobError extends Error {

	/**
	 * 
	 * @param message
	 *            Fehlernachricht
	 * @param tag
	 *            Objektspezfisches Error-Tag
	 */
	public JobError(final String message, final String tag) {
		super(message);
		this.tag = tag;
	}

	@Override
	protected void print() {
		System.err.print(this.tag);
		super.print();
	}

}
