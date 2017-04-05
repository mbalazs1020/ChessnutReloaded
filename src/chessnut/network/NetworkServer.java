package chessnut.network;

// �ltal�nos importok
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import chess.Board;
// Projekt specifikus importok
import chessnut.ILogic;
import chessnut.IPlayer;
import chessnut.logic.ClickData;


/**
 * Szerver oldali h�l�zatkezel� oszt�ly
 */
public class NetworkServer extends Network implements IPlayer
{
	// Konstansok
	/**  Port sz�ma   */
	private static final int port = 10007;
	
	/**  Ezen �rj�k el a gamelogic-ot   */
	private ILogic gameLogic;
	
	// H�l�zat r�szei
	/**  Szerver socker   */
	private ServerSocket serverSocket = null;
	
	/**  Kliens socket   */
	private Socket clientSocket = null;
	
	/**  Kimen� stream   */
	private ObjectOutputStream out = null;
	
	/**  Bej�v� stream   */
	private ObjectInputStream in = null;
	
	// F�ggv�nyek
	

	/**  Default konstruktor   */
	public NetworkServer(){}
	

	/**
	 * ILogic referenci�val l�trehoz� konstruktor
	 * @param logic: azon objektum, amelyre a referencia be lesz �ll�tva
	 */
	public NetworkServer(ILogic logic)
	{
		this.gameLogic = logic;
	}
	

	/**
	 * ILogic referencia be�ll�t�sa
	 * @param gameLogic: azon objektum, amelyre a referencia be lesz �ll�tva
	 */
	public void setGameLogic(ILogic gameLogic)
	{
		this.gameLogic = gameLogic;
	}
		
	
	/**
	 * H�l�zati kapcsolat �llapot�nak lek�r�se
	 * @return true, ha van kapcsolat. false, ha nincs
	 */
	public boolean isConnected()
	{
		if(clientSocket != null)
		{
			return clientSocket.isConnected();
		}
		return false;
	}
	
	
	/**
	 * Fogad� thread
	 */
	private class PlayerActionReceiver implements Runnable
	{	
		public void run()
		{
			try
			{
				System.out.println("Waiting for Client");
				// Kliensre v�rakoz�s (blokkol)
				clientSocket = serverSocket.accept();
				// Kapcsolat l�trej�tt
				System.out.println("Client connected.");
			} catch (IOException e)
			{
				System.err.println("Error while waiting for client connection.");
				disconnect();
				return;
			}

			try
			{
				// Stream-ek l�trehoz�sa
				out = new ObjectOutputStream(clientSocket.getOutputStream());
				in = new ObjectInputStream(clientSocket.getInputStream());
				out.flush();
			} catch (IOException e)
			{
				System.err.println("Error while creating streams.");
				disconnect();
				return;
			}

			try
			{
				// �rkez� objektumok itt j�nnek be
				while (true)
				{
					// �zenet bej�n
					Object received = (Object) in.readObject();
					
					// Ha click �zenet
					if(received instanceof ClickData)
					{
						// Tov�bbadom a kezel�nek
						if(gameLogic != null)
						{
							gameLogic.click((ClickData)received);
						}
					}
					
					// Ha nem tudom milyen �zenet
					else
					{
						System.err.println("Error: Ismeretlen dolgot kaptam a TCP-n: \n" + received);
					}
				}
			} catch (Exception ex)
			{
				System.out.println(ex.getMessage());
				System.err.println("Client disconnected!");
				System.exit(0);
			} finally
			{
				// Ha v�letlen�l kiugrunk ebb�l, akkor bont�s
				disconnect();
			}
		}
	}
	
	
	/**
	 * Szerverg�p l�trehoz�sa, kliensre v�rakoz�s megnyit�sa
	 * @param ipAddr: nem haszn�lt param�ter, csup�n az �r�kl�d�s miatt van m�g itt
	 */
	@Override
	public void connect(String ipAddr)
	{
		disconnect();
		try
		{
			// Ki�rom az IP-met sz�vegbe
			String IPAddr = InetAddress.getLocalHost().getHostAddress();
			System.out.println("Starting server on local IP: " + IPAddr );
			
			// Server socket l�trehoz�s
			serverSocket = new ServerSocket(port);
			// Fogad� thread l�trehoz�s �s ind�t�s
			Thread rec = new Thread(new PlayerActionReceiver());
			rec.start();
		} catch (IOException e)
		{
			System.err.println("Error while creating PlayerActionReceiver thread.");
		}
	}
	

	/**
	 * Lekapcsol�d�s
	 */
	@Override
	void disconnect()
	{
		try
		{
			// Becsukok mindent, ami m�g egy�ltal�n van
			if (out != null)
				out.close();
			if (in != null)
				in.close();
			if (clientSocket != null)
				clientSocket.close();
			if (serverSocket != null)
				serverSocket.close();
		} catch (IOException ex)
		{
			System.out.println("Exception at disconnect: " + ex.getMessage());
		}
	}
	
	
	/**
	 * setChessboard �zenet k�ld�se a kliensnek
	 * @param chessboard: az �tk�ld�tt sakkt�bla
	 */
	@Override
	public void setChessboard(Board chessboard)
	{
		// Ha nincs meg az output stream, akkor gond van
		if (out == null)
		{
			System.out.println("Could not send: output stream is not open.");
			return;
		}
		// K�ld�s
		try
		{
			Board chessboardToSend = new Board(chessboard);
			out.writeObject(chessboardToSend );
			out.flush();
		} catch (IOException ex)
		{
			System.err.println("Error while sending message to client: " + ex.getMessage());
		}
	}
	
	// Out of scope
	@Override
	void sendMsg(String msg)
	{
		return;
	}
}
