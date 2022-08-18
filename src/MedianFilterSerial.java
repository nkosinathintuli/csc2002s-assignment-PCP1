import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


public class MedianFilterSerial
{

	public static void main(String[] args)
	{
		//check user input
		if (args.length!=3)
		{
			System.out.println("Usage: java MedianFilterSerial <input image> <output image> <filter size>");
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

		// read the pixels in the image and apply a median filter to it and save it to a new image
		BufferedImage outputImg = new BufferedImage(width, height, inputImg.getType());
		for (int i=1; i<width-1; i++)
		{
			for (int j=1; j<height-1; j++)
			{
				int red = 0;
				int green = 0;
				int blue = 0;
				int[] redArray = new int[windowWidth*windowWidth];
				int[] greenArray = new int[windowWidth*windowWidth];
				int[] blueArray = new int[windowWidth*windowWidth];
				int count = 0;
				for (int k=-a; k<a+1; k++)
				{
					for (int l=-a; l<a+1; l++)
					{
						if (i+k>=0 && i+k<width && j+l>=0 && j+l<height)
						{
							redArray[count] = inputImg.getRGB(i+k, j+l)>>16 & 0xFF;
							greenArray[count] = inputImg.getRGB(i+k, j+l)>>8 & 0xFF;
							blueArray[count] = inputImg.getRGB(i+k, j+l) & 0xFF;
							count++;
						}
					}
				}
				Arrays.sort(redArray);
				Arrays.sort(greenArray);
				Arrays.sort(blueArray);
				red = redArray[redArray.length/2];
				green = greenArray[greenArray.length/2];
				blue = blueArray[blueArray.length/2];
				
				outputImg.setRGB(i, j, (red<<16) | (green<<8) | blue);
			}
		}

		//save the image
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
