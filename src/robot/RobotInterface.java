package robot;

import chess.Move;

/**
 * A robottal végezhetõ mûveletek általános megfogalmazása
 * @author Balázs
 *
 */
public interface RobotInterface
{
	/** Kapcsolat van-e? */
	abstract public boolean isConnected();
	
	/** Bejövõ üzenet feldolgozása */
	abstract public void handleIncomingMessage( String msgReceived );
	
	/** Mozgás request küldés a robotnak */
	abstract public void sendMoveReq( Move m, boolean hit );
}
