import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import java.awt.*;

public class Portal{

    BufferedImage image;
    Location loc;
    int size;

    public Portal(int r, int c){
        try{
			this.image = ImageIO.read(new File("portal.png"));
		}
		catch(IOException io)
		{
			System.err.println(io.toString());
        }
        this.loc=new Location(r, c);
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