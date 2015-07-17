package com.WifiLocator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.EditText;

/**
 * Username bietet dem Benutzer die Möglichkeit einen Benutzernamen einzugeben.
 * 
 * @author rnb
 * 
 */
public class UsernameDialog implements OnClickListener {
	/**
	 * Enthält den Benutzernamen
	 */
	private String username = "";
	/**
	 * Referenz auf den Dialog
	 */
	private AlertDialog.Builder alert = null;
	/**
	 * Referenz auf die Eingabemöglichkeit
	 */
	private EditText input = null;

	/**
	 * 
	 * @param context
	 *            Applikationskontext zur Anzeige
	 */
	public UsernameDialog(final Context context) {
		this.input = new EditText(context);
		this.alert = new AlertDialog.Builder(context);

		this.alert.setTitle("Username");
		this.alert.setView(this.input);
		this.alert.setPositiveButton("Ok", this);
		this.alert.setNegativeButton("Chancel", this);
	}

	@Override
	public void onClick(final DialogInterface dialog, final int which) {
		switch (which) {
		case DialogInterface.BUTTON_POSITIVE: {
			/* Speichern der Eingabe */
			this.username = this.input.getText().toString();
			break;
		}
		case DialogInterface.BUTTON_NEGATIVE: {
			/* Verwerfen der Eingabe */
			dialog.cancel();
			break;
		}
		}
	}

	/**
	 * Anzeigen des Dialogs
	 */
	public void show() {
		if (this.alert != null) {
			this.alert.show();
		}
	}

	/**
	 * @return Benutzername
	 */
	public String getUsername() {
		return this.username;
	}
}
