package com.WifiScanner;

import java.text.NumberFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class PositionActivity extends Activity implements OnTouchListener,
		android.content.DialogInterface.OnClickListener {
	public ListView lv;
	public static PositionItemAdapter Adapter;
	private boolean touched = false;
	private AlertDialog.Builder alert;
	private EditText idEdit;
	private EditText xEdit, yEdit, zEdit, descriptionEdit;
	private Spinner angleSpinner;
	private Information information;
	private int positionId = 0;
	private boolean called = false;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main3);

		this.lv = (ListView) this.findViewById(R.id.ListView01);
		Adapter = new PositionItemAdapter(this, R.layout.position_item,
				MyWifiScanner.infos);
		this.lv.setAdapter(Adapter);
		this.lv.setOnTouchListener(this);
		this.information = new Information();
	}

	/**
	 * Nested class.
	 * 
	 * @author rnb
	 * 
	 */

	public class PositionItemAdapter extends ArrayAdapter<PositionInfo> {
		private final NumberFormat numberFormat;
		private final ArrayList<PositionInfo> infos;

		public PositionItemAdapter(final Context context,
				final int textViewResourceId,
				final ArrayList<PositionInfo> objects) {
			super(context, textViewResourceId, objects);
			this.infos = objects;
			this.numberFormat = NumberFormat.getInstance();
			this.numberFormat.setMinimumFractionDigits(1);
			this.numberFormat.setMaximumFractionDigits(1);
		}

		@Override
		public View getView(final int position, final View convertView,
				final ViewGroup parent) {
			View v = convertView;

			if (v == null) {
				final LayoutInflater inflater = (LayoutInflater) PositionActivity.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.position_item, null);
			}

			final PositionInfo info = this.infos.get(position);

			if (info != null) {
				final TextView bssid = (TextView) v
						.findViewById(R.id.TextView01);
				final TextView channel = (TextView) v
						.findViewById(R.id.TextView02);
				final TextView avgLevel = (TextView) v
						.findViewById(R.id.TextView03);
				final TextView medianLevel = (TextView) v
						.findViewById(R.id.TextView04);
				final ImageView iv = (ImageView) v
						.findViewById(R.id.ImageView01);

				if (bssid != null) {
					bssid.setText("ID: " + info.getBSSID());
				}
				if (channel != null) {
					channel.setText("Channel: " + info.getChannel());
				}
				if (avgLevel != null) {
					avgLevel.setText("Mean   : "
							+ this.numberFormat.format(info.calculateAvgLevel()));
				}
				if (medianLevel != null) {
					medianLevel.setText("Med: "
							+ this.numberFormat.format(info.getMedian()));
				}
				if (iv != null) {
					iv.setImageResource(R.drawable.wifisymbol);
				}
			}

			return v;
		}
	}

	@Override
	public boolean onTouch(final View v, final MotionEvent event) {
		if (!this.touched) {
			this.touched = true;
			this.Save();
		}
		return false;
	}

	@Override
	public void onClick(final DialogInterface dialog, final int which) {
		if (which == -2) {
			this.called = this.touched = false;
			dialog.dismiss();
			return;
		}

		// Id
		String inputId = this.idEdit.getText().toString();

		// Angle
		final String angleStr = (String) (this.angleSpinner.getSelectedItem());

		// Koordinaten
		final String xCord = this.xEdit.getText().toString();
		final String yCord = this.yEdit.getText().toString();
		final String zCord = this.zEdit.getText().toString();

		// Description
		final String descr = this.descriptionEdit.getText().toString();

		double x, y, z;
		x = y = z = 0.0;
		int angle = 0;

		try {
			this.positionId = Integer.parseInt(inputId);
			angle = Integer.parseInt(angleStr);
			x = Double.parseDouble(xCord);
			y = Double.parseDouble(yCord);
			z = Double.parseDouble(zCord);
			this.called = true;
		} catch (final NumberFormatException ne) {
			Log.v("#############", ne.getMessage());
			inputId = "";
		}

		if (this.called) {
			this.called = false;
			this.information.setX(x);
			this.information.setY(y);
			this.information.setZ(z);
			this.information.setDescription(descr);
			this.information.setId(this.positionId);
			this.information.setAngle(angle);

			final PositionInfos pInfos = new PositionInfos(MyWifiScanner.infos);
			pInfos.setInformation(this.information);
			pInfos.toXml();

			this.touched = false;
			this.information.setId(this.information.getId() + 1);
			this.information.setAngle((this.information.getAngle() + 90) % 360);
		}

		dialog.dismiss();

	}

	private void Save() {
		// pInfos = receiver.GetPositionInformations();

		View v = new View(this);
		final LayoutInflater inf = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = inf.inflate(R.layout.position_eingabe, null);

		this.alert = new AlertDialog.Builder(this);
		this.alert.setMessage("Positionsinformationen").setCancelable(true)
				.setPositiveButton("Ok", this)
				.setNegativeButton("Cancel", this).setView(v);

		this.angleSpinner = (Spinner) v.findViewById(R.id.Spinner01);
		final ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
				R.array.angles, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.angleSpinner.setAdapter(adapter);

		this.idEdit = (EditText) v.findViewById(R.id.EditText01);
		this.xEdit = (EditText) v.findViewById(R.id.EditText02);
		this.yEdit = (EditText) v.findViewById(R.id.EditText03);
		this.zEdit = (EditText) v.findViewById(R.id.EditText04);
		this.descriptionEdit = (EditText) v.findViewById(R.id.EditText05);

		this.idEdit.setText("" + this.information.getId());
		this.xEdit.setText("" + this.information.getX());
		this.yEdit.setText("" + this.information.getY());
		this.zEdit.setText("" + this.information.getZ());
		this.descriptionEdit.setText("" + this.information.getDescription());
		this.angleSpinner.setSelection(this.information.getAngle() / 90);

		this.alert.show();
	}
}
