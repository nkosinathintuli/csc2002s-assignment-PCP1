import java.io.File;
import java.io.IOException;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

class MeanFilter extends RecursiveTask<Integer>
{
	BufferedImage inputImg, outputImg;
	int startW, width, startH, height, windowWidth;

	static final int SEQUENTIAL_CUTOFF = 128;

	public MeanFilter(BufferedImage inputImg, BufferedImage outputImg, int startW, int width, int startH, int height, int windowWidth)
	{
		this.inputImg = inputImg;
		this.outputImg = outputImg;
		this.startW= startW;
		this.width = width;
		this.startH = startH;
		this.height = height;
		this.windowWidth = windowWidth;
	}
	// use the fork/join framework to parallelize the computation
	public Integer compute()
	{
		if (width * height <= SEQUENTIAL_CUTOFF)
		{
			// compute the mean filter
			for (int i = startW; i < startW + width; i++)
			{
				for (int j = startH; j < startH + height; j++)
				{
					int[] rgb = new int[3];
					int[] rgbSum = new int[3];
					int count = 0;
					for (int k = i - windowWidth / 2; k <= i + windowWidth / 2; k++)
					{
						for (int l = j - windowWidth / 2; l <= j + windowWidth / 2; l++)
						{
							if (k >= 0 && k < inputImg.getWidth() && l >= 0 && l < inputImg.getHeight())
							{
								rgb = inputImg.getRaster().getPixel(k, l, rgb);
								rgbSum[0] += rgb[0];
								rgbSum[1] += rgb[1];
								rgbSum[2] += rgb[2];
								count++;
							}
						}
					}
					rgbSum[0] /= count;
					rgbSum[1] /= count;
					rgbSum[2] /= count;
					outputImg.getRaster().setPixel(i, j, rgbSum);
				}
			}
			return 0;
		}
		else
		{
			// split the task into two subtasks
			MeanFilter subtask1 = new MeanFilter(inputImg, outputImg, startW, width / 2, startH, height, windowWidth);
			MeanFilter subtask2 = new MeanFilter(inputImg, outputImg, startW + width / 2, width - width / 2, startH, height, windowWidth);
			MeanFilter subtask3 = new MeanFilter(inputImg, outputImg, startW, width, startH + height / 2, height - height / 2, windowWidth);
			subtask1.fork();
			subtask2.fork();
			subtask3.compute();
			subtask1.join();
			subtask2.join();
			return 0;
		}
	}

}

public class MeanFilterParallel
{
	public static void main(String[] args) throws IOException
	{
		if (args.length != 3)
		{
			System.out.println("Usage: java MeanFilterParallel <input image> <output image> <window width>");
			return;
		}
		
		String inputImageName = args[0];
		String outputImageName = args[1];
		int windowWidth = Integer.parseInt(args[2]);

		//read input image
		BufferedImage inputImg = null;
		try {
			inputImg = ImageIO.read(new File("./data/"+inputImageName));
		} catch (IOException e) {
			System.out.println("Error reading input image.");
			return;
		}
		
		//get image width and height
		int width = inputImg.getWidth();
		int height = inputImg.getHeight();

		BufferedImage outputImg = new BufferedImage(width, height, inputImg.getType());
		
		ForkJoinPool pool = new ForkJoinPool();
		MeanFilter task = new MeanFilter(inputImg, outputImg, 0, width, 0, height, windowWidth);
		pool.invoke(task);
		try
		{
			ImageIO.write(outputImg, "png", new File("./data/"+outputImageName));
		}
		catch (IOException e)
		{
			System.out.println("Error: " + e.getMessage());
			System.exit(0);
		}
	}
}