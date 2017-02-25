package chess.pieces;

import chess.Board;
import chess.Move;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Piece implements Serializable
{
	/**
	 * Sorosításhoz szükséges egyedi azonosító
	 */
	private static final long serialVersionUID = 684348343845L;
	
	public static final boolean WHITE = true, BLACK = false;
	protected boolean color;
	protected boolean selected;
	protected int value;
	
	public boolean getColor() {
		return color;
	}
	
	public Piece() {
		this.color = WHITE;
		this.selected = false;
		value = 0;
	}

	public Piece(boolean color) {
		this.color = color;
		this.selected = false;
		value = 0;
	}
	
	public Piece(Piece p)
	{
		this.selected = p.isSelected();
		this.color = p.getColor();
		this.value = p.getValue();
	}
	
	
	public int getValue() {
		return value;
	}
	
	public abstract Piece clone();
	
	public abstract ArrayList<Move> getMoves(Board b, int x, int y);
	
	/**
	 * @param b Board
	 * @param x x location of piece
	 * @param y y location of piece
	 * @return
	 */
	static public boolean valid(int x, int y) {
		if(x < 0 || x > 7 || y < 0 || y > 7)
			return false;
		else
			return true;
	}
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
