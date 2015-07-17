import java.io.File;
import java.util.ArrayList;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;

/**
 * Ermöglicht den Zugriff auf die verwendete Datenbank.
 * 
 * @author rnb
 * 
 */
public class DatabaseAdapter {
	/**
	 * Datenbankverbindung
	 */
	private SQLiteConnection db = null;

	/**
	 * 
	 * @param databasePath
	 *            Pfad der Datenbank
	 */
	public DatabaseAdapter(final String databasePath) {
		try {
			this.db = new SQLiteConnection(new File(databasePath));
		} catch (final Exception e) {
			System.err.println((e.getMessage()));
		}
	}

	/**
	 * Oeffnet die Datenbank
	 * 
	 * @return <b>true</b>, wenn die Datenbank erfolgreich geöffnet wurde<br />
	 *         <b>false</b>, wenn die Datenbank nicht geoeffnet wurde
	 */
	public boolean open() {
		if (this.db == null) {
			return false;
		}
		if (this.db.isOpen()) {
			return true;
		}

		final boolean open = false;

		try {
			this.db.open(false);
		} catch (final SQLiteException e) {
			System.err.println(e.getMessage());
		}

		return open;
	}

	/**
	 * Anhand der PositionId werden alle Spots in der Datenbank gesucht die die
	 * überlieferte PositonsId enthalten.
	 * 
	 * @param positionId
	 *            einer Position
	 * @return eine Liste aller Spots die die PositionsId enthalten
	 * @throws SQLiteException
	 */
	private PositionInfos getPositionInfos(final int positionId)
			throws SQLiteException {

		/* SQL-Query */
		final String query = "SELECT BSSID,Level,Median,Channel FROM "
				+ SQLTable.SPOT_TABLE + " WHERE Position_Id = ?";
		final String query2 = "SELECT Description FROM "
				+ SQLTable.POSITION_TABLE + " WHERE Position_Id = ?";
		final SQLiteStatement statement1 = this.db.prepare(query);
		final SQLiteStatement positionStatement = this.db.prepare(query2);

		statement1.bind(1, positionId);
		PositionInfos infos = null;

		/* === P O S I T I O N === */
		positionStatement.bind(1, positionId);
		if (positionStatement.step()) {
			infos = new PositionInfos();
			infos.setDescription(positionStatement.columnString(0));
		}

		/* Cursor schließen */
		if (positionStatement != null) {
			positionStatement.dispose();
		}

		/* Mindestens ein Element ist vorhanden */
		if (statement1.step()) {
			/* PositionsId wird gesetzt um später referenzieren zu können */
			infos.setPositionId(positionId);

			do {
				/* === S P O T === */
				/* Spalte 1 BSSID */
				final String BSSID = statement1.columnString(0);
				/* Spalte 2 level */
				final Double level = statement1.columnDouble(1);
				/* Spalte 3 Median */
				final Double median = statement1.columnDouble(2);
				/* Spalte 4 channel */
				final int channel = statement1.columnInt(3);

				/* Speicher der Information */
				infos.AddPositionInfo(new PositionInfo(BSSID, "", level
						.floatValue(), median.floatValue(), channel, 0.0));

			} while (statement1.step());
		}

		if (statement1 != null && !statement1.isDisposed()) {
			statement1.dispose();
		}

		return infos;
	}

	/**
	 * Selektiert alle möglichen Referenzpunkte in der Datenbank anhand der
	 * BSSIDs
	 * 
	 * @param BSSID
	 *            der akutellen Position
	 * @param positions
	 *            Datenstruktur um die Referenzpunkte zu speichern
	 * @throws SQLiteException
	 */
	public void selectSpotsFromBSSID(final String[] BSSID,
			final ArrayList<PositionInfos> positions) throws SQLiteException {
		// Verwaltet das Ergebnis in einer Datenstruktur
		final int[] positionIds = this.getPositionIdsFromBSSID(BSSID);

		/* Keine passende BSSID gefunden */
		if (positionIds == null) {
			return;
		}

		/* Anzahl der Einträge */
		final int positionCount = positionIds.length;

		/*
		 * Erstellt PositionsInformationen von der Position_Id. Die einzelnen
		 * Spots werden auf BSSIDs überprüft, wenn true dann wird die Position
		 * hinzugefügt
		 */
		for (int index = 0; index < positionCount; ++index) {
			final PositionInfos infos = this
					.getPositionInfos(positionIds[index]);
			/* true wenn eine der BSSIDs enthalten ist */
			if (infos.containsBSSID(BSSID)) {
				positions.add(infos);
			}
		}
	}

	/**
	 * 
	 * @param BSSID
	 *            Kennung der aktuellen Position
	 * @return Position_Ids der enthaltenen Referenzpunkte
	 * @throws SQLiteException
	 */
	private int[] getPositionIdsFromBSSID(final String[] BSSID)
			throws SQLiteException {
		// SQL-Select Anweisung
		final String query1 = "SELECT Position_Id FROM " + SQLTable.SPOT_TABLE
				+ " WHERE BSSID = ? GROUP BY Position_Id";
		final SQLiteStatement statement = this.db.prepare(query1);

		int[] positionIds = null;
		statement.bind(1, BSSID[0]);

		if (statement.step()) {
			// positionIds = new int[ROWCOUNT]; Danke sqlite4java...

			// Primitive Datentypen können nicht in komplexere Datenstrukturen
			// verwaltet werden
			final ArrayList<Integer> tmp_ids = new ArrayList<Integer>(); // Danke
																			// java...

			do {
				tmp_ids.add(statement.columnInt(0));
			} while (statement.step());
			positionIds = new int[tmp_ids.size()];
			for (int index = 0; index < tmp_ids.size(); ++index) {
				positionIds[index] = tmp_ids.get(index);
			}

		}

		if (statement != null && !statement.isDisposed()) {
			statement.dispose();
		}
		return positionIds;
	}

	/**
	 * 
	 * @param IMEI
	 *            Nummer des Mobilgerätes
	 * @return <b>Benutzername</b>, wenn die IMEI in der Datenbank eingetragen
	 *         ist<br/>
	 *         <b>IMEI</b>, wenn kein zugehöriger Eintrag in der Datenbank
	 *         gefunden werden konnte
	 * @throws SQLiteException
	 *             ?
	 */
	public String getUsernameFromIMEI(final String IMEI) throws SQLiteException {
		String username = null;
		final String query = "SELECT Username FROM " + SQLTable.USERNAME_TABLE
				+ " WHERE IMEI = ?";
		final SQLiteStatement statement = this.db.prepare(query);

		/* Bindet den Benutzernamen an den ersten Parameter */
		statement.bind(1, IMEI);

		/* Wenn ein Eintrag vorhanden ist... */
		if (statement.step()) {
			username = statement.columnString(0);
		} else {
			username = IMEI;
		}

		System.out.println("IMEI " + IMEI + " Username " + username);
		/* Rückgabe des Benutzernamens */
		return username;
	}

	/**
	 * Speichert den Namen des Benutzer, zur eindeutigen Identifikation wird die
	 * IMEI verwendet
	 * 
	 * @param IMEI
	 *            GeräteId des Telefons
	 * @param username
	 *            Name des Benutzers
	 * @throws SQLiteException
	 *             SQL Fehler
	 */
	public void insertUsername(final String IMEI, final String username)
			throws SQLiteException {
		final String query = String.format(
				"INSERT OR REPLACE INTO ? VALUES('?','?')",
				SQLTable.USERNAME_TABLE, IMEI, username);

		this.db.exec(query);
	}

	/**
	 * Nested Class Verwaltet die Tabellennamen der Datenbank
	 */
	private static class SQLTable {
		/**
		 * SQL Table Positions
		 */
		public static final String POSITION_TABLE = "Positions";
		/**
		 * SQL Table Spots
		 */
		public static final String SPOT_TABLE = "Spots";
		/**
		 * SQL Table Username
		 */
		public static final String USERNAME_TABLE = "Users";
	}
}
