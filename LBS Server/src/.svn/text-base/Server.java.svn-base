/**
 * Start der GUI
 * 
 * @author rnb
 * 
 */
public class Server {
	/**
	 * @param args
	 */
	public static void main(final String[] args) {

		final LbsSettings settings = new LbsSettings();

		/* Modaler Dialog zum einstellen der Setting wird angezeigt */
		settings.show();
		settings.dispose();

		/* Werte der Eintstellungen werden bezogen */
		final int port = settings.getPort();
		final String databasePath = settings.getDatabasePath();
		final String imagePath = settings.getMapPath();

		System.out.println(imagePath);

		final LbsServer server = new LbsServer(port, databasePath, imagePath);

		/* LBS Server wird gestartet */
		server.start();
	}
}
