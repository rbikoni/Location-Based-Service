import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;

/**
 * GUI der Referenzkarte
 * 
 * @author rnb
 * 
 */
public class MapDesktop extends JDesktopPane {
	/**
	 * Diese HashMap verwaltet die PositionsId der Clienten, als Key wird der
	 * Benutzername und als Value der Referenzpunkt gespeichert.<br/>
	 * <b>HashMap<Benutzername,PositionId></b>
	 */
	private final HashMap<String, Integer> userMap = new HashMap<String, Integer>();

	/**
	 * Diese HashMap verwaltet assoziationen zwischen PositionId und
	 * Koordinatenpunkt. Als Key wird die PositionId des Referenzpunktes
	 * verwendetet, als Value der dazugehörige Kooridinatenpunkt
	 * <b>HashMap<PositionId, Koordinatenpunkt></b>
	 */
	private final HashMap<Integer, Point> referenceMap = this
			.createReferenceMap();

	/**
	 * Speichert den Pfad des Bildes
	 */
	private String imagePath = null;

	/**
	 * 
	 * @param imagePath
	 *            Pfad der Bildatei
	 */
	public MapDesktop(final String imagePath) {
		this.imagePath = imagePath;
	}

	@Override
	public void paintComponent(final Graphics g) {
		final ImageIcon img = new ImageIcon(this.imagePath);
		g.drawImage(img.getImage(), 0, 0, this);

		/*
		 * Mit Hilfe des eines Iterators wird die HashMap usermap durchlaufen
		 * und die Koordinaten der Benutzer neugezeichnet
		 */
		final Iterator<Entry<String, Integer>> it = this.userMap.entrySet()
				.iterator();

		int offset = 0;

		/* Punkte werden gezeichnet */
		while (it.hasNext()) {
			// Entry<Benutzername, PositionId>
			final Entry<String, Integer> entry = it.next();
			final String username = entry.getKey();
			final int positionId = entry.getValue();

			/*
			 * Die Variable kann ggf. den Wert Null enthalten, wenn der Punkt
			 * nicht mit einem Koordinatenpunkt auf der Karte assoziiert wurde
			 */
			final Point p = this.referenceMap.get(positionId);
			;
			/*
			 * Der Benutzer wird auf den Koordinaten in der Karte eingezeichnet,
			 * falls die Position zuvor auf dem Punkt definiert wurde
			 */
			if (p != null) {
				g.setColor(Color.red);
				g.drawString(username, p.x, p.y + (offset * 30));
				g.fillArc(p.x, p.y + (offset * 30), 25, 25, 60, 60);
				offset++;
			}
		}
	}

	/**
	 * Zeigt einen Benutzer in der Referenzkarte an
	 * 
	 * @param username
	 *            Benutzername
	 * @param referencePoint
	 *            Position des Benutzers
	 */
	public void addUserToMap(final String username,
			final PositionInfos referencePoint) {

		/*
		 * Benutz wird zur Karte hinzugefügt, falls ein vorheriges Element
		 * exisitiert wird das Element ersetzt.
		 */
		this.userMap.put(username, referencePoint.getPositionId());

		/* Karte wird neugezeichnet */
		this.repaint();
	}

	/**
	 * Erstellt eine Referenzkarte die die in der Offlinephase gescannten
	 * Referenzpunkte mit Koordinaten verbindent
	 * 
	 * @return eine Referenzkarte die die PositionIds mit Koordinaten verknüpft
	 */
	private HashMap<Integer, Point> createReferenceMap() {
		final HashMap<Integer, Point> map = new HashMap<Integer, Point>();

		/* --- Verknüpfungen werden erstellt */

		/* N242 Id: 1-36 */
		for (int id = 1; id < 37; ++id) {
			map.put(id, new Point(119, 132));
		}

		/* N240 Id: 37-72 */
		for (int id = 37; id < 73; ++id) {
			map.put(id, new Point(505, 367));
		}

		/* N241 Id: 73-108 */
		for (int id = 38; id < 109; ++id) {
			map.put(id, new Point(213, 373));
		}

		/* Aufenthaltsraum(N247) Id: 109-128 */
		for (int id = 109; id < 128; ++id) {
			map.put(id, new Point(820, 249));
		}
		return map;
	}
}
