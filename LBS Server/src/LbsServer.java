import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.concurrent.SynchronousQueue;

/**
 * Die Klasse stellt die eine Verbindung via TCP zwischen dem Android-Client und
 * dem Server her und verwaltet die laufende Sitzung.
 * 
 * @author rnb
 * 
 */
public class LbsServer {
	/**
	 * Default Port, falls ein ungültiger Port belegt ist
	 */
	private final int DEFAULT_PORT = 1234;
	/**
	 * Range der Root Ports auf die der Server nicht gebunden werden darf <100
	 */
	private final int ROOT_PORTS = 100;
	/**
	 * Port des Servers
	 */
	private int port = -1;
	/**
	 * Server Fehlermeldungen
	 */
	private ServerError error = null;
	/**
	 * Referenz eines Server Socket, der das TCP Protokoll unterstüzt
	 */
	private ServerSocket tcpSocket = null;
	/**
	 * MessageQueue, enthält die empfangne Netzwerknachrichten der Clients
	 */
	private final SynchronousQueue<Message> messages = new SynchronousQueue<Message>();
	/**
	 * Verwaltet die ClientThreads, um ggf alle höflich zu beenden
	 */
	private final ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	/**
	 * Ein Thread der die MessageQueue parst
	 */
	private JobThread jt = null;

	/**
	 * 
	 * @param port
	 *            Nummer des Ports auf den der Server gebunden werden soll. Der
	 *            Wert des Paramater muss >100 sein ansonsten wird der
	 *            <b>Default Port:1234</b> verwendet.
	 * @param imagePath
	 *            Pfad der Bilddatei
	 * @param databasePath
	 *            Pfad der Datenbank
	 */
	public LbsServer(final int port, final String databasePath,
			final String imagePath) {
		/*
		 * Android Handys können nur mit root Rechten einen Socket auf der
		 * Portnummber < 100 erstellen
		 */
		if (port < this.ROOT_PORTS) {
			this.port = this.DEFAULT_PORT;
		} else {
			this.port = port;
		}

		/* Der Socket wird erstellt und auf einem Port gebunden */
		try {
			this.tcpSocket = new ServerSocket(this.port);
		} catch (final IOException e) {
			this.error = new ServerError(e.getMessage(), "server>instance>");
			this.error.print();
		}

		this.jt = new JobThread(this.messages, databasePath, imagePath,
				this.tcpSocket);
	}

	/**
	 * Startet den Location Based Server und ein JobThread, der JobThread
	 * arbeitet die MessageQueue ab.
	 */
	public void start() {
		/*
		 * Der Server wird gestartet, wenn kein Fehler bei der initializierung
		 * aufgetreten ist
		 */
		if (this.error != null) {
			return;
		}

		/* JobThread wird gestartet */
		this.jt.start();

		try {
			while (true) {
				System.out.println("Waiting for clients");

				/*
				 * Warten auf Clients. Bei einer eingehenden Verbindung wird ein
				 * Thread für den Client intanziiert.
				 */
				this.clients.add(new ClientThread(this.tcpSocket.accept(),
						this.messages));
				System.out.println("Client accepted.");

				/* ClienThread wird gestartet */
				this.clients.get(this.clients.size() - 1).start();
			}
		} catch (final IOException e) {
			this.error = new ServerError(e.getMessage(), "server>start>accept>");
			this.error.print();
		} finally {
			try {
				this.tcpSocket.close();
			} catch (final IOException e) {
				this.error = new ServerError(e.getMessage(),
						"server>start>close>");
				this.error.print();
			}
		}

		this.closeThreads();

	}

	/**
	 * Alle Threads werden durch unterbrochen und damit geschlossen
	 */
	private void closeThreads() {
		if (this.clients != null) {
			/* ClientThreads werden unterbrochen */
			for (final ClientThread client : this.clients) {
				if (client.isAlive() && !client.isInterrupted()) {
					client.interrupt();
				}
			}
		}

		/* JobThread hat zu dem Zeitpunkt seine Aufgabe ausgefürt */
		this.jt = null;

	}
}
