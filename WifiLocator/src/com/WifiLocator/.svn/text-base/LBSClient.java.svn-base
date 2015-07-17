package com.WifiLocator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

/**
 * Der LBSClient baut eine Verbindung zum LBSServer auf und stellt Methoden zur
 * Kommunikation zu Verfügung.
 * 
 * @author rnb
 * 
 */
public class LBSClient {
	/**
	 * Addresse des LBSServer
	 */
	private String hostaddress = "";
	/**
	 * Portnummer des LBSServers
	 */
	private int port = 0;
	/**
	 * Referenz auf den Socket
	 */
	private Socket tcpSocket = null;
	/**
	 * Benutzername des Clients, IMEI Nummer
	 */
	private String username = "";
	/**
	 * Fehlernachricht
	 */
	private ClientError error = null;

	/**
	 * 
	 * @param hostaddress
	 *            Addresse des LBSServers
	 * @param port
	 *            Portnummer des LBSServers
	 * @param username
	 *            Benutzername des Clients
	 */
	public LBSClient(final String hostaddress, final int port,
			final String username) {
		this.hostaddress = hostaddress;
		this.port = port;
		this.username = username;
	}

	/**
	 * Baut eine Verbindung zum LBSServer auf
	 * 
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public void connect() {
		try {
			/* Konvertierung des Hostnamen/IP-Addresse */
			final InetAddress address = InetAddress.getByName(this.hostaddress);

			/* Erstellen,binden des Sockets */
			this.tcpSocket = new Socket(address, this.port);
		} catch (final UnknownHostException e) {
			this.error = new ClientError(e.getMessage(), "lbsclient>address>");
			this.error.print();
		} catch (final IOException e) {
			this.error = new ClientError(e.getMessage(),
					"lbsclient>connect>socket>instance>");
			this.error.print();
		} catch (final NullPointerException e) {
			this.error = new ClientError(e.getMessage(),
					"lbsclient>connect>socket>");
			this.error.print();
		}

	}

	/**
	 * Sendet eine benutzerdefinierte Nachricht an den Sever
	 * 
	 * @param message
	 *            benutzerdefinierte Nachricht
	 */
	public void send(final String message) {
		/* Fehlerüberprüfung */
		if (this.tcpSocket == null || !this.tcpSocket.isConnected()) {
			return;
		} else if (this.error != null) {
			return;
		}

		try {
			/* Max 8KB Nachricht */
			final PrintWriter out = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(this.tcpSocket.getOutputStream())),
					true);
			/* Senden der Nachricht */
			out.println(message);
		} catch (final IOException e) {
			this.error = new ClientError(e.getMessage(),
					"lbsclient>send>tcpSocket>");
			this.error.print();
			// this.close();
		}
	}

	/**
	 * @return empfangene Netzwerknachricht
	 */
	public String receive() {
		if (this.error != null) {
			return this.error.message;
		}

		String msg = "";
		try {
			final BufferedReader inFromServer = new BufferedReader(
					new InputStreamReader(this.tcpSocket.getInputStream()));
			msg = inFromServer.readLine();
		} catch (final IOException e) {
			e.printStackTrace();
		}

		return msg;
	}

	/**
	 * Sendet die aktuell gescannte Position and den Server
	 * 
	 * @param location
	 *            gescannte Position
	 */
	public void sendLocation(final PositionInfos location) {

		if (this.error != null) {
			return;
		}

		/* LOCATE [USERNAME] [[BSSID] [MEDIAN], ...] */
		String message = String.format("LOCATE %s", this.username);

		/* Extrahieren der BSSIDs und Mediane */
		for (int index = 0; index < location.getSize(); ++index) {
			final PositionInfo spot = location.getPosition(index);

			/* Zusammenbau der Nachricht */
			message += " " + spot.BSSID + " " + spot.getMedian();
		}
		Log.e("HOOOOST", this.hostaddress);
		/* Senden der Nachricht */
		try {

			final PrintWriter out = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(this.tcpSocket.getOutputStream())),
					true);
			out.println(message);

		} catch (final IOException e) {
			this.error = new ClientError(e.getMessage(),
					"lbsclient>sendLocation>tcpSocket>");
			this.error.print();
		}
	}

	/**
	 * Socket wird geschlossen.
	 */
	public void close() {
		if (this.tcpSocket == null) {
			return;
		}

		try {
			this.tcpSocket.close();
		} catch (final IOException e) {
			this.error = new ClientError(e.getMessage(),
					"lbsclient>close>tcpSocket>");
			this.error.print();
		}
	}
}
