import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.SynchronousQueue;

import javax.swing.JButton;
import javax.swing.JInternalFrame;

/**
 * ControllerFrame ist ein InternalFrame, das verschiedene Funktionen enthält um
 * den Benutzer interaktionen mithilfe der MessageQueue zu ermöglichen, wie z.B
 * das Stoppen des Servers.<br />
 * <br />
 * Um ein Kommando zu implementieren muss nachdem der Button implementiert
 * wurde, muss<br/>
 * <ul>
 * <li>Ein Objekt vom Typ Message instanziiert werden, das eine Nachricht
 * enthält, siehe {@link #actionPerformed(ActionEvent)}</li>
 * <li>Das Objekt muss der MessageQueue hinzugefügt werden {@link #messages}</li>
 * <li>Das Kommando muss in der JobQueue geparst werden</li>
 * </ul>
 * 
 * @author rnb
 * 
 */
public class ControllerFrame extends JInternalFrame implements ActionListener {
	/**
	 * Konstante Breite des InternalFrames
	 */
	private final int width = 200;
	/**
	 * Konstante Höhe des InternalFrames
	 */
	private final int height = 100;

	/**
	 * Refernz auf die MessageQueue
	 */
	private SynchronousQueue<Message> messages = null;

	/**
	 * Referenz auf den TerminateButton
	 */
	private JButton terminateButton = null;

	/**
	 * 
	 * @param messages
	 *            Referenz der verwendeten MessageQueue
	 */
	public ControllerFrame(final SynchronousQueue<Message> messages) {
		/* Jinternal Frame Settings */
		super("Controller", true, // resizable
				false, // closable
				false, // maximizable
				true);// iconifiable
		this.setSize(this.width, this.height);
		this.setVisible(true);

		/* Terminate Button */
		this.terminateButton = new JButton("Terminate");
		this.terminateButton.setActionCommand("Terminate");
		this.terminateButton.addActionListener(this);
		this.terminateButton.setVisible(true);
		this.add(this.terminateButton);

		/* MessageQueue */
		this.messages = messages;
	}

	@Override
	public void actionPerformed(final ActionEvent actionEvent) {

		/* Der Messagequeue wird das Kommando 'Terminate' angehängt */
		if (actionEvent.getActionCommand().equals("Terminate")) {
			try {
				this.messages.put(new Message(null, "Terminate"));
			} catch (final InterruptedException e) {
				/*
				 * Nachricht konnte nicht in die MessageQueue hinzugefügt,
				 * dieses Fehler lässt sich schwer bis gar nicht produzieren
				 */
			}
		}
	}

}
