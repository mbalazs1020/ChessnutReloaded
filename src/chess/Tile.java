/**
 * 
 */
package chess;

import java.io.Serializable;

import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Piece;
import chess.pieces.Queen;
import chess.pieces.Rook;

/**
 * @author Gunnar Atli
 *
 */
public class Tile implements Serializable
{
	/**
	 * Sorosításhoz szükséges egyedi azonosító
	 */
	private static final long serialVersionUID = 35431381358431L;
	
	private boolean occupied;
	private Piece piece = null;
	/**
	 * 
	 */
	public Tile() {
		occupied = false;
	}
	
	public Tile(Tile tile) {
		this.occupied = tile.isOccupied();
		if( tile.isOccupied() )
			this.piece = getNewPiece(tile.getPiece());
		else
			this.piece = null;
	}
	
	
	public Tile(Piece piece) {
		occupied = true;
		this.piece = getNewPiece(piece);
	}
	
	public Piece getNewPiece( Piece piece )
	{
		if( piece instanceof Bishop )
		{
			return new Bishop( (Bishop) piece);
		}
		
		else if( piece instanceof King )
		{
			return new King( (King) piece);
		}
		
		else if( piece instanceof Knight )
		{
			return new Knight( (Knight) piece);
		}
		
		else if( piece instanceof Pawn )
		{
			return new Pawn( (Pawn) piece);
		}
		
		else if( piece instanceof Queen )
		{
			return new Queen( (Queen) piece);
		}
		
		else if( piece instanceof Rook )
		{
			return new Rook( (Rook) piece);
		}
		return null;
	}
	
	public String toString() {
		if(occupied)
		{
			if(piece == null) return "o";
			return piece.toString();
		}
		else
			return ".";
	}

	public Piece getPiece() {
		return piece;
	}
	
	public boolean isOccupied() {
		return occupied;
	}

}
