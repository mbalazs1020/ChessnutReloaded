package chess.player;

import chess.Board;
import chess.Move;
import chess.pieces.Piece;
import chessnut.ILogic;
import chessnut.IPlayer;
import chessnut.logic.ClickData;

public class AI implements IPlayer
{
	ILogic logic = null;
	Player myAIPlayer = null;
	Move move = null; // Ez lesz a lépésem
	MOVE_SM moveStateMachine = MOVE_SM.START;
	
	
	public AI( ILogic logic )
	{
		this.logic = logic;
		myAIPlayer = new AlphaBetaPlayer(Piece.BLACK, 2);  // Most mindig sötét vagyok, és alfabéta
		moveStateMachine = MOVE_SM.START;  // Állapotgép belövése
	}
	
	// Táblát kaptam
	@Override
	public void setChessboard(Board chessboard)
	{		
		// Állapotgép szerinti elágazás
		if( moveStateMachine == MOVE_SM.START )  // Most kezdõdik az egész
		{
			// Ha nem õ jön, ne kattintgasson
			if( myAIPlayer.getColor() != chessboard.getNextPlayerToMove() )
			{
				return;
			}
		//	System.out.println("Mesterséges Intelligencia játékos feldolgozza a táblát...");
			move = null; // Még nincs lépés
			moveStateMachine = MOVE_SM.CREATE_MOVE; // Átrakom a döntés meghozása állapotba, aztán mehet tovább
		}
		
		if( moveStateMachine == MOVE_SM.SECOND_CLICK_SENT )
		{
			// Ha már nem én vagyok soron az új táblán, akkor jól végeztem dolgomat megyek a startra
			if( chessboard.getNextPlayerToMove() != myAIPlayer.getColor() )
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
			if( chessboard.getTile(move.getX1(), move.getY1()).getPiece().isSelected() )
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
			move = new Move( myAIPlayer.getNextMove(chessboard));
			if(move == null && chessboard.isCheck(myAIPlayer.getColor())) // sakk van, és mozogni se tudok: sakk matt van nekem :(
			{
				String loser = chessboard.getNextPlayerToMove()? "Világos" : "Sötét";
				System.out.println(loser + "játékos vesztett, sakk-matt.");
			}
			if(move == null) // no check but can't move
				System.out.println("Patt helyzet, nincs több lépés");
			
			// Elsõ klikk elküldése
		//	System.out.println("AI elsõ kattintását beküldi.");
			moveStateMachine = MOVE_SM.FIRST_CLICK_SENT; // Állapotgép állítása
			logic.click( new ClickData(move.getX1(), move.getY1(), myAIPlayer.getColor()) );
			
		}
		
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
 
}
