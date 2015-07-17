import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Enthält Informationen die die Position eindeutig identifizieren kann.
 * 
 * @author rnb
 * 
 */
public class PositionInfos {
	/**
	 * Eine Datenstruktur die Positionselemente verwaltet
	 */
	private final ArrayList<PositionInfo> infos = new ArrayList<PositionInfo>();
	/**
	 * Eine Referenz die zusätzliche Informationen zu der Position enthält
	 */
	private Information information = null;

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

		/* Elemente werden nur bei einem gultigen Objekt hinzugefügt */
		if (infos != null) {
			for (final PositionInfo info : infos) {

				this.infos.add(info);
			}
		}
	}

	/**
	 * AddPositionInfo versucht die neue Information in eine bereits bestehende
	 * PositionInformation einzubetten (via List). Dadurch kann die
	 * avgSignalstärke des AP innherlab des Zeitintervalls festgestellt werden.
	 * 
	 * @param positionInfo
	 *            Information die verwaltet ggf. zugewiesen werden muss.
	 */
	public synchronized void AddPositionInfo(final PositionInfo positionInfo) {
		final ListIterator<PositionInfo> it = this.infos.listIterator();

		// found true: Ein Element mit der gleichen BSSID ist vorhanden
		// found false: Ein Element mit der gleichen BSSID ist nicht vorhanden
		boolean found = false;

		// Vorhandene PositionInformationen werden durchlaufen
		while (it.hasNext()) {
			final PositionInfo info = it.next();
			// Gleichheit der BSSID wird überprüft
			if (info.getBSSID().matches(positionInfo.getBSSID())) {
				/**
				 * AddResponse funktioniert leider nicht richtig und macht große
				 * Probleme beim Debuggen. Die ArrayList-Struktur responses in
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
	 * Sortiert die Positionen anhand des Medians, absteigend
	 */
	public void sortPositionInfos() {
		final int len = this.infos.size();
		for (int index = 0; index < len; ++index) {
			final PositionInfo best = this.infos.get(index);

			for (int index2 = index + 1; index2 < len; ++index2) {
				final PositionInfo challenger = this.infos.get(index2);

				if (best.getMedian() < challenger.getMedian()) {
					final PositionInfo loser = best.clone();
					this.infos.set(index, challenger);
					this.infos.set(index2, loser);
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
	 *            eines Positionselementes
	 * @return Positionselement
	 */
	public PositionInfo getPosition(final int index) {
		if (index >= this.infos.size() || index < 0) {
			return null;
		}

		return this.infos.get(index);
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
			return;
		}
		this.information.setId(id);
	}

	/**
	 * Alle Elemente der Datenstruktur werden gelöscht.
	 */
	public void ClearPositionInfos() {
		this.infos.clear();
	}

	/**
	 * Setzt das Informationsobjekt neu.
	 * 
	 * @param information
	 *            neues Informationsobjekt
	 */
	public void setInformation(final Information information) {
		this.information = new Information(information.getId(),
				information.getX(), information.getY(), information.getZ(),
				information.getDescription(), information.getAngle());
	}

	/**
	 * Anzahl der enthaltenen Positionen
	 * 
	 * @return Anzahl der Positionen
	 */
	public int getSize() {
		return this.infos.size();
	}

	/**
	 * Ermöglicht den Zugriff auf interne Elemente
	 * 
	 * @return Referenz der verwaltenden Datenstruktur
	 */
	public ArrayList<PositionInfo> getArrayList() {
		return this.infos;
	}

	/**
	 * @return alle enthaltenen BSSIDs
	 */
	public String[] getBSSIDs() {
		final String[] bssids = new String[this.infos.size()];
		final int len = this.infos.size();

		/*
		 * Aus allen enthaltenen PositionInformation Elementen wird die BSSID in
		 * eine BSSID gespeichert
		 */
		for (int index = 0; index < len; ++index) {
			bssids[index] = this.infos.get(index).BSSID;
		}

		return bssids;
	}

	/**
	 * Überprüft die Position alle BSSIDs des Parameters enthalten sind
	 * 
	 * @param BSSID
	 *            Strings
	 * @return true: falls alle der Werte enthalten ist<br />
	 *         false: falls nicht alle Werte enthalten ist
	 */
	public boolean containsBSSID(final String[] BSSID) {
		int matchingIndex = 0;

		for (final PositionInfo info : this.infos) {
			for (int index = 0; index < BSSID.length; ++index) {
				/* Vergleicht die BSSIDs miteinander */
				if (info.BSSID.contains(BSSID[index])) {
					++matchingIndex;
				}
			}
		}
		/* Wenn alle BSSIDS enthalten sind, true */
		if (matchingIndex == BSSID.length) {
			return true;
		}

		return false;
	}

	/**
	 * Eine Beschreibung der Position wird gesetzt
	 * 
	 * @param description
	 *            Beschreibung
	 */
	public void setDescription(final String description) {
		this.information.setDescription(description);
	}

	/**
	 * Rückgabe der Positionsinformation
	 * 
	 * @return Beschreibung
	 */
	public String getPositionDescription() {
		return this.information.getDescription();
	}

	/**
	 * Rückgabe der PositionId
	 * 
	 * @return PositionId
	 */
	public int getPositionId() {
		return this.information.getId();
	}
}
