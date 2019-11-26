public class Location
{
	private int r,c;
	public Location(int r, int c)
	{
		this.r=r;
		this.c=c;
	}
	public int getRow()
	{
		return r;
	}
	public int getCol()
	{
		return c;
	}
	public void moveEast(){
		this.c++;
	}
	public void moveWest(){
		this.c--;
	}
	public void moveNorth(){
		this.r--;
	}
	public void moveSouth(){
		this.r++;
	}
}