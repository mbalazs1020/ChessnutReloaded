package chessnut;

import chess.Board;

/**
 * Ezen az interfészen keresztül érhetõek el a játékos típusú objektumok: GUI és AI
 */
public interface IPlayer
{	
	/**  Ezen tudjuk leküldeni a sakktáblát, annak minden tartozékával együtt   */
	public abstract void setChessboard(Board chessboard);   
	
	/**  ILogic interfészû egységre referencia beállítása   */
	public abstract void setGameLogic( ILogic logic );          
}
