package chessnut;

import chessnut.logic.*;


/**
 * Ezen az interf�szen kereszt�l el�rhet� a j�t�klogika.
 */
public interface ILogic
{
	/**  Player/AI �ltali kattint�s ezen jut fel a logik�hoz   */
	public boolean click(ClickData position);                                
	
	/**  IPlayer interf�sz� elemre referencia be�ll�t�sa   */
	public abstract void setPlayer2( IPlayer player );      
}