package util;

import java.util.Scanner;

import chessnut.network.RobotConnectionServer;

public class DebugConsoleReader implements Runnable
{
	Scanner inFromUser = new Scanner(System.in);
	RobotConnectionServer robot;
	
	public DebugConsoleReader( RobotConnectionServer r )
	{
		robot = r;
	}
	
	public void run()
	{
		while(true)
		{
			// Próbáljunk meg beszedni valamit
			robot.sendMsg( inFromUser.nextLine() );
		}
	}
	
	
}
