package chessnut;

import chessnut.logic.*;


/**
 * Ezen az interfészen keresztül elérhetõ a játéklogika.
 */
public interface ILogic
{
	/**  Player/AI általi kattintás ezen jut fel a logikához   */
	public boolean click(ClickData position);                                
	
	/**  IPlayer interfészû elemre referencia beállítása   */
	public abstract void setPlayer2( IPlayer player );      
}