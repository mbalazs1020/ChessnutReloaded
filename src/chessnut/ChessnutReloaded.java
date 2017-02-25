package chessnut;


import chess.player.AI;
import chessnut.gui.*;
import chessnut.logic.*;
import chessnut.network.*;


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
		Logic.setPlayer(Opponent);                   // Beállítom a hálózatot ellenfélnek
		((NetworkServer) Opponent).connect("localhost"); // Hálózat nyitása
		
	}
	
	
	/**
	 * Kliens oldal felállítása
	 * @param IP: erre az IP címre csatlakozunk
	 */
	public static void setupClient(String IP)
	{
		Logic = new NetworkClient();                  // Kliens oldali hálózat jelképezi a logikát
		Logic.setPlayer(GUI);                         // Az õ játékosa a GUI
		GUI.setGameLogic(Logic);                      // GUI logikája a hálózat
		((NetworkClient) Logic).connect(IP); // Csatlakozás
		
	}
	
	/**
	 * Single player játék felállítása
	 */
	public static void setupSinglePlayer()
	{
		Logic = new GameLogic(GUI);                  // Játéklogikát létrehozom
		Opponent  = new AI(Logic);                   // AI az ellenfél PIG másféle lesz
		GUI.setGameLogic(Logic);                     // Beállítom a GUI gamelogic-ját
		Logic.setPlayer(Opponent);                   // Beállítom az AI-t ellenfélnek
	}
}
