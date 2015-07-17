package com.WifiLocator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * 
 * @author rnb
 * 
 */
public class MyReceiver extends BroadcastReceiver {
	/**
	 * Speichert die Scanergebnisse
	 */
	private final ArrayList<ScanResult> scanResults;

	/**
	 * Wifi Schnittstelle
	 */
	private final WifiManager wifiManager;

	/**
	 * Speichert ScanResults
	 */
	private final PositionInfos pInfos = new PositionInfos();

	/**
	 * @param wifiManager
	 *            enthält ScanResults
	 */
	public MyReceiver(final WifiManager wifiManager) {
		this.wifiManager = wifiManager;
		this.scanResults = new ArrayList<ScanResult>();
	}

	@Override
	public void onReceive(final Context arg0, final Intent arg1) {

		/*
		 * scanResults enthält alle aktuellen Antworten von den Probe-Requests.
		 */
		final List<ScanResult> scanResults = this.wifiManager.getScanResults();
		String msg = ""; // StringBuilder;

		// Durchlauf der Probe-Responses
		for (final ScanResult result : scanResults) {
			/*
			 * Antworten werden in der Datenstruktur this.scanResults
			 * gespeichert um ggf. die gespeicherte Information in einem
			 * XML-File zu sichern.
			 */
			msg += "SSID:" + result.SSID + "\n" + " * BSSID:" + result.BSSID
					+ "\n" + " * Frequency:" + result.frequency + "\n"
					+ " * Channel:"
					+ PositionInfo.FrequencyToChannel(result.frequency) + "\n"
					+ " * Level:" + result.level + "\n" + " * SPercent:"
					+ PositionInfo.calculateSignal(result.level) + "\n";

			this.scanResults.add(result);
			Log.v("RECEIVER#########", "" + this.scanResults.size());
		}
	}

	/**
	 * @return Erstellte PositionInfos von der Scannung
	 */
	public PositionInfos GetPositionInformations() {
		// Ausgewählte APs werden als PositionsInformation gespeichert
		final Iterator<ScanResult> position = this.scanResults.iterator();

		while (position.hasNext()) {
			this.pInfos.AddPositionInfo(new PositionInfo(position.next()));
		}

		// Strukturen werden geleert
		return new PositionInfos(this.pInfos.getArrayList());
	}

	/**
	 * Verwaltete PositionInfos werden gelöscht
	 */
	public void ClearScanResults() {
		if (this.scanResults != null) {
			this.scanResults.clear();
		}
	}

}
