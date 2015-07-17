import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.SynchronousQueue;

import javax.swing.JFrame;

import com.almworks.sqlite4java.SQLiteException;

/**
 * Der JobThread parst die eingehenden Netzwerknachrichten und führt die
 * passenden Kommandos aus
 * 
 * @author rnb
 * 
 */
public class JobThread extends Thread {

	/**
	 * Referenz auf die MessageQueue
	 */
	private SynchronousQueue<Message> messages = null;

	/**
	 * Referenz auf ein Toplevel Container
	 */
	final JFrame frame = new JFrame();

	/**
	 * GUI der Referenzkarte
	 */
	private MapDesktop desktop = null;

	/**
	 * Referenz auf die verwendetete Datenbank
	 */
	private DatabaseAdapter db = null;

	/**
	 * Referenz auf das Controller Frame
	 */
	private ControllerFrame controller_frame = null;

	/**
	 * Referenz auf den Serversocket
	 */
	private ServerSocket tcpSocket = null;

	/**
	 * Exitflag
	 */
	private volatile boolean stop = false;

	/**
	 * 
	 * @param messages
	 *            Referenz auf die verwendete MessageQueue
	 * @param dbPath
	 *            Pfad der verwendeten Datenbank
	 * @param tcpSocket
	 *            ServerSocket
	 * @param imagePath
	 *            Pfad der Bilddatei
	 */
	public JobThread(final SynchronousQueue<Message> messages,
			final String dbPath, final String imagePath,
			final ServerSocket tcpSocket) {
		;
		this.tcpSocket = tcpSocket;
		this.messages = messages;
		this.db = new DatabaseAdapter(dbPath);
		this.desktop = new MapDesktop(imagePath);

		/* Controller Frame */
		this.controller_frame = new ControllerFrame(messages);
		this.desktop.add(this.controller_frame);

		/* GUI */
		this.frame.setSize(700, 300);
		this.frame.add(this.desktop);
		this.frame.setTitle("Location Based KomInf Server");
		this.frame.setVisible(true);
		this.desktop.setVisible(true);
	}

	@Override
	public void run() {
		if (this.messages == null) {
			return;
		}

		/* Öffnen der Datenbank */
		this.db.open();

		JobError error = null;

		/*
		 * Abarbeiten der MessageQueue sobald Nachrichten vorhanden sind, take
		 * blockiert, bis Nachrichten vorhanden sind.
		 */
		while (!this.stop) {
			Message message = null;

			/* Auslesen einer Nachricht */
			try {
				message = this.messages.take();
			} catch (final InterruptedException e) {
				error = new JobError(e.getMessage(), "job>message>take>");
				error.print();
			}

			/* Fehlerüberprüfung */
			if (error != null || message == null) {
				break;
			}
			/* Netzwerknachricht */
			final String networkMessage = message.getMessage();
			/*
			 * Ein Tokenizer wird verwendet um die Nachricht, Stück für Stück zu
			 * parsen
			 */
			final StringTokenizer tokenizer = new StringTokenizer(
					networkMessage);

			/*
			 * Ausführbare Netzwerkkommandos enthalten mehr als ein Element,
			 * mindestens 2, NACHRICHT:[KOMMANDO] [USERNAME] [...]
			 */
			if (tokenizer.countTokens() > 1) {
				final String token = tokenizer.nextToken();
				final PositionInfos currentPosition = new PositionInfos();

				/* KOMMANDO: REGISTER IMEI USERNAME */
				if (token.equals("REGISTER")) {
					final String IMEI = tokenizer.nextToken();
					final String username = tokenizer.nextToken();

					try {
						this.db.insertUsername(IMEI, username);
					} catch (final SQLiteException e) {
						error = new JobError(e.getMessage(), "job>register>");
						error.print();
					}
				}

				/* KOMMANDO: Locate */
				else if (token.contains("LOCATE")) {
					String username = null, response = "";

					final String IMEI = tokenizer.nextToken();
					final PositionInfos myLocation = this.locate(tokenizer,
							currentPosition);

					if (myLocation == null) {
						response = "No reference position found.";
					} else {

						response = myLocation.getPositionDescription();

						/* IMEI => Benutzername */
						try {
							username = this.db.getUsernameFromIMEI(IMEI);
						} catch (final SQLiteException e) {
							/* SQL Fehler */
						}
						if (username == null) {
							username = IMEI;
						}
						System.out.println(username + " in" + response);
						this.desktop.addUserToMap(username, myLocation);
					}

					/* Sendet die Referenzposition zurück an den Client */
					this.send(message, response);
				}
			}
			/*
			 * Wenn die Anzahl der Elemente 1 im Tokenizer beträgt, dann handelt
			 * es sich um lokale Kommandos
			 */
			else {

				final String token = tokenizer.nextToken();

				/* KOMMANDO: Terminate */
				if (token.equals("Terminate")) {
					this.terminate();
				}
			}
		}
	}

	/**
	 * Beendet den Thread und die GUI
	 */
	private void terminate() {
		try {
			this.stop = true;
			this.frame.dispose();
			this.tcpSocket.close();
		} catch (final IOException e) {
			/* ServerSocket Exception */
		}
	}

	/**
	 * Sendet einen Nachricht an den Client, wenn die Verbindung noch besteht
	 * 
	 * @param message
	 *            Aktuelles Nachrichtenelement in der Queue
	 * @param networkMessage
	 *            Netzwerknachricht die übermittelt werden soll
	 */
	private void send(final Message message, final String networkMessage) {
		try {
			final Socket client = message.getClient();
			if (client.isConnected() && !client.isClosed()) {
				final PrintWriter out = new PrintWriter(
						client.getOutputStream(), true);
				out.println(networkMessage);
			}
		} catch (final IOException e) {
			final JobError error = new JobError(e.getMessage(), "job>run>send>");
			error.print();
		}
	}

	/**
	 * NACHRICHT:LOCATE [USERNAME] [[BSSID] [MEDIAN], [BSSID] [MEDIAN], ...]
	 * Locate Kommando
	 * 
	 * @param tokenizer
	 *            verwendeter Tokenizer
	 * @param currentPosition
	 *            Referenz auf die akutell gescannte Position
	 * @return gefundene Referenzposition
	 */
	private PositionInfos locate(final StringTokenizer tokenizer,
			final PositionInfos currentPosition) {

		while (tokenizer.hasMoreTokens()) {
			/* BSSID */
			final String BSSID = tokenizer.nextToken();
			/* Median */
			final Double median = Double.parseDouble(tokenizer.nextToken());
			/*
			 * Erstellen einer PositionInfo um die zugehörigen Metdadaten zu
			 * speichern
			 */
			final PositionInfo spot = new PositionInfo(BSSID, "", 0.0f,
					median.floatValue(), 0, 0.0);
			currentPosition.AddPositionInfo(spot);
		}

		/* ---- */
		final ArrayList<PositionInfos> referencePositions = new ArrayList<PositionInfos>();
		try {
			this.db.selectSpotsFromBSSID(currentPosition.getBSSIDs(),
					referencePositions);
		} catch (final SQLiteException e) {
			System.err.println(e.getMessage());
		}

		if (referencePositions.size() == 0) {
			return null;
		}

		final Locator locator = new Locator();
		return locator.getLocation(currentPosition, referencePositions);
	}
}
