package chessnut.network;

/**
 * Hálózatkezelés absztrakt osztálya.
 * Ebbõl származik le a kliens és a szerver oldali hálózatkezelõ osztály
 */
public abstract class Network
{
    /**  Kapcsolódás   */
	abstract void connect(String ipAddr);
	
	/**  Kapcsolat bontás  */
	abstract void disconnect();
	
	/**  Üzenet küldés */
	abstract void sendMsg(String msg);
}
