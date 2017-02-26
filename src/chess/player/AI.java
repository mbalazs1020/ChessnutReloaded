package chess.player;

import chess.Board;
import chess.Move;
import chessnut.ILogic;
import chessnut.IPlayer;
import chessnut.logic.ClickData;

public class AI implements IPlayer
{
	ILogic logic = null;
	Player myAIPlayer = null;
	Move move = null; // Ez lesz a lépésem
	MOVE_SM moveStateMachine = MOVE_SM.START;
	private Thread thread;     // Ebben a threadben futunk
	
	Board myChessboard = null;  // Sakktábla amit kaptam
	
	
	
	public AI( ILogic logic, boolean PlayerColor )
	{
		this.logic = logic;
		myAIPlayer = new AlphaBetaPlayer(PlayerColor, 2);  // Most mindig sötét vagyok, és alfabéta
		moveStateMachine = MOVE_SM.START;  // Állapotgép belövése		
	}
	
	// Táblát kaptam
	@Override
	public void setChessboard(Board chessboard)
	{		
		myChessboard = new Board(chessboard);
		thread = new Thread( new ChessboardHandlerThread() );
		thread.start();  // Külön threadben futtatom az állapotgépet
	}
	
	@Override
	public void setGameLogic(ILogic logic)
	{
		this.logic = logic;
	}
	
	// Lépés állapotgépe
	private enum MOVE_SM
	{
		START,
		CREATE_MOVE,
		FIRST_CLICK_SENT,
		SECOND_CLICK_SENT;
	}
	
	private class ChessboardHandlerThread implements Runnable
	{
		public void run()
		{
			try{
			
			// Állapotgép szerinti elágazás
			if( moveStateMachine == MOVE_SM.START )  // Most kezdõdik az egész
			{
				// Ha nem õ jön, ne kattintgasson
				if( myAIPlayer.getColor() != myChessboard.getNextPlayerToMove() )
				{
					return;
				}
			//	System.out.println("Mesterséges Intelligencia játékos feldolgozza a táblát...");
				move = null; // Még nincs lépés
				moveStateMachine = MOVE_SM.CREATE_MOVE; // Átrakom a döntés meghozása állapotba, aztán mehet tovább
				
				// Aludjon rá néhány milliszekundumot, mert így még kirajzolni se lehet a táblát, olyan gyors
				Thread.sleep(100);
			}
			
			if( moveStateMachine == MOVE_SM.SECOND_CLICK_SENT )
			{
				// Ha már nem én vagyok soron az új táblán, akkor jól végeztem dolgomat megyek a startra
				if( myChessboard.getNextPlayerToMove() != myAIPlayer.getColor() )
				{
					moveStateMachine = MOVE_SM.START;
				}
				else
				{
					move = null; // Még nincs lépés
					moveStateMachine = MOVE_SM.CREATE_MOVE; // Kénytelen leszek új lépést kreálni, mert az elõzõ szar volt
				}
			}
			
			if( moveStateMachine == MOVE_SM.FIRST_CLICK_SENT )
			{
				// Megnézem, hogy kijelölték-e a mezõt az új táblán. Ez azt jelenti, hogy elfogadták
				if( myChessboard.getTile(move.getX1(), move.getY1()).getPiece().isSelected() )
				{
					// Elküldöm a második lépést, és megyek tovább abba az állapotba
					moveStateMachine = MOVE_SM.SECOND_CLICK_SENT; 
				//	System.out.println("AI második kattintását beküldi.");
					logic.click( new ClickData(move.getX2(), move.getY2(), myAIPlayer.getColor()) );
				}
				else
				{
					move = null; // Még nincs lépés
					moveStateMachine = MOVE_SM.CREATE_MOVE; // Egyébként új lépést kell kitalálnom
				}
			}
			
			if( moveStateMachine == MOVE_SM.CREATE_MOVE ) // DÖntés meghozása, ez itt van a végén, mert bármelyik másik állapotból itt folytathatom
			{
				move = new Move( myAIPlayer.getNextMove(myChessboard));
				if(move == null && myChessboard.isCheck(myAIPlayer.getColor())) // sakk van, és mozogni se tudok: sakk matt van nekem :(
				{
					String loser = myChessboard.getNextPlayerToMove()? "Világos" : "Sötét";
					System.out.println(loser + "játékos vesztett, sakk-matt.");
					return;
					
				}
				if(move == null) // no check but can't move
				{
					System.out.println("Patt helyzet, nincs több lépés");
					return;
				}
				
				// Elsõ klikk elküldése
			//	System.out.println("AI elsõ kattintását beküldi.");
				moveStateMachine = MOVE_SM.FIRST_CLICK_SENT; // Állapotgép állítása
				logic.click( new ClickData(move.getX1(), move.getY1(), myAIPlayer.getColor()) );
				
			}
			
			}catch(Exception ex)
			{
				System.out.println("JÁTÉK VÉGE");
			}
		}
		
	}
 
}
