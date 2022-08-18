import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


public class MeanFilterParallel
{

	public static void main(String[] args)
	{
		//check user input
		if (args.length!=3)
		{
			System.out.println("Usage: java MeanFilterSerial <input image> <output image> <filter size>");
			System.exit(1);
		}


		//capture user input
		String inputImgName = args[0];
		String outputImgName = args[1];
		int windowWidth = Integer.parseInt(args[2]);
		
		int a = windowWidth/2;

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
		
		//read pixel in the image and apply a windowWidth x windowWidth mean filter to it and save it to a new image
		BufferedImage outputImg = new BufferedImage(width, height, inputImg.getType());
		for (int i=1; i<width-1; i++)
		{
			for (int j=1; j<height-1; j++)
			{
				int red = 0;
				int green = 0;
				int blue = 0;
				
				for (int k=-a; k<a+1; k++)
				{
					for (int l=-a; l<a+1; l++)
					{
						if (i+k>=0 && i+k<width && j+l>=0 && j+l<height)
						{
							red += inputImg.getRGB(i+k, j+l)>>16 & 0xFF;
							green += inputImg.getRGB(i+k, j+l)>>8 & 0xFF;
							blue += inputImg.getRGB(i+k, j+l) & 0xFF;
						}
					}
				}
				red /= (windowWidth*windowWidth);
				green /= (windowWidth*windowWidth);
				blue /= (windowWidth*windowWidth);
				outputImg.setRGB(i, j, (red<<16) | (green<<8) | blue);
			}

		}
		
		//write output image
		try
		{
			ImageIO.write(outputImg, "png", new File("./data/"+outputImgName));
		}
		catch (IOException e)
		{
			System.out.println("Error: " + e.getMessage());
			System.exit(0);
		}

	}
}
