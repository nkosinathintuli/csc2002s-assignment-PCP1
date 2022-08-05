import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


public class MedianFilterSerial
{

	public static void main(String[] args)
	{
		//check user input
		if (args.length<3)
		{
			System.exit(0);
		}

		//declare variables

		//capture user input
		String inputImgName = args[0];
		String outputImgName = args[1];
		int windowWidth = Integer.parseInt(args[2]);

		//

		//Apply a mean filter function
	}
}
