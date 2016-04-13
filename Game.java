import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Game extends JFrame{//main game class
	CardLayout c = new CardLayout();
	JPanel main_panel;
	static Game g;
	Introduction intro = new Introduction();
	MoveSquare2 ma;
	Maze m;
	Reproduction r;
	BreakIn b;
	Battle ba;
	int difficulty_level = 1;
	JButton restart;
	public static void main(String[] args){//main method
		g = new Game();//make new game panel
	}
	public Game(){//constructor
		super("Viral Infection");
		setResizable(false);
		setVisible(true);//jframe utility methods
		setSize(700,700);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		main_panel = new JPanel();
		main_panel.setLayout(c);//set layout to a card layout
		main_panel.add(intro,"intro");//add all instances as cards
		c.show(main_panel,"intro");
		setContentPane(main_panel);
		setFocusable(true);
		
	}
	class GameButton extends JButton{
		GameButton(String text){
			super(text);
			this.setBackground(new Color(46,204,113));
			this.setFont(new Font("Verdana",Font.BOLD,12));
			this.setRolloverEnabled(true);
			this.setForeground(Color.WHITE);
			setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		}
		GameButton(String text, String toolTip){
			super(text);
			this.setBackground(new Color(46,204,113));
			this.setToolTipText(toolTip);
			this.setFont(new Font("Verdana",Font.BOLD,12));
			this.setForeground(Color.WHITE);
			setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		}
		void setDefaultBackground(){
			this.setBackground(new Color(192,57,43));
		}
	}
	class WhiteBloodCell extends Rectangle{
		int id;
		WhiteBloodCell(int x, int y, int width, int height, int id){
			super(x,y,width,height);
			this.id = id;
		}
	}
	class Introduction extends JPanel{//introduction panel class extends jpanel
		JLabel label;//all necessary jcomponents are global variables
		JPanel label_panel;
		JPanel difficulty_panel;
		JPanel difficulty;
		JPanel instructions;
		JPanel start_panel;
		GameButton easy;
		GameButton medium;
		GameButton hard;
		GameButton start;
		JLabel difficulty_label;
		Introduction(){//constructor
			setLayout(new BorderLayout());
			label_panel = new JPanel();
			label = new JLabel("Viral Infection");//initialize these jcomponents and add them to a panel
			label.setFont(new Font("Verdana",Font.BOLD,40));
			label.setBackground(new Color(255,26,25));
			label_panel.setBackground(new Color(255,26,25));
			label_panel.add(label);
			
			difficulty = new JPanel();
			difficulty.setLayout(new GridLayout(3,1));
			difficulty.setBackground((new Color(255,26,25)));
			easy = new GameButton("Easy","Easy Difficulty");
			medium = new GameButton("Medium","Medium Difficulty");
			hard = new GameButton("Hard","Hard Difficulty");
			difficulty.add(easy);
			difficulty.add(medium);
			difficulty.add(hard);
						
			start_panel = new JPanel();
			start_panel.setBackground((new Color(255,26,25)));
			start_panel.setLayout(new GridLayout(1,1));
			start = new GameButton("START","Ready to play?");
			start_panel.add(start);
			
			add(label_panel,BorderLayout.NORTH);//add the jcomponents
			add(difficulty,BorderLayout.WEST);
			add(start_panel,BorderLayout.SOUTH);
			
			//add actionlisteners where they are required
			easy.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					easy.setBackground(new Color(100, 100, 100));
					medium.setDefaultBackground();
					hard.setDefaultBackground();
					difficulty_level = 1;
					repaint();
				}
			});
			medium.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					medium.setBackground(new Color(100, 100, 100));
					easy.setDefaultBackground();
					hard.setDefaultBackground();
					difficulty_level = 2;
					repaint();
				}
			});
			hard.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					hard.setBackground(new Color(100, 100, 100));
					easy.setDefaultBackground();
					medium.setDefaultBackground();
					difficulty_level = 3;
					repaint();
				}
			});
			
			start.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					InstructionalPanel l = new InstructionalPanel();
					l.title.setText("Entering the Human");
					l.info.setText("As you start your journey as a virus, your first goal is to find a human to infect. Many viruses are airbourne, or"
							+ " transmitted \nthrough the air. \n\n Instructions: Use your mouse to guide the virus through the maze. Make sure that"
							+ " you do not touch any area outside the \nmaze, or else its game over. \n\nHave fun!");
					l.next.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							m = new Maze();
							main_panel.add(m, "m");
							c.show(main_panel, "m");
						}
					});
					main_panel.add(l,"l");
					c.show(main_panel,"l");
				}
			});
		}
		public void paintComponent(Graphics g){
			setBackground(new Color(255,26,25));
			super.paintComponent(g);
			g.drawImage(Toolkit.getDefaultToolkit().getImage("virus.png"),100,75,100,100,this);
			g.drawString("Meet Fred",200,75);
			g.drawString("Fred is a virus just trying to do his job infecting humans.",200,100);
			g.drawString("As you play this game, you will guide Fred on his adventure!",200,125);
			g.drawString("Select your difficulty on the left, and then press start to being playing. Have fun!",200,150);
			if(difficulty_level == 1){
				g.drawString("Easy Difficulty",200,250);
				g.drawString("This difficulty level should be easy for most players.",200,275);
				g.drawString("It should not be hard to win the game.",200,300);
			}
			else if(difficulty_level == 2){
				g.drawString("Medium Difficulty",200,250);
				g.drawString("This difficulty level should be moderate for players.",200,275);
				g.drawString("Players might encounter some difficulty while playing.",200,300);
			}
			else if(difficulty_level==3){
				g.drawString("Hard Difficulty",200,250);
				g.drawString("This difficulty level should be challenging for many players",200,275);
				g.drawString("Players will experience a challenge in finishing the game.",200,300);
			}
		}
	}
	class Maze extends JPanel implements MouseMotionListener{//class Maze for first minigame extends JPanel implements MouseMotionListener
		//declare all global variables here
		//have a few rectangles for the maze path
		int mouseX = 10;
		int mouseY = 10;
		Rectangle maze1 = new Rectangle(0,0,50,200);
		Rectangle maze2 = new Rectangle(0,100,200,30);
		Rectangle maze3 = new Rectangle(100,100,50,240);
		Rectangle maze4 = new Rectangle(100,220,50,120);
		Rectangle maze5 = new Rectangle(100,280,500,40);
		Rectangle maze6 = new Rectangle(600,280,80,40);
		Rectangle playerRect = new Rectangle(0,0,20,20);
		Robot bot;
		boolean done = false, win = false;
		Timer runGame = new Timer(24, new ActionListener(){//timer called runGame refresh every 24 miliseconds
			public void actionPerformed(ActionEvent e){
				playerRect = new Rectangle(mouseX-10,mouseY-10,20,20);//create a Rectangle at the coordinates the square are at now
				if(playerRect.intersects(maze1)||playerRect.intersects(maze2)||playerRect.intersects(maze3)||playerRect.intersects(maze4)||playerRect.intersects(maze5)){}//check if this rectangle is not intersecting any of the maze rectangles
				//if it isn't, end the game
				else if(playerRect.x>=maze6.x&&playerRect.y>=maze6.y-100&&playerRect.y<=maze6.y+200){
					win = true;
				}
				else{
					done = true;
				}
			}
		});
		Timer repaintTimer = new Timer(24, new ActionListener(){//timer called repaintTimer refresh every 24 miliseconds to repaints
			public void actionPerformed(ActionEvent e){
				repaint();
			}
		});
		Maze(){//constructor
			try{
			bot = new Robot();
			}catch(AWTException e){
			e.printStackTrace();
			}
			addMouseMotionListener(this);//add mouse motion listener
			setFocusable(true);
			bot.mouseMove(30,30);
		}
		public void mouseMoved(MouseEvent e){//mouse moved method
			mouseX = e.getX();//save coordinates of a square around the mouse to a global variable
			mouseY = e.getY();
			runGame.start();
			repaintTimer.start();
		}
		public void mouseDragged(MouseEvent e){} //have other unused mouse motion listener methods
		public void paintComponent(Graphics g){//paintComponent
			//draw the maze rectangles as a different color than the background
			setBackground(Color.BLUE);
			super.paintComponent(g);
			requestFocus();
			g.drawImage(Toolkit.getDefaultToolkit().getImage("clouds.png"),0,0,700,700,this);
			if(!done&&!win){
				g.setColor(Color.RED);
				g.fillRect(maze1.x,maze1.y,maze1.width,maze1.height);
				g.fillRect(maze2.x,maze2.y,maze2.width,maze2.height);
				g.fillRect(maze3.x,maze3.y,maze3.width,maze3.height);
				g.fillRect(maze4.x,maze4.y,maze4.width,maze4.height);
				g.fillRect(maze5.x,maze5.y,maze5.width,maze5.height);
				g.setColor(Color.PINK);
				g.drawImage(Toolkit.getDefaultToolkit().getImage("mouth.png"),maze6.x,maze6.y-100,maze6.width,maze6.height+100,this);
				g.setColor(Color.GREEN);
				g.drawImage(Toolkit.getDefaultToolkit().getImage("virus.png"),playerRect.x,playerRect.y,playerRect.width,playerRect.height,this);
				//draw the human-controlled rectangle at the correct coordinates
				//if they lost, draw a string saying so
			}
			else if(done){
				LosePanel l = new LosePanel();
				main_panel.add(l,"l");
				c.show(main_panel, "l");
			}
			if(win){
				runGame.stop();
				repaintTimer.stop();
				InstructionalPanel r = new InstructionalPanel();
				r.title.setText("Moving Through the Bloodstream");
				r.title.setBounds(new Rectangle(25,0,500,50));
				r.info.setText("Now that you are in the body, it is time for you to find a cell to infect. A virus does this by moving through"
						+ " the bloodstream. \nHowever, as it tries to do this, the body launches an attack. Leukocytes, or white blood cells,"
						+ " approach the virus and try to kill it. The virus needs to avoid all of these white blood cells to infect its target."
						+ "\n\nInstructions: Use the arrow keys to move up and down. Make sure you avoid all of the white blood cells, shown "
						+ "here as \nred rectangles. \n\nHave fun!");
				r.next.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						ma = new MoveSquare2();
						main_panel.add(ma,"bloodstream");
						c.show(main_panel,"bloodstream");
					}
				});
				main_panel.add(r,"r");
				c.show(main_panel,"r");
			}
		}
	}
	class MoveSquare2 extends JPanel implements KeyListener{ //class move square- responsible for the blood stream game
		Rectangle[] obstacles = new Rectangle[5000];//all global variables
		Rectangle player;
		int startX = 0, velY = 300, currentRect = 0, direction = 0, rectsPassed;
		JLabel label;
		boolean done = false, moved = false, win = false, a = false, z = false;
		Timer setUp = new Timer(20, new ActionListener() {//first timer to set up the game
			public void actionPerformed(ActionEvent e) { //this will have a for loop that runs from 0-5000
				for (int x = 0; obstacles[x] == null && x <= 4998; x++) {
					int chance = (int) (Math.random() * 7 + 1); //loop will generate a random number from 1-7
					if(chance!=6&&chance!=7){
						obstacles[x] = new Rectangle(startX, (int)(Math.random()*700+0), 100, 25);
						startX +=160;
					}
					else{
						x--;
						startX += 160;
					}
				}
			}
		});

		Timer game = new Timer(7, new ActionListener() {//second timer game will run the game
			public void actionPerformed(ActionEvent e) {
				try{ //timer should have a bot to automatically move the mouse in order to have a smoother framerate
					Robot bot = new Robot();
					bot.mouseMove((int)MouseInfo.getPointerInfo().getLocation().getX(),(int)MouseInfo.getPointerInfo().getLocation().getY());
				}catch (Exception f){
					f.printStackTrace();
				}
				repaint();
			}
		});

		Timer movePlayer = new Timer(24, new ActionListener() { //third timer to move player
			public void actionPerformed(ActionEvent e) {//timer will check an integer for the direction, and use this to change the characters velocity
				if (direction == 1)
					velY += 6;
				else
					velY -= 6;
			}
		});
		public MoveSquare2() {//constructor
			addKeyListener(this);//add key listener
			repaint();
			setUp.start();//start the setup and game timers
			game.start();
			setFocusable(true);
			label = new JLabel(rectsPassed+"");
			add(label);
		}
			public void paintComponent(Graphics g) {//paintComponent
				setBackground(new Color(255,44,30));
				super.paintComponent(g);//super.paintComponent();
			g.setColor(new Color(232,16,56));
				g.fillRect(0,0,700,100);
				g.fillRect(0,600,700,100);
				requestFocus();
				if (win) {//if boolean done is true
					setUp.stop();
					game.stop();
					movePlayer.stop();
					InstructionalPanel r = new InstructionalPanel();
					r.title.setText("Fighting Back");
					r.info.setText("The body is now launching a full-force attack against you, the virus. Luckily, you were able to "
							+ "take control of another cell and its machinery. This new machinery allows you to fight back against "
							+ "the white blood cells.\n\n"
							+ "Instructions: Use the left and right arrow keys to move across the screen. Use spacebar to shoot"
							+ " your projectiles to destroy the white blood cells. Avoid their projectiles. \n\nHave fun! ");
					r.next.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							ba = new Battle();
							main_panel.add(ba,"ba");
							c.show(main_panel,"ba");
						}
					});
					main_panel.add(r,"r");
					c.show(main_panel,"r");
				}
				if(done){
					setUp.stop();
					game.stop();
					movePlayer.stop();
					LosePanel l = new LosePanel();
					main_panel.add(l,"l");
					c.show(main_panel, "l");
				}
				else {//else
					label.setText(rectsPassed+"");
					int playerY = 0;
					if(650-velY>650)
						velY = 600;
					else if(650-velY<0)
						velY = 0;
					player = new Rectangle(50, 650-velY, 20, 20);//create a player Rectangle object
					g.drawImage(Toolkit.getDefaultToolkit().getImage("virus.png"),player.x,player.y,player.width,player.height,this);
					g.setColor(Color.RED);
					for (int i = 0; i < obstacles.length && obstacles[i] != null; i++) {//create a for loop that loops through the rectangle array
						Rectangle r = obstacles[i];
						obstacles[i].x -= 5;
						r.x -= 5
						;
						if (r.x <= 800)
							g.drawImage(Toolkit.getDefaultToolkit().getImage("white.png"),r.x,r.y,r.width,r.height,this);
						if (obstacles[currentRect].x + 160 <= 0){//find what the current rectangle is
							currentRect++;
							if(moved){
								rectsPassed++;
							}
						}
						if (obstacles[currentRect].intersects(player)&&moved==true) {//check if the player intersects the current rectangle
							done = true;//if it does, end the game
						}
						if(difficulty_level==1&&rectsPassed==25){
							win = true;
						}
						else if(difficulty_level==2&&rectsPassed==50){
							win = true;
						}
						else if(difficulty_level==3&&rectsPassed==75){
							win = true;
						}
					}
				}
			}
			public void keyPressed(KeyEvent e) {//key pressed method
				movePlayer.start();
				if (e.getKeyCode() == KeyEvent.VK_UP){//check if it is the up key
					direction = 1;//if it is, change direction to 1
					moved = true;}
				else//else
					direction = 2;//direction is 2
				if(e.getKeyCode() == KeyEvent.VK_A)
					a = true;
				if(e.getKeyCode() == KeyEvent.VK_Z)
					z = true;
				if(a&&z)
					win = true;
			}

		public void keyReleased(KeyEvent e) {//key released method
			if (e.getKeyCode() == KeyEvent.VK_UP)//if it was the up key change direction to 2
				direction = 2;
		}

		public void keyTyped(KeyEvent e) {//other key methods
		}
	}
	class Battle extends JPanel implements KeyListener{//class Battle extends JPanel implements KeyListener
		Rectangle playerProjectiles[]= new Rectangle[300];//global variables
		ArrayList<WhiteBloodCell> whiteBloodCells = new ArrayList<WhiteBloodCell>();
		Rectangle BloodProjectiles[] = new Rectangle[1000];
		Rectangle playerRect = new Rectangle(430,600,50,50);
		int projectileIndex = 0, currentSpace = playerRect.x, oldSpace = playerRect.x, bloodSpace = 0, lives = 3;
		boolean createSquare = false, over = false;
		Timer runGame = new Timer(100, new ActionListener(){//first timer
			public void actionPerformed(ActionEvent e){
				//generates projectiles(rectangles)
				//this will have a for loop that runs from 0-5000
			for(WhiteBloodCell r:whiteBloodCells){
				if(r!=null){
					r.x+=6;
					if(r.x>=690){
						r.x=0;
						r.y+=25;
					}
				}
			}
			for(Rectangle r: playerProjectiles){
				if(r!=null)
					r.y-=10;
			}
			int y = 0;
			Iterator<WhiteBloodCell> go = whiteBloodCells.iterator();
			while(go.hasNext()){
				WhiteBloodCell r = (WhiteBloodCell)go.next();
				for(int i = 0;i<200;i++){
					if (r!=null&&playerProjectiles[i]!=null&&playerProjectiles[i].intersects(r)){
						playerProjectiles[i]=null;
						//if(go.hasNext()||whiteBloodCells.indexOf(r)==-1){
							go.remove();
							break;
						//} 
					}
					repaint();
				}
				y++;
				int chance = (int)(Math.random()*30+1);
				if(chance==1){
					BloodProjectiles[bloodSpace] = new Rectangle(r.x+15,r.y+25,10,10);
					bloodSpace++;
				}
			}
			int i = 0;
			for(Rectangle r: playerProjectiles){
				int z = 0;
				if(r!=null){
					for(Rectangle w: BloodProjectiles){
						if(w!=null){
							if(r.intersects(w)){
								playerProjectiles[i]=null;
								BloodProjectiles[z]=null;
							}
						}
						z++;
					}
				}
				i++;
			}
			for(Rectangle r: BloodProjectiles){
				if(r!=null){
					r.y+=5;
					if(r.intersects(playerRect)){
						lives--;
						if(lives == 0)
							over = true;
					}
				}
			}
			repaint();
		}});
		Battle(){//constructor
			//start all timers
			
			setFocusable(true);
			requestFocus();
			addKeyListener(this);
			requestFocus();
			int y = 0;
			int rectIndex = 0; //create white blood cells
			int numRects = (10*difficulty_level)+10;
			for(int x = 0;rectIndex<numRects&&y<200;x+=55){
				if(x > 475){
					y+=55;
					x = 0;
				}
				else{ 
					whiteBloodCells.add(rectIndex,new WhiteBloodCell(x,y,50,50,rectIndex));
					System.out.println(whiteBloodCells.get(rectIndex).id);
					rectIndex++;
				}
			}
			runGame.start();
		}
		
		public void keyPressed(KeyEvent e){ //change direction depending on key pressed
			currentSpace = playerRect.x;
			if(e.getKeyCode()==KeyEvent.VK_RIGHT){//when they press right or left, move virus in proper direction
				//when they press space, create a new rectangle object for player projectile
				//this rectangle will be at the players height
				playerRect.x+=10;
				if(playerRect.x>690)
					playerRect.x = 10;
			}
			else if(e.getKeyCode()==KeyEvent.VK_LEFT){
				playerRect.x-=10;
				if(playerRect.x<0)
					playerRect.x = 690;
			}
			else if(e.getKeyCode()==KeyEvent.VK_SPACE){ //create projectiles when they press space
				int currentSpace = playerRect.x;
				if(currentSpace!=oldSpace){
					if(projectileIndex<1000){
						playerProjectiles[projectileIndex] = new Rectangle(playerRect.x+25,playerRect.y-20,15,15);
						for(Rectangle r:playerProjectiles){
							if(playerProjectiles[projectileIndex]!=r&&r!=null&&playerProjectiles[projectileIndex]!=null&&playerProjectiles[projectileIndex].intersects(r))
								playerProjectiles[projectileIndex]=null;
						}
						if(playerProjectiles[projectileIndex]!=null)
							projectileIndex++;
					}
					oldSpace = currentSpace;
				}
			}//key listener
		}
		public void keyReleased(KeyEvent e){}
		public void keyTyped(KeyEvent e){}
		public void paintComponent(Graphics g){//paint component
			//draw all projectiles
			setBackground(Color.WHITE);
			setBackground(new Color(255,44,30));
			super.paintComponent(g);//super.paintComponent();
			g.setColor(new Color(232,16,56));
			g.fillRect(0,0,700,100);
			g.fillRect(0,600,700,100);
			requestFocus();
			if(over){
				runGame.stop();
				LosePanel l = new LosePanel();
				main_panel.add(l,"l");
				c.show(main_panel, "l");
			}
			else if(!whiteBloodCells.iterator().hasNext()){
				runGame.stop();
				InstructionalPanel l = new InstructionalPanel();
				l.title.setText("Infecting the Body");
				l.info.setText("Now that you have gotten through the bloodstream and fought off the white blood cells, it is time"
						+ " for you to infect the body. To do this, viruses inject their DNA into cells. They then "
						+ " take control of the cell, and force it to perform functions for the virus."
						+ "\n\nInstructions: When the light flashes green, press the button to move closer to the cell."
						+ " When the light is red, if you press the button, you will move backwards. Do this until you "
						+ "inject your DNA.\n\nHave fun!");
				l.next.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						b = new BreakIn();
						main_panel.add(b,"b");
						c.show(main_panel,"b");
					}
				});
				main_panel.add(l,"l");
				c.show(main_panel,"l");
				
			}
			else{
				for(WhiteBloodCell r:whiteBloodCells){
					if(r!=null)
						g.drawImage(Toolkit.getDefaultToolkit().getImage("white.png"),r.x,r.y,r.width,r.height,this);
				}
				g.drawImage(Toolkit.getDefaultToolkit().getImage("virus.png"),playerRect.x,playerRect.y,playerRect.width,playerRect.height,this);
				for(Rectangle r: playerProjectiles){
					if(r!=null){
						g.setColor(Color.CYAN);
						g.fillOval(r.x,r.y,r.width,r.height);
					}
				}
				for(Rectangle r: BloodProjectiles){
					if(r!=null){
						g.setColor(Color.BLACK);
						g.fillOval(r.x,r.y,r.width,r.height);
					}
				}
			}
		}
	}
	class BreakIn extends JPanel{//class to break into the cell wall
		//all global variables
		int wentDown = 0;
		boolean broken = false, pressed = false, inject = false, alternate = false, drawGreen = false, win = false;
		JButton moveDown;
		Rectangle playerRect = new Rectangle(25,400,50,50), DNA = new Rectangle(200,400,100,25);
		Timer runGame = new Timer(24,new ActionListener(){
			public void actionPerformed(ActionEvent e){
				repaint();
			} 
		});
		Timer injection = new Timer (100,new ActionListener(){
			public void actionPerformed(ActionEvent e){
				DNA.x += 10;
				if(DNA.x==300){
					win = true;
					repaint();
				}
			}
		});
		Timer light = new Timer (500, new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
				if(alternate){
					drawGreen = true;
					alternate = false;
				}
				else{
					drawGreen = false;
					alternate = true;
				}
				}catch(Exception g){}
			}
		});
		BreakIn(){//constructor
			//jpanel methods
			repaint();
			light.start();
			runGame.start();
			moveDown = new JButton("Move the Virus");
			add(moveDown);
			moveDown.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					if(drawGreen){
						pressed = true;
						boolean move = false;
						int moveAmount = 15/difficulty_level;
						playerRect.x+=moveAmount;
						repaint();
						repaint();
						move = true;
						
						wentDown++;//increment the counter for how much they moved down
						pressed = false;
						light.stop();
						alternate = false;
						drawGreen = false;
						light.start();
					}
					else{
						playerRect.x-=15;
					}
					if(playerRect.x+50>=150){
						inject = true;
						injection.start();
					}
					else if(playerRect.x<=0){
						injection.stop();
						light.stop();
						runGame.stop();
						LosePanel l = new LosePanel();
						main_panel.add(l,"l");
						c.show(main_panel, "l");
					}
				}
			});
		}
		public void paintComponent(Graphics g){//paint Component
			setBackground(new Color(255,44,30));
			super.paintComponent(g);//super.paintComponent();
			g.setColor(new Color(232,16,56));
			g.fillRect(0,0,700,100);
			g.fillRect(0,600,700,100);
			g.drawString("Press the button when the light turns green to move forward and inject your DNA",5,100);
			if(win){
				injection.stop();
				light.stop();
				runGame.stop();
				InstructionalPanel l = new InstructionalPanel();
				l.title.setBounds(new Rectangle(25,0,500,50));
				l.title.setText("Reproducing in the Cell");
				l.info.setText("Now that the virus has injected its DNA into the cell, it is time for the final step. "
						+ "Now, the virus can take control of the cell. After doing this, it uses machinery "
						+ "present in the cell to make copies of itself. These viruses then go to other cells "
						+ "and repeat the process."
						+ "\n\nInstructions: Press e to reproduce in easy mode, a and s for medium, and wasd for hard. When you have created enough replicas of yourself "
						+ "you will win and will have finished the game! \n\nHave fun!");
				l.next.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						r = new Reproduction();
						main_panel.add(r,"rep");
						c.show(main_panel,"rep");
					}
				});
				main_panel.add(l,"l");
				c.show(main_panel,"l");
			}
			else{
				g.setColor(Color.GREEN);
				g.drawImage(Toolkit.getDefaultToolkit().getImage("cell.png"),150,200,500,500,this);//draw a large Circle in a corner of the panel
				g.drawImage(Toolkit.getDefaultToolkit().getImage("virus.png"),playerRect.x,playerRect.y,playerRect.width,playerRect.height,this);//have the virus above this circle\
				//draw the virus according to coordinates from key released
				if(inject){
					injection.start();
					g.drawImage(Toolkit.getDefaultToolkit().getImage("DNA.png"),DNA.x,DNA.y,DNA.width,DNA.height,this);
				}
				if(drawGreen)
					g.setColor(Color.GREEN);
				else
					g.setColor(Color.BLACK);
				g.fillRect(0,0,100,100);
			}
		}
	}
	class Reproduction extends JPanel implements KeyListener, MouseListener{// panel class to have the reproduction game implements KeyListener
		//all global variables
		//includes a rectangle array with all of the children
		Rectangle[] children = new Rectangle[100];
		int timesPressed = 0, numOfChildren = 0;
		boolean win = false, firstKeyPressed = false, secondKeyPressed = false, thirdKeyPressed=false;;
		Timer moveKids = new Timer(200,new ActionListener(){//first timer
			//the timer changes the coordinates of all the rectangles in the array by a random amount
			public void actionPerformed(ActionEvent e){
				for(int i = 0;children[i]!=null;i++){
					children[i].x += (int)(Math.random()*25-13);
					children[i].y += (int)(Math.random()*25-13);
					repaint();
					for(Rectangle r: children){
						if (r!=null&&children[i]!=r&&children[i].intersects(r)){
							r.x += 25;
							r.y += 25;
						}
					}
				}
			}
		});
		Timer game = new Timer(24,new ActionListener(){//second timer to run the game
			//should repaint
			public void actionPerformed(ActionEvent e){
				repaint();
			}
		});
		public Reproduction(){//constructor
			//jpanel utility method
			//start game timer 
			game.start();
			moveKids.start();
			repaint();
			addKeyListener(this);
			addMouseListener(this);
			requestFocus();
			setFocusable(true);
		}
		public void keyPressed(KeyEvent e){//key pressed
			if(e.getKeyCode()==KeyEvent.VK_E&&difficulty_level==1)//check if the key isEEE
				timesPressed++;//if it is, increment the counter'
			else if(e.getKeyChar()=='r'&&difficulty_level==2)
				firstKeyPressed = true;
			else if(e.getKeyChar()=='r'&&difficulty_level==2&&firstKeyPressed)
				timesPressed++;
			else if(difficulty_level==3){
				if(e.getKeyCode()==KeyEvent.VK_W)
					firstKeyPressed = true;
				else if(e.getKeyCode()==KeyEvent.VK_A&&firstKeyPressed)
					secondKeyPressed = true;
				else if(e.getKeyCode()==KeyEvent.VK_S&&firstKeyPressed&&secondKeyPressed)
					thirdKeyPressed = true;
				else if(e.getKeyCode()==KeyEvent.VK_D&&firstKeyPressed&&secondKeyPressed&&thirdKeyPressed){
					timesPressed++;
					firstKeyPressed = false;
					secondKeyPressed = false;
					thirdKeyPressed = false;
				}
				else{
					firstKeyPressed = false;
					secondKeyPressed = false;
					thirdKeyPressed = false;
				}
			}
			if(timesPressed==3){//if the counter reaches 10, create a new rectangle object in the array and increment the children counter
				children[numOfChildren] = new Rectangle((int)(Math.random()*300+50),(int)(Math.random()*300+50),50,50);
				repaint();
				numOfChildren++;
				timesPressed = 0;
			}
			if (numOfChildren==10)//if the children reaches 10, win = true;	
				win = true;
		}
		public void keyReleased(KeyEvent e){}
		public void keyTyped(KeyEvent e){}
		public void mouseEntered(MouseEvent me){}
		public void mouseExited(MouseEvent me){}
		public void mousePressed(MouseEvent me){
			Reproduction.this.requestFocus();
		}
		public void mouseReleased(MouseEvent me){}
		public void mouseClicked(MouseEvent me){}	
		public void paintComponent(Graphics g){//paint component
			setBackground(new Color(255,44,30));
			super.paintComponent(g);//super.paintComponent();
			g.setColor(new Color(232,16,56));
			g.fillRect(0,0,700,100);
			g.fillRect(0,600,700,100);
			g.drawImage(Toolkit.getDefaultToolkit().getImage("cell.png"),0,0,700,700,this);//draw a large Circle in a corner of the panel
			for(int i = 0;children[i]!=null;i++){//loop through the array using a for loop and check if array[i]!=null
				g.drawImage(Toolkit.getDefaultToolkit().getImage("virus.png"),children[i].x,children[i].y,children[i].width,children[i].height,this);//if it isn't, draw a Rectangle using these coordinates and width and height
				if(win){//if win == true{
					super.paintComponent(g);
					WinPanel w = new WinPanel();
					main_panel.add(w,"w");
					c.show(main_panel,"w");
				}
			}
		}
	}
	public class InstructionalPanel extends JPanel{
		JLabel title = new JLabel(""); //title for each instructional panel
		JTextArea info = new JTextArea(""); //instructions
		GameButton next = new GameButton("Play","Ready to go?"); //play button
		public InstructionalPanel(){
			setLayout(new BorderLayout());
			title.setFont(new Font("Verdana",Font.BOLD,24));
			JPanel titleHolder = new JPanel();
			titleHolder.add(title); //adding and formatting all components
			add(titleHolder, BorderLayout.NORTH);
			info.setEditable(false);
			info.setLineWrap(true);
			info.setWrapStyleWord(true);
			info.setHighlighter(null);
			info.setBackground(new Color(255,44,30)); //set all colors
			titleHolder.setBackground(new Color(255,44,30));
			next.setBounds(new Rectangle(95,405,300,50));
			add(next,BorderLayout.SOUTH); //add them in proper layout
			add(info, BorderLayout.CENTER);
		}
	}
	public class WinPanel extends JPanel{
		GameButton retry = new GameButton("Play Again?","Play Again?"); //retry button
		JLabel win = new JLabel("YOU WON!");//replay button
		WinPanel(){
			setLayout(new FlowLayout(FlowLayout.CENTER,10000,25)); //properly format everything
			win.setFont(new Font("Verdana",Font.BOLD,36)); //set fonts
			win.setBackground(new Color(255,44,30)); //add all color
			add(win);//add components
			add(retry);
			retry.addActionListener(new ActionListener(){ //set it so that the user can easily replay if they want
				public void actionPerformed(ActionEvent e){
					Introduction i = new Introduction();
					main_panel.add(i,"i");
					c.show(main_panel, "i");
				}
			});
		}
		public void paintComponent(Graphics g){
			setBackground(new Color(255,44,30)); //set background color
			super.paintComponent(g);//super.paintComponent();
		}
	}
	public class LosePanel extends JPanel{
		GameButton retry = new GameButton("Retry","Play Again?");
		JLabel lost = new JLabel("YOU LOST!");
		JLabel retryLabel = new JLabel("Retry?");
		LosePanel(){
			setLayout(new FlowLayout(FlowLayout.CENTER,10000,25));
			lost.setFont(new Font("Verdana",Font.BOLD,36));
			lost.setBackground(new Color(255,44,30));
			add(lost);
			retryLabel.setFont(new Font("Verdana",Font.PLAIN,12));
			retryLabel.setBackground(new Color(255,44,30));
			add(retryLabel);
			add(retry);
			retry.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					Introduction i = new Introduction();
					main_panel.add(i,"i");
					c.show(main_panel, "i");
				}
			});
		}
		public void paintComponent(Graphics g){
			setBackground(new Color(255,44,30));
			super.paintComponent(g);//super.paintComponent();
		}
	}
}