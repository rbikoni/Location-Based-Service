package com.WifiScanner;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MyWifiScanner extends Activity implements OnTouchListener {
	private WifiManager wifiManager;
	private TextView results, countdown;
	private MyReceiver receiver;
	private PositionInfos pInfos;
	private final long cdInterval = 1000, cdDuration = 60000, fakeTime = 200; // msec
	private CountDownTimer cdt;
	private ScrollView scrollView;
	private boolean touched = false;
	public static ArrayList<PositionInfo> infos = new ArrayList<PositionInfo>();

	boolean registered = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.main2);

		/* TextView ProbeResponses */
		this.results = (TextView) this.findViewById(R.id.TextView01);

		/* Countdown Scanzeit */
		this.countdown = (TextView) this.findViewById(R.id.TextView02);

		/* ScrollView */
		this.scrollView = (ScrollView) this.findViewById(R.id.ScrollView01);
		this.scrollView.setOnTouchListener(this);

		/* WiFi Service */
		this.wifiManager = (WifiManager) this
				.getSystemService(Context.WIFI_SERVICE);
	}

	public void Scan() {
		if (this.wifiManager.isWifiEnabled()) {
			this.register();
			this.wifiManager.startScan();
		} else {
			this.results.setText("Wifi not enabled.");
		}
	}

	private void register() {
		if (!this.registered) {
			this.registerReceiver(this.receiver, new IntentFilter(
					WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
			this.registered = true;
		}
	}

	private void unregister() {
		if (this.registered) {
			this.unregisterReceiver(this.receiver);
			this.registered = false;
		}
	}

	@Override
	public boolean onTouch(final View arg0, final MotionEvent arg1) {
		if (this.touched) {
			return false;
		} else {
			this.touched = true;
		}

		this.receiver = new MyReceiver(this.wifiManager, this.results);
		if (infos != null) {
			infos.clear();
		}

		if (this.pInfos != null) {
			this.pInfos.ClearPositionInfos();
		}
		if (this.receiver != null) {
			this.receiver.ClearScanResults();
		}

		/*
		 * Der eigentliche Countdown unterscheidet sich von der Eingabe, weil
		 * zwischen den einzelnen Ticks Zeit verloren geht. Dadurch kann es
		 * passieren, dass der eigentlich Wert z.B: 5000 msec mit einem
		 * Intervall von 1000msec beim letzten Tick nicht mehr im Intervall ist
		 * da: Zeit-(rand msec)/Interval > Interval für ein Tick sein muss.
		 */
		final long realTime = this.cdDuration + this.fakeTime;
		this.cdt = new CountDownTimer(realTime, this.cdInterval) {

			@Override
			public void onFinish() {
				;
				MyWifiScanner.this.unregister();

				final WifiScanner th = (WifiScanner) MyWifiScanner.this
						.getParent();
				th.setTab(TABS.POSITIONS);

				MyWifiScanner.this.pInfos = MyWifiScanner.this.receiver
						.GetPositionInformations();
				MyWifiScanner.this.pInfos.calculateAvgLevel();

				Toast.makeText(
						MyWifiScanner.this,
						"Accesspoints gefunden:"
								+ MyWifiScanner.this.pInfos.getSize(),
						Toast.LENGTH_SHORT).show();

				infos.clear();
				MyWifiScanner.this.pInfos.sortPositionInfos();
				infos.addAll(MyWifiScanner.this.pInfos.getArrayList());

				MyWifiScanner.this.receiver.ClearScanResults();
				PositionActivity.Adapter.notifyDataSetChanged();

				MyWifiScanner.this.touched = false;
				MyWifiScanner.this.receiver = null;
			}

			@Override
			public void onTick(final long millisUntilFinished) {
				MyWifiScanner.this.Scan();
				final long seconds = (realTime - millisUntilFinished) / 1000;
				MyWifiScanner.this.countdown.setText(seconds + 1 + "sec");
			}
		};

		this.cdt.start();

		return false;
	}
}