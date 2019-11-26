import java.awt.*;
public class Wall
{
	Location loc;
	int size;
	public Wall(int r, int c, int size)
	{
		loc=new Location(r,c);
		this.size=size;
	}
	public Rectangle getRect()
	{
		return new Rectangle(50+loc.getCol()*size,50+loc.getRow()*size,size,size);
	}
	public Location getLocation(){
		return loc;
	}
}