import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.sound.sampled.*;
class AudioVolumeException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AudioVolumeException()
	{
		super("Audio Volume Cannot be grater than 47");
	}
}
class Audio
{	
	public Clip audioclip = null;
	public Audio(String filename)
	{ 
		try {        
	    }
	    catch (Exception e) {
	        //whatevers
	    }
	}
	public void PlayAudio()
	{		
		audioclip.start();
	}
	public void StopAudio()
	{
		audioclip.stop();
		audioclip.loop(500);
	}
	public void SetLoop(int arg)
	{		
		audioclip.loop(arg);
	}
	public void SetVolume(int volume)
	{
		if(volume<=47)
		{
		FloatControl gainControl = 
		    (FloatControl) audioclip.getControl(FloatControl.Type.MASTER_GAIN);
		if(volume==0)
		    gainControl.setValue(-80);
		else if(volume>0)
			gainControl.setValue((volume-80)+39);
		} else
			try {
				throw new AudioVolumeException();
			} catch (AudioVolumeException e){}
		}
}
public class mnsweep 
{
	static int start = 0;   //Check is Game is Started or not
	static JPanel mpane = new JPanel();   //Main Panel
	static JFrame mwin = new JFrame();  //Main Frame
	static JPanel sgmpane = new JPanel();  //Start Game Menu Panel
	static JPanel gpane = new JPanel();   //Game Panel
	static JButton sbtn = new JButton("Start"),  //Start Game Button
	               mmbtn = new JButton("Main Menu");  //Main Menu Button
	static int sgmpsx=0, sgmpsy=5, sgmplx = 650, sgmply = 400, sgmsxy = 1;   //Size and Location X Y of Game Levels Panel
	static int mines = 0;   //Number of Mines to Put
	static int x=50, y=50, w=100, h=100, icons = 0;   //Game Icons x, y, w, h //Icons Done	
	static int mineshown = 0,   //How Many Mines are shown
	           icosdone = 0;   //How Many Icons Have Been Clicked
	static int minesbg = 5, minesid = 7, minesex = 10;   //Mines for Beginner Intermediate Expert Levels
	static boolean sound  = true,   //Determine if Sound is On or Off
	               oldsound = true;   //Determine if Sound was On or Off before opeing Main Menu
	static int[] Mines = new int[1];   //Mines Places
	static JPanel rpane = new JPanel(),   	//Right Pane
	              pausepane = new JPanel();   //Pause Menu Panel
	static int scores = 0,   //How many stars find
	           lost = 0,   //Determine if you lost or not
	           firstopen = 0,  //Determine if u have opened an icon to remove error - auto show all mines sometimes when restarted
	           levellvl = 1,   //Easy or Hard Level
	           oldlevellvl = 1;    //Old Easy or Hard Level
	static Timer sallmtr = new Timer();  //Show All Mines Timer
	static TimerTask sallmtt = null;  //Show All Mines Timer Taks
	static JButton[] gicons = new JButton[25];  //All Buttons
	static ImageIcon miicon = new ImageIcon("");   //Mines Icon
	static ImageIcon smileicon  = new ImageIcon("");   //Smile Icon
	static ImageIcon cryicon  = new ImageIcon("");   //Cry Icon
	static JLabel piclabel = new JLabel();   //Smile & Cry Picture Label
	static int minefoundwplaying = 0,   //if Mine is Clicked While Playing(used in hard Mode)
	           minesout = 0,   //How Many Mines are Out(used in har mode)
	           continuestars = 0,   //Count Continue number of Stars
	           continuemines = 0;   //Count Continue number of Mines
	static JLabel Remarks = new JLabel("Enjoy!!!", SwingConstants.CENTER);
	static int resX = 0, resY = 0;
	//Getting Resolution
	public static void getResolution()
	{
		resX = Toolkit.getDefaultToolkit().getScreenSize().width;
		resY = Toolkit.getDefaultToolkit().getScreenSize().height;
	}
	//Load show All Mines Timer & Task
	public static void Loadshowallminestt()
	{
		sallmtr = new Timer();
		//Timer & TmerTask to Show all Mines
		sallmtt = new TimerTask()
		{
			public void run()
			{
				gicons[Mines[mineshown]].setIcon(miicon);
				if(mineshown==mines-1)
					sallmtr.cancel();
				mineshown++;
			}			
		};
		//_____________________________________________
	}
	//Restart Game
	public static void performRestart()
	{
		new mnsweep();
		firstopen = 0;
		Loadshowallminestt();
		lost = 0;
		scores=0;
		rpane.repaint();
		start = 0;
		for(int i=0;i<=24;i++)
    		gicons[i].setVisible(false);		
		StartGame();		
	}
	//AudioClips
	static Audio minea = new Audio(""),
	      suna = new Audio("");
	//_________________________________________
	//Images
	static Image mimg=null, snimg=null, uimg=null;
	//Starting Game
	public static void StartGame()
	{
		new mnsweep();
		minesout = 0;continuestars=0;continuemines=0;start = 1;
		x=(int)(resX/28.8); y=(int)(resY/18); w=(int)(resX/14.4); h=(int)(resY/9); icons = 0;
		mineshown = 0; icosdone = 0;
		rpane.repaint();		
		//Images
		//Mine Image
		Image mimgn = null;		
		mimgn = mimg.getScaledInstance((int)(resX/14.4545), (int)(resY/9.0909), java.awt.Image.SCALE_SMOOTH);
		miicon = new ImageIcon(mimgn);
		//Sun Image
		Image simgn=null;
		simgn = snimg.getScaledInstance((int)(resX/14.4545), (int)(resY/9.0909), java.awt.Image.SCALE_SMOOTH);		
		final ImageIcon siicon = new ImageIcon(simgn);
		//Unknown Image
		Image uimgn=null;
		uimgn = uimg.getScaledInstance((int)(resX/14.4545), (int)(resY/9.0909), java.awt.Image.SCALE_SMOOTH);		
		final ImageIcon uiicon = new ImageIcon(uimgn);
		//___________________________________________________________________				
		int minesdone = 0,   //How Many Mines found by Random
		    minefound = 1;   //If Right Random Found		    
		Mines = new int[mines];
		//Setting Value of All Mines
		for(int i=0;i<=mines-1;i++)
			Mines[i] = 0;
		int result = 0;   //Random Number		
		Random r = new Random();
		if(mines>0)
		{
		    do
		    {
		        result = r.nextInt(25-1) + 1;
		        for(int i=0;i<=mines-1;i++)
		        {
		    	    if(Mines[i]==result)
		    	    	minefound = 0;
		        }
		        if(minefound==1)
		        {
		    	    Mines[minesdone] = result;
		    	    minesdone++;
		        }
		        else
		        	minefound = 1;		    
		    }		
		    while(minesdone<mines);
		}
		sgmpane.setVisible(false);
		Loadshowallminestt();
		//Building Game Setup		
		final Timer tr = new Timer();
		TimerTask tt = new TimerTask()
		{			
			public void run() 
			{
				gicons[icons] = new JButton("");
				gicons[icons].setIcon(uiicon);
				gicons[icons].addMouseListener(new MouseListener()
				{
					public void mouseClicked(MouseEvent e){}
					public void mouseEntered(MouseEvent arg0){}
					public void mouseExited(MouseEvent arg0){}
					public void mousePressed(MouseEvent e)
					{						
						minefoundwplaying = 0;
						firstopen = 1;
						String ss = e.getSource().toString().substring(e.getSource().toString().indexOf("[")+1, e.getSource().toString().indexOf(","));
						if(gicons[Integer.parseInt(ss)].isEnabled()==true)
						{
					    for(int ii=0;ii<=mines-1;ii++)
					    {
					    	if(firstopen==1)
					    	{
					        if(gicons[Integer.parseInt(ss)].getName().equals(String.valueOf(Mines[ii])) && gicons[Integer.parseInt(ss)].getIcon()!=miicon)
					        {
					    	    try {					    	    	
					    	    	//for Continue Remarks
					    	    	Remarks.setForeground(Color.red);
					    	    	continuestars=0;
					    	    	if(continuemines>=0)
					    	    		continuemines++;
					    	    	if(continuemines==5)
					    	    		Remarks.setText("Very Bad!!!");
					    	    	else if(continuemines==10)
					    	    		Remarks.setText("Worst!!!");
					    	    	else if(continuemines<5)
					    				Remarks.setText("Mine!!!");
					    	    	//____________________________________
					    	    	minefoundwplaying = 1;
					    	    	minesout++;					    	    	
					    	    	if(levellvl==2)
					    	    	    scores--;
					    	    	rpane.repaint();
					    	    	piclabel.setIcon(cryicon);
					    		    //Showing Clicked Mine
					    		    gicons[Integer.parseInt(ss)].setIcon(miicon);
					    		    if(sound==true)
					    		    {
					    		    	new mnsweep();	
    					    		    //Playing Sound
					    		        minea.PlayAudio();
					    		    }
					    		    //if Lost settings
					    		    lost = 1;
					    		    rpane.repaint();
					    		    //___________________________________________________
					    		    if(levellvl==1 || minesout==mines)
					    		    {					    		    	
					    		    	Remarks.setText("Lost!!!:(");
        					    		//Showing All Mines
	        				    		sallmtr.schedule(sallmtt, 50, 50);
	        				    		//if Yes then restart else close
    		    			    		int selectedoption = JOptionPane.showConfirmDialog(null, "Do You Want to Restart?", "You Lost!", JOptionPane.YES_NO_OPTION);
	    		    		    		if(selectedoption==JOptionPane.NO_OPTION)
	    			   	        		{
	    		    		    			Remarks.setForeground(Color.white);
	    				          			Remarks.setText("Enjoy!!!");
	    				    		    	piclabel.setIcon(smileicon);
    	    				    			firstopen = 0;
	        				    			lost = 0;
		    	    		    			scores=0;
		         							rpane.repaint();
				    	        			start = 0;
					        	    		for(int i=0;i<=24;i++)
					         		    		gicons[i].setVisible(false);
    					        			sbtn.setEnabled(true);
	    				    	    	}
			    		    		    else
        			    	    		{
    			    		    			performRestart();
    			    		    			Remarks.setForeground(Color.white);
	    		    		    			Remarks.setText("Enjoy!!!");
		    	    		    			continuestars=0;
			        		    			continuemines=0;
			        		    			piclabel.setIcon(smileicon);
			    	    	    			minesout = 0;
	    				        		}
					    		    }	    				    		
		    					} catch (NumberFormatException e1) {
    							}					    	    
	    				    }
					        if(gicons[Integer.parseInt(ss)].getIcon()==miicon)
					        	minefoundwplaying = 1;
		    			}
					    }
			    		if(minefoundwplaying == 0 && gicons[Integer.parseInt(ss)].getIcon()!=siicon)
				    	{
			    			//for Continue Remarks
			    			Remarks.setForeground(Color.green);
			    			continuemines=0;
			    			if(continuestars>=0)
			    				continuestars++;
			    			if(continuestars==5)
			    				Remarks.setText("Very Good!!!");
			    			else if(continuestars==10)
			    	    		Remarks.setText("Excellent!!!");
			    			else if(continuestars<5)
			    				Remarks.setText("Star!!!");
			    			//___________________________________________
    						gicons[Integer.parseInt(ss)].setIcon(siicon);
			    		    if(sound==true)
			    		    {
			    		    	new mnsweep();
        						//Playing Sound
        						suna.PlayAudio();
			    		    }
    						piclabel.setIcon(smileicon);    						
    						scores++;
    						icosdone++;    						
    						lost=0;    						
    						rpane.repaint();	    					
		    				//if Won
			    			if(icosdone==25-mines)
				    		{
			    				Remarks.setText("Won!!!:)");
			    				//if Won
			    				lost = 2;
				    		    rpane.repaint();
				    		   //if Yes then restart else close
					    		int selectedoption = JOptionPane.showConfirmDialog(null, "Do You Want to Restart?", "Congratulation! You Won.", JOptionPane.YES_NO_OPTION);
    				    		if(selectedoption==JOptionPane.NO_OPTION)
    				    		{
    				    			Remarks.setForeground(Color.white);
    				    			Remarks.setText("Enjoy!!!");
    				    			piclabel.setIcon(smileicon);
    				    			firstopen = 0;
    				    			lost = 0;
		    		    			scores=0;
	    							rpane.repaint();
				        			start = 0;
				    	    		for(int i=0;i<=24;i++)
				    		    		gicons[i].setVisible(false);
					    			sbtn.setEnabled(true);
    				    		}
		    		    		else
			    	    		{
		    		    			Remarks.setForeground(Color.white);
		    		    			Remarks.setText("Enjoy!!!");
		    		    			performRestart();
		    		    			piclabel.setIcon(smileicon);
		    		    			minesout = 0;
		    		    			continuestars=0;
		    		    			continuemines=0;
    				    		}
						    }
    					}					    
						}
					}
					public void mouseReleased(MouseEvent e)
					{}					
	    	    });				
		   		gicons[icons].setName(String.valueOf(icons));				
				gicons[icons].setFont(new Font("Arial", Font.BOLD, (int)(resX/28.8)));
    			gicons[icons].setBounds(x, y, w, h);
	    		gpane.add(gicons[icons]);
	    		x+=(int)(resX/14.4);
		    	//Increase Lines
    			if(icons==4 || icons==9 || icons==14 || icons==19)
	    		{
		   			x=(int)(resX/28.8);
		    		y+=(int)(resY/9);
			    }
			    else if(icons==24)
				{
    				tr.cancel();
	    		}
	    		icons++;
		    	//To Show Buttons Properly
				gpane.repaint();
			    }			
		    };		    
		    tr.schedule(tt, 0, 1);
		//________________________________________________________________		
	}
	//Showing Level menu
	public static void StartGameMenu()
	{		
		sbtn.setEnabled(false);
		sgmpsx = 0; sgmpsy = (int)(resY/450); sgmsxy = 1; sgmply = (int)(resY/2.25); sgmplx=(int)(resX/2.215);
		sgmpane.setLocation((int)(resX/3.6), (int)(resY/2.25));
		sgmpane.setSize(0, (int)(resY/180));
		sgmpane.setVisible(true);
		//Show Graphics
		final Timer tr = new Timer();
		TimerTask tt = new TimerTask()
		{
			public void run()
			{
				if(sgmsxy==1)
				{
					sgmplx-=(int)(resX/175);
				    sgmpsx+=(int)(resX/78);
				}
				else
				{
					sgmply-=(int)(resY/90);
					sgmpsy+=(int)(resY/45);
				}				
				sgmpane.setBounds(sgmplx, sgmply, sgmpsx, sgmpsy);
				if(sgmpsx>=(int)(resX/2.5))
					sgmsxy=2;
				if(sgmpsy>=(int)(resY/3))
					tr.cancel();
			}
		};		
		tr.schedule(tt, 0, 10);
	}
	static JPanel mmpane = new JPanel();   //Main Menu Panel
	//Text Fields for mines in each level
	static JTextField bgtf = new JTextField("5");
	static JTextField idtf = new JTextField("7");
	static JTextField extf = new JTextField("10");
	//_____________________________________________________
	//Main Menu Pane
	public static void mmpanef()
	{
		bgtf.setEditable(false);
		idtf.setEditable(false);
		extf.setEditable(false);		
		mmpane.setVisible(true);
	}
	//KeyBinding Objects
	static JLabel obj1 = new JLabel();
	private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;
	private static final String mainmenu = "Show Main Menu",
	                            mainmenuetf = "Enable Main Menu Text Fields",
	                            hidemenu = "Hide Game Level Menu",
	                            startgame = "Start The Game",
	                            cheat = "Show All Mines",
	                            enter = "ENTER",
	                            quit = "QUIT",
	                            minimize = "MINIMIZE";
	//Images
	static Image simg=null, cimg=null;
	//Key Binding
	public mnsweep()
	{
		//Contorl + M
	    obj1.getInputMap(IFW).put(KeyStroke.getKeyStroke("control M"), mainmenu);
	    obj1.getActionMap().put(mainmenu, new KeyBSet(1));
	    //Control+Alt+E
	    obj1.getInputMap(IFW).put(KeyStroke.getKeyStroke("control alt E"), mainmenuetf);
	    obj1.getActionMap().put(mainmenuetf, new KeyBSet(2));
	    //Esacpe
	    obj1.getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), hidemenu);
	    obj1.getActionMap().put(hidemenu, new KeyBSet(3));
	    //Control+S
	    obj1.getInputMap(IFW).put(KeyStroke.getKeyStroke("control S"), startgame);
	    obj1.getActionMap().put(startgame, new KeyBSet(4));
	    //Cotrol+Shift+Alt+F3
	    obj1.getInputMap(IFW).put(KeyStroke.getKeyStroke("control shift alt F3"), cheat);
	    obj1.getActionMap().put(cheat, new KeyBSet(5));
	    //Enter
	    obj1.getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), enter);
	    obj1.getActionMap().put(enter, new KeyBSet(6));
	    //Control + Z
	    obj1.getInputMap(IFW).put(KeyStroke.getKeyStroke("control Z"), minimize);
	    obj1.getActionMap().put(minimize, new KeyBSet(7));
	    //Control + X
	    obj1.getInputMap(IFW).put(KeyStroke.getKeyStroke("control X"), quit);
	    obj1.getActionMap().put(quit, new KeyBSet(8));
	    mwin.add(obj1);
	    //Loading Images
	    try {
	    	if(start==0)
	    	{
			simg = ImageIO.read(getClass().getResource("/res/smile.png"));
			cimg = ImageIO.read(getClass().getResource("/res/cry.png"));
			snimg = ImageIO.read(getClass().getResource("/res/sun.png"));
			uimg = ImageIO.read(getClass().getResource("/res/unknown.png"));
			mimg = ImageIO.read(getClass().getResource("/res/mines.png"));
	    	}
			//Background Audio
			URL is = this.getClass().getResource("/res/background.wav");
			AudioInputStream ains = AudioSystem.getAudioInputStream(is);
			bgaudio.audioclip = AudioSystem.getClip();
			bgaudio.audioclip.open(ains);
			//Mine and Sun Audio
			is = this.getClass().getResource("/res/mineshown.wav");
			ains = AudioSystem.getAudioInputStream(is);
			minea.audioclip = AudioSystem.getClip();
			minea.audioclip.open(ains);
			is = this.getClass().getResource("/res/sunshown.wav");
			ains = AudioSystem.getAudioInputStream(is);
			suna.audioclip = AudioSystem.getClip();
			suna.audioclip.open(ains);
		}catch (IOException e){
		}catch (UnsupportedAudioFileException e){
		}catch (LineUnavailableException e){}
	}
	int extra = 0;   //Extra variable used in below function KeyBSet
	//Setting KeyBinding Class
	public class KeyBSet extends AbstractAction
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		int kk = 0;
		KeyBSet(int keycode)
		{			
			kk = keycode;
		}
		public void actionPerformed(ActionEvent arg0)
		{
			if(kk == 1)
				mmbtn.doClick();
			else if(kk==2)
			{
				bgtf.setEditable(true);
				idtf.setEditable(true);
				extf.setEditable(true);
			}
			else if(kk==3)
			{
				if(start==0)
				{
				    if(sgmpane.isVisible()==true)
				    {
				        sgmpane.setVisible(false);
				        sbtn.setEnabled(true);
				    }
				    if(mmpane.isVisible()==true)
				    {
					    cancelbtn.doClick();
				    }
				}
				else if(start==1)
				{
					pausepane.setVisible(true);
					for(int i=0;i<=24;i++)
					{
						gicons[i].setVisible(false);
					}
				}
			}
			else if(kk==4)
				sbtn.doClick();
			else if(kk==5)
			{
				if(start==1)
				{
				    String values="";
				    for(int i=0;i<=mines-1;i++)
					    values+=String.valueOf(Mines[i])+" ";
				    JOptionPane.showMessageDialog(null, values);
				}
			}
			else if(kk==6)
			{
				if(mmpane.isVisible()==true)
					okbtn.doClick();
			}
			else if(kk==7)
				MinB.doClick();
			else if(kk==8)
				ExitB.doClick();
		}
	}	
	public static  void OnlineAdvTimFunc(final int LineNo, String FLine, String SLine, String TLine, Image OnlineImg)
	{
		FirstL = FLine;
		SecondL = SLine;
		ThirdL = TLine;
		OImg = OnlineImg;
		if(OAdno<(LineNo+1)/2)
		{
			OAdno++;
			FirstLine+=3;
			SecondLine+=3;
			ThirdLine+=3;
		}
		else
		{
			OAdno=1;
			FirstLine = 0;
			SecondLine = 1;
			ThirdLine = 2;
		}
		lineno = 0;
		if(mmpane.isVisible()==false && sgmpane.isVisible()==false)
		{
			gpane.repaint();
			AdOpacityTimerSettings();
			AdOpacityTimer.schedule(AdOpacityTimerTask, 50, 50);
			AdOpacityChanging = 1;
		}
		try
		{
			Thread.sleep(10000);
			ReadWebData();
		}
		catch(Exception ex) {}
	}
	public static void ReadWebData()
	{
		new Thread()
		{
			public void run()
			{
				try
				{
					URL url = new URL("https://bitnbyteComputers.co.in/SoftwareAds/MinesweeperAdv/Text.txt");
					BufferedReader read = new BufferedReader(
							new InputStreamReader(url.openStream()));
					int LNum = 0;
					String FLin="", SLin="", TLin = "";
					String i;
					while ((i = read.readLine()) != null)
					{
						if(LNum==FirstLine)
							FLin = i;
						if(LNum==SecondLine)
							SLin = i;
						if(LNum==ThirdLine)
							TLin = i;
						LNum++;
					}
					read.close();
					URL url2 = new URL("https://bitnbytecomputers.co.in/SoftwareAds/MinesweeperAdv/Ad"+OAdno+".png");
					Image OAdvImg = ImageIO.read(url2);
					OnlineAdvTimFunc(LNum, FLin, SLin, TLin, OAdvImg);
				}
				catch(Exception ex) {}
			}
		}.start();
	}
	//Main Menu Buttons
	static JButton okbtn = new JButton("OK");   //Ok Button
	static JButton cancelbtn = new JButton("Cancel");   //Cancel Button
	//Minimize & Quit Button
	static JButton ExitB = new JButton("X");
	static JButton MinB = new JButton("_");
	//____________________________________________________
	static Color fwcolor = Color.green;   //FreeWare Color
	static Audio bgaudio=new Audio("");
	static JButton sonoffbtn = new JButton("ON");  //Sound On/Off Button
	static boolean soundbefmin = true;
	static int BnbAdno = 1;
	static int OAdno = 1;
	static int FirstLine = 0, SecondLine = 1, ThirdLine = 2;
	static Image OImg;
	static String FirstL, SecondL, ThirdL;
	static int lineno = 0;
	//Ad Panels
	static JPanel LeftAdPanel = new JPanel();
	static float AdOpacity = 0.1f;
	static Timer AdOpacityTimer = new Timer();
	static TimerTask AdOpacityTimerTask = null;
	static int AdOpacityChanging = 0;
	//Opacity Timer Settings
	public static void AdOpacityTimerSettings()
	{
		AdOpacityTimer = new Timer();
		AdOpacityTimerTask = new TimerTask()
		{
			public void run()
			{
				if(AdOpacity<1.0f)
				{
					AdOpacity+=0.1f;
					LeftAdPanel.repaint();
				}
				else
				{
					AdOpacity=0.1f;
					AdOpacityChanging = 0;
					AdOpacityTimer.cancel();
				}
			}
		};		
	}
	public static void MainWindow() throws IOException
	{
		//Audio Clips				
		//____________________________________________
		//Smile & Cry Images
		//Smile Image
		Image simgn = null;		
		simgn = simg.getScaledInstance((int)(resX/2.88), (int)(resY/3), java.awt.Image.SCALE_SMOOTH);		
		smileicon = new ImageIcon(simgn);
		//Cry Image
		Image cimgn = null;
		cimgn = cimg.getScaledInstance((int)(resX/2.88), (int)(resY/3), java.awt.Image.SCALE_SMOOTH);		
		cryicon = new ImageIcon(cimgn);
		//Main Window
		//___________________________________________________________________
		//Main Window
		mwin.setUndecorated(true);
		//To Close Program Totally
		mwin.addWindowListener(new WindowListener()
		{
			public void windowActivated(WindowEvent arg0){}
			public void windowClosed(WindowEvent arg0){}
			public void windowClosing(WindowEvent arg0){System.exit(0);}
			public void windowDeactivated(WindowEvent arg0){}
			public void windowDeiconified(WindowEvent arg0){}
			public void windowIconified(WindowEvent arg0){}
			public void windowOpened(WindowEvent arg0){}
		});
		//Set Sound to null if window state chnaged
		mwin.addWindowStateListener(new WindowStateListener()
		{
			public void windowStateChanged(WindowEvent e)
			{
				JFrame f = (JFrame) e.getSource();
				if(f.getState() == Frame.ICONIFIED)
				{
					soundbefmin = sound;
					if(sound==true)
					{						
						sonoffbtn.doClick();					
					}
				}
				else
				{
					if(soundbefmin == true)
						sonoffbtn.doClick();
				}
			}			
		});
		mwin.setLayout(null);
		mwin.setExtendedState(mwin.getExtendedState()|JFrame.MAXIMIZED_BOTH);
		mwin.setVisible(true);
		mwin.setIconImage(mimg);
		bgaudio.PlayAudio();
		//Main Panel
		mpane = new JPanel();
		mpane.setLayout(null);
		mpane.setBounds(0, 0, resX, resY);
		mpane.setBackground(new Color(127, 127, 127));
		mwin.add(mpane);
		//Start Game menu Panel
		sgmpane = new JPanel()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				g.setColor(Color.black);
				g.fillRect(0, 0, (int)(resX/2.4), (int)(resX/3));
				g.setFont(new Font("Arial", Font.BOLD, (int)(resY/36)));
				g.setColor(Color.white);
				g.drawString("Choose Level Which u Want?", (int)(resX/144), (int)(resY/11.25));
			}
		};
		sgmpane.setLayout(null);
		sgmpane.setBounds((int)(resX/3.6), (int)(resY/3.6), (int)(resX/2.4), (int)(resY/3));
		sgmpane.setVisible(false);
		mpane.add(sgmpane);
		//Main Menu Panel
		mmpane = new JPanel()
		{
			/**
			 * .
			 */
			private static final long serialVersionUID = 1L;

			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				g.setColor(Color.black);
				g.fillRect(0, 0, (int)(resX/2.4), (int)(resY/3));
				g.setColor(Color.white);
				g.setFont(new Font("Algerian", Font.BOLD, (int)(resX/48)));
				g.drawString("MineSweeper Main Menu", (int)(resX/16), (int)(resY/22.5));
				g.setFont(new Font("Arial", Font.CENTER_BASELINE, (int)(resX/72)));
				g.drawString("Mines in Beginner:-", (int)(resX/28.8), (int)(resY/9));
				g.drawString("Mines in Intemediate:-", (int)(resX/28.8), (int)(resY/6.4285));
				g.drawString("Mines in Expert:-", (int)(resX/28.8), (int)(resY/5));
				g.drawString("Sound On/Off:-", (int)(resX/28.8), (int)(resY/4.0909));
				g.drawString("Mode:-", (int)(resX/28.8), (int)(resY/3.4615));
				g.drawRect((int)(resX/7.2361), (int)(resY/4.52261), (int)(resX/10.14085), (int)(resY/33.3333));
				g.drawRect((int)(resX/12.10084), (int)(resY/3.76569), (int)(resX/10.14085), (int)(resY/33.3333));
			}
		};
		mmpane.setBounds((int)(resX/3.6), (int)(resY/3.6), (int)(resX/2.4), (int)(resY/3));		
		mmpane.setLayout(null);		
		mmpane.setVisible(false);
		mpane.add(mmpane);
		//Pause Panel
		pausepane = new JPanel()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				g.setColor(Color.black);
				g.fillRect(0, 0, (int)(resX/2.4), (int)(resY/3));
				g.setColor(Color.white);
				g.setFont(new Font("Arial", Font.CENTER_BASELINE, (int)(resX/14.4)));
				g.drawString("PAUSED", (int)(resX/14.4), (int)(resY/10));
			}
		};
		pausepane.setBounds((int)(resX/3.6), (int)(resY/4.5), (int)(resX/2.4), (int)(resY/3));
		pausepane.setVisible(false);
		pausepane.setLayout(null);
		mpane.add(pausepane);
		//Restart Button
		JButton resbtn = new JButton("Restart");
		resbtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				performRestart();
				pausepane.setVisible(false);
			}
		});		
		resbtn.setFont(new Font("Arial",Font.CENTER_BASELINE, (int)(resX/72)));
		resbtn.setBounds((int)(resX/28.8), (int)(resY/3.6), (int)(resX/12), (int)(resY/30));
		pausepane.add(resbtn);
		//Close Button
		JButton closebtn = new JButton("Cancel");
		closebtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				for(int i=0;i<=24;i++)
				{
					gicons[i].setVisible(true);
				}
				pausepane.setVisible(false);
			}		
		});
		closebtn.setFont(new Font("Arial",Font.CENTER_BASELINE, (int)(resX/72)));
		closebtn.setBounds((int)(resX/6), (int)(resY/3.6), (int)(resX/12), (int)(resY/30));
		pausepane.add(closebtn);
		//Quit Button
		JButton quitbtn = new JButton("Quit");
		quitbtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Remarks.setForeground(Color.white);
				Remarks.setText("Enjoy!!!");
				piclabel.setIcon(smileicon);
				firstopen = 0;
    			lost = 0;
    			scores=0;
				rpane.repaint();
    			start = 0;
	    		for(int i=0;i<=24;i++)
		    		gicons[i].setVisible(false);
    			sbtn.setEnabled(true);
    			pausepane.setVisible(false);
			}
		});
		quitbtn.setFont(new Font("Arial",Font.CENTER_BASELINE, (int)(resX/72)));
		quitbtn.setBounds((int)(resX/3.34883), (int)(resY/3.6), (int)(resX/7.5), (int)(resY/30));
		pausepane.add(quitbtn);
		//_________________________________________________________________________
		//Text Fields
		//Beginner
		bgtf.addKeyListener(new KeyListener()
		{
			public void keyPressed(KeyEvent ev){}
			public void keyReleased(KeyEvent arg0){}
			public void keyTyped(KeyEvent e)
			{				
				//Accept only digits, backspace, delete
				char c = e.getKeyChar();
		           if (!(Character.isDigit(c) ||
		              (c == KeyEvent.VK_BACK_SPACE) ||
		              (c == KeyEvent.VK_DELETE))) {
		                e.consume();
		              }
		           else
		           {
		        	   if(c!=KeyEvent.VK_BACK_SPACE && c!=KeyEvent.VK_DELETE)
		        	   {
		        	       String value = bgtf.getText()+String.valueOf(c);
		        	       if(Integer.parseInt(value)>=20)
		        		       e.consume();
		        	   }
		           }
			}
		});
		bgtf.setBounds((int)(resX/5.76), (int)(resY/11.25), (int)(resX/14.4), (int)(resY/36));
		bgtf.setEditable(false);
		mmpane.add(bgtf);
		//Intermediate
		idtf.addKeyListener(new KeyListener()
		{
			public void keyPressed(KeyEvent ev){}
			public void keyReleased(KeyEvent arg0){}
			public void keyTyped(KeyEvent e)
			{				
				//Accept only digits, backspace, delete
				char c = e.getKeyChar();
		           if (!(Character.isDigit(c) ||
		              (c == KeyEvent.VK_BACK_SPACE) ||
		              (c == KeyEvent.VK_DELETE))) {
		                e.consume();
		              }
			}
		});
		idtf.setBounds((int)(resX/5.3333), (int)(resY/7.5), (int)(resX/14.4), (int)(resY/36));
		idtf.setEditable(false);
		mmpane.add(idtf);
		//Expert
		extf.addKeyListener(new KeyListener()
		{
			public void keyPressed(KeyEvent ev){}
			public void keyReleased(KeyEvent arg0){}
			public void keyTyped(KeyEvent e)
			{
				//Accept only digits, backspace, delete
				char c = e.getKeyChar();
		           if (!(Character.isDigit(c) ||
		              (c == KeyEvent.VK_BACK_SPACE) ||
		              (c == KeyEvent.VK_DELETE))) {
		                e.consume();
		              }
			}
		});
		extf.setBounds((int)(resX/6.5454), (int)(resY/5.625), (int)(resX/14.4), (int)(resY/36));
		extf.setEditable(false);
		mmpane.add(extf);
		//_________________________________________________________________
		//Sound On/Off Button		
		if(sound==true)
		{			
			bgaudio.PlayAudio();
			bgaudio.SetLoop(Clip.LOOP_CONTINUOUSLY);
			sonoffbtn.setText("ON");
			sonoffbtn.setBackground(Color.green);
			sonoffbtn.setBounds((int)(resX/5.3333), (int)(resY/4.5), (int)(resX/20.57142), (int)(resY/36));
		}
		else
		{			
			bgaudio.StopAudio();
			sonoffbtn.setText("OFF");
			sonoffbtn.setBackground(Color.red);
			sonoffbtn.setBounds((int)(resX/7.2), (int)(resY/4.5), (int)(resX/20.5714), (int)(resY/36));
		}
		sonoffbtn.setFocusable(false);
		sonoffbtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if(sound==true)
				{					
					bgaudio.SetVolume(0);
					sound=false;
					sonoffbtn.setText("OFF");
					sonoffbtn.setBackground(Color.red);
					sonoffbtn.setLocation((int)(resX/7.2), (int)(resY/4.5));
				}
				else
				{
					bgaudio.SetVolume(40);
					sound=true;
					sonoffbtn.setText("ON");
					sonoffbtn.setBackground(Color.green);
					sonoffbtn.setLocation((int)(resX/5.33333), (int)(resY/4.5));
				}
			}			
		});		
		mmpane.add(sonoffbtn);
		//_________________________________________________________________
		//Level Hard Easy Button
		final JButton lhardeasybtn = new JButton("HARD");
		if(levellvl==1)
		{
			lhardeasybtn.setText("EASY");
			lhardeasybtn.setBackground(Color.green);
			lhardeasybtn.setBounds((int)(resX/12), (int)(resY/3.75), (int)(resX/20.57142), (int)(resY/36));
		}
		else
		{
			lhardeasybtn.setText("HARD");
			lhardeasybtn.setBackground(Color.red);
			lhardeasybtn.setBounds((int)(resX/7.578947), (int)(resY/3.75), (int)(resX/12.857142), (int)(resY/36));
		}
		lhardeasybtn.setFocusable(false);
		lhardeasybtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if(levellvl==1)
				{
					levellvl=2;
					lhardeasybtn.setText("HARD");
					lhardeasybtn.setBackground(Color.red);
					lhardeasybtn.setLocation((int)(resX/7.578947), (int)(resY/3.75));
				}
				else
				{
					levellvl=1;
					lhardeasybtn.setText("EASY");
					lhardeasybtn.setBackground(Color.green);
					lhardeasybtn.setLocation((int)(resX/12), (int)(resY/3.75));
				}
			}			
		});		
		mmpane.add(lhardeasybtn);
		//_________________________________________________________________
		//OK Button		
		okbtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{				
				if(bgtf.getText().length()>0)
				    minesbg = Integer.parseInt(bgtf.getText());
				else
					minesbg = 0; 
				if(idtf.getText().length()>0)
				    minesid = Integer.parseInt(idtf.getText());
				else
					minesid = 0;
				if(extf.getText().length()>0)
				    minesex = Integer.parseInt(extf.getText());
				else
					minesex = 0;
				mmpane.setVisible(false);
			}			
		});
		okbtn.setFont(new Font("Arial", Font.CENTER_BASELINE, (int)(resX/96)));
		okbtn.setBounds((int)(resX/2.93877), (int)(resY/3.3333), (int)(resX/14.4), (int)(resY/36));
		mmpane.add(okbtn);
		//_________________________________________________________________
		//Cancel Button		
		cancelbtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				levellvl = oldlevellvl;
				sound = oldsound;
				mmpane.setVisible(false);
				gpane.repaint();
			}			
		});
		cancelbtn.setFont(new Font("Arial", Font.CENTER_BASELINE, (int)(resX/96)));
		cancelbtn.setBounds((int)(resX/3.789473642), (int)(resY/3.3333), (int)(resX/14.4), (int)(resY/36));
		mmpane.add(cancelbtn);
		//_________________________________________________________________
		//________________________________________________________________________		
		//Beginner Level Btn
		JButton bgbtn = new JButton("Beginner");
		bgbtn.addActionListener(new ActionListener()
		{public void actionPerformed(ActionEvent ev){mines = minesbg;StartGame();}});		
		bgbtn.setBounds((int)(resX/28.8), resY/9, (int)(resX/7.2), resY/18);
		bgbtn.setFont(new Font("Arial", Font.CENTER_BASELINE, (int)(resX/72)));
		sgmpane.add(bgbtn);
		//Intermediate Level Btn
		JButton idbtn = new JButton("Intermediate");
		idbtn.addActionListener(new ActionListener()
		{public void actionPerformed(ActionEvent ev){mines = minesid;StartGame();}});
		idbtn.setBounds((int)(resX/4.114285), resY/9, (int)(resX/7.2), resY/18);
		idbtn.setFont(new Font("Arial", Font.CENTER_BASELINE, (int)(resX/72)));
		sgmpane.add(idbtn);
		//Expert Level Btn
		JButton exbtn = new JButton("Expert");
		exbtn.addActionListener(new ActionListener()
		{public void actionPerformed(ActionEvent ev){mines = minesex;StartGame();}});
		exbtn.setBackground(Color.red);
		exbtn.setBounds((int)(resX/7.2), (int)(resY/4.5), (int)(resX/7.2), resY/18);
		exbtn.setFont(new Font("Arial", Font.CENTER_BASELINE, (int)(resX/72)));
		sgmpane.add(exbtn);
		//_________________________________________________________________________
		//Title Pane
		JPanel tpane = new JPanel()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				g.setColor(Color.blue);
				g.fillRect(0, 0, resX, resY/30);
				g.setColor(Color.black);
				g.setFont(new Font("Arial", Font.BOLD, (int)(resX/72)));
				g.drawString("Bit n Byte MineSweeper v1.0", (int)(resX/48), (int)(resY/45));
				g.drawImage(mimg, 0, 0, (int)(resX/48), resY/30, null);
			}
		};
		tpane.setBackground(Color.blue);
		tpane.setBounds(0, 0, resX, resY/30);
		tpane.setLayout(null);
		mpane.add(tpane);
		//Exit Button		
		ExitB.setFocusable(false);
		ExitB.setFont(new Font("Arial", Font.BOLD, (int)(resX/65)));
		ExitB.addActionListener(new ActionListener()
		{
			//To Exit Program
			public void actionPerformed(ActionEvent arg0){System.exit(0);}
			
		});
		ExitB.setBounds((int)(resX/1.05), 0, (int)(resX/20), resY/30);
		ExitB.setBackground(Color.RED);
		tpane.add(ExitB);
		//Minimize Button
		MinB.setFocusable(false);
		MinB.setFont(new Font("Arial", Font.BOLD, (int)(resX/72)));
		MinB.addActionListener(new ActionListener()
		{
			//To Minimize Program
			public void actionPerformed(ActionEvent arg0){mwin.setState(Frame.ICONIFIED);}
			
		});
		MinB.setBounds((int)(resX/1.1), 0, (int)(resX/20), resY/30);
		MinB.setBackground(Color.RED);
		tpane.add(MinB);
		//GamePanel
		gpane = new JPanel()
		{
			public void paintComponent(final Graphics g)
			{
				final Graphics2D g2d = (Graphics2D)g;
				g2d.setColor(new Color(140,140,140));
				g2d.fillRect(0, 0, (int)(resX/2.4), resY);				
			}
		};
		gpane.setLayout(null);
		gpane.setBounds((int)(resX/3.6), resY/30, (int)(resX/2.4), resY);
		gpane.setBackground(new Color(140, 140, 140));
		mpane.add(gpane);		
		//Picture Label		
		piclabel.setIcon(smileicon);
		piclabel.setBounds((int)(resX/28.8), (int)(resY/1.57894), (int)(resX/2.88), resY/3);
		gpane.add(piclabel);
		//_______________________________________________________
		//Game Menu Panel
		final JPanel gmpane = new JPanel()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				g.setColor(new Color(127, 127, 127));
				g.fillRect(0, 0, (int)(resX/3.6), resY);
				g.setColor(Color.white);
				g.setFont(new Font("Arial", Font.BOLD, resX/48));
				g.drawString("MineSweeper v1.0", (int)(resX/20), resY/18);
				g.setFont(new Font("Arial", Font.BOLD, (int)(resX/57.6)));
				g.drawString("Made By Bit n Byte Team", (int)(resX/24), resY/10);
				g.drawString("ph:-+91 9815341886", resX/resX, (int)(resY/4.0909));
				g.drawString("Created On:-26-12-2018", resX/resX, (int)(resY/3.6));
				g.drawString("Released On:-31-12-2018", resX/resX, (int)(resY/3.214285));
				g.setFont(new Font("Arial", Font.BOLD, (int)(resX/18)));
				g.setColor(fwcolor);
				g.drawString("FreeWare", (int)(resX/144), (int)(resY/2.307692));				
			}
		};
		gmpane.setLayout(null);
		gmpane.setBounds(0, resY/30, (int)(resX/3.6), resY);
		gmpane.setBackground(new Color(127, 127, 127));
		mpane.add(gmpane);
		//Left Column Ads Panel
		LeftAdPanel = new JPanel()
		{
			public void paintComponent(Graphics g)
			{
				Graphics2D g2d = (Graphics2D)g;
				if(FirstL!=null)
				{
					if(AdOpacity<=1.0f && AdOpacityChanging==1)
					{
						g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, AdOpacity));
					}
					//Phone Number
					g.setFont(new Font("Arial", Font.CENTER_BASELINE, (int)(resX/100)));
					FontMetrics fm = g.getFontMetrics();
					g.setColor(Color.white);
					g.drawString(ThirdL, 10, resY/60);
					g2d.setColor(Color.blue);
					g2d.setFont(new Font("Arial", Font.BOLD, resX/72));
					fm = g.getFontMetrics();
					g2d.drawString(FirstL, (int)(resX/3.6)/2-(fm.stringWidth(FirstL))/2, resY/25);
					g2d.setColor(Color.yellow);
					g2d.setFont(new Font("Arial", Font.BOLD, resX/96));
					String SLines[] = SecondL.split("&&");
					for(int i=0;i<SLines.length;i++)
					{
						fm = g.getFontMetrics();
						g2d.drawString(SLines[i], (int)(resX/3.6)/2-(fm.stringWidth(SLines[i]))/2, (int)(resY/22.5)+(i+1)*resY/60);
					}
					g2d.drawImage(OImg, (int)(resX/9.66667), resY/7, (int)(resX/14.4), resY/9, null);
				}
			}
		};
		LeftAdPanel.setBounds(0, (int)(resY/2.2), (int)(resX/3.6), resY/3);
		LeftAdPanel.setOpaque(false);
		gmpane.add(LeftAdPanel);
		ReadWebData();
		//Start Button		
		sbtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				if(mmpane.isVisible()==false)
				    StartGameMenu();
			}			
		});
		sbtn.setFont(new Font("Arial", Font.CENTER_BASELINE, (int)(resX/57.6)));
		sbtn.setBounds((int)(resX/14.4), (int)(resY/1.28571), (int)(resX/9.6), (int)(resY/18));
		gmpane.add(sbtn);
		//Menu Button
		mmbtn.setFont(new Font("Arial", Font.CENTER_BASELINE, (int)(resX/57.6)));
		mmbtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if(sgmpane.isVisible()==false)
				{
					if(start==0)
					{
						oldlevellvl = levellvl;
				        oldsound = sound;
				        mmpanef();
					}
				}							
			}			
		});
		mmbtn.setBounds((int)(resX/19.2), (int)(resY/1.40625), (int)(resX/7.2), resY/18);
		gmpane.add(mmbtn);
        //Right Panel
		rpane = new JPanel()
		{
			private static final long serialVersionUID = 1L;

			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				if(lost==0)
				    g.setColor(Color.white);
				else if(lost==1)
					g.setColor(Color.red);
				else if(lost==2)
					g.setColor(Color.green);
				if(start==1)
				{
				    g.setFont(new Font("Arial", Font.CENTER_BASELINE, resX/36));				
				    g.drawString("Scores: ", (int)(resX/28.8), resY/18);
				    if(scores>0)
					    g.setColor(Color.green);
				    else if(scores<0)
					    g.setColor(Color.red);
				    else if(scores==0)
					    g.setColor(Color.white);
				    if(lost==1)
					    g.setColor(Color.red);
				    g.drawString(String.valueOf(scores), (int)(resX/7.02439), (int)(resY/16.981132));
				}
				//Ads
				g.setColor(Color.white);
				//Bit_n_Byte Computer Centers
				g.drawRect(resX/36, (int)(resY/7.5), (int)(resX/3.6923), (int)(resY/7.6));
				g.setFont(new Font("Arial", Font.CENTER_BASELINE, (int)(resX/57.6)));
				g.setColor(Color.yellow);
				g.drawString("Bit_n_Byte Computer Center", (int)(resX/20.57142), resY/6);
				g.drawString("Smadh Road", (int)(resX/8.470588), resY/5);
				g.drawString("Batala-143505", resX/9, (int)(resY/4.2857142));
				try {
					g.drawImage(ImageIO.read(getClass().getResource("/res/BnbAd"+BnbAdno+".jpg")), (int)(resX/28.8), (int)(resY/3.5), (int)(resX/4.11428), resY/3, null);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		};
		//Start Ad Timer
		Timer tim = new Timer();
		TimerTask timtask = new TimerTask()
		{
			public void run()
			{
				URL u = getClass().getResource("/res/BnbAd"+(BnbAdno+1)+".jpg");				
				if (u != null) {
					BnbAdno++;					
				}
				else
				{
					BnbAdno = 1;
				}
				rpane.repaint();
			}
		};
		tim.schedule(timtask, 10000, 10000);
		rpane.setBounds((int)(resX/1.44), resY/30, (int)(resX/3.272727), (int)(resY/1.344827586));
		rpane.setBackground(Color.GRAY);
		rpane.setLayout(null);
		mpane.add(rpane);		
		mwin.repaint();
		//FreeWare Change Color Timer
		Timer fwtr = new Timer();
		TimerTask fwtt = new TimerTask()
		{
			public void run()
			{
				if(fwcolor == Color.red)
					fwcolor = Color.green;
				else if(fwcolor == Color.green)
					fwcolor = Color.red;
				gmpane.repaint();
			}
		};
		fwtr.schedule(fwtt, 0, 500);
		//Setting Remarks Label		
		Remarks.setFont(new Font("Arial", Font.BOLD, (int)(resX/28.8)));
		Remarks.setForeground(Color.white);
		Remarks.setBounds(0, (int)(resY/1.28571), resX/36, resY/15);
		rpane.add(Remarks);
	}
	public static void main(String args[]) throws IOException
	{
		getResolution();
		new mnsweep();
		MainWindow();		
	}
}
