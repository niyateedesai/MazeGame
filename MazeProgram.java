import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class MazeProgram extends JPanel implements KeyListener, MouseListener
{
	JFrame frame;
	Wall[] walls = new Wall[1442];

	ArrayList<Polygon> rightWalls = new ArrayList<Polygon>();
	ArrayList<Polygon> leftWalls = new ArrayList<Polygon>();
	//ArrayList<Polygon> ceilings = new ArrayList<Polygon>();
	//ArrayList<Polygon> floors = new ArrayList<Polygon>();

	Color[] colors = new Color[6];

	Polygon wallAhead = new Polygon();
	Explorer niyatee = new Explorer();
	Maze maze;
	Location end = new Location(30, 72);

	Key key = new Key();
	Door door = new Door();
	LightSwitch light = new LightSwitch();
	Portal portalA = new Portal(9, 28);
	Portal portalB = new Portal(8, 58);
	int distanceToKey = -1;
	int distanceToDoor = -1;
	int distanceToLight = -1;
	int distanceToPortalA = -1;
	int distanceToPortalB = -1;

	int moves = 0;

	boolean showMiniMap = false;
	boolean showDirections = true;

	boolean keyCollected = false;
	boolean keyInSight = false;
	boolean doorUnlocked = false;
	boolean doorInSight = false;
	boolean lightOn = false;
	boolean lightInSight = false;
	boolean prohibitMove = false;
	boolean portalAInSight = false;
	boolean portalBInSight = false;

	boolean gameOver = false;

	public MazeProgram()
	{
		setBoard();
		frame=new JFrame();
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(775,800);
		frame.setVisible(true);
		frame.addKeyListener(this);

		colors[0] = new Color(102,0,204);
		colors[1] = new Color(77,0,153);
		colors[2] = new Color(51,0,102);
		colors[3] = new Color(38,0,77);
		colors[4] = new Color(20,0,45);
		colors[5] = new Color(10,0,15);

		this.addMouseListener(this); //in case you need mouse clicking
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.setColor(new Color(0, 0, 0));	
		g.fillRect(0,0,775,800);

		int colorIndex = 0;

		setWalls();
		for(int i = 0; i < rightWalls.size(); i++){
			g.setColor(colors[colorIndex]);
			//g.fillPolygon(ceilings.get(i));
			if(rightWalls.get(i) != null){
				g.fillPolygon(rightWalls.get(i));
				g.setColor(Color.BLACK);
				g.drawPolygon(rightWalls.get(i));
			}
			g.setColor(colors[colorIndex]);
			if(leftWalls.get(i) != null){
				g.fillPolygon(leftWalls.get(i));
				g.setColor(Color.BLACK);
				g.drawPolygon(leftWalls.get(i));
			}
			g.setColor(colors[colorIndex]);

			colorIndex++;
		}

		g.fillPolygon(wallAhead);
		g.setColor(Color.BLACK);
		g.drawPolygon(wallAhead);

		if(keyCollected && !doorUnlocked){
			Image newImage = key.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT);
			g.drawImage(newImage, 600, 50, null);
		}
		if(keyInSight && !keyCollected){
			int width = 150-distanceToKey*30;
			Image newImage = key.getImage().getScaledInstance(width, width, Image.SCALE_DEFAULT);
			g.drawImage(newImage, 375-(int)(0.5*width), 400-(int)(0.5*width), null);
		}
		if(doorInSight && !doorUnlocked){
			int width = 550 - 100*distanceToDoor;
			int height = 600 - 100*distanceToDoor;
			Image newImage = door.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
			g.drawImage(newImage, 100+50*distanceToDoor, 100+50*distanceToDoor, null);
		}
		if(lightInSight && !lightOn){
			int width = 250-distanceToLight*30;
			Image newImage = light.getImage().getScaledInstance(width, width, Image.SCALE_DEFAULT);
			g.drawImage(newImage, 375-(int)(0.5*width), 400-(int)(0.5*width), null);
		}
		if(portalAInSight){
			int width = 400-distanceToPortalA*50;
			Image newImage = portalA.getImage().getScaledInstance(width, width, Image.SCALE_DEFAULT);
			g.drawImage(newImage, 375-(int)(0.5*width), 400-(int)(0.5*width), null);
		}
		if(portalBInSight){
			int width = 400-distanceToPortalB*50;
			Image newImage = portalB.getImage().getScaledInstance(width, width, Image.SCALE_DEFAULT);
			g.drawImage(newImage, 375-(int)(0.5*width), 400-(int)(0.5*width), null);
		}

		if(showMiniMap){
			g.setColor(Color.WHITE);
			g.drawRect(50, 50, 365, 165);
			for(Wall wall : walls){
				int rowNiyatee = niyatee.getLoc().getRow();
				int colNiyatee = niyatee.getLoc().getCol();
				int rowWall = wall.getLocation().getRow();
				int colWall = wall.getLocation().getCol();
				Rectangle rect = wall.getRect();

				if(!lightOn){
					if(rowNiyatee- rowWall > -10 && rowNiyatee- rowWall < 10 && colNiyatee-colWall>-10 && colNiyatee-colWall<10){
						g.drawRect((int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight());
					}
				}
				else{
					g.drawRect((int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight());
				}
			}
			g.setColor(Color.CYAN);
			Rectangle niyateeRectangle = niyatee.getRect();
			g.fillOval((int)niyateeRectangle.getX(), (int)niyateeRectangle.getY(), 5, 5);

			g.setColor(Color.ORANGE);
			Rectangle portalARectangle = portalA.getRect();
			g.fillOval((int)portalARectangle.getX(), (int)portalARectangle.getY(), 5, 5);
			Rectangle portalBRectangle = portalB.getRect();
			g.fillOval((int)portalBRectangle.getX(), (int)portalBRectangle.getY(), 5, 5);
				
			if(!keyCollected){
				g.setColor(Color.GREEN);
				Rectangle keyRectangle= key.getRect();
				g.fillOval((int)keyRectangle.getX(), (int)keyRectangle.getY(), 5, 5);
			}

			if(!doorUnlocked){
				g.setColor(Color.BLUE);
				Rectangle doorRectangle = door.getRect();
				g.fillOval((int)doorRectangle.getX(), (int)doorRectangle.getY(), 5, 5);
			}
			if(!lightOn){
				g.setColor(Color.YELLOW);
				Rectangle lightRectangle = light.getRect();
				g.fillOval((int)lightRectangle.getX(), (int)lightRectangle.getY(), 5, 5);
			}
		}

		if(showDirections){
			g.setFont(new Font("Times New Roman",Font.PLAIN,18));
			g.setColor(Color.WHITE);
			g.drawString("Press Spacebar to show map.", 272, 350);
			g.drawString("Press left and right to turn.", 280, 400);
			g.drawString("Press up to move forward.", 283, 450);
			showDirections = false;
		}
		
		if(niyatee.getLoc().getCol() == end.getCol() && niyatee.getLoc().getRow() == end.getRow()){
			g.setColor(Color.MAGENTA);
			g.setFont(new Font("Times New Roman",Font.BOLD|Font.ITALIC,40));
			g.drawString("Game Over", 283, 400);
			g.drawString("Moves: " + moves, 297, 450);
			gameOver = true;
		}
		else{
			g.setFont(new Font("Times New Roman",Font.PLAIN,18));
			g.setColor(Color.LIGHT_GRAY);
			g.drawString("Moves: " + moves, 600, 750);
		}
	}

	public void setBoard()
	{
		File name = new File("mazel.txt");
		try
		{
			BufferedReader input = new BufferedReader(new FileReader(name)); 
			String text;
			int index = 0;
			int c = 0;
			String[][] grid = new String[33][73];

			while( (text=input.readLine())!= null)
			{
				for(int r = 0; r<text.length(); r++){
					grid[c][r] = text.substring(r, r+1);
					if(grid[c][r].equals("*")){
						walls[index] = new Wall(c, r, 5);
						index++;
					}
				}
				c++;
			}
			maze = new Maze(grid);
		}
		catch (IOException io)
		{
			System.err.println(io.toString());
		}

		setWalls();
	}

	public void setWalls()
	{
		rightWalls.clear();
		leftWalls.clear();
		//ceilings.clear();
		//floors.clear();

		boolean hitWall = false;
		keyInSight = false;
		doorInSight = false;
		lightInSight = false;
		prohibitMove = false;
		portalAInSight = false;
		portalBInSight = false;

		for(int d = 0; d<5; d++){
			int[] xL={50+50*d,100+50*d,100+50*d,50+50*d};
			int[] yL={50+50*d,100+50*d,700-50*d,750-50*d};
			int[] xR={700-50*d,650-50*d,650-50*d,700-50*d};
			int[] yR={50+50*d,100+50*d,700-50*d,750-50*d};

			if(hitWall){
				break;
			}

			int row = niyatee.getLoc().getRow();
			int col = niyatee.getLoc().getCol();
		
			switch(niyatee.getDirection()){
				case 0:
				case 360:
					if(maze.isWallAt(row-d, col)){
						hitWall = true;
						break;
					}
					if(col-1 > 0){
						if(maze.isWallAt(row-d, col-1)){
							leftWalls.add(new Polygon(xL, yL, 4));
						}
						else{
							addLeftHallway(d);
						}
					}
					else{
						leftWalls.add(null);
					}
					if(col + 1 < 73){
						if(maze.isWallAt(row-d, col+1)){
							rightWalls.add(new Polygon(xR, yR, 4));
						}
						else{
							addRightHallway(d);
						}
					}
					else{
						rightWalls.add(null);
					}
					break;
				case 90:
				{
					if(col+d < 73){
						if(maze.isWallAt(row, col+d)){
							hitWall = true;
							break;
						}
						if(maze.isWallAt(row-1, col+d)){
							leftWalls.add(new Polygon(xL, yL, 4));
						}
						else{
							addLeftHallway(d);
						}
						if(maze.isWallAt(row+1, col+d)){
							rightWalls.add(new Polygon(xR, yR, 4));
						}
						else{
							addRightHallway(d);
						}
					}
					break;
				}
				case 180:
				{
					if(key.getLoc().getCol() == col && key.getLoc().getRow() == row){
						keyCollected = true;
					}
					if(key.getLoc().getCol() == col && key.getLoc().getRow() == row+d){
						keyInSight = true;
						distanceToKey = d;
					}
					if(door.getLoc().getCol() == col && door.getLoc().getRow() == row){
						if(keyCollected){
							doorUnlocked = true;
						}
						else{
							prohibitMove = true;
						}
					}
					if(door.getLoc().getCol() == col && door.getLoc().getRow() == row+d){
						doorInSight= true;
						distanceToDoor = d;
					}
					if(light.getLoc().getCol() == col && light.getLoc().getRow() == row){
						lightOn = true;
					}
					if(light.getLoc().getCol() == col && light.getLoc().getRow() == row+d){
						lightInSight = true;
						distanceToLight = d;
					}
					if(portalA.getLoc().getCol() == col && portalA.getLoc().getRow() == row){
						niyatee.transport(new Location(7, 58));
					}
					if(portalA.getLoc().getCol() == col && portalA.getLoc().getRow() == row+d){
						portalAInSight = true;
						distanceToPortalA = d;
					}
					if(portalB.getLoc().getCol() == col && portalB.getLoc().getRow() == row){
						niyatee.transport(new Location(8, 28));
					}
					if(portalB.getLoc().getCol() == col && portalB.getLoc().getRow() == row+d){
						portalBInSight = true;
						distanceToPortalB = d;
					}
					if(maze.isWallAt(row+d, col)){
						hitWall = true;
						break;
					}
					if(col + 1 < 73){
						if(maze.isWallAt(row+d, col+1)){
							leftWalls.add(new Polygon(xL, yL, 4));
						}
						else{
							addLeftHallway(d);
						}
					}
					else{
						leftWalls.add(null);
					}
					if(col-1 >= 0){
						if(maze.isWallAt(row+d, col-1)){
							rightWalls.add(new Polygon(xR, yR, 4));
						}
						else{
							addRightHallway(d);
						}
					}
					else{
						rightWalls.add(null);
					}
					break;
				}
				case 270:
				{
					if(col -d >= 0){
						if(maze.isWallAt(row, col-d) || col-d <= 0){
							hitWall = true;
							break;
						}
						if(maze.isWallAt(row+1, col-d)){
							leftWalls.add(new Polygon(xL, yL, 4));
						}
						else{
							addLeftHallway(d);
						}
						if(maze.isWallAt(row-1, col-d)){
							rightWalls.add(new Polygon(xR, yR, 4));
						}
						else{
							addRightHallway(d);
						}
					}
					break;
				}
			}
			
			addCeilings(d);
			
			if(hitWall){
				int[] x = {50+50*d,700-50*d,700-50*d,50+50*d};
				int[] y= {50+50*d,50+50*d,750-50*d,750-50*d};
				wallAhead = new Polygon(x, y, 4);
			}
			else{
				wallAhead = new Polygon();
			}
		}
	}
	public void addCeilings(int d){
		int[] xC={50+50*d,100+50*d,650-50*d,700-50*d};
		int[] yC={50+50*d,100+50*d,100+50*d,50+50*d};
		//ceilings.add(new Polygon(xC, yC, 4));
	}
	public void addRightHallway(int d){
		int[] x={700-50*d,650-50*d,650-50*d,700-50*d};
		int[] y={100+50*d,100+50*d,700-50*d,700-50*d};
		rightWalls.add(new Polygon(x, y, 4));
	}

	public void addLeftHallway(int d){
		int[] x={50+50*d,100+50*d,100+50*d,50+50*d};
		int[] y={100+50*d,100+50*d,700-50*d,700-50*d};
		leftWalls.add(new Polygon(x, y, 4));
	}

	public void keyPressed(KeyEvent e)
	{
		if(!gameOver){
			if(e.getKeyCode()==37){
				niyatee.turnLeft();
			}
			if(e.getKeyCode()==38){
				if(!maze.isWallAhead(niyatee) && !prohibitMove){
					niyatee.move();
					moves ++;
				}
			}
			if(e.getKeyCode()==39){
				niyatee.turnRight();
			}
			if(e.getKeyCode()==32){
				showMiniMap = !showMiniMap;
			}
			repaint();
		}
	}
	public void keyReleased(KeyEvent e)
	{
	}
	public void keyTyped(KeyEvent e)
	{
	}
	public void mouseClicked(MouseEvent e)
	{
	}
	public void mousePressed(MouseEvent e)
	{
		System.out.println(e.getPoint());
	}
	public void mouseReleased(MouseEvent e)
	{
	}
	public void mouseEntered(MouseEvent e)
	{
	}
	public void mouseExited(MouseEvent e)
	{
	}
	public static void main(String args[])
	{
		MazeProgram app=new MazeProgram();
	}
}
