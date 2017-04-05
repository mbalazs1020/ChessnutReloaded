package chessnut.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import robot.RobotInterface;

public class RobotConnectionServer extends Network
{
	// Robot Observer referencia
	RobotInterface robotHandler;
	
	// Konstansok
	/**  Port száma   */
	private static final int port = 50003;
	
	// Hálózat részei
	/**  Szerver socker   */
	private ServerSocket serverSocket = null;
	
	/**  Kliens socket   */
	private Socket clientSocket = null;
	
	/**  Kimenõ stream   */
	private DataOutputStream out = null;
	
	/**  Bejövõ stream   */
	private BufferedReader in = null;
	
	
	
	// Függvények
	
	/**  Default konstruktor   */
	public RobotConnectionServer(){}
	
	
	public void setRobotInterface( RobotInterface o )
	{
		this.robotHandler = o;
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
			Thread rec = new Thread(new RobotReceiverThread());
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
	public void disconnect()
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
	 * Üzenetküldés a szervernek
	 */
	@Override
	public void sendMsg(String msg)
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
			out.writeBytes(msg + "\n");
			out.flush();
		} catch (IOException ex)
		{
			System.err.println("Error while sending message to server.");
		}
	}
	
	
	private class RobotReceiverThread implements Runnable
	{
		public void run() 
		{
			try
			{
				System.out.println("Robot csatlakozásra várás....");
				// Kliensre várakozás (blokkol)
				clientSocket = serverSocket.accept();
				// Kapcsolat létrejött
				System.out.println("Robot csatlakozva.");
			} catch (IOException e)
			{
				System.err.println("Error while waiting for client connection.");
				disconnect();
				return;
			}

			try
			{
				// Stream-ek létrehozása
				out = new DataOutputStream(clientSocket.getOutputStream());
				in = new BufferedReader( new InputStreamReader(clientSocket.getInputStream() ));
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
					String received = null;
					received = (String) in.readLine();
					
					if( received != null )
					{
						if( robotHandler != null )
						{
							robotHandler.handleIncomingMessage(received);
						}
					}
				}
			} catch (Exception ex)
			{
				//ex.printStackTrace();
				System.err.println("Client disconnected!");
				System.exit(0);
			} finally
			{
				// Ha véletlenül kiugrunk ebbõl, akkor bontás
				disconnect();
			}
		}
	}
	
}
