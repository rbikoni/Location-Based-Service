package com.WifiScanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.os.Environment;
import android.util.Log;

/**
 * Die Importer-Klasse importiert die gespeicherten Informationen von der
 * XML-Datei positions.xml in die Datenbank.
 * 
 * @author rnb
 * 
 */
public class Importer {
	/**
	 * Referenz der Datenbank
	 */
	private final MyDatabase database;

	/**
	 * @param database
	 *            Datenbankobjekt in dem die Information gespeichert wird.
	 */
	public Importer(final MyDatabase database) {
		this.database = database;
		;
	}

	/**
	 * Versucht die XML Datei zu parsen. Die Klasse ist noch sehr unsicher und
	 * überprüft nicht das Schema. Die Methode geht von einem validen Schema aus
	 * wie es im Trac definiert ist. ImportXMLToDatabase call (n)
	 * PositionNodeThreads
	 * 
	 * @param xmlPath
	 *            Relativer Pfad der XML Datei von der SD-Karte.
	 * @return Fehler
	 */
	public boolean ImportXMLToDatabase(final String xmlPath) {
		final File root = Environment.getExternalStorageDirectory();
		final File xmlFile = new File(root, xmlPath);

		/*
		 * Die XML Datei wird in verschiedene Streams umgewandelt. Mithilfe der
		 * InputSource können XPath ausdrücke auf die Datei ausgeführt werden.
		 */
		InputStream input = null;
		try {
			input = new FileInputStream(xmlFile);
		} catch (final FileNotFoundException e) {
			return false;
		}

		final InputStreamReader isr = new InputStreamReader(input);
		final InputSource is = new InputSource(isr);

		// Alle <Position> Elemente werden in eine Variable gespeichert.
		final NodeList positionNodes = this.getNodeList("/Map/Position", is);
		final int len = positionNodes.getLength();

		// Elemente vorhanden ?
		if (len > 0) {
			/*
			 * Für jedes Position Element wird ein PositionNodeThread gestartet.
			 * Der Thread startet 1 InformationNodeThread für die Information n
			 * SpotNodeThreads für die Spotinformationen In den Threads werden
			 * bereits die SQL-Insert-Statements ausgeführt Für Threading
			 * Variante wurde sich entschieden, weil die XML sehr viele Elemente
			 * beinhalten kann.
			 */
			final PositionNodeThread[] positions = new PositionNodeThread[len];

			for (int index = 0; index < len; ++index) {
				// Kindelemente der n <Position>-Elemente
				final NodeList positionChildNodes = positionNodes.item(index)
						.getChildNodes();

				// Threads werden gestartet
				positions[index] = new PositionNodeThread(positionChildNodes);
				positions[index].start();
				// try {
				// pt.join();
				// } catch (final InterruptedException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
			}
			boolean waiting_for_threads = false;
			do {
				waiting_for_threads = false;
				for (int index = 0; index < len; ++index) {
					if (positions[index].isAlive()) {
						waiting_for_threads = true;
					}
				}
			} while (waiting_for_threads);
		}
		return false;
	}

	/**
	 * Führt eine XPath Ausdruck auf die angebene InputSource aus.
	 * 
	 * @param xPathExpression
	 *            XPath Ausdruck
	 * @param inputSource
	 *            EingabeQuelle
	 * @return Liste der gefunden Elemente des XPath Ausdrucks. Kann null sein!
	 */
	private NodeList getNodeList(final String xPathExpression,
			final InputSource inputSource) {
		final InputSource src = inputSource;
		final XPath xpath = XPathFactory.newInstance().newXPath();
		NodeList nodes = null;

		// XPath Ausdruck wird auf die InputSource ausgeührt
		try {
			nodes = (NodeList) xpath.evaluate(xPathExpression, src,
					XPathConstants.NODESET);
		} catch (final XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Rückgabe der gefunden Elemente
		return nodes;
	}

	/**
	 * NodeThread repräsentiert ein Thread für jedes Position Element
	 * PositionNodeThread[n] call (1) InformationNodeThread PositioNodeThread[n]
	 * call (1..n) SpotNodeThread
	 * 
	 * @author rnb
	 */
	private class PositionNodeThread extends Thread {
		/**
		 * Referenz auf die Kindelemente in <Position>
		 */
		private final NodeList childNodes;

		/**
		 * Instanz der Information Klasse
		 */
		private final Information information = new Information();

		/**
		 * Speichert die Referenz der Kindelemente
		 * 
		 * @param childNodes
		 */
		public PositionNodeThread(final NodeList childNodes) {
			this.childNodes = childNodes;
		}

		@Override
		public void run() {
			// Anzahl der Kinder
			final int len = this.childNodes.getLength();

			// SpotNodes deklaration
			SpotNodeThread[] spotThread;

			// Information der Position
			InformationNodeThread infoThread = null;

			// Durchlauf der einzelnen Kinder
			for (int child = 0; child < len; ++child) {
				switch (child) {
				// 1 InformationNode Thread wird gestartet
				case POSITIONELEMENT.INFORMATION: {
					final NodeList informationChildNodes = this.childNodes
							.item(child).getChildNodes();
					infoThread = new InformationNodeThread(
							informationChildNodes, this.information);
					infoThread.start();
					break;
				}

					// n SpotNodeThreads werden gestartet
				case POSITIONELEMENT.SPOTS: {

					final NodeList spotsChildNodes = this.childNodes
							.item(child).getChildNodes();
					final int len2 = spotsChildNodes.getLength();

					// SpotThreadArray wird initialisiert
					spotThread = new SpotNodeThread[len2];

					// Durchlauf der Kindelemente
					for (int child2 = 0; child2 < len2; ++child2) {
						// spotNode stellt ein Spot Element dar
						final Node spotNode = spotsChildNodes.item(child2);

						// initialisierung der Elemente
						spotThread[child2] = new SpotNodeThread(spotNode,
								this.information);

						try {
							// Warten auf Threadende
							infoThread.join();
						} catch (final InterruptedException e) {
							// TODO Auto-generated catch block
							Log.e("InfoThread>join", e.getMessage());
						}
						// SpotNodeThread wird gestartet
						spotThread[child2].start();
						try {
							spotThread[child2].join();
						} catch (final InterruptedException e) {
							Log.e("SpotThread>join", e.getMessage());
						}
					}
					break;
				}
				}
			}
		}
	}

	/**
	 * Durchläuft die Kindelemente von <Information>, füllt die Referenzvariable
	 * information und speichert die Informationen in die Datenbank.
	 * 
	 * @author rnb
	 * 
	 */
	private class InformationNodeThread extends Thread {
		/**
		 * Referenz der Kindelemente von <Information>
		 */
		private final NodeList childNodes;

		/**
		 * Referenz des Information Objekts
		 */
		private final Information information;

		/**
		 * @param childNodes
		 *            Referenz der Kindelemente von <Information>
		 * @param information
		 *            Referenz eines Information-Objektes
		 */
		public InformationNodeThread(final NodeList childNodes,
				final Information information) {
			this.childNodes = childNodes;
			this.information = information;
		}

		@Override
		public void run() {
			// Anzahl der Kindelemente
			final int len = this.childNodes.getLength();

			// Durchlaufen der Kindelemente
			for (int child = 0; child < len; ++child) {
				// Aktuelles Element
				final Node node = this.childNodes.item(child);

				/*
				 * Je nach Element wird der Element in das Information-Objekt
				 * gespeichert.
				 */
				switch (child) {
				case INFORMATIONELEMENT.POSITION_ID: {
					final int id = Integer.parseInt(node.getFirstChild()
							.getNodeValue());
					this.information.setId(id);
					break;
				}

				case INFORMATIONELEMENT.DESCRIPTION: {
					final String description = node.getFirstChild()
							.getNodeValue();
					this.information.setDescription(description);
					break;
				}

				case INFORMATIONELEMENT.X: {
					final double x = Double.parseDouble(node.getFirstChild()
							.getNodeValue());
					this.information.setX(x);
					break;
				}
				case INFORMATIONELEMENT.Y: {
					final double y = Double.parseDouble(node.getFirstChild()
							.getNodeValue());
					this.information.setY(y);
					break;
				}
				case INFORMATIONELEMENT.Z: {
					final double z = Double.parseDouble(node.getFirstChild()
							.getNodeValue());
					this.information.setZ(z);
					break;
				}
				case INFORMATIONELEMENT.ANGLE: {
					final int angle = Integer.parseInt(node.getFirstChild()
							.getNodeValue());
					this.information.setAngle(angle);
					break;
				}
				default:
				}
			}
			// Speicherung in die Datenbank
			Importer.this.database.insertPosition(this.information.getX(),
					this.information.getY(), this.information.getZ(),
					this.information.getAngle(),
					this.information.getDescription());
		}
	}

	/**
	 * Durchläuft die Kindelement von <Spot> und speichert die Werte in die
	 * Datenbank.
	 * 
	 * @author rnb
	 * 
	 */
	private class SpotNodeThread extends Thread {
		/**
		 * Referenz auf das Element <Spot>
		 */
		private final Node spotNode;

		/**
		 * Referenz auf die vorhergespeicherte Information
		 */
		private final Information information;

		/**
		 * @param spotNode
		 *            Referenz des <Spot> Elements
		 * @param information
		 *            Referenz des zugehörigen Information Objekts
		 */
		public SpotNodeThread(final Node spotNode, final Information information) {
			this.spotNode = spotNode;
			this.information = information;
		}

		@Override
		public void run() {
			// Liste der Kindlemente
			final NodeList spotChildNodes = this.spotNode.getChildNodes();
			final int len = spotChildNodes.getLength();
			double level = 0.0, median = 0.0;
			int channel = 0;
			String BSSID = "";

			// Kindelemente werden durchlaufen
			for (int child = 0; child < len; ++child) {
				switch (child) {
				case SPOTELEMENT.BSSID: {
					BSSID = spotChildNodes.item(child).getFirstChild()
							.getNodeValue();
					break;
				}
				case SPOTELEMENT.Channel: {
					channel = Integer.parseInt(spotChildNodes.item(child)
							.getFirstChild().getNodeValue());
					break;
				}
				case SPOTELEMENT.Level: {
					level = Double.parseDouble(spotChildNodes.item(child)
							.getFirstChild().getNodeValue());
					break;
				}
				case SPOTELEMENT.MEDIAN: {
					median = Double.parseDouble(spotChildNodes.item(child)
							.getFirstChild().getNodeValue());
					break;
				}
				default:
				}
			}

			// Speichern in die Datenbank
			Importer.this.database.insertSpot(BSSID, level, median, channel,
					this.information.getId());
		}
	}
}
