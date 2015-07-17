package com.WifiScanner;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Zeigt die verfügbaren Importerfunktionen an
 * 
 * @author rnb
 * 
 */
public class ImporterActivity extends Activity implements OnItemClickListener {
	/**
	 * Referenz der lokalen Datenbank
	 */
	private MyDatabase mdb;

	/**
	 * Name der Datenbank
	 */
	private final String databasename = "datenbank.db";

	/**
	 * Version der Datenbank
	 */
	private final int VERSION = 7;

	/**
	 * Referenz auf ein XMLDialog
	 */
	private XMLDialog xmlDialog = null;

	/**
	 * Die Variable enthält den XML-Pfad
	 */
	private String xmlPath = null;

	/**
	 * ProgressDialog
	 */
	private ProgressDialog progressDialog = null;

	/**
	 * ListView
	 */
	private ListView menu = null;

	/**
	 * Menueinträge
	 */
	private final String[] menuEntries = new String[] {
			"Import XML -> Database", "Copy Database -> SD-Card" };

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main4);

		/* ListView Menu */
		this.menu = (ListView) this.findViewById(R.id.ListView01);
		this.menu.setOnItemClickListener(this);
		this.menu.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, this.menuEntries));

	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {

		/* MenuItem Conntection */
		menu.add(Menu.NONE, 1, Menu.NONE, "XML-File...");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case 1: {
			this.xmlDialog = new XMLDialog(ImporterActivity.this);
			this.xmlDialog.show();
			break;
		}
		default:
		}

		return true;
	}

	@Override
	public void onItemClick(final AdapterView<?> parent, final View view,
			final int position, final long id) {
		this.mdb = new MyDatabase(this, this.databasename, this.VERSION);

		switch (position) {
		case 0: {
			if (this.xmlDialog == null) {
				this.xmlDialog = new XMLDialog(ImporterActivity.this);
				this.xmlDialog.show();
				break;
			}

			final Importer imp = new Importer(this.mdb);
			this.xmlPath = this.xmlDialog.getXMLPath();
			/*
			 * imp.ImportXMLToDatabase(this.xmlPath);
			 */
			this.progressDialog = ProgressDialog.show(ImporterActivity.this,
					"Please Wait...", "Importing XML to Database...");
			final ImporterThread it = new ImporterThread(imp, this.xmlPath,
					this.progressDialog, this.mdb);
			it.start();
			break;
		}
		case 1: {
			ArrayList<String> rows = null;
			try {
				rows = this.mdb.selectAllFromTable("Positions");
			} catch (final Exception e) {
				rows = new ArrayList<String>();
			}
			Toast.makeText(this, "" + rows.size(), Toast.LENGTH_SHORT).show();
			try {
				this.mdb.copyDatabase();
			} catch (final IOException e) {
				Log.v("sadssssssssssss", e.getMessage());
			}

			if (this.mdb != null) {
				this.mdb.Close();
			}
			break;
		}
		}
	}
}
