import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import java.awt.*;

public class Key{

    BufferedImage image;
    Location loc;
    int size;

    public Key(){
        try{
			this.image = ImageIO.read(new File("key.png"));
		}
		catch(IOException io)
		{
			System.err.println(io.toString());
        }
        this.loc=new Location(19, 13);
        this.size = 5;

    }

    public BufferedImage getImage(){
        return image;
    }

    public Location getLoc(){
        return loc;
    }
    public Rectangle getRect()
	{
		return new Rectangle(50+loc.getCol()*size,50+loc.getRow()*size,size,size);
	}
}