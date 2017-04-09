package robot;

import javax.management.RuntimeErrorException;

import chess.Move;
import chessnut.network.RobotConnectionServer;

public class RobotHandler
{
	// Lehet, hogy �ppen j�tszik is a robot, akkor itt van a j�t�kra k�pes kieg�sz�t�je
	RobotPlayer player = null;
	
	// Ez lesz az �pp kik�ld�tt �s visszav�rt �zenet
	String msgSent;
	String msgAckWaiting;
	boolean isMsgAcked = false;
	
	// Kacsolat a robotos szoftverrel
	RobotConnectionServer robot;
	
	
	public void setRobotPlayer( RobotPlayer player )
	{
		this.player = player;
	}
	
	// Kapcsolat l�trehoz�sa
	public RobotHandler() throws InterruptedException
	{
		robot = new RobotConnectionServer();
		robot.connect(null);
		robot.setRobotInterface(this);
	}
	
	// Kapcsolat fenn�ll-e
	public boolean isConnected(  )
	{
		return robot.isConnected();
	}
	
	// L�p�s �tk�ld�se a robotnak obszerver m�dban
	public void sendMoveReq( Move m, boolean hit ) 
	{
		// �sszerakom az �zenetet
		msgSent = "START REQ ";
		msgSent += m.toString();
		if( hit )
			msgSent += " HIT";
		else
			msgSent += " ---";
		
		msgSent += " END";
		
		System.out.println("K�ld�tt �zenet: " + msgSent);
		robot.sendMsg(msgSent);
		
		// ACK-t erre v�rom:
		msgAckWaiting = new String(msgSent.substring(0, 6));
		msgAckWaiting += "ACK";
		msgAckWaiting += msgSent.substring(9, 15);
	}
	
	// Robott�l j�tt �zenet elkap�sa
	public void handleIncomingMessage( String msgReceived )
	{
		// Ha tartalmazza a v�rt ACK-t, akkor elfogadom
		if( msgReceived.contains(msgAckWaiting) && ( !isMsgAcked ) ) 
			isMsgAcked = true;
		else if ( msgReceived.contains("REQ") )
		{
			
		}
		else
			throw new RuntimeErrorException(new Error("Le�llok, mert nem v�rt �zenet j�tt! :("));
	}
	
	// Visszaj�tt-e helyesen az �zenet
	public boolean isMsgAcked()
	{
		if( isMsgAcked )
		{
			isMsgAcked = false; // Kezd�dik �jra
			return true;
		}
		return false;
	}
	
}
