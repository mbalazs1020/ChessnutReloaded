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
 * GUI fõ osztály
 */
public class GUI extends JFrame implements IPlayer
{
	/**  Egyedi magicnumber a sorosításhoz   */
	private static final long serialVersionUID = 1111111111111111L;
	
	/** Ezen a referencián tudom a kapcsolatot tartani a játéklogikával */
	private ILogic logic;
	
	/** Ebben megjegyzem, hogy milyen oldal vagyok, hogy néhány dologról el tudjam dönteni, hogy vonatkozik-e rám*/
	private boolean myPlayerColor;
	
	/** Játék kezdése. Innentõl lehet kattintani */
	private boolean gameStarted = false;
	
	/** Saját elmentett sakktáblám */
	private Board chessBoard;
	
	/**  Sakkfigurák képei  */
	BufferedImage BBishop = null;
	/**  Sakkfigurák képei  */
	BufferedImage BKing = null;
	/**  Sakkfigurák képei  */
	BufferedImage BKnight = null;
	/**  Sakkfigurák képei  */
	BufferedImage BPawn = null;
	/**  Sakkfigurák képei  */
	BufferedImage BQueen = null;
	/**  Sakkfigurák képei  */
	BufferedImage BRook = null;
	/**  Sakkfigurák képei  */
	BufferedImage WBishop = null;
	/**  Sakkfigurák képei  */
	BufferedImage WKing = null;
	/**  Sakkfigurák képei  */
	BufferedImage WKnight = null;
	/**  Sakkfigurák képei  */
	BufferedImage WPawn = null;
	/**  Sakkfigurák képei  */
	BufferedImage WQueen = null;
	/**  Sakkfigurák képei  */
	BufferedImage WRook = null;
	
	/** 
	 * GUI konstruktor
	 */
	public GUI()
	{
		/** Alapvetõ beállítások */
		super("Chessnut Reloaded");                                    // Létrejön az ablak
		setSize(600, 600);                                    // Ablakméret beállítása
		setDefaultCloseOperation(EXIT_ON_CLOSE);              // Alapértelmezett kilépési beállítás
		setLayout(null);                                      // Layout
		
		/** Menüsor, amiben létrehozunk egy "Start game" menüpontot,
		 * melybõl legördülõ listából kiválaszthatjuk a játékmódot:
		 * Szerver indítás
		 * Csatlakozás szerverhez
		 * Gép elleni játék */
		JMenuBar menuBar = new JMenuBar();                    // Menüsor létrejön
		JMenu menu = new JMenu("Robot nélküli játék");                 // Start game menüpont
		
		/** Szerverindítás almenüpont */
		JMenuItem menuItem = new JMenuItem("Szerver indítása");   // Start szerver almenüpont
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Szerver indítása, ha még nem fut játék
				if( !gameStarted )
				{
					ChessnutReloaded.setupServer();  // Szerver setup
					myPlayerColor = true; // white
					gameStarted = true;
				}
			}
		});
		menu.add(menuItem);
		
		/** Szerverhez csatlakozás almenüpont */
		menuItem = new JMenuItem("Csatlakozás szerverhez");        // Connect to server játékmód almenüpontja
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Kliens indítása, ha még nem fut játék
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

		/** Gép elleni játék almenüpontja */
		menuItem = new JMenuItem("Játék a gép ellen");       // AI elleni játék almenüpontja
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// AI elleni játék indítása
				if( !gameStarted )
				{
					ChessnutReloaded.setupSinglePlayer(); // Singleplayer setup
					myPlayerColor = true; // fehér
					gameStarted = true;
				}
			}
		});
		menu.add(menuItem);
		
		/** Gép a gép ellen játék almenüpontja */
		menuItem = new JMenuItem("Gép a gép ellen");       //  játék almenüpontja
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// AI elleni játék indítása
				if( !gameStarted )
				{
					ChessnutReloaded.setupAIvsAI(); // Singleplayer setup
					myPlayerColor = true; // fehér
					gameStarted = true;
				}
			}
		});
		menu.add(menuItem);
		
		menuBar.add(menu);
		
		
        menu = new JMenu("Robot játék");                 // Start game menüpont
        /*
         * - Valós játék robot ellen
         * - Szerver indítás: Online játék robot végrehajtással
         * - Gép elleni játék robot végrehajtással
         * - Gép a gép ellen játék robot végrehajtással
         */
		
		/** Szerverindítás almenüpont */
		menuItem = new JMenuItem("Valós játék robot ellen");   // Start szerver almenüpont
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Szerver indítása, ha még nem fut játék
				if( !gameStarted )
				{
					//ChessnutReloaded.setupSinglePlayer(); // Singleplayer setup TODO új játékmód felállítása
					myPlayerColor = true; // white
					gameStarted = true;
				}
			}
		});
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Szerver indítás: Online játék robot végrehajtással");   // Start szerver almenüpont
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Szerver indítása, ha még nem fut játék
				if( !gameStarted )
				{
					//ChessnutReloaded.setupSinglePlayer(); // Singleplayer setup TODO új játékmód felállítása
					myPlayerColor = true; // white
					gameStarted = true;
				}
			}
		});
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Gép elleni játék robot végrehajtással");   // Start szerver almenüpont
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Szerver indítása, ha még nem fut játék
				if( !gameStarted )
				{
					try{
					ChessnutReloaded.setupSinglePlayerWithRobotObserver(); // Játékmód felállítása
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
		
		menuItem = new JMenuItem("Gép a gép ellen játék robot végrehajtással");   // Start szerver almenüpont
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Szerver indítása, ha még nem fut játék
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
		

		/** Kilépés menüpont */
		menuItem = new JMenuItem("Kilépés");                      // Kilépõ gomb
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

		/** Sakktáblán való kattintás helyének kiszámítása */
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
		        
		        // A fasz egerem magától kattintgat, ezt kiszûröm:
		        try{
		        ClickData click = new ClickData(posX, 7-posY, myPlayerColor);
		        if ( click != null )
		        logic.click(click);
		        } catch(Exception ex)
		        {
		        	System.out.println("A fasz egerem magától kattintgat, de leszarom: " + ex.getMessage());
		        }
		     }
		 });
		
		/** Sakkfigurák képeinek beolvasása  */
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
		
		/** Ablak láthatóvá tétele */
		setVisible(true);               // Láthatóvá teszem az ablakot
	}
	
	
	/**
	 * ILogic referencia beállítása
	 * @param logic: ahova a referencia mutat
	 */
	@Override
	public void setGameLogic(ILogic logic)
	{
		this.logic = logic;
	}
	
	
	/**
	 * ChessBoard referencia beállítása
	 * @param chessboard: akire a referencia mutat
	 */
	@Override
	public void setChessboard(Board chessboard)
	{
		this.chessBoard = chessboard;

		/** Felület újbóli kirajzolása */
		repaint();
		
		setVisible(true);
		
	}
	
	
	/** 
	 * A látható felület megjelenítése.
	 * Itt történik a sakktábla kirajzolása és a figurák elhelyezése a táblán.
	 * 
	 */
	public void paint(Graphics g) {
        super.paint(g);  // fixes the immediate problem.
        
        /** Tábla mezõinek színe */
        Color darkBrown = new Color(139, 69, 19);
		Color lightBrown = new Color(232, 194, 145);
		
		/** Mezõk színe a lehetséges lépések helyein */
		Color selectColor1 = new Color((int)(139*1.5), (int)(69*1.5), (int)(19*1.5));
		Color selectColor2 = new Color((int)(255), (int)(194*1.3), (int)(145*1.5));

		/** Ablak méreteinek meghatározása*/
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
		
		for (int j = 0; j < 8; j++) // Itt megy végig a mezõkön
		{
			for (int i = 0; i < 8; i++) 
			
			{
				if ((i + j) % 2 == 0)  // Beállítja a mezõ alap színét
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
					Piece p = chessBoard.getTile(j, i).getPiece();    // Kiveszi a bábut
					if( p != null)
					{
						if (chessBoard.getTile(j, i).getPiece().isSelected())
						{      // Ha ki van jelölve
								if ((i + j) % 2 == 0) {					
									g.setColor(selectColor1);  // Beállítja a kijelölés színeket
								} else {
									g.setColor(selectColor2);
								}
								g.fillRect( x, y, size, size);
						}

					if( p != null)
					{
						// Bábuk felrajzolása
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
