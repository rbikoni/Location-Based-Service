package com.WifiScanner;

import android.app.ProgressDialog;

public class ImporterThread extends Thread {
	private Importer importer = null;
	private String xmlPath = null;
	private ProgressDialog progressDialog = null;
	private MyDatabase mdb = null;

	public ImporterThread(final Importer importer, final String xmlPath,
			final ProgressDialog progressDialog, final MyDatabase mdb) {
		this.importer = importer;
		this.xmlPath = xmlPath;
		this.progressDialog = progressDialog;
		this.mdb = mdb;
	}

	@Override
	public void run() {
		this.importer.ImportXMLToDatabase(this.xmlPath);
		this.progressDialog.dismiss();
		this.mdb.Close();
	}
}
