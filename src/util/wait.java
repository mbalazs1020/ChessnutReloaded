package util;

/**
 * Várakozáshoz való osztály, amely belül kezeli az exception-t is, hogy ne kelljen máshol bohóckodni vele.
 */
public class wait
{
	/**  Egy másodperc definíciója   */
	public static final int SEC = 1000;
	

	/**
	 * Ez a függvény a paraméterben megadott másodperc ideig vár
	 * @param sec: hány másodpercig várjunk
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
