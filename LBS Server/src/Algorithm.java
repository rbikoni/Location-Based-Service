import java.util.ArrayList;

/**
 * Implementiert einen Konkreten Algorithmus
 * 
 * @author rnb
 * 
 * 
 */
public class Algorithm {
	/**
	 * Referenz auf die verwendete Strategie
	 */
	private Strategy strategy = null;

	/**
	 * @param strategy
	 *            verwendete Strategie
	 */
	public Algorithm(final Strategy strategy) {
		this.strategy = strategy;
	}

	/**
	 * Ausführen des Algorithmuses.
	 * 
	 * @param currentPosition
	 *            aktuelle Position
	 * @param referencePositions
	 *            reference Positionen
	 * @return ReferenzPosition
	 */
	public PositionInfos executeAlgorithm(final PositionInfos currentPosition,
			final ArrayList<PositionInfos> referencePositions) {
		return this.strategy.calculateReferencePosition(currentPosition,
				referencePositions);
	}
}
