package com.WifiScanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

/**
 * In der Datenbank: Spots mit gleicher Position_Id<br/>
 * Enthält Informationen die die Position eindeutig identifizieren kann.
 * PositionInfos enthält nach der Datenbank mehrer Spots zu einer PositionId. Um
 * eine Position eindeutig zu Identfizieren, werden diverese Informationen der
 * AccessPoints benötigt wie die, BSSID und den Channel.
 * 
 * @author rnb
 * 
 */
public class PositionInfos implements IExport {
	/**
	 * Eine Datenstruktur die Positionselemente verwaltet
	 */
	private final ArrayList<PositionInfo> infos = new ArrayList<PositionInfo>();
	/**
	 * Eine Referenz die zusätzliche Informationen zu der Position enthält
	 */
	private Information information;

	/**
	 * Konstruktor
	 */
	public PositionInfos() {
		this.information = new Information();
	}

	/**
	 * Erstellt Elemente anhand der übergebenen Datenstruktur
	 * 
	 * @param infos
	 *            Datenstruktur
	 */
	public PositionInfos(final ArrayList<PositionInfo> infos) {
		super();
		for (final PositionInfo info : infos) {

			this.infos.add(info);
		}
	}

	/**
	 * AddPositionInfo versucht die neue PositionInformation in eine bereits
	 * bestehende PositionInformation einzubetten (via List). Dadurch kann die
	 * avgSignalstärke oder Median des AP's innherlab des Zeitintervalls
	 * festgestellt werden.
	 * 
	 * @param positionInfo
	 *            PositionInformation die verwaltet ggf. zugewiesen werden muss.
	 */
	public synchronized void AddPositionInfo(final PositionInfo positionInfo) {
		final ListIterator<PositionInfo> it = this.infos.listIterator();

		// found true: Ein Element mit der gleichen BSSID ist vorhanden
		// found false: Ein Element mit der gleichen BSSID ist nicht vorhanden
		boolean found = false;

		// Vorhandene PositionInformationen werden durchlaufen
		// Falls ein Spot mit gleicher BSSID vorhanden ist, wird das Level der
		// PositionInformation hinzugefügt,
		// wenn nicht, wird dieses Element hinzugefügt
		while (it.hasNext()) {
			final PositionInfo info = it.next();
			// Gleichheit der BSSID wird überprüft
			if (info.getBSSID().matches(positionInfo.getBSSID())) {
				/**
				 * AddResponse funktioniert leider nicht richtig und macht große
				 * Probleme beim Debuggen. Die ArrayList-Struktur 'responses' in
				 * PositionInfos ist immer null.
				 */
				// info.AddResponse(positionInfo);

				// Workaround
				info.AddLevel(positionInfo.getLevel());
				it.set(info);
				found = true;
			}
		}

		if (!found) {
			this.infos.add(positionInfo);
		}
	}

	/**
	 * Sortiert die PositionsElemente mithilfe des Medians. Absteigende
	 * Sortierung.
	 */
	public void sortPositionInfos() {
		final int len = this.infos.size();
		for (int index = 0; index < len; ++index) {
			PositionInfo best = this.infos.get(index);

			for (int index2 = index + 1; index2 < len; ++index2) {
				final PositionInfo challenger = this.infos.get(index2);

				if (best.getMedian() < challenger.getMedian()) {
					final PositionInfo tmp = best.clone();
					Log.v("PositionInfos>Median",
							"best Median:" + best.getMedian()
									+ "\nchallenger Median:"
									+ challenger.getMedian());
					this.infos.set(index, challenger);
					this.infos.set(index2, tmp);

					best = challenger;
				}
			}
		}
	}

	/**
	 * Berechnet einen Mittelwert anhand der gesammelten Information.
	 */
	public void calculateAvgLevel() {
		final ListIterator<PositionInfo> it = this.infos.listIterator();

		while (it.hasNext()) {
			final PositionInfo pi = it.next();
			pi.setLevel(pi.calculateAvgLevel());
			it.set(pi);
		}
	}

	/**
	 * 
	 * @param index
	 *            PositionInfo Index
	 * @return PositionInfo Element
	 */
	public PositionInfo getPosition(final int index) {
		return this.infos.get(index);
	}

	/**
	 * Ausgabe der PositionInformationen.
	 * 
	 * @param view
	 *            Objekt mit dem die Ausgabe erstellt wird.
	 */
	public void printPositionInfos(final TextView view) {
		final StringBuilder sb = new StringBuilder();
		final Iterator<PositionInfo> it = this.infos.iterator();

		/*
		 * Alle vorhandenen Elemente werden mit einem Iterator durchlaufen und
		 * die Informationen werden mithilfe eines StringBuilders bearbeitet um
		 * die Ausgabe in der TextView darzustellen.
		 */
		while (it.hasNext()) {
			final PositionInfo pi = it.next();

			final String BSSID = pi.getBSSID();

			sb.append(BSSID);
			sb.append(":");
			sb.append(pi.getLevel());
			sb.append("\n");
		}

		// Ausgabe
		view.setText(sb);
	}

	/**
	 * PositionsId wird gesetzt
	 * 
	 * @param id
	 *            Wert der PositionsId
	 */
	public void setPositionId(final int id) {
		if (id < 0) {
			// Fehlermeldung
		}
		this.information.setId(id);
	}

	/**
	 * Alle Elemente der Datenstrukture werden gelöscht.
	 */
	public void ClearPositionInfos() {
		this.infos.clear();
	}

	/**
	 * Alle Elemente werden mithilfe eines XML-Streams in Elemnete umgewandelt
	 * und in eine XML-Datei serialisiert.
	 */
	@Override
	public void toXml() {
		// Der Status der Speicherkarte wird überprüft
		final String state = Environment.getExternalStorageState();
		if (!(Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY
				.equals(state))) {
			// Fehlermeldung: Can't write.
			return;
		}

		final File root = Environment.getExternalStorageDirectory();
		final File xmlFile = new File(root, "positions.xml");
		final boolean append = true;
		FileOutputStream fout;
		final StringBuilder sb = new StringBuilder();

		// sb.append("<Map>");
		sb.append("<Position>");
		sb.append("<Information>");

		// Position
		sb.append("<Position_ID>");
		sb.append(this.information.getId());
		sb.append("</Position_ID>");

		// Description
		sb.append("<Description>");
		sb.append(this.information.getDescription());
		sb.append("</Description>");

		// Koordinates x,y,z
		sb.append("<X>");
		sb.append(this.information.getX());
		sb.append("</X>");
		sb.append("<Y>");
		sb.append(this.information.getY());
		sb.append("</Y>");
		sb.append("<Z>");
		sb.append(this.information.getZ());
		sb.append("</Z>");

		// Angle
		sb.append("<Angle>");
		sb.append(this.information.getAngle());
		sb.append("</Angle>");
		sb.append("</Information>");

		// Spots
		sb.append("<Spots>");

		final Iterator<PositionInfo> info = this.infos.iterator();

		// Spot
		// Die XML-Elemente werden in einem StringBuilder gespeichert
		while (info.hasNext()) {
			sb.append(info.next().ToXMLElements());
			// sb.append("\n");
		}

		sb.append("</Spots>");
		sb.append("</Position>");
		// sb.append("</Map>");

		// Die XML-Elemente werden in eine Datei gespeichert
		try {
			fout = new FileOutputStream(xmlFile, append);
			fout.write(sb.toString().getBytes());
			fout.flush();
			fout.close();

		} catch (final FileNotFoundException e) {
			// Fehlermeldung: File not Found
			Log.e("PositionInfos>toXml>", e.getMessage());

		} catch (final IOException e) {
			// Fehlermeldung: IO error
			Log.e("PositionInfos>toXml>", e.getMessage());
		}
	}

	/**
	 * Informationen {@link #information} der Position werden hinzugefügt.
	 * 
	 * @param information
	 *            Informationen der Position
	 */
	public void setInformation(final Information information) {
		this.information = new Information(information.getId(),
				information.getX(), information.getY(), information.getZ(),
				information.getDescription(), information.getAngle());
	}

	/**
	 * @return Die Anzahl der PositionInfo Elemente
	 */
	public int getSize() {
		return this.infos.size();
	}

	/**
	 * @return interne Datenstruktur
	 */
	public ArrayList<PositionInfo> getArrayList() {
		return this.infos;
	}

}
