import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import java.awt.*;
public class Door{

    Location loc;
    int size;
    BufferedImage image;

    public Door(){
        try{
			this.image = ImageIO.read(new File("jail.png"));
		}
		catch(IOException io)
		{
			System.err.println(io.toString());
        }
        this.loc=new Location(6, 71);
        this.size = 5;
    }

    public Location getLoc(){
        return loc;
    }
    public Rectangle getRect()
	{
		return new Rectangle(50+loc.getCol()*size,50+loc.getRow()*size,size,size);
    }
    public BufferedImage getImage(){
        return image;
    }
}