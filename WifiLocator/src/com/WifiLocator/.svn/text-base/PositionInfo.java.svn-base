package com.WifiLocator;

import java.io.StringWriter;
import java.util.ArrayList;

import org.xmlpull.v1.XmlSerializer;

import android.net.wifi.ScanResult;
import android.util.Log;
import android.util.Xml;

/**
 * In der Datenbank: Spot<br/>
 * PositionInfo enthält Informationen zum Identifizieren eines AP's und bietet
 * darüberhinaus noch Methoden zu der Berechnung des Mittelwertes und des
 * Medians
 * 
 * @author rnb
 * 
 */
public class PositionInfo {
	/**
	 * AP SSID
	 */
	protected String SSID;
	/**
	 * AP BSSID
	 */
	protected final String BSSID;
	/**
	 * Signalstärke
	 */
	protected float level;
	/**
	 * Kanal
	 */
	protected final int channel;
	/**
	 * Signalstärke in Prozent
	 */
	protected double percent = 0.0;
	/**
	 * ----
	 */
	protected ArrayList<PositionInfo> responses;
	/**
	 * Da in einem Intervall über einen längeren Zeitraum gemessen wird ist es
	 * wichtig zu wissen, wie oft der AP gesendet hat,.
	 */
	protected int count = 1;
	/**
	 * Median
	 */
	protected Median median;

	/**
	 * 
	 * @param BSSID
	 *            BSSID des AP's
	 * @param SSID
	 *            Namen des Funknetzes
	 * @param level
	 *            Signalstärke des Funknetzes
	 * @param channel
	 *            Kanal des Funknetzes
	 * @param percent
	 *            Prozentangabe der Signalstärke, kann über
	 *            {@link #calculateSignal(double)} berechnet werden
	 */
	public PositionInfo(final String BSSID, final String SSID,
			final float level, final int channel, final double percent) {
		this(BSSID, level, channel, percent);
		this.SSID = SSID;
	}

	/**
	 * 
	 * @param BSSID
	 *            BSSID des AP's
	 * @param level
	 *            Signalstärke des Funknetzes
	 * @param channel
	 *            Kanal des Funknetzes
	 * @param percent
	 *            Prozentangabe der Signalstärke, kann über
	 *            {@link #calculateSignal(double)} berechnet werden
	 */
	public PositionInfo(final String BSSID, final float level,
			final int channel, final double percent) {
		this.BSSID = BSSID;
		this.SSID = "";
		this.level = level;
		this.channel = channel;
		this.percent = percent;

		this.median = new Median();
		this.median.addValue(level);
	}

	/**
	 * 
	 * @param BSSID
	 *            BSSID des AP's
	 * @param SSID
	 *            Netzwerkennung des Funknetzes
	 * @param level
	 *            Signalstärke des Funknetzes
	 * @param median
	 *            Median
	 * @param channel
	 *            Kanal des Funknetzes
	 * @param percent
	 *            Prozentangabe der Signalstärke, kann über
	 *            {@link #calculateAvgLevel()} berechnet werden
	 */
	public PositionInfo(final String BSSID, final String SSID,
			final float level, final float median, final int channel,
			final double percent) {

		this.BSSID = BSSID;
		this.SSID = SSID;
		this.level = level;
		this.channel = channel;
		this.percent = percent;

		this.median = new Median();
		this.median.addValue(median);
	}

	/**
	 * 
	 * @param result
	 */
	public PositionInfo(final ScanResult result) {
		this(result.BSSID, result.SSID, result.level, PositionInfo
				.FrequencyToChannel(result.frequency), PositionInfo
				.calculateSignal(result.level));
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
	 * @return Signalstärke des Funknetzes
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
	 * @return Signalstärke in Prozent des Funknetzes
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
	 *            Signalstärke
	 */
	public void setLevel(final float level) {
		this.count = 1;
		this.level = level;
	}

	/**
	 * Fügt eine Signalstärke dem Objekt hinzu
	 * 
	 * @param level
	 *            Signalstäke des Funknetzes
	 */
	public void AddLevel(final float level) {
		this.count++;
		if (this.median == null) {
			this.median = new Median();
		}
		this.median.addValue(level);
		this.median.calculate();
		final String logmsg = "Count:" + this.count + " value: "
				+ this.median.getMedian();
		Log.v("PositionInfo>MEDIAN", logmsg);
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
			Log.e("PositionInfo>ToXMLElements>", e.getMessage());
		}

		return writer.toString();
	}

	/**
	 * Berechnet die Signalstärke in Prozent
	 * 
	 * @param level
	 *            Signalstärke des Funknetzes
	 * @return Signalstärke des Funknetzes in Prozent
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
			Log.e("PositionInfo>clone>", e.getMessage());
		}

		return pi;
	}
}
