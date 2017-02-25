package chessnut.gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * IP kérõ ablak osztály.
 */
public class InputIPAddrDialog extends JFrame
{
	/**  Magicnumber a sorosításhoz   */
	private static final long serialVersionUID = 1532472210101010188L;
	
	/** Megadott IP cím */
	String IPAddr;  //!< IP címet ebbe fogjuk kapni
	

	/**
	 * Konstruktor
	 */
	public InputIPAddrDialog()
	{
		// Alapvetõ beállítások
		/** Ablak alapvetõ beállításai */
		super();                                 // Létrejön az ablak
		setSize(300, 140);                       // Ablakméret beállítása
		setDefaultCloseOperation(EXIT_ON_CLOSE); // Alapértelmezett kilépés
		setLayout(null);                         // Layout
		
		// Szövegdoboz beadja a sztringet
		/** Sztring bekérése */
		IPAddr = (String) JOptionPane.showInputDialog(this,
				"Server IP:\n",
				"", JOptionPane.PLAIN_MESSAGE);

		setVisible(true);               // Láthatóvá teszem az ablakot
	}
	
	/** 
	 * IP cím lekérdezése
	 * @return IPAddr: visszaadja a beírt IP címet 
	 */
	String getIp()
	{
		// Ha már van mit visszaadni, akkor eltüntetem ezt az ablakot
		if(IPAddr != null)
		{
			this.setVisible(false);
		}
		
		return this.IPAddr;
	}
}
