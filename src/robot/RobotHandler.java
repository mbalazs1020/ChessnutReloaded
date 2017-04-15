package robot;

import javax.management.RuntimeErrorException;

import chess.Move;
import chessnut.network.RobotConnectionServer;

/**
 * Robot üzenetkezelõ
 * @author Balázs
 *
 */
public class RobotHandler
{
	// Lehet, hogy éppen játszik is a robot, akkor itt van a játékra képes kiegészítõje
	RobotPlayer player = null;
	
	// Ez lesz az épp kiküldött és visszavárt üzenet
	String msgSent;
	String msgAckWaiting;
	boolean isMsgAcked = false; // Ezzel jegyzem meg, hogy ACK-t várok elküldök lépés REQ-re
	
	// Kacsolat a robotos szoftverrel
	RobotConnectionServer robot;
	
	
	// Robotjátékos referencia beállítása
	public void setRobotPlayer( RobotPlayer player )
	{
		this.player = player;
	}
	
	// Konstruktor, amelyben a kapcsolat létrehozása is megvalósul
	public RobotHandler() throws InterruptedException
	{
		robot = new RobotConnectionServer();
	}
	
	private void setMyReferenceToRobotNetwork()
	{
		robot.setRobotHandler(this);
		System.out.println("Robot csatlakozva, referencia beállítva");
	}
	
	public void connect( )
	{
		robot.connect(null);
		Runnable waitForConn = new Runnable()
		{
			public void run()
			{
				try{
					// Várok, hogy csatlakozva legyen
					while( !robot.isConnected() )
					{
						Thread.sleep(1);
					}
					setMyReferenceToRobotNetwork(); // Utána beállítom a referenciát
				} catch (Exception exc)
				{
					exc.printStackTrace();
				}
			}
		};
		waitForConn.run();
	}
	
	// Kapcsolat fennáll-e
	public boolean isConnected(  )
	{
		return robot.isConnected();
	}
	
	// Lépés request küldése a robotnak
	public void sendMoveReq( Move m, boolean hit ) 
	{
		// Összerakom az üzenetet
		msgSent = "START REQ ";
		msgSent += m.toString();
		if( hit )
			msgSent += " HIT";
		else
			msgSent += " ---";
		
		msgSent += " END";
		
		System.out.println("Küldött üzenet: " + msgSent);
		robot.sendMsg(msgSent);
		
		// ACK-t erre várom:
		msgAckWaiting = new String(msgSent.substring(0, 6));
		msgAckWaiting += "ACK";
		msgAckWaiting += msgSent.substring(9, 15);
	}
	
	// Robottól jött üzenet elkapása
	public void handleIncomingMessage( String msgReceived )
	{
		if ( msgReceived.contains("REQ") )
		{ // Ha lépés request jött, akkor a robot játékosként szerepel, továbbadom a játékos osztálynak
			if( this.player != null )
				this.player.handleReq(msgReceived);
		}
		// Ha tartalmazza a várt ACK-t, akkor elfogadom
		else if( msgReceived.contains(msgAckWaiting) && ( !isMsgAcked ) ) 
			isMsgAcked = true;
		else
			throw new RuntimeErrorException(new Error("Leállok, mert nem várt üzenet jött! :("));
	}
	
	// Visszajött-e helyesen az üzenet
	public boolean isMsgAcked()
	{
		if( isMsgAcked )
		{
			isMsgAcked = false; // Kezdõdik újra
			return true;
		}
		return false;
	}
	
}
