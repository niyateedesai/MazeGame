import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import java.awt.*;

public class LightSwitch{

    BufferedImage image;
    Location loc;
    int size;

    public LightSwitch(){
        try{
			this.image = ImageIO.read(new File("lightbulb.png"));
		}
		catch(IOException io)
		{
			System.err.println(io.toString());
        }
        this.loc=new Location(29, 2);
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