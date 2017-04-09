package robot;

import chess.Board;
import chess.Move;
import chessnut.ILogic;
import chessnut.IPlayer;

/*
 * Ez pont annyit tud, mint a robot handler, csak még játszik is.
 * Ez a képessége azt jelenti, hogy ha lépés req jön be, azt kezeli az AI-hoz hasonló állapotgéppel
 */
public class RobotPlayer implements IPlayer 
{
	// Kacsolat a robotos szoftverrel
	RobotHandler robot;
	ILogic logic;
	boolean playerColor = false; // MINDIG SÖTÉT LEGYEN A ROBOT, NE BONYOLÍTSUNK MÉG
	
	Move incomingMove = null;
	
	public boolean getPlayerColor()
	{
		return playerColor;
	}

	// Ha táblát kapok, nem csinálok semmit
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
		System.out.println("Megjött a request: " + movSrcChar1 + movSrcChar2 + " " + movDstChar1 + movDstChar2 );
		/// TODO feldolgozni
	}
}
