import java.net.Socket;

/**
 * Die Klasse stellt den Zusammenhang zwischen Netzwerknachricht und Socket
 * wiederher um im späteren Verlauf auf Anfragen des Client antworten zu können.
 * 
 * @author rnb
 * 
 */
public class Message {
	/**
	 * Referenz auf ein Client
	 */
	private Socket client = null;
	/**
	 * Netzwerknachricht des Clients
	 */
	private String message = "";

	/**
	 * 
	 * @param client
	 *            Client
	 * @param message
	 *            Netzwerknachricht
	 */
	public Message(final Socket client, final String message) {
		this.client = client;
		this.message = message;
	}

	/**
	 * 
	 * @return Clientreferenz
	 */
	public Socket getClient() {
		return this.client;
	}

	/**
	 * 
	 * @return Netzwerknachricht
	 */
	public String getMessage() {
		return this.message;
	}

}
