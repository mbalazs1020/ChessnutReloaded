package chessnut.logic;

import util.*;
import chess.Board;
import chess.Move;
import chessnut.ILogic;
import chessnut.IPlayer;
import chessnut.logic.ClickData;
import chessnut.network.NetworkServer;

/**
 * J�t�klogika oszt�lya
 */
public class GameLogic implements ILogic
{
	// J�t�k elemei
	/**  Sakkt�bl�m   */
	Board chessboard;
	
	/**  Egyik j�t�kos a helyi GUI   */
	IPlayer gui;
	
	/**  M�sik j�t�kos az AI/Network   */
	IPlayer opponent;

	/**  Azzal kezd�dik a j�t�k, hogy az indul� t�bl�kat kik�ldtem   */
	private boolean gameStarted = false;
	
	/**  Folyamatban l�v� l�p�s adatai  */
	private Move currentMove = null; 
	private int x1, y1;
	private boolean moveStart = true;
	

	/**
	 * L�trehoz�s GUI alapj�n
	 * @param gui: amire a referencia be�ll�t�dik
	 */
	public GameLogic(IPlayer gui)
	{
		chessboard = new Board(); // T�bla l�trehoz�sa
		this.gui = gui;
	}


	/**
	 * Ezzel lehet be�ll�tani a t�loldali j�t�kosra vonatkoz� referenci�t (AI / NetworkServer)
	 * @param player: akire a referencia be�ll�t�dik
	 */
	@Override
	public void setPlayer(IPlayer player)
	{
		this.opponent = player;
		chessboard = new Board();   // Sakkt�bl�t inicializ�lom
		sendInitialBoardToBothPlayers(); // Kik�ld�m a kezdeti sakkt�bl�kat is
	}

	
	/**
	 * J�t�klogika a click-et kezeli
	 */
	@Override
	public void click(ClickData position)
	{
//		String colorString = position.getPlayerColor() ? "WHITE" : "BLACK";
//		System.out.println(colorString + " j�t�kos kattintotta: " + position);

		// Ha nem � j�n, akkor minek kattintgat?
		if(chessboard.getNextPlayerToMove() != position.getPlayerColor())
		{
			System.out.println("Nem � j�n, ne kattintgasson.");
			return;
		}

		// Els� kattint�sa j�n:
		if(moveStart == true)
		{
			firstClick(position);
		}
		else // M�sodik kattint�sa j�n:
		{
			secondClick(position);
		}
	}

	
	/**
	 * Els� click kezel�se
	 */
	private void firstClick(ClickData position)
	{
		// Ha olyan helyre kattintott, ahol nincs is b�bu
		if( !chessboard.isTileOccupied(position.getX(), position.getY()))
		{
			System.out.println("Olyan mez�re kattintott, amin nincs b�bu. Pr�b�lja �jra");
			return;
		}
		
		// Ha nem a saj�t sz�n�re kattintott, akkor nem foglalkozok vele
		if(chessboard.getTile(position.getX(), position.getY()).getPiece().getColor() != chessboard.getNextPlayerToMove())
		{
			System.out.println("Nem a saj�t sz�n�re kattintott. Kattintson �jra");
			SendChessboardToOne(chessboard.getNextPlayerToMove()); // Visszak�ld�m neki
			return;
		}

		// Elmentem a l�p�s kiindul� mez�j�t
		x1 = position.getX();
		y1 = position.getY();

		// Kijel�l�m a sakkt�bl�n ezt a mez�t, �s a lehets�ges c�lmez�ket
		chessboard.getTile(position.getX(), position.getY()).getPiece().setSelected(true);

		moveStart = false; // M�sodik kattint�sa j�het
		// Visszak�ld�m az adott j�t�kosnak az �j sakkt�bl�t, amelyen a kijel�l�s m�r szerepel
		SendChessboardToOne(chessboard.getNextPlayerToMove());
	}

	/**
	 * M�sodik click kezel�se
	 */
	private void secondClick(ClickData position)
	{
		// �sszerakom a l�p�st
		currentMove = new Move(x1, y1, position.getX(), position.getY());
		
		chessboard.getTile(x1, y1).getPiece().setSelected(false); // Leveszem a kijel�l�st a t�bl�r�l
		
		// T�j�koztat�s
		String player = position.getPlayerColor()? "Vil�gos" : "S�t�t";
		System.out.println(player + " j�t�kos ezt l�pte: " + currentMove );
		
		// Megpr�b�lok l�pni
		if( chessboard.validateMove(currentMove, chessboard.getNextPlayerToMove(), true) ) // Ha �rtelmes volt a l�p�s
		{
			if( chessboard.makeMove(currentMove) ) // Ha siker�lt megtenni a l�p�st
			{
				chessboard.switchNextPlayerToMove(); // K�vetkez� j�t�kos l�p
				moveStart = true; // Els� kattint�sa j�het �jra
				sendChessboardToBoth();   // Kik�ld�m a t�bl�t mindk�t j�t�kosnak
			}
		}
		else // Nem volt j� a l�p�s
		{
			// Kik�ld�m �jra a j�t�kosnak a t�bl�t
			System.out.println("Ez a l�p�s nem megengedett! �jrapr�b�lkoz�s");
			moveStart = true; // Els� kattint�sa j�het �jra
			SendChessboardToOne( chessboard.getNextPlayerToMove() );
		}
	}


	/**
	 * Egy adott j�t�kosnak sakkt�bla kik�ld�se
	 * @param color: a j�t�kos sz�ne, aki t�bl�t kap
	 */
	private void SendChessboardToOne(boolean playerColor)
	{
		if(playerColor == true) // vil�gos
		{
			gui.setChessboard(chessboard);
		}
		if(playerColor == false) // s�t�t
		{
			opponent.setChessboard(chessboard);
		}
	}


	/**
	 * Mindk�t oldalnak sakkt�bla kik�ld�se
	 */
	private void sendChessboardToBoth()
	{
		gui.setChessboard(chessboard);
		opponent.setChessboard(chessboard);
	}

	/**
	 * Kezdeti sakkt�bl�t kik�ld�m mindk�t f�lnek - csak egyszer, az elej�n
	 * K�l�n thread-b�l fut, hogy ne fagyja �ssze mag�t a v�rakoz�sba.
	 */
	private void sendInitialBoardToBothPlayers()
	{
		if(!gameStarted) // Csak egyszer lehet
		{
			Thread connectionWait = new Thread(new ConnectionWaitingThread());
			connectionWait.start();
		}
	}


	/**
	 * Ez v�rja, hogy a kliens becsatlakozzon, majd kik�ldi a kezd� j�t�k�ll�st
	 */
	private class ConnectionWaitingThread implements Runnable
	{
		@Override
		public void run()
		{
			wait waitingCycle = new wait();

			// Ha h�l�zatba vagyok
			if(opponent instanceof NetworkServer)
			{
				// V�rok, am�g a kliens becsatlakozik
				while(!((NetworkServer) opponent).isConnected())
				{
					waitingCycle.waitSec(1);
				}
			}

			// Mehet ki a t�bla
			sendChessboardToBoth();
			gameStarted = true;
		}
	}

}