import java.awt.*;

public class Explorer
{
	Location loc;
	int size;
	int direction;
	public Explorer()
	{
		loc=new Location(2, 0);
		this.size=5;
		this.direction = 90;
	}
	public Rectangle getRect()
	{
		return new Rectangle(50+loc.getCol()*size,50+loc.getRow()*size,size,size);
	}
	public Location getLoc(){
		return loc;
	}
	public int getDirection(){
		return direction;
	}
	public void turnRight(){
		if(direction == 360){
			direction = 90;
		}
		else{
			direction +=90;
		}
	}
	public void turnLeft(){
		if(direction == 0){
			direction = 270;
		}
		else{
			direction -= 90;
		}
	}
	public void move(){
		if(direction == 90){
			loc.moveEast();
		}
		else if(direction == 180){
			loc.moveSouth();
		}
		else if(direction == 270){
			loc.moveWest();
		}
		else if(direction == 360 || direction == 0){
			loc.moveNorth();
		}
	}
	public void transport(Location loc){
		this.loc = loc;
	}


}