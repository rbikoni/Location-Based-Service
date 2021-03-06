package com.WifiScanner;

import java.io.StringWriter;
import java.util.ArrayList;

import org.xmlpull.v1.XmlSerializer;

import android.net.wifi.ScanResult;
import android.util.Log;
import android.util.Xml;

/**
 * In der Datenbank: Spot<br/>
 * PositionInfo enth�lt Informationen zum Identifizieren eines AP's und bietet
 * dar�berhinaus noch Methoden zu der Berechnung des Mittelwertes und des
 * Medians
 * 
 * @author rnb
 * 
 */
public class PositionInfo {
	/**
	 * AP SSID
	 */
	private final String SSID;
	/**
	 * AP BSSID
	 */
	private final String BSSID;
	/**
	 * Signalst�rke
	 */
	private float level;
	/**
	 * Kanal
	 */
	private final int channel;
	/**
	 * Signalst�rke in Prozent
	 */
	private double percent = 0.0;
	/**
	 * ----
	 */
	private ArrayList<PositionInfo> responses;
	/**
	 * Da in einem Intervall �ber einen l�ngeren Zeitraum gemessen wird ist es
	 * wichtig zu wissen, wie oft der AP gesendet hat,.
	 */
	private int count = 1;
	/**
	 * Median
	 */
	private Median median;

	/**
	 * 
	 * @param BSSID
	 *            BSSID des AP's
	 * @param SSID
	 *            Namen des Funknetzes
	 * @param level
	 *            Signalst�rke des Funknetzes
	 * @param channel
	 *            Kanal des Funknetzes
	 * @param percent
	 *            Prozentangabe der Signalst�rke, kann �ber
	 *            {@link #calculateSignal(double)} berechnet werden
	 */
	public PositionInfo(final String BSSID, final String SSID,
			final float level, final int channel, final double percent) {
		this.BSSID = BSSID;
		this.SSID = SSID;
		this.level = level;
		this.channel = channel;
		this.percent = percent;

		this.median = new Median();
		this.median.addValue(level);
	}

	/**
	 * 
	 * @param result
	 */
	public PositionInfo(final ScanResult result) {
		this.BSSID = result.BSSID;
		this.SSID = result.SSID;
		this.level = result.level;
		this.channel = PositionInfo.FrequencyToChannel(result.frequency);
		this.percent = PositionInfo.calculateSignal(this.level);

		this.median = new Median();
		this.median.addValue(this.level);
	}

	/**
	 * 
	 * @param positionInfo
	 *            kopierende PositionInfo
	 */
	public PositionInfo(final PositionInfo positionInfo) {
		this.BSSID = positionInfo.BSSID;
		this.SSID = positionInfo.SSID;
		this.level = positionInfo.level;
		this.channel = positionInfo.channel;
		this.percent = positionInfo.percent;
		try {
			this.median = positionInfo.median.clone();
		} catch (final CloneNotSupportedException e) {
			Log.e("PositionInfo>clone>", e.getMessage());
		}
	}

	/**
	 * @return Eindeutige Kennung des AP's
	 */
	public String getBSSID() {
		return this.BSSID;
	}

	/**
	 * 
	 * @return Name des Funknetzes
	 */
	public String getSSID() {
		return this.SSID;
	}

	/**
	 * 
	 * @return Signalst�rke des Funknetzes
	 */
	public float getLevel() {
		return this.level;
	}

	/**
	 * 
	 * @return Kanal des Funknetzes
	 */
	public int getChannel() {
		return this.channel;
	}

	/**
	 * 
	 * @return Signalst�rke in Prozent des Funknetzes
	 */
	public double getPercent() {
		return this.percent;
	}

	/**
	 * 
	 * @return Median des Funknetzes
	 */
	public double getMedian() {
		if (this.median == null) {
			return 0.0d;
		}
		return this.median.getMedian();
	}

	/**
	 * <b>ACHTUNG</b>, {@link #count} wird auf 1 gesetzt
	 * 
	 * @param level
	 *            Signalst�rke
	 */
	public void setLevel(final float level) {
		this.count = 1;
		this.level = level;
	}

	/**
	 * F�gt eine Signalst�rke dem Objekt hinzu
	 * 
	 * @param level
	 *            Signalst�ke des Funknetzes
	 */
	public void AddLevel(final float level) {
		/* Anzahl der empfangen Signale wird erh�ht */
		this.count++;

		/* Erstellen des Median, wenn keiner existiert */
		if (this.median == null) {
			this.median = new Median();
		}

		/* Berechnen des Medians */
		this.median.addValue(level);
		this.median.calculate();

		/* DEBUG Nachricht */
		final String logmsg = "Count:" + this.count + " value: "
				+ this.median.getMedian();
		Log.v("PositionInfo>MEDIAN", logmsg);

		/* Inkrementieren der Signalst�rke */
		this.level += level;
	}

	/**
	 * Berechnet den Mittelwert des Funknetzes
	 * 
	 * @return Mittelwert des Funktnetzes
	 */
	public float calculateAvgLevel() {
		return this.level / this.count;
	}

	/**
	 * NICHT BENUTZEN
	 * 
	 * @param pi
	 */
	public void AddResponse(final PositionInfo pi) {
		if (this.responses == null) {
			this.responses = new ArrayList<PositionInfo>();
		}

		this.responses.add(pi);
	}

	/**
	 * Erzeugt XML-Elemente
	 * 
	 * @return XML-Elemente
	 */
	public String ToXMLElements() {
		final XmlSerializer serializer = Xml.newSerializer();
		final StringWriter writer = new StringWriter();

		try {
			serializer.setOutput(writer);
			// serializer.startDocument("UTF-8", true);
			serializer.startTag("", "Spot");
			serializer.startTag("", "BSSID");
			serializer.text(this.BSSID);
			serializer.endTag("", "BSSID");
			serializer.startTag("", "Channel");
			serializer.text("" + this.channel);
			serializer.endTag("", "Channel");
			serializer.startTag("", "Level");
			serializer.text("" + this.calculateAvgLevel());
			serializer.endTag("", "Level");
			serializer.startTag("", "Median");
			serializer.text("" + this.median.getMedian());
			serializer.endTag("", "Median");
			serializer.endTag("", "Spot");
			serializer.endDocument();
		} catch (final Exception e) {
			// ---
			Log.v("PositionInfo>TOXMLElements", e.getMessage());
		}

		return writer.toString();
	}

	/**
	 * Berechnet die Signalst�rke in Prozent
	 * 
	 * @param level
	 *            Signalst�rke des Funknetzes
	 * @return Signalst�rke des Funknetzes in Prozent
	 */
	public static double calculateSignal(final double level) {
		final double maxSignal = -20.0, disassociationSignal = -95.0;
		final double percent = 100.0 - 80.0 * (maxSignal - level)
				/ (maxSignal - disassociationSignal);

		return percent;
	}

	/**
	 * Wandelt die Frequenz in ein Channelwert um. Die Frequenz und Channelwerte
	 * wurde aus Wikipedia bezogen...
	 * 
	 * @param frequency
	 *            Fequenz des Channel
	 * @return Channel
	 */
	public static int FrequencyToChannel(final int frequency) {
		int channel = 0;

		switch (frequency) {
		case 2412:
			channel = 1;
			break;
		case 2417:
			channel = 2;
			break;
		case 2422:
			channel = 3;
			break;
		case 2427:
			channel = 4;
			break;
		case 2432:
			channel = 5;
			break;
		case 2437:
			channel = 6;
			break;
		case 2442:
			channel = 7;
			break;
		case 2447:
			channel = 8;
			break;
		case 2452:
			channel = 9;
			break;
		case 2457:
			channel = 10;
			break;
		case 2462:
			channel = 11;
			break;
		case 2467:
			channel = 12;
			break;
		case 2472:
			channel = 13;
			break;
		}

		return channel;
	}

	/**
	 * Tiefe Kopie eines PositionInfo Objekts
	 */
	@Override
	public PositionInfo clone() {
		final PositionInfo pi = new PositionInfo(this.BSSID, this.SSID,
				this.level, this.channel, this.percent);
		try {
			pi.median = this.median.clone();
		} catch (final CloneNotSupportedException e) {
			e.printStackTrace();
		}

		return pi;
	}
}
