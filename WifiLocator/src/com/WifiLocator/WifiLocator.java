package com.WifiLocator;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Visualiserung der Lokalisierungsfunktionen
 * 
 * @author rnb
 * 
 */
public class WifiLocator extends Activity implements OnItemClickListener {
	/**
	 * Referenz auf ein WifiManager Objekt, ermöglicht Methoden um mit der
	 * internen Wifischnittstelle zu kommunizieren
	 */
	private WifiManager wifiManager = null;
	/**
	 * Der Wifimanager muss einen Service für das Scannen registrieren, die
	 * Variable ist <br/>
	 * <b>true</b> wenn der Service zuvor registriert wurde<br/>
	 * <b>false</b> wenn der Service nicht registriert wurde.
	 */
	private boolean registered = false;
	/**
	 * Referenz auf ein MyReceiver Objekt
	 */
	private MyReceiver receiver = null;
	/**
	 * Referenz auf ein LBSClient Objekt
	 */
	private LBSClient client = null;
	/**
	 * Scanintervall
	 */
	private final long cdInterval = 1000;
	/**
	 * Scandauer
	 */
	private final long cdDuration = 5000;
	/**
	 * Fakezeit
	 */
	private final long fakeTime = 200;
	/**
	 * Referenz auf ein CountdownTimerObjekt
	 */
	private CountDownTimer cdt;
	/**
	 * Enthält die IMEI des Clients, die IMEI wird über den TelephonyManager
	 * bezogen
	 */
	private String IMEI = "";
	/**
	 * Enthält die Hostaddresse, die über ein ConnectionDialog bezogen wird.
	 */
	private String host = "";
	/**
	 * Referenz auf ein ConnectionDialog Objekt
	 */
	private ConnectionDialog connectionDialog = null;

	/**
	 * ListView Menu
	 */
	private ListView menu = null;

	/**
	 * MenuItems
	 */
	private final String[] menuItems = new String[] { "Post My Location" };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main);

		/* WiFi Service */
		this.wifiManager = (WifiManager) this
				.getSystemService(Context.WIFI_SERVICE);

		/* IMEI wird über den TelephonyManager bezogen */
		final TelephonyManager mg = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		this.IMEI = mg.getDeviceId();

		/* ListView Menu */
		this.menu = (ListView) this.findViewById(R.id.ListView01);
		this.menu.setOnItemClickListener(this);
		this.menu.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, this.menuItems));
	}

	/**
	 * Registrieren des Scanservice
	 */
	private void register() {
		if (!this.registered) {
			this.registerReceiver(this.receiver, new IntentFilter(
					WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
			this.registered = true;
		}
	}

	/**
	 * Unregistriert den Scanservice
	 */
	private void unregister() {
		if (this.registered) {
			this.unregisterReceiver(this.receiver);
			this.registered = false;
		}
	}

	/**
	 * Registriert einen WifiService und startet den Scanvorgang
	 */
	public void Scan() {
		if (this.wifiManager.isWifiEnabled()) {
			this.register();
			this.wifiManager.startScan();
		} else {
			/* Fehlermeldung, falls WiFi nicht aktiviert ist */
			Toast.makeText(WifiLocator.this, "WiFi not enabled",
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Sendet die aktuelle Position an den Server, der in der Variable host
	 * eingetragen ist. Wurde der ConnectionDialog zuvor noch nicht aufgerufen,
	 * wird die Methode nicht ausgeführt.
	 * 
	 * @param positions
	 *            aktuelle Posiiton
	 */
	private void PostMyLocation(final PositionInfos positions) {
		if (this.connectionDialog == null) {
			this.connectionDialog = new ConnectionDialog(WifiLocator.this);
			this.connectionDialog.show();
			return;
		}

		this.host = this.connectionDialog.getHostAddress();

		if (this.host.length() > 0) {
			/* LBSClient Instanz */
			WifiLocator.this.client = new LBSClient(this.host, 1234, this.IMEI);
			WifiLocator.this.client.connect();
			WifiLocator.this.client.sendLocation(positions);
			final String message = this.client.receive();
			Toast.makeText(WifiLocator.this, message, Toast.LENGTH_SHORT)
					.show();
			this.client.close();
		} else {
			Toast.makeText(WifiLocator.this,
					"Bitte geben Sie die Hostaddresse ein.", Toast.LENGTH_SHORT)
					.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {

		/* MenuItem Conntection */
		menu.add(Menu.NONE, 1, Menu.NONE, "Connection...");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case 1: {
			this.connectionDialog = new ConnectionDialog(WifiLocator.this);
			this.connectionDialog.show();
			break;
		}
		default:
		}

		return true;
	}

	@Override
	public void onItemClick(final AdapterView<?> parent, final View view,
			final int position, final long id) {
		this.receiver = new MyReceiver(this.wifiManager);

		switch (position) {
		case 0: {
			// 1. Scan für ein Intervall (einstellbar)
			final long realTime = this.cdDuration + this.fakeTime;
			this.cdt = new CountDownTimer(realTime, this.cdInterval) {

				@Override
				public void onFinish() {
					WifiLocator.this.unregister();

					// Ergebnisse werden gespeichert
					final PositionInfos currentPosition = new PositionInfos(
							WifiLocator.this.receiver.GetPositionInformations()
									.getArrayList());

					// Löschen der Ergebnisse
					WifiLocator.this.receiver.ClearScanResults();
					WifiLocator.this.receiver = null;

					// Berechnungen, sortierungen
					currentPosition.calculateAvgLevel();
					currentPosition.sortPositionInfos();

					PositionInfos tmp_currentPosition = new PositionInfos();
					// Stärksten 3/4 AccessPoints speichern
					if (currentPosition.getSize() > 4) {
						for (int index = 0; index < 3; ++index) {
							tmp_currentPosition.AddPositionInfo(currentPosition
									.getPosition(index));
						}
					} else {
						tmp_currentPosition = currentPosition;
					}

					WifiLocator.this.PostMyLocation(tmp_currentPosition);
				}

				@Override
				public void onTick(final long millisUntilFinished) {
					WifiLocator.this.Scan();
					// final long seconds = (realTime - millisUntilFinished) /
					// 1000;
				}
			};

			this.cdt.start();

			break;
		}
		}
	}
}