package robot;

import chess.Board;
import chess.Move;
import chessnut.ILogic;
import chessnut.IPlayer;

/*
 * Ez pont annyit tud, mint a robot handler, csak m�g j�tszik is.
 * Ez a k�pess�ge azt jelenti, hogy ha l�p�s req j�n be, azt kezeli az AI-hoz hasonl� �llapotg�ppel
 */
public class RobotPlayer implements IPlayer 
{
	// Kacsolat a robotos szoftverrel
	RobotHandler robot;
	ILogic logic;
	boolean playerColor = false; // MINDIG S�T�T LEGYEN A ROBOT, NE BONYOL�TSUNK M�G
	
	Move incomingMove = null;
	
	public boolean getPlayerColor()
	{
		return playerColor;
	}

	// Ha t�bl�t kapok, nem csin�lok semmit
	@Override
	public void setChessboard(Board chessboard)
	{
		// semmit
	}
	
	@Override
	public void setGameLogic(ILogic logic)
	{
		this.logic = logic;
	}
	
	public void setRobotHandler( RobotHandler h )
	{
		this.robot = h;
	}
	
	public void handleReq( String msg )
	{
		char movSrcChar1 = msg.charAt(10);
		char movSrcChar2 = msg.charAt(11);
		char movDstChar1 = msg.charAt(13);
		char movDstChar2 = msg.charAt(14);
		System.out.println("Megj�tt a request: " + movSrcChar1 + movSrcChar2 + " " + movDstChar1 + movDstChar2 );
		/// TODO feldolgozni
	}
}
