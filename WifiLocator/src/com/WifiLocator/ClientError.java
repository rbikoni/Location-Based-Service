package com.WifiLocator;

import android.util.Log;

/**
 * Repr�sentiert einen Fehler im ClientThread
 * 
 * @author rnb
 * 
 */
public class ClientError extends Error {
	/**
	 * 
	 * @param message
	 *            Fehlernachricht
	 * @param tag
	 *            Objektspezifisches Error-Tag
	 */
	public ClientError(final String message, final String tag) {
		super(message);
		this.tag = tag;
	}

	@Override
	protected void print() {
		Log.v(this.tag, this.message);
	}
}
