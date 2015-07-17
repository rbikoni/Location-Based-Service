package com.WifiScanner;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;

/**
 * Die Klasse erstellt und verwaltet alle angezeigten Tabs
 * <ul>
 * <li>Scan</li>
 * <li>Positions</li>
 * <li>Import/Export</li>
 * 
 * @author rnb
 * 
 */
public class WifiScanner extends TabActivity implements TabContentFactory {

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* Scan Activity */
		final Intent i = new Intent();
		i.setClass(WifiScanner.this, MyWifiScanner.class);

		/* Position Activity */
		final Intent i2 = new Intent();
		i2.setClass(WifiScanner.this, PositionActivity.class);

		/* Importer Activity */
		final Intent i3 = new Intent();
		i3.setClass(WifiScanner.this, ImporterActivity.class);

		/* TabHost verwaltet Tabs */
		final android.widget.TabHost tabHost = this.getTabHost();

		/* Scan Tab */
		tabHost.addTab(tabHost.newTabSpec("Scan").setIndicator("Scan")
				.setContent(i));

		/* Positions Tab */
		tabHost.addTab(tabHost.newTabSpec("Positions")
				.setIndicator("Positions").setContent(i2));

		/* Export/Import Tab */
		tabHost.addTab(tabHost.newTabSpec("Export/Import")
				.setIndicator("Export/Import").setContent(i3));
	}

	@Override
	public View createTabContent(final String tag) {
		final TextView tv = new TextView(this);
		tv.setText("Content for tab with tag " + tag);
		return tv;
	}

	/**
	 * Setzt den Tabindex
	 * 
	 * @param tab
	 *            tabindex
	 */
	public void setTab(final int tab) {
		this.getTabHost().setCurrentTab(tab);
	}
}