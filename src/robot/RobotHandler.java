package robot;

import javax.management.RuntimeErrorException;

import chess.Move;
import chessnut.network.RobotConnectionServer;

public class RobotHandler
{
	// Lehet, hogy éppen játszik is a robot, akkor itt van a játékra képes kiegészítõje
	RobotPlayer player = null;
	
	// Ez lesz az épp kiküldött és visszavárt üzenet
	String msgSent;
	String msgAckWaiting;
	boolean isMsgAcked = false;
	
	// Kacsolat a robotos szoftverrel
	RobotConnectionServer robot;
	
	
	public void setRobotPlayer( RobotPlayer player )
	{
		this.player = player;
	}
	
	// Kapcsolat létrehozása
	public RobotHandler() throws InterruptedException
	{
		robot = new RobotConnectionServer();
		robot.connect(null);
		robot.setRobotInterface(this);
	}
	
	// Kapcsolat fennáll-e
	public boolean isConnected(  )
	{
		return robot.isConnected();
	}
	
	// Lépés átküldése a robotnak obszerver módban
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
		// Ha tartalmazza a várt ACK-t, akkor elfogadom
		if( msgReceived.contains(msgAckWaiting) && ( !isMsgAcked ) ) 
			isMsgAcked = true;
		else if ( msgReceived.contains("REQ") )
		{
			
		}
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
