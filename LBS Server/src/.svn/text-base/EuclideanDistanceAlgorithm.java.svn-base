import java.util.ArrayList;
import java.util.Arrays;

/**
 * Konkrete Implementierungsstrategie eines Algorithmus.
 * 
 * @author rnb
 */
public class EuclideanDistanceAlgorithm implements Strategy {

	@Override
	public PositionInfos calculateReferencePosition(
			final PositionInfos currentPosition,
			final ArrayList<PositionInfos> referencePositions) {
		// result = aktuelle Position
		// positions = Schnittpunkte der aktuellen Position mit der
		// Referenzposition

		// Anzahl der gefunden Referencepositionen
		final int len = referencePositions.size();
		int nearestPositionIndex = -1;

		// EKD-Werte der einzelnen Positionen
		final double[] results = new double[len];

		final ArrayList<PositionSpotContainer> pscList = new ArrayList<PositionSpotContainer>();

		for (int index = 0; index < len; ++index) {
			// Referenz auf eine Referenzposition
			final PositionInfos referencePosition = referencePositions
					.get(index);

			// EKD-Wert der Position
			double result = 0.00d;

			// Spots werden durchiteriert
			final int currentSpotsLen = currentPosition.getSize();
			final int referenceSpotsLen = referencePosition.getSize();

			for (int currentSpotIndex = 0; currentSpotIndex < currentSpotsLen; ++currentSpotIndex) {
				final PositionInfo currentSpot = currentPosition
						.getPosition(currentSpotIndex);

				// In der Schleife werden die BSSIDs des aktuellen und der
				// gefunden Referenzpositionen verglichen
				// Wenn die BSSID beider Spots identisch ist, kann der
				// Algorithmus ausgeführt werden
				for (int referenceSpotIndex = 0; referenceSpotIndex < referenceSpotsLen; ++referenceSpotIndex) {
					final PositionInfo referenceSpot = referencePosition
							.getPosition(referenceSpotIndex);

					// Vergleich der BSSID
					if (currentSpot.getBSSID().contains(
							referenceSpot.getBSSID())) {
						// Teil der Formel sqrt(pow(aSpot(med)-refSpot(med)+
						// ...))
						result += Math.pow(currentSpot.getMedian()
								- referenceSpot.getMedian(), 2);
					}
				}

				// Eukldischer Distanz Wert
				// result = Math.sqrt(result); // Zu früh (?)
			}
			result = Math.sqrt(result);
			// Der Wert wird in einem double Array gespeichert
			results[index] = result;

			// Ein Element des Typs PositionSpotContainer wird instanziiert um
			// den Index, Wert in einer Datenstruktur zu speichern
			pscList.add(new PositionSpotContainer(index, result));
		}

		// Das Array wird aufsteigenden sortiert
		Arrays.sort(results);

		/* ++++++++++ DEBUG ++++++++++ */
		for (int index = 0; index < results.length; ++index) {
			// Ausgabe des EKDS
		}

		//
		for (final PositionSpotContainer psc : pscList) {

			if (psc.getValue() == results[0]) {
				nearestPositionIndex = psc.getPositionIndex();
			}
		}

		/**
		 * nearestPositionInfo enthält die nahste Position
		 */
		final PositionInfos nearestPositionInfo = referencePositions
				.get(nearestPositionIndex);

		return nearestPositionInfo;
	}

}
