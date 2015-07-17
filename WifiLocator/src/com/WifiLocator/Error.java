package com.WifiLocator;

import android.util.Log;

/**
 * Abstrakte Klasse zur Darstellung eines Fehler in verschiedenen Objekten
 * 
 * @author rnb
 * 
 */
public abstract class Error {
	/**
	 * Fehlermeldung
	 */
	protected String message = "";
	/**
	 * Objektspezifisches Error-Tag
	 */
	protected String tag = "error>";

	/**
	 * 
	 * @param message
	 *            Fehlernachricht
	 */
	public Error(final String message) {
		this.message = message;
	}

	/**
	 * Ausgabe des Fehlers via Log.e
	 */
	protected void print() {
		Log.e(this.tag, this.message);
	}
}
