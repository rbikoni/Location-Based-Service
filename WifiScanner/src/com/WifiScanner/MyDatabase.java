package com.WifiScanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.Environment;

public class MyDatabase {
	private SQLiteDatabase db = null;
	private String dbName = "";
	private final String path = "/data/data/com.WifiScanner/databases/";
	private final int VERSION = 1;
	private final Context context;

	/*
	 * INSERT SQL Queries
	 */
	private final String INSERT_POS = "INSERT INTO " + SQLTable.POSITION_TABLE
			+ "(X,Y,Z,Angle,Description) VALUES (?,?,?,?,?);";
	private final String INSERT_SPOT = "INSERT INTO " + SQLTable.SPOT_TABLE
			+ "(BSSID,Level,Median,Channel,Position_Id) VALUES (?,?,?,?,?);";
	/*
	 * 
	 */

	private final SQLiteStatement insertPosStmt, insertSpotStmt;

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

		// Helper verwaltet die Datenbank und stellt unter
		final DBHelper helper = new DBHelper(context, databaseName, version);

		// Erstellt oder öffnet eine Datenbank
		this.db = helper.getWritableDatabase();

		// Version der Datenbank wird gesetzt, bei einer neueren Version
		// wird sie geupdated via onUpgrade()
		// this.db.setVersion(version);

		// Compiliert eine Insert-Anweisung zur späteren Verwendung
		this.insertPosStmt = this.db.compileStatement(this.INSERT_POS);
		this.insertSpotStmt = this.db.compileStatement(this.INSERT_SPOT);
	}

	/**
	 * Erstellt einen Eintrag in die Tabelle Positions ein.
	 * 
	 * @see final STRING INSERT Splatenpositionen
	 * @param X
	 *            x-Koordinate in Meter
	 * @param Y
	 *            y-Koordinate in Meter
	 * @param Z
	 *            z-Koordinate in Meter
	 * @param Angle
	 *            Standpunkt in °
	 * @return Bei einem Fehler wird -1 zurückgegeben.
	 */
	public synchronized long insertPosition(final double X, final double Y,
			final double Z, final int Angle, final String Description) {
		// Spaltenposition beginnend ab 1
		final int xPos = 1;
		final int yPos = 2;
		final int zPos = 3;
		final int anglePos = 4;
		final int descPos = 5;

		// Die vorkompilierte Insert Anweisung wird ergänzt
		this.insertPosStmt.bindDouble(xPos, X);
		this.insertPosStmt.bindDouble(yPos, Y);
		this.insertPosStmt.bindDouble(zPos, Z);
		this.insertPosStmt.bindLong(anglePos, Angle);
		this.insertPosStmt.bindString(descPos, Description);

		// Das Statement wird ausgefürt
		return this.insertPosStmt.executeInsert();
	}

	public synchronized long insertSpot(final String BSSID, final double Level,
			final double Median, final int Channel, final int PosId) {
		// Spaltenposition beginnend ab 1
		final int bssidPos = 1;
		final int levelPos = 2;
		final int medianPos = 3;
		final int channelPos = 4;
		final int posPos = 5;

		// Vorkompilierte Inseranweisung wird ergänzt
		this.insertSpotStmt.bindString(bssidPos, BSSID);
		this.insertSpotStmt.bindDouble(levelPos, Level);
		this.insertSpotStmt.bindDouble(medianPos, Median);
		this.insertSpotStmt.bindLong(channelPos, Channel);
		this.insertSpotStmt.bindLong(posPos, PosId);

		// Das Statement wird ausgeführt
		return this.insertSpotStmt.executeInsert();
	}

	/**
	 * Selektiert alle Einträge in dem Table tbl_positions
	 * 
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

	public void copyDatabase() throws IOException {
		final FileInputStream fis = new FileInputStream(this.path + this.dbName);
		final File f = new File(Environment.getExternalStorageDirectory() + "/"
				+ this.dbName);
		final FileOutputStream fout = new FileOutputStream(f);

		final byte[] buffer = new byte[0xFFFF];
		for (int len = 0; (len = fis.read(buffer)) != -1;) {
			fout.write(buffer, 0, len);
		}

		fis.close();
		fout.close();
	}

	public void Close() {
		this.db.close();
	}

	/**
	 * Nested Class Verwaltet die Tabellennamen der Datenbank
	 */
	private static class SQLTable {
		public static final String POSITION_TABLE = "Positions";
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
		private final String cPositionTable = "CREATE TABLE IF NOT EXISTS "
				+ SQLTable.POSITION_TABLE
				+ " (Position_Id integer PRIMARY KEY autoincrement,"
				+ " X double, " + " Y double, " + " Z double, "
				+ " Angle integer, " + " Description varchar(50))";

		private final String cSpotsTable = "CREATE TABLE IF NOT EXISTS "
				+ SQLTable.SPOT_TABLE
				+ "(Spot_Id integer primary key autoincrement,"
				+ " BSSID text(17), " + " Level double, " + " Median double,"
				+ " Channel integer, " + " Position_Id integer, "
				+ "FOREIGN KEY(Position_Id) REFERENCES Positions(Position_Id))";

		/*
    	 * 
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
	}
}
