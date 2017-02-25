package util;

/**
 * V�rakoz�shoz val� oszt�ly, amely bel�l kezeli az exception-t is, hogy ne kelljen m�shol boh�ckodni vele.
 */
public class wait
{
	/**  Egy m�sodperc defin�ci�ja   */
	public static final int SEC = 1000;
	

	/**
	 * Ez a f�ggv�ny a param�terben megadott m�sodperc ideig v�r
	 * @param sec: h�ny m�sodpercig v�rjunk
	 */
	public void waitSec(int sec)
	{
		try
		{
			Thread.sleep(sec * SEC);
		} catch (InterruptedException e)
		{
			System.out.println("Waiting exception: " + e.getMessage());
		}
	}
}
