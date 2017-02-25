package chessnut;

import chess.Board;

/**
 * Ezen az interf�szen kereszt�l �rhet�ek el a j�t�kos t�pus� objektumok: GUI �s AI
 */
public interface IPlayer
{	
	/**  Ezen tudjuk lek�ldeni a sakkt�bl�t, annak minden tartoz�k�val egy�tt   */
	public abstract void setChessboard(Board chessboard);   
	
	/**  ILogic interf�sz� egys�gre referencia be�ll�t�sa   */
	public abstract void setGameLogic( ILogic logic );          
}
