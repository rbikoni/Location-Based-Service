import java.util.ArrayList;

/**
 * Berechnet und implementiert Algorithmen zur Lokalisierung.
 * 
 * @author rnb
 */
public class Locator {
	/**
	 * Referenz der Implementierungsstrategie
	 */
	private Algorithm algorithm = null;

	/**
	 * Default-Konstruktor erstellt Euklidischen-Distanz-Algorithmus
	 */
	public Locator() {
		this.algorithm = new Algorithm(new EuclideanDistanceAlgorithm());
	}

	/**
	 * 
	 * @param algorithm
	 *            verwendeter Algorithmus
	 */
	public Locator(final Algorithm algorithm) {
		this.algorithm = algorithm;
	}

	/**
	 * Lokalisierung
	 * 
	 * @param currentPosition
	 *            aktuell gescannte Position
	 * @param referencePositions
	 *            mögliche Referenzpunkte der gescannten Position
	 * @return errechnete Referenzposition
	 */
	public PositionInfos getLocation(final PositionInfos currentPosition,
			final ArrayList<PositionInfos> referencePositions) {

		return this.algorithm.executeAlgorithm(currentPosition,
				referencePositions);
	}
}
