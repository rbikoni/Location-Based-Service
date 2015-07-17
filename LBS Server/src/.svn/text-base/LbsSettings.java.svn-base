import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * LbsSettings stellt dem Benutzer mehrere Methoden zur Verfügung, in dem Er/Sie
 * mit der Hilfe einer GUI Datenbank, Map und Port des LbsServers auswählen
 * kann.
 * 
 * @author rnb
 * 
 */
public class LbsSettings implements ActionListener {
	/**
	 * Titel
	 */
	private final String settingsTitle = "Location Based KomInf Server";
	/**
	 * Default Pfad der Datenbank
	 */
	private final String defaultDatabasePath = "C:\\Study\\Sqlite3\\litters.db";
	/**
	 * Pfad der Datenbank
	 */
	private String databasePath = this.defaultDatabasePath;
	/**
	 * Default Pfad des Mapabbilds
	 */
	private final String defaultImagePath = "C:\\Study\\Sqlite3\\litters-keller.png";
	/**
	 * Pfad des Mapabbilds
	 */
	private String imagePath = this.defaultImagePath;
	/**
	 * Port
	 */
	private int port = 1234;

	/**
	 * Frame Instanz
	 */
	JDialog frame = new JDialog();

	/**
	 * Default Constructor
	 */
	public LbsSettings() {
		// Nothing to do
	}

	/**
	 * Zeigt das komplette Frame an
	 */
	public void show() {
		// --- Buttons werden instanziiert
		final JButton dbButton = new JButton("Database");
		final JButton imgButton = new JButton("Map");
		final JButton portButton = new JButton("Port");

		// Erkennsstring wird zugewiesen
		dbButton.setActionCommand("Database");
		imgButton.setActionCommand("Map");
		portButton.setActionCommand("Port");

		// ActionListener wird zugewiesen
		dbButton.addActionListener(this);
		imgButton.addActionListener(this);
		portButton.addActionListener(this);

		// Diverseres
		this.frame.setLayout(new FlowLayout());
		this.frame.setTitle(this.settingsTitle);
		this.frame.setSize(300, 75);

		// Komponenten werden dem Frame hinzugefügt
		this.frame.add(dbButton, BorderLayout.WEST);
		this.frame.add(imgButton, BorderLayout.CENTER);
		this.frame.add(portButton, BorderLayout.EAST);

		// Modaler Frame
		this.frame.setModal(true);

		// Sichtbarkeit
		this.frame.setVisible(true);
		dbButton.setVisible(true);
		imgButton.setVisible(true);
		portButton.setVisible(true);
	}

	@Override
	public void actionPerformed(final ActionEvent arg0) {
		if (arg0.getActionCommand().equals("Database")) {
			this.dbSettings();
		}

		if (arg0.getActionCommand().equals("Map")) {
			this.mapSettings();
		}

		if (arg0.getActionCommand().equals("Port")) {
			this.portSettings();
		}
	}

	/**
	 * portSettings öffnet einen modalen Dialog in dem der Benutzer eine
	 * Portnummer eingeben muss, der Dialog schließt nur, wenn der Benutzer eine
	 * valide Nummer eingegeben hat.
	 */
	private void portSettings() {
		do {
			try {
				/* Modaler Dialog */
				final String result = JOptionPane.showInputDialog(this.frame,
						"Portnummer", "Einstellungen - Portnummer (>100)",
						JOptionPane.OK_CANCEL_OPTION);
				/* Parsen der Eingabe */
				this.port = Integer.parseInt(result);
				if (this.port < 100) {
					throw new NumberFormatException();
				}
			} catch (final NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Ungültige Portnummer",
						"Settings - Error", JOptionPane.ERROR_MESSAGE);
			}
		} while (this.port < 100);
		/* ======================================= */
	}

	/**
	 * mapSettings öffnet einen FileDialog in dem der Benutzer den Pfad der
	 * Bilddatei der Referenzmap auswählt.
	 */
	private void mapSettings() {
		final JFileChooser jfc = new JFileChooser();
		final FileNameExtensionFilter extensions = new FileNameExtensionFilter(
				"Picture Image File", "jpg", "gif", "png");

		jfc.setFileFilter(extensions);
		final int ret = jfc.showOpenDialog(null);

		if (ret == JFileChooser.APPROVE_OPTION) {
			this.imagePath = jfc.getSelectedFile().getAbsolutePath();
		} else if (ret == JFileChooser.CANCEL_OPTION) {
			this.imagePath = this.defaultImagePath;
		}
	}

	/**
	 * dbSettings öffnet einen FileDialog in dem der Benutzer den Pfad der
	 * Datenbank auswählt.
	 */
	private void dbSettings() {
		final JFileChooser jfc = new JFileChooser();
		final FileNameExtensionFilter extensions = new FileNameExtensionFilter(
				"Database Files", "db");

		jfc.setFileFilter(extensions);
		final int ret = jfc.showOpenDialog(null);

		if (ret == JFileChooser.APPROVE_OPTION) {
			this.databasePath = jfc.getSelectedFile().getAbsolutePath();
		} else {
			this.databasePath = this.defaultDatabasePath;
		}
	}

	/**
	 * 
	 * @return Datenbankpfad
	 */
	public String getDatabasePath() {
		return this.databasePath;
	}

	/**
	 * 
	 * @return Imagepfad
	 */
	public String getMapPath() {
		return this.imagePath;
	}

	/**
	 * 
	 * @return Portnummer
	 */
	public int getPort() {
		return this.port;
	}

	/**
	 * Freigeben der Resourcen
	 */
	public void dispose() {
		this.frame.dispose();
	}
}
