package chessnut.gui;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;

import chess.Board;
import chess.pieces.Piece;
import chessnut.ILogic;
import chessnut.IPlayer;
import chessnut.ChessnutReloaded;
import chessnut.logic.ClickData;


/**
 * GUI f� oszt�ly
 */
public class GUI extends JFrame implements IPlayer
{
	/**  Egyedi magicnumber a soros�t�shoz   */
	private static final long serialVersionUID = 1111111111111111L;
	
	/** Ezen a referenci�n tudom a kapcsolatot tartani a j�t�klogik�val */
	private ILogic logic;
	
	/** Ebben megjegyzem, hogy milyen oldal vagyok, hogy n�h�ny dologr�l el tudjam d�nteni, hogy vonatkozik-e r�m*/
	private boolean myPlayerColor;
	
	/** J�t�k kezd�se. Innent�l lehet kattintani */
	private boolean gameStarted = false;
	
	/** Saj�t elmentett sakkt�bl�m */
	private Board chessBoard;
	
	/**  Sakkfigur�k k�pei  */
	BufferedImage BBishop = null;
	/**  Sakkfigur�k k�pei  */
	BufferedImage BKing = null;
	/**  Sakkfigur�k k�pei  */
	BufferedImage BKnight = null;
	/**  Sakkfigur�k k�pei  */
	BufferedImage BPawn = null;
	/**  Sakkfigur�k k�pei  */
	BufferedImage BQueen = null;
	/**  Sakkfigur�k k�pei  */
	BufferedImage BRook = null;
	/**  Sakkfigur�k k�pei  */
	BufferedImage WBishop = null;
	/**  Sakkfigur�k k�pei  */
	BufferedImage WKing = null;
	/**  Sakkfigur�k k�pei  */
	BufferedImage WKnight = null;
	/**  Sakkfigur�k k�pei  */
	BufferedImage WPawn = null;
	/**  Sakkfigur�k k�pei  */
	BufferedImage WQueen = null;
	/**  Sakkfigur�k k�pei  */
	BufferedImage WRook = null;
	
	/** 
	 * GUI konstruktor
	 */
	public GUI()
	{
		/** Alapvet� be�ll�t�sok */
		super("Chessnut Reloaded");                                    // L�trej�n az ablak
		setSize(600, 600);                                    // Ablakm�ret be�ll�t�sa
		setDefaultCloseOperation(EXIT_ON_CLOSE);              // Alap�rtelmezett kil�p�si be�ll�t�s
		setLayout(null);                                      // Layout
		
		/** Men�sor, amiben l�trehozunk egy "Start game" men�pontot,
		 * melyb�l leg�rd�l� list�b�l kiv�laszthatjuk a j�t�km�dot:
		 * Szerver ind�t�s
		 * Csatlakoz�s szerverhez
		 * G�p elleni j�t�k */
		JMenuBar menuBar = new JMenuBar();                    // Men�sor l�trej�n
		JMenu menu = new JMenu("Robot n�lk�li j�t�k");                 // Start game men�pont
		
		/** Szerverind�t�s almen�pont */
		JMenuItem menuItem = new JMenuItem("Szerver ind�t�sa");   // Start szerver almen�pont
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Szerver ind�t�sa, ha m�g nem fut j�t�k
				if( !gameStarted )
				{
					ChessnutReloaded.setupServer();  // Szerver setup
					myPlayerColor = true; // white
					gameStarted = true;
				}
			}
		});
		menu.add(menuItem);
		
		/** Szerverhez csatlakoz�s almen�pont */
		menuItem = new JMenuItem("Csatlakoz�s szerverhez");        // Connect to server j�t�km�d almen�pontja
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Kliens ind�t�sa, ha m�g nem fut j�t�k
				if( !gameStarted )
				{
					InputIPAddrDialog ipDialog = new InputIPAddrDialog();
					String IP = null;
					while ( IP == null )
					{
						IP = ipDialog.getIp();
					}
					
					ChessnutReloaded.setupClient(IP);  // Kliens szetup
					myPlayerColor = false; // black
					gameStarted = true;
				}
			}
		});
		menu.add(menuItem);

		/** G�p elleni j�t�k almen�pontja */
		menuItem = new JMenuItem("J�t�k a g�p ellen");       // AI elleni j�t�k almen�pontja
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// AI elleni j�t�k ind�t�sa
				if( !gameStarted )
				{
					ChessnutReloaded.setupSinglePlayer(); // Singleplayer setup
					myPlayerColor = true; // feh�r
					gameStarted = true;
				}
			}
		});
		menu.add(menuItem);
		
		/** G�p a g�p ellen j�t�k almen�pontja */
		menuItem = new JMenuItem("G�p a g�p ellen");       //  j�t�k almen�pontja
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// AI elleni j�t�k ind�t�sa
				if( !gameStarted )
				{
					ChessnutReloaded.setupAIvsAI(); // Singleplayer setup
					myPlayerColor = true; // feh�r
					gameStarted = true;
				}
			}
		});
		menu.add(menuItem);
		
		menuBar.add(menu);
		
		
        menu = new JMenu("Robot j�t�k");                 // Start game men�pont
        /*
         * - Val�s j�t�k robot ellen
         * - Szerver ind�t�s: Online j�t�k robot v�grehajt�ssal
         * - G�p elleni j�t�k robot v�grehajt�ssal
         * - G�p a g�p ellen j�t�k robot v�grehajt�ssal
         */
		
		/** Szerverind�t�s almen�pont */
		menuItem = new JMenuItem("Val�s j�t�k robot ellen");   // Start szerver almen�pont
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Szerver ind�t�sa, ha m�g nem fut j�t�k
				if( !gameStarted )
				{
					//ChessnutReloaded.setupSinglePlayer(); // Singleplayer setup TODO �j j�t�km�d fel�ll�t�sa
					myPlayerColor = true; // white
					gameStarted = true;
				}
			}
		});
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Szerver ind�t�s: Online j�t�k robot v�grehajt�ssal");   // Start szerver almen�pont
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Szerver ind�t�sa, ha m�g nem fut j�t�k
				if( !gameStarted )
				{
					//ChessnutReloaded.setupSinglePlayer(); // Singleplayer setup TODO �j j�t�km�d fel�ll�t�sa
					myPlayerColor = true; // white
					gameStarted = true;
				}
			}
		});
		menu.add(menuItem);
		
		menuItem = new JMenuItem("G�p elleni j�t�k robot v�grehajt�ssal");   // Start szerver almen�pont
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Szerver ind�t�sa, ha m�g nem fut j�t�k
				if( !gameStarted )
				{
					try{
					ChessnutReloaded.setupSinglePlayerWithRobotObserver(); // J�t�km�d fel�ll�t�sa
					myPlayerColor = true; // white
					gameStarted = true;
					} catch (Exception exc)
					{
						exc.printStackTrace();
					}
				}
			}
		});
		menu.add(menuItem);
		
		menuItem = new JMenuItem("G�p a g�p ellen j�t�k robot v�grehajt�ssal");   // Start szerver almen�pont
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Szerver ind�t�sa, ha m�g nem fut j�t�k
				if( !gameStarted )
				{
					try{
					ChessnutReloaded.setupAIvsAIWithRobotObserver(); // setup
					gameStarted = true;
					} catch (Exception exc)
					{
						exc.printStackTrace();
					}
				}
			}
		});
		menu.add(menuItem);
		

		
		menuBar.add(menu);
		

		/** Kil�p�s men�pont */
		menuItem = new JMenuItem("Kil�p�s");                      // Kil�p� gomb
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		menuBar.add(menuItem);
		

		setJMenuBar(menuBar);

		/** Sakkt�bl�n val� kattint�s hely�nek kisz�m�t�sa */
		addMouseListener(new MouseAdapter()
		{
		     @Override
		     public void mousePressed(MouseEvent e) {
		 //       System.out.println("X" + e.getX() + " Y" + e.getY() );
		    	 
		    	if( logic == null ) return;
		        
		        int width=getContentPane().getWidth();
				int height=getContentPane().getHeight();
				int clickX=e.getX();
				int clickY=e.getY();
				int yOffset = getHeight() - getContentPane().getHeight();
				clickY=clickY - yOffset;


				int size;

				if ( width <=height)
				{
					size=width/8;
				}
				else
					size=height/8;
				
		        int posX=clickX/size;
		        int posY=clickY/size;
		        
		        // A fasz egerem mag�t�l kattintgat, ezt kisz�r�m:
		        try{
		        ClickData click = new ClickData(posX, 7-posY, myPlayerColor);
		        if ( click != null )
		        logic.click(click);
		        } catch(Exception ex)
		        {
		        	System.out.println("A fasz egerem mag�t�l kattintgat, de leszarom: " + ex.getMessage());
		        }
		     }
		 });
		
		/** Sakkfigur�k k�peinek beolvas�sa  */
		try {
			BBishop = ImageIO.read(getClass().getResource(("pictures/BBishop.png")));
			BKing = ImageIO.read(getClass().getResource(("pictures/BKing.png")));
			BKnight = ImageIO.read(getClass().getResource(("pictures/BKnight.png")));
			BPawn = ImageIO.read(getClass().getResource(("pictures/BPawn.png")));
			BQueen = ImageIO.read(getClass().getResource(("pictures/BQueen.png")));
			BRook = ImageIO.read(getClass().getResource(("pictures/BRook.png")));
			WBishop = ImageIO.read(getClass().getResource(("pictures/WBishop.png")));
			WKing = ImageIO.read(getClass().getResource(("pictures/WKing.png")));
			WKnight = ImageIO.read(getClass().getResource(("pictures/WKnight.png")));
			WPawn = ImageIO.read(getClass().getResource(("pictures/WPawn.png")));
			WQueen = ImageIO.read(getClass().getResource(("pictures/WQueen.png")));
			WRook = ImageIO.read(getClass().getResource(("pictures/WRook.png")));
		} catch (Exception ex)
		{
			System.out.println("File not found: " + ex.getMessage());
		}
		
		/** Ablak l�that�v� t�tele */
		setVisible(true);               // L�that�v� teszem az ablakot
	}
	
	
	/**
	 * ILogic referencia be�ll�t�sa
	 * @param logic: ahova a referencia mutat
	 */
	@Override
	public void setGameLogic(ILogic logic)
	{
		this.logic = logic;
	}
	
	
	/**
	 * ChessBoard referencia be�ll�t�sa
	 * @param chessboard: akire a referencia mutat
	 */
	@Override
	public void setChessboard(Board chessboard)
	{
		this.chessBoard = chessboard;

		/** Fel�let �jb�li kirajzol�sa */
		repaint();
		
		setVisible(true);
		
	}
	
	
	/** 
	 * A l�that� fel�let megjelen�t�se.
	 * Itt t�rt�nik a sakkt�bla kirajzol�sa �s a figur�k elhelyez�se a t�bl�n.
	 * 
	 */
	public void paint(Graphics g) {
        super.paint(g);  // fixes the immediate problem.
        
        /** T�bla mez�inek sz�ne */
        Color darkBrown = new Color(139, 69, 19);
		Color lightBrown = new Color(232, 194, 145);
		
		/** Mez�k sz�ne a lehets�ges l�p�sek helyein */
		Color selectColor1 = new Color((int)(139*1.5), (int)(69*1.5), (int)(19*1.5));
		Color selectColor2 = new Color((int)(255), (int)(194*1.3), (int)(145*1.5));

		/** Ablak m�reteinek meghat�roz�sa*/
		int width=getContentPane().getWidth();
		int height=getContentPane().getHeight();
		int yOffset = getHeight() - getContentPane().getHeight();

		int size;

		if ( width <=height)
		{
			size=width/8;
		}
		else
			size=height/8;
		
		for (int j = 0; j < 8; j++) // Itt megy v�gig a mez�k�n
		{
			for (int i = 0; i < 8; i++) 
			
			{
				if ((i + j) % 2 == 0)  // Be�ll�tja a mez� alap sz�n�t
				{					
					g.setColor(darkBrown);
				}
				else
				{
					g.setColor(lightBrown);
				}
				int y = (7-i) * size + yOffset;
				int x = j * size;
				g.fillRect( x, y, size, size);
				
				if (this.chessBoard != null) {
					Piece p = chessBoard.getTile(j, i).getPiece();    // Kiveszi a b�but
					if( p != null)
					{
						if (chessBoard.getTile(j, i).getPiece().isSelected())
						{      // Ha ki van jel�lve
								if ((i + j) % 2 == 0) {					
									g.setColor(selectColor1);  // Be�ll�tja a kijel�l�s sz�neket
								} else {
									g.setColor(selectColor2);
								}
								g.fillRect( x, y, size, size);
						}

					if( p != null)
					{
						// B�buk felrajzol�sa
						if (p.toString() == "B")
						{
							g.drawImage(WBishop, x, y, size, size, null);
						}
						else if (p.toString() == "b")
						{
							g.drawImage(BBishop, x, y, size, size, null);
						}
						else if (p.toString() == "K")
						{
							g.drawImage(WKing, x, y, size, size, null);
						}
						else if (p.toString() == "k")
						{
							g.drawImage(BKing, x, y, size, size, null);
						}	
						else if (p.toString() == "R")
						{
							g.drawImage(WRook, x, y, size, size, null);
						}	
						else if (p.toString() == "r")
						{
							g.drawImage(BRook, x, y, size, size, null);
						}	
						else if (p.toString() == "N")
						{
							g.drawImage(WKnight, x, y, size, size, null);
						}	
						else if (p.toString() == "n")
						{
							g.drawImage(BKnight, x, y, size, size, null);
						}	
						else if (p.toString() == "Q")
						{
							g.drawImage(WQueen, x, y, size, size, null);
						}	
						else if (p.toString() == "q")
						{
							g.drawImage(BQueen, x, y, size, size, null);
						}	
						else if (p.toString() == "P")
						{
							g.drawImage(WPawn, x, y, size, size, null);
						}	
						else if (p.toString() == "p")
						{
							g.drawImage(BPawn, x, y, size, size, null);
						}

					}
					}
				}
				
				
			}
			
		}

	}
	
}
