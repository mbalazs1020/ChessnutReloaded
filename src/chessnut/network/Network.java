package chessnut.network;

/**
 * H�l�zatkezel�s absztrakt oszt�lya.
 * Ebb�l sz�rmazik le a kliens �s a szerver oldali h�l�zatkezel� oszt�ly
 */
public abstract class Network
{
    /**  Kapcsol�d�s   */
	abstract void connect(String ipAddr);
	
	/**  Kapcsolat bont�s  */
	abstract void disconnect();
}
