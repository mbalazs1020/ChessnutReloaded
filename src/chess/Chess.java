/**
 * 
 */
package chess;

//import chess.player.*;
//import chess.pieces.*;


/**
 * @author Gunnar Atli
 *
 */
public class Chess {
	
//	public static void main(String[] args)
//	{
//		int winner = 0;    // Ki lesz a gyõztes
//
//		// Játék létrehozása
//		Board board = new Board();
//
//		// Játékosok létrehozása
//		Player player1 = new AlphaBetaPlayer(Piece.WHITE,2);
//		Player player2 = new AlphaBetaPlayer(Piece.BLACK,1);
//		
//		winner = play(player1, player2, board);
//
//		// Valami értelmeset írjon már ki
//		if( winner == -1 ) System.out.println("Player 2 (Black) wins.");
//		else if( winner == 1 ) System.out.println("Player 1 (White) wins.");
//		else if( winner == 0 ) System.out.println("Nobody wins.");
//		else System.err.println("Unknown result.");
//		//System.out.println(player1Score);
//	}
//	
//	/** Returns 1 if player1 wins
//	 * Returns 0 if draw
//	 * Returns -1 if player2 wins
//	 */
//	public static int play(Player player1, Player player2, Board b) {
//		
//		Move move;
//		while(true) {
//			
//			move = player1.getNextMove(b);
//			if(move == null && b.isCheck(player1.getColor())) // check and can't move
//				return -1;
//			if(move == null) // no check but can't move
//				return 0;
//			
//			b.makeMove(move);
//			System.out.println(b);
//			//if(result == -1) return (player1.getColor() == Piece.WHITE) ? -1 : 1; // black wins
//			//if(result == 1) return (player1.getColor() == Piece.WHITE) ? 1 : -1; // white wins
//			
//			
//			move = player2.getNextMove(b);
//			if(move == null && b.isCheck(player2.getColor())) // check and can't move
//				return 1;
//			if(move == null) // no check but can't move
//				return 0;
//			
//			b.makeMove(move);
//			System.out.println(b);
//			
//		} 
//	}

}
