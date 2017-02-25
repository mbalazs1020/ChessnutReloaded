/**
 * 
 */
package chess.pieces;

import chess.Board;
import chess.Move;
import java.util.ArrayList;

/**
 * @author Gunnar Atli 
 *
 */
public class Pawn extends Piece
{
	/**
	 * Soros�t�shoz sz�ks�ges egyedi azonos�t�
	 */
	private static final long serialVersionUID = 2345676543554454L;

	/**
	 * 
	 */
	public Pawn(boolean color) {
		super(color);
		value = 1;
	}
	
	public Pawn( Pawn p)
	{
		super(p);	
	}
	
	public Pawn()
	{
		super();
	}
	
	public Pawn clone() {
		return new Pawn(color);
	}

	public String toString() {
		if(color == Piece.WHITE)
			return "P";
		else
			return "p";
	}
	
	/**
	 * @param b Board
	 * @param x x location of piece
	 * @param y y location of piece
	 * @return
	 */
	public ArrayList<Move> getMoves(Board b, int x, int y) {
		ArrayList<Move> moves = new ArrayList<Move>();
		
		if(color == Piece.WHITE) {
			// forward
			if(valid(x,y+1) && !b.getTile(x, y+1).isOccupied())
				moves.add(new Move(x,y,x,y+1));

			// Itt hozz�adom, hogy a gyalog az elej�n kett�t is l�phet, mert ez alapb�l nem volt meg benne
			if( y==1 && valid(x,y+2) && !b.getTile(x, y+2).isOccupied())
				moves.add(new Move(x,y,x,y+2));
			
			// kill diagonally
			if(valid(x+1,y+1) && b.getTile(x+1, y+1).isOccupied() && b.getTile(x+1, y+1).getPiece().getColor() != color)
				moves.add(new Move(x,y,x+1,y+1));
			
			// kill diagonally
			if(valid(x-1,y+1) && b.getTile(x-1, y+1).isOccupied() && b.getTile(x-1, y+1).getPiece().getColor() != color)
				moves.add(new Move(x,y,x-1,y+1));
		}
		else {
			// forward
			if(valid(x,y-1) && !b.getTile(x, y-1).isOccupied())
				moves.add(new Move(x,y,x,y-1));
			
			// Itt hozz�adom, hogy a gyalog az elej�n kett�t is l�phet, mert ez alapb�l nem volt meg benne
			if( y==6 && valid(x,y-2) && !b.getTile(x, y-2).isOccupied())
				moves.add(new Move(x,y,x,y-2));
			
			// kill diagonally
			if(valid(x+1,y-1) && b.getTile(x+1, y-1).isOccupied() && b.getTile(x+1, y-1).getPiece().getColor() != color)
				moves.add(new Move(x,y,x+1,y-1));
			
			// kill diagonally
			if(valid(x-1,y-1) && b.getTile(x-1, y-1).isOccupied() && b.getTile(x-1, y-1).getPiece().getColor() != color)
				moves.add(new Move(x,y,x-1,y-1));
		}
		
		return moves;
	}
}
