/**
 * In der Datenbank: Eintrag in Positions<br/>
 * Speichert die Spotinformationen die im Trac definiert sind. Die Klasse
 * enthält eine Sammlung von Getter und Settern zu den einzelnen Attributen.
 * 
 * @author rnb
 */
public class Information {
	/**
	 * x-Koordinate in Meter
	 */
	private double x;
	/**
	 * y-Koordinate in Meter
	 */
	private double y;
	/**
	 * z-Koordinate in Meter
	 */
	private double z;
	/**
	 * Beschreibt die Position
	 */
	private String Description;

	/**
	 * Drehpunkt
	 */
	private int Angle;

	/**
	 * Position_Id
	 */
	private int Id;

	/**
	 * Initialisierung der Variablen
	 */
	public Information() {
		this.x = this.y = this.z = 0.0;
		this.Description = " "; // Muss " " sein wegen XML! Leere Node =>
								// Exception
		this.Id = this.Angle = 0;
	}

	/**
	 * 
	 * @param id
	 *            Position ID
	 * @param x
	 *            Koordinate in Meter
	 * @param y
	 *            Koordinate in Meter
	 * @param z
	 *            Koordinate in Meter
	 * @param Description
	 *            Beschreibung der Position
	 * @param Angle
	 *            Drehpunkt der Position
	 */
	public Information(final int id, final double x, final double y,
			final double z, final String Description, final int Angle) {
		this.Id = id;
		this.Angle = Angle;
		this.x = x;
		this.y = y;
		this.z = z;
		this.Description = Description;
	}

	/**
	 * @return x Koordinate
	 */
	public double getX() {
		return this.x;
	}

	/**
	 * @return y Koordinate
	 */
	public double getY() {
		return this.y;
	}

	/**
	 * @return z Koordinate
	 */
	public double getZ() {
		return this.z;
	}

	/**
	 * @return Beschreibung der Position
	 */
	public String getDescription() {
		return this.Description;
	}

	/**
	 * @return Drehpunkt der Position
	 */
	public int getAngle() {
		return this.Angle;
	}

	/**
	 * @return Position ID
	 */
	public int getId() {
		return this.Id;
	}

	/**
	 * @param x
	 *            Koordinate
	 */
	public void setX(final double x) {
		this.x = x;
	}

	/**
	 * @param y
	 *            Koordinate
	 */
	public void setY(final double y) {
		this.y = y;
	}

	/**
	 * @param z
	 *            Kooridnate
	 */
	public void setZ(final double z) {
		this.z = z;
	}

	/**
	 * @param Description
	 */
	public void setDescription(final String Description) {
		this.Description = Description;
	}

	/**
	 * @param Angle
	 */
	public void setAngle(final int Angle) {
		this.Angle = Angle;
	}

	/**
	 * @param id
	 *            Positions ID
	 */
	public void setId(final int id) {
		this.Id = id;
	}
}
