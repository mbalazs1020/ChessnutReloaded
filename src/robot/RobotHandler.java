package robot;

import javax.management.RuntimeErrorException;

import chess.Move;
import chessnut.network.RobotConnectionServer;

/**
 * Robot �zenetkezel�
 * @author Bal�zs
 *
 */
public class RobotHandler
{
	// Lehet, hogy �ppen j�tszik is a robot, akkor itt van a j�t�kra k�pes kieg�sz�t�je
	RobotPlayer player = null;
	
	// Ez lesz az �pp kik�ld�tt �s visszav�rt �zenet
	String msgSent;
	String msgAckWaiting;
	boolean isMsgAcked = false; // Ezzel jegyzem meg, hogy ACK-t v�rok elk�ld�k l�p�s REQ-re
	
	// Kacsolat a robotos szoftverrel
	RobotConnectionServer robot;
	
	
	// Robotj�t�kos referencia be�ll�t�sa
	public void setRobotPlayer( RobotPlayer player )
	{
		this.player = player;
	}
	
	// Konstruktor, amelyben a kapcsolat l�trehoz�sa is megval�sul
	public RobotHandler() throws InterruptedException
	{
		robot = new RobotConnectionServer();
	}
	
	private void setMyReferenceToRobotNetwork()
	{
		robot.setRobotHandler(this);
		System.out.println("Robot csatlakozva, referencia be�ll�tva");
	}
	
	public void connect( )
	{
		robot.connect(null);
		Runnable waitForConn = new Runnable()
		{
			public void run()
			{
				try{
					// V�rok, hogy csatlakozva legyen
					while( !robot.isConnected() )
					{
						Thread.sleep(1);
					}
					setMyReferenceToRobotNetwork(); // Ut�na be�ll�tom a referenci�t
				} catch (Exception exc)
				{
					exc.printStackTrace();
				}
			}
		};
		waitForConn.run();
	}
	
	// Kapcsolat fenn�ll-e
	public boolean isConnected(  )
	{
		return robot.isConnected();
	}
	
	// L�p�s request k�ld�se a robotnak
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
		if ( msgReceived.contains("REQ") )
		{ // Ha l�p�s request j�tt, akkor a robot j�t�kosk�nt szerepel, tov�bbadom a j�t�kos oszt�lynak
			if( this.player != null )
				this.player.handleReq(msgReceived);
		}
		// Ha tartalmazza a v�rt ACK-t, akkor elfogadom
		else if( msgReceived.contains(msgAckWaiting) && ( !isMsgAcked ) ) 
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
