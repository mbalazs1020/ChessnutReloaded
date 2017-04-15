package robot;

import javax.management.RuntimeErrorException;

import chess.Board;
import chess.Move;
import chess.pieces.Piece;
import chessnut.ILogic;
import chessnut.IPlayer;
import chessnut.logic.ClickData;

/*
 * Robot handler kiegészítése arra, ha a robot a játékos
 */
public class RobotPlayer implements IPlayer 
{
	// Kacsolat a robotos szoftverrel
	RobotHandler robot;
	ILogic logic;
	boolean playerColor = Piece.BLACK; // MINDIG SÖTÉT LEGYEN A ROBOT, NE BONYOLÍTSUNK MÉG
	
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
	
	private int convertRankCharToInt(char c)
	{
		if( c == 'A' ) return 0;
		if( c == 'B' ) return 1;
		if( c == 'C' ) return 2;
		if( c == 'D' ) return 3;
		if( c == 'E' ) return 4;
		if( c == 'F' ) return 5;
		if( c == 'G' ) return 6;
		if( c == 'H' ) return 7;
		else return -1;
	}
	
	private int convertFileCharToInt(char c)
	{
		if( c == '1' ) return 0;
		if( c == '2' ) return 1;
		if( c == '3' ) return 2;
		if( c == '4' ) return 3;
		if( c == '5' ) return 4;
		if( c == '6' ) return 5;
		if( c == '7' ) return 6;
		if( c == '8' ) return 7;
		else return -1;
	}
	
	public void handleReq( String msg )
	{
		char movSrcChar1 = msg.charAt(10);
		char movSrcChar2 = msg.charAt(11);
		char movDstChar1 = msg.charAt(13);
		char movDstChar2 = msg.charAt(14);
		System.out.println("Megjött a request: " + movSrcChar1 + movSrcChar2 + " " + movDstChar1 + movDstChar2 );

		ClickData pos1 = new ClickData( convertRankCharToInt(movSrcChar1),
										convertFileCharToInt(movSrcChar2),
										Piece.BLACK);
		
		ClickData pos2 = new ClickData( convertRankCharToInt(movDstChar1),
										convertFileCharToInt(movDstChar2),
										Piece.BLACK);
		
		if( logic == null )
			throw new RuntimeErrorException(new Error("ELFELEJTETTED BEÁLLÍTANI A LOGIC-OT A ROBOT PLAYERNER"));
		if( !logic.click(pos1) )
			throw new RuntimeErrorException(new Error("ROSSZ LÉPÉS"));
		if( !logic.click(pos2) )
			throw new RuntimeErrorException(new Error("ROSSZ LÉPÉS"));
	}
}
