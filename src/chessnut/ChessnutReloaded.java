package chessnut;


import chess.pieces.Piece;
import chess.player.AI;
import chessnut.gui.*;
import chessnut.logic.*;
import chessnut.network.*;
import robot.RobotHandler;
import robot.RobotPlayer;


/**
 * Program fõ osztálya.
 */
public class ChessnutReloaded
{	
	/**  GUI mindkét oldalon van */
	private static GUI     GUI;          
	
	/**  Szervernél a hálózat/AI, kliensnél nincs */
	private static IPlayer Opponent;
	
	/**  Szervernél a játéklogika, kliensnél a hálózat */
	private static ILogic  Logic;        
	
	/**
	 * Ezzel a függvénnyel indul a program.
	 */
	public static void main(String[] args)
	{
		GUI = new GUI();         // GUI létrehozása
	}
	
	
	/**
	 * Szerver oldal felállítása
	 */
	public static void setupServer()
	{
		// Ez a rendes futás
		Logic = new GameLogic(GUI);                  // Játéklogikát létrehozom
		Opponent  = new NetworkServer(Logic);        // Szerver oldali hálózatot létrehozom
		GUI.setGameLogic(Logic);                     // Beállítom a GUI gamelogic-ját
		Logic.setPlayer2(Opponent);                   // Beállítom a hálózatot ellenfélnek
		((NetworkServer) Opponent).connect("localhost"); // Hálózat nyitása
		((GameLogic) Logic).START_GAME();
	}
	
	
	/**
	 * Kliens oldal felállítása
	 * @param IP: erre az IP címre csatlakozunk
	 */
	public static void setupClient(String IP)
	{
		Logic = new NetworkClient();                  // Kliens oldali hálózat jelképezi a logikát
		Logic.setPlayer2(GUI);                         // Az õ játékosa a GUI
		GUI.setGameLogic(Logic);                      // GUI logikája a hálózat
		((NetworkClient) Logic).connect(IP); // Csatlakozás
		((GameLogic) Logic).START_GAME();
	}
	
	/**
	 * Single player játék felállítása
	 */
	public static void setupSinglePlayer()
	{
		Logic = new GameLogic(GUI);                  // Játéklogikát létrehozom
		Opponent  = new AI(Logic, Piece.BLACK);      // AI az ellenfél, sötét színben
		GUI.setGameLogic(Logic);                     // Beállítom a GUI gamelogic-ját
		Logic.setPlayer2(Opponent);                   // Beállítom az AI-t ellenfélnek
		((GameLogic) Logic).START_GAME();
	}
	
	/**
	 * Gép a gép ellen játék felállítása
	 */
	public static void setupAIvsAI()
	{
		// Ez a rendes futás
		Logic = new GameLogic();                      // Játéklogikát létrehozom
		((GameLogic) Logic).setPlayer1(new AI(Logic, Piece.WHITE));  // AI az egyik játékos, világos színben
		Opponent  = new AI(Logic, Piece.BLACK);                      // AI az ellenfél, sötét színben
		Logic.setPlayer2(Opponent);                      // Beállítom a másik AI-t ellenfélnek
		((GameLogic) Logic).setObserver(GUI);            // GUI az obszerváló tag		
		((GameLogic) Logic).START_GAME();
	}
	
	/**
	 * Gép elleni játék robot végrehajtással
	 */
	public static void setupSinglePlayerWithRobotObserver() throws InterruptedException
	{
		Logic = new GameLogic(GUI);                          // Játéklogikát létrehozom
		Opponent  = new AI(Logic, Piece.BLACK);              // AI az ellenfél, sötét színben
		GUI.setGameLogic(Logic);                             // Beállítom a GUI gamelogic-ját
		Logic.setPlayer2(Opponent);                          // Beállítom az AI-t ellenfélnek
		RobotHandler robotObserver = new RobotHandler();   // Robot obszerver csatlakozás
		while( !robotObserver.isConnected() ) Thread.sleep(1); // Várom hogy csatlakozzon 
		
		// TODO PIG
				RobotPlayer pl = new RobotPlayer();
				pl.setRobotHandler(robotObserver);
		
		((GameLogic) Logic).setRobotObserver(robotObserver); // Beállítom a robot végrehajtót
		((GameLogic) Logic).START_GAME();
	}
	
	/**
	 * Gép a gép ellen játék robot végrehajtássa
	 */
	public static void setupAIvsAIWithRobotObserver() throws InterruptedException
	{
		// Ez a rendes futás
		Logic = new GameLogic();                      // Játéklogikát létrehozom
		((GameLogic) Logic).setPlayer1(new AI(Logic, Piece.WHITE));  // AI az egyik játékos, világos színben
		Opponent  = new AI(Logic, Piece.BLACK);                      // AI az ellenfél, sötét színben
		Logic.setPlayer2(Opponent);                      // Beállítom a másik AI-t ellenfélnek
		((GameLogic) Logic).setObserver(GUI);            // GUI az obszerváló tag
		RobotHandler robotObserver = new RobotHandler();   // Robot obszerver csatlakozás
		while( !robotObserver.isConnected() ) Thread.sleep(1); // Várom hogy csatlakozzon 
		((GameLogic) Logic).setRobotObserver(robotObserver); // Beállítom a robot végrehajtót
		((GameLogic) Logic).START_GAME();
	}
}
