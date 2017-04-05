package chessnut.network;

//Általános importok
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

import chess.Board;
//Projekt specifikus importok
import chessnut.ILogic;
import chessnut.IPlayer;
import chessnut.logic.ClickData;


/**
 * Kliens oldali hálózatkezelõ osztály.
 */
public class NetworkClient extends Network implements ILogic
{
	// Konstansok
	/**  Port száma   */
	private static final int port = 10007;
	
	/**  GUI elérése   */
	private IPlayer gui;

	// Hálózat részei
	/**  Kliens socket   */
	private Socket socket = null;          
	
	/**  Kimenõ stream   */
	private ObjectOutputStream out = null; 
	
	/**  Bejövõ stream   */
	private ObjectInputStream in = null;   
	
	
	/**
	 * Default konstruktor
	 */
	public NetworkClient(){}
	
	/**
	 * Konstruktor: Létrehozható GUI alapján
	 * @param gui: amely gui be fog kerülni referenciaként a létrehozott objektumba
	 */
	NetworkClient(IPlayer gui)
	{
		this.gui = gui;
	}
	
	
	/**
	 * IPlayer referencia beállítása
	 * @param player: akire a referencia mutat
	 */
	public void setPlayer2(IPlayer player)
	{
		this.gui = player;
	}
	
	/**
	 * Hálózati kapcsolat állapotának lekérdezése
	 * @return true, ha kapcsolódva vagyunk. Ellenben false.
	 */
	public boolean isConnected()
	{
		if (socket != null)
		{
			return socket.isConnected();
		}
		return false;
	}
	
	/**
	 * Fogadó thread
	 */
	private class ServerNotificaionReceiver implements Runnable
	{
		public void run()
		{
			try
			{
				while (true)
				{
					// Objektum beérkezése
					Object received = (Object) in.readObject();
					
					// Ha setChessboard üzenet jött
					if(received instanceof Board )
					{
						// Meghívom a kezelõt:
						if(gui != null)
						{
							gui.setChessboard((Board)received );
						}
					}
					// Ha nem tudom mi jött
					else
					{
						System.err.println("Error: Nem tudom, mit kaptam a TCP-n: \n" + received);
					}
				}
			} catch (Exception ex)
			{
				System.out.println(ex.getMessage());
				System.err.println("Server disconnected!");
				System.exit(0);
			} finally
			{
				disconnect();
			}
		}
	}
	
	
	/**
	 * Kapcsolódás szerverhez
	 * @param ip: szerver IP címe
	 */
	@Override
	public void connect(String ip)
	{
		disconnect();
		try
		{
			// Szöveg
			System.out.println("Starting client.... ");
			
			// Socket létrehozása
			socket = new Socket(ip, port);

			// Objektum sztream-ek létrehozása
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			out.flush();

			// Thread indítása
			Thread rec = new Thread(new ServerNotificaionReceiver());
			rec.start();
		} catch (UnknownHostException e)
		{
			System.err.println("Error: host not found.");
		} catch (IOException e)
		{
			System.err.println("Couldn't connect to server: " + e.getMessage());
		}
	}
	
	
	/**
	 * Kapcsolat bontás
	 */
	@Override
	void disconnect()
	{
		try
		{
			// Becsukok mindent, ami még van
			if (out != null)
				out.close();
			if (in != null)
				in.close();
			if (socket != null)
				socket.close();
		} catch (IOException ex)
		{
			System.err.println("Error while disconnecting: " + ex.getMessage());
		}
	}
	

	/**
	 * click átküldése a szervernek
	 */
	@Override
	public void click( ClickData position )
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
			out.writeObject(position);
			out.flush();
		} catch (IOException ex)
		{
			System.err.println("Error while sending message to server.");
		}
	}
	
	// Out of scope
	@Override
	void sendMsg(String msg)
	{
		return;
	}
	
}
