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
	Move move = null; // Ez lesz a l�p�sem
	MOVE_SM moveStateMachine = MOVE_SM.START;
	private Thread thread;     // Ebben a threadben futunk
	
	Board myChessboard = null;  // Sakkt�bla amit kaptam
	
	
	
	public AI( ILogic logic, boolean PlayerColor )
	{
		this.logic = logic;
		myAIPlayer = new AlphaBetaPlayer(PlayerColor, 2);  // Most mindig s�t�t vagyok, �s alfab�ta
		moveStateMachine = MOVE_SM.START;  // �llapotg�p bel�v�se		
	}
	
	// T�bl�t kaptam
	@Override
	public void setChessboard(Board chessboard)
	{		
		myChessboard = new Board(chessboard);
		thread = new Thread( new ChessboardHandlerThread() );
		thread.start();  // K�l�n threadben futtatom az �llapotg�pet
	}
	
	@Override
	public void setGameLogic(ILogic logic)
	{
		this.logic = logic;
	}
	
	// L�p�s �llapotg�pe
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
			
			// �llapotg�p szerinti el�gaz�s
			if( moveStateMachine == MOVE_SM.START )  // Most kezd�dik az eg�sz
			{
				// Ha nem � j�n, ne kattintgasson
				if( myAIPlayer.getColor() != myChessboard.getNextPlayerToMove() )
				{
					return;
				}
			//	System.out.println("Mesters�ges Intelligencia j�t�kos feldolgozza a t�bl�t...");
				move = null; // M�g nincs l�p�s
				moveStateMachine = MOVE_SM.CREATE_MOVE; // �trakom a d�nt�s meghoz�sa �llapotba, azt�n mehet tov�bb
				
				// Aludjon r� n�h�ny milliszekundumot, mert �gy m�g kirajzolni se lehet a t�bl�t, olyan gyors
				Thread.sleep(100);
			}
			
			if( moveStateMachine == MOVE_SM.SECOND_CLICK_SENT )
			{
				// Ha m�r nem �n vagyok soron az �j t�bl�n, akkor j�l v�geztem dolgomat megyek a startra
				if( myChessboard.getNextPlayerToMove() != myAIPlayer.getColor() )
				{
					moveStateMachine = MOVE_SM.START;
				}
				else
				{
					move = null; // M�g nincs l�p�s
					moveStateMachine = MOVE_SM.CREATE_MOVE; // K�nytelen leszek �j l�p�st kre�lni, mert az el�z� szar volt
				}
			}
			
			if( moveStateMachine == MOVE_SM.FIRST_CLICK_SENT )
			{
				// Megn�zem, hogy kijel�lt�k-e a mez�t az �j t�bl�n. Ez azt jelenti, hogy elfogadt�k
				if( myChessboard.getTile(move.getX1(), move.getY1()).getPiece().isSelected() )
				{
					// Elk�ld�m a m�sodik l�p�st, �s megyek tov�bb abba az �llapotba
					moveStateMachine = MOVE_SM.SECOND_CLICK_SENT; 
				//	System.out.println("AI m�sodik kattint�s�t bek�ldi.");
					logic.click( new ClickData(move.getX2(), move.getY2(), myAIPlayer.getColor()) );
				}
				else
				{
					move = null; // M�g nincs l�p�s
					moveStateMachine = MOVE_SM.CREATE_MOVE; // Egy�bk�nt �j l�p�st kell kital�lnom
				}
			}
			
			if( moveStateMachine == MOVE_SM.CREATE_MOVE ) // D�nt�s meghoz�sa, ez itt van a v�g�n, mert b�rmelyik m�sik �llapotb�l itt folytathatom
			{
				move = new Move( myAIPlayer.getNextMove(myChessboard));
				if(move == null && myChessboard.isCheck(myAIPlayer.getColor())) // sakk van, �s mozogni se tudok: sakk matt van nekem :(
				{
					String loser = myChessboard.getNextPlayerToMove()? "Vil�gos" : "S�t�t";
					System.out.println(loser + "j�t�kos vesztett, sakk-matt.");
					return;
					
				}
				if(move == null) // no check but can't move
				{
					System.out.println("Patt helyzet, nincs t�bb l�p�s");
					return;
				}
				
				// Els� klikk elk�ld�se
			//	System.out.println("AI els� kattint�s�t bek�ldi.");
				moveStateMachine = MOVE_SM.FIRST_CLICK_SENT; // �llapotg�p �ll�t�sa
				logic.click( new ClickData(move.getX1(), move.getY1(), myAIPlayer.getColor()) );
				
			}
			
			}catch(Exception ex)
			{
				System.out.println("J�T�K V�GE");
			}
		}
		
	}
 
}
