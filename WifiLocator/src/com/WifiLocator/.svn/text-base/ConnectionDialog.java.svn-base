package com.WifiLocator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

/**
 * Erstellt ein Dialog indem der Benutzer die Addresse des Servers eingeben
 * kann.
 * 
 * @author rnb
 * 
 */
public class ConnectionDialog implements DialogInterface.OnClickListener {
	/**
	 * Erstellt Dialoge
	 */
	private AlertDialog.Builder alert = null;
	/**
	 * Enthält die Referenz des Eingabetext
	 */
	private EditText input = null;
	/**
	 * Enthält die Hostaddresse
	 */
	private String host = "";

	/**
	 * 
	 * @param context
	 *            Applikationskontext der Activity
	 */
	public ConnectionDialog(final Context context) {
		this.alert = new AlertDialog.Builder(context);
		this.input = new EditText(context);

		this.alert.setTitle("Server Address");
		this.alert.setView(this.input);
		this.alert.setPositiveButton("Ok", this);
		this.alert.setNegativeButton("Chancel", this);
	}

	@Override
	public void onClick(final DialogInterface dialog, final int which) {
		switch (which) {
		case Dialog.BUTTON_POSITIVE: {
			/* Speichern der Eingabe */
			this.host = this.input.getText().toString().trim();
			break;
		}
		case Dialog.BUTTON_NEGATIVE: {
			/* Eingabe wird verworfen */
			dialog.cancel();
			break;
		}
		default:
		}
	}

	/**
	 * Anzeigen des Dialogs
	 */
	public void show() {
		if (this.alert == null) {
			return;
		}

		this.alert.show();
	}

	/**
	 * @return Hostaddresse
	 */
	public String getHostAddress() {
		return this.host;
	}
}
