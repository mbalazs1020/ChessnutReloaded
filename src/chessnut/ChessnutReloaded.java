package chessnut;


import chess.pieces.Piece;
import chess.player.AI;
import chessnut.gui.*;
import chessnut.logic.*;
import chessnut.network.*;
import robot.RobotHandler;
import robot.RobotPlayer;


/**
 * Program f� oszt�lya.
 */
public class ChessnutReloaded
{	
	/**  GUI mindk�t oldalon van */
	private static GUI     GUI;          
	
	/**  Szervern�l a h�l�zat/AI, kliensn�l nincs */
	private static IPlayer Opponent;
	
	/**  Szervern�l a j�t�klogika, kliensn�l a h�l�zat */
	private static ILogic  Logic;        
	
	/**
	 * Ezzel a f�ggv�nnyel indul a program.
	 */
	public static void main(String[] args)
	{
		GUI = new GUI();         // GUI l�trehoz�sa
	}
	
	
	/**
	 * Szerver oldal fel�ll�t�sa
	 */
	public static void setupServer()
	{
		// Ez a rendes fut�s
		Logic = new GameLogic(GUI);                  // J�t�klogik�t l�trehozom
		Opponent  = new NetworkServer(Logic);        // Szerver oldali h�l�zatot l�trehozom
		GUI.setGameLogic(Logic);                     // Be�ll�tom a GUI gamelogic-j�t
		Logic.setPlayer2(Opponent);                   // Be�ll�tom a h�l�zatot ellenf�lnek
		((NetworkServer) Opponent).connect("localhost"); // H�l�zat nyit�sa
		((GameLogic) Logic).START_GAME();
	}
	
	
	/**
	 * Kliens oldal fel�ll�t�sa
	 * @param IP: erre az IP c�mre csatlakozunk
	 */
	public static void setupClient(String IP)
	{
		Logic = new NetworkClient();                  // Kliens oldali h�l�zat jelk�pezi a logik�t
		Logic.setPlayer2(GUI);                         // Az � j�t�kosa a GUI
		GUI.setGameLogic(Logic);                      // GUI logik�ja a h�l�zat
		((NetworkClient) Logic).connect(IP); // Csatlakoz�s
		((GameLogic) Logic).START_GAME();
	}
	
	/**
	 * Single player j�t�k fel�ll�t�sa
	 */
	public static void setupSinglePlayer()
	{
		Logic = new GameLogic(GUI);                  // J�t�klogik�t l�trehozom
		Opponent  = new AI(Logic, Piece.BLACK);      // AI az ellenf�l, s�t�t sz�nben
		GUI.setGameLogic(Logic);                     // Be�ll�tom a GUI gamelogic-j�t
		Logic.setPlayer2(Opponent);                   // Be�ll�tom az AI-t ellenf�lnek
		((GameLogic) Logic).START_GAME();
	}
	
	/**
	 * G�p a g�p ellen j�t�k fel�ll�t�sa
	 */
	public static void setupAIvsAI()
	{
		// Ez a rendes fut�s
		Logic = new GameLogic();                      // J�t�klogik�t l�trehozom
		((GameLogic) Logic).setPlayer1(new AI(Logic, Piece.WHITE));  // AI az egyik j�t�kos, vil�gos sz�nben
		Opponent  = new AI(Logic, Piece.BLACK);                      // AI az ellenf�l, s�t�t sz�nben
		Logic.setPlayer2(Opponent);                      // Be�ll�tom a m�sik AI-t ellenf�lnek
		((GameLogic) Logic).setObserver(GUI);            // GUI az obszerv�l� tag		
		((GameLogic) Logic).START_GAME();
	}
	
	/**
	 * G�p elleni j�t�k robot v�grehajt�ssal
	 */
	public static void setupSinglePlayerWithRobotObserver() throws InterruptedException
	{
		Logic = new GameLogic(GUI);                          // J�t�klogik�t l�trehozom
		Opponent  = new AI(Logic, Piece.BLACK);              // AI az ellenf�l, s�t�t sz�nben
		GUI.setGameLogic(Logic);                             // Be�ll�tom a GUI gamelogic-j�t
		Logic.setPlayer2(Opponent);                          // Be�ll�tom az AI-t ellenf�lnek
		RobotHandler robotObserver = new RobotHandler();   // Robot obszerver csatlakoz�s
		while( !robotObserver.isConnected() ) Thread.sleep(1); // V�rom hogy csatlakozzon 
		
		// TODO PIG
				RobotPlayer pl = new RobotPlayer();
				pl.setRobotHandler(robotObserver);
		
		((GameLogic) Logic).setRobotObserver(robotObserver); // Be�ll�tom a robot v�grehajt�t
		((GameLogic) Logic).START_GAME();
	}
	
	/**
	 * G�p a g�p ellen j�t�k robot v�grehajt�ssa
	 */
	public static void setupAIvsAIWithRobotObserver() throws InterruptedException
	{
		// Ez a rendes fut�s
		Logic = new GameLogic();                      // J�t�klogik�t l�trehozom
		((GameLogic) Logic).setPlayer1(new AI(Logic, Piece.WHITE));  // AI az egyik j�t�kos, vil�gos sz�nben
		Opponent  = new AI(Logic, Piece.BLACK);                      // AI az ellenf�l, s�t�t sz�nben
		Logic.setPlayer2(Opponent);                      // Be�ll�tom a m�sik AI-t ellenf�lnek
		((GameLogic) Logic).setObserver(GUI);            // GUI az obszerv�l� tag
		RobotHandler robotObserver = new RobotHandler();   // Robot obszerver csatlakoz�s
		while( !robotObserver.isConnected() ) Thread.sleep(1); // V�rom hogy csatlakozzon 
		((GameLogic) Logic).setRobotObserver(robotObserver); // Be�ll�tom a robot v�grehajt�t
		((GameLogic) Logic).START_GAME();
	}
}
