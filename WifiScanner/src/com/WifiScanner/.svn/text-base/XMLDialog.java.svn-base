package com.WifiScanner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

/**
 * Die Klasse erstellt einen Dialog indem der Benutzer den XML Positionspfad
 * eingeben. Mithilfe von {@link #getXMLPath()} kann der Eingabewert bezogen
 * werden.
 * 
 * @author rnb
 * 
 */
public class XMLDialog implements DialogInterface.OnClickListener {

	/**
	 * Default XML Positions Pfad
	 */
	private final String defaultXMLpath = "positions.xml";
	/**
	 * Variable speichert den XML Positionspfad
	 */
	private String xmlPath = this.defaultXMLpath;
	/**
	 * Referenz auf ein AlertDialog.Builder, der komplette Dialog enthält ein
	 * Eingabefeld
	 */
	private AlertDialog.Builder alert = null;
	/**
	 * Referenz auf EditText, das Eingabefeld
	 */
	private EditText input = null;

	/**
	 * 
	 * @param context
	 *            Ausgabekontext
	 */
	public XMLDialog(final Context context) {
		this.alert = new AlertDialog.Builder(context);
		this.input = new EditText(context);

		this.alert.setTitle("XML-Position Filename");
		this.alert.setView(this.input);
		this.alert.setPositiveButton("Ok", this);
		this.alert.setNegativeButton("Chancel", this);
	}

	@Override
	public void onClick(final DialogInterface dialog, final int which) {
		switch (which) {
		case DialogInterface.BUTTON_POSITIVE: {
			this.xmlPath = this.input.getText().toString();
			break;
		}
		case DialogInterface.BUTTON_NEUTRAL: {
			dialog.cancel();
			break;
		}
		default:
			dialog.cancel();
		}
	}

	/**
	 * Anzeige des Dialogs.
	 */
	public void show() {
		if (this.alert == null) {
			return;
		}

		this.alert.show();
	}

	/**
	 * @return XML Positionspfad
	 */
	public String getXMLPath() {
		if (this.xmlPath.length() < 5) {
			this.xmlPath = this.defaultXMLpath;
		}
		return this.xmlPath;
	}

}
