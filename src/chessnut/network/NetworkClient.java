package chessnut.network;

//�ltal�nos importok
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
 * Kliens oldali h�l�zatkezel� oszt�ly.
 */
public class NetworkClient extends Network implements ILogic
{
	// Konstansok
	/**  Port sz�ma   */
	private static final int port = 10007;
	
	/**  GUI el�r�se   */
	private IPlayer gui;

	// H�l�zat r�szei
	/**  Kliens socket   */
	private Socket socket = null;          
	
	/**  Kimen� stream   */
	private ObjectOutputStream out = null; 
	
	/**  Bej�v� stream   */
	private ObjectInputStream in = null;   
	
	
	/**
	 * Default konstruktor
	 */
	public NetworkClient(){}
	
	/**
	 * Konstruktor: L�trehozhat� GUI alapj�n
	 * @param gui: amely gui be fog ker�lni referenciak�nt a l�trehozott objektumba
	 */
	NetworkClient(IPlayer gui)
	{
		this.gui = gui;
	}
	
	
	/**
	 * IPlayer referencia be�ll�t�sa
	 * @param player: akire a referencia mutat
	 */
	public void setPlayer2(IPlayer player)
	{
		this.gui = player;
	}
	
	/**
	 * H�l�zati kapcsolat �llapot�nak lek�rdez�se
	 * @return true, ha kapcsol�dva vagyunk. Ellenben false.
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
	 * Fogad� thread
	 */
	private class ServerNotificaionReceiver implements Runnable
	{
		public void run()
		{
			try
			{
				while (true)
				{
					// Objektum be�rkez�se
					Object received = (Object) in.readObject();
					
					// Ha setChessboard �zenet j�tt
					if(received instanceof Board )
					{
						// Megh�vom a kezel�t:
						if(gui != null)
						{
							gui.setChessboard((Board)received );
						}
					}
					// Ha nem tudom mi j�tt
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
	 * Kapcsol�d�s szerverhez
	 * @param ip: szerver IP c�me
	 */
	@Override
	public void connect(String ip)
	{
		disconnect();
		try
		{
			// Sz�veg
			System.out.println("Starting client.... ");
			
			// Socket l�trehoz�sa
			socket = new Socket(ip, port);

			// Objektum sztream-ek l�trehoz�sa
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			out.flush();

			// Thread ind�t�sa
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
	 * Kapcsolat bont�s
	 */
	@Override
	void disconnect()
	{
		try
		{
			// Becsukok mindent, ami m�g van
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
	 * click �tk�ld�se a szervernek
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
		// K�ld�s
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
