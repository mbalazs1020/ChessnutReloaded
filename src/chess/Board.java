/**
 * 
 */
package chess;

import chess.pieces.*;
import chess.Tile;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Gunnar Atli
 *
 */
public class Board implements Serializable
{
	/**
	 * Sorosításhoz szükséges egyedi azonosító
	 */
	private static final long serialVersionUID = 4765376737687362L;
	
	public static final int a=0, b=1, c=2, d=3, e=4, f=5, g=6, h=7;
	
	private boolean nextPlayerToMove;
	private Tile[][] tiles;
	/**	
	 * 	 8	r n b q k b n r 
	 *	 7	p p p p p p p p 
	 *	 6	. . . . . . . . 
	 *	 5	. . . . . . . . 
	 *	 4	. . . . . . . . 
	 *	 3	. . . . . . . . 
	 *	 2	P P P P P P P P 
	 *	 1  R N B Q K B N R
	 *    	
	 *    	a b c d e f g h
	 *    
	 * P=pawn, K=king, Q=queen, R=rook, N=knight, B=Bishop
	 * Uppercase is white
	 */
	
	public Board(Tile[][] tiles) 
	{
		this.tiles=tiles;
		nextPlayerToMove = Piece.WHITE;
	}
	
	public Board(Board board)
	{
		tiles = new Tile[8][8];
		for(int x=0; x<8; x++)
		{
			for(int y=0; y<8; y++)
			{
				tiles[x][y] = board.getNewTile(x, y);
			}
		}
		this.nextPlayerToMove = board.getNextPlayerToMove();

	}

	/**
	 * 
	 */
	public Board() {
		// initialize board
		nextPlayerToMove = Piece.WHITE;
		
		boolean co = Piece.WHITE;
		tiles = new Tile[8][8];
		tiles[a][1-1] = new Tile(new Rook(co));
		tiles[b][1-1] = new Tile(new Knight(co));
		tiles[c][1-1] = new Tile(new Bishop(co));
		tiles[d][1-1] = new Tile(new Queen(co));
		tiles[e][1-1] = new Tile(new King(co));
		tiles[f][1-1] = new Tile(new Bishop(co));
		tiles[g][1-1] = new Tile(new Knight(co));
		tiles[h][1-1] = new Tile(new Rook(co));
		
		for(int i = 0; i < 8; i++) {
			tiles[i][2-1] = new Tile(new Pawn(co));
		}
		
		for(int i = 2; i < 7; i++) {
			for(int j = 0; j < 8; j++) {
				tiles[j][i] = new Tile();
			}
		}
		
		co = Piece.BLACK;
		tiles[a][8-1] = new Tile(new Rook(co));
		tiles[b][8-1] = new Tile(new Knight(co));
		tiles[c][8-1] = new Tile(new Bishop(co));
		tiles[d][8-1] = new Tile(new Queen(co));
		tiles[e][8-1] = new Tile(new King(co));
		tiles[f][8-1] = new Tile(new Bishop(co));
		tiles[g][8-1] = new Tile(new Knight(co));
		tiles[h][8-1] = new Tile(new Rook(co));
		
		for(int i = 0; i < 8; i++) {
			tiles[i][7-1] = new Tile(new Pawn(co));
		}

	}

//	/** EZ MI A FASZ
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		Board b = new Board();
//		System.out.println(b);
//	}
	
	
	public String toString() {
		String str = "";
		// Bábuk kiírása
		for(int i = 7; i >= 0; i--) {
			str += (i+1) + "  ";
			for(int j = 0; j < 8; j++) {
				str += tiles[j][i] + " ";
			}
			str += "\n";
		}
		
		return str;
	}
	
	public String printSelections()
	{
		String str = "";
		// Kijelölés kiírása
		for(int i = 7; i >= 0; i--) {
			str += (i+1) + "  ";
			for(int j = 0; j < 8; j++) {
				str += tiles[j][i].getPiece().isSelected() ? "O" : ".";
				str += " ";
			}
			str += "\n";
		}
		
		str += "\n   a b c d e f g h";
		
		return str;
	}
	
	public ArrayList<Move> getMoves(boolean color) {
		return getMoves(color, true);
	}
	
	
	/**
	 * Checks if player color is under check
	 * 
	 * @param color
	 * @return
	 */
	public boolean isCheck(boolean color) {
		int x = -1, y = -1;
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++) {
				if(tiles[i][j].isOccupied() && 
						tiles[i][j].getPiece().getColor() == color &&
						tiles[i][j].getPiece().toString().equalsIgnoreCase("K")) {
					x = i; y = j;
				}
			}
		
		// check a move if after making this move the king can be killed (moving into check)
		ArrayList<Move> opponentMoves = getMoves(!color, false);
		
		// check all opponent moves if they kill king (opponent moves in next round)
		for(int j = 0; j < opponentMoves.size(); j++) {
			if(opponentMoves.get(j).getX2() == x && opponentMoves.get(j).getY2() == y)
				return true;
		}
		
		return false;	
	}
	
	/**
	 * Checks if player color is under check
	 * 
	 * @param color
	 * @return
	 */
	public boolean isCheckAfter(boolean color, ArrayList<Move> moves) {
		
		Tile[][] newTiles = getTilesAfter(moves);
		
		int x = -1, y = -1;
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++) {
				if(newTiles[i][j].isOccupied() && 
						newTiles[i][j].getPiece().getColor() == color &&
						newTiles[i][j].getPiece().toString().equalsIgnoreCase("K")) {
					x = i; y = j;
				}
			}
		
		// check a move if after making this move the king can be killed (moving into check)
		ArrayList<Move> opponentMoves = getMovesAfter(!color, moves, false);
		
		// check all opponent moves if they kill king (opponent moves in next round)
		for(int j = 0; j < opponentMoves.size(); j++) {
			if(opponentMoves.get(j).getX2() == x && opponentMoves.get(j).getY2() == y)
				return true;
		}
		
		return false;	
	}
	
	public ArrayList<Move> getMoves(boolean color, boolean checkCheck) {
		ArrayList<Move> moves = new ArrayList<Move>();
		
		for(int i = 0; i < 8; i++)               // végigmegy a mezõkön
			for(int j = 0; j < 8; j++)
			{
				if(tiles[i][j].isOccupied() &&               // ha foglalt a mezõ, és az adott mezõ a kérdezett színû
				   tiles[i][j].getPiece().getColor() == color) {
					moves.addAll(tiles[i][j].getPiece().getMoves(this, i, j)); // ... akkor hozzáadja a lehetséges lépéseit
				}
			}

		// check if move is valid (must not be check after move) and throw away erroneous moves
		if(checkCheck) {
			// find king (of correct color)
			int x = -1, y = -1;
			for(int i = 0; i < 8; i++)
				for(int j = 0; j < 8; j++) {
					if(tiles[i][j].isOccupied() && 
							tiles[i][j].getPiece().getColor() == color &&
							tiles[i][j].getPiece().toString().equalsIgnoreCase("K")) {
						x = i; y = j;
					}
				}
			
			ArrayList<Move> removeThese = new ArrayList<Move>();
			for(int i = 0; i < moves.size(); i++) {
				// check a move if after making this move the king can be killed (moving into check)
				ArrayList<Move> checkThis = new ArrayList<Move>(moves.subList(i, i+1));
				ArrayList<Move> opponentMoves = getMovesAfter(!color, checkThis, false);
				
				int xUpdated = x, yUpdated = y;
				if(checkThis.get(0).getX1() == x && checkThis.get(0).getY1() == y) { // get updated king position
					xUpdated = checkThis.get(0).getX2();
					yUpdated = checkThis.get(0).getY2();
				}
				
				// check all opponent moves if they kill king (opponent moves in next round)
				for(int j = 0; j < opponentMoves.size(); j++) {
					if(opponentMoves.get(j).getX2() == xUpdated && opponentMoves.get(j).getY2() == yUpdated)
						removeThese.add(checkThis.get(0));
				}
			}
			
			moves.removeAll(removeThese); // remove invalid moves
		}
		
		return moves;
	}
	
	/**
	 * Ezzel vizsgálom meg, hogy érvényes-e a lépés, amit kattintottam
	 */
	public boolean validateMove( Move move, boolean color, boolean checkCheck )
	{
		ArrayList<Move> moves = new ArrayList<Move>();
		
		if(tiles[move.getX1()][move.getY1()].isOccupied() &&               // ha foglalt a mezõ, és az adott bábu a kérdezett színû
		   tiles[move.getX1()][move.getY1()].getPiece().getColor() == color)
		{
		   moves.addAll(tiles[move.getX1()][move.getY1()].getPiece().getMoves(this, move.getX1(), move.getY1())); // ... akkor hozzáadja a lehetséges lépéseit
		}
		else
		{
			System.out.println("Szerinte a mezõn nincs bábu, vagy nem az én színem.");
			return false;
		}
		
		// check if move is valid (must not be check after move) and throw away erroneous moves
		if(checkCheck) {
			// find king (of correct color)
			int x = -1, y = -1;
			for(int i = 0; i < 8; i++)
				for(int j = 0; j < 8; j++) {
					if(tiles[i][j].isOccupied() && 
							tiles[i][j].getPiece().getColor() == color &&
							tiles[i][j].getPiece().toString().equalsIgnoreCase("K")) {
						x = i; y = j;
					}
				}
			
			ArrayList<Move> removeThese = new ArrayList<Move>();
			for(int i = 0; i < moves.size(); i++) {
				// check a move if after making this move the king can be killed (moving into check)
				ArrayList<Move> checkThis = new ArrayList<Move>(moves.subList(i, i+1));
				ArrayList<Move> opponentMoves = getMovesAfter(!color, checkThis, false);
				
				int xUpdated = x, yUpdated = y;
				if(checkThis.get(0).getX1() == x && checkThis.get(0).getY1() == y) { // get updated king position
					xUpdated = checkThis.get(0).getX2();
					yUpdated = checkThis.get(0).getY2();
				}
				
				// check all opponent moves if they kill king (opponent moves in next round)
				for(int j = 0; j < opponentMoves.size(); j++) {
					if(opponentMoves.get(j).getX2() == xUpdated && opponentMoves.get(j).getY2() == yUpdated)
						removeThese.add(checkThis.get(0));
				}
			}
			
			moves.removeAll(removeThese); // remove invalid moves
		}
		
		// Itt meg kellene nézni, hogy az általam megadott lépés szerepel-e a listában
		if( moves.contains(move)) return true;
		
		System.out.println("Ezek lettek volna a lehetséges lépések:");
		for( Move actmove: moves )
		{
			System.out.println(actmove);
		}
		return false;
	}
	
	public ArrayList<Move> getMovesAfter(boolean color, ArrayList<Move> moves) {
		return getMovesAfter(color, moves, true);
	}
	
	public ArrayList<Move> getMovesAfter(boolean color, ArrayList<Move> moves, boolean checkCheck) {
		
		Tile[][] temp = new Tile[8][8];
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				temp[x][y] = new Tile(this.tiles[x][y]);
			}
		}
		
		Board b = new Board(temp);

		for(int i = 0; i < moves.size(); i++)
			b.makeMove(moves.get(i));
		
		ArrayList<Move> futureMoves = b.getMoves(color, checkCheck);
		
		return futureMoves;
	}
	
	public Tile[][] getTilesAfter(ArrayList<Move> moves) {
		
		Tile[][] temp = new Tile[8][8];
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				temp[x][y] = new Tile(this.tiles[x][y]);
			}
		}
		
		Board b = new Board(temp);

		for(int i = 0; i < moves.size(); i++)
			b.makeMove(moves.get(i));
		
		Tile[][] temp2 = new Tile[8][8];
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				temp2[x][y] = new Tile(b.getTile(x, y));
			}
		}
		
		return temp2;
	}
	
	/**
	 * @param m move
	 * @return true, ha sikeres a lépés
	 */
	public boolean makeMove(Move m) {
	//	Tile oldTile = tiles[m.getX1()][m.getY1()];
				
		tiles[m.getX2()][m.getY2()] = tiles[m.getX1()][m.getY1()];
		tiles[m.getX1()][m.getY1()] = new Tile();
		
		if(m.isCastling()) {
			// Sáncolást nem engedem meg
			System.err.println("Sáncolni akart ez a kötsög!!!");
			return false;
//			if(m.getX2() == g && m.getY2() == 1-1) {
//				tiles[f][1-1] = tiles[h][1-1];
//				tiles[h][1-1] = new Tile();
//			}
//			if(m.getX2() == c && m.getY2() == 1-1) {
//				tiles[d][1-1] = tiles[a][1-1];
//				tiles[a][1-1] = new Tile();			
//			}
//			if(m.getX2() == g && m.getY2() == 8-1) {
//				tiles[f][8-1] = tiles[h][8-1];
//				tiles[h][8-1] = new Tile();
//			}
//			if(m.getX2() == c && m.getY2() == 8-1) {
//				tiles[d][8-1] = tiles[a][8-1];
//				tiles[a][8-1] = new Tile();	
//			}
		}
		
		// pawn at top? - Gyalogváltást nem engedem meg
//		if(oldTile.getPiece().toString().equals("P") && m.getY2() == 8-1)
//			tiles[m.getX2()][m.getY2()] = new Tile(new Queen(Piece.WHITE));
//		
//		if(oldTile.getPiece().toString().equals("p") && m.getY2() == 1-1)
//			tiles[m.getX2()][m.getY2()] = new Tile(new Queen(Piece.BLACK));
//		
		return true;
	}
	
	public Tile getTile(int x, int y) {
		return tiles[x][y];
	}
	
	public Tile getNewTile(int x, int y) {
		return new Tile ( tiles[x][y] );
	}
	
	public Tile getNewTile( Tile tile ) {
		return new Tile ( tile );
	}
	
	// Ezzel megnézem, foglalt-e a mezõ
	public boolean isTileOccupied(int x, int y)
	{
		return getTile(x, y).isOccupied();
	}

	public boolean getNextPlayerToMove()
	{
		return nextPlayerToMove;
	}

	public void switchNextPlayerToMove()
	{
		if( nextPlayerToMove == Piece.WHITE )
		{
			this.nextPlayerToMove = Piece.BLACK;
		}
		else
		{
			this.nextPlayerToMove = Piece.WHITE;
		}
	}

}
