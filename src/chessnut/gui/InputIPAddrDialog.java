package chessnut.gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * IP k�r� ablak oszt�ly.
 */
public class InputIPAddrDialog extends JFrame
{
	/**  Magicnumber a soros�t�shoz   */
	private static final long serialVersionUID = 1532472210101010188L;
	
	/** Megadott IP c�m */
	String IPAddr;  //!< IP c�met ebbe fogjuk kapni
	

	/**
	 * Konstruktor
	 */
	public InputIPAddrDialog()
	{
		// Alapvet� be�ll�t�sok
		/** Ablak alapvet� be�ll�t�sai */
		super();                                 // L�trej�n az ablak
		setSize(300, 140);                       // Ablakm�ret be�ll�t�sa
		setDefaultCloseOperation(EXIT_ON_CLOSE); // Alap�rtelmezett kil�p�s
		setLayout(null);                         // Layout
		
		// Sz�vegdoboz beadja a sztringet
		/** Sztring bek�r�se */
		IPAddr = (String) JOptionPane.showInputDialog(this,
				"Server IP:\n",
				"", JOptionPane.PLAIN_MESSAGE);

		setVisible(true);               // L�that�v� teszem az ablakot
	}
	
	/** 
	 * IP c�m lek�rdez�se
	 * @return IPAddr: visszaadja a be�rt IP c�met 
	 */
	String getIp()
	{
		// Ha m�r van mit visszaadni, akkor elt�ntetem ezt az ablakot
		if(IPAddr != null)
		{
			this.setVisible(false);
		}
		
		return this.IPAddr;
	}
}
