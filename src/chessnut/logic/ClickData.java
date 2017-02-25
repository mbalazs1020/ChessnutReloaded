package chessnut.logic;

import java.io.Serializable;

/**
 * A sakktábla egy pozícióját reprezentálja
 * A sorok (rank) 0-tól 7-ig számozódnak, a valódi táblán 1-tõl 8-ig vannak jelölve.
 * Az oszlopok (file) 0-tól 7-ig számozódnak, a valódi táblán a-tól h-ig vannak jelölve.
 */
public class ClickData implements Serializable
{
	/**
	 * Sorosításhoz szükséges azonosító
	 */
	private static final long serialVersionUID = 2758435244468732197L;
	
	private final int x; // Kattintott mezõk
	private final int y;

	boolean playerColor; // Játékos színe
	
	/**
	 * Konstruktor
	 * @param rank A pozíció sora
	 * @param file A pozíció oszlopa
	 */
	public ClickData(int rank, int file, boolean color)
	{
		this.x = rank;
		this.y = file;
		this.playerColor = color;
		if (!inRange(rank, file))
			throw new IllegalArgumentException();
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}
	
	public boolean getPlayerColor()
	{
		return this.playerColor;
	}
	
	public void setPlayerColor( boolean color )
	{
		this.playerColor = color;
	}

	/**
	 * Megvizsgálja, hogy az adott sor/oszlop kombináció egy sakktábla tartományán belülre esik el
	 * @param rank a pozíció sora
	 * @param file a pozíció oszlopa
	 * @return True, ha a megadott paraméterek létezõ pozícióra mutatnak
	 */
	private static boolean inRange(int rank, int file)
	{
		return rank >= 0 && rank < 8 && file >= 0 && file < 8;
	}

	@Override
	public String toString()
	{
		return String.valueOf((char)('a' + y)) + String.valueOf(1 + x);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof ClickData))
			return false;
		ClickData other = (ClickData) obj;
		return other.getY() == y && other.getX() == x;
	}
	
	@Override
	public int hashCode() 
	{
		return y * 10 + x;		
	};
	
	/**
	 * Megrpóbál létrehozni egy position objektumot a megadott adatokból
	 * @param rank A pozíció sora
	 * @param file A pozíció oszlopa
	 * @return A létrehozott pozíció, vagy null, ha az adatok kimutatnak a sakktáblán kívülre
	 */
	static public ClickData tryCreate(int rank, int file, boolean color)
	{
		if(!inRange(rank, file))
			return null;
		return new ClickData(rank, file, color);
	}
}
