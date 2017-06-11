import java.awt.Dimension;
import javax.swing.JFrame;

public class ControllerGUI {
	/**
	 * @author Muhammad Ali 13156253
	 */
	public static void main(String[] args) {

		// initiate the creation of the GUI on the event dispatching thread
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainForm f = new MainForm();
				f.setSize(940, 600);
				f.setMinimumSize(new Dimension(940, 600));
				f.setVisible(true);
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		});
	}
}
