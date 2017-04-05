package chessnut.network;

// Általános importok
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
 * Szerver oldali hálózatkezelõ osztály
 */
public class NetworkServer extends Network implements IPlayer
{
	// Konstansok
	/**  Port száma   */
	private static final int port = 10007;
	
	/**  Ezen érjük el a gamelogic-ot   */
	private ILogic gameLogic;
	
	// Hálózat részei
	/**  Szerver socker   */
	private ServerSocket serverSocket = null;
	
	/**  Kliens socket   */
	private Socket clientSocket = null;
	
	/**  Kimenõ stream   */
	private ObjectOutputStream out = null;
	
	/**  Bejövõ stream   */
	private ObjectInputStream in = null;
	
	// Függvények
	

	/**  Default konstruktor   */
	public NetworkServer(){}
	

	/**
	 * ILogic referenciával létrehozó konstruktor
	 * @param logic: azon objektum, amelyre a referencia be lesz állítva
	 */
	public NetworkServer(ILogic logic)
	{
		this.gameLogic = logic;
	}
	

	/**
	 * ILogic referencia beállítása
	 * @param gameLogic: azon objektum, amelyre a referencia be lesz állítva
	 */
	public void setGameLogic(ILogic gameLogic)
	{
		this.gameLogic = gameLogic;
	}
		
	
	/**
	 * Hálózati kapcsolat állapotának lekérése
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
	 * Fogadó thread
	 */
	private class PlayerActionReceiver implements Runnable
	{	
		public void run()
		{
			try
			{
				System.out.println("Waiting for Client");
				// Kliensre várakozás (blokkol)
				clientSocket = serverSocket.accept();
				// Kapcsolat létrejött
				System.out.println("Client connected.");
			} catch (IOException e)
			{
				System.err.println("Error while waiting for client connection.");
				disconnect();
				return;
			}

			try
			{
				// Stream-ek létrehozása
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
				// Érkezõ objektumok itt jönnek be
				while (true)
				{
					// Üzenet bejön
					Object received = (Object) in.readObject();
					
					// Ha click üzenet
					if(received instanceof ClickData)
					{
						// Továbbadom a kezelõnek
						if(gameLogic != null)
						{
							gameLogic.click((ClickData)received);
						}
					}
					
					// Ha nem tudom milyen üzenet
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
				// Ha véletlenül kiugrunk ebbõl, akkor bontás
				disconnect();
			}
		}
	}
	
	
	/**
	 * Szervergép létrehozása, kliensre várakozás megnyitása
	 * @param ipAddr: nem használt paraméter, csupán az öröklõdés miatt van még itt
	 */
	@Override
	public void connect(String ipAddr)
	{
		disconnect();
		try
		{
			// Kiírom az IP-met szövegbe
			String IPAddr = InetAddress.getLocalHost().getHostAddress();
			System.out.println("Starting server on local IP: " + IPAddr );
			
			// Server socket létrehozás
			serverSocket = new ServerSocket(port);
			// Fogadó thread létrehozás és indítás
			Thread rec = new Thread(new PlayerActionReceiver());
			rec.start();
		} catch (IOException e)
		{
			System.err.println("Error while creating PlayerActionReceiver thread.");
		}
	}
	

	/**
	 * Lekapcsolódás
	 */
	@Override
	void disconnect()
	{
		try
		{
			// Becsukok mindent, ami még egyáltalán van
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
	 * setChessboard üzenet küldése a kliensnek
	 * @param chessboard: az átküldött sakktábla
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
		// Küldés
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
