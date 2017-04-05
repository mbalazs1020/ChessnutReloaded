package robot;

import javax.management.RuntimeErrorException;

import chess.Move;
import chessnut.network.RobotConnectionServer;

public class RobotObserver implements RobotInterface
{
	// Ez lesz az �pp kik�ld�tt �s visszav�rt �zenet
	String msgSent;
	String msgAckWaiting;
	boolean isMsgAcked = false;
	
	// Kacsolat a robotos szoftverrel
	RobotConnectionServer robot;
	
	// Kapcsolat l�trehoz�sa
	public RobotObserver() throws InterruptedException
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
		// Ha tartalmazza amit kell, akkor elfogadom
		if( msgReceived.contains(msgAckWaiting) ) 
			isMsgAcked = true;
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
