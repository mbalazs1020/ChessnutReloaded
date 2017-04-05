package robot;

import chess.Move;

/**
 * A robottal v�gezhet� m�veletek �ltal�nos megfogalmaz�sa
 * @author Bal�zs
 *
 */
public interface RobotInterface
{
	/** Kapcsolat van-e? */
	abstract public boolean isConnected();
	
	/** Bej�v� �zenet feldolgoz�sa */
	abstract public void handleIncomingMessage( String msgReceived );
	
	/** Mozg�s request k�ld�s a robotnak */
	abstract public void sendMoveReq( Move m, boolean hit );
}
