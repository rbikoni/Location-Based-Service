
/**
 * Die Klasse verwaltet berechnete Werte des EKD-Algorithmus und stellt den
 * Bezug zu einer anderen Datenstruktur her, mithilfe Indexes
 * {@link #positionIndex} Enthält ein Wertepaar P(PositionsIndex,EKD-Wert)
 * 
 * @author rnb
 * 
 */
public class PositionSpotContainer {
	/**
	 * PositionIndex referenziert auf eine bestimmte Position
	 */
	private int positionIndex = 0;
	/**
	 * Value enthält den berechneten Wert des Euklidischen Distanz Algorithmus
	 */
	private double value = 0.00d;

	/**
	 * Initialisierung
	 * 
	 * @param positionIndex
	 *            Index einer Position
	 * @param value
	 *            Euklidischer Distanz Wert
	 */
	public PositionSpotContainer(final int positionIndex, final double value) {
		this.positionIndex = positionIndex;
		this.value = value;
	}

	/**
	 * @return Index der Position
	 */
	public int getPositionIndex() {
		return this.positionIndex;
	}

	/**
	 * @return Wert des Berechneten Algorithmus
	 */
	public double getValue() {
		return this.value;
	}
}
