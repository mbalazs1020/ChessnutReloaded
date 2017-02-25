/**
 * 
 */
package chess.pieces;

import java.util.ArrayList;

import chess.*;


/**
 * @author Gunnar Atli
 *
 */
public class Bishop extends Piece
{
	/**
	 * Sorosításhoz szükséges egyedi azonosító
	 */
	private static final long serialVersionUID = 691453474354545343L;

	
	public Bishop(boolean color) {
		super(color);
		value = 3;
	}
	
	public Bishop( Bishop p)
	{
		super(p);	
	}
	
	public String toString() {
		if(color == Piece.WHITE)
			return "B";
		else
			return "b";
	}
	
	public Bishop clone() {
		return new Bishop(color);
	}

	public ArrayList<Move> getMoves(Board b, int x, int y) {
		ArrayList<Move> moves = new ArrayList<Move>();
		
		
		// NE
		for(int i = 1; i < 8; i++) {
			if(valid(x+i, y+i)) {
				if(b.getTile(x+i, y+i).isOccupied()) {
					if(b.getTile(x+i, y+i).getPiece().color != color)
						moves.add(new Move(x,y,x+i,y+i));	
					
					break;
				}
				else
					moves.add(new Move(x,y,x+i,y+i));	
			}
		}
		
		// NW
		for(int i = 1; i < 8; i++) {
			if(valid(x-i, y+i)) {
				if(b.getTile(x-i, y+i).isOccupied()) {
					if(b.getTile(x-i, y+i).getPiece().color != color)
						moves.add(new Move(x,y,x-i,y+i));	
					
					break;
				}
				else
					moves.add(new Move(x,y,x-i,y+i));	
			}
		}
		
		// SE
		for(int i = 1; i < 8; i++) {
			if(valid(x+i, y-i)) {
				if(b.getTile(x+i, y-i).isOccupied()) {
					if(b.getTile(x+i, y-i).getPiece().color != color)
						moves.add(new Move(x,y,x+i,y-i));	
					
					break;
				}
				else
					moves.add(new Move(x,y,x+i,y-i));	
			}
		}
		
		// SW
		for(int i = 1; i < 8; i++) {
			if(valid(x-i, y-i)) {
				if(b.getTile(x-i, y-i).isOccupied()) {
					if(b.getTile(x-i, y-i).getPiece().color != color)
						moves.add(new Move(x,y,x-i,y-i));	
					
					break;
				}
				else
					moves.add(new Move(x,y,x-i,y-i));	
			}
		}
		
		return moves;
	}
}
