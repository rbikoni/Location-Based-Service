import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.SynchronousQueue;

/**
 * Ein ClientThread stellt eine eingehene Verbindung dar. Die Informationen die
 * der Client überträgt werden in einer MessageQueue gespeichert
 * 
 * @author rnb
 * 
 */
public class ClientThread extends Thread {
	/**
	 * Client Referenz
	 */
	private Socket clientSocket = null;
	/**
	 * Referenz auf die MessageQueue
	 */
	private SynchronousQueue<Message> messages = null;

	/**
	 * Buffer für den eingehenden Stream
	 */
	private BufferedReader buffer = null;

	/**
	 * 
	 * @param clientSocket
	 *            Client Socket
	 * @param messageQueue
	 *            Gemeinsame MessageQueue der Clients
	 */
	public ClientThread(final Socket clientSocket,
			final SynchronousQueue<Message> messageQueue) {
		this.clientSocket = clientSocket;
		this.messages = messageQueue;
	}

	@Override
	public void run() {
		if (this.clientSocket == null || this.messages == null) {
			return;
		}

		ClientError error = null;

		/* Empfang der Nachricht */
		try {
			/* Eingehender Stream wird gebuffert */
			this.buffer = new BufferedReader(new InputStreamReader(
					this.clientSocket.getInputStream()));
		} catch (final IOException e) {
			/*
			 * Eine Exception wird beim kompletten lesens des Streams IMMER
			 * geworfen, daher ist kein Fehlerverarbeitung möglich
			 */
			System.out.println("outch");
			error = new ClientError(e.getMessage(), "client>getInputStream>");
		}

		/* Fehlerüberprüfung */
		if (error == null) {

			/* Verwalten der Nachricht */
			try {
				/* Auslesen der kompletten Nachricht */
				final String message = this.buffer.readLine();

				/* +++++++++++ DEBUG AUSGABE ++++++++++++ */
				/* Echo der Nachricht */
				System.out.println(message);
				/*
				 * Nachricht wird der MessageQueue hinzugefügt, JobThread wartet
				 * bereits auf Arbeit
				 */
				this.messages.add(new Message(this.clientSocket, message));
			} catch (final IOException e) {
				error = new ClientError(e.getMessage(), "client>buffer>manage>");
				error.print();
			}
		}
	}
}
