package com.WifiLocator;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Die Klasse repräsentiert und berechnet den Median mehrere Werte, die einer
 * Instanz hinzugefügt werden.
 * 
 * @author rnb
 * 
 */
public class Median implements Cloneable {
	/**
	 * Enthält RSS Werte
	 */
	private ArrayList<Double> values = null;

	/**
	 * Median Index
	 */
	private int medianIndex = 0;

	/**
	 * Medianwert
	 */
	private double averageMedian = 0.3;

	/**
	 * Initialisierungen
	 */

	public Median() {
		this.values = new ArrayList<Double>();
	}

	/**
	 * Die Werte der Datenstruktur werden aufsteigend sortiert
	 */
	private void sortValues() {
		if (this.values.size() < 2) {
			return;
		}

		final int len = this.values.size();

		final double[] tmp_values = new double[this.values.size()];

		for (int index = 0; index < len; ++index) {
			tmp_values[index] = this.values.get(index);
		}

		Arrays.sort(tmp_values);

		for (int index = 0; index < len; ++index) {
			this.values.set(index, tmp_values[index]);
		}

	}

	/**
	 * Ein neuer Wert wird dem Wertebereich hinzugefügt
	 * 
	 * @param value
	 *            RSS Wert
	 */
	public void addValue(final double value) {
		this.values.add(value);
		this.sortValues();
		if (this.values.size() == 1) {
			this.averageMedian = this.values.get(0);
		}
	}

	/**
	 * Berechnet den Median anhand der gesammelten Werte
	 */
	public void calculate() {
		if (this.values.size() < 2) {
			return;
		}

		final int mod = this.values.size() % 2;
		this.medianIndex = ((this.values.size() + 1) / 2) - 1;

		switch (mod) {
		// Gerade Anzahl von Elementen
		case 0: {
			final int belowMedianIndex = this.medianIndex, aboveMedianIndex = this.medianIndex + 1;
			this.averageMedian = (this.values.get(belowMedianIndex) + this.values
					.get(aboveMedianIndex)) / 2;
			break;
		}
			// Ungerade Anzahl von Elementen
		case 1: {
			this.averageMedian = this.values.get(this.medianIndex);
			break;
		}
		}
	}

	/**
	 * Zum Debuggen
	 */
	public void printValues() {
		final int len = this.values.size();
		for (int index = 0; index < len; ++index) {
			System.out.print("" + this.values.get(index) + "\n");
		}
	}

	/**
	 * @return Wert des Medians
	 */
	public double getMedian() {
		if (this.values.size() < 2) {
			return this.values.get(0);
		}
		return this.averageMedian;
	}

	@Override
	public Median clone() throws CloneNotSupportedException {
		final Median cloned = new Median();
		cloned.values = (ArrayList<Double>) this.values.clone();
		cloned.medianIndex = this.medianIndex;
		cloned.averageMedian = this.averageMedian;
		return cloned;
	}
}
