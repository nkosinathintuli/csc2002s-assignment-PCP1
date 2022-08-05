import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


public class MeanFilterSerial
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


		//read input image
		BufferedImage inputImg = null;
		try
		{
			inputImg = ImageIO.read(new File("./data/"+inputImgName));
		}
		catch (IOException e)
		{
			System.out.println("Error: " + e.getMessage());
			System.exit(0);
		}

		//get image width and height
		int width = inputImg.getWidth();
		int height = inputImg.getHeight();

		//read every pixel in the image and apply a 3 by 3 mean filter to it and save it to a new image
		BufferedImage outputImg = new BufferedImage(width, height, inputImg.getType());
		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < height; j++)
			{
				int red = 0;
				int green = 0;
				int blue = 0;
				int count = 0;
				for (int k = -windowWidth; k <= windowWidth; k++)
				{
					for (int l = -windowWidth; l <= windowWidth; l++)
					{
						if (i+k >= 0 && i+k < width && j+l >= 0 && j+l < height)
						{
							red += inputImg.getRGB(i+k, j+l) >> 16 & 0xFF;
							green += inputImg.getRGB(i+k, j+l) >> 8 & 0xFF;
							blue += inputImg.getRGB(i+k, j+l) & 0xFF;
							count++;
						}
					}
				}
				red /= count;
				green /= count;
				blue /= count;
				outputImg.setRGB(i, j, (red << 16) | (green << 8) | blue);
			}
		} 
		//write output image
		try
		{
			ImageIO.write(outputImg, "png", new File(outputImgName));
		}
		catch (IOException e)
		{
			System.out.println("Error: " + e.getMessage());
			System.exit(0);
		}



		//Apply a mean filter function
	}
}
