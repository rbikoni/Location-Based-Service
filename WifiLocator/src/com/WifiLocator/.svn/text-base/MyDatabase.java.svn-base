package com.WifiLocator;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

/**
 * Ein Adapter um den Zugriff auf die Datenbank zu verwalten
 * 
 * @author rnb
 */
public class MyDatabase {
	/**
	 * Referenz auf eine Sqlitedatenbank
	 */
	private SQLiteDatabase db = null;
	/**
	 * Pfad der Datenbank
	 */
	private final String path = Environment.getExternalStorageDirectory() + "/";
	/**
	 * Name der Datenbank
	 */
	private String dbName = "";

	/**
	 * Mithilfe der Referenz können Ausgaben erzeugt werden
	 */
	private final Context context;
	/**
	 * ErrorCode
	 */
	private int error = 0;

	/**
	 * @param context
	 *            Applikationskontext
	 * @param databaseName
	 *            Name der Datenbank
	 * @param version
	 *            Versionsnummer der Datenbank
	 */
	public MyDatabase(final Context context, final String databaseName,
			final int version) {
		this.context = context;
		this.dbName = databaseName;

		/* Helper verwaltet die Datenbank und stellt unter */
		final DBHelper helper = new DBHelper(context, databaseName, version);

		/* Datenbank wird zum lesen geöffnet */
		try {
			this.db = helper.openDatabase(this.path + this.dbName);
		} catch (final SQLiteException sqlError) {
			Toast.makeText(context, "Fehler beim öffnen der Datenbank!",
					Toast.LENGTH_SHORT).show();
			this.db = null;
			this.error = 1;
		}
	}

	/**
	 * Anhand der PositionId werden alle Spots in der Datenbank gesucht die die
	 * überlieferte PositonsId enthalten.
	 * 
	 * @param positionId
	 *            einer Position
	 * @return eine Liste aller Spots die die PositionsId enthalten
	 */
	private PositionInfos getPositionInfos(final int positionId) {

		/* SQL-Query */
		final String query = "SELECT BSSID,Level,Median,Channel FROM "
				+ SQLTable.SPOT_TABLE + " WHERE Position_Id = ?;";
		final String query2 = "SELECT Description FROM "
				+ SQLTable.POSITION_TABLE + " WHERE Position_Id = ?;";

		final Cursor cursor = this.db.rawQuery(query, new String[] { ""
				+ positionId });
		PositionInfos infos = null;

		/* === P O S I T I O N === */
		final Cursor positionCursor = this.db.rawQuery(query2,
				new String[] { "" + positionId });
		if (positionCursor.moveToFirst()) {
			infos = new PositionInfos();
			infos.setDescription(positionCursor.getString(0));
		}

		/* Cursor schließen */
		if (positionCursor != null && positionCursor.isClosed()) {
			positionCursor.close();
		}

		/* Mindestens ein Element ist vorhanden */
		if (cursor.moveToFirst()) {
			/* PositionsId wird gesetzt um später referenzieren zu können */
			infos.setPositionId(positionId);

			do {
				/* === S P O T === */
				/* Spalte 1 BSSID */
				final String BSSID = cursor.getString(0);
				/* Spalte 2 level */
				final Double level = cursor.getDouble(1);
				/* Spalte 3 Median */
				final Double median = cursor.getDouble(2);
				/* Spalte 4 channel */
				final int channel = cursor.getInt(3);

				/* Speicher der Information */
				infos.AddPositionInfo(new PositionInfo(BSSID, "", level
						.floatValue(), median.floatValue(), channel, 0.0));

			} while (cursor.moveToNext());
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
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
	 */
	public void selectSpotsFromBSSID(final String[] BSSID,
			final ArrayList<PositionInfos> positions) {
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

		Log.v("INFOOOOOS", "" + positions.size());
	}

	/**
	 * 
	 * @param BSSID
	 *            Kennung der aktuellen Position
	 * @return Position_Ids der enthaltenen Referenzpunkte
	 */
	private int[] getPositionIdsFromBSSID(final String[] BSSID) {
		// SQL-Select Anweisung
		final String query1 = "SELECT Position_Id FROM " + SQLTable.SPOT_TABLE
				+ " WHERE BSSID = ?" + " GROUP BY Position_Id;";
		final Cursor cursor = this.db.rawQuery(query1,
				new String[] { BSSID[0] });
		int[] positionIds = null;

		for (int i = 0; i < BSSID.length; ++i) {
			Log.v("DB:GetPositionIds:BSSID[]", "index:" + i + " value"
					+ BSSID[i]);
		}

		Log.v("DB:getPositionIds:cursor", "" + cursor.getCount());
		if (cursor.moveToFirst()) {

			positionIds = new int[cursor.getCount()];
			int index = 0;
			do {
				positionIds[index++] = cursor.getInt(0);
			} while (cursor.moveToNext());
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return positionIds;
	}

	/**
	 * Selektiert alle Einträge in dem Table tbl_positions
	 * 
	 * @param tableName
	 *            Name des Tables
	 * @return Liste die die gefunden Rows enthält
	 * @throws Exception
	 */
	public ArrayList<String> selectAllFromTable(final String tableName)
			throws Exception {
		// Überprüft den Tabellennamen
		if (!tableName.matches(SQLTable.POSITION_TABLE)
				&& !tableName.matches(SQLTable.SPOT_TABLE)) {
			throw new Exception("Table " + tableName + " not found.");
		}

		// Datenstruktur um das Ergebnis der SQL-Query zu speichern
		final ArrayList<String> list = new ArrayList<String>();

		// SQL-Select Anweisung
		final Cursor cursor = this.db.query(tableName, new String[] { "*" },
				null, null, null, null, null);

		// Speichern der Ergebnisse
		if (cursor.moveToFirst()) {
			do {
				String row = "";

				// Einzelne Spalten
				for (int i = 0; i < cursor.getColumnCount(); ++i) {
					row = row + cursor.getString(i) + " ";
				}

				list.add(row);
			} while (cursor.moveToNext());
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		return list;
	}

	/**
	 * Die Datenbank wird geschlossen.
	 */
	public void Close() {
		this.db.close();
	}

	/**
	 * Mit der Methode kann überprüft werden ob ein Fehler bei der letzten
	 * Datenbankaktion aufgetreten ist oder nicht.
	 * 
	 * @return true: Fehler <br />
	 *         false: kein Fehler
	 */
	public boolean hasErrors() {
		if (this.error == 0) {
			return false;
		}
		return true;
	}

	/**
	 * Nested Class Verwaltet die Tabellennamen der Datenbank
	 */
	private static class SQLTable {
		/**
		 * SQL Table
		 */
		public static final String POSITION_TABLE = "Positions";
		/**
		 * SQL Table
		 */
		public static final String SPOT_TABLE = "Spots";
	}

	/**
	 * Nested Class Verwaltet die Datenbank beim erstellen, öffnen oder updaten
	 * Die Klasse enthält SQL-Queries um den PositionTable zu erstellen.
	 */
	class DBHelper extends SQLiteOpenHelper {

		/*
		 * CREATE SQL Queries
		 */
		/**
		 * CREATE POSITION TABLE
		 */
		private final String cPositionTable = "CREATE TABLE IF NOT EXISTS "
				+ SQLTable.POSITION_TABLE
				+ " (Position_id integer PRIMARY KEY autoincrement,"
				+ " X double, " + " Y double, " + " Z double, "
				+ " Angle integer, " + " Description varchar(50))";
		/**
		 * CREATE SPOTS TABLE
		 */
		private final String cSpotsTable = "CREATE TABLE IF NOT EXISTS "
				+ SQLTable.SPOT_TABLE
				+ "(Spot_id integer primary key autoincrement,"
				+ " BSSID text(17), " + " Level double, "
				+ " Channel integer, " + " Position_id integer, "
				+ "FOREIGN KEY(Position_id) REFERENCES Positions(Position_id))";

		/**
		 * @param context
		 * @param databaseName
		 *            Name der Datenbank
		 * @param version
		 *            Versionsnummer
		 */
		public DBHelper(final Context context, final String databaseName,
				final int version) {
			super(context, databaseName, null, version);
		}

		/**
		 * Erstellt Tabellen der Datenbank
		 */
		@Override
		public void onCreate(final SQLiteDatabase db) {
			db.execSQL(this.cPositionTable);
			db.execSQL(this.cSpotsTable);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database
		 * .sqlite.SQLiteDatabase, int, int) Erstellt eine neue Version der
		 * Datenbank, dabei werden vorhandene Tabellen gelöscht und durch neue
		 * Tabellen ersetzt.
		 */
		@Override
		public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
				final int newVersion) {
			final String dPosition = "DROP TABLE IF EXISTS "
					+ SQLTable.POSITION_TABLE;
			final String dSpot = "DROP TABLE IF EXISTS " + SQLTable.SPOT_TABLE;

			db.execSQL(dPosition);
			db.execSQL(dSpot);

			this.onCreate(db);
		}

		/**
		 * @param path
		 *            Datenbankpfad
		 * @return Referenz auf die sqlite Datenbank
		 * @throws SQLiteException
		 *             Fehler beim oeffnen
		 */
		public SQLiteDatabase openDatabase(final String path)
				throws SQLiteException {
			return SQLiteDatabase.openDatabase(path, null,
					SQLiteDatabase.OPEN_READONLY);
		}

	}
}
