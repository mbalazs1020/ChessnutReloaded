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
	/**  Port sz�ma   */
	private static final int port = 50003;
	
	// H�l�zat r�szei
	/**  Szerver socker   */
	private ServerSocket serverSocket = null;
	
	/**  Kliens socket   */
	private Socket clientSocket = null;
	
	/**  Kimen� stream   */
	private DataOutputStream out = null;
	
	/**  Bej�v� stream   */
	private BufferedReader in = null;
	
	
	
	// F�ggv�nyek
	
	/**  Default konstruktor   */
	public RobotConnectionServer(){}
	
	
	public void setRobotInterface( RobotInterface o )
	{
		this.robotHandler = o;
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
			Thread rec = new Thread(new RobotReceiverThread());
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
	public void disconnect()
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
	 * �zenetk�ld�s a szervernek
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
		// K�ld�s
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
				System.out.println("Robot csatlakoz�sra v�r�s....");
				// Kliensre v�rakoz�s (blokkol)
				clientSocket = serverSocket.accept();
				// Kapcsolat l�trej�tt
				System.out.println("Robot csatlakozva.");
			} catch (IOException e)
			{
				System.err.println("Error while waiting for client connection.");
				disconnect();
				return;
			}

			try
			{
				// Stream-ek l�trehoz�sa
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
				// �rkez� objektumok itt j�nnek be
				while (true)
				{
					// �zenet bej�n
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
				// Ha v�letlen�l kiugrunk ebb�l, akkor bont�s
				disconnect();
			}
		}
	}
	
}
