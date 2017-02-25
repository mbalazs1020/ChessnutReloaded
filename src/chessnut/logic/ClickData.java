package chessnut.logic;

import java.io.Serializable;

/**
 * A sakkt�bla egy poz�ci�j�t reprezent�lja
 * A sorok (rank) 0-t�l 7-ig sz�moz�dnak, a val�di t�bl�n 1-t�l 8-ig vannak jel�lve.
 * Az oszlopok (file) 0-t�l 7-ig sz�moz�dnak, a val�di t�bl�n a-t�l h-ig vannak jel�lve.
 */
public class ClickData implements Serializable
{
	/**
	 * Soros�t�shoz sz�ks�ges azonos�t�
	 */
	private static final long serialVersionUID = 2758435244468732197L;
	
	private final int x; // Kattintott mez�k
	private final int y;

	boolean playerColor; // J�t�kos sz�ne
	
	/**
	 * Konstruktor
	 * @param rank A poz�ci� sora
	 * @param file A poz�ci� oszlopa
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
	 * Megvizsg�lja, hogy az adott sor/oszlop kombin�ci� egy sakkt�bla tartom�ny�n bel�lre esik el
	 * @param rank a poz�ci� sora
	 * @param file a poz�ci� oszlopa
	 * @return True, ha a megadott param�terek l�tez� poz�ci�ra mutatnak
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
	 * Megrp�b�l l�trehozni egy position objektumot a megadott adatokb�l
	 * @param rank A poz�ci� sora
	 * @param file A poz�ci� oszlopa
	 * @return A l�trehozott poz�ci�, vagy null, ha az adatok kimutatnak a sakkt�bl�n k�v�lre
	 */
	static public ClickData tryCreate(int rank, int file, boolean color)
	{
		if(!inRange(rank, file))
			return null;
		return new ClickData(rank, file, color);
	}
}
