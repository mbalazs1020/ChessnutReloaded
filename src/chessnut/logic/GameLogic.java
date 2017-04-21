package chessnut.logic;

import util.*;

import chess.Board;
import chess.Move;
import chessnut.ILogic;
import chessnut.IPlayer;
import chessnut.logic.ClickData;
import chessnut.network.NetworkServer;
import robot.RobotHandler;
import robot.RobotPlayer;

/**
 * Játéklogika osztálya
 */
public class GameLogic implements ILogic
{
	// Játék elemei
	/**  Sakktáblám   */
	Board chessboard;
	
	/**  Egyik játékos, általában a helyi GUI   */
	IPlayer gui;
	
	/**  Másik játékos az AI/Network   */
	IPlayer opponent;
	
	/**  Obszerváló játékos, aki csak a táblát nézegeti */
	IPlayer observer;
	
	/** Robot obszerver, aki végrehajtja a lépéseket */
	RobotHandler robotObserver;

	/**  Azzal kezdõdik a játék, hogy az induló táblákat kiküldtem   */
	private boolean gameStarted = false;
	
	/**  Folyamatban lévõ lépés adatai  */
	private Move currentMove = null; 
	private int x1, y1;
	private boolean moveStart = true;
	

	/**
	 * Létrehozás GUI alapján
	 * @param gui: amire a referencia beállítódik
	 */
	public GameLogic(IPlayer gui)
	{
		chessboard = new Board(); // Tábla létrehozása
		this.gui = gui;
	}
	
	public GameLogic()
	{
		chessboard = new Board(); // Tábla létrehozása
	}


	public void setPlayer1( IPlayer player )
	{
		this.gui = player;
	}
	
	/**
	 * Ezzel lehet beállítani a túloldali játékosra vonatkozó referenciát (AI / NetworkServer)
	 * @param player: akire a referencia beállítódik
	 */
	@Override
	public void setPlayer2(IPlayer player)
	{
		this.opponent = player;
	}
	
	public void setObserver( IPlayer obs )
	{
		this.observer = obs;
	}
	
	public void setRobotObserver( RobotHandler r )
	{
		this.robotObserver = r;
	}
	
	public void START_GAME()
	{
		sendInitialBoardToBothPlayers(); // Kiküldöm a kezdeti sakktáblákat, kezdõdhet
	}

	
	/**
	 * Játéklogika a click-et kezeli
	 */
	@Override
	public boolean click(ClickData position)
	{
//		String colorString = position.getPlayerColor() ? "WHITE" : "BLACK";
//		System.out.println(colorString + " játékos kattintotta: " + position);

		// Ha nem õ jön, akkor minek kattintgat?
		if(chessboard.getNextPlayerToMove() != position.getPlayerColor())
		{
			System.out.println("Nem õ jön, ne kattintgasson.");
			return false;
		}

		// Elsõ kattintása jön:
		if(moveStart == true)
		{
			firstClick(position);
		}
		else // Második kattintása jön:
		{
			secondClick(position);
		}
		return true;
	}

	
	/**
	 * Elsõ click kezelése
	 */
	private void firstClick(ClickData position)
	{
		// Ha olyan helyre kattintott, ahol nincs is bábu
		if( !chessboard.isTileOccupied(position.getX(), position.getY()))
		{
			System.out.println("Olyan mezõre kattintott, amin nincs bábu. Próbálja újra");
			return;
		}
		
		// Ha nem a saját színére kattintott, akkor nem foglalkozok vele
		if(chessboard.getTile(position.getX(), position.getY()).getPiece().getColor() != chessboard.getNextPlayerToMove())
		{
			System.out.println("Nem a saját színére kattintott. Kattintson újra");
			SendChessboardToOne(chessboard.getNextPlayerToMove()); // Visszaküldöm neki
			return;
		}

		// Elmentem a lépés kiinduló mezõjét
		x1 = position.getX();
		y1 = position.getY();

		// Kijelölöm a sakktáblán ezt a mezõt, és a lehetséges célmezõket
		chessboard.getTile(position.getX(), position.getY()).getPiece().setSelected(true);

		moveStart = false; // Második kattintása jöhet
		// Visszaküldöm az adott játékosnak az új sakktáblát, amelyen a kijelölés már szerepel
		SendChessboardToOne(chessboard.getNextPlayerToMove());
	}

	/**
	 * Második click kezelése
	 */
	private void secondClick(ClickData position)
	{
		// Összerakom a lépést
		currentMove = new Move(x1, y1, position.getX(), position.getY());
		
		chessboard.getTile(x1, y1).getPiece().setSelected(false); // Leveszem a kijelölést a tábláról
		
		// Tájékoztatás
		String player = position.getPlayerColor()? "Világos" : "Sötét";
		System.out.println(player + " játékos ezt lépte: " + currentMove );
		
		boolean isHitMove = chessboard.isTileOccupied(currentMove.getX2(), currentMove.getY2()); // Ütés volt-e, lekérdezem, hogy a robot tudja
		
		// Megpróbálok lépni
		if( chessboard.validateMove(currentMove, chessboard.getNextPlayerToMove(), true) ) // Ha értelmes volt a lépés
		{
			if( chessboard.makeMove(currentMove) ) // Ha sikerült megtenni a lépést
			{
				chessboard.switchNextPlayerToMove(); // Következõ játékos lép
				moveStart = true; // Elsõ kattintása jöhet újra
			//	sendChessboardToBoth();   // Kiküldöm a táblát mindkét játékosnak
				
				// Ha van robot obszerver, elküldöm neki, hogy lépje meg
				if( robotObserver != null )
				{
					// Le kellene szûrnöm, hogy ha a játékosom is maga a robot, akkor ezt ne küldje el
					if( ( gui instanceof RobotPlayer ) || ( opponent instanceof RobotPlayer ) ) // Ha bármelyik játékos robotból van
					{
						// Ha a világos lép, akkor mehet ez
						if( position.getPlayerColor() )
						{
							System.out.println("Világosnak elküldve az obszerver lépés a világostól");
							robotObserver.sendMoveReq(currentMove, isHitMove);
							Thread ack = new Thread(new RobotMoveAcked());
							ack.start(); // Várakozás van a robot visszajelzésére
						}
						// Egyébként a sötét lépett, ezt a világosnak megmutatom
						else
						{
							sendChessboardToBoth();
						}
					}
					else
					{
						robotObserver.sendMoveReq(currentMove, isHitMove);
						Thread ack = new Thread(new RobotMoveAcked());
						ack.start(); // Várakozás van a robot visszajelzésére
					}
				}
				else
					sendChessboardToBoth();   // Kiküldöm a táblát mindkét játékosnak
			}
		}
		else // Nem volt jó a lépés
		{
			// Kiküldöm újra a játékosnak a táblát
			System.out.println("Ez a lépés nem megengedett! Újrapróbálkozás");
			moveStart = true; // Elsõ kattintása jöhet újra
			SendChessboardToOne( chessboard.getNextPlayerToMove() );
		}
	}


	/**
	 * Egy adott játékosnak sakktábla kiküldése
	 * @param color: a játékos színe, aki táblát kap
	 */
	private void SendChessboardToOne(boolean playerColor)
	{
		if(playerColor == true) // világos
		{
			gui.setChessboard(chessboard);
		}
		if(playerColor == false) // sötét
		{
			opponent.setChessboard(chessboard);
		}
	}


	/**
	 * Mindkét oldalnak sakktábla kiküldése
	 */
	private void sendChessboardToBoth()
	{
		gui.setChessboard(chessboard);
		opponent.setChessboard(chessboard);
		
		if(observer != null)
		{
			observer.setChessboard(chessboard);
		}
	}

	/**
	 * Kezdeti sakktáblát kiküldöm mindkét félnek - csak egyszer, az elején
	 * Külön thread-bõl fut, hogy ne fagyja össze magát a várakozásba.
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
	 * Ez várja, hogy a kliens becsatlakozzon, majd kiküldi a kezdõ játékállást
	 */
	private class ConnectionWaitingThread implements Runnable
	{
		@Override
		public void run()
		{
			wait waitingCycle = new wait();

			// Ha hálózatba vagyok
			if(opponent instanceof NetworkServer)
			{
				// Várok, amíg a kliens becsatlakozik
				while(!((NetworkServer) opponent).isConnected())
				{
					waitingCycle.waitSec(1);
				}
			}

			// Mehet ki a tábla
			sendChessboardToBoth();
			
			System.out.println("ELSÕ TÁBLÁK KIKÜLDVE");
			
			gameStarted = true;
		}
	}
	
	/**
	 * Robot lépés visszajelzés megvárása
	 */
	private class RobotMoveAcked implements Runnable
	{
		@Override
		public void run()
		{
			try {
			while ( !robotObserver.isMsgAcked() )
			{
				Thread.sleep(1);
			}
			System.out.println("A robot végrehajtotta a lépést.\n");
			sendChessboardToBoth(); // Küldöm a táblákat, ezzel folytatódhat
			
			} catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

}
